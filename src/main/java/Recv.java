import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import java.io.IOException;

public class Recv {

  private static final String QUEUE_NAME = "rabbitmq_sample";

  public static void main(String[] argv) throws Exception {
    ConnectionFactory connectionFactory = new ConnectionFactory();
    connectionFactory.setUsername("ycudoyez");
    connectionFactory.setPassword("wP4zaI_pvwfob4UOPc4FGgxkyf-oz_kC");
    connectionFactory.setHost("rhino.rmq.cloudamqp.com");
    connectionFactory.setVirtualHost("ycudoyez");
    Connection connection = connectionFactory.newConnection();
    Channel channel = connection.createChannel();
    channel.queueDeclare(QUEUE_NAME, false, false, false, null);

    Consumer consumer =
        new DefaultConsumer(channel) {
          @Override
          public void handleDelivery(
              String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
              throws IOException {
            String message = new String(body, "UTF-8");
            System.out.println("Received '" + message + "'");
          }
        };
    channel.basicConsume(QUEUE_NAME, true, consumer);
  }
}
