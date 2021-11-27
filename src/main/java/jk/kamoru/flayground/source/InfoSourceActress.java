package jk.kamoru.flayground.source;

import java.io.File;
import java.util.List;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import jk.kamoru.flayground.Flayground;
import jk.kamoru.flayground.domain.Actress;
import jk.kamoru.flayground.domain.Image;

@Repository
public class InfoSourceActress extends InfoSourceJsonAdapter<Actress, String> {

	@Autowired Flayground flayground;
	@Autowired ImageSource imageSource;

	@Override
	File getInfoFile() {
		return new File(flayground.getInfoPath(), flayground.getInfoFilename().ACTRESS);
	}

	@Override
	TypeReference<List<Actress>> getTypeReference() {
		return new TypeReference<List<Actress>>() {};
	}

	@Override
	Actress newInstance(String actressname) {
		return new Actress(actressname);
	}

	@Override
	void extraInfoLoad() {
		for (Actress actress : list) {
			actress.setCovers(imageSource.list().stream().filter(i -> i.getName().toLowerCase().startsWith(actress.getName().toLowerCase())).map(Image::getFile).toList());
		}
	}

}
