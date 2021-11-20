package jk.kamoru.flayground.source;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import jk.kamoru.flayground.FlayProperties;
import jk.kamoru.flayground.commons.FlayUtils;
import jk.kamoru.flayground.domain.Actress;
import jk.kamoru.flayground.domain.Flay;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@Repository
public class FileBasedFlaySource implements FlaySource {

	@Autowired InfoSourceVideo infoSourceVideo;

	@Autowired InfoSourceActress infoSourceActress;

	@Autowired InfoSourceStudio infoSourceStudio;

	@Autowired FlayProperties flayProperties;

	Map<Boolean, File[]> pathMap = new HashMap<>();

	Map<String, Flay> instanceFlayMap = new HashMap<>();
	Map<String, Flay> archiveFlayMap = new HashMap<>();

	@PostConstruct
	@Override
	public void load() {
		pathMap.put(true, ArrayUtils.addAll(flayProperties.getStagePaths(), flayProperties.getCoverPath(), flayProperties.getStoragePath()));
		pathMap.put(false, ArrayUtils.toArray(flayProperties.getArchivePath()));

		for (Entry<Boolean, File[]> entry : pathMap.entrySet()) {
			boolean instance = entry.getKey();
			File[] paths = entry.getValue();
			Collection<File> listFiles = new ArrayList<>();
			for (File path : paths) {
				assert path.isDirectory();

				Collection<File> found = FileUtils.listFiles(path, null, true);
				log.info(String.format("%5s file    - [%s] %s", found.size(), instance ? "Instance" : "Archive", path));
				listFiles.addAll(found);
			}

			for (File file : listFiles) {
				Resolved resolved = resolveFile(file);
				if (resolved.invalid) {
					log.warn(" invalid file - {}", file);
					continue;
				}

				Map<String, Flay> flayMap = instance ? instanceFlayMap : archiveFlayMap;
				if (!flayMap.containsKey(resolved.opus)) {
					flayMap.put(resolved.opus, obtainFlay(resolved, instance));
				}

				addFile(flayMap.get(resolved.opus), file);
			}
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

	class Resolved {

		public boolean invalid;
		public String studio;
		public String opus;
		public String title;
		public String actress;
		public String release;

	}

	private Resolved resolveFile(File file) {
		Resolved result = new Resolved();
		String rowData = file.getName();
		String[] parts = StringUtils.split(rowData, "]");
		if (parts == null || parts.length < 5) {
			result.invalid = true;
		} else {
			result.invalid = false;
			result.studio = StringUtils.replace(parts[0], "[", "");
			result.opus = StringUtils.replace(parts[1], "[", "");
			result.title = StringUtils.replace(parts[2], "[", "");
			result.actress = StringUtils.replace(parts[3], "[", "");
			result.release = StringUtils.replace(parts[4], "[", "");
		}
		return result;
	}

	private Flay obtainFlay(Resolved resolved, boolean instance) {
		Flay flay = new Flay();
		flay.setInstance(instance);
		flay.setStudio(infoSourceStudio.getOrNew(resolved.studio));
		flay.setOpus(resolved.opus);
		flay.setTitle(resolved.title);
		flay.setActress(findActress(resolved.actress));
		flay.setRelease(resolved.release);
		flay.setVideo(infoSourceVideo.getOrNew(resolved.opus));
		return flay;
	}

	private List<Actress> findActress(String names) {
		return Arrays.asList(StringUtils.split(names, ",")).stream().map(name -> infoSourceActress.getOrNew(name)).toList();
	}

	private void addFile(Flay flay, File file) {
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

}
