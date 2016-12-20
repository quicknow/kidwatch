package com.kidwatch;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.*;

/*
 * 客户端
 */
public class KidWatchClient {
	
	  public static byte[] byteMerger(byte[] byte_1, byte[] byte_2){  
	        byte[] byte_3 = new byte[byte_1.length+byte_2.length];  
	        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);  
	        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);  
	        return byte_3;  
	    }  
	  
	  
	  public static void printHexString( byte[] b)
	    {
	        //System.out.print(hint);
	        for (int i = 0; i < b.length; i++)
	        {
	            String hex = Integer.toHexString(b[i] & 0xFF);
	            if (hex.length() == 1)
	            {
	                hex = '0' + hex;
	            }
	            System.out.print(hex.toUpperCase() + " ");
	        }
	        System.out.println("");
	    }  
	
	
    public static void main(String[] args) throws IOException {
        /*
         * 向服务器端发送数据
         */
        // 1.定义服务器的地址、端口号、数据
        InetAddress address = InetAddress.getByName("10.129.52.44");    	
        int port = 8000;          
        
        byte ucProtoVer[]={0x01};  //版本
        byte ucProtoTag[]={0x01};  //类型
        byte uiProtoLen[]={0x18,0x00};       //参数长度
        byte ucDevID[]={0x33,0x35,0x37,0x32,0x34,0x34,0x30,0x37,0x30,0x31,0x38,0x39,0x36,0x34,0x35,0x00}; //deviceid 16byte
        byte uiLanguage[]={0x1F,0x00,0x00,0x00};                //language 4
        byte uiFeatrueList[]={0x00,0x07,(byte) 0xff,(byte) 0xff};             //FeatrueList 4
        
        //将所有字节数组 组成一个大字节数组
        byte[] data=byteMerger(byteMerger(byteMerger(byteMerger(byteMerger(ucProtoVer,ucProtoTag), uiProtoLen),ucDevID),uiLanguage),uiFeatrueList);     
              
        // 2.创建数据报，包含发送的数据信息             
        
        DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
        // 3.创建DatagramSocket对象
        DatagramSocket socket = new DatagramSocket();
        // 4.向服务器端发送数据报
        socket.send(packet);

        /*
         * 接收服务器端响应的数据
         */
        // 1.创建数据报，用于接收服务器端响应的数据
        byte[] data2 = new byte[1024];
        DatagramPacket packet2 = new DatagramPacket(data2, data2.length);
        // 2.接收服务器响应的数据
        socket.receive(packet2);
        // 3.读取数据
        //String reply = new String(data2, 0, packet2.getLength());
        //System.out.println("我是客户端，服务器说：" + reply);
        System.out.println(packet2.getLength());
        printHexString(data2);
        
        // 4.关闭资源
        //socket.close();
    }
}
