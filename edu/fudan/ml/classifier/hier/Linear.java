package edu.fudan.ml.classifier.hier;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import edu.fudan.ml.classifier.AbstractClassifier;
import edu.fudan.ml.classifier.LabelParser.Type;
import edu.fudan.ml.classifier.LabelParser;
import edu.fudan.ml.classifier.Predict;
import edu.fudan.ml.classifier.TPredict;
import edu.fudan.ml.classifier.linear.inf.Inferencer;
import edu.fudan.ml.feature.BaseGenerator;
import edu.fudan.ml.types.Instance;
import edu.fudan.ml.types.alphabet.AlphabetFactory;
import edu.fudan.ml.types.sv.HashSparseVector;
import edu.fudan.nlp.pipe.Pipe;
import edu.fudan.util.exception.LoadModelException;
/**
 * 大规模层次化多类分类，类别数很大，因此权重l向量用稀疏数组表示
 * @author xpqiu
 *
 */
public class Linear extends AbstractClassifier{

	private static final long serialVersionUID = -1599891626397888670L;
	/**
	 * 特征转换器
	 */
	public Pipe pipe;
	/**
	 * 特征权重向量数组，每一类对应一个向量
	 */
	public HashSparseVector[] weights;
	/**
	 * 推理器
	 */
	public Inferencer inf;
	/**
	 * 特征向量生成器
	 */
	public BaseGenerator gen;
	/**
	 * 字典管理器
	 */
	public AlphabetFactory factory;

	public Linear(HashSparseVector[] weights, Inferencer msolver) {	
		this.weights = weights;
		this.inf = msolver;
		this.inf.isUseTarget(false);
	}
	
	public Linear(HashSparseVector[] weights, Inferencer msolver, 
			BaseGenerator gen, Pipe pipes, AlphabetFactory af) {
		this.weights = weights;
		this.pipe = pipes;
		this.gen = gen;
		this.inf = msolver;
		this.inf.isUseTarget(false);
		this.factory = af;
		this.factory.setStopIncrement(true);
	}

	@Override
	public TPredict classify(Instance instance,int nbest) {		
		return inf.getBest(instance, nbest);
	}

	@Override
	public Predict classify(Instance instance, Type type, int n) {
		TPredict res = (TPredict) inf.getBest(instance, n);
		Predict pred = LabelParser.parse(res,factory.DefaultLabelAlphabet(),type);
		return pred;
	}

	
	/**
	 * 将模型存储到文件
	 * @param file
	 * @throws IOException
	 */
	public void saveTo(String file) throws IOException {
		File f = new File(file);
		File path = f.getParentFile();
		if(path!=null&&!path.exists()){
			path.mkdirs();
		}
		ObjectOutputStream out = new ObjectOutputStream(new GZIPOutputStream(
				new BufferedOutputStream(new FileOutputStream(file))));
		out.writeObject(this);
		out.close();
	}
	/**
	 * 
	 * @param file
	 * @return
	 * @throws LoadModelException
	 */
	public static Linear loadFrom(String file) throws LoadModelException {
		Linear cl;
		try {
			ObjectInputStream in = new ObjectInputStream(new GZIPInputStream(
					new BufferedInputStream(new FileInputStream(file))));
			cl = (Linear) in.readObject();
			in.close();
		} catch (Exception e) {
			throw new LoadModelException("分类器读入错误");
		}
		return cl;
	}
	
	
	public void setPipe(Pipe pipe) {
		this.pipe = pipe;		
	}
	public Pipe getPipe() {
		return pipe;		
	}


}
