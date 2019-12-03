package com.mingrisoft.news.bean;

import java.util.List;

/**
 * Author LYJ
 * Created on 2016/12/6.
 * Time 14:09
 */

public class Bean {
    private List<Reasult> reasults;
    private int errorCode;
    private String reason;

    public List<Reasult> getReasults() {
        return reasults;
    }

    public void setReasults(List<Reasult> reasults) {
        this.reasults = reasults;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("reason :" + reason + "\n");
        builder.append("errorCode :" + errorCode + "\n");
        for (Reasult reasult: reasults) {
            builder.append("-------------------------\n\n")
                    .append("ctime"+reasult.getCtime()+ "\n")
                    .append("title"+reasult.getTitle()+ "\n")
                    .append("description"+reasult.getDescription()+ "\n")
                    .append("picurl"+reasult.getPicUrl()+ "\n")
                    .append("url"+reasult.getUrl()+ "\n");
        }
        return builder.toString();
    }
}
