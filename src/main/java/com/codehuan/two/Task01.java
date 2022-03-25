package com.codehuan.two;

import com.codehuan.utils.RabbitMqConnectionUtil;
import com.rabbitmq.client.Channel;

import java.util.Scanner;

/**
 * @author ZhangHuan created on 2022/2/24 23:08
 * @version 1.0
 * @description 生产者
 * @email: codehuan@163.com
 */
public class Task01 {

    // 队列名称
    private static final String QUEUE_NAME = "hello";

    /**
     * 发送大量消息
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMqConnectionUtil.getChannel();

        // 声明队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        // 从控制台中接收信息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println("发送消息完成：" + message);
        }
    }
}
