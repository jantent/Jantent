package springboot.util;


import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * 检查端口是否开放
 *
 * @author tangj
 * @date 2018/4/15 15:33
 */
public class PortChecker {
    public static boolean checkPort(String ip,int port){
        return checkPort(ip,port,1000);
    }

    public static boolean checkPort(String ip,int port,int timeout){
        if (port<0){
            // 通常端口小于0时，该端口对应的服务应小于0
            return true;
        }
        String ipAddr = "127.0.0.1";
        if (null != ip){
            ipAddr = ip;
        }
        boolean invaild = false;
        try {
            Socket socket = new Socket();
            InetSocketAddress address = new InetSocketAddress(ipAddr,port);
            socket.connect(address,timeout);
            socket.close();
        }catch (Exception e){
            invaild = true;
        }
        return invaild;
    }

}
