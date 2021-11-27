package jk.kamoru.flayground.source;

import java.util.Collection;
import jk.kamoru.flayground.domain.Image;

public interface ImageSource {

	Collection<Image> list();

	int size();

	Image get(Long id);

	void delete(Long id);

}
