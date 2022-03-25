package com.codehuan.seven;

import com.codehuan.utils.RabbitMqConnectionUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * @Author: ZhangHuan created on 2022/2/26 21:38
 * @Email: codehuan@163.com
 * @Version: 1.0
 * @Description: 声明主题交换机 及相关队列
 * 消费者C1
 */
public class ReceiveLogsTopic01 {
    //交换机名称
    public static final String EXCHANGE_NAME = "topic_name";

    // 接收消息
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqConnectionUtil.getChannel();
        // 声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        // 声明队列
        String queueName = "Q1";
        channel.queueDeclare(queueName, false, false, false, null);
        // 绑定
        channel.queueBind(queueName, EXCHANGE_NAME, "*.orange.*");
        System.out.println("ReceiveLogsTopic01等待接收消息......");

        // 成功回调
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println(new String(message.getBody(), "UTF-8"));
            System.out.println("接收队列：" + queueName + " 绑定键：" + message.getEnvelope().getRoutingKey());
        };
        // 取消回调
        CancelCallback cancelCallback = (consumerTag) -> {

        };
        // 接收消息
        channel.basicConsume(queueName, true, deliverCallback, cancelCallback);
    }
}
