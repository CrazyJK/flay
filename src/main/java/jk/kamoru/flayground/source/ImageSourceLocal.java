package jk.kamoru.flayground.source;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import javax.annotation.PostConstruct;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import jk.kamoru.flayground.Flayground;
import jk.kamoru.flayground.commons.FlayUtils;
import jk.kamoru.flayground.domain.Image;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class ImageSourceLocal implements ImageSource {

	@Autowired Flayground flayground;

	Map<Long, Image> imageMap = new HashMap<>();

	private AtomicLong atomicId = new AtomicLong(0);

	@PostConstruct
	public void load() {
		File[] imagePaths = flayground.getImagePaths();
		for (File path : imagePaths) {
			Collection<File> found = FileUtils.listFiles(path, null, true);
			int count = 0;
			for (File file : found) {
				if (FlayUtils.FILE.isImage(file)) {
					Long id = atomicId.getAndIncrement();
					imageMap.put(id, Image.getInstance(id, file));
					count++;
				}
			}
			log.info(String.format("%5s image   - %s", count, path));
		}
	}

	@Override
	public Collection<Image> list() {
		return imageMap.values();
	}

	@Override
	public int size() {
		return imageMap.size();
	}

	@Override
	public Image get(Long id) {
		return imageMap.get(id);
	}

	@Override
	public void delete(Long id) {
		Image image = imageMap.remove(id);
		if (flayground.isRecyclebinUse()) {
			File imageFile = image.getFile();
			File recyclebin = new File(imageFile.toPath().getRoot().toFile(), flayground.getRecyclebin());
			try {
				FileUtils.moveFileToDirectory(imageFile, recyclebin, true);
			} catch (IOException e) {
				log.warn("fail to move", e);
			}
		} else {
			FileUtils.deleteQuietly(image.getFile());
		}
	}

}
