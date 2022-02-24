package service;

import common.Message;
import common.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;

/**
 * 发送消息服务类
 */
public class MessageService {
    /**
     * 单独聊天功能
     * @param sender 发送者
     * @param receiver 接收者
     * @param content 消息内容
     */
    public void privateMessage(String sender,String receiver,String content){
        // 消息封装
        Message msg = new Message();
        msg.setMsgType(MessageType.MESSAGE_COMMON);
        msg.setSender(sender);
        msg.setReceiver(receiver);
        msg.setContent(content);
        msg.setTime(new Date().toString());
        System.out.println(sender+" ---> "+receiver+" : "+content);

        // 消息发送
        try {
            ObjectOutputStream oos = new ObjectOutputStream(
                    UserConnectionPool.getUserConnection(sender).getClientSocket().getOutputStream());
            oos.writeObject(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 群发功能
     * @param sender 发送者
     * @param content 聊天内容
     */
    public void publicMessage(String sender,String content){
        // 消息封装
        Message msg = new Message();
        msg.setMsgType(MessageType.MESSAGE_PUBLIC);
        msg.setSender(sender);
        msg.setContent(content);
        msg.setTime(new Date().toString());
        System.out.println(sender+" ---> "+"ALL"+" : "+content);

        // 消息发送
        try {
            ObjectOutputStream oos = new ObjectOutputStream(
                    UserConnectionPool.getUserConnection(sender).getClientSocket().getOutputStream());
            oos.writeObject(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

