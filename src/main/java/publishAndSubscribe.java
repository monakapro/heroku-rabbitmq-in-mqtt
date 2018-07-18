import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;

public class publishAndSubscribe {

  public static void main(String[] args) {
    // set up the connection
    CachingConnectionFactory connectionFactory=new CachingConnectionFactory("rhino.rmq.cloudamqp.com");
    connectionFactory.setUsername("ycudoyez");
    connectionFactory.setPassword("wP4zaI_pvwfob4UOPc4FGgxkyf-oz_kC");
    connectionFactory.setVirtualHost("ycudoyez");

    //Recommended settings
    connectionFactory.setRequestedHeartBeat(30);
    connectionFactory.setConnectionTimeout(30000);

    //Set up queue, exchanges and bindings
    RabbitAdmin admin = new RabbitAdmin(connectionFactory);
    Queue queue = new Queue("myQueue");
    admin.declareQueue(queue);
    TopicExchange exchange = new TopicExchange("myExchange");
    admin.declareExchange(exchange);
    admin.declareBinding(
        BindingBuilder.bind(queue).to(exchange).with("foo.*"));

    //Set up the listener
    SimpleMessageListenerContainer container =
        new SimpleMessageListenerContainer(connectionFactory);
    Object listener = new Object() {
      public void handleMessage(Byte[] foo) {
        System.out.println(foo);
      }
    };

    //Send a message
    MessageListenerAdapter adapter = new MessageListenerAdapter(listener);
    container.setMessageListener(adapter);
    container.setQueueNames("myQueue");
    container.start();

    RabbitTemplate template = new RabbitTemplate(connectionFactory);
    template.convertAndSend("myExchange", "foo.bar", "Hello CloudAMQP!");
    try{
      Thread.sleep(1000);
    } catch(InterruptedException e) {
      Thread.currentThread().interrupt();
    }
    container.stop();
  }
}
