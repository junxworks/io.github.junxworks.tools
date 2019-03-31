package io.github.junxworks.tools.pojo.db.utils;

import java.io.File;
import java.util.Locale;

public class StringUtil {

	public static String capitalize(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	public static boolean isEmpty(String str) {
		return str == null || str.trim().length() == 0 || str.equals("null");
	}

	public static String packagePathToFilePath(String packagepath) {
		String result = null;
		if (packagepath != null) {
			result = packagepath.replace(".", File.separator);
		}
		return result;
	}

	public static String toUnicodeString(String s) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c >= 0 && c <= 255) {
				sb.append(c);
			} else {
				sb.append("\\u" + Integer.toHexString(c));
			}
		}
		return sb.toString();
	}

	public static String getCamelCaseString(String inputString, boolean firstCharacterUppercase) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(inputString.toLowerCase());// 简化java属性，让其跟数据库字段名字保持一致
		if (firstCharacterUppercase) {
			sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
		}

		return sb.toString();
	}

	public static String getValidPropertyName(String inputString) {
		String answer;

		if (inputString == null) {
			answer = null;
		} else if (inputString.length() < 2) {
			answer = inputString.toLowerCase(Locale.US);
		} else {
			if (Character.isUpperCase(inputString.charAt(0)) && !Character.isUpperCase(inputString.charAt(1))) {
				answer = inputString.substring(0, 1).toLowerCase(Locale.US) + inputString.substring(1);
			} else {
				answer = inputString;
			}
		}

		return answer;
	}

	public static String escape(final String val) {

		int len = val.length();
		StringBuffer buf = new StringBuffer(len);
		for (int i = 0; i < len; i++) {
			if (val.charAt(i) == '\'')
				buf.append('\'');
			buf.append(val.charAt(i));
		}
		return buf.toString();
	}
}
