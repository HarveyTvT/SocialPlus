package edu.fudan.nlp.cn.tag.format;

import java.util.ArrayList;
import java.util.List;

import edu.fudan.ml.types.Instance;
import edu.fudan.ml.types.InstanceSet;
/**
 * 将序列标注结果转换成List输出
 * 将BMES标签转为成词的序列
 * @author Administrator
 *
 */
public class FormatCWS {

	public static ArrayList<String> toList(Instance inst, String[] labels) {
		String[][] data = (String[][]) inst.getSource();
		int len = data[0].length;
		ArrayList<String> res = new ArrayList<String>(len);
		StringBuilder sb = new StringBuilder();
		for (int j = 0; j < len; j++) {
			String label = labels[j];
			String w = data[0][j];
			if(data[1][j].equals("B")){//空格特殊处理
				if(sb.length()>0){
					res.add(sb.toString());
					sb = new StringBuilder();
				}
				continue;
			}
			sb.append(w);
			if (label.equals("E") || label.equals("S")) {
				res.add(sb.toString());
				sb = new StringBuilder();
//			}else if(j<len-1&&data[1][j].equals("C")&&(data[1][j+1].endsWith("L")||data[1][j+1].endsWith("D"))){
//				res.add(sb.toString());
//				sb = new StringBuilder();
//			}
//			else if(j<len-1&&data[1][j+1].equals("C")&&(data[1][j].endsWith("L"))){
//				res.add(sb.toString());
//				sb = new StringBuilder();
			}
		}
		if(sb.length()>0){
			res.add(sb.toString());
		}
		return res;
	}
	/**
	 * 将BMES标签转为#delim#隔开的字符串
	 * @param instSet 样本集
	 * @param labelsSet  标签集
	 * @param delim 字之间的间隔符
	 * @return
	 */
	public static String toString(InstanceSet instSet, String[][] labelsSet, String delim) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < instSet.size(); i++) {
			Instance inst = instSet.getInstance(i);
			String[] labels = labelsSet[i];
			sb.append(toString(inst, labels,delim));
			sb.append("\n");
		}
		return sb.toString();
	}

	/**
	 * 将BMES标签转为#delim#隔开的字符串
	 * @param inst 样本
	 * @param labels  标签
	 * @param delim 字之间的间隔符
	 * @return
	 */
	public static String toString(Instance inst, String[] labels,String delim) {
		String[][] data = (String[][]) inst.getSource();
		int len = data[0].length;
		StringBuilder sb = new StringBuilder();
		for (int j = 0; j < len-1; j++) {
			String label = labels[j];
			String w = data[0][j];			
			sb.append(w);
			if(data[1][j].equals("B")||data[1][j+1].equals("B"))
				continue;
			else if (label.equals("E") || label.equals("S")) {
				sb.append(delim);
//			}else if(data[1][j].equals("C")&&(data[1][j+1].endsWith("L")||data[1][j+1].endsWith("D"))){
//				sb.append(delim);
//			}else if(data[1][j+1].equals("C")&&(data[1][j].endsWith("L"))){//||data[1][j].endsWith("D")
//				sb.append(delim);
			}
		}
		sb.append(data[0][len-1]);
		return sb.toString();
	}
	
}
