package jk.kamoru.flayground.source;

import java.text.SimpleDateFormat;
import java.util.Collection;
import jk.kamoru.flayground.domain.History;

public interface HistorySource {

	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	Collection<History> list();

	History get(Long id);

	History save(History history);

}
