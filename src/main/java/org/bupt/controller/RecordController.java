package org.bupt.controller;

import com.google.gson.Gson;
import org.bupt.bean.Record;
import org.bupt.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;

@Controller
public class RecordController {

    @Autowired
    private RecordService recordService;

    @RequestMapping("/getrecord")
    @ResponseBody
    public String getRecord(String clusterId) {

        List<Record> list = recordService.getDetectionRecordsList(clusterId);
        Gson gson = new Gson();
        return gson.toJson(list);

    }

}
