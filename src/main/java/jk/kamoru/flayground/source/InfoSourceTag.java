package jk.kamoru.flayground.source;

import java.io.File;
import java.util.List;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import jk.kamoru.flayground.Flayground;
import jk.kamoru.flayground.domain.Tag;

@Repository
public class InfoSourceTag extends InfoSourceJsonAdapter<Tag, Integer> {

	@Autowired Flayground flayground;

	@Override
	File getInfoFile() {
		return new File(flayground.getInfoPath(), flayground.getInfoFilename().TAG);
	}

	@Override
	TypeReference<List<Tag>> getTypeReference() {
		return new TypeReference<List<Tag>>() {};
	}

	@Override
	Tag newInstance(Integer key) {
		return new Tag(key);
	}

}
