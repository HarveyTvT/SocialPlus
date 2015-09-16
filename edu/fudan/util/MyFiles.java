package edu.fudan.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import edu.fudan.nlp.pipe.seq.String2Sequence;
/**
 * 自定义文件操作类
 * @author xpqiu
 * @version 1.0
 * @since FudanNLP 1.5
 */
public class MyFiles {

	private static void allPath(List<File> files, File handle, String suffix) {
		if (handle.isFile()){
			if(suffix==null||handle.getName().endsWith(suffix))
				files.add(handle);
		}
		else if (handle.isDirectory()) {
			for (File sub : Arrays.asList(handle.listFiles()))
				allPath(files, sub,suffix);
		}
	}

	public static List<File> getAllFiles(String path, String suffix) {
		ArrayList<File> files = new ArrayList<File>();
		allPath(files, new File(path), suffix);
		return files;
	}
	/**
	 * 将字符串写到文件
	 * @param path
	 * @param str
	 * @throws IOException
	 */
	public static void write(String path, String str) throws IOException {
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
				path), "utf8"));
		out.append(str);
		out.close();		
	}
	public static void write(String path, Serializable o) {
		try {
			ObjectOutputStream outstream = new ObjectOutputStream(
					new GZIPOutputStream(new FileOutputStream(path)));
			outstream.writeObject(o);
			outstream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * 转换文件编码
	 * @param infile
	 * @param outfile
	 * @param enc1
	 * @param enc2
	 * @throws IOException
	 */
	public static void conver(String infile, String outfile, String enc1, String enc2) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(
				new FileInputStream(infile), enc1));
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
				outfile), enc2));

		String line = null;
		while ((line = in.readLine()) != null) {
			line = line.trim();	
			if(line.length()==0)
				continue;
			out.write(line);
			out.newLine();
		}
		in.close();
		out.close();
		
	}
	
	public static Object read(String path) {
		Object o=null;
		try {
			ObjectInputStream instream = new ObjectInputStream(new GZIPInputStream(
					new FileInputStream(path)));
			o = instream.readObject();
			instream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return o;
	}

	public static Object loadObject(String path) throws IOException,
    ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(
                new GZIPInputStream(new FileInputStream(path))));
        Object obj = in.readObject();
        in.close();
        return obj;
    }

	public static void saveObject(String path, Object obj) throws IOException {
		ObjectOutputStream out = new ObjectOutputStream(
				new BufferedOutputStream(new GZIPOutputStream(
						new FileOutputStream(path))));
		out.writeObject(obj);
		out.close();
	}
	/**
	 * 从文件中读入完整的字符串，包括\r\n等。
	 * @param file
	 * @return
	 * @throws IOException 
	 */
	public static String loadString(String file) throws IOException {
		StringBuilder sb = new StringBuilder();
		UnicodeReader reader = new UnicodeReader(new FileInputStream(file), "utf8");
		int n;	
		char[] cs = new char[256];
		while ((n = reader.read(cs)) != -1) {		
			sb.append(Arrays.copyOfRange(cs, 0, n));			
		}
		return sb.toString();
	}
}
