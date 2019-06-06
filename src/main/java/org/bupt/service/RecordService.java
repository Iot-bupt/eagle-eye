package org.bupt.service;

import org.bupt.bean.Record;
import org.bupt.dao.RecordImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class RecordService {

    @Autowired
    private RecordImpl recordDao;

    public boolean addDetectionRecords(Record record) throws IOException {

        boolean flag = false;
        flag = recordDao.addDetectionRecords(record);
        System.out.println("RecordService,addDetectionParameters:"+flag);
        return flag;
    }

    public Record getDetectionRecords(String cluster_id) {

        Record record;
        record = recordDao.getDetectionRecords(cluster_id);
        return record;
    }

    public List<Record> getDetectionRecordsList(String cluster_id) {

        List<Record> list = recordDao.getDetectionRecordsList(cluster_id);
        return list;
    }

    public boolean deleteDetectionRecord(String cluster_id) {

        boolean flag = false;
        flag = recordDao.deleteDetectionRecord(cluster_id);
        return flag;

    }

    public List<Record> getFprRecordR() {

        List<Record> list;
        list = recordDao.getFprRecordR();
        return list;

    }

    public List<Record> getFprRecordDR() {

        List<Record> list;
        list = recordDao.getFprRecordDR();
        return list;

    }

    public List<Record> getTprRecordRorDR() {

        List<Record> list;
        list = recordDao.getTprRecordRorDR();
        return list;

    }

}
