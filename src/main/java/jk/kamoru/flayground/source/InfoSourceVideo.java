package jk.kamoru.flayground.source;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import jk.kamoru.flayground.FlayProperties;
import jk.kamoru.flayground.domain.Tag;
import jk.kamoru.flayground.domain.Video;

@Repository
public class InfoSourceVideo extends InfoSourceJsonAdapter<Video, String> {

	@Autowired FlayProperties flayProperties;

	@Autowired InfoSourceTag infoSourceTag;

	@Override
	File getInfoFile() {
		return new File(flayProperties.getInfoPath(), flayProperties.getInfoFilename().VIDEO);
	}

	@Override
	TypeReference<List<Video>> getTypeReference() {
		return new TypeReference<List<Video>>() {};
	}

	@Override
	Video newInstance(String key) {
		return new Video(key);
	}

	void extraInfoLoad() {
		// valid tag
		list.forEach(v -> {
			List<Tag> existsTagList = new ArrayList<>();
			v.getTags().forEach(t -> {
				try {
					existsTagList.add(infoSourceTag.get(t.getId()));
				} catch (InfoNotfoundException e) {
					// if not found, pass
				}
			});
			v.setTags(existsTagList);
		});
		// save new composed tag list
		save();
	}

}
