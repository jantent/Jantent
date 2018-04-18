package springboot.client;

import java.io.*;
import java.net.Socket;

public class SocketClient {
    public static void start() throws Exception {
        Socket socket = new Socket("127.0.0.1", 3480);
        // 获取一个输出流，向服务端发送消息
        OutputStream outputStream = socket.getOutputStream();
        // 将输出流包装成打印流
        PrintWriter printWriter = new PrintWriter(outputStream);
        printWriter.print("hello");
        printWriter.flush();
        socket.shutdownOutput();

        // 获取一个输入流，接受服务端的消息
        InputStream inputStream = socket.getInputStream();
        // 包装成字符流 提高效率
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        // 增加缓冲区
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String acceptString;
        while ((acceptString = bufferedReader.readLine()) != null) {
            System.out.println("客户端接受到返回消息" + acceptString);
        }
        //关闭相对应的资源
        bufferedReader.close();
        inputStream.close();
        printWriter.close();
        outputStream.close();
        socket.close();
    }

    public static void main(String args[]) throws Exception {
        start();
    }
}
