package jk.kamoru.flayground.domain;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Video implements Info<String> {

	String opus;
	int play;
	int rank;
	String comment;
	long lastAccess;
	List<Tag> tags = new ArrayList<>();

	public Video(String key) {
		setKey(key);
	}

	@Override
	public String getKey() {
		return opus;
	}

	@Override
	public void setKey(String opus) {
		this.opus = opus;
	}

	public void increasePlayCount() {
		++play;
	}

}
