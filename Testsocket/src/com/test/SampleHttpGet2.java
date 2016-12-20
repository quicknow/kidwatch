package com.test;

import java.io.IOException;  
import java.io.InputStream;  
import java.io.InputStreamReader;  
import java.io.LineNumberReader;  
import java.io.OutputStream;  
import java.net.Socket;  
import java.net.UnknownHostException;  
  
public class SampleHttpGet2 {  
    public static final String SEQUENCE = "\r\n";  
  
    public static void main(String[] args) throws UnknownHostException,  
            IOException {  
        String host = "www.renren.com";  
        Socket socket = new Socket(host, 80);  
        OutputStream os = socket.getOutputStream();  
        StringBuffer head = new StringBuffer();  
        // 这些是必须的  
        head.append("GET / HTTP/1.1" + SEQUENCE);  
        head.append("Host:" + host + SEQUENCE+SEQUENCE);   //此处一定要两个回车换行，否则会出错
        // 这些是可选的  
        head.append("Accept:text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8" + SEQUENCE);  
        head.append("Accept-Language:zh-CN,zh;q=0.8"+SEQUENCE);  
        head.append("User-Agent:Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11"+SEQUENCE);  
        

        os.write(head.toString().getBytes());  
        os.flush();  
        InputStream is = socket.getInputStream();  
        LineNumberReader lnr = new LineNumberReader(new InputStreamReader(is));  
        StringBuffer headRes = new StringBuffer();  
        String line = null;  
        int contentLength = 0;  
        do {  
            line = lnr.readLine();  
            headRes.append(line + SEQUENCE);  
            if (line.startsWith("Content-Length")) {  
                contentLength = Integer.parseInt(line.split(":")[1].trim());  
            }  
        // 由于LineNumberReader会把\r\n替换掉，所以如果读到一行为""证明http head结束  
        } while (!line.equals(""));  
          
        int totalCount = 0;  
        byte[] buff = new byte[256];  
        StringBuffer contentRes = new StringBuffer();  
        while (totalCount < contentLength) {  
            int len = is.read(buff);  
            totalCount += len;  
            contentRes.append(new String(buff, 0, len, "gbk"));  
        }  
        System.out.println(headRes.toString());  
        System.out.println(contentRes.toString());  
        socket.close();  
    }  
}