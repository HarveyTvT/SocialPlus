package edu.fudan.nlp.similarity.train;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import edu.fudan.data.reader.Reader;
import edu.fudan.ml.types.Instance;
import edu.fudan.nlp.cn.ChineseTrans;

public class SougouCA  extends Reader {

	private static ChineseTrans tc = new ChineseTrans();
	File file = null;
	BufferedReader reader = null;
	String url = null;
	String docno = null;
	String contenttitle = null;
	String content = null;

	public SougouCA(String strfile) {
		file = new File(strfile);
		if (file.exists()) {
			try {
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "GBK"));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} else {
			file = null;
			reader = null;
		}
	}

	/**
	 * 向后读取一项
	 * @return
	 */
	public boolean hasNext() {
		if (reader == null)
			return false;
		String line;
		try {
			line = reader.readLine();
			if (line == null) return false;
			if (line.equals("<doc>")) {
				line = reader.readLine();
				url = line.replaceAll("^<url>", "");
				url = url.replaceAll("</url>$", "");
				line = reader.readLine();
				docno = line.replaceAll("^<docno>", "");
				docno = docno.replaceAll("</docno>$", "");
				line = reader.readLine();
				contenttitle = line.replaceAll("^<contenttitle>", "");
				contenttitle = contenttitle.replaceAll("</contenttitle>$", "");
				line = reader.readLine();
				content = line;
				while(!line.endsWith("</content>")){
					line = reader.readLine();
					content += line;
				}
				content = content.replaceAll("^<content>", "");
				content = content.replaceAll("</content>$", "");
			}
			line = reader.readLine();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 读取内容
	 * @return
	 */
	public Instance next(){
		return new Instance(content);
	}
	

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		BufferedWriter bout = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream("./tmpdata/trad.txt"), "UTF-8"));
		SougouCA sca = new SougouCA("./tmpdata/SogouCa/news.allsites.010805.txt");
		while(sca.hasNext()){
			String s = (String) sca.next().getData();
			
			s = tc.normalize(s);
//			System.out.println(s);
			if (s.length() == 0)
                continue;
			bout.write(s);			
			bout.write("\n");
		}
		bout.close();
		System.out.println("Done!");
	}

}
