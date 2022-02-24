package service;

import common.Message;
import common.MessageType;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * 文件发送服务类
 */
public class FileService {
    /**
     * 文件发送
     * @param sender 发送者
     * @param receiver 接收者
     * @param src 本地路径
     * @param destination 对方接收路径
     */
    public void privateFile(String sender,String receiver,String src,String destination){
        // 消息封装
        Message file = new Message();
        file.setMsgType(MessageType.MESSAGE_FILE);
        file.setSender(sender);
        file.setReceiver(receiver);
        file.setSrc(src);
        file.setDestination(destination);

        // 文件流处理
        FileInputStream fileInputStream = null;
        byte[] bytes = new byte[(int) new File(src).length()];

        try {
            fileInputStream = new FileInputStream(src);
            fileInputStream.read(bytes);
            file.setFileBytes(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println("\n"+sender+"("+src+")"+"--->"+receiver+"("+destination+")");

        // 消息发送
        try {
            ObjectOutputStream oos = new ObjectOutputStream(UserConnectionPool.getUserConnection(sender).getClientSocket().getOutputStream());
            oos.writeObject(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

