package jk.kamoru.flayground.web;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import jk.kamoru.flayground.domain.Flay;
import jk.kamoru.flayground.service.FlaygroundService;
import jk.kamoru.flayground.source.ImageSource;

@Controller
@RequestMapping("/static")
public class StaticImageProvider {

	@Autowired FlaygroundService flaygroundService;

	@Autowired ImageSource imageSource;

	@GetMapping("/cover/{opus}")
	@ResponseBody
	public HttpEntity<byte[]> getCover(@PathVariable String opus) throws IOException {
		return getImageEntity(flaygroundService.getFlay(opus).getFiles().get(Flay.FileType.COVER).get(0));
	}

	@GetMapping("/image/{id}")
	@ResponseBody
	public HttpEntity<byte[]> getImage(@PathVariable Long id) throws IOException {
		return getImageEntity(imageSource.get(id).getFile());
	}

	private HttpEntity<byte[]> getImageEntity(File file) throws IOException {
		if (file == null) {
			return null;
		}
		byte[] bytes = FileUtils.readFileToByteArray(file);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentLength(file.length());
		headers.setContentType(probeMediaType(file));
		return new HttpEntity<byte[]>(bytes, headers);
	}

	private MediaType probeMediaType(File file) {
		try {
			return MediaType.valueOf(Files.probeContentType(file.toPath()));
		} catch (InvalidMediaTypeException | IOException e) {
			String suffix = FilenameUtils.getExtension(file.getName());
			if ("webp".equalsIgnoreCase(suffix)) {
				return MediaType.valueOf("image/webp");
			}
			return MediaType.IMAGE_JPEG;
		}
	}

}
