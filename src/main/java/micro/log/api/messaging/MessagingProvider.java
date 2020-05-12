package micro.log.api.messaging;


import micro.log.api.log.Log;

public interface MessagingProvider {
    void sendMessage(String message);
    void addLogToQueue(Log log);
}
