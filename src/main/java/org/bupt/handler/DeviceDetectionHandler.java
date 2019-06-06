package org.bupt.handler;

import com.google.gson.Gson;
import org.bupt.bean.DetectionStatus;
import org.bupt.service.DetectionService;
import org.bupt.service.InitParameterService;
import org.bupt.service.UpdateParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

//64b94eb0-1aa6-11e9-9bd2-373cce0d1c87,3hOW7NMmUf9RMM1zqnCy 70ba9480-1aa6-11e9-9bd2-373cce0d1c87,3EvJV90kcsWH3Ilhdyp3
//7642e6a0-1aa6-11e9-9bd2-373cce0d1c87,L3uXvuLzRB6dVnzHSn9v

//08f46b00-1aa6-11e9-86ad-6d717511d21e,zOD7lvs47nI6tiprnvJD dc70d960-1aa5-11e9-86ad-6d717511d21e,99lg38YkzBDJ6a6rWWR7
//e59712c0-1aa5-11e9-86ad-6d717511d21e,ph7KZ3rntqUIy6eNmsjd

@Component
@Scope("prototype")
public class DeviceDetectionHandler implements Runnable {

    @Autowired
    private DetectionService detectionService;

    @Autowired
    private InitParameterService initParameterService;

    @Autowired
    private UpdateParameterService updateParameterService;

    private String cluster_id;

    private Gson gson = new Gson();

    public void setCluster_id(String cluster_id) {
        this.cluster_id = cluster_id;
    }

    @Override
    public void run() {

        List<DetectionStatus> list = detectionService.getDetectionStatus(cluster_id);
        DetectionStatus detectionStatus = list.get(0);
        try {
            Thread.sleep(detectionStatus.getTime_interval());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int times = 0;

        while (true) {

            list = detectionService.getDetectionStatus(cluster_id);
            detectionStatus = list.get(0);
            System.out.println(detectionStatus);
            Long last_update_time = new Date().getTime();

            boolean allZeroFlag = true;

            if (detectionStatus.getRunning_status() == 1) {

                if (!(detectionStatus.getInit_pattern().equals("database") && detectionStatus.getInit_or_update().equals("init"))) {

                    System.out.println("进来了！");

                    for (int i = 0; i < list.size(); i++) {

                        DetectionStatus temp = list.get(i);
                        if (temp.getData_update().equals("0")) {
                            allZeroFlag = false;
                        }

                    }

                    if (allZeroFlag == false) {

                        for (int i = 0; i < list.size(); i++) {

                            DetectionStatus temp = list.get(i);
                            double[] last_tendata = new double[2];
                            if (!temp.getData_update().equals("0")) {
                                double[] data_update = gson.fromJson(temp.getData_update(), double[].class);
                                double total = 0;
                                int count = 0;
                                for (int j = 0; j < data_update.length; j++) {
                                    total += data_update[j];
                                    count++;
                                }
                                last_tendata[0] = total / count;
                                last_tendata[1] = 0;
                            } else {
                                last_tendata[0] = -1;
                                last_tendata[1] = 0;
                            }
                            temp.setLast_update_time(last_update_time);
                            temp.setLast_tendata(gson.toJson(last_tendata));
                            temp.setData_update("0");
                            detectionService.updateDetectionStatus(temp);

                        }

                    } else {

                        if (detectionStatus.getInit_or_update().equals("init")) {

                            for (int i = 0; i < list.size(); i++) {

                                DetectionStatus temp = list.get(i);

                                String data_array = temp.getData_array();
                                String data_update = temp.getData_update();
                                String time_array = temp.getTime_array();
                                double[] last_tendata = new double[2];
                                double[] data_arrayfin;

                                double[] data_update1 = gson.fromJson(data_update, double[].class);
                                double total = 0;
                                int count = 0;
                                for (int k = 0; k < data_update1.length; k++) {
                                    total += data_update1[k];
                                    count++;
                                }

                                if (data_array.equals("0")) {

                                    double[] data_array1 = new double[1];
                                    data_array1[0] = total / count;
                                    String data_array2 = gson.toJson(data_array1);
                                    temp.setData_array(data_array2);

                                } else {

                                    double[] data_array1 = gson.fromJson(data_array, double[].class);
                                    int length = data_array1.length;
                                    double[] data_array2 = new double[length + 1];
                                    for (int j = 0; j < length; j++) {
                                        data_array2[j] = data_array1[j];
                                    }
                                    data_array2[length] = total / count;
                                    String data_array3 = gson.toJson(data_array2);
                                    temp.setData_array(data_array3);
                                    data_arrayfin = data_array2;

                                }
                                temp.setData_update("0");
                                last_tendata[0] = total / count;
                                last_tendata[1] = 1;

                                temp.setTime_array("0");
                                temp.setCurrent_init(temp.getCurrent_init() + 1);
                                temp.setLast_update_time(last_update_time);
                                temp.setLast_tendata(gson.toJson(last_tendata));
                                detectionService.updateDetectionStatus(temp);

                            }

                            if (detectionStatus.getCurrent_init() == detectionStatus.getInit_number()) {

                                double[][] initArray = new double[list.size()][detectionStatus.getInit_number()];

                                for (int i = 0; i < list.size(); i++) {

                                    DetectionStatus temp = list.get(i);
                                    temp.setInit_or_update("update");

                                    double[] data_array1 = gson.fromJson(temp.getData_array(), double[].class);
                                    for (int j = 0; j < data_array1.length; j++) {
                                        initArray[i][j] = data_array1[j];
                                    }

                                    detectionService.updateDetectionStatus(temp);

                                }

                                initParameterService.initParameter(initArray, cluster_id);

                            }

                        } else {

                            double[] updateArray = new double[list.size()];

                            for (int i = 0; i < list.size(); i++) {

                                DetectionStatus temp = list.get(i);

                                double[] data_update = gson.fromJson(temp.getData_update(), double[].class);
                                double total = 0;
                                int count = 0;
                                for (int k = 0; k < data_update.length; k++) {
                                    total += data_update[k];
                                    count++;
                                }
                                updateArray[i] = total / count;

                                temp.setData_update("0");
                                temp.setLast_update_time(last_update_time);
                                detectionService.updateDetectionStatus(temp);

                            }

                            System.out.println("updateArray" + Arrays.asList(updateArray));
                            double[] updateResult = updateParameterService.updateParameter(cluster_id, last_update_time, updateArray);

                            for (int i = 0; i < list.size(); i++) {

                                DetectionStatus temp = list.get(i);
                                double[] last_tendata = new double[2];
                                last_tendata[0] = updateArray[i];
                                last_tendata[1] = updateResult[i];
                                temp.setLast_tendata(gson.toJson(last_tendata));
                                detectionService.updateDetectionStatus(temp);

                            }

                        }

                    }

                    System.out.println("run:" + (++times));

                }

                /*long endTime = System.currentTimeMillis();
                System.out.println("\033[31;4m" + "Execute time:"+(endTime-startTime)+"ms" + "\033[0m");*/

                try {
                    Thread.sleep(detectionStatus.getTime_interval());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            } else {
                break;
            }

        }

        /*for (int i=0; i<10; i++) {
            System.out.println(cluster_id);
            try {
                //sleep用于线程间的调度，wait用于同一单例的调度，sleep不能释放对象锁但wait可以释放对象锁，wait必须用于同步方法或同步块中
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/

    }

}
