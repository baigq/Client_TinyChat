package service;

import common.Message;
import common.MessageType;
import common.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * 用户服务类
 */
public class UserService {
    // 用户类
    private User user = new User();
    // 客户端socket对象
    private Socket clientSocket;

    /**
     * 用户登录
     * @param userID 用户名
     * @param password 密码
     * @return 是否登录成功
     */
    public boolean login(String userID, String password){
        // 是否登陆成功
        boolean isSuccess = false;

        // 用户信息封装
        user.setUserID(userID);
        user.setPassword(password);

        try {
            // 连接到Server,得到socket
            clientSocket = new Socket(InetAddress.getByName("127.0.0.1"),9999);
            // 通过输出流发送用户信息到Server
            ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
            oos.writeObject(user);
            // 通过输入流接收Server响应
            ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
            Message msg = (Message) ois.readObject();
            // 解析响应
            if(msg.getMsgType().equals(MessageType.MESSAGE_LOGIN_SUCCEED)) {
                // Create a new Thread to keep Connection
                UserConnection userConnection = new UserConnection(clientSocket);
                userConnection.start();
                // Add to the Thread Pool
                UserConnectionPool.addUserConnection(userID,userConnection);
                isSuccess = true;
            } else {
                // 登陆失败,关闭连接
                clientSocket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isSuccess;
    }

    /**
     * 查看在线用户
     */
    public void getOnlineList() {
        Message msg = new Message();
        msg.setMsgType(MessageType.MESSAGE_GET_ONLINE_LIST);

        try {
            // 从线程池中取出当前用户线程,得到输出流
            ObjectOutputStream oos = new ObjectOutputStream(
                    UserConnectionPool.getUserConnection(user.getUserID()).getClientSocket().getOutputStream());
            // 通过输出流发送请求
            oos.writeObject(msg);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用户登出
     */
    public void logOut(){
        // 登出请求
        Message msg = new Message();
        msg.setMsgType(MessageType.MESSAGE_CLIENT_EXIT);
        msg.setSender(user.getUserID());

        try {
            // 从线程池中取出当前用户线程,得到输出流
            ObjectOutputStream oos = new ObjectOutputStream(
                    UserConnectionPool.getUserConnection(user.getUserID()).getClientSocket().getOutputStream());
            // 通过输出流发送请求
            oos.writeObject(msg);

            System.out.println("EXIT... ");

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            // 退出主线程及其启动的线程
            System.exit(0);
        }

    }

}

