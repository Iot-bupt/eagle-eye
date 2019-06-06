package org.bupt.service;

import com.google.gson.Gson;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.bupt.bean.Detection;
import org.bupt.bean.Record;
import org.bupt.dao.DetectionImpl;
import org.bupt.dao.RecordImpl;
import org.bupt.util.CovMatrixUtil;
import org.bupt.util.RecursionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Map;

@Service
public class UpdateParameterService {

    @Autowired
    private DetectionImpl detectionDao;
    @Autowired
    private RecordImpl recordDao;

    @Transactional
    public double[] updateParameter(String cluster_id, long updateTime, double[] recentData) {

        double[] result = new double[recentData.length];

        Detection detection = detectionDao.getDetectionParameters(cluster_id);
        Gson gson = new Gson();
        double[] mu = gson.fromJson(detection.getMu(),double[].class);
        double[] sigma = gson.fromJson(detection.getSigma(),double[].class);
        double SPEmu = gson.fromJson(detection.getSPEmu(),double.class);
        double SPEsigma = gson.fromJson(detection.getSPEsigma(),double.class);
        RealMatrix eigenvectorMatrix = gson.fromJson(detection.getEigenvectorMatrix(),Array2DRowRealMatrix.class);
        RealMatrix X = gson.fromJson(detection.getX(),Array2DRowRealMatrix.class);

        int row = X.getRowDimension();
        int column = X.getColumnDimension();
        Map<String,Object> map = RecursionUtil.meanAndSigmaRecursion(recentData,mu,sigma,column);
        double[] muRecursion = (double[]) map.get("muRecursion");
        double[] sigmaRecursion = (double[]) map.get("sigmaRecursion");
        double[] normalizationRecentData = RecursionUtil.calculateNormalizationArray(recentData,muRecursion,sigmaRecursion);

        RealMatrix normalizationRecentDataMatrix = new Array2DRowRealMatrix(normalizationRecentData);
        RealMatrix Y = eigenvectorMatrix.multiply(normalizationRecentDataMatrix);
        RealMatrix eigenvectorMatrixT = eigenvectorMatrix.transpose();
        RealMatrix remainX = normalizationRecentDataMatrix.subtract(eigenvectorMatrixT.multiply(Y));
        RealVector remainXVector = remainX.getColumnVector(0);
        double normPow = remainXVector.getNorm();

        double SPERecentData = Math.pow(normPow,2);
        double maxThreshold = SPEmu + 1.96*SPEsigma;
        double minThreshold = SPEmu - 1.96*SPEsigma;
        String recentDataStr = Arrays.toString(recentData);

        System.out.println("\033[31;4m" + "maxThreshold:"+maxThreshold + "\033[0m");
        System.out.println("\033[31;4m" + "minThreshold:"+minThreshold + "\033[0m");
        System.out.println("\033[31;4m" + "SPERecentData:"+SPERecentData + "\033[0m");

        if ((SPERecentData<maxThreshold) && (SPERecentData>minThreshold)) {

            System.out.println("\033[31;4m" + "cluster:true" + "\033[0m");
            map = RecursionUtil.meanAndSigmaRecursion(SPEmu,SPEsigma,SPERecentData,column);
            double SPEmuRecursion = Double.parseDouble(map.get("SPEmuRecursion").toString());
            double SPEsigmaRecursion = Double.parseDouble(map.get("SPEsigmaRecursion").toString());
            double[][] arrayX = X.getData();
            double[][] arrayxRecursion = new double[row][column+1];
            for (int i=0; i<row; i++) {
                for (int j=0; j<column; j++) {
                    arrayxRecursion[i][j] = arrayX[i][j];
                }
                arrayxRecursion[i][column] = normalizationRecentData[i];
            }
            RealMatrix xRecursion = new Array2DRowRealMatrix(arrayxRecursion);

            RealMatrix covMatrix = CovMatrixUtil.calculateMatrixCov(xRecursion);
            EigenDecomposition eigenDecomposition = new EigenDecomposition(covMatrix);
            double[] eigenValues = eigenDecomposition.getRealEigenvalues();
            double sum = 0;
            double total = 0;
            int count = 0;
            int eigenvectorLength = eigenValues.length;

            for (int m=0; m<eigenvectorLength; m++) {
                total += eigenValues[m];
            }
            for (int k=0; k<eigenvectorLength; k++) {
                if ((sum/total)>0.9){
                    break;
                }
                count ++;
                sum += eigenValues[k];
            }

            double[][] eigenvector2DArray = new double[count][eigenvectorLength];
            for (int l=0; l<count; l++) {
                RealVector realVector = eigenDecomposition.getEigenvector(l);
                eigenvector2DArray[l] = realVector.toArray();
            }
            RealMatrix eigenvectorRecursion = new Array2DRowRealMatrix(eigenvector2DArray);

            Detection detection1 = new Detection();
            detection1.setCluster_id(cluster_id);
            detection1.setMu(gson.toJson(muRecursion));
            detection1.setSigma(gson.toJson(sigmaRecursion));
            detection1.setSPEmu(gson.toJson(SPEmuRecursion));
            detection1.setSPEsigma(gson.toJson(SPEsigmaRecursion));
            detection1.setEigenvectorMatrix(gson.toJson(eigenvectorRecursion));
            detection1.setX(gson.toJson(xRecursion));
            detectionDao.updateDetectionParameters(detection1);

        } else {

            System.out.println("\033[31;4m" + "cluster:false" + "\033[0m");
            Record record = new Record();
            record.setCluster_id(cluster_id);
            record.setUpdate_time(updateTime);
            record.setRecentDataStr(recentDataStr);
            record.setSPERecentData(SPERecentData);
            record.setMaxThreshold(maxThreshold);
            record.setMinThreshold(minThreshold);
            recordDao.addDetectionRecords(record);

            /*这里的残差有两种计算方式：第一种是eigenvectorMatrix.multiply(normalizationRecentDataMatrix)
                    第二种是取eigenvectorMatrix中第i维与normalizationRecentDataMatrix第i维相乘取残差计算，
            下面用的是第一种，第二种对应每一维的数组因此可能性能更优，待研究。*/
            double[] remainArray = remainXVector.toArray();
            for (int j=0; j<row; j++) {
                remainArray[j] = remainArray[j]*remainArray[j];
            }

            for (int k=0; k<row; k++) {

                double sum1 = 0;
                double sum2 = 0;
                for (int s=0; s<row; s++) {
                    sum1 += remainArray[s];
                }
                double muOne = (sum1-remainArray[k])/(row-1);
                for (int a=0; a<row; a++) {
                    sum2 += Math.pow(remainArray[a]-muOne,2);
                }
                double sigmaOne = Math.sqrt((sum2-Math.pow(remainArray[k]-muOne,2))/(row-2));
                double maxOne = muOne + 1.96*sigmaOne;
                double minOne = muOne - 1.96*sigmaOne;

                if (remainArray[k]<maxOne && remainArray[k]>minOne) {
                    result[k] = 1;
                    System.out.println("\033[31;4m" + k+":true" + "\033[0m");
                } else {
                    result[k] = 0;
                    System.out.println("\033[31;4m" + k+":false" + "\033[0m");
                }

            }

            return result;

        }

        for (int i=0; i<result.length; i++) {
            result[i] = 1;
        }

        return result;

    }

