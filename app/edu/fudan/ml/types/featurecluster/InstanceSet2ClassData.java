package edu.fudan.ml.types.featurecluster;

import java.util.ArrayList;

import edu.fudan.ml.types.Instance;
import edu.fudan.ml.types.InstanceSet;

import gnu.trove.map.hash.TIntObjectHashMap;

public class InstanceSet2ClassData {
    private InstanceSet instanceSet;
    private ArrayList<ClassData> classdataList;
    private TIntObjectHashMap<String> index;
    private int[] counts;
    private int labelSize;

    public InstanceSet2ClassData(InstanceSet instanceSet, TIntObjectHashMap<String> index, int feaSize, int labelSize) {
        this.instanceSet = instanceSet;
        this.labelSize = labelSize;
        this.index = index;
        classdataList = new ArrayList<ClassData>();
        counts = new int[feaSize];
        calCounts();
        genClassData();
    }

    /**
     * @return the classdataList
     */
    public ArrayList<ClassData> getClassdataList() {
        return classdataList;
    }

    private void calCounts() {
        for (int ii = 0; ii < instanceSet.size(); ii++) {
            Instance inst = instanceSet.getInstance(ii);
            int[][] data = (int[][]) inst.getData();
            int[] golds = (int[]) inst.getTarget();
            
            for (int i = 0; i < data.length; i++) {
                for (int j = 0; j < data[0].length; j++) {
                    int idx = data[i][j] + golds[i];
                    if (idx >= 0)
                        counts[idx]++;
                }
            }
        }
    }

    private void genClassData() {
        for (int i = 0; i < counts.length;) {
            int key = i;
            int indent = getIndent(i);
            double[] label = genLabel(i, i + indent);
            if (checkZero(label)) {
                int c = getLabelCount(label);
                ClassData cd = new ClassData(key, label, c);
                classdataList.add(cd);
            }
            i += indent;
        }
    }

    private double[] genLabel(int p, int q) {
        double[] label = new double[q-p];
        for (int i = p, j = 0; i < q; i++, j++)
            label[j] = (double)counts[i];
        return label;
    }

    private int getLabelCount(double[] label) {
        double c = 0;
        for (int i = 0; i < label.length; i++) {
            c += label[i];
        }
        return (int)c;
    }

    private boolean checkZero(double[] label) {
        for (double data : label) {
            if (data != 0)
                return true;
        }
        System.out.println("Error: zero");
        return false;
    }

    private int getIndent(int id) {
        String feature = index.get(id);
        if (feature.startsWith("0"))
            return labelSize * labelSize;
        else
            return labelSize;
    }
}
