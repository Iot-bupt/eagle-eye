package org.bupt.listener;

import org.bupt.handler.DeviceDetectionHandler;
import org.bupt.service.DetectionService;
import org.bupt.util.ApplicationContextUtil;
import org.springframework.context.ApplicationContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.List;

public class InitDetectionListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        ApplicationContext applicationContext = ApplicationContextUtil.getApplicationContext();
        DetectionService detectionService = (DetectionService) applicationContext.getBean("detectionService");
        List<String> list = detectionService.getDistinctClusterId();

        for (int i=0; i<list.size(); i++) {

            DeviceDetectionHandler deviceDetectionHandler = (DeviceDetectionHandler) applicationContext.getBean("deviceDetectionHandler");
            deviceDetectionHandler.setCluster_id(list.get(i));

            Thread thread = new Thread(deviceDetectionHandler);
            thread.start();

        }

        /*Thread thread = new Thread(deviceDetection);
        Thread thread1 = new Thread(deviceDetection1);
        thread.start();
        thread1.start();*/
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("InitDetectionListener stoped!");
    }
}
