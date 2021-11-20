package jk.kamoru.flayground.source;

import java.util.Collection;
import java.util.Optional;
import jk.kamoru.flayground.domain.Flay;

public interface FlaySource {

	void load();

	Optional<Flay> get(String opus);

	Collection<Flay> list();

	Collection<Flay> listOfInstance();

	Collection<Flay> listOfArchive();

	Flay update(Flay flay);

	void optimize();

}
