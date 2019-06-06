package org.bupt.transport;

import java.util.Properties;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

public class KafkaProducer implements Runnable {

    private final org.apache.kafka.clients.producer.KafkaProducer producer;
    private final String topic;
    public KafkaProducer(String topicName) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "127.0.0.1:9092");
        props.put("acks", "1");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("key.serializer", StringSerializer.class.getName());
        props.put("value.serializer", StringSerializer.class.getName());
        this.producer = new org.apache.kafka.clients.producer.KafkaProducer(props);
        this.topic = topicName;
    }

    @Override
    public void run() {
        try {

            producer.send(new ProducerRecord<String, String>(topic, "cyl24"));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            producer.close();
        }
    }

    public static void main(String args[]) {
        KafkaProducer test = new KafkaProducer("test");
        Thread thread = new Thread(test);
        thread.start();
    }
}
