package org.bupt.util;

import org.apache.commons.math3.linear.RealMatrix;

public class MatrixSigmaUtil {

    public static double calculateMatrixSigma(RealMatrix realMatrix,double mu) {

        int column = realMatrix.getColumnDimension();
        double sum = 0;

        for (int i=0; i<column; i++) {
            double d_value = realMatrix.getEntry(0,i) - mu;
            sum = sum + Math.pow(d_value,2);
        }

        return Math.sqrt(sum/(column-1));

    }

    public static double calculateArraySigma(double[] array,double mu) {

        int column = array.length;
        double sum = 0;

        for (int i=0; i<column; i++) {
            double d_value = array[i] - mu;
            sum = sum + Math.pow(d_value,2);
        }

        return Math.sqrt(sum/(column-1));

    }

}
