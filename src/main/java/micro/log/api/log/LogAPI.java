package micro.log.api.log;

public interface LogAPI {
    void createLog(Log log);
    void foodCreateLog(Broker broker, Object food);
}
