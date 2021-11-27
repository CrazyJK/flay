package jk.kamoru.flayground.source;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jk.kamoru.flayground.Flayground;
import jk.kamoru.flayground.commons.FlayUtils;
import jk.kamoru.flayground.domain.Flay;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@Repository
public class FileBasedFlaySource implements FlaySource {

	private static final Pattern FLAYFILE_PATTERN = Pattern.compile("[\\[](.*?)[\\]]");

	@Autowired InfoSourceVideo infoSourceVideo;

	@Autowired InfoSourceActress infoSourceActress;

	@Autowired InfoSourceStudio infoSourceStudio;

	@Autowired Flayground flayground;

	Map<Boolean, File[]> pathMap = new HashMap<>();

	Map<String, Flay> instanceFlayMap = new HashMap<>();
	Map<String, Flay> archiveFlayMap = new HashMap<>();

	@PostConstruct
	@Override
	public void load() {
		pathMap.put(true, ArrayUtils.addAll(flayground.getStagePaths(), flayground.getCoverPath(), flayground.getStoragePath()));
		pathMap.put(false, ArrayUtils.toArray(flayground.getArchivePath()));

		for (Entry<Boolean, File[]> entry : pathMap.entrySet()) {
			boolean instance = entry.getKey();
			File[] paths = entry.getValue();
			log.info("[{}]", instance ? "Instance" : "Archive");

			Collection<File> listFiles = new ArrayList<>();
			for (File path : paths) {
				assert path.isDirectory();

				Collection<File> found = FileUtils.listFiles(path, null, true);
				log.info(String.format("%5s file    - %s", found.size(), path));
				listFiles.addAll(found);
			}

			Map<String, Flay> flayMap = instance ? instanceFlayMap : archiveFlayMap;
			for (File file : listFiles) {

				Matcher matcher = FLAYFILE_PATTERN.matcher(file.getName());
				String[] part = new String[5];
				int count = 0;
				while (matcher.find()) {
					part[count++] = matcher.group(1).trim();
				}
				if (count != 5) {
					log.warn(" invalid file - {}", file);
					continue;
				}

				if (!flayMap.containsKey(part[1])) { // compare opus
					Flay flay = new Flay();
					flay.setStudio(infoSourceStudio.getOrNew(part[0]));
					flay.setOpus(part[1]);
					flay.setTitle(part[2]);
					flay.setActress(Stream.of(StringUtils.split(part[3], ",")).map(name -> infoSourceActress.getOrNew(name.trim())).toList());
					flay.setRelease(part[4]);
					flay.setVideo(infoSourceVideo.getOrNew(part[1]));
					flay.setInstance(instance);

					flayMap.put(part[1], flay);
				}

				Flay flay = flayMap.get(part[1]);

				if (FlayUtils.FILE.isVideo(file)) {
					flay.getFiles().get(Flay.FileType.MOVIE).add(file);
				} else if (FlayUtils.FILE.isSubtitles(file)) {
					flay.getFiles().get(Flay.FileType.SUBTITLES).add(file);
				} else if (FlayUtils.FILE.isImage(file)) {
					flay.getFiles().get(Flay.FileType.COVER).add(file);
				} else {
					log.warn("unknown file {} -> {}", flay.getOpus(), file);
				}
			}
			log.info(String.format("%5s Flay", flayMap.size()));
		}
	}

	@Override
	public Optional<Flay> get(String opus) {
		return Optional.ofNullable(instanceFlayMap.containsKey(opus) ? instanceFlayMap.get(opus) : archiveFlayMap.containsKey(opus) ? archiveFlayMap.get(opus) : null);
	}

	@Override
	public Collection<Flay> list() {
		return Stream.concat(instanceFlayMap.values().stream(), archiveFlayMap.values().stream()).toList();
	}

	@Override
	public Collection<Flay> listOfInstance() {
		return instanceFlayMap.values();
	}

	@Override
	public Collection<Flay> listOfArchive() {
		return archiveFlayMap.values();
	}

	@Override
	public Flay update(Flay flay) {
		try {
			get(flay.getOpus()).orElseThrow(Exception::new); // check is exists
			if (flay.isInstance()) {
				instanceFlayMap.put(flay.getOpus(), flay);
			} else {
				archiveFlayMap.put(flay.getOpus(), flay);
			}
			return get(flay.getOpus()).get();
		} catch (Exception e) {
			throw new FlayNotfoundException(flay.getOpus());
		}
	}

	@Override
	public void optimize() {
		// TODO 배치 처리
	}

}
