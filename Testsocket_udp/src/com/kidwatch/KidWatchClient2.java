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
        
        //发送位置信息的初始数据
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
    	
    	
    	//gps信息初始化
    	GPSInfo_t pGPSInfo=new GPSInfo_t(uiDate, ucTime, ucSatelliteNum, dLatitude, dLongitude);
    	LBSInfo_t               pLBSInfo;
        WifiLocInfo_t           pWifiInfo;
    	G_SensorInfo_t          pGSensorInfo;
        
		
		//登录请求首部初始化
		KW_Proto_Head logIn_Req_head=new KW_Proto_Head(ucProtoVer, ucProtoTag, uiProtoLen);
		//登录请求结构体 初始化
		KW_ProtoDev_LogIn_Req LogIn_Req = new KW_ProtoDev_LogIn_Req(logIn_Req_head,ucDeviceID,uiLanguage,uiFeatrueList);
		LogIn_Req_data=LogIn_Req.get_data_bytes();
		LogIn_Req.send(LogIn_Req_data);	
		
		KW_Proto_Head heartBeat_Req_head=new KW_Proto_Head(ucProtoVer, heartBeat_Req_ucProtoTag, heartBeat_Req_uiProtoLen);
		KW_ProtoDev_HeartBeat_Req heartBeat_Req=new KW_ProtoDev_HeartBeat_Req(heartBeat_Req_head, ucDeviceID, usBatCap);
		HeartBeat_Req_data=heartBeat_Req.get_data_bytes();
		heartBeat_Req.send(HeartBeat_Req_data);
		//head初始化
		KW_Proto_Head deliverySensorInfo_Req_head=new KW_Proto_Head(ucProtoVer, DeliverySensorInfo_Req_ucProtoTag, DeliverySensorInfo_Req_Len);
		//发送KW_ProtoDev_DeliverySensorInfo_Req 请求消息
		KW_ProtoDev_DeliverySensorInfo_Req deliverSensorInfo_Req=new KW_ProtoDev_DeliverySensorInfo_Req(deliverySensorInfo_Req_head, ucDeviceID, ucGPSItemNum, ucLBSItemNum, ucWifiItemNum, ucGsensorItemNum, pGPSInfo, null, null, null);
		
		deliverSensorInfo_Req_data=deliverSensorInfo_Req.get_data_bytes();
		deliverSensorInfo_Req.send(deliverSensorInfo_Req_data);
		
	
	}

}

//定义首部字段
class KW_Proto_Head{
	byte ucProtoVer[]; //协议类型 1byte
	byte ucProtoTag[]; //消息类型1byte
	byte uiProtoLen[]; //定义body长度(uiToken+body的byte数) 
	//byte uiToken[];    //定义加密字段
	
	//初始化
	public KW_Proto_Head(byte ucProtoVer[],byte ucProtoTag[],byte uiProtoLen[]){
		this.ucProtoVer=ucProtoVer;
		this.ucProtoTag=ucProtoTag;
		this.uiProtoLen=uiProtoLen;
		//this.uiToken=uiToken;
	}
}

//登录请求消息结构体 （首部+body）
class KW_ProtoDev_LogIn_Req{
	KW_Proto_Head kwHead;
	byte ucDeviceID[];      //IMEI(ascii码值)16byte 最右边1byte为0x00
	byte uiLanguage[];      //语言种类 4byte
	byte uiFeatrueList[];   //功能种类 4byte
	public KW_ProtoDev_LogIn_Req(KW_Proto_Head kwHead,byte ucDeviceID[],byte uiLanguage[],byte uiFeatrueList[]){
		this.kwHead=kwHead;
		this.ucDeviceID=ucDeviceID;
		this.uiLanguage=uiLanguage;
		this.uiFeatrueList=uiFeatrueList;
	}
	
	//将数据包转化成字节数
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
	
	//发送字节流
	public void send(byte[] data){		
		 
		try {			
			 /*
	         * 向服务器端发送数据
	         */
	        // 1.定义服务器的地址、端口号、数据
	        InetAddress address = InetAddress.getByName("10.129.52.44");    	
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
	        //socket.close();
	        
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


//登录确认消息结构体 （首部+body）
class KW_ProtoDev_LogIn_Ack{
	KW_Proto_Head kwHead;
	byte usRegStatus[];   //返回消息2byte
	
	//初始化
	public KW_ProtoDev_LogIn_Ack(KW_Proto_Head kwHead,byte usRegStatus[]){
		this.kwHead=kwHead;
		this.usRegStatus=usRegStatus;
	}
}



//请求心跳消息
class KW_ProtoDev_HeartBeat_Req{
	KW_Proto_Head kwHead;
	byte ucDeviceID[];
	byte usBatCap[];
	
	public KW_ProtoDev_HeartBeat_Req(KW_Proto_Head kwHead,byte ucDeviceID[],byte usBatCap[]){
		
		this.kwHead=kwHead;
		this.ucDeviceID=ucDeviceID;
		this.usBatCap=usBatCap;
	}
	
	
	
	//将数据包转化成字节数
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
		
		
		//发送字节流
		public void send(byte[] data){		
			 
			try {			
				 /*
		         * 向服务器端发送数据
		         */
		        // 1.定义服务器的地址、端口号、数据
		        InetAddress address = InetAddress.getByName("10.129.52.44");    	
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

//上传SensorInfo信息请求
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
	
	
	
	
	//将数据包转化成字节数
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
	
	
	//发送字节流
	public void send(byte[] data){		
		 
		try {			
			 /*
	         * 向服务器端发送数据
	         */
	        // 1.定义服务器的地址、端口号、数据
	        InetAddress address = InetAddress.getByName("10.129.52.44");    	
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

//gps信息体
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

//LBS信息体
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

//wifi位置信息
class WifiLocInfo_t{

	byte[]	uiDate;//获取wifi信息日期
	byte[]	uiTime;//获取wifi信息时间
	byte[]	ucAPMac;//AP MAC地址
	byte[]	ssSignalStrength;//wifi信号强度
	byte[]	cSsid;
	
	public WifiLocInfo_t(byte[]	uiDate,byte[]	uiTime,byte[]	ucAPMac,byte[]	ssSignalStrength,byte[]	cSsid){
		this.uiDate=uiDate;
		this.uiTime=uiTime;
		this.ucAPMac=ucAPMac;
		this.ssSignalStrength=ssSignalStrength;
		this.cSsid=cSsid;
		
	}
}

//G_SensorInfo_t信息体
class G_SensorInfo_t{
	byte[] 			uiStartDate;//获取sensor信息日期
	byte[]			uiStartTime;//获取sensor信息时间
	byte[]			uiEndDate;//获取sensor信息日期
	byte[]			uiEndTime;//获取sensor信息时间
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







