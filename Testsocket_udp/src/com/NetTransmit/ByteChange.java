/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.NetTransmit;

/**
 *
 * @author Administrator
 */
public class ByteChange {
    //将int存入byte数组,使用小端模式
    public static void intToByte(byte b[],int start,int num){
        for(int i=start;i<start+4;i++){
            b[i]=(byte)num;
            num>>>=8;
        }
    }
    //从byte数组中提取int,使用小端模式
    public static int byteToInt(byte b[],int start){
        int num=0;
        for(int i=3+start;i>=start;i--){
            num=num<<8;
            num+=(0xff&(int)b[i]);
        }
        return num;
    }
    //将short存入byte数组,使用小端模式
    public static void shortToByte(byte b[],int start,short num){
        for(int i=start;i<start+2;i++){
            b[i]=(byte)num;
            num>>>=8;
        }
    }
    //从byte数组中提取short,使用小端模式
    public static short byteToShort(byte b[],int start){
        short num=0;
        for(int i=1+start;i>=start;i--){
            num=(short)(num<<8);
            num+=(0xff&(int)b[i]);
        }
        return num;
    }
    //将String填充入数组
    public static void StringToByte(String str,byte b[],int start,int length){
        byte c[]=str.getBytes();
        for(int i=0;i<length;i++){
            b[start+i]=c[i];
        }
    }
    //清空数组
    public static void cleanByte(byte b[]){
        for(int i=0;i<b.length;i++){
            b[i]=0;
        }
    }
    public static void main(String args[]){
        byte buffer[]=new byte[2000];
        for(int i=0;i<1000;i++){
            shortToByte(buffer,i*2,(short)i);
        }
        for(int i=0;i<1000;i++){
            System.out.print(byteToShort(buffer,i*2)+"  ");
            if(i!=0&&i%30==0){
                System.out.println();
            }
        }
    }
}
