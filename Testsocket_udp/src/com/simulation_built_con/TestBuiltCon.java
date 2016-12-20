package com.simulation_built_con;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class TestBuiltCon {
	static ArrayList<KW_ProtoDev_HeartBeat_Req> al;
	

	public static void main(String[] args) {
		//ArrayList<KW_ProtoDev_LogIn_Req> kidwatchs_list=new ArrayList<KW_ProtoDev_LogIn_Req>();
		//按顺序生生成一个固定长度的imei号,并启动设备
		CountDownLatch latch=new CountDownLatch(10);
		System.out.println("开始的时间："+System.currentTimeMillis());
		al=new ArrayList<KW_ProtoDev_HeartBeat_Req>();
    	for(int i=0;i<10;i++){    		
    		String str=String.format("3572440703000"+"%02d", i);
    		//System.out.println(str);
    		//kidwatchs_list.add(new KW_ProtoDev_LogIn_Req(str));
    		KW_ProtoDev_LogIn_Req log_req=new KW_ProtoDev_LogIn_Req(str);
    		log_req.send();
    		
    		KW_ProtoDev_HeartBeat_Req heartBeat_Req=new KW_ProtoDev_HeartBeat_Req(str,latch);    		
    		heartBeat_Req.start();  //不停发心跳包   
    		
    		al.add(heartBeat_Req);
    		
    	}
    	
    	try {
			latch.await();  //等线程执行完毕
			System.out.println("结束的时间："+System.currentTimeMillis());
			for(int i=0;i<al.size();i++){
				int times=al.get(i).time;
				System.out.println(al.get(i).imei+"运行次数："+times);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    	
    			
		
		// TODO Auto-generated method stub
		//登陆
  //  KW_ProtoDev_LogIn_Req Login_Req=new KW_ProtoDev_LogIn_Req("857244070200002");			
	//Login_Req.send();
		
		//发送心跳请求
//		KW_ProtoDev_HeartBeat_Req heartBeat_Req=new KW_ProtoDev_HeartBeat_Req("357244070189645");		
//		heartBeat_Req.send();
//		
//		KW_ProtoDev_LogIn_Req logIn_Req=new KW_ProtoDev_LogIn_Req("857244070200002");
//		Thread tt = new Thread(logIn_Req);
//		tt.start();
		//发送登出请求
		//KW_ProtoDev_Logout_Req logout_Req=new KW_ProtoDev_Logout_Req("357244070189645");		
		//logout_Req.send();
	}
	
	
	


}
