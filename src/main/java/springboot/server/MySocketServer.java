package springboot.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class MySocketServer {
    private static final Logger logger = LoggerFactory.getLogger(MySocketServer.class);
    private ServerSocket serverSocket = null;
    private Thread thread = null;

    private void start() throws Exception {
        serverSocket = new ServerSocket(3480);
        try {
            Socket socket = serverSocket.accept();
            // 根据输入输出流和客户端连接
            // 得到输入流，接受客户端传递的信息
            InputStream inputStream = socket.getInputStream();
            //提高效率，将自己字节流转为字符流
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            // 加入缓冲区
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String acceptString;
            while ((acceptString = bufferedReader.readLine()) != null) {
                logger.info(acceptString);
                System.out.println("服务端接收到客户端信息：" + acceptString + ",当前客户端ip为：" + socket.getInetAddress().getHostAddress());
            }
            //获取一个输出流，向服务端发送信息
            OutputStream outputStream = socket.getOutputStream();
            //将输出流包装成打印流
            PrintWriter printWriter = new PrintWriter(outputStream);
            printWriter.print("你好，服务端已接收到您的信息");
            printWriter.flush();
            socket.shutdownOutput();//关闭输出流
            printWriter.close();
            outputStream.close();
            bufferedReader.close();
            inputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String args[]) {
        try {
            new MySocketServer().start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
