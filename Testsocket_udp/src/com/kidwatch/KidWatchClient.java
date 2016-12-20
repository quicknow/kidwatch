package com.kidwatch;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.*;

/*
 * �ͻ���
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
         * ��������˷�������
         */
        // 1.����������ĵ�ַ���˿ںš�����
        InetAddress address = InetAddress.getByName("10.129.52.44");    	
        int port = 8000;          
        
        byte ucProtoVer[]={0x01};  //�汾
        byte ucProtoTag[]={0x01};  //����
        byte uiProtoLen[]={0x18,0x00};       //��������
        byte ucDevID[]={0x33,0x35,0x37,0x32,0x34,0x34,0x30,0x37,0x30,0x31,0x38,0x39,0x36,0x34,0x35,0x00}; //deviceid 16byte
        byte uiLanguage[]={0x1F,0x00,0x00,0x00};                //language 4
        byte uiFeatrueList[]={0x00,0x07,(byte) 0xff,(byte) 0xff};             //FeatrueList 4
        
        //�������ֽ����� ���һ�����ֽ�����
        byte[] data=byteMerger(byteMerger(byteMerger(byteMerger(byteMerger(ucProtoVer,ucProtoTag), uiProtoLen),ucDevID),uiLanguage),uiFeatrueList);     
              
        // 2.�������ݱ����������͵�������Ϣ             
        
        DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
        // 3.����DatagramSocket����
        DatagramSocket socket = new DatagramSocket();
        // 4.��������˷������ݱ�
        socket.send(packet);

        /*
         * ���շ���������Ӧ������
         */
        // 1.�������ݱ������ڽ��շ���������Ӧ������
        byte[] data2 = new byte[1024];
        DatagramPacket packet2 = new DatagramPacket(data2, data2.length);
        // 2.���շ�������Ӧ������
        socket.receive(packet2);
        // 3.��ȡ����
        //String reply = new String(data2, 0, packet2.getLength());
        //System.out.println("���ǿͻ��ˣ�������˵��" + reply);
        System.out.println(packet2.getLength());
        printHexString(data2);
        
        // 4.�ر���Դ
        //socket.close();
    }
}
