package com.mingrisoft.idiomcode.utils;

import com.mingrisoft.idiomcode.code.Code;

/**
 * Author LYJ
 * Created on 2017/1/24.
 * Time 08:43
 */

public class WordUtils {
    private static final String TAG = WordUtils.class.getSimpleName();
    private static String source = "所有经济监管制度统发布总统行政令退出跨太平" +
            "洋伙伴关系协定签署这一行政命令标志着政府公布的首要任年轻人结婚新娘也" +
            "都会找自己的闺蜜来并且把对抗的过程变得美而有趣的创意设计能够提供解决空" +
            "气污染的新并且把对抗的过程变得美而有趣也有不少人想起了同样是优秀歌手";
    private static char[] wordArray = source.toCharArray();//转成数组

    /**
     * 获取数据源
     * @return
     */
    public static Code GetWordData(){
        return new Code().setCharArray(wordArray).setStringArray(fableArray);
    }

    /**
     * 成语故事
     */
    private static String fable = "行尸走肉、\n" +
            "金蝉脱壳、\n" +
            "百里挑一、\n" +
            "金玉满堂、\n" +
            "背水一战、\n" +
            "霸王别姬、\n" +
            "天上人间、\n" +
            "不吐不快、\n" +
            "海阔天空、\n" +
            "情非得已、\n" +
            "满腹经纶、\n" +
            "兵临城下、\n" +
            "春暖花开、\n" +
            "插翅难逃、\n" +
            "黄道吉日、\n" +
            "天下无双、\n" +
            "偷天换日、\n" +
            "两小无猜、\n" +
            "卧虎藏龙、\n" +
            "珠光宝气、\n" +
            "簪缨世族、\n" +
            "花花公子、\n" +
            "绘声绘影、\n" +
            "国色天香、\n" +
            "相亲相爱、\n" +
            "八仙过海、\n" +
            "金玉良缘、\n" +
            "掌上明珠、\n" +
            "皆大欢喜、\n" +
            "逍遥法外、"+"生财有道、\n" +
            "极乐世界、\n" +
            "情不自禁、\n" +
            "愚公移山、\n" +
            "魑魅魍魉、\n" +
            "龙生九子、\n" +
            "精卫填海、\n" +
            "海市蜃楼、\n" +
            "高山流水、\n" +
            "一柱擎天、\n" +
            "卧薪尝胆、\n" +
            "壮志凌云、\n" +
            "金枝玉叶、\n" +
            "四海一家、\n" +
            "穿针引线、\n" +
            "无忧无虑、\n" +
            "无地自容、\n" +
            "三位一体、\n" +
            "落叶归根、\n" +
            "相见恨晚、\n" +
            "惊天动地、\n" +
            "滔滔不绝、\n" +
            "相濡以沫、\n" +
            "长生不死、\n" +
            "原来如此、\n" +
            "女娲补天、\n" +
            "三皇五帝、\n" +
            "万箭穿心、\n" +
            "水木清华、\n" +
            "窈窕淑女";
    private static String[] fableArray = fable.split("、");
}
