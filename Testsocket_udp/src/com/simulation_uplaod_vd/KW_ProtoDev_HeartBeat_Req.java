package com.simulation_uplaod_vd;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;



class KW_ProtoDev_HeartBeat_Req extends Thread{
	
	byte ucProtoVer[]={0x01};        //协议类型 1byte
	byte ucProtoTag[]={0x02};       //消息类型1byte
	byte uiProtoLen[]={0x18,0x00}; //定义body长度(uiToken+body的byte数) 
	byte ucDeviceID[];
	byte usBatCap[]={0x5F, 0x11};
	
	public KW_ProtoDev_HeartBeat_Req(String ucDeviceID){
		
		set_ucDeviceID(ucDeviceID);	
		
	}
	
	
	
	 //设置ucDeviceID的字节数
	public void set_ucDeviceID(String ucDeviceID){
		byte[]imei=ucDeviceID.getBytes();		
		byte a[]={0x00};	//低位补一个字节的0补足十六个字节	
		
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
	
	//将数据包转化成字节数
		public byte[] get_data_bytes(){
			byte data[]=null;
			ByteArrayOutputStream bos=new ByteArrayOutputStream();
			try {
				bos.write(ucProtoVer);
				bos.write(ucProtoTag);
				bos.write(uiProtoLen);
				//bos.write(kwHead.uiToken);
				bos.write(ucDeviceID);
				bos.write(usBatCap);				
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
		
		
		//发送字节流
		public void send(){		
			byte[] data=get_data_bytes();
			try {			
				 /*
		         * 向服务器端发送数据
		         */
		        // 1.定义服务器的地址、端口号、数据
		        InetAddress address = InetAddress.getByName("10.129.52.146");    	
		        int port = 8000;     
		     
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
		        System.out.println(packet2.getLength());
		        printHexString(data2);
		        
		        // 4.关闭资源
		        socket.close();
		        
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();				
				}		
		}
		
		//调式打印出字节流
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
		 
		 //线程运行程序
		 public void run(){
			 
			 while(true){
				 
				 try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 send(); //不停发心跳包
			 }
			 
		 }
		
}


