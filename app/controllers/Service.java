package controllers;

import play.mvc.Controller;
import models.Results.Outcome;
import play.mvc.Result;

/**
 * Created by harvey on 15-9-9.
 * 这个类是系统分析服务的入口,
 * 返回分析后的页面
 */
public class Service extends Controller {
    public Result analysis(String url) {
        /*
         *  TODO 分析链接入口，进入对应的处理流程，处理接口返回一个Outcome,返回渲染后的页面
         */

        String platform = getPlatform(url);
        switch (platform) {
            case "weibo":
                break;
            case "renren":
                break;
            case "twitter":
                break;
        }
        return TODO;
    }

    /**
     * Return the name of  target platform according to the url given
     *
     * @param url
     * @return
     */
    public String getPlatform(String url) {
        // TODO 对url平台的判断
        return "weibo";
    }

    /**
     * The workflow of weibo, return a Outcome for front side
     *
     * @param url
     * @return
     */
    public Outcome weibo(String url) {
        //TODO 微博处理流程
        return new Outcome();
    }

    /**
     * @param url
     * @return
     */
    public Outcome twitter(String url) {
        //TODO 推特处理流程
        return new Outcome();
    }

    public Outcome renren(String url) {
        //TODO 人人处理流程
        return new Outcome();
    }
}
