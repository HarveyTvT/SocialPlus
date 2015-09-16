package edu.fudan.nlp.corpus;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Set;

import edu.fudan.nlp.cn.Chars;
import edu.fudan.nlp.cn.Chars.StringType;
import edu.fudan.util.UnicodeReader;

public class Tags {

	/**
	 * 字符串文件转换为序列标注文件
	 * @param infile
	 * @param outfile
	 * @throws IOException
	 */
	public static void processFile(String infile, String outfile,String delimer,int tagnum) throws IOException {
		BufferedReader in = new BufferedReader(new UnicodeReader(new FileInputStream(infile), "utf8"));
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
				outfile), "utf8"));
		String line = null;
		while ((line = in.readLine()) != null) {
			line = line.trim();
			String newline;			
			newline= genSegSequence(line,delimer,tagnum);
			out.write(newline);
			//			out.newLine();
		}
		in.close();
		out.close();

	}

	/**
	 * 将序列标签转为BMES
	 * @param wordArray
	 * @return
	 */
	public static String genSequence4Tags(String[] wordArray){
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<wordArray.length; i++) {
			String word = wordArray[i];
			for(int j=0; j<word.length(); j++) {
				char c = word.charAt(j);
				if(Chars.getType(c)==Chars.CharType.B)
					System.err.println("包含空格");
				sb.append(c);
				sb.append('\t');
				if(j == 0) {
					if(word.length() == 1)
						sb.append('S');
					else
						sb.append('B');
				} else if(j == word.length()-1) {
					sb.append('E');
				} else {
					sb.append('M');
				}
				sb.append('\n');
			}
		}
		sb.append('\n');
		return sb.toString();
	}
	/**
	 * 将序列标签转为BMES
	 * @param wordArray
	 * @return
	 */
	public static String genSequence6Tags(String[] wordArray){
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<wordArray.length; i++) {
			String word = wordArray[i];
			String tag = null;
			int len = word.length();
			switch(len){
			case 1:
				tag = "S";break;
			case 2:
				tag = "BE";break;
			case 3:
				tag = "B2E";break;
			case 4:
				tag = "B23E";break;
			default :
				tag = "B23";
				int rest = len-4;
				while(rest-->0){
					tag+="M";
				}
				tag+="E";
			}
			assert tag.length() ==len;
			for(int j=0; j<word.length(); j++) {
				char c = word.charAt(j);	
				sb.append(c);
				sb.append('\t');
				sb.append(tag.charAt(j));
				sb.append('\n');
			}
		}
		sb.append('\n');
		return sb.toString();
	}
	/**
	 * 将分好词的字符串转换为标注序列
	 * @param sent
	 * @param delimer
	 * @param tagnum
	 * @return
	 */
	public static String genSegSequence(String sent,String delimer,int tagnum){		
		String[] wordArray = sent.split(delimer);	
		
		if(tagnum ==4 )
			return genSequence4Tags(wordArray);
		else if (tagnum == 6)
			return genSequence6Tags(wordArray);
		else 
			return null;
	}
	/**
	 * 生成Cross-Label序列
	 * @param sent
	 * @param delim
	 * @param delimTag
	 * @param filter
	 * @return
	 */
	public static String genCrossLabel(String sent,String delim,String delimTag,Set<String> filter){
		sent = sent.trim();
		if(sent.length()==0)
			return sent;
		StringBuilder sb = new StringBuilder();
		String[] wordArray = sent.split(delim);
		for(int i=0; i<wordArray.length; i++) {
			//得到tag
			int idx = wordArray[i].lastIndexOf(delimTag);
			if(idx==-1||idx==wordArray[i].length()-1){
				System.err.println(wordArray[i]);
			}			
			String word = wordArray[i].substring(0,idx);
			String tag = wordArray[i].substring(idx+1);
			for(int j=0; j<word.length(); j++) {
				char c = word.charAt(j);	
				sb.append(c);
				sb.append('\t');
				if(filter==null||filter.contains(tag)){//不过滤或是保留项					
					if(j == 0) {
						if(word.length() == 1)
							sb.append("S-"+tag);
						else
							sb.append("B-"+tag);
					} else if(j == word.length()-1) {
						sb.append("E-"+tag);
					} else {
						sb.append("M-"+tag);
					}
				}else{//过滤项
					sb.append("O");
				}
				sb.append('\n');
			}
		}
		sb.append('\n');
		return sb.toString();
	}

}
