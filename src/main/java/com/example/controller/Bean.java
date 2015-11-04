package com.example.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.TabChangeEvent;

@Named
// @SessionScoped
@ConversationScoped
public class Bean implements Serializable {

	@Inject
	Conversation conversation;

	/**
	 * 
	 */
	private static final long serialVersionUID = -4110767446166151553L;
	private String page;
	private String img;
	private String menu;
	private static final String DEFAULT_PATH = "/WEB-INF/includes/";
	private static final String DEFAULT_SUFFIX = ".xhtml";
	private static final String PAGE_1 = "include1";
	private static final String PAGE_2 = "galleria";
	private static final String PAGE_3 = "upload";
	private static final String PAGE_4 = "imgData";
	private static final String PAGE_5 = "menu";
	private static final String PAGE_6 = "media";
	private static final String PAGE_7 = "onemenu";
	private static final String PAGE_8 = "elastic";
	private static final String PAGE_LOGIN = "login";
	private boolean isLogin;
	private boolean showImg;
	private boolean isAdmin;
	private int selectedTab = 0;

	@PostConstruct
	public void init() {
		if (this.conversation.isTransient()) {
			this.conversation.begin();
		}
		this.page = DEFAULT_PATH + PAGE_LOGIN + DEFAULT_SUFFIX;

		this.img = DEFAULT_PATH + PAGE_4 + DEFAULT_SUFFIX;
		this.menu = DEFAULT_PATH + PAGE_5 + DEFAULT_SUFFIX;

	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public void logOut(String pageCode) {
		showImg = false;
		if ("login".equals(pageCode))
			this.page = DEFAULT_PATH + PAGE_LOGIN + DEFAULT_SUFFIX;

	}

	public void switchToPage(String pageCode) {

		if ("1".equals(pageCode))
			this.page = DEFAULT_PATH + PAGE_1 + DEFAULT_SUFFIX;
		if ("2".equals(pageCode))
			this.page = DEFAULT_PATH + PAGE_2 + DEFAULT_SUFFIX;

		if ("media".equals(pageCode))
			this.img = DEFAULT_PATH + PAGE_6 + DEFAULT_SUFFIX;
		if ("imgData".equals(pageCode))
			this.img = DEFAULT_PATH + PAGE_4 + DEFAULT_SUFFIX;
	}

	public void switchHomeTab(String pageCode) {

		if ("home".equals(pageCode))
			this.page = DEFAULT_PATH + PAGE_1 + DEFAULT_SUFFIX;
		if ("galleria".equals(pageCode))
			this.page = DEFAULT_PATH + PAGE_2 + DEFAULT_SUFFIX;
		if ("upload".equals(pageCode))
			this.page = DEFAULT_PATH + PAGE_3 + DEFAULT_SUFFIX;
		if ("elastic".equals(pageCode))
			this.page = DEFAULT_PATH + PAGE_8 + DEFAULT_SUFFIX;
		showImg = false;

	}

	public void onTabChange(TabChangeEvent event) {

		if (event.getTab().getTitle().equals("Home"))
			switchHomeTab("home");
		if (event.getTab().getTitle().equals("Galleria"))
			switchHomeTab("galleria");
		if (event.getTab().getTitle().equals("Upload"))
			switchHomeTab("upload");

	}

	public boolean isLogin() {
		return isLogin;
	}

	public void setLogin(boolean isLogin) {
		this.isLogin = isLogin;
	}

	public boolean isShowImg() {
		return showImg;
	}

	public void setShowImg(boolean showImg) {
		this.showImg = showImg;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getMenu() {
		return menu;
	}

	public void setMenu(String menu) {
		this.menu = menu;
	}

	public int getSelectedTab() {
		return selectedTab;
	}

	public void setSelectedTab(int selectedTab) {
		this.selectedTab = selectedTab;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

}
