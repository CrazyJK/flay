package jk.kamoru.flayground.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import jk.kamoru.flayground.domain.Actress;
import jk.kamoru.flayground.domain.Flay;
import jk.kamoru.flayground.domain.Studio;
import jk.kamoru.flayground.service.FlaygroundService;
import jk.kamoru.flayground.web.FlaygroundController.Faram.FIELD;
import jk.kamoru.flayground.web.support.QueryStringArgResolver;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/ground")
public class FlaygroundController {

	@Autowired FlaygroundService flaygroundService;

	// GET /flay
	@GetMapping("/flay")
	public MappingJacksonValue listFlay(@QueryStringArgResolver Faram faram) throws JsonProcessingException {
		return getJacksonValue(flaygroundService.listFlay(faram), faram.getFields());
	}

	// GET /flay/{opus}
	@GetMapping("/flay/{opus}")
	public MappingJacksonValue getFlay(@PathVariable String opus) {
		return getJacksonValue(flaygroundService.getFlay(opus), null);
	}

	// PATCH /flay/{opus}
	@PatchMapping("/flay/{opus}")
	public MappingJacksonValue updateFlay(@PathVariable String opus, @RequestBody Flay flay) {
		return getJacksonValue(flaygroundService.updateFlay(opus, flay), null);
	}

	// HEAD /flay/{opus}/play
	@GetMapping("/flay/{opus}/play")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void callPlayer(@PathVariable String opus) {
		flaygroundService.callPlayer(opus);
	}

	// HEAD /flay/{opus}/subtitles
	@GetMapping("/flay/{opus}/subtitles")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void callEditorOfSubtitles(@PathVariable String opus) {
		flaygroundService.callEditorOfSubtitles(opus);
	}

	// GET /actress
	@GetMapping("/actress")
	public Collection<Actress> listActress(@QueryStringArgResolver Faram faram) {
		return flaygroundService.listActress(faram);
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
	public Collection<Studio> listStudio(@QueryStringArgResolver Faram faram) {
		return flaygroundService.listStudio(faram);
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

	private MappingJacksonValue getJacksonValue(Object object, List<FIELD> fields) {
		MappingJacksonValue mapping = new MappingJacksonValue(object);
		mapping.setFilters(new SimpleFilterProvider().addFilter("flayJsonFilter", new FlayJsonFilter(fields)));
		return mapping;
	}


	/**
	 * <pre>
	 * Params
	 *  - Source: ?source=[instance|archive|all]
	 * 	- Filtering: ?filter=(opus = FSDSS202 or title = 며느리 or tag = 늙은이) and rank > 3
	 * 	- Ordering: ?order=name,-level
	 * 	- Field-Selecting: ?fields=field[, field]
	 * </pre>
	 */
	@Data
	@Slf4j
	public static class Faram {

		public static enum SOURCE {
			ALL, INSTANCE, ARCHIVE;
		}

		public static enum FIELD {
			STUDIO, OPUS, TITLE, ACTRESS, RELEASE, LASTMODIFIED, VIDEO, FILES;
		}

		public static enum ORDER {
			// for flay
			STUDIO, OPUS, TITLE, ACTRESS, RELEASE, LASTMODIFIED, LENGTH, PLAY, RANK, LASTACCESS, SCORE, _STUDIO, _OPUS, _TITLE, _ACTRESS, _RELEASE, _LASTMODIFIED, _LENGTH, _PLAY, _RANK, _LASTACCESS, _SCORE,
			// for actress
			NAME, LOCALNAME, BIRTH, BODY, HEIGHT, DEBUT, FAVORITE, _NAME, _LOCALNAME, _BIRTH, _BODY, _HEIGHT, _DEBUT, _FAVORITE,
			// for studio
			COMPANY, HOMEPAGE, _COMPANY, _HOMEPAGE;
		}

		SOURCE source = SOURCE.ALL;
		String filter;
		List<ORDER> order = new ArrayList<>();
		List<FIELD> fields = new ArrayList<>();

		public void setSource(String source) {
			try {
				this.source = SOURCE.valueOf(source.toUpperCase());
			} catch (Exception e) {
				log.trace("not exists source: {}", e.getMessage());
			}
		}

		public void setOrder(String values) {
			if (values == null) {
				return;
			}
			for (String value : StringUtils.split(values, ",")) {
				try {
					order.add(ORDER.valueOf(value.trim().toUpperCase()));
				} catch (Exception e) {
					log.trace("not exists order: {}", e.getMessage());
				}
			}
		}

		public void setFields(String values) {
			if (values == null) {
				return;
			}
			for (String value : StringUtils.split(values, ",")) {
				try {
					fields.add(FIELD.valueOf(value.trim().toUpperCase()));
				} catch (Exception e) {
					log.trace("not exists field: {}", e.getMessage());
				}
			}
		}

		public static class Filter {

			String studio;
			String opus;
			String title;
			String actress;
			String release;
			String rank;
			String tag;

		}

	}

}
