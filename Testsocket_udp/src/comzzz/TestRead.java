package comzzz;
import  com.crc.*;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;



public class TestRead {	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		byte data[]=get_file_Data("f:/zwb.amr");
		
		//DemoCrc tt= new DemoCrc();
		//byte crc[]= tt.Crc32Checksum(data);
		
		//printHexString(data);
		
	}
	
	
	
	public static byte[] get_file_Data(String path){
		byte [] fileallbytes;
		byte [] filereversebytes = null;
		FileInputStream fis=null;	
		byte b[]=new byte[1024]; //一次读1024字节
		int n ;  //每次实际读取的字节数
		ByteArrayOutputStream bos=null;
		try {
			
			bos=new ByteArrayOutputStream();
			fis=new FileInputStream(path);
			
			while((n=fis.read(b))!=-1){
				//printHexString(b);				
				System.out.println(n);		
				bos.write(b, 0, n);			
			}
			
			fileallbytes=bos.toByteArray();
			
			//得到逆序字节数组
			filereversebytes=reversebytes(fileallbytes);
			
			System.out.println(filereversebytes.length);
			int index=filereversebytes.length/1024;
			int lastbytes=fileallbytes.length%1024;
			
			//System.out.println("show now");
			//System.out.println(index);
			//System.out.println(lastbytes);
			printHexString(fileallbytes);
			
			//return filereversebytes;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			
			try {
				fis.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return filereversebytes;
		
		
		
	}
	
	
	
	public static byte[] reversebytes(byte []b){
		byte reverse[]=new byte[b.length]; 
		int j=0;
		
		for(int i=b.length-1;i>=0;i--){
			
			reverse[j]=b[i];
			j++;
		}
		
		return reverse;
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
