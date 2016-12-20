package com.simulation_download_vd;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class KW_ProtoDev_DL_VD_Req {

	byte ucProtoVer[]={0x01};        //Э������ 1byte
	byte ucProtoTag[]={0x09};       //��Ϣ����1byte
	byte uiProtoLen[]={0x20,0x00}; //����body����(uiToken+body��byte��) 
	byte ucDeviceID[]={};	
	
	//VDID_INFO�ṹ��
	byte uiDate[]={0x07,(byte) 0xdf,0x02,0x18};
	byte ucStartTime[]={0x01,0x01,0x01};
	byte ucDuration[]={0x03};
	byte usVDFileSize[]={0x19,0x08};
	byte ucVDFileResource[]={0x02};
	byte ucVDFileDestination[]={(byte) 0xfe};
	byte uiCrc32[]={0x00,0x00,0x00,0x00};
	
	public KW_ProtoDev_DL_VD_Req(String ucDeviceID){
		
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
		
		
		
	
		
		public byte[] get_data_bytes(){
			byte data[]=null;
			ByteArrayOutputStream bos=new ByteArrayOutputStream();
			try {
				bos.write(this.ucProtoVer);
				bos.write(this.ucProtoTag);
				bos.write(this.uiProtoLen);			
				bos.write(this.ucDeviceID);			
				bos.write(this.uiDate);
				bos.write(this.ucStartTime);
				bos.write(this.ucDuration);
				bos.write(this.usVDFileSize);
				bos.write(this.ucVDFileResource);
				bos.write(this.ucVDFileDestination);
				bos.write(this.uiCrc32);
				
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
				byte data[]={};
					
					//�õ����ݰ����ܷ���					
						try {			
							 /*
					         * ��������˷�������
					         */
					        // 1.����������ĵ�ַ���˿ںš�����
					        InetAddress address = InetAddress.getByName("10.129.52.146");    	
					        int port = 8000;     
					     
					        // 2.�������ݱ����������͵�������Ϣ           
					        data=get_data_bytes();
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
