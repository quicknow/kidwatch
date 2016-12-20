package com.simulation_deliver_sensor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Calendar;


public class KW_ProtoDev_DeliverySensorInfo_Req extends Thread{
	
	byte ucProtoVer[]={0x01};        //协议类型 1byte
	byte ucProtoTag[]={0x05};       //消息类型1byte
	byte uiProtoLen[]={0x30,0x00}; //定义body长度(uiToken+body的byte数) 	
	
	byte[] ucDeviceID;
	byte[] ucGPSItemNum={0x01};
	byte[] ucLBSItemNum={0x00};
	byte[] ucWifiItemNum={0x00};
	byte[] ucGsensorItemNum={0x00};
	
	
	//gps信息体
	byte[] pGPSInfo_uiDate={gettime()[3],gettime()[2],gettime()[1],gettime()[0]}; //日月年2 年1
	byte[] pGPSInfo_ucTime={gettime()[4],gettime()[5],gettime()[6]}; //时分秒
	byte[] pGPSInfo_ucSatelliteNum={0x03};
	byte[] pGPSInfo_dLatitude={(byte) 0xAE,0x47,(byte) 0xE1,0x7A,0x14,0x0E,0x5C,0x40};
	byte[] pGPSInfo_dLongitude={0x3D,0x0A,(byte) 0xD7,(byte) 0xA3,0x70,(byte) 0x9D,0x4D,0x40};	
	
	//LBS信息体
	byte[] pLBSInfo_uiDate={0x07,(byte) 0xdf,0x02,0x18};
	byte[] pLBSInfo_ucTime={0x00,0x00,0x00};
	byte[] pLBSInfo_usMCC={0x03,0x00};
	byte[] pLBSInfo_usMNC={0x03,0x00};
	byte[] pLBSInfo_ssSignalStrength={0x03,0x00};
	byte[] pLBSInfo_usLAC={0x00,0x00};
	byte[] pLBSInfo_uiCellID={0x00,0x00};
	byte[] pLBSInfo_uslsConnected={0x00,0x02};	
   
	//wifi信息体
    byte[] pWifiInfo_uiDate={0x07,(byte) 0xdf,0x02,0x18};
	byte[] pWifiInfo_ucTime={0x00,0x00,0x00};
	byte[] pWifiInfo_ucAPMac={0x03};
	byte[] pWifiInfo_ssSignalStrength={0x03,0x00};
	byte[] cSsid={0x03};
    
    //Gsensor信息体
	byte[] pGSensorInfo_uiStartDate={0x07,(byte) 0xdf,0x02,0x18};  
	byte[] pGSensorInfo_ucStartTime={0x01,0x00,0x00,0x00};
	byte[] pGSensorInfo_uiEndDate={0x00,0x00};
	byte[] pGSensorInfo_uiEndTime={0x03,0x00};
	byte[] pGSensorInfo_ubSportType={0x03,0x00};
	byte[] pGSensorInfo_ubStepCount={0x03,0x00};
	byte[] pGSensorInfo_fEnergyCost={0x00,0x00};
	
	
	
	
	public KW_ProtoDev_DeliverySensorInfo_Req(String ucDeviceID){
	
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
			bos.write(ucGPSItemNum);			
			bos.write(ucLBSItemNum);
			bos.write(ucWifiItemNum);
			bos.write(ucGsensorItemNum);
			
			//gps信息体的字节数		
			bos.write(pGPSInfo_uiDate);
			bos.write(pGPSInfo_ucTime);
			bos.write(pGPSInfo_ucSatelliteNum);
			bos.write(pGPSInfo_dLatitude);
			bos.write(pGPSInfo_dLongitude);
			
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
	 public static void printHexString(byte[] b)
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
			 send(); //不停发送位置信息包
		 }
		 
	 }
					
}


