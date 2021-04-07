# Packet communications api.
Api for minecraft server network, connecting servers etc.
Based on redis (RedissonClient, maybe Jedis in future).
Not recommended for license systems etc.

# Additional information
If you use this api for minecraft server network etc, and you receive a big amount of heavy packets in packet handler, I recommend use a `PacketManager#registerAsyncPacketListener` method in registration.
If you find any issue please report it on github or write to me on discord `jerzyk#3904`

## Usage:
```java
        PacketManager packetManager = new PacketManager(Redisson.create(), new FastSerializationSerializer());
        packetManager.registerPacketListener(new PacketListener<Packet>(
                "channel", //listening channel
                packetManager.getRedisSerialization()) { //your redis serializer
            @Override
            protected void onPacketReceived(Packet received) {
                //code
                System.out.println("Received packet!");
            }
        });

        packetManager.sendPacket("channel", //channel to send packet
        packet); //your packet implementation
```

## Serializers
Create your own serializer. (Only serializer to byte-array)
```java

public final class YourSerializer implements RedisSerializer {

    @Override
    public byte[] serialize(Packet packet) {
       //code returning byte-array as packet
    }

    @Override
    public <T extends Packet> T deserialize(byte[] bytes) {
       //code returning packet from byte-array 
    }
}

```

## Async packet listeners.
`PacketManager#registerAsyncPacketListener`
```java 
        packetManager.registerAsyncPacketListener(new PacketListener<Packet>(
                "channel",
                packetManager.getRedisSerialization()) {
            @Override
            protected void onPacketReceived(Packet received) {
                //code
                System.out.println("Received packet in async!");
            }
        });
```
## Callback (responding/reply) function.
API makes easy responding to server request packets.
#### Ok but examples?
**Example packet:**
```java
public final class EnableServiceRequest extends PacketCallback {

    private final String serviceName;
    private boolean canEnable;

    public EnableServiceRequest(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setCanEnable(boolean canEnable) {
        this.canEnable = canEnable;
    }

    public boolean isCanEnable() {
        return canEnable;
    }
}
```
**Responding:**
```java
packetManager.reply(packet);
```
**Example listener? here you are**
```java
public final class EnableServiceHandler extends PacketListener<EnableServiceRequest> {

    private final PacketManager packetManager;

    public EnableServiceHandler(RedisSerializer redisSerializer, PacketManager packetManager) {
        super("channel", redisSerializer);
        this.packetManager = packetManager;
    }

    @Override
    protected void onPacketReceived(EnableServiceRequest received) {
        if (received.getServiceName().equals("33"))
            received.setCanEnable(true);

        packetManager.reply(received);
    }
}
```

**Example packet sending:**
```java
        PacketManager packetManager = new PacketManager(Redisson.create(), new FastSerializationSerializer());
        packetManager.registerPacketListener(new EnableServiceHandler(packetManager.getRedisSerialization(), packetManager));
        packetManager.sendPacket("channel", new EnableServiceRequest("33"), response -> {
             EnableServiceRequest enableServiceRequest = (EnableServiceRequest) response;
             if (!enableServiceRequest.isCanEnable()) {
                 return;
             }
             System.out.println("Turning on service... (" + enableServiceRequest.getServiceName() + ")");
        });
```




### Default serializers
* **[Fast-Serializer](https://github.com/RuedigerMoeller/fast-serialization)** - [Class](https://github.com/sadcenter/server-communication/blob/main/src/main/java/xyz/sadcenter/redis/serializers/impl/FastSerializationSerializer.java)