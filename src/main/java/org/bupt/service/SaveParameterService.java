package org.bupt.service;

import com.google.gson.Gson;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.bupt.bean.Detection;
import org.bupt.dao.DetectionImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SaveParameterService {

    @Autowired
    private DetectionImpl detectionDao;

    public void saveInitParameter(Map<String,Object> map, String cluster_id) {

        double[] mu = (double[]) map.get("mu");
        double[] sigma = (double[]) map.get("sigma");
        double SPEmu = Double.parseDouble(map.get("SPEmu").toString());
        double SPEsigma = Double.parseDouble(map.get("SPEsigma").toString());
        RealMatrix X = (Array2DRowRealMatrix) map.get("X");
        RealMatrix eigenvectorMatrix = (Array2DRowRealMatrix) map.get("eigenvectorMatrix");

        Gson gson = new Gson();
        String mustr = gson.toJson(mu);
        String sigmastr = gson.toJson(sigma);
        String SPEmustr = gson.toJson(SPEmu);
        String SPEsigmastr = gson.toJson(SPEsigma);
        String Xstr = gson.toJson(X);
        String eigenvectorMatrixstr = gson.toJson(eigenvectorMatrix);

        Detection detection = new Detection();
        detection.setMu(mustr);
        detection.setSigma(sigmastr);
        detection.setSPEmu(SPEmustr);
        detection.setSPEsigma(SPEsigmastr);
        detection.setEigenvectorMatrix(eigenvectorMatrixstr);
        detection.setX(Xstr);
        detection.setCluster_id(cluster_id);

        detectionDao.addDetectionParameters(detection);
    }

}
