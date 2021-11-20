package jk.kamoru.flayground.domain;

import java.net.URL;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Studio implements Info<String> {

	String name;
	String company;
	URL homepage;

	public Studio(String key) {
		setKey(key);
		this.company = "";
		this.homepage = null;
	}

	@Override
	public String getKey() {
		return name;
	}

	@Override
	public void setKey(String key) {
		this.name = key;
	}

}
