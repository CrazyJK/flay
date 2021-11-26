package jk.kamoru.flayground;

import java.io.File;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "flay")
public class FlayProperties {

	// locallize properties
	private File playerApp;
	private File editorApp;
	private File paintApp;

	private File archivePath;
	private File storagePath;
	private File[] stagePaths;
	private File coverPath;
	private File queuePath;
	private File candidatePath;
	private File subtitlesPath;
	private File infoPath;
	private File[] todayisPaths;
	private File[] imagePaths;
	private File backupPath;

	// common properties
	private int storageLimit = 7168;
	private String recyclebin = "FLAY_RECYCLEBIN";
	private boolean recyclebinUse = true;

	private Score score = new Score();
	private Backup backup = new Backup();
	private InfoFilename infoFilename = new InfoFilename();

	@Data
	public static class Score {
		private int rankPoint = 20;
		private int playPoint = 1;
		private int subtitlesPoint = 30;
		private int favoritePoint = 30;
	}

	@Data
	public static class Backup {
		private String instanceJarFilename = "flayground-instance.jar";
		private String archiveJarFilename = "flayground-archive.jar";
		private String instanceCsvFilename = "flay-instance.csv";
		private String archiveCsvFilename = "flay-archive.csv";
	}

	@Data
	public static class InfoFilename {
		public String HISTORY = "history.csv";
		public String ACTRESS = "actress.json";
		public String STUDIO = "studio.json";
		public String VIDEO = "video.json";
		public String TAG = "tag.json";
		public String ACCESS = "access.json";
		public String NOTE = "note.json";
	}

}
