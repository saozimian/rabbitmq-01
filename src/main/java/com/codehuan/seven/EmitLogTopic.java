package com.codehuan.seven;

import com.codehuan.utils.RabbitMqConnectionUtil;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: ZhangHuan created on 2022/2/26 21:53
 * @Email: codehuan@163.com
 * @Version: 1.0
 * @Description: 生产者
 */
public class EmitLogTopic {
    //交换机名称
    public static final String EXCHANGE_NAME = "topic_name";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqConnectionUtil.getChannel();

        /**
         * 绑定关系
         * Q1-->绑定的是
         * 		中间带 orange带3个单词的字符串(*.orange.*)
         *
         * 	Q2-->绑定的是
         * 	最后一个单词是rabbit的3个单词(*.*.rabbit)
         *  第一个单词是lazy的多个单词(lazy.#
         */
        Map<String, String> bindingKeyMap = new HashMap<>();
        bindingKeyMap.put("quick.orange.rabbit", "被队列Q1Q2接收到");
        bindingKeyMap.put("lazy.orange.elephant", "被队列Q1Q2接收到");
        bindingKeyMap.put("quick.orange.fox", "被队列Q1接收到");
        bindingKeyMap.put("lazy.brown.fox", "被队列Q2接收到");
        bindingKeyMap.put("lazy.pink.rabbit", "虽然满足两个绑定但只被队列Q2接收一次");
        bindingKeyMap.put("quick.brown.fox", "不匹配任何绑定不会被任何队列接收到会被丢弃");
        bindingKeyMap.put("quick.orange.male.rabbit", "是四个单词不匹配任何绑定会被丢弃");
        bindingKeyMap.put("lazy.orange.male.rabbit", "是四个单词但匹配Q2");

        for (Map.Entry<String, String> stringEntry : bindingKeyMap.entrySet()) {
            String routingKey = stringEntry.getKey();
            String message = stringEntry.getValue();
            channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println("生产者发出消息：" + message);
        }
    }
}
