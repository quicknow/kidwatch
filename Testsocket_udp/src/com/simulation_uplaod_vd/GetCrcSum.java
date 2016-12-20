package com.simulation_uplaod_vd;

public class GetCrcSum {

	byte[]Polynomial={0x04,(byte) 0xc1,0x1d,(byte) 0xb7};
	int crcTable[]=new int[256];	
	
	public void genCrcTable() {
		int accum; 
		
		for(int i= 0; i < 256; i++) {
			accum = i << 24;
			for (int j= 0; j < 8; j++) {
				if ((accum&0x80000000) < 0) {					
					byte temp[]=intToByte((accum << 1));
					byte accum_bytes[]=new byte[4];
					
					for(int k=0;i<Polynomial.length;k++){
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


}
