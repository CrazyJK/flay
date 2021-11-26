package jk.kamoru.flayground.domain;

import java.util.Date;
import jk.kamoru.flayground.source.HistorySource;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class History {

	public static enum Action {
		PLAY, DELETE, UPDATE;
	}

	Long id;
	String date;
	String opus;
	Action action;
	String desc;

	public static History getInstance(String opus, String action, String desc) {
		return getInstance(opus, History.Action.valueOf(action), desc);
	}

	public static History getInstance(String opus, Action action, String desc) {
		return new History(null, HistorySource.dateFormat.format(new Date()), opus, action, desc);
	}

}
