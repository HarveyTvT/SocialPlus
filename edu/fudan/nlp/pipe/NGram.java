package edu.fudan.nlp.pipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.fudan.ml.types.Instance;
import edu.fudan.util.exception.UnsupportedDataTypeException;

/**
 * 由输入序列生成ngram特征，data类型转为List
 * 输入数据类型：String[],List<String>,String
 * 输出数据类型：List<String>
 * @author xpqiu
 *
 */
public class NGram extends Pipe{

    private static final long serialVersionUID = -2329969202592736092L;
    int[] gramSizes = null;

    public NGram(int[] sizes) {
        this.gramSizes = sizes;
    }

    @Override
    public void addThruPipe(Instance inst) throws UnsupportedDataTypeException {
        Object data = inst.getData();
        ArrayList<String> list = null;
        if (data instanceof String) {
            list = ngram((String) data,gramSizes);
        }else if (data instanceof List) {
            list = ngram((List) data,gramSizes);
        }else if(data instanceof String[]){
            list = ngram((String[]) data,gramSizes);
        }else{
            throw new UnsupportedDataTypeException("不支持处理数据类型："+data.getClass().toString());
        }
        inst.setData(list);
    }
    
    
    
    private ArrayList<String> ngram(String[] strs, int[] grams) {
        ArrayList<String> list = new ArrayList<String>();
        StringBuffer buf = new StringBuffer();
        for (int j = 0; j < gramSizes.length; j++) {
            int len = gramSizes[j];
            if (len <= 0 || len > strs.length)
                continue;
            for (int i = 0; i < strs.length - len+1; i++) {
                buf.delete(0, buf.length());
                int k = 0;
                for(; k < len-1; ++k)   {
                    buf.append(strs[i+k]);
                    buf.append(' ');
                }
                buf.append(strs[i+k]);
                list.add(buf.toString().intern());
            }
        }
        return list;
    }

    private ArrayList<String> ngram(List tokens, int[] gramSizes2) {
        ArrayList<String> list = new ArrayList<String>();
        StringBuffer buf = new StringBuffer();
        for (int j = 0; j < gramSizes.length; j++) {
            int len = gramSizes[j];
            if (len <= 0 || len > tokens.size())
                continue;
            for (int i = 0; i < tokens.size() - len+1; i++) {
                buf.delete(0, buf.length());
                int k = 0;
                for(; k < len-1; ++k)   {
                    buf.append(tokens.get(i+k));
                    buf.append(' ');
                }
                buf.append(tokens.get(i+k));
                list.add(buf.toString().intern());
            }
        }
        return list;
    }

    /**
     * 提取ngram
     * @param data
     * @param gramSizes
     * @return ngram字符串数组
     */
    public static ArrayList<String> ngram(String data,int[] gramSizes) {
        // 提取ngram
        ArrayList<String> list = new ArrayList<String>();
        ngram(data, gramSizes, list);
        return list;
    }
    
    /**
     * 提取ngram
     * @param data
     * @param gramSizes
     * @return ngram字符串集合
     */
    public static Set<String> ngramSet(String data,int[] gramSizes) {
        // 提取ngram
        Set<String> list = new HashSet<String>();
        ngram(data, gramSizes, list);
        return list;
    }

    private static void ngram(String data, int[] gramSizes, Collection<String> list) {
        for (int j = 0; j < gramSizes.length; j++) {
            int len = gramSizes[j];
            if (len <= 0 || len > data.length())
                continue;
            for (int i = 0; i < data.length() - len; i++) {
                list.add(data.substring(i, i + len));
            }
        }
    }
    
}
