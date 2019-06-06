package org.bupt.util;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

public class CovMatrixUtil {

    private static double calculateTwoVectorCov(RealMatrix realMatrix1, RealMatrix realMatrix2, double mu1, double mu2) {

        int column1 = realMatrix1.getColumnDimension();
        int column2 = realMatrix2.getColumnDimension();
        if (column1 != column2) throw new IllegalArgumentException("The vector's length is not equal!");
        double sum = 0;

        for (int i=0; i<column1; i++) {
            sum += (realMatrix1.getEntry(0,i)-mu1) * (realMatrix2.getEntry(0,i)-mu2);
        }
        return sum/(column1-1);
    }

    public static RealMatrix calculateMatrixCov(RealMatrix realMatrix) {

        int row = realMatrix.getRowDimension();
        double[][] cov = new double[row][row];
        double[] mu = new double[row];

        for (int k=0; k<row; k++) {
            mu[k] = MatrixMeanUtil.calculateMatrixMean(realMatrix.getRowMatrix(k));
        }

        for (int i=0; i<row; i++) {
            for (int j=0; j<row; j++) {
                cov[i][j] = calculateTwoVectorCov(realMatrix.getRowMatrix(i),realMatrix.getRowMatrix(j),mu[i],mu[j]);
            }
        }
        return new Array2DRowRealMatrix(cov);
    }

}
