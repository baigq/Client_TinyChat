package service;

import java.util.HashMap;

/**
 * 用户连接线程池类
 */
public class UserConnectionPool {
    // HashMap 模拟 线程池
    private static HashMap<String,UserConnection> userConnections = new HashMap<>();

    /**
     * 将用户连接加入线程池
     * @param userID 用户名
     * @param userConnection 用户连接对象
     */
    public static void addUserConnection(String userID,UserConnection userConnection){
        userConnections.put(userID,userConnection);
    }

    /**
     * 通过用户名获取对应连接对象
     * @param userID 用户名
     * @return 对应的用户连接
     */
    public static UserConnection getUserConnection(String userID){
        return userConnections.get(userID);
    }
}
