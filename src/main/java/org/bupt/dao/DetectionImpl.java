package org.bupt.dao;

import org.apache.ibatis.annotations.Param;
import org.bupt.bean.Data;
import org.bupt.bean.Detection;
import org.bupt.bean.DetectionStatus;
import org.bupt.bean.Resource;

import java.util.List;

public interface DetectionImpl{

    public boolean addDetectionParameters(Detection detection);

    public Detection getDetectionParameters(String cluster_id);

    public boolean updateDetectionParameters(Detection detection);

    public boolean deleteDetection(String cluster_id);

    public boolean addDetectionStatus(DetectionStatus detectionStatus);

    public List<DetectionStatus> getDetectionStatus(String cluster_id);

    public List<DetectionStatus> getAllDetectionStatus();

    public List<DetectionStatus> getDetectionStatusByDeviceId(String device_id);

    public boolean updateDetectionStatus(DetectionStatus detectionStatus);

    public boolean updateDetectionStatusWeb(DetectionStatus detectionStatus);

    public boolean startorStopDetection(DetectionStatus detectionStatus);

    public boolean updateDetectionStatusInitTest(DetectionStatus detectionStatus);

    public boolean deleteDetectionStatus(String cluster_id);

    public List<String> getDistinctDeviceId();

    public List<String> getDistinctClusterId();

    public List<Data> getAllDetectionData();

    public List<Data> getOneDetectionData(@Param("startTime") long startTime, @Param("endTime")  long endTime);

    public boolean addDetectionData(Data data);

    public boolean addDetectionResource(Resource resource);

    public List<Resource> getAllDetectionResource();

    public List<Resource> getOneDetectionResource(@Param("startTime") long startTime,@Param("endTime")  long endTime);

    public List<Resource> getOneDetectionResourceTest(@Param("startTime") long startTime,@Param("endTime")  long endTime,@Param("startStation") int startStation,@Param("endStation")  int endStation);

}
