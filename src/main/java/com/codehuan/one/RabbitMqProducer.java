package com.codehuan.one;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ZhangHuan created on 2022/2/23 16:58
 * @version 1.0
 * @description TODO
 * @email: codehuan@163.com
 */
public class RabbitMqProducer {

    // 队列名称
    private static final String QUEUE_NAME = "hello";
    // 用户名
    private static final String USERNAME = "admin";
    // 密码
    private static final String PASSWORD = "admin";
    // host
    private static final String HOST = "192.168.118.129";
    // port
    private static final int PORT = 5672;

    /**
     * 发送消息
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        // 1、创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();

        // 2、设置连接配置
        factory.setHost(HOST);
        factory.setPort(PORT);
        factory.setUsername(USERNAME);
        factory.setPassword(PASSWORD);
        factory.setVirtualHost("/");

        // 3、创建连接
        Connection connection = factory.newConnection();

        // 4、创建信道
        Channel channel = connection.createChannel();

        /**
         * 创建队列
         * 1、队列名称
         * 2、是否持久化队列（磁盘） 默认存储在内存
         * 3、是否独占本次连接，也就是是否进行消息共享，true表示可以多个消费者消费 false 只能一个消费者
         * 4、是否在不使用的时候自动删除队列,最后一个消费者断开连接以后 该队列是否自动删除 true 自动删除 false 不删除
         * 5、队列其他参数；
         */
        // channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        Map<String, Object> params = new HashMap();
        // 大小为 0-255
        params.put("x-max-priority", 10);
        channel.queueDeclare(QUEUE_NAME, true, false, false, params);
        for (int i = 1; i < 11; i++) {
            String message = "hello world" + i;
            if (i == 5) {
                AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().priority(5).build();
                channel.basicPublish("", QUEUE_NAME, properties, message.getBytes(StandardCharsets.UTF_8));
            } else {
                channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
            }
        }
        // 发送消息

        /**
         * 发送一个消费
         *  1、发送到哪个交换机
         *  2、路由的key值是哪一个 本次队列的名称
         *  3、其他参数信息
         *  4、发送消息的消息体 message
         */
        // channel.basicPublish("", QUEUE_NAME, null, message.getBytes());

        // // 5、关闭连接
        // channel.close();
        // connection.close();
        System.out.println("消息发送成功！！！");
    }
}
