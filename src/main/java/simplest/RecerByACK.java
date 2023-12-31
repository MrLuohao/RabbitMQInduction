package simplest;

import com.rabbitmq.client.*;
import util.ConnectionUtil;

import java.io.IOException;

/**
 * @Title: Recer
 * @Author Mr.罗
 * @Package simplest
 * @Date 2023/11/27 18:04
 * @description: 消息接收者, 通过ACK确认机制
 */
public class RecerByACK {
    public static void main(String[] args) throws Exception {
        //1.获取连接
        Connection connection = ConnectionUtil.getConnection();
        //2.获得通道（信道）
        final Channel channel = connection.createChannel();
        //3.从信道中获得消息
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override//交付处理(收件人信息,包裹上的快递标签,协议的配置,消息)
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                //body就是从队列中获取的消息
                String s = new String(body);
                System.out.println("接收 = " + s);
                //手动确认（收件人信息，是否同时确认多个信息）
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        };
        //4.监听队列 false:手动消息确认
        channel.basicConsume("queue1", false, consumer);
    }
}
