# Packet communications api.
Api for spigot sectors, connecting servers etc.
Based on redis (RedissonClient).
Not recommended for license systems etc.

# Additional informations
If you use this api for sectors etc, and you receive a big amount of packets in packet handler I recommend use a `PacketManager#registerAsyncPacketListener` method in registration.
If you find issue please report it on github or write to me on discord `jerzyk#3904`


# TODO
Codec system. 

#

## Usage:
```java
        PacketManager packetManager = new PacketManager(Redisson.create(), new FastSerializationSerializer());
        packetManager.sendPacket(
                "channel", //channel to send packet
                packet); //your packet implementation
        packetManager.registerPacketListener(new PacketListener<Packet>(
                "channel", //listening channel
                packetManager.getRedisSerialization()) { //your redis serializer
            @Override
            protected void onPacketReceived(Packet received) {
                //code
                System.out.println("Received packet!");
            }
        });
```

## Serializers
Create your own serializer. (Only serializer to byte-array)
```java

public final class YourSerializer implements RedisSerializer {

    @Override
    public byte[] serialize(Packet packet) {
       //code returning packet as byte array
    }

    @Override
    public <T extends Packet> T deserialize(byte[] bytes) {
       //code returning byte-array as packet
    }
}

```

## Async functions.
Async listeners.
```java
    packetManager.registerPacketListener(new AsyncPacketListener<Packet>(
            "channel", //listening channel
            packetManager.getRedisSerialization(), //your serializer
            new FutureTaskAsyncRunner()) { //async runner
            
            @Override
            protected void onPacketReceived(Packet received) {
                //code
                System.out.println("received packet in async!");
            }
        });

```
#
OR use `PacketManager#registerAsyncPacketListener` method
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
#
Async runners. Create your own async runner!
```java
public final class YourAsyncRunner implements AsyncRunner {

    @Override
    public void runAsync(Runnable runnable) {
        //run runnable as async
        ForkJoinPool.commonPool().submit(runnable);
    }
}
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



## Ready implementation
This api contains ready implementation for you!

### Serializers
* **[Fast-Serializer](https://github.com/RuedigerMoeller/fast-serialization)** - [Class](https://github.com/sadcenter/server-communication/blob/main/src/main/java/xyz/sadcenter/redis/serializers/impl/FastSerializationSerializer.java)

### Async Runners

**From java:**

* **[ForkJoinPool](https://github.com/sadcenter/server-communication/blob/main/src/main/java/xyz/sadcenter/redis/async/impl/java/ForkJoinPoolAsyncRunner.java)**
* **[FutureTask](https://github.com/sadcenter/server-communication/blob/main/src/main/java/xyz/sadcenter/redis/async/impl/java/FutureTaskAsyncRunner.java)**

**For bukkit developers:**

* **[BukkitScheduler](https://github.com/sadcenter/server-communication/blob/main/src/main/java/xyz/sadcenter/redis/async/impl/bukkit/BukkitSchedulerAsyncRunner.java)**

**Guava:**
* **[ListenableFuture](https://github.com/sadcenter/server-communication/blob/main/src/main/java/xyz/sadcenter/redis/async/impl/guava/ListenableFutureAsyncRunner.java)**
