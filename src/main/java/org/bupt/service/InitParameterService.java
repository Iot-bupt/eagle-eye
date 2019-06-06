package org.bupt.service;

import org.apache.commons.math3.linear.*;
import org.bupt.util.CovMatrixUtil;
import org.bupt.util.MatrixMeanUtil;
import org.bupt.util.MatrixSigmaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class InitParameterService {

    @Autowired
    private SaveParameterService saveParameterService;

    public void initParameter(double[][] x, String cluster_id) {

        /*double[][] x = new double[][]{{1,1,1,1,1.1}, {2,2,2,2,2.2}, {3,3,3,3,3.3}, {4,4,4,4,4.4}};*/
        RealMatrix X = new Array2DRowRealMatrix(x);
        System.out.println("initParameter X;"+X);
        int row = X.getRowDimension();
        int column = X.getColumnDimension();
        double[] mu = new double[row];
        double[] sigma = new double[row];

        for (int i=0; i<row; i++) {
            RealMatrix xVector = X.getRowMatrix(i);
            mu[i] = MatrixMeanUtil.calculateMatrixMean(xVector);
            double x_bar;
            sigma[i] = MatrixSigmaUtil.calculateMatrixSigma(xVector,mu[i]);
            for (int j=0; j<column; j++) {
                x_bar = (xVector.getEntry(0,j)-mu[i])/sigma[i];
                X.setEntry(i,j,x_bar);
            }
        }

        RealMatrix covMatrix = CovMatrixUtil.calculateMatrixCov(X);

        EigenDecomposition eigenDecomposition = new EigenDecomposition(covMatrix);
        double[] eigenValues = eigenDecomposition.getRealEigenvalues();
        double sum = 0;
        double total = 0;
        int count = 0;
        int eigenvectorLength = eigenValues.length;

        for (int m=0; m<eigenvectorLength; m++) {
            total = total+eigenValues[m];
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
        RealMatrix eigenvectorMatrix = new Array2DRowRealMatrix(eigenvector2DArray);

        RealMatrix Y = eigenvectorMatrix.multiply(X);
        RealMatrix X1 = eigenvectorMatrix.transpose().multiply(Y);
        RealMatrix remainValueMatrix = X.subtract(X1);

        double[] SPE = new double[column];
        for (int s=0; s<column; s++) {
            RealVector remainValueVector = remainValueMatrix.getColumnVector(s);
            double norm2 = remainValueVector.getNorm();
            SPE[s] = Math.pow(norm2,2);
        }
        double SPEmu = MatrixMeanUtil.calculateArrayMean(SPE);
        double SPEsigma = MatrixSigmaUtil.calculateArraySigma(SPE,SPEmu);

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("mu",mu);
        map.put("sigma",sigma);
        map.put("SPEmu",SPEmu);
        map.put("SPEsigma",SPEsigma);
        map.put("X",X);
        map.put("eigenvectorMatrix",eigenvectorMatrix);
        saveParameterService.saveInitParameter(map,cluster_id);

    }

    /*public static void main(String[] args) {

        ApplicationContext ctx = ApplicationContextUtil.getApplicationContext();
        InitParameterService initParameterService = (InitParameterService) ctx.getBean("initParameterService");

        Map<String,Object> map;
        map = initParameterService.initParameter();
        double[] mu = (double[]) map.get("mu");
        double[] sigma = (double[]) map.get("sigma");
        double SPEmu = Double.parseDouble(map.get("SPEmu").toString());
        double SPEsigma = Double.parseDouble(map.get("SPEsigma").toString());
        RealMatrix X = (Array2DRowRealMatrix) map.get("X");
        RealMatrix eigenvectorMatrix = (Array2DRowRealMatrix) map.get("eigenvectorMatrix");
        String device_id = "111";
        String cluster_id = "222";

        Gson gson = new Gson();
        String mustr = gson.toJson(mu);
        String sigmastr = gson.toJson(sigma);
        String SPEmustr = gson.toJson(SPEmu);
        String SPEsigmastr = gson.toJson(SPEsigma);
        String Xstr = gson.toJson(X);
        String eigenvectorMatrixstr = gson.toJson(eigenvectorMatrix);

        Detection detection = new Detection(mustr,sigmastr,SPEmustr,SPEsigmastr,eigenvectorMatrixstr,Xstr,device_id,cluster_id);
        System.out.println(initParameterService.detectionDao.addDetectionParameters(detection));

    }*/

    /*public static void main(String[] args) {

        ApplicationContext ctx = ApplicationContextUtil.getApplicationContext();
        InitParameterService initParameterService = (InitParameterService) ctx.getBean("initParameterService");

        Detection detection = initParameterService.detectionDao.getDetectionParameters("111");
        System.out.println(detection);
        Gson gson = new Gson();
        double SPEmu = gson.fromJson(detection.getSPEmu(),double.class);
        System.out.println(SPEmu);
        double[] mu = gson.fromJson(detection.getMu(),double[].class);
        System.out.println(Arrays.toString(mu));
        RealMatrix eigenvectorMatrixgson = gson.fromJson(detection.getEigenvectorMatrix(),Array2DRowRealMatrix.class);
        System.out.println(eigenvectorMatrixgson);
    }*/

    /*public static void main(String[] args) {

        ApplicationContext ctx = ApplicationContextUtil.getApplicationContext();
        InitParameterService initParameterService = (InitParameterService) ctx.getBean("initParameterService");

        Map<String,Object> map;
        map = initParameterService.initParameter();
        double[] mudb = (double[]) map.get("mu");

        Gson gson = new Gson();
        String a = gson.toJson(mudb);

        boolean flag = initParameterService.detectionDao.addDetectionParameters(new Detection(a));
        System.out.println(flag);

    }*/

    /*public static void main(String[] args) {

        ApplicationContext ctx = ApplicationContextUtil.getApplicationContext();
        InitParameterService initParameterService = (InitParameterService) ctx.getBean("initParameterService");

        Detection detection = initParameterService.detectionDao.getDetectionParameters(3);
        String a = detection.getMu();

        Gson gson = new Gson();
        double[] b = gson.fromJson(a,double[].class);
        System.out.println(Arrays.toString(b));

    }*/

    public static void main(String[] args) {

        int a = -((1 << 2) - 1);
        int b = 15;
        System.out.println(Integer.toBinaryString(a));
        System.out.println(Integer.toBinaryString(b));
        System.out.println(Integer.toBinaryString(a & b));

        int c = 2^31 - 3;
        System.out.println(Integer.toBinaryString(c));
        System.out.println(Integer.toBinaryString(-c));
        System.out.println(c & (-c));

        System.out.println(Math.pow(64,0.3));

        int d = 496 & 495;
        System.out.println(Integer.toBinaryString(d));
        System.out.println(d);

        System.out.println("\nHashMap\n");

        Map<Integer,Integer> map = new HashMap<Integer, Integer>();
        map.put(3,1);
        map.put(1,10);
        map.put(1,2);
        map.put(2,2);

        System.out.println(map);
        System.out.println(map.isEmpty());
        System.out.println(map.containsKey(3));
        System.out.println(map.get(3));
        System.out.println(map.remove(2));

        Set<Map.Entry<Integer,Integer>> set = map.entrySet();
        System.out.println(set);
        for (Map.Entry<Integer,Integer> entry : set) {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
        }
        System.out.println("----");
        System.out.println(map.get(1));

        System.out.println("\nTreeMap\n");

        Map<Integer,Integer> map1 = new TreeMap<Integer, Integer>();
        map1.put(3,1);
        map1.put(1,10);
        map1.put(1,2);
        map1.put(2,2);

        System.out.println(map1);
        System.out.println(map1.isEmpty());
        System.out.println(map1.containsKey(3));
        System.out.println(map1.get(3));
        System.out.println(map1.remove(2));

        Set<Map.Entry<Integer,Integer>> set1 = map1.entrySet();
        System.out.println(set1);
        for (Map.Entry<Integer,Integer> entry : set1) {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
        }

        System.out.println("\nHashSet\n");

        Set<String> set2 = new HashSet<String>();
        set2.add("a");
        set2.add("a");
        set2.add("b");
        System.out.println(set2);
        set2.remove("b");
        System.out.println(set2);
        for (String string : set2) {
            System.out.println(string);
        }

        System.out.println("\nArrayList\n");

        List<String> list = new ArrayList<String>();
        list.add("a");
        list.add("a");
        list.add("b");
        list.set(0,"d");
        list.add(2,"c");
        System.out.println(list);
        list.remove("a");
        Collections.sort(list);
        System.out.println(list);
        for (String string : list) {
            System.out.println(string);
        }

        System.out.println("\nVector\n");

        Vector<String> vector = new Vector<String>();
        vector.add("a");
        vector.add("a");
        vector.add("b");
        vector.set(0,"d");
        vector.add(2,"c");
        System.out.println(vector);
        vector.remove("a");
        Collections.sort(vector);
        System.out.println(vector);
        for (String string : vector) {
            System.out.println(string);
        }

        System.out.println("\nTreeSet\n");

        TreeSet<String> treeSet = new TreeSet<String>();
        treeSet.add("c");
        treeSet.add("a");
        treeSet.add("e");
        System.out.println(treeSet);
        System.out.println(treeSet.first());
        for (String string : treeSet) {
            System.out.println(string);
        }

        System.out.println("\nLinkedList\n");

        LinkedList<String> linkedList = new LinkedList<String>();
        linkedList.add("cba");
        linkedList.add("abc");
        linkedList.add("bca");
        System.out.println(linkedList);
        linkedList.pop();
        System.out.println(linkedList);
        linkedList.push("dcb");
        System.out.println(linkedList);
        linkedList.push("eac");
        System.out.println(linkedList);
        linkedList.offer("fba");
        System.out.println(linkedList);
        System.out.println(linkedList.poll());
        System.out.println(linkedList);
        System.out.println(linkedList.peek());;
        System.out.println(linkedList);

    }

}
