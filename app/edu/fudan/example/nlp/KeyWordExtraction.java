package edu.fudan.example.nlp;
import edu.fudan.util.exception.LoadModelException;
import models.APIRequest.WeiboUtils;
import org.fnlp.app.keyword.AbstractExtractor;
import org.fnlp.app.keyword.WordExtract;

import edu.fudan.nlp.cn.tag.CWSTagger;
import edu.fudan.nlp.corpus.StopWords;
import org.junit.Test;
import play.Logger;
import play.Play;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 关键词抽取使用示例
 * @author jyzhao,ltian
 *
 */
public class KeyWordExtraction {

	@Test
	public void main(){
	}

	public static List<String> getKeyExtract(String content) {

		StopWords sw= new StopWords("./resource/stopwords");
		CWSTagger seg = null;
		try {
			seg = new CWSTagger("./resource/seg.m");
		} catch (LoadModelException e) {
			e.printStackTrace();
		}
		AbstractExtractor key = new WordExtract(seg,sw);
		Map<String,Integer> result = key.extract(content, 20);
		Set<String> keys = result.keySet();
		List<String> keyWords = new ArrayList<>(keys);
		Logger.info(keyWords.toString());
		return keyWords;
	}
}
