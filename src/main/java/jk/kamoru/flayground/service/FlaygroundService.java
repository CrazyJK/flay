package jk.kamoru.flayground.service;

import java.util.Collection;
import java.util.Map;
import jk.kamoru.flayground.domain.Flay;
import jk.kamoru.flayground.domain.Actress;
import jk.kamoru.flayground.domain.Studio;

public interface FlaygroundService {

	Collection<Flay> listFlay(Map<String, String> params);

	Flay getFlay(String opus);

	Flay updateFlay(String opus, Flay flay);

	void callPlayer(String opus);

	void callEditorOfSubtitles(String opus);

	Collection<Actress> listActress(Map<String, String> params);

	Actress createActress(Actress actress);

	Actress getActress(String name);

	Actress updateActress(String name, Actress actress);

	Collection<Studio> listStudio(Map<String, String> params);

	Studio getStudio(String name);

	Studio updateStudio(String name, Studio studio);

}
