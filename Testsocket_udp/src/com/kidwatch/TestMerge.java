package com.kidwatch;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TestMerge {
	
	
	public void streamCopy(int arrays,int aryLen) {  
	    byte[] destAray = null;  
	    
	    ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
	    
	    byte[][] srcArrays = createSubArrays(arrays,aryLen);  
	    try {  
	        for (int i=0;i<arrays;i++) {  
	            bos.write(srcArrays[i]);          
	        }  
	        bos.flush();  
	        destAray = bos.toByteArray();  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    } finally {  
	        try {  
	            bos.close();  
	        } catch (IOException e) {  
	        }  
	    }  
	    //outArray(destAray);       
	}
	
	public byte[][] createSubArrays(int arrays,int aryLength) {  
	    byte bt = 0;  
	    byte[][] subarrays = new byte[arrays][];  
	    for (int i=0;i<arrays;i++) {  
	        subarrays[i] = new byte[aryLength];  
	        for(int j=0;j<aryLength;j++) {  
	            subarrays[i][j] = bt;  
	            bt++;  
	        }             
	    }  
	    return subarrays;  
	}

	
    public static byte[] byteMerger(byte[] byte_1, byte[] byte_2){  
        byte[] byte_3 = new byte[byte_1.length+byte_2.length];  
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);  
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);  
        return byte_3;  
    }  
	
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
		// TODO Auto-generated method stub
		byte[]b1={0x01};
		byte[]b2={0x12};
		byte[]b3={0x11,0x08};
		byte[]b4=byteMerger(byteMerger(b1, b2), b3);
		
	    byte ucProtoVer[]={0x01};  //版本
        byte ucProtoTag[]={0x01};  //类型
        byte uiProtoLen[]={0x00,0x10};       //参数长度
        byte ucDevID[]={0x00,0x03,0x05,0x07,0x04,0x04,0x00,0x07,0x00,0x01,0x08,0x09,0x06,0x04,0x05};                   //deviceid 16byte
        byte uiLanguage[]={0x00,0x00,0x01,0x00};                //language 4
        byte uiFeatrueList[]={0x00,0x00,0x00,0x01};             //FeatrueList 4
        
        //将所有字节数组 组成一个大字节数组
        byte[] data=byteMerger(byteMerger(byteMerger(byteMerger(byteMerger(ucProtoVer,ucProtoTag), uiProtoLen),ucDevID),uiLanguage),uiFeatrueList);  
		
		//printHexString(b4);
		//printHexString(data);
        byte[]f={(byte) 0xff};        
       
	}

}
