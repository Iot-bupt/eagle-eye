package org.bupt.transport;

import java.util.Arrays;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.StringDeserializer;


/**
 *
 * Title: KafkaConsumer
 * Description:
 *  kafka消费者 demo
 * Version:1.0.0
 * @author pancm
 * @date 2018年1月26日
 */
public class KafkaConsumer implements Runnable {

    private final org.apache.kafka.clients.consumer.KafkaConsumer consumer;
    private ConsumerRecords<String, String> msgList;
    private final String topic;
    private static final String GROUPID = "groupG";

    public KafkaConsumer(String topicName) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "127.0.0.1:9092");
        props.put("group.id", GROUPID);
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("auto.offset.reset", "earliest");
        props.put("key.deserializer", StringDeserializer.class.getName());
        props.put("value.deserializer", StringDeserializer.class.getName());
        this.consumer = new org.apache.kafka.clients.consumer.KafkaConsumer(props);
        this.topic = topicName;
        this.consumer.subscribe(Arrays.asList(topic));
    }

    @Override
    public void run() {
        System.out.println("---------开始消费---------");
        try {
            while (true){
                msgList = consumer.poll(1000);
                if(null!=msgList&&msgList.count()>0){
                    for (ConsumerRecord<String, String> record : msgList) {
                        //消费100条就打印 ,但打印的数据不一定是这个规律的
                        System.out.println("=======receive: key = " + record.key() + ", value = " + record.value()+" offset==="+record.offset());
                        //当消费了1000条就退出
                        break;
                    }
                }else{
                    Thread.sleep(1000);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            consumer.close();
        }
    }

    public static void main(String args[]) {
        KafkaConsumer test1 = new KafkaConsumer("test");
        Thread thread1 = new Thread(test1);
        thread1.start();
    }

}

