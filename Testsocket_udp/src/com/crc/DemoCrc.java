package com.crc;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;

public class DemoCrc {
	
	byte[]Polynomial={0x04,(byte) 0xc1,0x1d,(byte) 0xb7};
	int crcTable[]=new int[256];
	
	public  static void main(String args[]){
		
		
		
		byte data[]=get_file_Data("f:/zwb.amr");
		
		DemoCrc tt= new DemoCrc();
		byte result[]= tt.Crc32Checksum(data);
		
		printHexString(result);
			
		
	}

		
	
	//初始化
	public DemoCrc() {
		int accum; 
		
		for(int i= 0; i < 256; i++) {
			accum = i << 24;
			for (int j= 0; j < 8; j++) {
				if ((accum&0x80000000) != 0) {					
					byte temp[]=intToByte((accum << 1));
					byte accum_bytes[]=new byte[4];
					
					for(int k=0;k<Polynomial.length;k++){
						accum_bytes[k]=(byte)(temp[k]^Polynomial[k]);
					}
					accum=byteToint(accum_bytes);
					
				} else {
					accum = accum << 1;
				}
			}
			crcTable[i] = accum;
		}		
		
	}
	
	
	public byte[] Crc32Checksum(byte[] data)  {
		int crc = 0xffffffff;
		int len=data.length;
		int i=0;
		while(len > 0) {
			// crc = (crc << 8) ^ crc_table[((crc >> 24) ^ *buf++) & 0xff];
			crc = (crc << 8) ^ crcTable[((crc>>24)^(int)(data[i]))&0xff];
			len--;
			i++;
		}
		return intToByte(crc);
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
	 
	 //int 转化成 byte
	 public static byte[] intToByte(int n) {
	        byte[] b = new byte[4];
	        b[0] = (byte) ((n >> 24)& 0xFF);
	        b[1] = (byte) ((n >> 16)& 0xFF);
	        b[2] = (byte) ((n >> 8)& 0xFF);
	        b[3] = (byte) (n&0xFF);
	        return b;
	   }	 
	
	 // 字节类型转成int类型
    public static int byteToint(byte b[]) {
        return b[3] & 0xff | (b[2] & 0xff) << 8 | (b[1] & 0xff) << 16
                | (b[0] & 0xff) << 24;
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
			
			//System.out.println(filereversebytes.length);
			int index=filereversebytes.length/1024;
			int lastbytes=fileallbytes.length%1024;
			
			//System.out.println("show now");
			//System.out.println(index);
			//System.out.println(lastbytes);
			//printHexString(filereversebytes);
			
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
	 


}
