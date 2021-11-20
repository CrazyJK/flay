package jk.kamoru.flayground.domain;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.math.NumberUtils;
import lombok.Data;

@JsonFilter("flayJsonFilter")
@Data
public class Flay {

	public static enum FileType {
		MOVIE, SUBTITLES, COVER, CANDIDATES;
	}

	private boolean instance;

	private Studio studio;
	private String opus;
	private String title;
	private List<Actress> actress;
	private String release;

	private Video video;

	// files
	private Map<FileType, List<File>> files = new HashMap<>();

	// logic
	@JsonIgnore boolean changedVideo = false;
	@JsonIgnore boolean changedFiles = false;

	public Flay() {
		files.put(FileType.MOVIE, new ArrayList<File>());
		files.put(FileType.SUBTITLES, new ArrayList<File>());
		files.put(FileType.COVER, new ArrayList<File>());
		files.put(FileType.CANDIDATES, new ArrayList<File>());
	}

	public long getModified() {
		return NumberUtils.max(
				files.get(FileType.MOVIE).stream().mapToLong(File::lastModified).max().orElse(-1),
				files.get(FileType.SUBTITLES).stream().mapToLong(File::lastModified).max().orElse(-1),
				files.get(FileType.COVER).stream().mapToLong(File::lastModified).max().orElse(-1));
	}

	public long getLength() {
		return files.get(FileType.MOVIE).stream().mapToLong(File::length).sum()
				+ files.get(FileType.SUBTITLES).stream().mapToLong(File::length).sum()
				+ files.get(FileType.COVER).stream().mapToLong(File::length).sum();
	}

}
