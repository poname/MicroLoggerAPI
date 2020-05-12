package micro.log.api.log;

import micro.log.api.messaging.MessagingProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Locale;

@Service
public class LogAPIImpl implements LogAPI {

    private final MessagingProvider messagingProvider;
    private final MessageSource messageSource;

    @Autowired
    public LogAPIImpl(MessagingProvider messagingProvider,
                      MessageSource messageSource) {
        this.messagingProvider = messagingProvider;
        this.messageSource = messageSource;
    }


    @Override
    public void createLog(Log log) {
        messagingProvider.addLogToQueue(log);
    }

    @Override
    public void foodCreateLog(Broker broker, Object food) {
        messagingProvider.addLogToQueue(
                new Log()
                        .setBroker(broker.getType())
                        .setBrokerId(broker.getId())
                        .setReference(LogReference.Food)
                        /*.setReferenceId(food.getId())*/
                        .setLevel(LogLevel.Info)
                        .setDate(new Date())
                        .setMessage(messageSource.getMessage("logs.food created", new Object[]{
                                        new Date(),
                                        broker.getName()
                                },
                                Locale.getDefault()))
        );
    }
}
