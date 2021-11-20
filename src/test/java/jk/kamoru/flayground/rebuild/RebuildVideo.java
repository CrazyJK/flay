package jk.kamoru.flayground.rebuild;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import jk.kamoru.flayground.domain.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class RebuildVideo {

	public static void main(String[] args) throws Exception {
		ObjectMapper jsonReader = new ObjectMapper();
		ObjectWriter jsonWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();

		// load tag
		File tagInfoFile = new File("K:\\Crazy\\Info\\tag.json");
		List<Tag> tagList = jsonReader.readValue(tagInfoFile, getTagTypeReference());

		File oldInfoVideoFile = new File("K:\\Crazy\\Info\\video.json");
		File newInfoVideoFile = new File("K:\\Crazy\\Info\\video_new.json");

		List<OldVideo> oldVideoList = jsonReader.readValue(oldInfoVideoFile, getTypeReference());
		List<NewVideo> newVideoList = new ArrayList<>();

		for (OldVideo oldVideo : oldVideoList) {
			List<Tag> tags = new ArrayList<>();

			for (Tag tag : tagList) {
				for (Integer idx : oldVideo.getTags()) {
					if (idx.equals(tag.getId())) {
						tags.add(tag);
						break;
					}
				}
			}

			NewVideo newVideo = new NewVideo(oldVideo.getOpus(), oldVideo.getPlay(), oldVideo.getRank(), oldVideo.getComment(), oldVideo.getLastAccess(), tags);
			newVideoList.add(newVideo);
		}

		jsonWriter.writeValue(newInfoVideoFile, newVideoList);
	}

	static TypeReference<List<Tag>> getTagTypeReference() {
		return new TypeReference<List<Tag>>() {};
	}

	static TypeReference<List<OldVideo>> getTypeReference() {
		return new TypeReference<List<OldVideo>>() {};
	}

}


@NoArgsConstructor
@Data
class OldVideo {
	String opus;
	int play;
	int rank;
	String comment;
	long lastAccess;
	List<Integer> tags;
}


@AllArgsConstructor
@NoArgsConstructor
@Data
class NewVideo {
	String opus;
	int play;
	int rank;
	String comment;
	long lastAccess;
	List<Tag> tags;
}
