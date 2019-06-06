package org.bupt.api;

import com.google.gson.Gson;
import org.bupt.bean.DetectionStatus;
import org.bupt.bean.Resource;
import org.bupt.service.DetectionService;
import org.bupt.service.InitParameterService;
import org.bupt.service.RecordService;
import org.bupt.service.UpdateParameterService;
import org.bupt.util.ApplicationContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class RecourseInitTestProcesser implements Runnable {

    @Autowired
    private DetectionService detectionService;

    @Autowired
    private InitParameterService initParameterService;

    private String cluster_id;
    private Integer startStation;
    private Integer endStation;

    public void setDetectionService(DetectionService detectionService) {
        this.detectionService = detectionService;
    }

    public void setStartStation(Integer startStation) {
        this.startStation = startStation;
    }

    public void setEndStation(Integer endStation) {
        this.endStation = endStation;
    }

    public void setCluster_id(String cluster_id) {
        this.cluster_id = cluster_id;
    }

    public void run() {

        try {
            sampleDetection(cluster_id);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /*@Transactional*/
    public void sampleDetection(String cluster_id) throws InterruptedException {

        List<DetectionStatus> list1 = detectionService.getDetectionStatus(cluster_id);

        int dataNumber = list1.get(0).getInit_number();

        long initStartTime = 1546272000000L;
        long initEndTime = initStartTime + (dataNumber - 1) * 3600000;

        List<Resource> list = detectionService.getOneDetectionResourceTest(initStartTime,initEndTime,startStation,endStation);

        int stationNum = endStation - startStation + 1;

        int initInterger = 500;

        double[][] initArray2X = new double[stationNum][dataNumber];

        Long last_update_time = new Date().getTime();

        Gson gson = new Gson();

        for (int i=0; i<stationNum; i++) {

            DetectionStatus detectionStatus = list1.get(i);
            detectionStatus.setTime_interval(initInterger);
            detectionStatus.setCurrent_init(0);
            detectionStatus.setInit_or_update("init");
            detectionStatus.setData_update("0");
            detectionStatus.setLast_update_time(last_update_time);
            detectionStatus.setData_array("0");

            double[] last_tendata = new double[2];
            last_tendata[0] = 0;
            last_tendata[1] = 1;
            detectionStatus.setLast_tendata(gson.toJson(last_tendata));

            detectionService.updateDetectionStatus(detectionStatus);

        }

        for(int i=0; i<dataNumber; i++) {

            Long updateTime = new Date().getTime();

            for (int j=0; j<stationNum; j++) {

                DetectionStatus detectionStatus = new DetectionStatus();
                detectionStatus.setId(list1.get(j).getId());
                detectionStatus.setCurrent_init(i+1);
                detectionStatus.setInit_or_update("init");
                detectionStatus.setData_update("0");
                detectionStatus.setLast_update_time(updateTime);

                initArray2X[j][i] = list.get(dataNumber*j+i).getTEM();
                detectionStatus.setData_array(gson.toJson(initArray2X[j]));

                double[] last_tendata = new double[2];
                last_tendata[0] = initArray2X[j][i];
                last_tendata[1] = 1;
                detectionStatus.setLast_tendata(gson.toJson(last_tendata));

                detectionService.updateDetectionStatusInitTest(detectionStatus);

            }

            Thread.sleep(initInterger);

        }

        List<DetectionStatus> list2 = detectionService.getDetectionStatus(cluster_id);

        for (int i=0; i<stationNum; i++) {

            DetectionStatus detectionStatus = list2.get(i);
            detectionStatus.setTime_interval(30000);
            detectionStatus.setInit_or_update("update");

            detectionService.updateDetectionStatus(detectionStatus);

        }

        initParameterService.initParameter(initArray2X, cluster_id);

    }

    public static void main(String[] args) throws InterruptedException {

        ApplicationContext applicationContext = ApplicationContextUtil.getApplicationContext();
        RecourseInitTestProcesser recourseInitTestProcesser = (RecourseInitTestProcesser) applicationContext.getBean("recourseInitTestProcesser");
        Thread thread = new Thread(recourseInitTestProcesser);
        thread.start();

    }

}
