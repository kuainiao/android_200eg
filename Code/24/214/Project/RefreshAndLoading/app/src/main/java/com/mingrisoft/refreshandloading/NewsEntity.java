package com.mingrisoft.refreshandloading;

import java.util.List;

/**
 * Created by Administrator on 2017/1/9.
 */

public class NewsEntity {

    /**
     * reason : 成功的返回
     * result : {"stat":"1","data":[{"uniquekey":"e77a338a496d4be5764be108e95f473c","title":"童蕾一袭红裙亮相盛典 优雅大气又不失明朗妩媚","date":"2017-01-09 15:25","category":"头条","author_name":"腾讯娱乐","url":"http://mini.eastday.com/mobile/170109152559538.html","thumbnail_pic_s":"http://08.imgmini.eastday.com/mobile/20170109/20170109152559_81eca52daab6e7e3f54a8ab1afdaa2e1_1_mwpm_03200403.jpeg","thumbnail_pic_s02":"http://08.imgmini.eastday.com/mobile/20170109/20170109152559_81eca52daab6e7e3f54a8ab1afdaa2e1_2_mwpm_03200403.jpeg","thumbnail_pic_s03":"http://08.imgmini.eastday.com/mobile/20170109/20170109152559_81eca52daab6e7e3f54a8ab1afdaa2e1_3_mwpm_03200403.jpeg"},{"uniquekey":"c86e2776e662961e281932e2c30442db","title":"日本捕获1.8米长巨型鱼 重量或达百斤以上","date":"2017-01-09 15:56","category":"头条","author_name":"环球网","url":"http://mini.eastday.com/mobile/170109155641516.html","thumbnail_pic_s":"http://04.imgmini.eastday.com/mobile/20170109/20170109155641_d17feef6c3a082c7f6eebcb7d6a4cd0a_1_mwpm_03200403.jpeg"},{"uniquekey":"0d63445b8e0b23c8414c0d8fff497ddc","title":"杨幂耿直回应\"发际线\"后移：珍惜还有头发的我","date":"2017-01-09 15:48","category":"头条","author_name":"搜狐娱乐","url":"http://mini.eastday.com/mobile/170109154816032.html","thumbnail_pic_s":"http://03.imgmini.eastday.com/mobile/20170109/20170109154816_9b654af86b36d37ff16eedf60ac6f616_1_mwpm_03200403.jpeg"},{"uniquekey":"41ba2b14577cda545854b77767541c8a","title":"王弢被封单簧管老教授 生活中却是逗比男青年","date":"2017-01-09 15:25","category":"头条","author_name":"腾讯娱乐","url":"http://mini.eastday.com/mobile/170109152559239.html","thumbnail_pic_s":"http://06.imgmini.eastday.com/mobile/20170109/20170109152559_7ee09c866c45dfca1c9b31837153ec8e_1_mwpm_03200403.jpeg","thumbnail_pic_s02":"http://06.imgmini.eastday.com/mobile/20170109/20170109152559_7ee09c866c45dfca1c9b31837153ec8e_2_mwpm_03200403.jpeg","thumbnail_pic_s03":"http://06.imgmini.eastday.com/mobile/20170109/20170109152559_7ee09c866c45dfca1c9b31837153ec8e_3_mwpm_03200403.jpeg"},{"uniquekey":"f2e05edba70723676585ba85429b1b7a","title":"张馨予再上热搜 然而她妈妈的第一反应却是害怕","date":"2017-01-09 15:16","category":"头条","author_name":"搜狐娱乐","url":"http://mini.eastday.com/mobile/170109151620431.html","thumbnail_pic_s":"http://08.imgmini.eastday.com/mobile/20170109/20170109151620_3bd5fff7ae0cbb711c628091bb97011f_1_mwpm_03200403.jpeg","thumbnail_pic_s02":"http://08.imgmini.eastday.com/mobile/20170109/20170109151620_3bd5fff7ae0cbb711c628091bb97011f_2_mwpm_03200403.jpeg"},{"uniquekey":"7fd90c7f6a2e8f75bbe77c25f0986bdd","title":"高清：\u201c康熙\u201d合体！他们永远是镜头下的最佳拍档","date":"2017-01-09 15:02","category":"头条","author_name":"腾讯娱乐","url":"http://mini.eastday.com/mobile/170109150201245.html","thumbnail_pic_s":"http://03.imgmini.eastday.com/mobile/20170109/20170109150201_c041fc824c15b58bbd0e62f94a680978_1_mwpm_03200403.jpeg","thumbnail_pic_s02":"http://03.imgmini.eastday.com/mobile/20170109/20170109150201_c041fc824c15b58bbd0e62f94a680978_2_mwpm_03200403.jpeg","thumbnail_pic_s03":"http://03.imgmini.eastday.com/mobile/20170109/20170109150201_c041fc824c15b58bbd0e62f94a680978_3_mwpm_03200403.jpeg"}]}
     * error_code : 0
     */

