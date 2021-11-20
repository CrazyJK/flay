package jk.kamoru.flayground.source;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class InfoNotfoundException extends RuntimeException {

	public InfoNotfoundException(Object key) {
		super(key.toString());
	}

}
