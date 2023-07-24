package com.seah.specialsteel.service;

import org.springframework.stereotype.Service;

@Service
public class MinMaxScalerService {


        public double[] minMaxScale(double[] data, double min, double max) {
            double[] scaledData = new double[data.length];
            double dataMin = getMin(data);
            double dataMax = getMax(data);

            for (int i = 0; i < data.length; i++) {
                scaledData[i] = ((data[i] - dataMin) / (dataMax - dataMin)) * (max - min) + min;
            }

            return scaledData;
        }

        private double getMin(double[] data) {
            double min = data[0];
            for (double value : data) {
                if (value < min) {
                    min = value;
                }
            }
            return min;
        }

        private double getMax(double[] data) {
            double max = data[0];
            for (double value : data) {
                if (value > max) {
                    max = value;
                }
            }
            return max;
        }

//        public void main(String[] args) {
//            double min = 0;
//            double max = 1;
//
//            double[] scaledData = minMaxScale(data, min, max);
//
//            // 출력
//            for (double value : scaledData) {
//                System.out.println(value);
//            }

//    }

    public double[] inverseMinMaxScale(double[] scaledData, double min, double max) {
        double[] originalData = new double[scaledData.length];
        double dataMin = getMin(originalData);
        double dataMax = getMax(originalData);

        for (int i = 0; i < scaledData.length; i++) {
            originalData[i] = ((scaledData[i] - min) / (max - min)) * (dataMax - dataMin) + dataMin;
        }

        return originalData;
    }

//    public static void main(String[] args) {
//        double[] data = {10, 20, 30, 40, 50};
//        double min = 0;
//        double max = 1;
//
//        double[] scaledData = minMaxScale(data, min, max);
//
//        // 원래 데이터로 복원
//        double[] originalData = inverseMinMaxScale(scaledData, min, max);
//
//        // 출력
//        for (double value : originalData) {
//            System.out.println(value);
//        }
//    }

}
