package com.kidwatch;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

public class test {
	
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

	public static void main(String[] args) {	
		
		byte protocal_version=0x03;
		byte message_type=0x01;
		byte message_length[]={0x00,0x32};
		
		String imei_str="357244070189645";
		byte imei[]=imei_str.getBytes();
		
		byte Language_Info[]={0x00,0x00,0x00,0x01};
		
		byte Feature_Info[]={0x00,0x00,0x00,0x01};
		byte test[]={};
		
		byte data[]=null;
		
		System.out.println(message_length.length);
		
		ByteArrayOutputStream bos=new ByteArrayOutputStream();
		
		try {
			bos.write(protocal_version);
			//bos.write(message_type);
			//bos.write(message_length);
			//bos.write(imei);
			//bos.write(Language_Info);
			//bos.write(Feature_Info);
			bos.write(test);
			data=bos.toByteArray();
			System.out.println(data.length);
			printHexString(data);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

}
