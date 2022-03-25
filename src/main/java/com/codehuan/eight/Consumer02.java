package com.codehuan.eight;

import com.codehuan.utils.RabbitMqConnectionUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.util.HashMap;

/**
 * @Author: ZhangHuan created on 2022/2/26 23:10
 * @Email: codehuan@163.com
 * @Version: 1.0
 * @Description: 死信队列 实战
 * 消费者02
 */
public class Consumer02 {

    // 死信队列名称
    public static final String DEAD_QUEUE_NAME = "dead_queue";

    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMqConnectionUtil.getChannel();

        System.out.println("等待接收消息......");

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("Consumer02接收的message是：" + new String(message.getBody(), "UTF-8"));
        };
        channel.basicConsume(DEAD_QUEUE_NAME, true, deliverCallback, consumerTag -> {
        });
    }
}
