package com.simulation_download_vd;

public class TestDownloadVoice {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String imei="857244070200002";
		KW_ProtoDev_LogIn_Req  login_req=new KW_ProtoDev_LogIn_Req(imei);		
		login_req.send();
		
		KW_ProtoDev_HeartBeat_Req heartBeat_Req=new KW_ProtoDev_HeartBeat_Req(imei);		
		heartBeat_Req.send();
		
		KW_ProtoDev_DL_VD_Ready_Req dl_VD_Ready_Req=new KW_ProtoDev_DL_VD_Ready_Req(imei);
		dl_VD_Ready_Req.send();
		
		//KW_ProtoDev_DL_VD_Req dl_VD_Req=new KW_ProtoDev_DL_VD_Req(imei);
		//dl_VD_Req.send();
		
		
	}

}
