package com.mingrisoft.qrobot;

/**
 * Created by Administrator on 2016/12/7.
 */

public class UrlEntity {

    /**
     * reason : 成功的返回
     * result : {"code":200000,"text":"亲，已帮你找到图片","url":"http://m.image.so.com/i?q=%E7%8B%97"}
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
         * code : 200000
         * text : 亲，已帮你找到图片
         * url : http://m.image.so.com/i?q=%E7%8B%97
         */

        private int code;
        private String text;
        private String url;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
