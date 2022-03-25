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
 * 消费者01
 */
public class Consumer01 {

    // 普通交换机名称
    public static final String NORMAL_EXCHANGE_NAME = "normal_exchange";
    // 死信交换机名称
    public static final String DEAD_EXCHANGE_NAME = "dead_exchange";
    // 普通队列名称
    public static final String NORMAL_QUEUE_NAME = "normal_queue";
    // 死信队列名称
    public static final String DEAD_QUEUE_NAME = "dead_queue";

    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMqConnectionUtil.getChannel();
        // 声明死信和普通交换机 类型为 direct
        channel.exchangeDeclare(NORMAL_EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        // 声明普通队列
        HashMap<String, Object> arguments = new HashMap<>();
        // 过期时间 10ms 可以由生产者去设置  生成者可以自由设置过期时间，消费者设置后，后续不能修改
        // arguments.put("x-message-ttl", 10000);
        // 正常队列设置死信交换机
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE_NAME);
        // 设置死信routingKey
        arguments.put("x-dead-letter-routing-key", "lisi");
        // 设置队列的长度限制
        // arguments.put("x-max-length", 6);
        channel.queueDeclare(NORMAL_QUEUE_NAME, false, false, false, arguments);

        //声明死信队列
        channel.queueDeclare(DEAD_QUEUE_NAME, false, false, false, null);

        // 绑定普通的交换机与普通队列
        channel.queueBind(NORMAL_QUEUE_NAME, NORMAL_EXCHANGE_NAME, "zhangsan");
        // 绑定死信的交换机与死信队列
        channel.queueBind(DEAD_QUEUE_NAME, DEAD_EXCHANGE_NAME, "lisi");
        System.out.println("等待接收消息......");

        // 回调
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            String msg = new String(message.getBody(), "UTF-8");
            if ("info5".equals(msg)) {
                System.out.println("Consumer01 接收的message是：" + message + ",此消息是被Consumer01拒绝的");
                channel.basicReject(message.getEnvelope().getDeliveryTag(), false);
            } else {
                System.out.println("Consumer01接收的message是：" + new String(message.getBody(), "UTF-8"));
                channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
            }
        };
        // 开启手动应答
        channel.basicConsume(NORMAL_QUEUE_NAME, false, deliverCallback, consumerTag -> {
        });
    }
}
