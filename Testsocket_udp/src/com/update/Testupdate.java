package com.update;

import java.util.ArrayList;

public class Testupdate{
	

	public static void main(String[] args) {
		//ArrayList<KW_ProtoDev_LogIn_Req> kidwatchs_list=new ArrayList<KW_ProtoDev_LogIn_Req>();
		//��˳��������һ���̶����ȵ�imei��,�������豸
//    	for(int i=0;i<10;i++){
//    		System.out.println(i);
//    		String str=String.format("3572440703000"+"%02d", i);
//    		//System.out.println(str);
//    		//kidwatchs_list.add(new KW_ProtoDev_LogIn_Req(str));
//    		KW_ProtoDev_LogIn_Req log_req=new KW_ProtoDev_LogIn_Req(str);
//    		log_req.send();
//    		KW_ProtoDev_HeartBeat_Req heartBeat_Req=new KW_ProtoDev_HeartBeat_Req(str);
//    		heartBeat_Req.start();  //��ͣ��������
//    	}
    	
    			
	   String imei="857244070200002";
		// TODO Auto-generated method stub
		//��½
       KW_ProtoDev_LogIn_Req Login_Req=new KW_ProtoDev_LogIn_Req(imei);			
	   Login_Req.send();
		
		//������������
	   KW_ProtoDev_HeartBeat_Req heartBeat_Req=new KW_ProtoDev_HeartBeat_Req(imei);		
	   heartBeat_Req.send();
		
	   KW_ProtoDev_Update_Ready_Req update_Ready_Req=new KW_ProtoDev_Update_Ready_Req(imei);
	   update_Ready_Req.send();
		
	
	}
	
	


}
