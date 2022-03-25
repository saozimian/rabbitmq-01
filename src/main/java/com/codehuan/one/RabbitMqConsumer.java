package com.codehuan.one;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author ZhangHuan created on 2022/2/23 16:59
 * @version 1.0
 * @description TODO
 * @email: codehuan@163.com
 */
public class RabbitMqConsumer {

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
     * 消费者 接收消息
     *
     * @param args
     * @throws IOException
     * @throws TimeoutException
     * @throws InterruptedException
     */
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        // 封装 HOST PORT
        Address[] addresses = new Address[]{
                new Address(HOST, PORT)
        };

        // 1、创建工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername(USERNAME);
        factory.setPassword(PASSWORD);

        // 2、创建连接 这里的连接方式与生产者的demo略有不同，注意区分
        Connection connection = factory.newConnection(addresses);

        // 3、创建信道
        Channel channel = connection.createChannel();


        // 声明 接收消息回调
        DeliverCallback deliverCallback = (consumerTag, message) -> System.out.println("message=" + new String(message.getBody()));

        // 取消消息回调
        CancelCallback cancelCallback = consumerTag -> System.out.println("消息消费被中断");
        /**
         *  4、消费者消费消息
         *     1、消费哪个队列
         *     2、消费成功之后是否要自动应答， true 代表自动应答 false 代表手动应答
         *     3、消费者未成功消费的回调
         *     4、消费者取消消费的回调
         */
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);

        // // 5、关闭连接
        // channel.close();
        // connection.close();

    }
}
