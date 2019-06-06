package org.bupt.transport;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.http.server.ServletServerHttpRequest;

import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

/**
 * @ServerEndpoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端,
 * 注解的值将被用于监听用户连接的终端访问URL地址,客户端可以通过这个URL来连接到WebSocket服务器端
 */
/*@ServerEndpoint("/websocket/{asc}")*/
public class WebSocket {

    private ConsumerRecords<String, String> msgList;

    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
    private static CopyOnWriteArraySet<WebSocket> webSocketSet = new CopyOnWriteArraySet<WebSocket>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    private Thread t;

    private KafkaConsumer<String, String> consumer;

    /**
     * 连接建立成功调用的方法
     * @param session  可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(@PathParam("asc") String asc,Session session){
        this.session = session;
        webSocketSet.add(this);     //加入set中
        addOnlineCount();           //在线数加1
        System.out.println("有新连接加入！当前在线人数为" + getOnlineCount());
        System.out.println();

        t = new Thread(new Runnable(){
            public void run(){
                Properties props = new Properties();
                props.put("bootstrap.servers", "127.0.0.1:9092");
                props.put("group.id", UUID.randomUUID().toString());
                props.put("enable.auto.commit", "true");
                props.put("auto.commit.interval.ms", "1000");
                props.put("session.timeout.ms", "30000");
                props.put("auto.offset.reset", "earliest");
                props.put("key.deserializer", StringDeserializer.class.getName());
                props.put("value.deserializer", StringDeserializer.class.getName());
                consumer = new KafkaConsumer<String, String>(props);
                String topic = "deviceData";
                consumer.subscribe(Arrays.asList(topic));

                System.out.println("---------开始消费---------");
                try {
                    while (true){
                        msgList = consumer.poll(1000);
                        if(null!=msgList&&msgList.count()>0){
                            for (ConsumerRecord<String, String> record : msgList) {
                                //消费100条就打印 ,但打印的数据不一定是这个规律的
                                System.out.println("=======receive: key = " + record.key() + ", value = " + record.value()+" offset==="+record.offset());
                                String responseBody = record.value();
                                sendMessage(responseBody);
                                JsonParser parse = new JsonParser();
                                JsonObject json = (JsonObject) parse.parse(responseBody);
                                String deviceId = json.get("deviceId").getAsString();
                                sendMessage(deviceId);
                                JsonArray array = json.get("data").getAsJsonArray();
                                String ts = array.get(0).getAsJsonObject().get("ts").getAsString();
                                sendMessage(ts);
                                String value = array.get(0).getAsJsonObject().get("value").getAsString();
                                sendMessage(value);
                                break;
                            }
                        }else{
                            Thread.sleep(1000);
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    consumer.close();
                    System.out.println("Consumer closed!");
                }
            }});
        t.start();

    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(){
        t.interrupt();
        webSocketSet.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1
        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("来自客户端的消息:" + message);
        //群发消息
        for(WebSocket item: webSocketSet){
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
        }
    }

    /**
     * 发生错误时调用
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error){
        System.out.println("发生错误");
        error.printStackTrace();
    }

    /**
     * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException{
        this.session.getBasicRemote().sendText(message);
        //this.session.getAsyncRemote().sendText(message);
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocket.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocket.onlineCount--;
    }
}
