package edu.fudan.ml.classifier.knn;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import edu.fudan.ml.classifier.AbstractClassifier;
import edu.fudan.ml.classifier.LabelParser.Type;
import edu.fudan.ml.classifier.linear.Linear;
import edu.fudan.ml.classifier.LinkedPredict;
import edu.fudan.ml.classifier.Predict;
import edu.fudan.ml.classifier.TPredict;
import edu.fudan.ml.types.Instance;
import edu.fudan.ml.types.InstanceSet;
import edu.fudan.nlp.pipe.Pipe;
import edu.fudan.nlp.pipe.String2Dep;
import edu.fudan.nlp.similarity.ISimilarity;
import edu.fudan.util.exception.LoadModelException;

public class KNN extends AbstractClassifier{

	private static final long serialVersionUID = 4459814160943364300L;

	private ISimilarity sim;

	public ISimilarity getSim() {
		return sim;
	}

	public void setSim(ISimilarity sim) {
		this.sim = sim;
	}

	private int k;

	/**
	 * 特征转换器
	 */
	protected Pipe pipe;

	/**
	 * KNN模型
	 */
	protected InstanceSet prototypes;

	private boolean useScore = true; 

	/**
	 * 初始化
	 * @param instset
	 * @param i 
	 * @param p 
	 */
	public KNN(InstanceSet instset,Pipe p, ISimilarity sim, int k){
		prototypes = instset;
		this.pipe = p;
		this.sim = sim;
		this.k = k;
		int count1 =0,count2=0;
		int total = prototypes.size();
		System.out.println("实例数量："+total);
		for(int i=0;i<total;i++){
			Instance inst = prototypes.get(i);
			TPredict pred = classify(inst, 1);
			if(pred.getLabel(0).equals(inst.getTarget()))
				count1++;
			prototypes.remove(i);
			TPredict pred2 = classify(inst, 1);
			if(pred2.getLabel(0).equals(inst.getTarget()))
				count2++;
			prototypes.add(i, inst);
		}
		System.out.println("Leave-zero-out正确率："+count1*1.0f/total);
		System.out.println("Leave-one-out正确率："+count2*1.0f/total);
	}

	public void setPipe(Pipe p) {
		this.pipe = p;

	}



	/**
	 * 分类，返回标签，格式可自定义
	 * @param instance
	 * @return
	 * @throws Exception 
	 */
	public TPredict classify(Instance instance, int n){
		LinkedPredict<String>  pred = new LinkedPredict<String>(k);

		for(int i = 0; i < prototypes.size(); i++){
			Instance curInst = prototypes.get(i);
//			if(((String) curInst.getSource()).contains("听#per#的歌"))
//				System.out.println("");
			float score;
			try {
				score = sim.calc(instance.getData(), curInst.getData());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			pred.add((String) curInst.getTarget(), score,(String) curInst.getSource());
		}
		//排序
		LinkedPredict<String> newpred = pred.mergeDuplicate(useScore );
		newpred.assertSize(n);
		return newpred;
	}	

	@Override
	public TPredict classify(Instance instance, Type type, int n) {
		return  classify(instance, n);
	}
	/**
	 * 将分类器保存到文件
	 * @param file
	 * @throws IOException
	 */
	public void saveTo(String file) throws IOException {
		File f = new File(file);
		File path = f.getParentFile();
		if(!path.exists()){
			path.mkdirs();
		}
		ObjectOutputStream out = new ObjectOutputStream(new GZIPOutputStream(
				new BufferedOutputStream(new FileOutputStream(file))));
		out.writeObject(this);
		out.close();
	}
	/**
	 *  从文件读入分类器
	 * @param file
	 * @return
	 * @throws LoadModelException
	 */
	public static KNN loadFrom(String file) throws LoadModelException{
		KNN cl = null;
		try {
			ObjectInputStream in = new ObjectInputStream(new GZIPInputStream(
					new BufferedInputStream(new FileInputStream(file))));
			cl = (KNN) in.readObject();
			in.close();
		} catch (Exception e) {
			throw new LoadModelException(e,file);
		}
		return cl;
	}

	public void setK(int k) {
		this.k = k;
		
	}



}
