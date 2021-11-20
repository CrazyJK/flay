package jk.kamoru.flayground.service;

import java.util.Collection;
import jk.kamoru.flayground.FlaygroundController.Faram;
import jk.kamoru.flayground.domain.Actress;
import jk.kamoru.flayground.domain.Flay;
import jk.kamoru.flayground.domain.Studio;

public interface FlaygroundService {

	Collection<Flay> listFlay(Faram faram);

	Flay getFlay(String opus);

	Flay updateFlay(String opus, Flay flay);

	void callPlayer(String opus);

	void callEditorOfSubtitles(String opus);

	Collection<Actress> listActress(Faram faram);

	Actress createActress(Actress actress);

	Actress getActress(String name);

	Actress updateActress(String name, Actress actress);

	Collection<Studio> listStudio(Faram faram);

	Studio getStudio(String name);

	Studio updateStudio(String name, Studio studio);

}
