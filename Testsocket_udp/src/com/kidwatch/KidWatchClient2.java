package com.kidwatch;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class KidWatchClient2 {
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		byte ucProtoVer[]={0x01}; 
		byte ucProtoTag[]={0x01}; 
		byte heartBeat_Req_ucProtoTag[]={0x02};
		byte uiProtoLen[]={0x18,0x00};
		byte heartBeat_Req_uiProtoLen[]={0x12,0x00};
		byte uiToken[]=null;
		
		byte ucDeviceID[]={0x33,0x35,0x37,0x32,0x34,0x34,0x30,0x37,0x30,0x31,0x38,0x39,0x36,0x34,0x35,0x00};
		byte uiLanguage[]={0x1F,0x00,0x00,0x00};                        //language   4byte
        byte uiFeatrueList[]={0x00,0x07,(byte) 0xff,(byte) 0xff};      //FeatrueList 4byte
        byte LogIn_Req_data[]=null;
        byte HeartBeat_Req_data[]=null;         
        byte usBatCap[]={0x00,0x01};
        
        //����λ����Ϣ�ĳ�ʼ����
        byte DeliverySensorInfo_Req_ucProtoTag[]={0x05};
        byte DeliverySensorInfo_Req_Len[]={0x2A,00};
        
    	byte ucGPSItemNum[]={0x01};
    	byte ucLBSItemNum[] ={0x00};
    	byte ucWifiItemNum[]={0x00};    	
    	byte ucGsensorItemNum[]={0x00};
    	
    	byte uiDate[]={0x07,(byte) 0xdf,0x02,0x12};
    	byte ucTime[]={0x01};
    	byte ucSatelliteNum[]={0x03};
    	byte dLatitude[]={0x03,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
    	byte dLongitude[]={0x05,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
    	byte deliverSensorInfo_Req_data[]=null;
    	
    	
    	//gps��Ϣ��ʼ��
    	GPSInfo_t pGPSInfo=new GPSInfo_t(uiDate, ucTime, ucSatelliteNum, dLatitude, dLongitude);
    	LBSInfo_t               pLBSInfo;
        WifiLocInfo_t           pWifiInfo;
    	G_SensorInfo_t          pGSensorInfo;
        
		
		//��¼�����ײ���ʼ��
		KW_Proto_Head logIn_Req_head=new KW_Proto_Head(ucProtoVer, ucProtoTag, uiProtoLen);
		//��¼����ṹ�� ��ʼ��
		KW_ProtoDev_LogIn_Req LogIn_Req = new KW_ProtoDev_LogIn_Req(logIn_Req_head,ucDeviceID,uiLanguage,uiFeatrueList);
		LogIn_Req_data=LogIn_Req.get_data_bytes();
		LogIn_Req.send(LogIn_Req_data);	
		
		KW_Proto_Head heartBeat_Req_head=new KW_Proto_Head(ucProtoVer, heartBeat_Req_ucProtoTag, heartBeat_Req_uiProtoLen);
		KW_ProtoDev_HeartBeat_Req heartBeat_Req=new KW_ProtoDev_HeartBeat_Req(heartBeat_Req_head, ucDeviceID, usBatCap);
		HeartBeat_Req_data=heartBeat_Req.get_data_bytes();
		heartBeat_Req.send(HeartBeat_Req_data);
		//head��ʼ��
		KW_Proto_Head deliverySensorInfo_Req_head=new KW_Proto_Head(ucProtoVer, DeliverySensorInfo_Req_ucProtoTag, DeliverySensorInfo_Req_Len);
		//����KW_ProtoDev_DeliverySensorInfo_Req ������Ϣ
		KW_ProtoDev_DeliverySensorInfo_Req deliverSensorInfo_Req=new KW_ProtoDev_DeliverySensorInfo_Req(deliverySensorInfo_Req_head, ucDeviceID, ucGPSItemNum, ucLBSItemNum, ucWifiItemNum, ucGsensorItemNum, pGPSInfo, null, null, null);
		
		deliverSensorInfo_Req_data=deliverSensorInfo_Req.get_data_bytes();
		deliverSensorInfo_Req.send(deliverSensorInfo_Req_data);
		
	
	}

}

//�����ײ��ֶ�
class KW_Proto_Head{
	byte ucProtoVer[]; //Э������ 1byte
	byte ucProtoTag[]; //��Ϣ����1byte
	byte uiProtoLen[]; //����body����(uiToken+body��byte��) 
	//byte uiToken[];    //��������ֶ�
	
	//��ʼ��
	public KW_Proto_Head(byte ucProtoVer[],byte ucProtoTag[],byte uiProtoLen[]){
		this.ucProtoVer=ucProtoVer;
		this.ucProtoTag=ucProtoTag;
		this.uiProtoLen=uiProtoLen;
		//this.uiToken=uiToken;
	}
}

//��¼������Ϣ�ṹ�� ���ײ�+body��
class KW_ProtoDev_LogIn_Req{
	KW_Proto_Head kwHead;
	byte ucDeviceID[];      //IMEI(ascii��ֵ)16byte ���ұ�1byteΪ0x00
	byte uiLanguage[];      //�������� 4byte
	byte uiFeatrueList[];   //�������� 4byte
	public KW_ProtoDev_LogIn_Req(KW_Proto_Head kwHead,byte ucDeviceID[],byte uiLanguage[],byte uiFeatrueList[]){
		this.kwHead=kwHead;
		this.ucDeviceID=ucDeviceID;
		this.uiLanguage=uiLanguage;
		this.uiFeatrueList=uiFeatrueList;
	}
	
	//�����ݰ�ת�����ֽ���
	public byte[] get_data_bytes(){
		byte data[]=null;
		ByteArrayOutputStream bos=new ByteArrayOutputStream();
		try {
			bos.write(kwHead.ucProtoVer);
			bos.write(kwHead.ucProtoTag);
			bos.write(kwHead.uiProtoLen);
			//bos.write(kwHead.uiToken);
			bos.write(ucDeviceID);
			bos.write(uiLanguage);
			bos.write(uiFeatrueList);
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
	public void send(byte[] data){		
		 
		try {			
			 /*
	         * ��������˷�������
	         */
	        // 1.����������ĵ�ַ���˿ںš�����
	        InetAddress address = InetAddress.getByName("10.129.52.44");    	
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


//��¼ȷ����Ϣ�ṹ�� ���ײ�+body��
class KW_ProtoDev_LogIn_Ack{
	KW_Proto_Head kwHead;
	byte usRegStatus[];   //������Ϣ2byte
	
	//��ʼ��
	public KW_ProtoDev_LogIn_Ack(KW_Proto_Head kwHead,byte usRegStatus[]){
		this.kwHead=kwHead;
		this.usRegStatus=usRegStatus;
	}
}



//����������Ϣ
class KW_ProtoDev_HeartBeat_Req{
	KW_Proto_Head kwHead;
	byte ucDeviceID[];
	byte usBatCap[];
	
	public KW_ProtoDev_HeartBeat_Req(KW_Proto_Head kwHead,byte ucDeviceID[],byte usBatCap[]){
		
		this.kwHead=kwHead;
		this.ucDeviceID=ucDeviceID;
		this.usBatCap=usBatCap;
	}
	
	
	
	//�����ݰ�ת�����ֽ���
		public byte[] get_data_bytes(){
			byte data[]=null;
			ByteArrayOutputStream bos=new ByteArrayOutputStream();
			try {
				bos.write(kwHead.ucProtoVer);
				bos.write(kwHead.ucProtoTag);
				bos.write(kwHead.uiProtoLen);
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
		
		
		//�����ֽ���
		public void send(byte[] data){		
			 
			try {			
				 /*
		         * ��������˷�������
		         */
		        // 1.����������ĵ�ַ���˿ںš�����
		        InetAddress address = InetAddress.getByName("10.129.52.44");    	
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

//�ϴ�SensorInfo��Ϣ����
class  KW_ProtoDev_DeliverySensorInfo_Req{
	KW_Proto_Head kw_Proto_Head;
	byte[] ucDevID;
	byte[] ucGPSItemNum;
	byte[] ucLBSItemNum;
	byte[] ucWifiItemNum;
	byte[] ucGsensorItemNum;
	
	GPSInfo_t				pGPSInfo;
	LBSInfo_t               pLBSInfo;
    WifiLocInfo_t           pWifiInfo;
	G_SensorInfo_t          pGSensorInfo;
	
	public KW_ProtoDev_DeliverySensorInfo_Req(KW_Proto_Head kw_Proto_Head,byte[] ucDevID,byte[] ucGPSItemNum,byte[] ucLBSItemNum,byte[] ucWifiItemNum,byte[] ucGsensorItemNum,
			GPSInfo_t pGPSInfo,LBSInfo_t   pLBSInfo,WifiLocInfo_t  pWifiInfo,G_SensorInfo_t  pGSensorInfo){
		this.kw_Proto_Head=kw_Proto_Head;
		this.ucDevID=ucDevID;
		this.ucGPSItemNum=ucGPSItemNum;
		this.ucLBSItemNum=ucLBSItemNum;
		this.ucWifiItemNum=ucWifiItemNum;
		this.ucGPSItemNum=ucGPSItemNum;
		this.ucGsensorItemNum=ucGsensorItemNum;
		
		this.pGPSInfo=pGPSInfo;
		this.pLBSInfo=pLBSInfo;
		this.pWifiInfo=pWifiInfo;
		this.pGSensorInfo=pGSensorInfo;
				
		
		
	}
	
	
	
	
	//�����ݰ�ת�����ֽ���
	public byte[] get_data_bytes(){
		byte data[]=null;
		ByteArrayOutputStream bos=new ByteArrayOutputStream();
		try {
			bos.write(kw_Proto_Head.ucProtoVer);
			bos.write(kw_Proto_Head.ucProtoTag);
			bos.write(kw_Proto_Head.uiProtoLen);
			//bos.write(kwHead.uiToken);
			bos.write(ucDevID);
			bos.write(ucGPSItemNum);			
			bos.write(ucLBSItemNum);
			bos.write(ucWifiItemNum);
			bos.write(ucGsensorItemNum);
			
			bos.write(pGPSInfo.uiDate);
			bos.write(pGPSInfo.ucTime);
			bos.write(pGPSInfo.ucSatelliteNum);
			bos.write(pGPSInfo.dLatitude);
			bos.write(pGPSInfo.dLongitude);
			
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
	public void send(byte[] data){		
		 
		try {			
			 /*
	         * ��������˷�������
	         */
	        // 1.����������ĵ�ַ���˿ںš�����
	        InetAddress address = InetAddress.getByName("10.129.52.44");    	
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

//gps��Ϣ��
class GPSInfo_t{
	
	byte[] uiDate;
	byte[] ucTime;
	byte[] ucSatelliteNum;
	byte[] dLatitude;
	byte[] dLongitude;
	
	public GPSInfo_t(byte[] uiDate,byte[] ucTime,byte[] ucSatelliteNum,byte[] dLatitude,byte[] dLongitude){
		
		this.uiDate=uiDate;
		this.ucTime=ucTime;
		this.ucSatelliteNum=ucSatelliteNum;
		this.dLatitude=dLatitude;
		this.dLongitude=dLongitude;		
	}
	
}

//LBS��Ϣ��
class LBSInfo_t{
	byte[] uiDate;
	byte[] uiTime;
	byte[] usMCC;
	byte[] usMNC;
	byte[] ssSignalStrength;
	byte[] usLAC;
	byte[] usCellID;
	byte[] usIsConnected;
	
	public LBSInfo_t(byte[] uiDate,byte[] uiTime,byte[] usMCC,byte[] usMNC,byte[] ssSignalStrength,byte[] usLAC,byte[] usCellID,byte[] usIsConnected){
		
		this.uiDate=uiDate;
		this.uiTime=uiTime;
		this.usMCC=usMCC;
		this.usMNC=usMNC;
		this.ssSignalStrength=ssSignalStrength;
		this.usLAC=usLAC;
		this.usCellID=usCellID;
		this.usIsConnected=usIsConnected;
	}
	
}

//wifiλ����Ϣ
class WifiLocInfo_t{

	byte[]	uiDate;//��ȡwifi��Ϣ����
	byte[]	uiTime;//��ȡwifi��Ϣʱ��
	byte[]	ucAPMac;//AP MAC��ַ
	byte[]	ssSignalStrength;//wifi�ź�ǿ��
	byte[]	cSsid;
	
	public WifiLocInfo_t(byte[]	uiDate,byte[]	uiTime,byte[]	ucAPMac,byte[]	ssSignalStrength,byte[]	cSsid){
		this.uiDate=uiDate;
		this.uiTime=uiTime;
		this.ucAPMac=ucAPMac;
		this.ssSignalStrength=ssSignalStrength;
		this.cSsid=cSsid;
		
	}
}

//G_SensorInfo_t��Ϣ��
class G_SensorInfo_t{
	byte[] 			uiStartDate;//��ȡsensor��Ϣ����
	byte[]			uiStartTime;//��ȡsensor��Ϣʱ��
	byte[]			uiEndDate;//��ȡsensor��Ϣ����
	byte[]			uiEndTime;//��ȡsensor��Ϣʱ��
	byte[]			ubSportType;//staitionary(0x00), walk(0x01), run(0x03)
	byte[]          ubStepCount;//Step Count.
	byte[]		    fEnergyCost;//Energy cost
	
	public G_SensorInfo_t(byte[] uiStartDate,byte[]	uiStartTime,byte[]	uiEndDate,byte[] uiEndTime,byte[] ubSportType,byte[]  ubStepCount,byte[]  fEnergyCost){
		this.uiStartDate=uiStartDate;
		this.uiStartTime=uiStartTime;
		this.uiEndDate=uiEndDate;
		this.uiEndTime=uiEndTime;
		this.ubSportType=ubSportType;
		this.ubStepCount=ubStepCount;
		this.fEnergyCost=fEnergyCost;
		
	}
	
	
	
	
}







