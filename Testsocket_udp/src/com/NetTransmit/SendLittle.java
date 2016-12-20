package com.NetTransmit;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
/**
 *
 * @author Administrator
 */
public class SendLittle {
    //数据包类
    DatagramSocket socket;    
    //数据包基本信息
    String yourAddress="192.168.1.4"; 
    int yourPort=10000;
    int myPort=12000;
    /*用于分组的数据*/
    final int StartTip[]={12,0,4,8};                   //开始传输包(12)    标识(4)|次随机数(4)|报尾(4)
    final int StartAckTip[]={12,0,4,8};                //开始确认包(12)    标识(4)|次随机数(4)|报尾(4)
    final int StopTip[]={12,0,4,8};                    //结束传输包(12)    标识(4)|次随机数(4)|报尾(4)
    final int StopAckTip[]={12,0,4,8};                 //结束确认包(12)    标识(4)|次随机数(4)|报尾(4)
    final int HeadTip[]={18,0,4,8,12,14};              //分组头包(18)      标识(4)|次随机数(4)|块随机数(4)|分组数量(2)|报尾(4)
    final int HeadAckTip[]={16,0,4,8,12};              //分组头确认包(16)  标识(4)|次随机数(4)|块随机数(4)|报尾(4)
    final int DataTip[]={1020,0,4,8,12,14,16,1016};      //分组数据包(520) 标识(4)|次随机数(4)|块随机数(4)|序号(2)|长度(2)|数据(500)|报尾(4)
    final int FinishTip[]={20,0,4,8,12,16};            //发送完毕包(20)    标识(40|次随机数(4)|块随机数(4)|小随机数(4)|报尾(4)
    final int ReqAgainTip[]={522,0,4,8,12,16,18,518};   //请求重发包(522)  标识(4)|次随机数(4)|块随机数(4)|小随机数(4)|长度(2)|数据(500)|报尾(4)
    /*标识*/
    final String datagramHead="[]01";
    final String datagramData="[]02";
    final String datagramHeadAck="[]03";
    final String datagramReqAgain="[]04";
    final String datagramFinish="[]05";    
    final String datagramStart="[]08";
    final String datagramStartAck="[]0A";
    final String datagramStop="[]06";
    final String datagramStopAck="[]07";
    final String datagramTail="end|";
    /*数据长度的规定*/
    final int dataLength=1000;
    final int groupNum=250;
    final int littleLength=1024*1024;
    //界面
    JFrame frame;
    JPanel contentPane;
    JButton bun;
    public SendLittle(String addr,int mp,int yp){
        try {
            Date begin;
            Date end;
            yourAddress=new String(addr);
            yourPort=yp;
            myPort=mp;
            socket = new DatagramSocket(myPort);
            socket.setReceiveBufferSize(1024*1024*5);
        } catch (SocketException ex) {
            Logger.getLogger(SendLittle.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    /*拆分分组,并发送*/
    public void sendAll(byte littleBuffer[],int littleLength){
        byte buffer[]=new byte[dataLength*groupNum];
        int numberRandom=(new Random()).nextInt();
        sendStart(numberRandom);
        int bigNumber=littleLength/(dataLength*groupNum)+(littleLength%(dataLength*groupNum)==0?0:1);
        for(int i=0;i<bigNumber;i++){
            //填充buffer
            int length;
            if(i==bigNumber-1){
                length=littleLength-i*(dataLength*groupNum);
            }else{
                length=dataLength*groupNum;
            }
            for(int j=0;j<length;j++){
                buffer[j]=littleBuffer[j+i*dataLength*groupNum];
            }
            sendGroup(numberRandom,buffer,length);
        }
        sendOver(numberRandom);
    }
    /*发送开始传输包*/
    public void sendStart(int numberRandom){
        DatagramPacket sendPacket=null,receivePacket=null;
        byte sendBuffer[]=new byte[StartTip[0]];
        byte receiveBuffer[]=new byte[StartAckTip[0]];
        //填充开始传输包
        ByteChange.StringToByte(datagramStart, sendBuffer, StartTip[1], 4);
        ByteChange.intToByte(sendBuffer, StartTip[2], numberRandom);
        ByteChange.StringToByte(datagramTail, sendBuffer, StartTip[3], 4);
        //设置超时1ms及初始化数据包
        try{
            socket.setSoTimeout(1);
            sendPacket=new DatagramPacket(sendBuffer,sendBuffer.length,InetAddress.getByName(yourAddress),yourPort);
            receivePacket=new DatagramPacket(receiveBuffer,receiveBuffer.length);
        }catch(Exception e){}
        boolean canSend=true;
        while(true){
            try{
                if(canSend){
                    socket.send(sendPacket);
                }
                socket.receive(receivePacket);
                //如果接收到数据包,无论如何,下一次都不能发送
                canSend=false;
                //判断标识
                String str=new String(receiveBuffer,StartAckTip[1],4);
                if(!str.equals(datagramStartAck)){
                    continue;
                }
                //判断次随机数
                int num=ByteChange.byteToInt(receiveBuffer, StartAckTip[2]);
                if(num!=numberRandom){
                    continue;
                }
                //否则接收成功,退出循环
                break;
            }catch(Exception e){
                if(e instanceof SocketTimeoutException){
                    canSend=true;
                }
            }
        }
    }
    /*发送一组数据包*/
    public void sendGroup(int numberRandom,byte buffer[],int length){
        assert length<=dataLength*groupNum:"Error:数据量大于最大分组容量!";
        //生成随机数
        int blockRandom=(new Random()).nextInt();
        //计算分组数量
        int number=length/dataLength+(length%dataLength==0?0:1);
        //发送分组头
        sendDatagramHead(numberRandom,blockRandom,number);
        //System.out.println("发送分组:"+number);
        //发送分组体
        /*
         * 发送方式是
         * 1:全部连续发送
         * 2.发送发送完毕包
         * 3:接收请求重发包.如果接收超时,执行2
         * 4:如果请求重发包中有数据,则重发相应的包,然后执行2.否则执行5
         * 5:发送成功,返回
         */
        //先将buffer拆分成number个分组,记录每个分组中buffer中的起始下标和数据长度
        int startTip[]=new int[number];
        int lengthTip[]=new int[number];
        boolean sendTip[]=new boolean[number];
        //一开始,每组数据都未传输成功
        for(int i=0;i<number;i++){
            sendTip[i]=false;
        }
        if(number==1){
            //如果只有一组的情况,比较特殊.为提高效率,单独考虑
            startTip[0]=0;
            lengthTip[0]=length;
        }else{
            //分组超过一组
            for(int i=0;i<number;i++){
                if(i==0){
                    startTip[i]=0;
                }else{
                    //很明显,只有最后一组数据长度才可能不为dataLength
                    //所以,每一组前面的分组长度肯定都是dataLength
                    startTip[i]=dataLength*i;
                }
                if(i!=number-1){
                    lengthTip[i]=dataLength;
                }else{
                    lengthTip[i]=length-dataLength*i;
                }
            }
        }
        //下面开始传送
        //设定产生小随机数的机器
        //用于生成小随机数
        MyRandom myRandom=new MyRandom(1);
        //设置random1,用于防止接收到同一个请求从发包而重复发送
        //发送端保证,绝不会发值为0的小随机数
        while(true){
            //按顺序发送尚未成功发送的数据包
            for(int i=0;i<number;i++){
                if(sendTip[i]==false){
                    sendDatagramData(numberRandom,blockRandom,i,buffer,startTip[i],lengthTip[i]);
                }
            }
            //接收请求重发包(超时5ms),内含发送"发送完毕包"
            //在getDatagramReq中,已经修改了sendTip的值
            boolean flag=getDatagramReq(numberRandom,blockRandom,myRandom.getNextRandom(),sendTip);
            if(flag){
                break;
            }
        }
    }    
    /*发送分组头*/
    public void sendDatagramHead(int numberRandom,int blockRandom,int number){
        byte buffer[]=new byte[HeadTip[0]];
        DatagramPacket packet=null;
        //填充分组头标识
        ByteChange.StringToByte(datagramHead, buffer, HeadTip[1], 4);
        //填充次随机数
        ByteChange.intToByte(buffer, HeadTip[2], numberRandom);
        //填充块随机数
        ByteChange.intToByte(buffer, HeadTip[3], blockRandom);
        //写入数组数量
        assert number<=groupNum:"分组数量超过500";
        ByteChange.shortToByte(buffer,HeadTip[4],(short)number);
        //填充报尾标识
        ByteChange.StringToByte(datagramTail, buffer, HeadTip[5], 4);
        //发送分组头
        try {
             //先设置接收超时时间
            socket.setSoTimeout(1);
            packet=new DatagramPacket(buffer,buffer.length,InetAddress.getByName(yourAddress),yourPort);
        } catch (Exception ex) {
            Logger.getLogger(SendLittle.class.getName()).log(Level.SEVERE, null, ex);
        }
        //发送后接收报头确认报,接收超时或者接收的不匹配则重发分组头
        boolean canSendHead=true;
        while(true){
            try {
                if(canSendHead){
                    socket.send(packet);
                }
                byte c[]=new byte[HeadAckTip[0]];
                DatagramPacket dp=new DatagramPacket(c,c.length);
                ByteChange.cleanByte(c);
                socket.receive(dp);
                //只要接收到数据包,不论是不正确,下次循环就不能发送头
                canSendHead=false;
                //进行检查
                 //1.检查其是否是分组头确认包
                String s=new String(c,HeadAckTip[1],4);
                if(!s.equals(datagramHeadAck)){
                    continue;
                }
                //2.检查其次随机数,是否与自己的匹配
                int r=ByteChange.byteToInt(c, HeadAckTip[2]);
                if(r!=numberRandom){
                    continue;
                }
                //3.检查其块随机数
                r=ByteChange.byteToInt(c, HeadAckTip[3]);
                if(r!=blockRandom){
                    continue;
                }
                //所有检查均正确,发送成功,返回
                break;
            } catch (Exception ex) {
                //Logger.getLogger(Send.class.getName()).log(Level.SEVERE, null, ex);
                //如果是接收超时,则一下次循环的时候,要先发送头
                if(ex instanceof SocketTimeoutException){
                    canSendHead=true;
                }
            }
        }
    }
    /*发送分组包,无回送*/
    public void sendDatagramData(int numberRandom,int blockRandom,int serial,byte buffer[],int start,int length){
        DatagramPacket packet=null;
        byte b[]=new byte[DataTip[0]];
        //System.out.println("发送分组,序列号:"+serial);
        assert length<=dataLength:"Error[发送分组包]:数据长度大于dataLength!";
        //填充分组包标识
        ByteChange.StringToByte(datagramData, b, DataTip[1], 4);
        //填充次随机数
        ByteChange.intToByte(b, DataTip[2], numberRandom);
        //填充块随机数
        ByteChange.intToByte(b, DataTip[3], blockRandom);
        //填充分组序号
        ByteChange.shortToByte(b, DataTip[4], (short)serial);
        //填充数据长度
        ByteChange.shortToByte(b, DataTip[5], (short)length);
        //填充等传输的数据
        for(int i=0;i<length;i++){
            b[i+DataTip[6]]=buffer[i+start];
        }
        //填充报尾
        ByteChange.StringToByte(datagramTail, b, DataTip[7], 4);
        //填充完毕,发送
        try {
            packet = new DatagramPacket(b, b.length, InetAddress.getByName(yourAddress), yourPort);
            socket.send(packet);
        } catch (Exception ex) {
            Logger.getLogger(SendLittle.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /*接收请求重发包*/
    /*返回接收方是否接收完毕*/
    public boolean getDatagramReq(int numberRandom,int blockRandom,int littleRandom,boolean sendTip[]){
        //首先,设置接收超时5ms
        try {
            socket.setSoTimeout(5);
        } catch (SocketException ex) {
            Logger.getLogger(SendLittle.class.getName()).log(Level.SEVERE, null, ex);
        }
        DatagramPacket packetReq=null;
        //由于请求重发包发送的是需要重发的分组
        //所以,先将成功发送标记全部置true,再根据
        for(int i=0;i<sendTip.length;i++){
            sendTip[i]=true;
        }
        //请求重发包将发送失败的置false
        byte buffer[]=new byte[ReqAgainTip[0]];
        boolean canSendFinish=true;
        while(true){
            try {
                if(canSendFinish){
                    sendFinish(numberRandom,blockRandom,littleRandom);
                }
                packetReq = new DatagramPacket(buffer, buffer.length);
                ByteChange.cleanByte(buffer);
                socket.receive(packetReq);
                //如果接收到数据包,不管是否正确,下一轮都不能发未接收请求重发包
                canSendFinish=false;
                //对接收到的请法求重发包进行解析
                //检证是不是请求重发包
                String s=new String(buffer,ReqAgainTip[1],4);
                if(!s.equals(datagramReqAgain)){
                    continue;
                }
                //匹配次随机数
                int r=ByteChange.byteToInt(buffer, ReqAgainTip[2]);
                if(r!=numberRandom){
                    continue;
                }
                //匹配块随机数
                r=ByteChange.byteToInt(buffer, ReqAgainTip[3]);
                if(r!=blockRandom){
                    continue;
                }       
//                //验证该包是否完整
//                s=new String(buffer,1014,4);
//                if(!s.equals(datagramTail)){
//                    continue;
//                }
                //验证此请求重发包的小随机数是否与本次发送的"发送完毕包"相同
                r=ByteChange.byteToInt(buffer, ReqAgainTip[4]);
                if(r!=littleRandom){
                    continue;
                }
                //验证完毕,提取请求重发的分组
                //提取数据长度
                int length=ByteChange.byteToShort(buffer, ReqAgainTip[5]);
                //如果数据长度是0,验证一下数据完整性,如果完整,则返回数据接收完毕
                if(length==0){
                    s=new String(buffer,ReqAgainTip[5]+2,4);
                    if(s.equals(datagramTail)){
                        //数据完整,返回
                        return true;
                    }else{
                        //数据不完整,此包无效,继续循环
                        continue;
                    }
                }else{
                    for(int i=0;i<length;i++){
                        int tip=ByteChange.byteToShort(buffer, i*2+ReqAgainTip[6]);
                        sendTip[tip]=false;
                    }
                    break;
                }
            } catch (Exception ex) {
                //Logger.getLogger(Send.class.getName()).log(Level.SEVERE, null, ex);
                //如果是接收超时,那么下一次循环就应该发送"发送完毕包"
                if(ex instanceof SocketTimeoutException){
                    canSendFinish=true;
                    //System.out.println("接收请求重发包超时");
                }
            }
        }
        return false;
    }
    /*发送发送完毕包*/
    public void sendFinish(int numberRandom,int blockRandom,int littleRandom){
        byte buffer[]=new byte[FinishTip[0]];
        //填充发送完毕包标识
        ByteChange.StringToByte(datagramFinish, buffer, FinishTip[1], 4);
        //填充次随机数
        ByteChange.intToByte(buffer, FinishTip[2], numberRandom);
        //填充块随机数
        ByteChange.intToByte(buffer, FinishTip[3], blockRandom);
        //填充小随机数
        ByteChange.intToByte(buffer, FinishTip[4], littleRandom);
        //填充报尾
        ByteChange.StringToByte(datagramTail, buffer, FinishTip[5], 4);
        try {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(yourAddress), yourPort);
            socket.send(packet);
        } catch (Exception ex) {
            Logger.getLogger(SendLittle.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /*发送结束传输包*/
    public void sendOver(int numberRandom){
        //设定接收超时1ms
        try {
            socket.setSoTimeout(1);
        } catch (SocketException ex) {
            Logger.getLogger(SendLittle.class.getName()).log(Level.SEVERE, null, ex);
        }
        //生成传输结束包
        DatagramPacket packetOver,packetOverAck;
        byte overBuffer[]=new byte[StopTip[0]];
        byte overAckBuffer[]=new byte[StopAckTip[0]];
        //填充标识
        ByteChange.StringToByte(datagramStop, overBuffer, StopTip[1], datagramStop.length());
        //填充次随机数
        ByteChange.intToByte(overBuffer, StopTip[2], numberRandom);
        //填充报尾
        ByteChange.StringToByte(datagramTail, overBuffer, StopTip[3], datagramTail.length());
        try {
            packetOver = new DatagramPacket(overBuffer, overBuffer.length, InetAddress.getByName(yourAddress), yourPort);
            packetOverAck =new DatagramPacket(overAckBuffer,overAckBuffer.length);
            while(true){
                try {
                    socket.send(packetOver);
                    ByteChange.cleanByte(overAckBuffer);
                    socket.receive(packetOverAck);
                    //检查ACK标识
                    String str=new String(overAckBuffer,StopAckTip[1],4);
                    if(!str.equals(datagramStopAck)){
                        continue;
                    }
                    //检查次随机数
                    int num=ByteChange.byteToInt(overAckBuffer, StopAckTip[2]);
                    if(num!=numberRandom){
                        continue;
                    }
                    break;
                } catch (Exception ex) {
                    if(ex instanceof SocketTimeoutException){
                        //接收超时,则继续循环
                    }
                }
            }
        } catch (UnknownHostException ex) {
            Logger.getLogger(SendLittle.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}


