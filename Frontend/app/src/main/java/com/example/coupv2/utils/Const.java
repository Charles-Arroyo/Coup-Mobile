package com.example.coupv2.utils;

import com.example.coupv2.R;

public class Const {
	public static final String DOMAIN = "http://192.168.50.11:8080";
	public static final String URL_GET_USR_BY_ID = DOMAIN + "/users/";
	public static final String URL_POST_CREATE_USER = DOMAIN + "/users/";

	public static final String URL_JSON_OBJECT = "https://api.androidhive.info/volley/person_object.json";
	public static final String URL_JSON_ARRAY = "https://api.androidhive.info/volley/person_array.json";
	public static final String URL_STRING_REQ =
			"https://api.androidhive.info/volley/string_response.html";
	private static String currentEmail = "";

	private static int currentTheme = R.style.DarkThemePurple;

	public static String getCurrentEmail() {
		return currentEmail;
	}

	public static void setCurrentEmail(String email) {
		currentEmail = email;
	}

	public static int getCurrentTheme() {
		return currentTheme;
	}

	public static void setCurrentTheme(int theme){
		currentTheme = theme;
	}
}
