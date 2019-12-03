package com.mingrisoft.wordhintcode.data;

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
    private static char[] codeArray = source.toCharArray();//转成数组
    private static String word = "abcdefghijklmnopqrstuvwxyz";
    private static char[] wordArray = word.toCharArray();//转成数组
    /**
     * 获取数据源
     * @return
     */
    public static Code GetWordData(){
        return new Code().setCordArray(codeArray).setWordArray(wordArray);
    }
}
