package com.codehuan.five;

import com.codehuan.utils.RabbitMqConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * @Author: ZhangHuan created on 2022/2/26 20:43
 * @Email: codehuan@163.com
 * @Version: 1.0
 * @Description: 消息接收
 */
public class ReceiveLogs01 {

    // 交换机名称
    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqConnectionUtil.getChannel();
        // 声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        // 声明一个临时队列
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, EXCHANGE_NAME, "");
        System.out.println("ReceiveLogs01等待接收消息，打印消息！！");
        // 声明 接收消息回调
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("ReceiveLogs01接收到的message：" + new String(message.getBody(), "UTF-8"));
        };
        // 取消
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });
    }
}
