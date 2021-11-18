package jk.kamoru.flayground.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import org.springframework.stereotype.Service;
import jk.kamoru.flayground.domain.Flay;
import jk.kamoru.flayground.domain.Actress;
import jk.kamoru.flayground.domain.Studio;

@Service
public class FlaygroundServiceImpl implements FlaygroundService {

	@Override
	public Collection<Flay> listFlay(Map<String, String> params) {
		return Arrays.asList(new Flay());
	}

	@Override
	public Flay getFlay(String opus) {
		return null;
	}

	@Override
	public Flay updateFlay(String opus, Flay flay) {
		return null;
	}

	@Override
	public void callPlayer(String opus) {}

	@Override
	public void callEditorOfSubtitles(String opus) {}

	@Override
	public Collection<Actress> listActress(Map<String, String> params) {
		return null;
	}

	@Override
	public Actress createActress(Actress actress) {
		return null;
	}

	@Override
	public Actress getActress(String name) {
		return null;
	}

	@Override
	public Actress updateActress(String name, Actress actress) {
		return null;
	}

	@Override
	public Collection<Studio> listStudio(Map<String, String> params) {
		return null;
	}

	@Override
	public Studio getStudio(String name) {
		return null;
	}

	@Override
	public Studio updateStudio(String name, Studio studio) {
		return null;
	}

}
