package com.codehuan.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author ZhangHuan created on 2022/2/24 15:37
 * @version 1.0
 * @description 连接工厂创建信道工具类
 * @email: codehuan@163.com
 */
public class RabbitMqConnectionUtil {

    // 用户名
    private static final String USERNAME = "admin";
    // 密码
    private static final String PASSWORD = "admin";
    // host
    private static final String HOST = "192.168.118.128";

    public static Channel getChannel() throws Exception {
        // 创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);
        factory.setUsername(USERNAME);
        factory.setPassword(PASSWORD);
        // 通过工厂获取连接
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        return channel;
    }
}
