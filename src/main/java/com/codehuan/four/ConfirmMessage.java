package com.codehuan.four;

import com.codehuan.utils.RabbitMqConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;

import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @Author ZhangHuan created on 2022/2/26 0:40
 * @Email codehuan@163.com
 * @Version 1.0
 * @Description: 发布确认模式？
 * 1、单个确认
 * 2、批量确认
 * 3、异步确认
 */
public class ConfirmMessage {

    // 批量发消息的数量
    public static final int MESSAGE_COUNT = 1000;

    public static void main(String[] args) throws Exception {
        // 1、单个确认 发布1000个单独确认消息，耗时894ms
        // publishMessageIndividually();
        // 2、批量确认 发布1000个批量确认消息，耗时74ms
        // publishMessageBatch();
        // 3、异步确认 发布1000个异步确认消息，耗时70ms  发布1000个异步确认消息，耗时75ms
        publishMessageAsync();
    }

    /**
     * 异步确认
     */
    private static void publishMessageAsync() throws Exception {
        Channel channel = RabbitMqConnectionUtil.getChannel();
        // 队列声明
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, false, false, false, null);

        // 开启发布确认
        channel.confirmSelect();

        /**
         * 线程安全有序的一个哈希表 使用于高并发的情况下
         * 1、轻松的将序号与消息进行关联
         * 2、轻松的批量删除条目 只需要给到序号
         * 3、支持高并发（多线程）
         */
        ConcurrentSkipListMap<Long, String> outstandingConfirms = new ConcurrentSkipListMap<>();
        /**
         *  消息确认成功 回调函数
         *  1、消息的标记
         *  2、是否为批量确认
         */
        ConfirmCallback ackCallback = (deliveryTag, multiple) -> {
            if (multiple) {
                // 2、删除已经确认的消息，剩下的就是未确认的
                ConcurrentNavigableMap<Long, String> confirmed =
                        outstandingConfirms.headMap(deliveryTag);
                confirmed.clear();
            } else {
                outstandingConfirms.remove(deliveryTag);
            }

            System.out.println("确认的消息：" + deliveryTag);
        };

        /**
         *   消息确认失败 回调函数
         *  1、消息的标记
         *  2、是否为批量确认
         */
        ConfirmCallback nackCallback = (deliveryTag, multiple) -> {
            // 3、打印未确认的消息
            String message = outstandingConfirms.get(deliveryTag);
            System.out.println("未确认的消息：" + message + ", 未确认的消息tag：" + deliveryTag);
        };
        /**
         *   // 准备消息监听器 监听消息是否成功
         *   1、监听哪些消息成功
         *   2、监听哪些消息失败
         */
        channel.addConfirmListener(ackCallback, nackCallback);
        // 开始时间
        long begin = System.currentTimeMillis();

        //批量发送消息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes());
            // 1、记录所有发布消息的数量
            outstandingConfirms.put(channel.getNextPublishSeqNo(), message);
        }

        // 结束时间
        long end = System.currentTimeMillis();

        //耗时
        System.out.println("发布" + MESSAGE_COUNT + "个异步确认消息，耗时" + (end - begin) + "ms");
    }

    /**
     * 批量确认
     *
     */
    private static void publishMessageBatch() throws Exception {
        Channel channel = RabbitMqConnectionUtil.getChannel();
        // 队列声明
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, false, false, false, null);

        // 开启发布确认
        channel.confirmSelect();
        // 开始时间
        long begin = System.currentTimeMillis();


        // 批量确认消息大小
        int batchSize = 1000;
        //批量发送消息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes());
            // 判断消息达到100条时，进行发布确认
            if (i % batchSize == 0) {
                // 发布确认
                channel.waitForConfirms(batchSize);
            }
        }
        // 结束时间
        long end = System.currentTimeMillis();

        //耗时
        System.out.println("发布" + MESSAGE_COUNT + "个批量确认消息，耗时" + (end - begin) + "ms");
    }

    /**
     * 单个确认
     *
     */
    private static void publishMessageIndividually() throws Exception {
        Channel channel = RabbitMqConnectionUtil.getChannel();
        // 队列声明
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, false, false, false, null);

        // 开启发布确认
        channel.confirmSelect();
        // 开始时间
        long begin = System.currentTimeMillis();

        //批量发送消息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes());
            // 单个消息马上信息发布确认
            boolean flag = channel.waitForConfirms();
            if (flag) {
                System.out.println("消息发送成功！！！");
            }
        }
        // 结束时间
        long end = System.currentTimeMillis();

        //耗时
        System.out.println("发布" + MESSAGE_COUNT + "个单独确认消息，耗时" + (end - begin) + "ms");
    }

}
