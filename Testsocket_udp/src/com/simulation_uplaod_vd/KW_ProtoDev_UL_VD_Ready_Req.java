package com.simulation_uplaod_vd;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Calendar;



class KW_ProtoDev_UL_VD_Ready_Req{
	
	byte ucProtoVer[]={0x01};        //Э������ 1byte
	byte ucProtoTag[]={0x06};       //��Ϣ����1byte
	byte uiProtoLen[]={0x20,0x00}; //����body����(uiToken+body��byte��) 
	byte ucDeviceID[];
	//VDID_INFO�ṹ��
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
	
	
	//�õ�ʱ���ʮ����������
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


