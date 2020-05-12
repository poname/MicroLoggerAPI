package micro.log.api.messaging;

import micro.log.api.log.Log;
import com.rabbitmq.client.ShutdownSignalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQProvider implements MessagingProvider {

    private final RabbitTemplate rabbitTemplate;
    private final String EXCHANGE_NAME;
    private final String ROUTING_KEY;
    private final String LOG_EXCHANGE;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public RabbitMQProvider(@Value("${mp.broker.exchange}") String EXCHANGE_NAME,
                            @Value("${mp.broker.key}") String ROUTING_KEY,
                            @Value("${mp.log.exchange}") String LOG_EXCHANGE,
                            final RabbitTemplate rabbitTemplate) {

        this.EXCHANGE_NAME = EXCHANGE_NAME;
        this.ROUTING_KEY = ROUTING_KEY;
        this.LOG_EXCHANGE = LOG_EXCHANGE;

        this.rabbitTemplate = rabbitTemplate;

        this.rabbitTemplate.getConnectionFactory().addConnectionListener(new ConnectionListener() {
            @Override
            public void onCreate(Connection connection) {
                logger.info("onCreate");
            }

            @Override
            public void onClose(Connection connection) {
                logger.info("onClose");
            }

            @Override
            public void onShutDown(ShutdownSignalException signal) {
                logger.info("onShutDown : {}", signal.getCause().getMessage());
            }
        });

    }

    @Override
    public void sendMessage(String message) {
        try {
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, message);
        } catch (AmqpException e) {
            logger.error("MessagingProvider sendMessage failed, {}", e.getMessage());
        }
    }

    @Override
    public void addLogToQueue(Log log) {
        try {
            rabbitTemplate.convertSendAndReceive(LOG_EXCHANGE, "", log);
        } catch (AmqpException e) {
            logger.error("RabbitMQProvider addLogToQueue() failed, log: {}, error: {}", log, e.getMessage());
        }
    }


}
