package edu.fudan.nlp.corpus.fnlp;

import java.util.ArrayList;
import java.util.List;

import edu.fudan.nlp.parser.dep.DependencyTree;
import edu.fudan.util.MyStrings;

/**
 * FudanNLP标准数据格式
 * @since FudanNLP 1.5
 */
public class FNLPSent {


	public String[] words;
	public String[] tags;
	public int[] heads;
	public String[] relations;
	/**
	 * 下标起始位置，缺省为0
	 */
	private int start = 0;

	/**
	 * 
	 * @param words 词数组
	 * @param tags 词性数组
	 * @param heads 父节点数组
	 * @param relations 关系类型数组
	 */
	public FNLPSent(String[] words,String[] tags, int[] heads, String[] relations) {
		this.words  = words;
		this.tags = tags;
		this.heads = heads;
		this.relations = relations;
	}

	public FNLPSent(List<String> list) {
		parse(list,start,true);
	}

	public FNLPSent() {
		// TODO Auto-generated constructor stub
	}

	public FNLPSent(int len) {
		this.words  = new String[len];
		this.tags = new String[len];
		this.heads = new int[len];
		this.relations = new String[len];
	}

	/**
	 * @param list 一个句子，每行是一组标记 上海	{"NR	2	NMOD", "浦东	NR	6	NMOD"}
	 * @param pos 标记开始位置，默认是1
	 * @param p 第一列是否为序列标记
	 */
	public void parse(List<String> list,int pos,boolean b) {

		int len = list.size();
		words = new String[len];
		tags = new String[len];
		heads = new int[len];
		relations = new String[len];
		int start=0;
		if(b){
			start = 1;	
		}
		for(int j=0;j<len;j++){

			String[] toks = list.get(j).split("[\\t\\s]+");
			if(b){
				assert (j+pos) == Integer.parseInt(toks[0]);
			}

			words[j] = toks[start];
			if(toks.length>start+1)
				tags[j] = toks[start+1];
			if(toks.length>start+2)
				heads[j] = Integer.parseInt(toks[start+2])-pos;
			if(toks.length>start+3)
				relations[j] = toks[start+3];
			if(toks.length>start+4)
				System.err.println("格式列表太多！");
		}


	}



	public String toString(){
		StringBuffer sb = new StringBuffer();
		for(int j=0;j<words.length;j++){
			if(words[j]!=null){
				sb.append(j);
				sb.append("\t");
				sb.append(words[j]);
				if(tags!=null){
					sb.append("\t");
					sb.append(tags[j]);
				}
				if(heads!=null){
					sb.append("\t");
					sb.append(heads[j]);
				}
				if(relations!=null){
					sb.append("\t");
					sb.append(relations[j]);
				}
				sb.append("\n");
			}
		}
		return sb.toString();
	}

	public int size() {		
		return words.length;
	}
	/**
	 * 处理只分词的字符串
	 * @param line
	 */
	public void put(String line) {
		words = line.split("(\\s|　| |\\t)+");		
	}

	public boolean hasTag() {
		if(tags[0]!=null)
			return true;
		else
			return false;
	}

	public DependencyTree toTree() {
		ArrayList<DependencyTree> nodes = new ArrayList<DependencyTree>();
		DependencyTree root = null;
		for(int j=0;j<words.length;j++){
			DependencyTree node = new DependencyTree(j, words[j],tags[j], relations[j]);
			nodes.add(node);
		}
		for(int j=0;j<words.length;j++){
			int head = heads[j];
			if(head==-1)
				root = nodes.get(j);
			else{
				if(head>j)
					nodes.get(head).addLeftChild(nodes.get(j));
				else
					nodes.get(head).addRightChild(nodes.get(j));
			}
		}
		return root;
	}

	public String getSentenceString() {
		String s = MyStrings.toString(words, "");
		return s;
	}
}
