package jk.kamoru.flayground.domain;

import java.util.List;
import lombok.Data;

@Data
public class Video {

	String opus;
	int play;
	int rank;
	String comment;
	long lastAccess;
	List<Tag> tags;

}
