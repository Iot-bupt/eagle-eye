package org.bupt.bean;

import java.io.Serializable;

public class DetectionStatus implements Serializable {

    private Integer id;
    private String device_id;
    private String cluster_id;
    private Integer running_status;
    private Integer time_interval;
    private Integer init_number;
    private Integer current_init;
    private String init_or_update;
    private String data_array;
    private String data_update;
    private String last_tendata;
    private Long last_update_time;
    private String time_array;
    private String cluster_name;
    private String init_pattern;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getCluster_id() {
        return cluster_id;
    }

    public void setCluster_id(String cluster_id) {
        this.cluster_id = cluster_id;
    }

    public Integer getRunning_status() {
        return running_status;
    }

    public void setRunning_status(Integer running_status) {
        this.running_status = running_status;
    }

    public Integer getTime_interval() {
        return time_interval;
    }

    public void setTime_interval(Integer time_interval) {
        this.time_interval = time_interval;
    }

    public Integer getInit_number() {
        return init_number;
    }

    public void setInit_number(Integer init_number) {
        this.init_number = init_number;
    }

    public Integer getCurrent_init() {
        return current_init;
    }

    public void setCurrent_init(Integer current_init) {
        this.current_init = current_init;
    }

    public String getInit_or_update() {
        return init_or_update;
    }

    public void setInit_or_update(String init_or_update) {
        this.init_or_update = init_or_update;
    }

    public String getData_array() {
        return data_array;
    }

    public void setData_array(String data_array) {
        this.data_array = data_array;
    }

    public String getData_update() {
        return data_update;
    }

    public void setData_update(String data_update) {
        this.data_update = data_update;
    }

    public String getLast_tendata() {
        return last_tendata;
    }

    public void setLast_tendata(String last_tendata) {
        this.last_tendata = last_tendata;
    }

    public Long getLast_update_time() {
        return last_update_time;
    }

    public void setLast_update_time(Long last_update_time) {
        this.last_update_time = last_update_time;
    }

    public String getTime_array() {
        return time_array;
    }

    public void setTime_array(String time_array) {
        this.time_array = time_array;
    }

    public String getCluster_name() {
        return cluster_name;
    }

    public void setCluster_name(String cluster_name) {
        this.cluster_name = cluster_name;
    }

    public String getInit_pattern() {
        return init_pattern;
    }

    public void setInit_pattern(String init_pattern) {
        this.init_pattern = init_pattern;
    }

    @Override
    public String toString() {
        return "DetectionStatus{" +
                "id=" + id +
                ", device_id='" + device_id + '\'' +
                ", cluster_id='" + cluster_id + '\'' +
                ", running_status=" + running_status +
                ", time_interval=" + time_interval +
                ", init_number=" + init_number +
                ", current_init=" + current_init +
                ", init_or_update='" + init_or_update + '\'' +
                ", data_array='" + data_array + '\'' +
                ", data_update='" + data_update + '\'' +
                ", last_tendata='" + last_tendata + '\'' +
                ", last_update_time=" + last_update_time +
                ", time_array='" + time_array + '\'' +
                ", cluster_name='" + cluster_name + '\'' +
                ", init_pattern='" + init_pattern + '\'' +
                '}';
    }
}

