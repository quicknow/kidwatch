package com.kidwatch;

import java.io.*;

import java.net.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class TestSentBytes {
	public static void main(String args[]) {
		try {
			Socket socket = new Socket("127.0.0.1", 6010);

			// 由Socket对象得到输出流
			DataOutputStream os = new DataOutputStream(socket.getOutputStream());

			// 由Socket对象得到输入流，并构造相应的BufferedReader对象
			BufferedReader is = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			String readline = "{\"cmd\":\"login\",\"username\":\"ceshi\",\"code\":\"123456\"}";

			byte[] dataBuf = readline.getBytes();
			int len = dataBuf.length;

			ByteBuffer dataLenBuf = ByteBuffer.allocate(4);
			dataLenBuf.order(ByteOrder.LITTLE_ENDIAN);
			dataLenBuf.putInt(0, len);
			
			ByteBuffer sendBuf = ByteBuffer.allocate(len + 4);
			sendBuf.order(ByteOrder.LITTLE_ENDIAN);
			sendBuf.put(dataLenBuf.array(), 0, 4);
			sendBuf.put(dataBuf, 0, len);
			
			// 向120.24.60.164的6010端口发出客户请求
			os.write(sendBuf.array(), 0, len + 4);
			
			// 刷新输出流，使Server马上收到该字符串
			os.flush();
			System.out.println("response:" + is.readLine());

			os.close(); // 关闭Socket输出流
			is.close(); // 关闭Socket输入流
			socket.close(); // 关闭Socket
		} catch (Exception e) {
			System.out.println("Error: " + e); // 出错，则打印出错信息
		}
	}

}