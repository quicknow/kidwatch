package com.test;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

public class test4 {

 public static void main(String[] args) {
  try {
	  String hostname = "www.renren.com";// 主机，可以是域名，也可以是ip地址 103.235.252.213
      int port = 80;// 端口 
      InetAddress addr = InetAddress.getByName(hostname); 
      System.out.println("addr="+addr);
      
   Socket socket = new Socket(addr, port);
   BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
   StringBuffer buffer = new StringBuffer();
   buffer.append("GET / HTTP/1.1\r\n");
   buffer.append("Host: www.renren.com\r\n");   //注意这里需要两个回车换行，否则会出错
   buffer.append("Connection: keep-alive\r\n");
   buffer.append("Upgrade-Insecure-Requests: 1\r\n");   
   buffer.append("User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.87 Safari/537.36\r\n");
   buffer.append("Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8\r\n");
   buffer.append("Accept-Encoding: gzip, deflate, sdch\r\n");
   buffer.append("Accept-Language: zh-CN,zh;q=0.8\r\n");
   buffer.append("\r\n");   // 注，这是关键的关键，忘了这里让我搞了半个小时。这里一定要一个回车换行，表示消息头完，不然服务器会等待
   writer.write(buffer.toString());
   writer.flush();

   // --输出服务器传回的消息的头信息
   BufferedReader reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
   String line = null;
   StringBuilder builder=new StringBuilder();
   while((line=reader.readLine())!=null){
    builder.append(line+"\r\n");
   }
   String result=builder.toString();
   System.out.println("result="+result);
   socket.close();
  } catch (Exception e) {
   e.printStackTrace();
  }
 }

}
