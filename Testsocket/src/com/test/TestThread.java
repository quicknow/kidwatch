package com.test;

import java.util.*;


public class TestThread {

	//public static String str;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<String> lists= new ArrayList<String>();
		lists.add("ping  192.168.0.1");
		lists.add("ping  192.168.0.2");
		
		 
		for(String s:lists){
			final String str=s;
			//System.out.println(s);
			new Thread(){
				public void run(){
					
					new cmddoc().docmddoc(str);
				}
			}.start();
			
			
		}
	}

}

class cmddoc{
	
	public void docmddoc(String str){
		System.out.println(str);
	}
}