package jk.kamoru.flayground.source;

import java.util.Collection;
import jk.kamoru.flayground.domain.Flay;

public interface FlaySource {

	void load();

	Flay get(String opus);

	Collection<Flay> list();

}
