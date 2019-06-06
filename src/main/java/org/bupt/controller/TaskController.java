package org.bupt.controller;

import com.google.gson.Gson;
import org.bupt.bean.Detection;
import org.bupt.bean.DetectionStatus;
import org.bupt.service.DetectionService;
import org.bupt.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class TaskController {

    @Autowired
    private DetectionService detectionService;
    @Autowired
    private RecordService recordService;

    @RequestMapping("/gettask")
    @ResponseBody
    public String getDetectionStatus() {

        List<String> list = detectionService.getDistinctClusterId();
        List<DetectionStatus> list2 = new ArrayList<DetectionStatus>();
        Gson gson = new Gson();

        for (int i=0; i<list.size(); i++) {

            List<String> deviceList = new ArrayList<String>();

            List<DetectionStatus> list1 = detectionService.getDetectionStatus(list.get(i));
            DetectionStatus detectionStatus = list1.get(0);
            for (int j=0; j<list1.size(); j++) {
                String deviceId = list1.get(j).getDevice_id();
                deviceList.add(deviceId);
            }
            detectionStatus.setDevice_id(gson.toJson(deviceList));
            list2.add(detectionStatus);
        }

        return gson.toJson(list2);

    }

    @RequestMapping("/createtask")
    @ResponseBody
    public String createDetectionStatus(String clustername,Integer period,Integer length,String device, String pattern) throws IOException {

        device = "["+device+"]";
        Gson gson = new Gson();
        String[] deviceArray = gson.fromJson(device,String[].class);

        boolean flag = false;

        String cluster_id = UUID.randomUUID().toString();
        for (int i=0; i<deviceArray.length; i++) {

            DetectionStatus detectionStatus = new DetectionStatus();
            detectionStatus.setCluster_name(clustername);
            detectionStatus.setCluster_id(cluster_id);
            detectionStatus.setTime_interval(period);
            detectionStatus.setInit_number(length);
            detectionStatus.setDevice_id(deviceArray[i]);
            detectionStatus.setRunning_status(0);
            detectionStatus.setCurrent_init(0);
            detectionStatus.setInit_or_update("init");
            detectionStatus.setData_array("0");
            detectionStatus.setData_update("0");
            detectionStatus.setLast_tendata("0");
            detectionStatus.setLast_update_time(0L);
            detectionStatus.setTime_array("0");
            detectionStatus.setInit_pattern(pattern);

            flag = detectionService.addDetectionStatus(detectionStatus);

            System.out.println(detectionStatus);

        }

        if (flag == true) {

            return "Create success!";

        }else {

            return "Create failed!";

        }

    }

    @RequestMapping("/updatetask")
    @ResponseBody
    public String updateDetectionStatus(String cluster_id,String cluster_name, Integer period, Integer length) {

        DetectionStatus detectionStatus = new DetectionStatus();
        detectionStatus.setCluster_id(cluster_id);
        detectionStatus.setCluster_name(cluster_name);
        detectionStatus.setTime_interval(period);
        detectionStatus.setInit_number(length);
        boolean flag = detectionService.updateDetectionStatusWeb(detectionStatus);

        if (flag == true) {

            return "Update success!";

        }else {

            return "Update failed!";

        }

    }

    @RequestMapping("/deletetask")
    @ResponseBody
    public String deleteDetectionStatus(String cluster_id) {

        detectionService.deleteDetection(cluster_id);
        recordService.deleteDetectionRecord(cluster_id);
        boolean flag = detectionService.deleteDetectionStatus(cluster_id);

        if (flag == true) {

            return "Delete success!";

        }else {

            return "Delete failed!";

        }

    }

    @RequestMapping("/reinittask")
    @ResponseBody
    public String reInit(String cluster_id) {

        List<DetectionStatus> list = detectionService.getDetectionStatus(cluster_id);

        boolean flag = false;

        for (int i=0; i<list.size(); i++) {

            DetectionStatus detectionStatus = list.get(i);
            detectionStatus.setCurrent_init(0);
            detectionStatus.setInit_or_update("init");
            detectionStatus.setData_array("0");
            detectionStatus.setData_update("0");
            detectionStatus.setLast_tendata("0");
            detectionStatus.setLast_update_time(0L);
            detectionStatus.setTime_array("0");
            detectionStatus.setRunning_status(0);
            detectionService.updateDetectionStatus(detectionStatus);

        }

        Detection detection = detectionService.getDetectionParameters(cluster_id);
        if (detection == null) {
            flag = true;
        }else {
            flag = detectionService.deleteDetection(cluster_id);
        }

        if (flag == true) {

            return "Success!";

        }else {

            return "Failed!";

        }

    }

}
