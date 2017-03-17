package lazycat.series.sqljam.expression;

import lazycat.series.lang.Assert;
import lazycat.series.lang.Ints;

/**
 * StringHelper
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class StringHelper {

	public static String parseText(String text, String token, Object[] args, int[] included) {
		Assert.hasNoText(text, "Can't parse empty text.");
		Assert.hasNoText(token, "Token string is required.");
		if (args == null || args.length == 0) {
			return text;
		}
		int offset = 0;
		int start = text.indexOf(token, offset);
		if (start == -1) {
			return text;
		}
		StringBuilder builder = new StringBuilder();
		char[] src = text.toCharArray();
		int i = 0, j = 0;
		while (start > -1) {
			if (start > 0 && src[start - 1] == '\\') {
				builder.append(src, offset, start - offset - 1).append(token);
				offset = start + token.length();
			} else {
				builder.append(src, offset, start - offset);
				if (included == null || Ints.contains(included, i)) {
					builder.append(args[j]);
					if (j < args.length - 1) {
						j++;
					}
				} else {
					builder.append(token);
				}
				i++;
				offset = start + token.length();
			}
			start = text.indexOf(token, offset);
		}
		if (offset < src.length) {
			builder.append(src, offset, src.length - offset);
		}
		return builder.toString();
	}

	private StringHelper() {
	}

	public static void main(String[] args) throws Exception {
		System.out.println(parseText("?,?,?,?", "?", new String[] { "a", "b" }, new int[] { 0, 2 }));
	}

}
