package com.jenkins.demo.rabbitmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;


/**
 * @author liuzeyanga
 * @Date 2021-02-23 15:13
 */
public class DlxProducer {

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri("amqp://guest:guest@127.0.0.1:5672");

        // 创建连接
        Connection connection = factory.newConnection();

        // 创建消息通道
        Channel channel = connection.createChannel();

        String msg = "2222222Hello Rabbit MQ,DLX MSG";

        // 设置属性，消息10秒钟过期
        AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                .deliveryMode(2) // 持久化消息
                .contentEncoding("UTF-8")
                .expiration("10000") // TTL
                .build();

        // 发送消息
        channel.basicPublish("GP_ORI_USE_EXCHANGE", "GP_ORI_USE_QUEUE", properties, msg.getBytes());

        channel.close();
        connection.close();

    }

}
