/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.NetTransmit;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Administrator
 */
public class TestSend {
    public static void main(String args[]){
        FileInputStream fis=null;
        try {
            //参数分别是：对方的IP，我的端口号，对方的端口号
            //产生send后，就可以向接收方发送数据了
            //每次发送数据调用sendAll，详细说明见后
            SendLittle send = new SendLittle("localhost", 12000, 10000);
            fis = new FileInputStream("screen.jpg");
            final int littleLength=1024*1024;
            byte b[]=new byte[littleLength];
            while(fis.available()>0){
                int length=fis.read(b);
                //b是待发送的字节数组，length是要发送的长度
                //这个可以发送无限大的数据，当然length是整型数，还是有范围滴
                //sendAll对于等发送的数据，会进行分组，每次发送一个分组，而一个分组有
                //250个数据包，每个包的最大长度是1000。sendAll在成功发送一个分组后才
                //会发送下一个分组。
                send.sendAll(b, length);
            }
            //之前，你可能会感到疑惑，sendAll是用来发送数据的，那么，就如这个例子，文件
            //传输结束了，对方如何知道呢？
            //我这里就用了分层的思想，sendAll只是负责发送数据，它只是把待分送的数据
            //准确地发送给对方。receiveAll只是负责接收数据。
            //至于发送和接收的数据，有什么样的意义，它们是不管的。
            //什么时候发送结束，你可以把相应的信息存入sendAll发送的数组中,正如下面所做的
            String end="end";
            ByteChange.StringToByte(end, b, 0, 3);
            send.sendAll(b, 3);
            //接收方接收数据并存入相应的数组，然后先判断一下接收到的字节数，如果是3
            //再看看是不是和"end"匹配，如果匹配，就结束接收。
            //我这只是一个简单的示例程序，只是传输一个文件。如果你是传输多个文件，或者想实现
            //传输文件夹中所有内容的功能，那么你可以将文件名，是否是目录等信息全部存入待传输的
            //字节数组。接收方再按照一定的规则从字节数组中提取信息。我的sendAll只是一个最
            //基础的模块
            fis.close();
        } catch (IOException ex) {
            Logger.getLogger(TestReceive.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(TestReceive.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
