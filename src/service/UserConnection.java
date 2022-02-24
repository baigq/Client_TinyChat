package service;

import common.Message;
import common.MessageType;

import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * 用户连接类
 */
public class UserConnection extends Thread{
    // 客户端socket
    private Socket clientSocket;
    // 线程控制符
    private boolean loop = true;

    /**
     * 连接初始化
     * @param socket 客户端socket
     */
    public UserConnection(Socket socket) {
        this.clientSocket = socket;
    }

    /**
     * 通过连接获取socket
     * @return 该连接对应的socket
     */
    public Socket getClientSocket() {
        return clientSocket;
    }

    /**
     * 连接监听
     */
    @Override
    public void run() {
        while(loop){
            try {
                System.out.println("Connection established,Listening ...");
                Message msg = null;

                // 线程阻塞在此处,等待Server响应
                try {
                    ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
                    msg = (Message) ois.readObject();
                }catch (Exception e){
                    e.printStackTrace();
                    clientSocket.close();
                    loop = false;
                }

                // 解析响应
                switch (msg.getMsgType()){
                    case MessageType.MESSAGE_RET_ONLINE_LIST:
                        String[] onlineList = msg.getContent().split(" ");
                        System.out.println("\n ========== 当前在线 ==========");
                        for(int i = 0; i < onlineList.length;i++)
                            System.out.println("User : "+onlineList[i]);
                        break;
                    case MessageType.MESSAGE_COMMON:
                        System.out.println("\n" + msg.getReceiver()+" <--- "+msg.getSender()+" : "+msg.getContent());
                        break;
                    case MessageType.MESSAGE_PUBLIC:
                        System.out.println("\n" + "ALL"+" <--- "+msg.getSender()+" : "+msg.getContent());
                        break;
                    case MessageType.MESSAGE_FILE:
                        System.out.println("Receiving file(s)...");
                        FileOutputStream fileOutputStream = new FileOutputStream(msg.getDestination());
                        fileOutputStream.write(msg.getFileBytes());
                        fileOutputStream.close();
                        System.out.println("File received successfully!");
                        break;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

