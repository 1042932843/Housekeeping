package cqnu.com.housekeeping.Adapter.AnnouncementFragment;

import java.util.List;

public class AnnouncementModel {
	private String code;
	private List<Bean> notice;

	public List<Bean> getNotice() {
		return notice;
	}

	public void setNotice(List<Bean> notice) {
		this.notice = notice;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
