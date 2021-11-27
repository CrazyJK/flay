package jk.kamoru.flayground.web;

import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jk.kamoru.flayground.domain.History;
import jk.kamoru.flayground.source.HistorySource;

@RestController
@RequestMapping("/history")
public class HistoryController {

	@Autowired HistorySource historySource;

	@GetMapping
	public Collection<History> list() {
		return historySource.list();
	}

	@GetMapping("/{id}")
	public History get(@PathVariable Long id) {
		return historySource.get(id);
	}

	@GetMapping("/opus/{opus}")
	public Collection<History> get(@PathVariable String opus) {
		return historySource.list().stream().filter(h -> h.getOpus().equalsIgnoreCase(opus)).toList();
	}

	@GetMapping("/opus/{opus}/{action}")
	public Collection<History> get(@PathVariable String opus, @PathVariable History.Action action) {
		return historySource.list().stream().filter(h -> h.getOpus().equalsIgnoreCase(opus)).filter(h -> h.getAction() == action).toList();
	}

	@GetMapping("/find/{keyword}")
	public Collection<History> find(@PathVariable String keyword) {
		String lowerCaseKeyword = keyword.toLowerCase();
		return historySource.list().stream().filter(h -> h.getOpus().toLowerCase().contains(lowerCaseKeyword) || h.getAction().name().equals(keyword) || h.getDesc().toLowerCase().contains(lowerCaseKeyword)).toList();
	}

}
