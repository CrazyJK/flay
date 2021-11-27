package jk.kamoru.flayground.web;

import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jk.kamoru.flayground.domain.Image;
import jk.kamoru.flayground.source.ImageSource;

@RestController
@RequestMapping("/image")
public class ImageController {

	@Autowired ImageSource imageSource;

	@GetMapping
	public Collection<Image> list() {
		return imageSource.list();
	}

	@GetMapping("/size")
	public int size() {
		return imageSource.size();
	}

	@GetMapping("/{id}")
	public Image get(@PathVariable Long id) {
		return imageSource.get(id);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		imageSource.delete(id);
	}

}
