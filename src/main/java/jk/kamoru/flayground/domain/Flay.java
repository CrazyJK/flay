package jk.kamoru.flayground.domain;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class Flay {

	String studio;
	String opus;
	String title;
	List<String> actressList;
	String release;
	boolean archive = false;

	// files
	Map<String, List<File>> files = new HashMap<>();

	Video video;

}
