package view;

import java.util.Scanner;
import service.FileService;
import service.MessageService;
import service.UserService;

/**
 * 界面实现类
 */
public class ChatView {
    private boolean loop = true;
    private String mode = "";
    private UserService userService = new UserService();
    private MessageService messageService = new MessageService();
    private FileService fileService = new FileService();

    public static void main(String[] args) {
        new ChatView().mainMenu();
    }

    /**
     * 主界面
     */
    private void mainMenu(){
        while(loop){
            System.out.println("==========  欢迎登陆  ==========");
            System.out.println("\t\t 1 登陆系统");
            System.out.println("\t\t 9 退出系统");
            System.out.print("Choose Function:");
            mode = new Scanner(System.in).next();

            // 登录
            switch (mode){
                case "1":
                    System.out.print("User Name:");
                    String userID = new Scanner(System.in).next();
                    System.out.print("Password:");
                    String passwd = new Scanner(System.in).next();

                    if(userService.login(userID,passwd)){
                        System.out.println("========== Welcome "+userID+" ==========");
                        while(loop){
                            System.out.println("========== Second Menu "+userID+" ==========");
                            System.out.println("\t\t 1 在线用户");
                            System.out.println("\t\t 2 群发消息(Only Online Users)");
                            System.out.println("\t\t 3 私聊消息");
                            System.out.println("\t\t 4 发送文件");
                            System.out.println("\t\t 9 退出系统");
                            System.out.print("Choose Function:");
                            mode = new Scanner(System.in).next();

                            // 功能选择
                            switch (mode){
                                case "1":
                                    userService.getOnlineList();
                                    break;
                                case "2":
                                    System.out.println("Message to all:");
                                    String toALL = new Scanner(System.in).next();
                                    messageService.publicMessage(userID,toALL);
                                    break;
                                case "3":{
                                    System.out.println("To:");
                                    String UID = new Scanner(System.in).next();
                                    System.out.println("Message:");
                                    String content = new Scanner(System.in).next();
                                    messageService.privateMessage(userID,UID,content);
                                    break;
                                }
                                case "4":{
                                    System.out.println("To:");
                                    String UID = new Scanner(System.in).next();
                                    System.out.println("src:");
                                    String src = new Scanner(System.in).next();
                                    System.out.println("dest:");
                                    String dest = new Scanner(System.in).next();
                                    fileService.privateFile(userID,UID,src,dest);
                                    break;
                                }
                                case "9":
                                    userService.logOut();
                                    loop = false;
                                    break;
                            }
                        }
                    } else{
                        System.out.println("登陆失败");
                    }
                    break;
                case "9":
                    loop = false;
                    break;
            }
        }
    }
}

