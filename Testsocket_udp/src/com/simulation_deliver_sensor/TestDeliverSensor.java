package com.simulation_deliver_sensor;


public class TestDeliverSensor {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String imei="357244070300000";
		//��½
		KW_ProtoDev_LogIn_Req Login_Req=new KW_ProtoDev_LogIn_Req(imei);				
		Login_Req.send();		
		
		//��������
		KW_ProtoDev_HeartBeat_Req heartBeat_Req=new KW_ProtoDev_HeartBeat_Req(imei);		
		heartBeat_Req.send();		
		
		//�ϴ�λ����Ϣ
		KW_ProtoDev_DeliverySensorInfo_Req dev_DeliverySensorInfo_Req=new KW_ProtoDev_DeliverySensorInfo_Req(imei);		
		dev_DeliverySensorInfo_Req.send();
		
	}

}