    @Transactional
    public void updateParameter(String cluster_id, long updateTime, int record_id, double[] recentData) {

        Detection detection = detectionDao.getDetectionParameters(cluster_id);
        Gson gson = new Gson();
        double[] mu = gson.fromJson(detection.getMu(),double[].class);
        double[] sigma = gson.fromJson(detection.getSigma(),double[].class);
        double SPEmu = gson.fromJson(detection.getSPEmu(),double.class);
        double SPEsigma = gson.fromJson(detection.getSPEsigma(),double.class);
        RealMatrix eigenvectorMatrix = gson.fromJson(detection.getEigenvectorMatrix(),Array2DRowRealMatrix.class);
        RealMatrix X = gson.fromJson(detection.getX(),Array2DRowRealMatrix.class);

        int row = X.getRowDimension();
        int column = X.getColumnDimension();
        Map<String,Object> map = RecursionUtil.meanAndSigmaRecursion(recentData,mu,sigma,column);
        double[] muRecursion = (double[]) map.get("muRecursion");
        double[] sigmaRecursion = (double[]) map.get("sigmaRecursion");
        double[] normalizationRecentData = RecursionUtil.calculateNormalizationArray(recentData,muRecursion,sigmaRecursion);

        RealMatrix normalizationRecentDataMatrix = new Array2DRowRealMatrix(normalizationRecentData);
        RealMatrix Y = eigenvectorMatrix.multiply(normalizationRecentDataMatrix);
        RealMatrix eigenvectorMatrixT = eigenvectorMatrix.transpose();
        RealMatrix remainX = normalizationRecentDataMatrix.subtract(eigenvectorMatrixT.multiply(Y));
        RealVector remainXVector = remainX.getColumnVector(0);
        double normPow = remainXVector.getNorm();

        double SPERecentData = Math.pow(normPow,2);
        double maxThreshold = SPEmu + 1.96*SPEsigma;
        double minThreshold = SPEmu - 1.96*SPEsigma;
        String recentDataStr = Arrays.toString(recentData);

        System.out.println("\033[31;4m" + "maxThreshold:"+maxThreshold + "\033[0m");
        System.out.println("\033[31;4m" + "minThreshold:"+minThreshold + "\033[0m");
        System.out.println("\033[31;4m" + "SPERecentData:"+SPERecentData + "\033[0m");

        if ((SPERecentData<maxThreshold) && (SPERecentData>minThreshold)) {

            System.out.println("\033[31;4m" + "cluster:true" + "\033[0m");
            map = RecursionUtil.meanAndSigmaRecursion(SPEmu,SPEsigma,SPERecentData,column);
            double SPEmuRecursion = Double.parseDouble(map.get("SPEmuRecursion").toString());
            double SPEsigmaRecursion = Double.parseDouble(map.get("SPEsigmaRecursion").toString());
            double[][] arrayX = X.getData();
            double[][] arrayxRecursion = new double[row][column+1];
            for (int i=0; i<row; i++) {
                for (int j=0; j<column; j++) {
                    arrayxRecursion[i][j] = arrayX[i][j];
                }
                arrayxRecursion[i][column] = normalizationRecentData[i];
            }
            RealMatrix xRecursion = new Array2DRowRealMatrix(arrayxRecursion);

            RealMatrix covMatrix = CovMatrixUtil.calculateMatrixCov(xRecursion);
            EigenDecomposition eigenDecomposition = new EigenDecomposition(covMatrix);
            double[] eigenValues = eigenDecomposition.getRealEigenvalues();
            double sum = 0;
            double total = 0;
            int count = 0;
            int eigenvectorLength = eigenValues.length;

            for (int m=0; m<eigenvectorLength; m++) {
                total += eigenValues[m];
            }
            for (int k=0; k<eigenvectorLength; k++) {
                if ((sum/total)>0.9){
                    break;
                }
                count ++;
                sum += eigenValues[k];
            }

            double[][] eigenvector2DArray = new double[count][eigenvectorLength];
            for (int l=0; l<count; l++) {
                RealVector realVector = eigenDecomposition.getEigenvector(l);
                eigenvector2DArray[l] = realVector.toArray();
            }
            RealMatrix eigenvectorRecursion = new Array2DRowRealMatrix(eigenvector2DArray);

            Detection detection1 = new Detection();
            detection1.setCluster_id(cluster_id);
            detection1.setMu(gson.toJson(muRecursion));
            detection1.setSigma(gson.toJson(sigmaRecursion));
            detection1.setSPEmu(gson.toJson(SPEmuRecursion));
            detection1.setSPEsigma(gson.toJson(SPEsigmaRecursion));
            detection1.setEigenvectorMatrix(gson.toJson(eigenvectorRecursion));
            detection1.setX(gson.toJson(xRecursion));
            detectionDao.updateDetectionParameters(detection1);

        } else {

            System.out.println("\033[31;4m" + "cluster:false" + "\033[0m");
            Record record = new Record();
            record.setCluster_id(cluster_id);
            record.setUpdate_time(updateTime);
            record.setRecord_id(record_id);
            record.setRecentDataStr(recentDataStr);
            record.setSPERecentData(SPERecentData);
            record.setMaxThreshold(maxThreshold);
            record.setMinThreshold(minThreshold);
            recordDao.addDetectionRecords(record);

            /*这里的残差有两种计算方式：第一种是eigenvectorMatrix.multiply(normalizationRecentDataMatrix)
                    第二种是取eigenvectorMatrix中第i维与normalizationRecentDataMatrix第i维相乘取残差计算，
            下面用的是第一种，第二种对应每一维的数组因此可能性能更优，待研究。*/
            double[] remainArray = remainXVector.toArray();
            for (int j=0; j<row; j++) {
                remainArray[j] = remainArray[j]*remainArray[j];
            }

            for (int k=0; k<row; k++) {

                double sum1 = 0;
                double sum2 = 0;
                for (int s=0; s<row; s++) {
                    sum1 += remainArray[s];
                }
                double muOne = (sum1-remainArray[k])/(row-1);
                for (int a=0; a<row; a++) {
                    sum2 += Math.pow(remainArray[a]-muOne,2);
                }
                double sigmaOne = Math.sqrt((sum2-Math.pow(remainArray[k]-muOne,2))/(row-2));
                double maxOne = muOne + 1.96*sigmaOne;
                double minOne = muOne - 1.96*sigmaOne;

                if (remainArray[k]<maxOne && remainArray[k]>minOne) {
                    System.out.println("\033[31;4m" + k+":true" + "\033[0m");
                } else {
                    System.out.println("\033[31;4m" + k+":false" + "\033[0m");
                }

            }

        }

    }

    /*public static void main(String[] args) {

        ApplicationContext ctx = ApplicationContextUtil.getApplicationContext();

        InitParameterService initParameterService = (InitParameterService) ctx.getBean("initParameterService");
        initParameterService.initParameter();

        Gson gson = new Gson();
        UpdateParameterService updateParameterService = (UpdateParameterService) ctx.getBean("updateParameterService");
        updateParameterService.updateParameter("111",111,"cluster_1");
    }*/
}
