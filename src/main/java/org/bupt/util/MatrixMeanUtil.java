package org.bupt.util;

import org.apache.commons.math3.linear.RealMatrix;

public class MatrixMeanUtil {

    public static double calculateMatrixMean(RealMatrix realMatrix) {

        int column = realMatrix.getColumnDimension();
        double sum = 0;

        for (int i=0; i<column; i++) {
            sum = sum + realMatrix.getEntry(0,i);
        }
        return sum/column;
    }

    public static double calculateArrayMean(double[] array) {

        int column = array.length;
        double sum = 0;

        for (int i=0; i<column; i++) {
            sum = sum + array[i];
        }
        return sum/column;
    }

}
