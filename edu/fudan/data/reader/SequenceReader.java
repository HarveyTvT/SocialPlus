package edu.fudan.data.reader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import edu.fudan.ml.types.Instance;
import edu.fudan.util.UnicodeReader;

/**
 * 读入序列标记的数据，目标值（若存在）在最后一列。列数必须一致
 * 为了数据处理方便，内部的行列和文件里的行列翻转
 * 格式为
 * x1	y1
 * x2	y2
 * 
 * x3	y3
 * x4	y4
 * 每行数据用\t隔开， * 不同样本以空行分开
 * 输出数据格式为：
 * 		data：ArrayList<ArrayList<String>>
 * 		target: ArrayList<String>
 * @author xpqiu
 *
 */
public class SequenceReader extends Reader {

	BufferedReader reader;
	Instance cur;
	/**
	 * 默认包含目标值
	 */
	private boolean hasTarget = true;;
	static final char delimiter = '\t';
	/**
	 * 当前行号
	 */
	int lineNo=0;

	/**
	 * 构造函数
	 * @param file 文件名
	 * @param hasTarget 是否包含目标值
	 */
	public SequenceReader(String file,boolean hasTarget) {
		this(file, hasTarget,"UTF-8");
	}
	
	public SequenceReader(String file,boolean hasTarget, String charsetName) {
		this.hasTarget  = hasTarget;
		try {
			reader = new BufferedReader(new UnicodeReader(
					new FileInputStream(file), charsetName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public SequenceReader(InputStream is) {

		reader = new BufferedReader(new UnicodeReader(
				is,null));
	}

	public boolean hasNext() {
		cur = readSequence();
		return (cur != null);
	}

	public Instance next() {
		return cur;
	}

	private Instance readSequence() {
		cur = null;
		try {
			ArrayList<ArrayList<String>> seq = new ArrayList<ArrayList<String>>();
			ArrayList<String> first = new ArrayList(); //至少有一列元素
			seq.add(first);
			ArrayList<String> labels = null;
			if(hasTarget){
				labels = new ArrayList<String>();
			}
			String content = null;
			while ((content = reader.readLine()) != null) {
				lineNo++;
//				content = content.trim();
				if (content.matches("^$")){
					if(first.size()>0) //第一列个数>0
						break;
					else
						continue;
				}
				int colsnum = 0;
				int start = 0;
				int next =0;
				while ((next = content.indexOf(delimiter, next)) != -1) {
					if(next==start){ //防止字符和分隔符相同 
						next++;
						continue;
					}
					ensure(colsnum,seq);
					seq.get(colsnum).add(content.substring(start,next));
					next++;
					colsnum++;
					start = next;
				}
				//处理最后一列
				if(hasTarget){
					if(start<2){
						System.out.println("数据格式错误，只有一列，请检查！");
						System.out.println("第"+lineNo+"行");
						continue;
					}
					labels.add(content.substring(start));
				}else{
					ensure(colsnum,seq);
					seq.get(colsnum).add(content.substring(start));
				}					
			}
			if (first.size() > 0){
				cur = new Instance(seq, labels);
			}
			seq = null;
			labels = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return cur;
	}

	private void ensure(int colsnum, ArrayList<ArrayList<String>> seq) {
		while(colsnum>=seq.size()){
			seq.add(new ArrayList<String>());
		}
		
	}

	public static void main(String[] args) {
				SequenceReader sr = new SequenceReader("example-data/sequence/train.txt",true);
//		SequenceReader sr = new SequenceReader("example-data/sequence/test0.txt",false);
		Instance inst = null;
		int count = 0;
		while (sr.hasNext()) {
			inst = sr.next();
			System.out.print(".");
			inst = null;
			count++;
		}
		System.out.println(count);
	}
}
