package io.github.junxworks.tools;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class StringUtil {
	public static String[] concat(String[] part1, String[] part2) {
		String[] full = new String[part1.length + part2.length];
		System.arraycopy(part1, 0, full, 0, part1.length);
		System.arraycopy(part2, 0, full, part1.length, part2.length);
		return full;
	}

	public static String[] concatUniq(String[] part1, String[] part2) {
		ArrayList nlist = new ArrayList();

		for (int i = 0; i < part1.length; i++) {
			if (!nlist.contains(part1[i])) {
				nlist.add(part1[i]);
			}
		}
		for (int i = 0; i < part2.length; i++) {
			if (!nlist.contains(part2[i])) {
				nlist.add(part2[i]);
			}
		}
		return (String[]) nlist.toArray(new String[0]);
	}

	public static String[] cutString(String str, String delim) {
		ArrayList strings = new ArrayList();
		StringTokenizer tokenizer = new StringTokenizer(str, delim);
		while (tokenizer.hasMoreTokens()) {
			strings.add(URLDecoder.decode(tokenizer.nextToken()));
		}

		return (String[]) strings.toArray(new String[0]);
	}
	
	public static boolean isNull(String str){
		if(str == null || str.length() == 0)
			return true;
		return false;
	}
}
