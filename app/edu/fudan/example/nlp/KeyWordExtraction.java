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

		Logger.error(Paths.get("./").toAbsolutePath().toString());
		StopWords sw= new StopWords("./resource/stopwords");
		CWSTagger seg = null;
		try {
			seg = new CWSTagger("./resource/seg.m");
		} catch (LoadModelException e) {
			e.printStackTrace();
		}
		AbstractExtractor key = new WordExtract(seg,sw);
		String result = key.extract(content, 15, false);
		String middle;
		play.Logger.debug("The raw word extract result is:"+result);
		if(result.length()>3) {
			middle = result.substring(1, result.length() - 2);
		}else{
			return new ArrayList<>();
		}
		String[] keyWords_raw = middle.split(" ");
		List<String> keyWords_pro = new ArrayList<>();
		for (String keyWord : keyWords_raw) {
			keyWords_pro.add(keyWord.split("=")[0]);
		}

		return keyWords_pro;
	}
}
