package org.bupt.dao;

import org.bupt.bean.Record;

import java.util.List;

public interface RecordImpl {

    public boolean addDetectionRecords(Record record);

    public Record getDetectionRecords(String cluster_id);

    public List<Record> getDetectionRecordsList(String cluster_id);

    public boolean deleteDetectionRecord(String cluster_id);

    public List<Record> getFprRecordR();

    public List<Record> getFprRecordDR();

    public List<Record> getTprRecordRorDR();

}
