package com.jenkins.demo.rabbitmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * 消费者
 *
 * @author liuzeyanga
 * @Date 2021-02-23 10:11
 */
public class MyConsumer {

    private final static String EXCHANGE_NAME = "SIMPLE_EXCHANGE";
    private final static String QUEUE_NAME = "SIMPLE_QUEUE";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        // 连接ip
        factory.setHost("127.0.0.1");
        // 默认监听端口
        factory.setPort(5672);
        // 虚拟机
        factory.setVirtualHost("/");

        // 设置访问的用户
        factory.setUsername("guest");
        factory.setPassword("guest");

        // 建立连接
        Connection connection = factory.newConnection();
        // 创建消息通道
        Channel channel = connection.createChannel();

        // 声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "direct", true, false, null);

        // 声明队列
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        System.out.println("Waiting for message .....");

        // 绑定队列与交换机
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "lzy.best");

        // 创建消费者
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                // 处理消息
                String msg = new String(body, Charset.forName("UTF-8"));
                System.out.println("Received message:'" + msg + "'");
                System.out.println("ConsumerTag:'" + consumerTag + "'");
                System.out.println("DeliveryTag:'" + envelope.getDeliveryTag() + "'");
            }
        };

        // 开始获取消息
        channel.basicConsume(QUEUE_NAME, true, consumer);
    }
    
}
