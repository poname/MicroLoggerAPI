package micro.log.api.log;

public class Broker {
    String id;
    String name;
    BrokerType type;

    public String getId() {
        return id;
    }

    public Broker setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Broker setName(String name) {
        this.name = name;
        return this;
    }

    public BrokerType getType() {
        return type;
    }

    public Broker setType(BrokerType type) {
        this.type = type;
        return this;
    }
}
