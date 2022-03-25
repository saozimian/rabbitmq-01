package com.codehuan.three;

import com.codehuan.utils.RabbitMqConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.util.Scanner;

/**
 * @Author ZhangHuan created on 2022/2/25 23:42
 * @Email codehuan@163.com
 * @Description 消息在手动应答时是不消失的，放回队列中重新消费
 * @Version 1.0
 */
public class Task2 {

    // 队列名称
    private static final String TASK_QUEUE_NAME = "ack_name";

    public static void main(String[] args) throws Exception {
        // 声明队列
        Channel channel = RabbitMqConnectionUtil.getChannel();
        // 让Queue持久化
        boolean durable = true;
        channel.queueDeclare(TASK_QUEUE_NAME, durable, false, false, null);
        // 开启发布确认
        channel.confirmSelect();

        // 从控制台输入信息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            // 设置生产者发送的消息为持久化消息（保存到磁盘上） 保存在内存中
            channel.basicPublish("", TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,
                    message.getBytes("UTF-8"));
            System.out.println("生产者发出消息：" + message);
        }
    }
}
