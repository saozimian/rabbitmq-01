package com.codehuan.eight;

import com.codehuan.utils.RabbitMqConnectionUtil;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;

/**
 * @Author: ZhangHuan created on 2022/2/26 23:33
 * @Email: codehuan@163.com
 * @Version: 1.0
 * @Description: 死信队列
 * 生产者代码
 */
public class Producer {
    // 普通交换机名称
    public static final String NORMAL_EXCHANGE_NAME = "normal_exchange";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqConnectionUtil.getChannel();
        // 死信消息 设置TTL时间 10ms 10000
        AMQP.BasicProperties properties =
                new AMQP.BasicProperties()
                        .builder().expiration("10000").build();
        for (int i = 1; i < 11; i++) {
            String message = "info" + i;
            channel.basicPublish(NORMAL_EXCHANGE_NAME, "zhangsan", properties, message.getBytes());
            System.out.println("发送消息：" + message);
        }
    }
}
