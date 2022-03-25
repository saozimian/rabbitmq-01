package com.codehuan.three;

import com.codehuan.utils.RabbitMqConnectionUtil;
import com.codehuan.utils.SleepUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * @Author ZhangHuan created on 2022/2/25 23:48
 * @Email codehuan@163.com
 * @Description 消息在手动应答时是不消失的，放回队列中重新消费
 * @Version 1.0
 */
public class Worker03 {

    // 队列名称
    private static final String TASK_QUEUE_NAME = "ack_name";

    // 接收消息
    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMqConnectionUtil.getChannel();
        System.out.println("C1等待接收消息时间较短！！！");

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            // 沉睡一秒
            SleepUtil.sleep(1);
            System.out.println("接收到的消息：" + new String(message.getBody(), "UTF-8"));
            // 手动应答
            /**
             * 参数说明
             * 1、消息的标记 tag
             * 2、是否批量应答 true 批量  false 不批量
             */
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
        };

        // 设置不公平分发
        // int prefetchCount = 1;
        // 预取值是2
        int prefetchCount = 2;
        channel.basicQos(prefetchCount);
        // 采用手动应答
        boolean autoAck = false;
        channel.basicConsume(TASK_QUEUE_NAME, autoAck, deliverCallback, (consumerTag -> {
            System.out.println(consumerTag + "消费者取消消费接口回调！！！");
        }));

    }
}
