package org.bupt.controller;

import org.bupt.bean.DetectionStatus;
import org.bupt.handler.DeviceDetectionHandler;
import org.bupt.service.DetectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RealtimeController {

    @Autowired
    private DetectionService detectionService;

    @Autowired
    private DeviceDetectionHandler deviceDetectionHandler;

    @RequestMapping("/startdetection")
    @ResponseBody
        public String startDetection(String cluster_id) {

        deviceDetectionHandler.setCluster_id(cluster_id);

        Thread thread = new Thread(deviceDetectionHandler);
        thread.start();

        DetectionStatus detectionStatus = new DetectionStatus();
        detectionStatus.setCluster_id(cluster_id);
        detectionStatus.setRunning_status(1);
        boolean flag = detectionService.startorStopDetection(detectionStatus);

        if (flag == true) {

            return "Start success!";

        }else {

            return "Start failed!";

        }

    }

    @RequestMapping("/stopdetection")
    @ResponseBody
    public String stopDetection(String cluster_id) {

        System.out.println("cluster_id:"+cluster_id);

        DetectionStatus detectionStatus = new DetectionStatus();
        detectionStatus.setCluster_id(cluster_id);
        detectionStatus.setRunning_status(0);
        boolean flag = detectionService.startorStopDetection(detectionStatus);

        if (flag == true) {

            return "Stop success!";

        }else {

            return "Stop failed!";

        }

    }

}
