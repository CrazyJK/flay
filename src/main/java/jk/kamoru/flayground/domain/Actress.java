package jk.kamoru.flayground.domain;

import java.io.File;
import java.util.Collection;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Actress implements Info<String> {

	String name;
	String localName;
	String birth;
	String body;
	Integer height;
	Integer debut;
	String comment;
	boolean favorite;
	@JsonIgnore Collection<File> covers;

	public Actress(String name) {
		setKey(name);
		this.localName = "";
		this.birth = "";
		this.body = "";
		this.height = null;
		this.debut = null;
		this.comment = "";
		this.favorite = false;
	}

	@Override
	public String getKey() {
		return name;
	}

	@Override
	public void setKey(String key) {
		this.name = key;
	}

	public int getCoverSize() {
		return covers == null ? 0 : covers.size();
	}

	public void setCoverSize(int coverSize) {}

}
