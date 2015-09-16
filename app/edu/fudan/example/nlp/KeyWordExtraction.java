package edu.fudan.example.nlp;
import models.APIRequest.WeiboUtils;
import org.fnlp.app.keyword.AbstractExtractor;
import org.fnlp.app.keyword.WordExtract;

import edu.fudan.nlp.cn.tag.CWSTagger;
import edu.fudan.nlp.corpus.StopWords;
import org.junit.Test;
import play.Play;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 关键词抽取使用示例
 * @author jyzhao,ltian
 *
 */
public class KeyWordExtraction {

	@Test
	public void main(){
	}
	
	public static void getKeyExtract(String content) throws Exception {

		StopWords sw= new StopWords("./resource/stopwords");
		CWSTagger seg = new CWSTagger("./resource/seg.m");
		AbstractExtractor key = new WordExtract(seg,sw);
		
		String result = key.extract(content,15,false);
		
		//处理已经分好词的句子
		sw=null;
		key = new WordExtract(seg,sw);
		key = new WordExtract();
		System.out.println(result);

	}
}
