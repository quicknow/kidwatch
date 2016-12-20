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
public class ReceiveLittle {
    //数据包类
    DatagramSocket socket;
    //DatagramPacket packet;
    //数据包基本信息
    String yourAddress="192.168.1.100";  //119.112.6.203 //180.109.158.89
    int yourPort=12000;
    int myPort=10000;
    /*用于分组的数据*/
    final int StartTip[]={12,0,4,8};                   //开始传输包(12)    标识(4)|次随机数(4)|报尾(4)
    final int StartAckTip[]={12,0,4,8};                //开始确认包(12)    标识(4)|次随机数(4)|报尾(4)
    final int StopTip[]={12,0,4,8};                    //结束传输包(12)    标识(4)|次随机数(4)|报尾(4)
    final int StopAckTip[]={12,0,4,8};                 //结束确认包(12)    标识(4)|次随机数(4)|报尾(4)
    final int HeadTip[]={18,0,4,8,12,14};              //分组头包(18)      标识(4)|次随机数(4)|块随机数(4)|分组数量(2)|报尾(4)
    final int HeadAckTip[]={16,0,4,8,12};              //分组头确认包(16)  标识(4)|次随机数(4)|块随机数(4)|报尾(4)
    final int DataTip[]={1020,0,4,8,12,14,16,1016};      //分组数据包(520)   标识(4)|次随机数(4)|块随机数(4)|序号(2)|长度(2)|数据(500)|报尾(4)
    final int FinishTip[]={20,0,4,8,12,16};            //发送完毕包(20)    标识(40|次随机数(4)|块随机数(4)|小随机数(4)|报尾(4)
    final int ReqAgainTip[]={522,0,4,8,12,16,18,518};   //请求重发包(522)   标识(4)|次随机数(4)|块随机数(4)|小随机数(4)|长度(2)|数据(500)|报尾(4)
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
    //用于连续存储收到的大,小随机数
    CircleStack randomStack=null,littleRandomStack=null;
    //用于存放上一次发送的请求重发包
    DatagramPacket lastReqAgain=null;
    public ReceiveLittle(String addr,int mp,int yp){
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
    /*分组接收*/
    public int receiveAll(byte littleBuffer[]){
        byte buffer[]=new byte[dataLength*groupNum];
        randomStack=new CircleStack(100);
        int numberRandom=receiveStart();
        int allLength=0;
        while(true){
            int length=receiveGroup(numberRandom,buffer);
            if(length!=-1){
                for(int i=0;i<length;i++){
                    littleBuffer[i+allLength]=buffer[i];
                }
                allLength+=length;
            }else{
                sendOverAck(numberRandom);
                return allLength;
            }
        }
    }
    /*接收开始传输包*/
    public int receiveStart(){
        DatagramPacket sendPacket=null,receivePacket=null;
        //用于发送开始确认包
        byte sendBuffer[]=new byte[StartAckTip[0]];
        //用于接收开始传输包
        byte receiveBuffer[]=new byte[StartTip[0]];
        try{
            socket.setSoTimeout(0);
            sendPacket=new DatagramPacket(sendBuffer,sendBuffer.length,InetAddress.getByName(yourAddress),yourPort);
            receivePacket=new DatagramPacket(receiveBuffer,receiveBuffer.length);
        }catch(Exception e){}
        while(true){
            try {
                socket.receive(receivePacket);
                //检查标识
                String str=new String(receiveBuffer,StartTip[1],4);
                if(!str.equals(datagramStart)){
                    continue;
                }
                //获取次随机数
                int numberRandom=ByteChange.byteToInt(receiveBuffer, StartTip[2]);
                sendStartAck(numberRandom);
                return numberRandom;
            } catch (IOException ex) {
                Logger.getLogger(ReceiveLittle.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    /*发送开始确认包*/
    public void sendStartAck(int numberRandom){
        DatagramPacket sendPacket=null;
        byte sendBuffer[]=new byte[StartAckTip[0]];
        //填充标识
        ByteChange.StringToByte(datagramStartAck, sendBuffer, StartAckTip[1], 4);
        //填充次随机数
        ByteChange.intToByte(sendBuffer, StartAckTip[2], numberRandom);
        //填充报尾
        ByteChange.StringToByte(datagramTail, sendBuffer, StartAckTip[3], 4);
        try {
            sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, InetAddress.getByName(yourAddress), yourPort);
            socket.send(sendPacket);
        } catch (Exception ex) {
            Logger.getLogger(ReceiveLittle.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    /*接收一次分组*/
    //返回值为该数组中数据的长度.并且,此函数进行传送结束的检查.发现结束,返回-1
    public int receiveGroup(int numberRandom,byte buffer[]){
        //接收分组头
        rdh_return rdh=receiveDatagramHead(numberRandom);
        //System.out.println("收到分组头,随机数:"+rdh.random+",分组数量:"+rdh.number);
        //记下随机数及分组数量
        int random=rdh.random;
        int number=rdh.number;
        if(number==0){
            return -1;
        }
        randomStack.add(random);
        littleRandomStack=new CircleStack(100);
        //设定数据总长度
        int lengthAll=0;
        //由于只有最后一组的数据长度不是dataLength,所以,先计算前number-1个分组的总长度
        lengthAll=dataLength*(number-1);
        //用于记录成功接收了哪些分组
        boolean receiveTip[]=new boolean[number];
        for(int i=0;i<number;i++){
            receiveTip[i]=false;
        }
        //预先计算各组应存入buffer的起始下标
        //由于除了最后一组,其它组的数据长度一定是dataLength.所以计算起始下标非常简单
        int startTip[]=new int[number];
        for(int i=0;i<number;i++){
            startTip[i]=i*dataLength;
        }
        //进入大循环,即连续去接收分组,直到所有分组完毕
        while(true){
            int result=getDatagramData(numberRandom,random,buffer,receiveTip,startTip);
            //如果接收到的是一个分组.要记录其长度.由于,除了最后一组,其他的长度一定是dataLength
            //所以,如果这个length不是dataLength,说明是最后一组的长度
            if(result>0){
                if(result!=dataLength){
                    lengthAll+=result;
                }
                continue;
            }
            //如果接收到是分组头
            else if(result==-2){
                //发送分组头确认包,然后循环
                sendDatagramHeadAck(numberRandom,random);
                continue;
            }
            //如果是新的发送完毕包
            else if(result==-3){
                //发送请求重发包
                //在发送请求重发包的时候,是要检查是否全部成功接收的,所以,这里不再重新检查,而是使用它检查的结果
                boolean flag=sendDatagramReqAgain(numberRandom,random,littleRandomStack.getLast(),receiveTip);
                //System.out.println("发送请求重发包");
                if(flag){
                    break;
                }
                continue; 
            }
            //如果是上次的发送完毕包
            else if(result==-4){
                //如果上次发送的不是0请求重发包,则重发
                //非0请求重发包的长度,一定是1018
                //System.out.println("接收到上次的发送完毕包:");
                if(lastReqAgain.getLength()==ReqAgainTip[0]){
                    try {
                        //System.out.println("重发非0请求重发包");
                        socket.send(lastReqAgain);
                    } catch (Exception ex) {
                        Logger.getLogger(ReceiveLittle.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                continue;
            }
        }
        //这里有个很蛋疼的问题.由于不确定最后一组是否完整,只有在接收到最后一个分组时才知道其数据长度
        //而接收分组是在单独的函数中完成,函数返回的是接收的数据长度.在循环中,如果发现数据长度不是dataLength
        //就认为它是最后一组,并将其长度加入lengthAll.但是,有一个严重的问题,因为,每个块,只有最后一个块的最后一个
        //分组的数据长度可能不是dataLength.所以,前面的块,在进行计算的时候,有会忽略它最后一组的长度.导致以后的块
        //连续错误
        if(lengthAll==(number-1)*dataLength){
            lengthAll+=dataLength;
        }
        return lengthAll;
    }
    /*接收分组头*/
    //1.不接收超时. 2.接收成功,会调用发送分组头确认包
    public rdh_return receiveDatagramHead(int numberRandom){
        byte buffer[]=new byte[HeadTip[0]];
        DatagramPacket packet=null;
        rdh_return rdh=new rdh_return();
        //设置不接收超时
        try {
            socket.setSoTimeout(0);
        } catch (SocketException ex) {
            Logger.getLogger(ReceiveLittle.class.getName()).log(Level.SEVERE, null, ex);
        }
        while(true){
            try {
                packet=new DatagramPacket(buffer,buffer.length);//,InetAddress.getByName(yourAddress),yourPort
                ByteChange.cleanByte(buffer);
                socket.receive(packet);
                //检查分组头标识
                String str=new String(buffer,HeadTip[1],4);
                if(!str.equals(datagramHead)){
                    /*既然不是分组头,重新接收是肯定的.只不过,有些情况,需要处理一下再重新接收*/
                    //如果是开始传输包
                    if(str.equals(datagramStart)){
                        int num=ByteChange.byteToInt(buffer, StartTip[2]);
                        if(num==numberRandom){
                            sendStartAck(numberRandom);
                            continue;
                        }
                    }
                    //如果是发送完毕包
                    if(str.equals(datagramFinish)){
                        //先检其次随机数
                        if( ByteChange.byteToInt(buffer, FinishTip[2])==numberRandom ){
                            //检验是不是上一次的发送完毕包
                            //如果是的,说明上次发的0请求重发包没发成功
                            //先检查其块随机数是不是上一次的
                            if( randomStack.getLast()!=-1 && ByteChange.byteToInt(buffer, FinishTip[3])==randomStack.getLast() ){
                                //再检查其小随机数,是不是上次的(即最后一次发过来的)
                                if(littleRandomStack!=null){
                                    //如果littelRandomStack是null,说明这是第一次接收分组头,当然不可能要重发
                                    if(littleRandomStack.getLast()==ByteChange.byteToInt(buffer, FinishTip[4])){
                                        //如果小随机数也匹配,则发送上次的0请求重发包
                                        if(lastReqAgain!=null){
                                            socket.send(lastReqAgain);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    //如果是传输结束包,则将0分组数量填充入rdh,然后返回
                    if(str.equals(datagramStop)){
                        rdh.number=0;
                        return rdh;
                    }
                    continue;
                }
                //检查该包是否完整
                str=new String(buffer,HeadTip[5],4);
                if(!str.equals(datagramTail)){
                    continue;
                }
                //检查完毕,获取随机数
                rdh.random=ByteChange.byteToInt(buffer, HeadTip[3]);
                //获取分组数量
                rdh.number=ByteChange.byteToShort(buffer, HeadTip[4]);
                sendDatagramHeadAck(numberRandom,rdh.random);
                break;
            } catch (Exception ex) {
                Logger.getLogger(ReceiveLittle.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return rdh;
    }
    //在此,由于需要返回两个信息: 1.分组的随机数 2.分组的数量 所以需要定义一个内部类
    class rdh_return{
        int random;
        int number;
    }
    /*发送分组头确认包*/
    public void sendDatagramHeadAck(int numberRandom,int blockRandom){
        //System.out.println("发送分组头确认包");
        byte buffer[]=new byte[HeadAckTip[0]];
        DatagramPacket packet=null;
        //填充分组头确认包标识
        ByteChange.StringToByte(datagramHeadAck, buffer, HeadAckTip[1], 4);
        //填充次随机数
        ByteChange.intToByte(buffer, HeadAckTip[2], numberRandom);
        //填充块随机数
        ByteChange.intToByte(buffer, HeadAckTip[3], blockRandom);
        //填充报尾
        ByteChange.StringToByte(datagramTail, buffer, HeadAckTip[4], 4);
        //填充完成,发送报文
        try {
             packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(yourAddress), yourPort);
             socket.send(packet);
        } catch (Exception ex) {
            //Logger.getLogger(Receive.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /*发送请求重发包*/
    public boolean sendDatagramReqAgain(int numberRandom,int blockRandom,int littleRandom,boolean receiveTip[]){
        //先判断是否全部接收成功
        boolean success=true;
        for(int i=0;i<receiveTip.length;i++){
            if(receiveTip[i]==false){
                success=false;
                break;
            }
        }
        byte buffer[]=new byte[ReqAgainTip[0]];
        DatagramPacket packet;
        //填充请求重发包标识
        ByteChange.StringToByte(datagramReqAgain, buffer, ReqAgainTip[1], 4);
        //填充次随机数
        ByteChange.intToByte(buffer, ReqAgainTip[2], numberRandom);
        //填充块随机数
        ByteChange.intToByte(buffer, ReqAgainTip[3], blockRandom);
        //填充小随机数
        ByteChange.intToByte(buffer, ReqAgainTip[4], littleRandom);
        //填充数据长度
        if(success){
            //全部成功接收,所以应该发送的是0请求重发包
            ByteChange.shortToByte(buffer, ReqAgainTip[5], (short)0);
            //由于长度为0,所以没有数据,直接填充报尾
            ByteChange.StringToByte(datagramTail, buffer, ReqAgainTip[6], 4);
        }
        else{
            //解析receiveTip,遇到false就将下标存入buffer
            int length=0;
            for(int i=0;i<receiveTip.length;i++){
                if(receiveTip[i]==false){
                    ByteChange.shortToByte(buffer, ReqAgainTip[6]+length*2, (short)i);
                    length++;
                }
            }
            //填充数据长度
            ByteChange.shortToByte(buffer, ReqAgainTip[5], (short)length);
            //填充报尾
            ByteChange.StringToByte(datagramTail, buffer, ReqAgainTip[7], 4);
        }
        try {
            //填充完毕,发送
            if(success){
                packet = new DatagramPacket(buffer, ReqAgainTip[6]+4, InetAddress.getByName(yourAddress), yourPort);
            }else{
                packet = new DatagramPacket(buffer, ReqAgainTip[0], InetAddress.getByName(yourAddress), yourPort);
            }
            lastReqAgain=packet;
            //System.out.println("发送请求重发包,长度"+lastReqAgain.getLength());
            socket.send(packet);
        } catch (Exception ex) {
            Logger.getLogger(ReceiveLittle.class.getName()).log(Level.SEVERE, null, ex);
        }
        return success;
    }
    /*接收一个分组*/
    /*返回值说明:
     * 1.如果正常接收,返回读取数据的长度
     * 2.如果接收超时,返回-1  //此句已经无效
     * 3.如果接收到本分组的分组头,返回-2
     * 4.如果接收到新的发送完毕包,返回-3
     * 5.如果接收到上次的发送完毕包,返回-4
     * 6.其他情况,返回-5
     */
    //此函数只用于接收,不发送任何信息.遇到错误,只是将错误标识返回.
    //如果接收到的是分组包,将其存入buffer中相应位置,返回数据的长度
    public int getDatagramData(int numberRandom,int random,byte buffer[],boolean receiveTip[],int startTip[]){
        //设置不接收超时
        try {
            socket.setSoTimeout(0);
        } catch (SocketException ex) {
            Logger.getLogger(ReceiveLittle.class.getName()).log(Level.SEVERE, null, ex);
        }
        byte receiveBuffer[]=new byte[DataTip[0]];
        DatagramPacket packet=new DatagramPacket(receiveBuffer,receiveBuffer.length);
        try {
            ByteChange.cleanByte(receiveBuffer);
            socket.receive(packet);
            //就算收到未接收请求重发包,有意义的随机数也应该是与本分组随机数相同的
            //次随机数
            if( ByteChange.byteToInt(receiveBuffer, DataTip[2])!=numberRandom ){
                return -5;
            }
            //所以,先判断块随机数
            int r=ByteChange.byteToInt(receiveBuffer, DataTip[3]);
            if(r!=random){
                return -5;
            }
            //判断分组标识
            String s=new String(receiveBuffer,DataTip[1],4);
            if(!s.equals(datagramData)){
                //如果此包不是分组包
                //判断其是否是本分组的分组头.由于随机数已经匹配,所以,只需要判断标识
                if(s.equals(datagramHead)){
                    return -2;
                }
                //再判断其是否是发送完毕包
                if(s.equals(datagramFinish)){
                    //先判断,是否是新的发送完毕包
                    r=ByteChange.byteToInt(receiveBuffer, FinishTip[4]);
                    if(!littleRandomStack.inIt(r)){
                        littleRandomStack.add(r);
                        return -3;
                    }
                    //如果是上次的发送完毕包
                    int r2=littleRandomStack.getLast();
                    if(r2!=-1&&r2==r){
                        return -4;
                    }
                }
                //否则,返回-5
                return -5;
            }
            //至此已经是分组包了,先判断一下包的完整性
            //因为,如果包是不完整的,它的数据就是有问题的,是不能接收的
            s=new String(receiveBuffer,DataTip[7],4);
            if(!s.equals(datagramTail)){
                return -5;
            }
            //包是完整的,获取包的序号
            int serial=ByteChange.byteToShort(receiveBuffer, DataTip[4]);
            int number=ByteChange.byteToShort(receiveBuffer, DataTip[5]);
            //如果该序列的包未被正确接收
            if(!receiveTip[serial]){
                receiveTip[serial]=true;
                for(int i=0;i<number;i++){
                    buffer[i+startTip[serial]]=receiveBuffer[i+DataTip[6]];
                }
                return number;
            }
            //如果已经被正确接收了,则丢弃该包
            else{
                return -5;
            }
        } catch (Exception ex) {
            //Logger.getLogger(Receive.class.getName()).log(Level.SEVERE, null, ex);
            if(ex instanceof SocketTimeoutException){
                return -1;
            }
            return -5;
        }
    }
    /*发送传输结束确认包*/
    public void sendOverAck(int numberRandom){
        DatagramPacket packetOverAck=null,packetOver=null;
        byte sendBuffer[]=new byte[StopAckTip[0]];
        byte receiveBuffer[]=new byte[StopTip[0]];
        //设置接收超时1000ms
        try {
            socket.setSoTimeout(1000);
            /*填充结束确认包*/
            //填充确认包标识
            ByteChange.StringToByte(datagramStopAck, sendBuffer, StopAckTip[1], 4);
            //填充次随机数
            ByteChange.intToByte(sendBuffer, StopAckTip[2], numberRandom);
            //填充报尾
            ByteChange.StringToByte(datagramTail, sendBuffer, StopAckTip[3], 4);
            /*填充完毕*/
            packetOverAck=new DatagramPacket(sendBuffer,sendBuffer.length,InetAddress.getByName(yourAddress),yourPort);
            packetOver=new DatagramPacket(receiveBuffer,receiveBuffer.length);
        } catch (Exception ex) {
            Logger.getLogger(ReceiveLittle.class.getName()).log(Level.SEVERE, null, ex);
        }
        boolean canSendAck=true;
        while(true){
            try{
                if(canSendAck){
                    socket.send(packetOverAck);
                }
                ByteChange.cleanByte(receiveBuffer);
                socket.receive(packetOver);
                //System.out.println("Over:收到包,IP"+packetOver.getAddress());
                //如果接收到传输结束包,就继续循环
                String str=new String(receiveBuffer,StopTip[1],4);
                if(str.equals(datagramStop)){
                    //检查次随机数
                    int num=ByteChange.byteToInt(receiveBuffer, StopTip[2]);
                    if(num==numberRandom){
                        canSendAck=true;
                        continue;
                    }
                }

                //如果收到开始传输包,并且其次随机数与这次的不一样,则退出
                if(str.equals(datagramStart)){
                    //检查次随机数
                    if( ByteChange.byteToInt(receiveBuffer, StartTip[2])!=numberRandom ){
                        break;
                    }
                }
                //如果是其他包,也继续循环,但是一下轮不发送确认包
                canSendAck=false;
                continue;
            }catch(Exception e){
                if(e instanceof SocketTimeoutException){
                    //如果接收超时,则退出循环
                    //System.out.println("接收超时");
                    break;
                }
            }
        }
    }
}
