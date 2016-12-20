package com.simulation_built_con;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class TestBuiltCon {
	static ArrayList<KW_ProtoDev_HeartBeat_Req> al;
	

	public static void main(String[] args) {
		//ArrayList<KW_ProtoDev_LogIn_Req> kidwatchs_list=new ArrayList<KW_ProtoDev_LogIn_Req>();
		//��˳��������һ���̶����ȵ�imei��,�������豸
		CountDownLatch latch=new CountDownLatch(10);
		System.out.println("��ʼ��ʱ�䣺"+System.currentTimeMillis());
		al=new ArrayList<KW_ProtoDev_HeartBeat_Req>();
    	for(int i=0;i<10;i++){    		
    		String str=String.format("3572440703000"+"%02d", i);
    		//System.out.println(str);
    		//kidwatchs_list.add(new KW_ProtoDev_LogIn_Req(str));
    		KW_ProtoDev_LogIn_Req log_req=new KW_ProtoDev_LogIn_Req(str);
    		log_req.send();
    		
    		KW_ProtoDev_HeartBeat_Req heartBeat_Req=new KW_ProtoDev_HeartBeat_Req(str,latch);    		
    		heartBeat_Req.start();  //��ͣ��������   
    		
    		al.add(heartBeat_Req);
    		
    	}
    	
    	try {
			latch.await();  //���߳�ִ�����
			System.out.println("������ʱ�䣺"+System.currentTimeMillis());
			for(int i=0;i<al.size();i++){
				int times=al.get(i).time;
				System.out.println(al.get(i).imei+"���д�����"+times);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    	
    			
		
		// TODO Auto-generated method stub
		//��½
  //  KW_ProtoDev_LogIn_Req Login_Req=new KW_ProtoDev_LogIn_Req("857244070200002");			
	//Login_Req.send();
		
		//������������
//		KW_ProtoDev_HeartBeat_Req heartBeat_Req=new KW_ProtoDev_HeartBeat_Req("357244070189645");		
//		heartBeat_Req.send();
//		
//		KW_ProtoDev_LogIn_Req logIn_Req=new KW_ProtoDev_LogIn_Req("857244070200002");
//		Thread tt = new Thread(logIn_Req);
//		tt.start();
		//���͵ǳ�����
		//KW_ProtoDev_Logout_Req logout_Req=new KW_ProtoDev_Logout_Req("357244070189645");		
		//logout_Req.send();
	}
	
	
	


}
