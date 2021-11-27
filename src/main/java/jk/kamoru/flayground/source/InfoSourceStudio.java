package jk.kamoru.flayground.source;

import java.io.File;
import java.util.List;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import jk.kamoru.flayground.Flayground;
import jk.kamoru.flayground.domain.Studio;

@Repository
public class InfoSourceStudio extends InfoSourceJsonAdapter<Studio, String> {

	@Autowired Flayground flayground;

	@Override
	File getInfoFile() {
		return new File(flayground.getInfoPath(), flayground.getInfoFilename().STUDIO);
	}

	@Override
	TypeReference<List<Studio>> getTypeReference() {
		return new TypeReference<List<Studio>>() {};
	}

	@Override
	Studio newInstance(String key) {
		return new Studio(key);
	}

}
