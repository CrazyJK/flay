package jk.kamoru.flayground.commons;

import java.io.File;
import java.net.URL;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

public class FlayUtils {

	public static class FILE {
		public static final String[] VIDEO_SUFFIXs = new String[] {"avi", "mpg", "mkv", "wmv", "mp4", "mov", "rmvb", "m2ts"};
		public static final String[] IMAGE_SUFFIXs = new String[] {"jpg", "jpeg", "png", "gif", "jfif", "webp"};
		public static final String[] SUBTITLES_SUFFIXs = new String[] {"smi", "srt", "ass", "smil"};

		public static boolean isVideo(File file) {
			return ArrayUtils.contains(VIDEO_SUFFIXs, FilenameUtils.getExtension(file.getName()).toLowerCase());
		}

		public static boolean isImage(File file) {
			return ArrayUtils.contains(IMAGE_SUFFIXs, FilenameUtils.getExtension(file.getName()).toLowerCase());
		}

		public static boolean isSubtitles(File file) {
			return ArrayUtils.contains(SUBTITLES_SUFFIXs, FilenameUtils.getExtension(file.getName()).toLowerCase());
		}
	}

	public static int compareInteger(Integer integer1, Integer integer2) {
		int n1 = integer1 == null ? 0 : integer1.intValue();
		int n2 = integer2 == null ? 0 : integer2.intValue();
		return NumberUtils.compare(n1, n2);
	}

	public static int compareLong(Long long1, Long long2) {
		long n1 = long1 == null ? 0 : long1.longValue();
		long n2 = long2 == null ? 0 : long2.longValue();
		return NumberUtils.compare(n1, n2);
	}

	public static int compareURL(URL homepage1, URL homepage2) {
		String s1 = homepage1 == null ? "" : homepage1.toString();
		String s2 = homepage2 == null ? "" : homepage2.toString();
		return StringUtils.compare(s1, s2);
	}

}
