package edu.fudan.ml.types.featurecluster;

import java.text.DecimalFormat;

public class JSDistance extends AbstractDistance {
    double max = 999999;
    DecimalFormat df = new DecimalFormat("0.###E0");

    public double cal(ClassData cd1, ClassData cd2) {
        if (checkLabelLength(cd1, cd2)) 
            return calJSDistance(cd1, cd2);
        else
            return Double.MAX_VALUE;
    }

    private boolean checkLabelLength(ClassData cd1, ClassData cd2) {
        return cd1.getLabel().length == cd2.getLabel().length;
    }

    private double calJSDistance(ClassData cd1, ClassData cd2) {
        double p1 = (double)cd1.getCount() / (double)cd1.getAllCount();
        double p2 = (double)cd2.getCount() / (double)cd2.getAllCount();
        double[] averageLabel = calAverageLabel(cd1, cd2);
        double kl1 = klDistance(cd1.getLabel(), averageLabel);
        double kl2 = klDistance(cd2.getLabel(), averageLabel);
        double distance = p1 * kl1 + p2 * kl2;
//        distance = format(distance);
        return distance;
    }

    private double klDistance(double[] label1, double[] label2) {
        double distance = 0;
        for (int i = 0; i < label1.length; i++) {
            if (label1[i] == 0)
                continue;
            else if (label2[i] == 0)
                distance += max;
            else {
                double tempDistance = label1[i] * Math.log(label1[i] / label2[i]);
                distance += tempDistance;
            }
        }
        return distance;
    }

    private double[] calAverageLabel(ClassData cd1, ClassData cd2) {
        double[] label1 = cd1.getLabel();
        double[] label2 = cd2.getLabel();
        int length = label1.length;
        double[] label = new double[length];
        double pi = (double)cd1.getCount() / (double)(cd1.getCount() + cd2.getCount());
        for (int i = 0; i < length; i++)
            label[i] = pi * label1[i] + (1-pi) * label2[i];
        return label;
    }

    private double format (double v) {
        String s = df.format(v);
        return Double.parseDouble(s);
    }
}
