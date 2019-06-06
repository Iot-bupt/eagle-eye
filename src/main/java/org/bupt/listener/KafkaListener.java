package org.bupt.listener;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.bupt.bean.DetectionStatus;
import org.bupt.bean.Url;
import org.bupt.service.DetectionService;
import org.bupt.util.ApplicationContextUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.ContextLoader;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

public class KafkaListener implements ServletContextListener {

    private KafkaConsumer<String, String> consumer;
    private ConsumerRecords<String, String> msgList;

    public void contextInitialized(ServletContextEvent servletContextEvent) {

        final ApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
        ApplicationContextUtil.setCtx(applicationContext);

        final Thread t = new Thread(new Runnable(){
            public void run(){

                Gson gson = new Gson();

                Properties props = new Properties();
                props.put("bootstrap.servers", Url.kafkaUrl);
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

                                System.out.println("=======receive: key = " + record.key() + ", value = " + record.value()+" offset==="+record.offset());

                                String responseBody = record.value();
                                JsonParser parse = new JsonParser();
                                JsonObject json = (JsonObject) parse.parse(responseBody);
                                String deviceId = json.get("deviceId").getAsString();
                                JsonArray array = json.get("data").getAsJsonArray();
                                String ts = array.get(0).getAsJsonObject().get("ts").getAsString();
                                String value = array.get(0).getAsJsonObject().get("value").getAsString();

                                DetectionService detectionService = (DetectionService) applicationContext.getBean("detectionService");
                                List<String> list = detectionService.getDistinctDeviceId();

                                if (list.contains(deviceId)) {

                                    List<DetectionStatus> list1 = detectionService.getDetectionStatusByDeviceId(deviceId);
                                    for (DetectionStatus device : list1) {
                                        if (device.getData_update().equals("0")) {

                                            double[] data_update = new double[1];
                                            data_update[0] = Double.parseDouble(value);
                                            String data_updateStr = gson.toJson(data_update);
                                            device.setData_update(data_updateStr);

                                            long[] time_array = new long[1];
                                            time_array[0] = Long.parseLong(ts);
                                            String time_arrayStr = gson.toJson(time_array);
                                            device.setTime_array(time_arrayStr);

                                            detectionService.updateDetectionStatus(device);

                                        } else {

                                            double[] data_update = gson.fromJson(device.getData_update(),double[].class);
                                            long[] time_array = gson.fromJson(device.getTime_array(),long[].class);
                                            int length = data_update.length;
                                            double[] data_update1 = new double[length+1];
                                            long[] time_array1 = new long[length+1];
                                            for (int i=0; i<length; i++) {

                                                data_update1[i] = data_update[i];
                                                time_array1[i] = time_array[i];

                                            }
                                            data_update1[length] = Double.parseDouble(value);
                                            time_array1[length] = Long.parseLong(ts);
                                            String data_updateStr = gson.toJson(data_update1);
                                            String time_arrayStr = gson.toJson(time_array1);

                                            device.setData_update(data_updateStr);
                                            device.setTime_array(time_arrayStr);
                                            detectionService.updateDetectionStatus(device);

                                        }
                                    }

                                    System.out.println("contains true");

                                } else {

                                    System.out.println("contains false");

                                }

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
                    System.out.println("Consumer closed!");
                }
            }});
        t.start();

    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {

        System.out.println("KafkaListener stoped!");

    }

}
