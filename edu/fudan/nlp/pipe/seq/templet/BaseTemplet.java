package edu.fudan.nlp.pipe.seq.templet;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.fudan.ml.types.Instance;
import edu.fudan.ml.types.alphabet.IFeatureAlphabet;

/**
 * 类CRF模板 
 * 格式为： 
 *  0: %x[-1,0]%x[1,0]%y[0];
 *  1: %y[-1]%y[0]
 * @author Feng Ji
 */
public class BaseTemplet implements Templet {

	private static final long serialVersionUID = 7543856094273600355L;
	Pattern parser = Pattern.compile("(?:%(x|y)\\[(-?\\d+)(?:,(\\d+))?\\])");
	String templet;
	/**
	 * 模板阶数
	 */
	int order;
	int id;
	/**
	 * x相对位置
	 */
	int[][] dims;
	/**
	 * y相对位置
	 */
	int[] vars;
	/**
	 * 对于某些情况不返回特定信息
	 */
	public int minLen = 0;

	/**
	 * 构造函数
	 * 
	 * @param templet
	 *            模板字符串 格式如：%x[0,0]%y[0]; %y[-1]%y[0]; %x[0,0]%y[-1]%y[0]
	 */
	public BaseTemplet(int id, String templet) {
		this.id = id;
		this.templet = templet;
		Matcher matcher = parser.matcher(this.templet);
		/**
		 * 解析y的位置
		 */
		List<String> l = new ArrayList<String>();
		List<String> x = new ArrayList<String>();
		while (matcher.find()) {
			if (matcher.group(1).equalsIgnoreCase("y")) {
				l.add(matcher.group(2));
			} else if (matcher.group(1).equalsIgnoreCase("x")) {
				x.add(matcher.group(2));
				x.add(matcher.group(3));
			}
		}
		if(l.size()==0){//兼容CRF++模板
			vars = new int[]{0};
		}else{
			vars = new int[l.size()];
			for (int j = 0; j < l.size(); j++) {
				vars[j] = Integer.parseInt(l.get(j));
			}
		}
		order = vars.length - 1;
		l = null;

		dims = new int[x.size() / 2][2];
		for (int i = 0; i < x.size(); i += 2) {
			dims[i / 2][0] = Integer.parseInt(x.get(i));
			dims[i / 2][1] = Integer.parseInt(x.get(i + 1));
		}
		x = null;
	}

	/**
	 * 得到y相对位置
	 */
	public int[] getVars() {
		return this.vars;
	}

	/**
	 * 得到模板阶数
	 */
	public int getOrder() {
		return this.order;
	}

	/**
	 * {@inheritDoc}
	 */
	public int generateAt(Instance instance, IFeatureAlphabet features, int pos,
			int... numLabels) throws Exception {
		assert (numLabels.length == 1);

		String[][] data = (String[][]) instance.getData();
		int len = data[0].length;

		if(order>0&& len==1) //对于长度为1的序列，不考虑1阶以上特征
			return -1;

		//		if(isForceTrain){
		//			if(len<minLen && order>0 )//训练时，对于长度过小的句子，不考虑开始、结束特征
		//				return -1;
		//		}
		StringBuilder sb = new StringBuilder();
		sb.append(id);
		sb.append(':');
		for (int i = 0; i < dims.length; i++) {
			int j = dims[i][0]; //行号
			int k = dims[i][1]; //列号
			if(k>data.length-1)
				return -1;
			int ii = pos + j;
			int iii = ii;
			if (ii < 0){
				if(len<minLen )//对于长度过小的句子，不考虑开始、结束特征
					return -1;
				while(iii++<0){
					sb.append("B");
				}
				sb.append("_");
			}else if (ii >= len){
				if(len<minLen )//对于长度过小的句子，不考虑开始、结束特征
					return -1;
				while(iii-->=len){
					sb.append("E");
				}
				sb.append("_");
			} else {
				sb.append(data[k][pos + j]); //这里数据行列和模板中行列相反
			}
			sb.append("/");
		}
		int index = features.lookupIndex(sb.toString(), (int) Math.pow(numLabels[0], order + 1));
		return index;
	}

	public String toString() {
		return this.templet;
	}

	public int offset(int... curs) {
		return 0;
	}
}
