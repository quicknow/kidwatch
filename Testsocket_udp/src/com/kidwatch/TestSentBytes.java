package com.kidwatch;

import java.io.*;

import java.net.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class TestSentBytes {
	public static void main(String args[]) {
		try {
			Socket socket = new Socket("127.0.0.1", 6010);

			// ��Socket����õ������
			DataOutputStream os = new DataOutputStream(socket.getOutputStream());

			// ��Socket����õ�����������������Ӧ��BufferedReader����
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
			
			// ��120.24.60.164��6010�˿ڷ����ͻ�����
			os.write(sendBuf.array(), 0, len + 4);
			
			// ˢ���������ʹServer�����յ����ַ���
			os.flush();
			System.out.println("response:" + is.readLine());

			os.close(); // �ر�Socket�����
			is.close(); // �ر�Socket������
			socket.close(); // �ر�Socket
		} catch (Exception e) {
			System.out.println("Error: " + e); // �������ӡ������Ϣ
		}
	}

}