    private String reason;
    private ResultBean result;
    private int error_code;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public static class ResultBean {
        /**
         * stat : 1
         * data : [{"uniquekey":"e77a338a496d4be5764be108e95f473c","title":"童蕾一袭红裙亮相盛典 优雅大气又不失明朗妩媚","date":"2017-01-09 15:25","category":"头条","author_name":"腾讯娱乐","url":"http://mini.eastday.com/mobile/170109152559538.html","thumbnail_pic_s":"http://08.imgmini.eastday.com/mobile/20170109/20170109152559_81eca52daab6e7e3f54a8ab1afdaa2e1_1_mwpm_03200403.jpeg","thumbnail_pic_s02":"http://08.imgmini.eastday.com/mobile/20170109/20170109152559_81eca52daab6e7e3f54a8ab1afdaa2e1_2_mwpm_03200403.jpeg","thumbnail_pic_s03":"http://08.imgmini.eastday.com/mobile/20170109/20170109152559_81eca52daab6e7e3f54a8ab1afdaa2e1_3_mwpm_03200403.jpeg"},{"uniquekey":"c86e2776e662961e281932e2c30442db","title":"日本捕获1.8米长巨型鱼 重量或达百斤以上","date":"2017-01-09 15:56","category":"头条","author_name":"环球网","url":"http://mini.eastday.com/mobile/170109155641516.html","thumbnail_pic_s":"http://04.imgmini.eastday.com/mobile/20170109/20170109155641_d17feef6c3a082c7f6eebcb7d6a4cd0a_1_mwpm_03200403.jpeg"},{"uniquekey":"0d63445b8e0b23c8414c0d8fff497ddc","title":"杨幂耿直回应\"发际线\"后移：珍惜还有头发的我","date":"2017-01-09 15:48","category":"头条","author_name":"搜狐娱乐","url":"http://mini.eastday.com/mobile/170109154816032.html","thumbnail_pic_s":"http://03.imgmini.eastday.com/mobile/20170109/20170109154816_9b654af86b36d37ff16eedf60ac6f616_1_mwpm_03200403.jpeg"},{"uniquekey":"41ba2b14577cda545854b77767541c8a","title":"王弢被封单簧管老教授 生活中却是逗比男青年","date":"2017-01-09 15:25","category":"头条","author_name":"腾讯娱乐","url":"http://mini.eastday.com/mobile/170109152559239.html","thumbnail_pic_s":"http://06.imgmini.eastday.com/mobile/20170109/20170109152559_7ee09c866c45dfca1c9b31837153ec8e_1_mwpm_03200403.jpeg","thumbnail_pic_s02":"http://06.imgmini.eastday.com/mobile/20170109/20170109152559_7ee09c866c45dfca1c9b31837153ec8e_2_mwpm_03200403.jpeg","thumbnail_pic_s03":"http://06.imgmini.eastday.com/mobile/20170109/20170109152559_7ee09c866c45dfca1c9b31837153ec8e_3_mwpm_03200403.jpeg"},{"uniquekey":"f2e05edba70723676585ba85429b1b7a","title":"张馨予再上热搜 然而她妈妈的第一反应却是害怕","date":"2017-01-09 15:16","category":"头条","author_name":"搜狐娱乐","url":"http://mini.eastday.com/mobile/170109151620431.html","thumbnail_pic_s":"http://08.imgmini.eastday.com/mobile/20170109/20170109151620_3bd5fff7ae0cbb711c628091bb97011f_1_mwpm_03200403.jpeg","thumbnail_pic_s02":"http://08.imgmini.eastday.com/mobile/20170109/20170109151620_3bd5fff7ae0cbb711c628091bb97011f_2_mwpm_03200403.jpeg"},{"uniquekey":"7fd90c7f6a2e8f75bbe77c25f0986bdd","title":"高清：\u201c康熙\u201d合体！他们永远是镜头下的最佳拍档","date":"2017-01-09 15:02","category":"头条","author_name":"腾讯娱乐","url":"http://mini.eastday.com/mobile/170109150201245.html","thumbnail_pic_s":"http://03.imgmini.eastday.com/mobile/20170109/20170109150201_c041fc824c15b58bbd0e62f94a680978_1_mwpm_03200403.jpeg","thumbnail_pic_s02":"http://03.imgmini.eastday.com/mobile/20170109/20170109150201_c041fc824c15b58bbd0e62f94a680978_2_mwpm_03200403.jpeg","thumbnail_pic_s03":"http://03.imgmini.eastday.com/mobile/20170109/20170109150201_c041fc824c15b58bbd0e62f94a680978_3_mwpm_03200403.jpeg"}]
         */

        private String stat;
        private List<DataBean> data;

        public String getStat() {
            return stat;
        }

        public void setStat(String stat) {
            this.stat = stat;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * uniquekey : e77a338a496d4be5764be108e95f473c
             * title : 童蕾一袭红裙亮相盛典 优雅大气又不失明朗妩媚
             * date : 2017-01-09 15:25
             * category : 头条
             * author_name : 腾讯娱乐
             * url : http://mini.eastday.com/mobile/170109152559538.html
             * thumbnail_pic_s : http://08.imgmini.eastday.com/mobile/20170109/20170109152559_81eca52daab6e7e3f54a8ab1afdaa2e1_1_mwpm_03200403.jpeg
             * thumbnail_pic_s02 : http://08.imgmini.eastday.com/mobile/20170109/20170109152559_81eca52daab6e7e3f54a8ab1afdaa2e1_2_mwpm_03200403.jpeg
             * thumbnail_pic_s03 : http://08.imgmini.eastday.com/mobile/20170109/20170109152559_81eca52daab6e7e3f54a8ab1afdaa2e1_3_mwpm_03200403.jpeg
             */

            private String uniquekey;
            private String title;
            private String date;
            private String category;
            private String author_name;
            private String url;
            private String thumbnail_pic_s;
            private String thumbnail_pic_s02;
            private String thumbnail_pic_s03;

            public String getUniquekey() {
                return uniquekey;
            }

            public void setUniquekey(String uniquekey) {
                this.uniquekey = uniquekey;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getCategory() {
                return category;
            }

            public void setCategory(String category) {
                this.category = category;
            }

            public String getAuthor_name() {
                return author_name;
            }

            public void setAuthor_name(String author_name) {
                this.author_name = author_name;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getThumbnail_pic_s() {
                return thumbnail_pic_s;
            }

            public void setThumbnail_pic_s(String thumbnail_pic_s) {
                this.thumbnail_pic_s = thumbnail_pic_s;
            }

            public String getThumbnail_pic_s02() {
                return thumbnail_pic_s02;
            }

            public void setThumbnail_pic_s02(String thumbnail_pic_s02) {
                this.thumbnail_pic_s02 = thumbnail_pic_s02;
            }

            public String getThumbnail_pic_s03() {
                return thumbnail_pic_s03;
            }

            public void setThumbnail_pic_s03(String thumbnail_pic_s03) {
                this.thumbnail_pic_s03 = thumbnail_pic_s03;
            }
        }
    }
}
