package cqnu.com.housekeeping.Design.vsView;

/**
 * Created by HP on 2016/9/10.
 */
public class AdverNotice {
    private String title;
    private String url;
    private String txt;

    public AdverNotice(String title, String url, String txt) {
        this.title = title;
        this.url = url;
        this.txt = txt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }
}
