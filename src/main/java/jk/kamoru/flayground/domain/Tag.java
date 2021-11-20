package jk.kamoru.flayground.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Tag implements Info<Integer> {

	Integer id;
	String name;
	String description;

	public Tag(Integer key) {
		setKey(key);
		this.name = "";
		this.description = "";
	}

	@Override
	public Integer getKey() {
		return id;
	}

	@Override
	public void setKey(Integer key) {
		this.id = key;
	}

}
