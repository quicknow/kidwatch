package com.simulation_deliver_sensor;

import java.util.Date; 
import java.util.Calendar;
import java.text.SimpleDateFormat;

public class TestGetTime {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Date now = new Date(); 
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//可以方便地修改日期格式


		String hehe = dateFormat.format( now ); 
		System.out.println(hehe);
		
		Calendar c = Calendar.getInstance();//可以对每个时间域单独修改	

		int year = c.get(Calendar.YEAR); 
		int month = c.get(Calendar.MONTH); 
		int date = c.get(Calendar.DATE); 
		int hour = c.get(Calendar.HOUR_OF_DAY); 
		int minute = c.get(Calendar.MINUTE); 
		int second = c.get(Calendar.SECOND); 
		System.out.println(year + "/" + month + "/" + date + " " +hour + ":" +minute + ":" + second); 
		
		
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