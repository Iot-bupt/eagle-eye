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
public class RecourseInitProcesser {

    @Autowired
    private DetectionService detectionService;
    @Autowired
    private InitParameterService initParameterService;
    @Autowired
    private UpdateParameterService updateParameterService;
    @Autowired
    private RecordService recordService;

    @Transactional
    public void sampleDetection() {

        long startSysTime = System.currentTimeMillis();

        detectionService.deleteDetection("839fa7d5-7f73-4dff-af09-9297c04e8353");

        //recordService.deleteDetectionRecord("839fa7d5-7f73-4dff-af09-9297c04e8353");

        /*
        北京：18个站点:54398-54597, 210个数据, start:2018-12-28 00:00:00(1545926400000) end:2019-01-06 00:00:00(1546704000000), No.100:2019-01-01 03:00:00(1546282800000), No.101:2019-01-01 04:00:00(1546286400000)
        上海：11个站点:55001-55011, 169个数据, start:2018-12-30 00:00:00(1546099200000) end:2019-01-06 00:00:00(1546704000000), No.100:2019-01-03 03:00:00(1546455600000), No.101:2019-01-03 04:00:00(1546459200000)
        广东：82个站点:57988-59754, 169个数据, start:2018-12-30 00:00:00(1546099200000) end:2019-01-06 00:00:00(1546704000000), No.100:2019-01-03 03:00:00(1546455600000), No.101:2019-01-03 04:00:00(1546459200000)
        all+41 endtime:2019-01-07 17:00:00(1546851600000)

        河北 16个站点:54601,54604-54618, 162个数据 start:2019-01-01 00:00:00(1546272000000) end:2019-01-07 17:00:00(1546851600000) No.100:2019-01-05 03:00:00(1546628400000), No.101:2019-01-05 04:00:00(1546632000000)
        河南 18个站点:53889-53994, 162个数据 start:2019-01-01 00:00:00(1546272000000) end:2019-01-07 17:00:00(1546851600000) No.100:2019-01-05 03:00:00(1546628400000), No.101:2019-01-05 04:00:00(1546632000000)
        山东 18个站点:54709-54751, 162个数据 start:2019-01-01 00:00:00(1546272000000) end:2019-01-07 17:00:00(1546851600000) No.100:2019-01-05 03:00:00(1546628400000), No.101:2019-01-05 04:00:00(1546632000000)
        山西 18个站点:53574-53665, 162个数据 start:2019-01-01 00:00:00(1546272000000) end:2019-01-07 17:00:00(1546851600000) No.100:2019-01-05 03:00:00(1546628400000), No.101:2019-01-05 04:00:00(1546632000000)

        湖南 18个站点:57544-57655, 162个数据 start:2019-01-01 00:00:00(1546272000000) end:2019-01-07 17:00:00(1546851600000) No.100:2019-01-05 03:00:00(1546628400000), No.101:2019-01-05 04:00:00(1546632000000)
        陕西 18个站点:57003-57037, 162个数据 start:2019-01-01 00:00:00(1546272000000) end:2019-01-07 17:00:00(1546851600000) No.100:2019-01-05 03:00:00(1546628400000), No.101:2019-01-05 04:00:00(1546632000000)
        四川 18个站点:56038-56182, 162个数据 start:2019-01-01 00:00:00(1546272000000) end:2019-01-07 17:00:00(1546851600000) No.100:2019-01-05 03:00:00(1546628400000), No.101:2019-01-05 04:00:00(1546632000000)
        all+139 endtime:2019-01-13 12:00:00(1547352000000)

        all: 301个数据 start:2019-01-01 00:00:00(1546272000000) No.100:2019-01-05 03:00:00(1546628400000) end:2019-01-13 12:00:00(1547352000000)

        */

        long initStartTime = 1546272000000L;
        long initEndTime = 1546621200000L;
        long startTime = 1546632000000L;
        long endTime = 1546632000000L;
        double randomNumber = 0;

        double updateDataNum = 201;
        int errorNumber = 1;
        int errorInterver = 5;

        int stationNum = 3;

        List<Resource> list = detectionService.getOneDetectionResource(initStartTime,initEndTime);

        double[][] initArray2X = new double[stationNum][98];

        for (int i=0; i<stationNum; i++) {
            for (int j=0; j<98; j++) {
                initArray2X[i][j] = list.get(98*i+j).getTEM();//getPRS,getPRS_Sea,getTEM
            }
        }

        Gson gson = new Gson();
        Long last_update_time = new Date().getTime();

        for (int i=0; i<3; i++) {

            DetectionStatus detectionStatus = new DetectionStatus();
            detectionStatus.setId(68224+i);
            detectionStatus.setCurrent_init(98);
            detectionStatus.setInit_or_update("init");
            detectionStatus.setData_array(gson.toJson(initArray2X[i]));
            detectionStatus.setData_update("0");
            detectionStatus.setLast_update_time(last_update_time);

            double[] last_tendata = new double[2];
            last_tendata[0] = initArray2X[i][97];
            last_tendata[1] = 1;
            detectionStatus.setLast_tendata(gson.toJson(last_tendata));

            detectionService.updateDetectionStatusInitTest(detectionStatus);

        }

        //initParameterService.initParameter(initArray2X,"839fa7d5-7f73-4dff-af09-9297c04e8353");

        /*for (int n=0; n<updateDataNum; n++) {

            double[] updateArray = new double[stationNum];

            long currentStartTime = startTime+3600000*n;
            long currentEndTime = endTime+3600000*n;
            List<Resource> list1 = detectionService.getOneDetectionResource(currentStartTime,currentEndTime);
            for (int k=0; k<stationNum; k++) {
                updateArray[k] = list1.get(k).getTEM();
            }

            *//*Math.floor(Math.random()*5)*//*
            if (n%errorInterver == 0){

                double[] errorArray = new double[stationNum];
                List<Resource> list2;

                if (n != updateDataNum-1) {
                    list2 = detectionService.getOneDetectionResource(currentStartTime+3600000,currentEndTime+3600000);
                } else {
                    list2 = detectionService.getOneDetectionResource(currentStartTime,currentEndTime);
                }

                for (int k=0; k<stationNum; k++) {
                    errorArray[k] = list2.get(k).getTEM();
                }

                *//*全设置为20，特殊情况下PRS设置为960，PRS_Sea设置为1010*//*
                for (int i=0; i<errorNumber; i++) {
                    errorArray[i] = 20;
                }

                updateParameterService.updateParameter("test1",-1,n,errorArray);
                randomNumber = randomNumber + 1;
            }

            *//*测试*//*
            *//*if (n>=2 && n<=3) {
                for (int k=0; k<18; k++) {
                    updateArray[k] = 0;
                }
            }
            if (n==5) {
                for (int k=0; k<18; k++) {
                    updateArray[k] = 0;
                }
            }*//*
            *//*测试*//*

            updateParameterService.updateParameter("test1",currentStartTime,n,updateArray);

        }

        long endSysTime = System.currentTimeMillis();

        List<Record> recordList1 = recordService.getFprRecordR();
        List<Record> recordList2 = recordService.getTprRecordRorDR();

        System.out.println("Method spend:"+(endSysTime-startSysTime)+"ms");

        double fpr = recordList1.size()/updateDataNum;
        double tpr = recordList2.size()/randomNumber;

        System.out.println("正常数据总数"+updateDataNum);
        System.out.println("异常数据总数"+randomNumber);
        System.out.println("站点总数：" + stationNum);
        System.out.println("异常间隔：" + errorInterver);
        System.out.println("\033[31;4m" + "R: fpr=" + fpr + "\033[0m");
        System.out.println("\033[31;4m" + "R: tpr=" + tpr + "\033[0m");*/

    }

    public static void main(String[] args) {

        ApplicationContext applicationContext = ApplicationContextUtil.getApplicationContext();
        RecourseInitProcesser recourseInitProcesser = (RecourseInitProcesser) applicationContext.getBean("recourseInitProcesser");
        recourseInitProcesser.sampleDetection();

    }

}
