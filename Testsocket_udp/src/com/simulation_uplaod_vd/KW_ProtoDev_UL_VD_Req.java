package com.simulation_uplaod_vd;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Calendar;



public class KW_ProtoDev_UL_VD_Req{
	
	byte ucProtoVer[]={0x01};        //Э������ 1byte
	byte ucProtoTag[]={0x07};       //��Ϣ����1byte
	byte uiProtoLen[]={}; //����body����(uiToken+body��byte��) 
	byte ucDeviceID[];
	
	//VDID_INFO�ṹ��
	byte uiDate[]={gettime()[3],gettime()[2],gettime()[1],gettime()[0]};
	byte ucStartTime[]={gettime()[4],gettime()[5],gettime()[6]};
	byte ucDuration[]={0x03};
	byte usVDFileSize[]={0x19,0x08}; //��Ҫʵʱ����
	byte ucVDFileResource[]={0x01};
	byte ucVDFileDestination[]={(byte) 0xFF};
	byte uiCrc32[]={(byte) 0xbc,(byte) 0x48,(byte) 0xe4,0x17}; //0x3f,(byte) 0xbc,(byte) 0xd2,(byte) 0x8f
	//0x17 e4 48 bc    60 42 A8 0D  
	byte usCurrentIndex[]={0x00,0x00};
	int currentindex;
	byte usTotalIndex[];
	int totalindex;
	byte pData[];
	int lastarrybytes=0;
	byte get_bytes[]={};
	
	
	
	//ArrayList<Package> al;
	
	public KW_ProtoDev_UL_VD_Req(String ucDeviceID,String filepath){
		
		set_ucDeviceID(ucDeviceID);  //����DeviceID
		
		get_file_Data(filepath);   //��ȡ�����������ݵĶ���ֵ���İ� ����
		
	}
	
	/*
	public void get_file_Data(String path){
		FileInputStream fis=null;
		Package pak=new Package();
		al=new ArrayList<Package>();
		byte b[]=new byte[1024]; //һ�ζ�1024�ֽ�
		int n ;  //ÿ��ʵ�ʶ�ȡ���ֽ���
		
		try {
			
			fis=new FileInputStream(path);
			
			//ÿ�ζ�1024�ֽ� ��װ��package�� ��������ӽ�����
			while((n=fis.read(b))!=-1){
				pak.b=b;    //��ȡ���ֽ�����
				pak.num=n;  //��ȡ���ֽ���
				al.add(pak); //���뼯����
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			
			try {
				fis.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}*/
	
	public void get_file_Data(String path){
		byte [] fileallbytes;
		byte [] filereversebytes;
		FileInputStream fis=null;
		File f=null;
		byte b[]=new byte[1024]; //һ������1024�ֽ�
		int n ;  //ÿ��ʵ�ʶ�ȡ���ֽ���
		ByteArrayOutputStream bos=null;
		try {
			f=new File(path);
			bos=new ByteArrayOutputStream();
			fis=new FileInputStream(f);
			
			while((n=fis.read(b))!=-1){
				//printHexString(b);				
				System.out.println("n="+n);		
				bos.write(b, 0, n);			
			}
			
			//�ļ��������ֽڷ���һ���ֽ�������
			fileallbytes=bos.toByteArray();
			
			//�õ������ֽ�����
			//filereversebytes=reversebytes(fileallbytes);
			
			//System.out.println(filereversebytes.length);
			
			lastarrybytes=fileallbytes.length%512;
			
			if(lastarrybytes==0){
				this.totalindex=fileallbytes.length/512;
			}else{
				this.totalindex=fileallbytes.length/512+1;
			}	
			
			
			System.out.println("show now");
			this.get_bytes=fileallbytes;
			//GetCrcSum crc=new GetCrcSum();
			//this.uiCrc32=crc.Crc32Checksum(filereversebytes);
			//System.out.println("crc:");
			//printHexString(this.uiCrc32);
			
			//printHexString(filereversebytes);
			
			
			System.out.println("totalindex="+totalindex);
			System.out.println("lastarrybytes��="+lastarrybytes);
			//printHexString(filereversebytes);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			
			try {
				fis.close();
				bos.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	//�õ�������ֽ�����
	public  byte[] reversebytes(byte []b){
		byte reverse[]=new byte[b.length]; 
		int j=0;
		
		for(int i=b.length-1;i>=0;i--){
			
			reverse[j]=b[i];
			j++;
		}
		
		return reverse;
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

	//���µ�int ת��Ϊ1byte
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
			
			bos.write(this.usCurrentIndex);
			bos.write(this.usTotalIndex);
			bos.write(this.pData);
			
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
				int len=0;
				//�õ����ݰ����ܷ���
				this.usTotalIndex=int2byte(this.totalindex);
				
				//��Ҫ����al.size()�����ݰ�
//				for(int i=0;i<al.size();i++){				
//					this.usCurrentIndex=int2byte(i);
//					Package pak=al.get(i);
//					if(i!=al.size()-1){
//						this.pData=pak.b;
//						len=this.pData.length+36;
//						this.uiProtoLen=int2byte(len); //�����ܳ���
//					}else{
//						this.pData = new byte[pak.num];  
//					    System.arraycopy(pak.b, 0, this.pData, 0, pak.num);
//					    len=this.pData.length+36;
//					    this.uiProtoLen=int2byte(len);
//					}
				for(int i=0;i<this.totalindex;i++){
					
					System.out.println("index:"+i);
					
					this.usCurrentIndex=int2byte(i);
					
					if(i<this.totalindex-1){
						this.pData=new byte[512];
						
						int start=0;
						for(int j=i*512;j<(i+1)*512;j++){
							this.pData[start]=this.get_bytes[j];
							start++;
						}
					}else{
						this.pData=new byte[lastarrybytes];
						int start=0;
						for(int j=i*512;j<i*512+lastarrybytes;j++){
							this.pData[start]=this.get_bytes[j];
							start++;
						}						
					}
					
					//this.usVDFileSize=int2byte(this.pData.length);
					len=this.pData.length+36;
					System.out.println("index:="+i+" "+"voice_len:="+this.pData.length);
					this.uiProtoLen=int2byte(len);
				
					
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
		 
         //��intת���ɶ���������(��ΪЭ��涨��ֻ��2�ֽ�) ���������int ���ܳ���65535 ��������(С��)		 
		 public static byte[] int2byte(int i) { 			 
			 byte[] targets = new byte[2]; 			   
			 targets[1] = (byte)((i >> 8) & 0xFF); 
			 targets[0] = (byte)(i & 0xFF);   
			 return targets;   
		} 
		
}


