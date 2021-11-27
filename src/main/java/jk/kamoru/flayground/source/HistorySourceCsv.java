package jk.kamoru.flayground.source;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;
import javax.annotation.PostConstruct;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import jk.kamoru.flayground.Flayground;
import jk.kamoru.flayground.domain.History;
import jk.kamoru.flayground.service.FlayException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class HistorySourceCsv implements HistorySource {

	@Autowired Flayground flayground;

	private Map<Long, History> historyMap = new TreeMap<>(Comparator.reverseOrder());

	private AtomicLong atomicId = new AtomicLong(0);

	private Path csvPath;

	@Override
	public Collection<History> list() {
		return historyMap.values();
	}

	@Override
	public History get(Long id) {
		return historyMap.get(id);
	}

	@Override
	public History save(History history) {
		Long id = atomicId.getAndIncrement();
		history.setId(id);
		historyMap.put(id, history);
		String line = String.format("%s, %s, %s, %s", history.getDate(), history.getOpus(), history.getAction(), history.getDesc());
		try {
			Files.writeString(csvPath, line, StandardCharsets.UTF_8, StandardOpenOption.APPEND);
		} catch (IOException e) {
			throw new FlayException("fail to save history", e);
		}
		return history;
	}

	@PostConstruct
	protected void load() throws IOException {
		csvPath = Paths.get(flayground.getInfoPath().toString(), flayground.getInfoFilename().getHISTORY());
		List<String> allLines = Files.readAllLines(csvPath, StandardCharsets.UTF_8);
		allLines.forEach(line -> {
			if (Strings.isNotEmpty(line)) {
				String[] parts = line.split(",", 4);
				Long id = atomicId.getAndIncrement();
				historyMap.put(id, new History(id, parts[0].trim(), parts[1].trim(), History.Action.valueOf(parts[2].trim()), parts[3].trim()));
			}
		});
		log.info(String.format("%5s line    - %s", allLines.size(), csvPath.toString()));
	}

}
