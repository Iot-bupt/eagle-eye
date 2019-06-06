package org.bupt.util;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RecursionUtil {

    public static Map<String,Object> meanAndSigmaRecursion(double[] recentData, double[] mu, double[] sigma, int column) {

        for (int i=0; i<recentData.length; i++) {
            mu[i] = mu[i]*column/(column+1) + recentData[i]/(column+1);
            sigma[i] = Math.sqrt(Math.pow(sigma[i],2)*column/(column+1)+Math.pow(recentData[i]-mu[i],2)/(column+1));
        }

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("muRecursion",mu);
        map.put("sigmaRecursion",sigma);

        return map;
    }

    public static Map<String,Object> meanAndSigmaRecursion(double SPEmu, double SPEsigma, double SPERecentData, int column) {

        SPEmu = SPEmu*column/(column+1) + SPERecentData/(column+1);
        SPEsigma = Math.sqrt(Math.pow(SPEsigma,2)*column/(column+1)+Math.pow(SPERecentData-SPEmu,2)/(column+1));

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("SPEmuRecursion",SPEmu);
        map.put("SPEsigmaRecursion",SPEsigma);

        return map;
    }

    public static double[] calculateNormalizationArray(double[] recentData, double[] mu, double[] sigma) {

        double[] normalizationRecentData = new double[recentData.length];

        for (int i=0; i<recentData.length; i++) {
            normalizationRecentData[i] = (recentData[i]-mu[i])/sigma[i];
        }

        return normalizationRecentData;
    }
}
