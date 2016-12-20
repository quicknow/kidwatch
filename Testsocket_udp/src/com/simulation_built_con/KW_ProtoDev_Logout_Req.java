package com.simulation_built_con;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class KW_ProtoDev_Logout_Req {

	byte ucProtoVer[]={0x01};        //Э������ 1byte
	byte ucProtoTag[]={0x03};       //��Ϣ����1byte
	byte uiProtoLen[]={0x10,0x00}; //����body����(uiToken+body��byte��) 	
	byte ucDeviceID[];             //IMEI(ascii��ֵ)16byte ���ұ�1byteΪ0x00

	public KW_ProtoDev_Logout_Req(String ucDeviceID){
		
		set_ucDeviceID(ucDeviceID);	
		
	}
	
	
	  //����ucDeviceID���ֽ���
	public void set_ucDeviceID(String ucDeviceID){
		byte[]imei=ucDeviceID.getBytes();		
		byte a[]={0x00};	//��λ��һ���ֽڵ�0����ʮ�����ֽ�	
		
		ByteArrayOutputStream bos=new ByteArrayOutputStream();
		try {
			bos.write(imei);
			bos.write(a);		
			this.ucDeviceID=bos.toByteArray();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			try {
				bos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
		//return this.ucDeviceID;		
	}
	
	//�����ݰ�ת�����ֽ���
	public byte[] get_data_bytes(){
		byte data[]=null;
		ByteArrayOutputStream bos=new ByteArrayOutputStream();
		try {
			bos.write(ucProtoVer);
			bos.write(ucProtoTag);
			bos.write(uiProtoLen);
			//bos.write(kwHead.uiToken);
			bos.write(ucDeviceID);			
			data=bos.toByteArray();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			try {
				bos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		return data;
	}
	
	//�����ֽ���
	public void send(){		
		byte[] data=get_data_bytes();
		try {			
			 /*
	         * ��������˷�������
	         */
	        // 1.����������ĵ�ַ���˿ںš�����
	        InetAddress address = InetAddress.getByName("10.129.52.146");    	
	        int port = 8000;     
	     
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
	        
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();				
			}		
	}
	
	//��ʽ��ӡ���ֽ���
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
}
