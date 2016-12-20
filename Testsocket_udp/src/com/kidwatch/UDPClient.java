package com.kidwatch;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/*
 * �ͻ���
 */
public class UDPClient {
    public static void main(String[] args) throws IOException {
        /*
         * ��������˷�������
         */
        // 1.����������ĵ�ַ���˿ںš�����
        InetAddress address = InetAddress.getByName("localhost");
        int port = 8800;
        
        StringBuffer head = new StringBuffer();
        head.append("3");  
        head.append("1");
        head.append("length");
        
        
        
        // 2.�������ݱ����������͵�������Ϣ
        byte[] data=head.toString().getBytes();        
        
        DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
        // 3.����DatagramSocket����
        DatagramSocket socket = new DatagramSocket();
        // 4.��������˷������ݱ�
        socket.send(packet);

        /*
         * ���շ���������Ӧ������
         */
        // 1.�������ݱ������ڽ��շ���������Ӧ������
        byte[] data2 = new byte[1024];
        DatagramPacket packet2 = new DatagramPacket(data2, data2.length);
        // 2.���շ�������Ӧ������
        socket.receive(packet2);
        // 3.��ȡ����
        String reply = new String(data2, 0, packet2.getLength());
        System.out.println("���ǿͻ��ˣ�������˵��" + reply);
        // 4.�ر���Դ
        socket.close();
    }
}