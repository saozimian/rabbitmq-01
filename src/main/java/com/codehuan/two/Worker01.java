package com.codehuan.two;

import com.codehuan.utils.RabbitMqConnectionUtil;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * @Author ZhangHuan created on 2022/2/24 22:42
 * @Version 1.0
 * @Description 这是一个工作线程，相当于之前的消费者
 * @Email codehuan@163.com
 */
public class Worker01 {

    // 队列名称
    private static final String QUEUE_NAME = "hello";

    /**
     * 接收消息
     */
    public static void main(String[] args) throws Exception {

        // 消息接收
        Channel channel = RabbitMqConnectionUtil.getChannel();

        // 声明 接收消息回调
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("message=" + new String(message.getBody()));
        };

        // 取消消息回调
        CancelCallback cancelCallback = consumerTag -> {
            System.out.println(consumerTag + "消息消费被中断!!!");
        };

        System.out.println("C2等待接收消息......");

        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
    }
}
