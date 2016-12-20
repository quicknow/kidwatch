package com.simulation_deliver_sensor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Calendar;


public class KW_ProtoDev_DeliverySensorInfo_Req extends Thread{
	
	byte ucProtoVer[]={0x01};        //Э������ 1byte
	byte ucProtoTag[]={0x05};       //��Ϣ����1byte
	byte uiProtoLen[]={0x30,0x00}; //����body����(uiToken+body��byte��) 	
	
	byte[] ucDeviceID;
	byte[] ucGPSItemNum={0x01};
	byte[] ucLBSItemNum={0x00};
	byte[] ucWifiItemNum={0x00};
	byte[] ucGsensorItemNum={0x00};
	
	
	//gps��Ϣ��
	byte[] pGPSInfo_uiDate={gettime()[3],gettime()[2],gettime()[1],gettime()[0]}; //������2 ��1
	byte[] pGPSInfo_ucTime={gettime()[4],gettime()[5],gettime()[6]}; //ʱ����
	byte[] pGPSInfo_ucSatelliteNum={0x03};
	byte[] pGPSInfo_dLatitude={(byte) 0xAE,0x47,(byte) 0xE1,0x7A,0x14,0x0E,0x5C,0x40};
	byte[] pGPSInfo_dLongitude={0x3D,0x0A,(byte) 0xD7,(byte) 0xA3,0x70,(byte) 0x9D,0x4D,0x40};	
	
	//LBS��Ϣ��
	byte[] pLBSInfo_uiDate={0x07,(byte) 0xdf,0x02,0x18};
	byte[] pLBSInfo_ucTime={0x00,0x00,0x00};
	byte[] pLBSInfo_usMCC={0x03,0x00};
	byte[] pLBSInfo_usMNC={0x03,0x00};
	byte[] pLBSInfo_ssSignalStrength={0x03,0x00};
	byte[] pLBSInfo_usLAC={0x00,0x00};
	byte[] pLBSInfo_uiCellID={0x00,0x00};
	byte[] pLBSInfo_uslsConnected={0x00,0x02};	
   
	//wifi��Ϣ��
    byte[] pWifiInfo_uiDate={0x07,(byte) 0xdf,0x02,0x18};
	byte[] pWifiInfo_ucTime={0x00,0x00,0x00};
	byte[] pWifiInfo_ucAPMac={0x03};
	byte[] pWifiInfo_ssSignalStrength={0x03,0x00};
	byte[] cSsid={0x03};
    
    //Gsensor��Ϣ��
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
	
	
	public byte[] gettime(){
		
		Calendar c = Calendar.getInstance();//���Զ�ÿ��ʱ���򵥶��޸�		
		int year = c.get(Calendar.YEAR); 
		int month = c.get(Calendar.MONTH)+1; 
		int date = c.get(Calendar.DATE); 
		int hour = c.get(Calendar.HOUR_OF_DAY); 
		int minute = c.get(Calendar.MINUTE); 
		int second = c.get(Calendar.SECOND); 
		
		byte year_1=year_int_to_byte(year); //year��ǰ��λת����һ���ֽ�
		byte year_2=int_to_byte(year);      //year�ĺ��λת����һ���ֽ�
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
	
	
	//�����ݰ�ת�����ֽ���
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
			
			//gps��Ϣ����ֽ���		
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
	        socket.close();
	        
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();				
			}		
	}
	
	//��ʽ��ӡ���ֽ���
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
	 
	 //�߳����г���
	 public void run(){
		 
		 while(true){
			 
			 try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 send(); //��ͣ����λ����Ϣ��
		 }
		 
	 }
					
}


