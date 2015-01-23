package com.speryans.saldobus.framework.tools;

public class URLTools {
	public static String username = "socialday";

	public static String getInfo() {
		return "http://eventians.com/api/v1/get/" + URLTools.username;
	}
	
	public static String getSpeakers() {
		return "http://eventians.com/api/v1/speakers/" + URLTools.username;
	}
	
	public static String getSchedule() {
		return "http://eventians.com/api/v1/schedule/" + URLTools.username;
	}
	
	public static String getTwitter() {
		return "http://eventians.com/api/v1/twitter/" + URLTools.username;
	}
}
