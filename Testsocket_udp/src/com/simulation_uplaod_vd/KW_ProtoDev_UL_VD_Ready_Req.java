package com.simulation_uplaod_vd;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Calendar;



class KW_ProtoDev_UL_VD_Ready_Req{
	
	byte ucProtoVer[]={0x01};        //协议类型 1byte
	byte ucProtoTag[]={0x06};       //消息类型1byte
	byte uiProtoLen[]={0x20,0x00}; //定义body长度(uiToken+body的byte数) 
	byte ucDeviceID[];
	//VDID_INFO结构体
	byte uiDate[]={gettime()[3],gettime()[2],gettime()[1],gettime()[0]};
	byte ucStartTime[]={gettime()[4],gettime()[5],gettime()[6]};
	byte ucDuration[]={0x03};
	byte usVDFileSize[]={0x19,0x08};
	byte ucVDFileResource[]={0x01};
	byte ucVDFileDestination[]={(byte) 0xff};
	byte uiCrc32[]={(byte) 0xbc,(byte) 0x48,(byte) 0xe4,0x17};
	
	public KW_ProtoDev_UL_VD_Ready_Req(String ucDeviceID){
		
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
	
	
	//得到时间的十六进制数据
		public byte[] gettime(){
			
			Calendar c = Calendar.getInstance();//可以对每个时间域单独修改		
			int year = c.get(Calendar.YEAR); 
			int month = c.get(Calendar.MONTH)+1; 
			int date = c.get(Calendar.DATE); 
			int hour = c.get(Calendar.HOUR_OF_DAY); 
			int minute = c.get(Calendar.MINUTE); 
			int second = c.get(Calendar.SECOND); 
			
			byte year_1=year_int_to_byte(year); //year的前二位转化成一个字节
			byte year_2=int_to_byte(year);      //year的后二位转化成一个字节
			byte month_1=int_to_byte(month);
			byte date_1=int_to_byte(date);
			byte hour_1=int_to_byte(hour);
			byte minute_1=int_to_byte(minute);
			byte second_1=int_to_byte(second);
			
			byte get_time[]=new byte[7];
			get_time[0]=year_1;
			get_time[1]=year_2;
			get_time[2]=month_1;
			get_time[3]=date_1;
			get_time[4]=hour_1;
			get_time[5]=minute_1;
		    get_time[6]=second_1;
		    
		    return get_time;
		}

		public static byte int_to_byte(int i) { 
			 
			byte targets;   			   
			 //targets[0] = (byte)((i >> 8) & 0xFF); 
			 targets = (byte)(i & 0xFF);   
			 return targets;   
		} 
	 
		public static byte year_int_to_byte(int i) { 
			 
			 byte targets; 			   
			 //targets[0] = (byte)((i >> 8) & 0xFF); 
			 targets = (byte)(i>>8 & 0xFF);   
			 return targets;   
		} 
	
	
	
	//将数据包转化成字节数
		public byte[] get_data_bytes(){
			byte data[]=null;
			ByteArrayOutputStream bos=new ByteArrayOutputStream();
			try {
				bos.write(ucProtoVer);
				bos.write(ucProtoTag);
				bos.write(uiProtoLen);
				
				bos.write(ucDeviceID);
				bos.write(uiDate);
				bos.write(ucStartTime);
				bos.write(ucDuration);
				bos.write(usVDFileSize);
				bos.write(ucVDFileResource);
				bos.write(ucVDFileDestination);
				bos.write(uiCrc32);
				
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
		        //String reply = new String(data2, 0, packet2.getLength());
		        //System.out.println("我是客户端，服务器说：" + reply);
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
	
		
}


