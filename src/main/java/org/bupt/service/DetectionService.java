package org.bupt.service;

import org.bupt.bean.Data;
import org.bupt.bean.Detection;
import org.bupt.bean.DetectionStatus;
import org.bupt.bean.Resource;
import org.bupt.dao.DetectionImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class DetectionService {

    @Autowired
    private DetectionImpl detectionDao;

    /*@Autowired
    private SqlSession sqlSession;*/

    public boolean addDetectionParameters(Detection detection) throws IOException {

        boolean flag = false;
        flag = detectionDao.addDetectionParameters(detection);
        System.out.println("DetectionService,addDetectionParameters:"+flag);
        return flag;

    }

    public Detection getDetectionParameters(String cluster_id) {

        Detection detection = detectionDao.getDetectionParameters(cluster_id);
        return detection;

    }

    public boolean updateDetectionParameters(Detection detection) {

        boolean flag = detectionDao.updateDetectionParameters(detection);
        return flag;

    }

    public boolean deleteDetection(String cluster_id) {

        boolean flag = false;
        flag = detectionDao.deleteDetection(cluster_id);
        return flag;

    }

    public boolean addDetectionStatus(DetectionStatus detectionStatus) throws IOException {

        boolean flag = false;
        flag = detectionDao.addDetectionStatus(detectionStatus);
        System.out.println("DetectionService,addDetectionStatus:"+flag);
        return flag;

    }

    public List<DetectionStatus> getDetectionStatus(String cluster_id) {

        List<DetectionStatus> list = detectionDao.getDetectionStatus(cluster_id);
        return list;

    }

    public List<DetectionStatus> getAllDetectionStatus() {

        List<DetectionStatus> list = detectionDao.getAllDetectionStatus();
        return list;

    }

    public List<DetectionStatus> getDetectionStatusByDeviceId(String device_id) {

        List<DetectionStatus> list = detectionDao.getDetectionStatusByDeviceId(device_id);
        return list;

    }

    public boolean updateDetectionStatus(DetectionStatus detectionStatus) {

        boolean flag = detectionDao.updateDetectionStatus(detectionStatus);
        return flag;

    }

    public boolean updateDetectionStatusWeb(DetectionStatus detectionStatus) {

        boolean flag = detectionDao.updateDetectionStatusWeb(detectionStatus);
        return flag;

    }

    public boolean startorStopDetection(DetectionStatus detectionStatus) {

        boolean flag = detectionDao.startorStopDetection(detectionStatus);
        return flag;

    }

    public boolean updateDetectionStatusInitTest(DetectionStatus detectionStatus) {

        boolean flag = detectionDao.updateDetectionStatusInitTest(detectionStatus);
        return flag;

    }

    public boolean deleteDetectionStatus(String cluster_id) {

        boolean flag = detectionDao.deleteDetectionStatus(cluster_id);
        return flag;

    }

    public List<String> getDistinctDeviceId() {

        List<String> list = detectionDao.getDistinctDeviceId();
        return list;

    }

    public List<String> getDistinctClusterId() {

        List<String> list = detectionDao.getDistinctClusterId();
        return list;

    }

    public List<Data> getAllDetectionData() {

        List<Data> list = detectionDao.getAllDetectionData();
        return list;

    }

    public List<Data> getOneDetectionData(long startTime, long endTime) {

        List<Data> list = detectionDao.getOneDetectionData(startTime, endTime);
        return list;

    }

    public boolean addDetectionData(Data data) throws IOException {

        boolean flag = false;
        flag = detectionDao.addDetectionData(data);
        System.out.println("DetectionService,addDetectionData:"+flag);
        return flag;

    }

    public List<Resource> getAllDetectionResource() {

        List<Resource> list = detectionDao.getAllDetectionResource();
        return list;

    }

    public List<Resource> getOneDetectionResource(long startTime, long endTime) {

        List<Resource> list = detectionDao.getOneDetectionResource(startTime, endTime);
        return list;

    }

    public List<Resource> getOneDetectionResourceTest(long startTime, long endTime, int startStation, int endStation) {

        List<Resource> list = detectionDao.getOneDetectionResourceTest(startTime, endTime, startStation, endStation);
        return list;

    }

    public boolean addDetectionResource(Resource resource) throws IOException {

        boolean flag = false;
        flag = detectionDao.addDetectionResource(resource);
        System.out.println("DetectionService,addDetectionResource:"+flag);
        return flag;

    }

    public static void main(String[] args) {

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        DetectionService detectionService = (DetectionService) applicationContext.getBean("detectionService");
        System.out.println(detectionService.getAllDetectionData());

    }

}
