package com.codehuan.six;

import com.codehuan.utils.RabbitMqConnectionUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * @Author: ZhangHuan created on 2022/2/26 21:06
 * @Email: codehuan@163.com
 * @Version: 1.0
 * @Description: 发布订阅模式
 */
public class ReceiveLogsDirect02 {

    // 交换机名称
    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqConnectionUtil.getChannel();
        // 声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        channel.queueDeclare("disk", false, false, false, null);
        channel.queueBind("disk", EXCHANGE_NAME, "error");
        // 声明 接收消息回调
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("ReceiveLogsDirect01接收到的message：" + new String(message.getBody(), "UTF-8"));
        };
        // 取消
        channel.basicConsume("disk", true, deliverCallback, consumerTag -> {
        });
    }
}
