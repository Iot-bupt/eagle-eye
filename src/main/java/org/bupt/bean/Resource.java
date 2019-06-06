package org.bupt.bean;

import java.io.Serializable;

public class Resource implements Serializable {

    private Integer id;
    private Integer Station_Id_C;
    private Long update_time;
    private Double PRS;
    private Double PRS_Sea;
    private Double PRS_Max;
    private Double PRS_Min;
    private Double TEM;
    private Double TEM_Max;
    private Double TEM_Min;
    private Double RHU;
    private Double RHU_Min;
    private Double VAP;
    private Double PRE_1h;
    private Double WIN_D_INST_Max;
    private Double WIN_S_Max;
    private Double WIN_D_S_Max;
    private Double WIN_S_Avg_2mi;
    private Double WIN_D_Avg_2mi;
    private Double WEP_Now;
    private Double WIN_S_Inst_Max;
    private Double tigan;
    private Double windpower;
    private Double VIS;
    private Double CLO_Cov;
    private Double CLO_Cov_Low;
    private Double CLO_COV_LM;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStation_Id_C() {
        return Station_Id_C;
    }

    public void setStation_Id_C(Integer station_Id_C) {
        Station_Id_C = station_Id_C;
    }

    public Long getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Long update_time) {
        this.update_time = update_time;
    }

    public Double getPRS() {
        return PRS;
    }

    public void setPRS(Double PRS) {
        this.PRS = PRS;
    }

    public Double getPRS_Sea() {
        return PRS_Sea;
    }

    public void setPRS_Sea(Double PRS_Sea) {
        this.PRS_Sea = PRS_Sea;
    }

    public Double getPRS_Max() {
        return PRS_Max;
    }

    public void setPRS_Max(Double PRS_Max) {
        this.PRS_Max = PRS_Max;
    }

    public Double getPRS_Min() {
        return PRS_Min;
    }

    public void setPRS_Min(Double PRS_Min) {
        this.PRS_Min = PRS_Min;
    }

    public Double getTEM() {
        return TEM;
    }

    public void setTEM(Double TEM) {
        this.TEM = TEM;
    }

    public Double getTEM_Max() {
        return TEM_Max;
    }

    public void setTEM_Max(Double TEM_Max) {
        this.TEM_Max = TEM_Max;
    }

    public Double getTEM_Min() {
        return TEM_Min;
    }

    public void setTEM_Min(Double TEM_Min) {
        this.TEM_Min = TEM_Min;
    }

    public Double getRHU() {
        return RHU;
    }

    public void setRHU(Double RHU) {
        this.RHU = RHU;
    }

    public Double getRHU_Min() {
        return RHU_Min;
    }

    public void setRHU_Min(Double RHU_Min) {
        this.RHU_Min = RHU_Min;
    }

    public Double getVAP() {
        return VAP;
    }

    public void setVAP(Double VAP) {
        this.VAP = VAP;
    }

    public Double getPRE_1h() {
        return PRE_1h;
    }

    public void setPRE_1h(Double PRE_1h) {
        this.PRE_1h = PRE_1h;
    }

    public Double getWIN_D_INST_Max() {
        return WIN_D_INST_Max;
    }

    public void setWIN_D_INST_Max(Double WIN_D_INST_Max) {
        this.WIN_D_INST_Max = WIN_D_INST_Max;
    }

    public Double getWIN_S_Max() {
        return WIN_S_Max;
    }

    public void setWIN_S_Max(Double WIN_S_Max) {
        this.WIN_S_Max = WIN_S_Max;
    }

    public Double getWIN_D_S_Max() {
        return WIN_D_S_Max;
    }

    public void setWIN_D_S_Max(Double WIN_D_S_Max) {
        this.WIN_D_S_Max = WIN_D_S_Max;
    }

    public Double getWIN_S_Avg_2mi() {
        return WIN_S_Avg_2mi;
    }

    public void setWIN_S_Avg_2mi(Double WIN_S_Avg_2mi) {
        this.WIN_S_Avg_2mi = WIN_S_Avg_2mi;
    }

    public Double getWIN_D_Avg_2mi() {
        return WIN_D_Avg_2mi;
    }

    public void setWIN_D_Avg_2mi(Double WIN_D_Avg_2mi) {
        this.WIN_D_Avg_2mi = WIN_D_Avg_2mi;
    }

    public Double getWEP_Now() {
        return WEP_Now;
    }

    public void setWEP_Now(Double WEP_Now) {
        this.WEP_Now = WEP_Now;
    }

    public Double getWIN_S_Inst_Max() {
        return WIN_S_Inst_Max;
    }

    public void setWIN_S_Inst_Max(Double WIN_S_Inst_Max) {
        this.WIN_S_Inst_Max = WIN_S_Inst_Max;
    }

    public Double getTigan() {
        return tigan;
    }

    public void setTigan(Double tigan) {
        this.tigan = tigan;
    }

    public Double getWindpower() {
        return windpower;
    }

    public void setWindpower(Double windpower) {
        this.windpower = windpower;
    }

    public Double getVIS() {
        return VIS;
    }

    public void setVIS(Double VIS) {
        this.VIS = VIS;
    }

    public Double getCLO_Cov() {
        return CLO_Cov;
    }

    public void setCLO_Cov(Double CLO_Cov) {
        this.CLO_Cov = CLO_Cov;
    }

    public Double getCLO_Cov_Low() {
        return CLO_Cov_Low;
    }

    public void setCLO_Cov_Low(Double CLO_Cov_Low) {
        this.CLO_Cov_Low = CLO_Cov_Low;
    }

    public Double getCLO_COV_LM() {
        return CLO_COV_LM;
    }

    public void setCLO_COV_LM(Double CLO_COV_LM) {
        this.CLO_COV_LM = CLO_COV_LM;
    }

    @Override
    public String toString() {
        return "Resource{" +
                "id=" + id +
                ", Station_Id_C=" + Station_Id_C +
                ", update_time=" + update_time +
                ", PRS=" + PRS +
                ", PRS_Sea=" + PRS_Sea +
                ", PRS_Max=" + PRS_Max +
                ", PRS_Min=" + PRS_Min +
                ", TEM=" + TEM +
                ", TEM_Max=" + TEM_Max +
                ", TEM_Min=" + TEM_Min +
                ", RHU=" + RHU +
                ", RHU_Min=" + RHU_Min +
                ", VAP=" + VAP +
                ", PRE_1h=" + PRE_1h +
                ", WIN_D_INST_Max=" + WIN_D_INST_Max +
                ", WIN_S_Max=" + WIN_S_Max +
                ", WIN_D_S_Max=" + WIN_D_S_Max +
                ", WIN_S_Avg_2mi=" + WIN_S_Avg_2mi +
                ", WIN_D_Avg_2mi=" + WIN_D_Avg_2mi +
                ", WEP_Now=" + WEP_Now +
                ", WIN_S_Inst_Max=" + WIN_S_Inst_Max +
                ", tigan=" + tigan +
                ", windpower=" + windpower +
                ", VIS=" + VIS +
                ", CLO_Cov=" + CLO_Cov +
                ", CLO_Cov_Low=" + CLO_Cov_Low +
                ", CLO_COV_LM=" + CLO_COV_LM +
                '}';
    }

}
