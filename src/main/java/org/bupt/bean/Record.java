package org.bupt.bean;

import java.io.Serializable;

public class Record implements Serializable {

    private Integer id;
    private String cluster_id;
    private Integer record_id;
    private Long update_time;
    private Double SPERecentData;
    private Double maxThreshold;
    private Double minThreshold;
    private String recentDataStr;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCluster_id() {
        return cluster_id;
    }

    public void setCluster_id(String cluster_id) {
        this.cluster_id = cluster_id;
    }

    public Integer getRecord_id() {
        return record_id;
    }

    public void setRecord_id(Integer record_id) {
        this.record_id = record_id;
    }

    public Long getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Long update_time) {
        this.update_time = update_time;
    }

    public Double getSPERecentData() {
        return SPERecentData;
    }

    public void setSPERecentData(Double SPERecentData) {
        this.SPERecentData = SPERecentData;
    }

    public Double getMaxThreshold() {
        return maxThreshold;
    }

    public void setMaxThreshold(Double maxThreshold) {
        this.maxThreshold = maxThreshold;
    }

    public Double getMinThreshold() {
        return minThreshold;
    }

    public void setMinThreshold(Double minThreshold) {
        this.minThreshold = minThreshold;
    }

    public String getRecentDataStr() {
        return recentDataStr;
    }

    public void setRecentDataStr(String recentDataStr) {
        this.recentDataStr = recentDataStr;
    }

    @Override
    public String toString() {
        return "Record{" +
                "id=" + id +
                ", cluster_id='" + cluster_id + '\'' +
                ", record_id=" + record_id +
                ", update_time=" + update_time +
                ", SPERecentData=" + SPERecentData +
                ", maxThreshold=" + maxThreshold +
                ", minThreshold=" + minThreshold +
                ", recentDataStr='" + recentDataStr + '\'' +
                '}';
    }
}
