package com.crc;

public class TestByte {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		//byte i=0x01;
		
		byte j=0x08;
		
		for(byte i=0x01;i<0xFD;i=(byte) (i+0x01)){
			String hex=Integer.toHexString(i);
			if(hex.length()==1){
				
				hex='0'+hex;
			}
			System.out.print(hex+" ");
			
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
