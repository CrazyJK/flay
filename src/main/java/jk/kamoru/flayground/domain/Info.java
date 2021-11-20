package jk.kamoru.flayground.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface Info<K> {

	@JsonIgnore
	K getKey();

	void setKey(K key);

}
