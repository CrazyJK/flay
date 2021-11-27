package jk.kamoru.flayground.domain;

import java.io.File;
import org.apache.commons.io.FilenameUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Image {

	Long id;
	String name;
	String type;
	long length;
	long lastModified;
	File file;

	public static Image getInstance(Long id, File file) {
		return new Image(id, FilenameUtils.getBaseName(file.getName()), FilenameUtils.getExtension(file.getName()), file.length(), file.lastModified(), file);
	}

}
