package org.bupt.bean;

import java.io.Serializable;
import java.util.Arrays;

public class Detection implements Serializable {

    private Integer id;
    private String mu;
    private String sigma;
    private String SPEmu;
    private String SPEsigma;
    private String eigenvectorMatrix;
    private String X;
    private String cluster_id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMu() {
        return mu;
    }

    public void setMu(String mu) {
        this.mu = mu;
    }

    public String getSigma() {
        return sigma;
    }

    public void setSigma(String sigma) {
        this.sigma = sigma;
    }

    public String getSPEmu() {
        return SPEmu;
    }

    public void setSPEmu(String SPEmu) {
        this.SPEmu = SPEmu;
    }

    public String getSPEsigma() {
        return SPEsigma;
    }

    public void setSPEsigma(String SPEsigma) {
        this.SPEsigma = SPEsigma;
    }

    public String getEigenvectorMatrix() {
        return eigenvectorMatrix;
    }

    public void setEigenvectorMatrix(String eigenvectorMatrix) {
        this.eigenvectorMatrix = eigenvectorMatrix;
    }

    public String getX() {
        return X;
    }

    public void setX(String x) {
        X = x;
    }

    public String getCluster_id() {
        return cluster_id;
    }

    public void setCluster_id(String cluster_id) {
        this.cluster_id = cluster_id;
    }

    @Override
    public String toString() {
        return "Detection{" +
                "id=" + id +
                ", mu='" + mu + '\'' +
                ", sigma='" + sigma + '\'' +
                ", SPEmu='" + SPEmu + '\'' +
                ", SPEsigma='" + SPEsigma + '\'' +
                ", eigenvectorMatrix='" + eigenvectorMatrix + '\'' +
                ", X='" + X + '\'' +
                ", cluster_id='" + cluster_id + '\'' +
                '}';
    }

}
