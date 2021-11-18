package jk.kamoru.flayground;

import java.util.Collection;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import jk.kamoru.flayground.domain.Flay;
import jk.kamoru.flayground.domain.Actress;
import jk.kamoru.flayground.domain.Studio;
import jk.kamoru.flayground.service.FlaygroundService;

@RestController
@RequestMapping("/ground")
public class FlaygroundController {

	@Autowired FlaygroundService flaygroundService;

	/*
	 * Params - Field-Selecting: ?fields=field[, field] - Filtering: ?filter=type eq ssd or (cpu eq 1 or
	 * memory gt 2) and staus ne 04 - Ordering: ?order=name,-level
	 */

	// GET /flay
	@GetMapping("/flay")
	public Collection<Flay> listFlay(@RequestParam Map<String, String> params) {
		return flaygroundService.listFlay(params);
	}

	// GET /flay/{opus}
	@GetMapping("/flay/{opus}")
	public Flay getFlay(@PathVariable String opus) {
		return flaygroundService.getFlay(opus);
	}

	// PATCH /flay/{opus}
	@PatchMapping("/flay/{opus}")
	public Flay updateFlay(@PathVariable String opus, @RequestBody Flay flay) {
		return flaygroundService.updateFlay(opus, flay);
	}

	// HEAD /flay/{opus}/play
	@GetMapping("/flay/{opus}/play")
	@ResponseStatus(HttpStatus.PROCESSING)
	public void callPlayer(@PathVariable String opus) {
		flaygroundService.callPlayer(opus);
	}

	// HEAD /flay/{opus}/subtitles
	@GetMapping("/flay/{opus}/subtitles")
	@ResponseStatus(HttpStatus.PROCESSING)
	public void callEditorOfSubtitles(@PathVariable String opus) {
		flaygroundService.callEditorOfSubtitles(opus);
	}

	// GET /actress
	@GetMapping("/actress")
	public Collection<Actress> listActress(@RequestParam Map<String, String> params) {
		return flaygroundService.listActress(params);
	}

	// POST /actress
	@PostMapping("/actress")
	@ResponseStatus(HttpStatus.CREATED)
	public Actress createActress(@RequestBody Actress actress) {
		return flaygroundService.createActress(actress);
	}

	// GET /actress/{name}
	@GetMapping("/actress/{name}")
	public Actress getActress(@PathVariable String name) {
		return flaygroundService.getActress(name);
	}

	// PATCH /actress/{name}
	@PatchMapping("/actress/{name}")
	public Actress updateActress(@PathVariable String name, @RequestBody Actress actress) {
		return flaygroundService.updateActress(name, actress);
	}

	// GET /studio
	@GetMapping("/studio")
	public Collection<Studio> listStudio(@RequestParam Map<String, String> params) {
		return flaygroundService.listStudio(params);
	}

	// GET /studio/{name}
	@GetMapping("/studio/{name}")
	public Studio getStudio(@PathVariable String name) {
		return flaygroundService.getStudio(name);
	}

	// PATCH /studio/{name}
	@PatchMapping("/studio/{name}")
	public Studio updateStudio(@PathVariable String name, @RequestBody Studio studio) {
		return flaygroundService.updateStudio(name, studio);
	}

}
