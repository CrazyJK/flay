package jk.kamoru.flayground.domain;

import java.io.File;
import java.util.List;
import lombok.Data;

@Data
public class Actress {

	String name;
	String localName;
	String birth;
	String body;
	int height;
	int debut;
	String comment;
	boolean favorite;
	List<File> covers;

}
