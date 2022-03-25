package com.codehuan.five;

import com.codehuan.utils.RabbitMqConnectionUtil;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @Author: ZhangHuan created on 2022/2/26 20:55
 * @Email: codehuan@163.com
 * @Version: 1.0
 * @Description: TODO
 */
public class EmitLog {

    // 交换机名称
    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqConnectionUtil.getChannel();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println("生产者发出消息："+message);
        }
    }
}
