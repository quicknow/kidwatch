package com.simulation_uplaod_vd;

public class TestUploadVoice {
	
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub  357244070189645
		String imei="857244070200002";
		//��½
		KW_ProtoDev_LogIn_Req Login_Req=new KW_ProtoDev_LogIn_Req(imei);				
		Login_Req.send();
		
		//��������
		KW_ProtoDev_UL_VD_Ready_Req ul_VD_Ready_Req=new KW_ProtoDev_UL_VD_Ready_Req(imei);		
		ul_VD_Ready_Req.send();
		
		//������������
		KW_ProtoDev_UL_VD_Req req=new KW_ProtoDev_UL_VD_Req(imei,"f:/zwb.amr");
		req.send();
		
	}

}
