package com.simulation_deliver_sensor;

public class DoubleToHex {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		System.out.println(Float.intBitsToFloat(Integer.parseInt("42f08000", 16)));
//		
//		Float f=0.15490197f;  Double d=110.2 ;
//	    System.out.println(Integer.toHexString(Float.floatToIntBits(f)));
//	    
//	    System.out.println(Float.floatToIntBits(f));
//	    
//	    
//	    System.out.println((Double.doubleToLongBits(d)));
//	    
//	    System.out.println(Double.toHexString(112.2));
		
		//double转化成十六进制
		long l = Double.doubleToLongBits(59.23);
		byte [] b = new byte[8]; 
		b[0] = (byte) (l & 0x000000000000FFL); 
		b[1] = (byte) ((l & 0x0000000000FF00L) >> 8); 
		b[2] = (byte) ((l & 0x0000000000FF0000L) >> 16); 
		b[3] = (byte) ((l & 0x00000000FF000000L) >> 24); 
		b[4] = (byte) ((l & 0x000000FF00000000L) >> 32); 
		b[5] = (byte) ((l & 0x0000FF0000000000L) >> 40); 
		b[6] = (byte) ((l & 0x00FF000000000000L) >> 48); 
		b[7] = (byte) ((l & 0xFF00000000000000L) >> 56);
		
		printHexString(b);
	    
	    	    
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
