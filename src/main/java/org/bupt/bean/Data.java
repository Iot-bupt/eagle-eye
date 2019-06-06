package org.bupt.bean;

import java.io.Serializable;
import java.util.UUID;

public class Data implements Serializable{

    private Integer id;
    private String Station_Id_C;
    private long update_time;
    private double PRS;
    private double PRS_Sea;
    private double TEM;
    private double RHU;
    private double VAP;
    private double PRE_1h;
    private double WIN_S_Avg_2mi;
    private double WIN_D_S_Max;
    private double CLO_COV_LM;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStation_Id_C() {
        return Station_Id_C;
    }

    public void setStation_Id_C(String station_Id_C) {
        Station_Id_C = station_Id_C;
    }

    public long getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(long update_time) {
        this.update_time = update_time;
    }

    public double getPRS() {
        return PRS;
    }

    public void setPRS(double PRS) {
        this.PRS = PRS;
    }

    public double getPRS_Sea() {
        return PRS_Sea;
    }

    public void setPRS_Sea(double PRS_Sea) {
        this.PRS_Sea = PRS_Sea;
    }

    public double getTEM() {
        return TEM;
    }

    public void setTEM(double TEM) {
        this.TEM = TEM;
    }

    public double getRHU() {
        return RHU;
    }

    public void setRHU(double RHU) {
        this.RHU = RHU;
    }

    public double getVAP() {
        return VAP;
    }

    public void setVAP(double VAP) {
        this.VAP = VAP;
    }

    public double getPRE_1h() {
        return PRE_1h;
    }

    public void setPRE_1h(double PRE_1h) {
        this.PRE_1h = PRE_1h;
    }

    public double getWIN_S_Avg_2mi() {
        return WIN_S_Avg_2mi;
    }

    public void setWIN_S_Avg_2mi(double WIN_S_Avg_2mi) {
        this.WIN_S_Avg_2mi = WIN_S_Avg_2mi;
    }

    public double getWIN_D_S_Max() {
        return WIN_D_S_Max;
    }

    public void setWIN_D_S_Max(double WIN_D_S_Max) {
        this.WIN_D_S_Max = WIN_D_S_Max;
    }

    public double getCLO_COV_LM() {
        return CLO_COV_LM;
    }

    public void setCLO_COV_LM(double CLO_COV_LM) {
        this.CLO_COV_LM = CLO_COV_LM;
    }

    @Override
    public String toString() {
        return "Data{" +
                "id=" + id +
                ", Station_Id_C='" + Station_Id_C + '\'' +
                ", update_time=" + update_time +
                ", PRS=" + PRS +
                ", PRS_Sea=" + PRS_Sea +
                ", TEM=" + TEM +
                ", RHU=" + RHU +
                ", VAP=" + VAP +
                ", PRE_1h=" + PRE_1h +
                ", WIN_S_Avg_2mi=" + WIN_S_Avg_2mi +
                ", WIN_D_S_Max=" + WIN_D_S_Max +
                ", CLO_COV_LM=" + CLO_COV_LM +
                '}';
    }

}
