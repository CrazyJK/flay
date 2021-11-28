package jk.kamoru.flayground.service;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileExistsException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import jk.kamoru.flayground.Flayground;
import jk.kamoru.flayground.commons.FlayUtils;
import jk.kamoru.flayground.domain.Actress;
import jk.kamoru.flayground.domain.Flay;
import jk.kamoru.flayground.domain.Flay.FileType;
import jk.kamoru.flayground.domain.History;
import jk.kamoru.flayground.domain.Studio;
import jk.kamoru.flayground.domain.Tag;
import jk.kamoru.flayground.domain.Video;
import jk.kamoru.flayground.source.FlayNotfoundException;
import jk.kamoru.flayground.source.FlaySource;
import jk.kamoru.flayground.source.HistorySource;
import jk.kamoru.flayground.source.InfoSourceActress;
import jk.kamoru.flayground.source.InfoSourceStudio;
import jk.kamoru.flayground.source.InfoSourceTag;
import jk.kamoru.flayground.source.InfoSourceVideo;
import jk.kamoru.flayground.web.FlaygroundController.Faram;
import jk.kamoru.flayground.web.FlaygroundController.Faram.ORDER;
import jk.kamoru.flayground.web.FlaygroundController.Faram.SOURCE;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FlaygroundServiceImpl implements FlaygroundService {

	@Autowired FlaySource flaySource;

	@Autowired InfoSourceActress infoSourceActress;

	@Autowired InfoSourceStudio infoSourceStudio;

	@Autowired InfoSourceTag infoSourceTag;

	@Autowired InfoSourceVideo infoSourceVideo;

	@Autowired HistorySource historySource;

	@Autowired ObjectMapper objectMapper;

	@Autowired Flayground flayground;

	@Override
	public Collection<Flay> listFlay(Faram faram) {
		log.info("listFlay - {}", faram);

		// source
		final SOURCE source = faram.getSource();
		Collection<Flay> list;
		switch (source) {
			case INSTANCE:
				list = flaySource.listOfInstance();
				break;
			case ARCHIVE:
				list = flaySource.listOfArchive();
				break;
			case ALL:
			default:
				list = flaySource.list();
				break;
		}

		// TODO filter
		// (title = 며느리 or tag = 늙은이) and actress like Karen and rank in (0, 5)
		// (, ) 로 분리
		// key operator value 분리

		// order
		final List<ORDER> orders = faram.getOrder();
		if (orders.size() > 0) {
			return list.stream().sorted((f1, f2) -> {
				int compared = 0;
				for (ORDER order : orders) {
					if (compared != 0) {
						break;
					}
					switch (order) {
						case STUDIO:
							compared = StringUtils.compareIgnoreCase(f1.getStudio().getName(), f2.getStudio().getName());
							break;
						case OPUS:
							compared = StringUtils.compareIgnoreCase(f1.getOpus(), f2.getOpus());
							break;
						case TITLE:
							compared = StringUtils.compareIgnoreCase(f1.getTitle(), f2.getTitle());
							break;
						case ACTRESS:
							compared = StringUtils.compareIgnoreCase(f1.getActress().toString(), f2.getActress().toString());
							break;
						case RELEASE:
							compared = StringUtils.compareIgnoreCase(f1.getRelease(), f2.getRelease());
							break;
						case LASTMODIFIED:
							compared = FlayUtils.compareLong(f1.getModified(), f2.getModified());
							break;
						case LENGTH:
							compared = FlayUtils.compareLong(f1.getLength(), f2.getLength());
							break;
						case PLAY:
							compared = FlayUtils.compareInteger(f1.getVideo().getPlay(), f2.getVideo().getPlay());
							break;
						case RANK:
							compared = FlayUtils.compareInteger(f1.getVideo().getRank(), f2.getVideo().getRank());
							break;
						case LASTACCESS:
							compared = FlayUtils.compareLong(f1.getVideo().getLastAccess(), f2.getVideo().getLastAccess());
							break;
						case SCORE:
							compared = FlayUtils.compareInteger(calcScore(f1), calcScore(f2));
							break;
						case _STUDIO:
							compared = StringUtils.compareIgnoreCase(f2.getStudio().getName(), f1.getStudio().getName());
							break;
						case _OPUS:
							compared = StringUtils.compareIgnoreCase(f2.getOpus(), f1.getOpus());
							break;
						case _TITLE:
							compared = StringUtils.compareIgnoreCase(f2.getTitle(), f1.getTitle());
							break;
						case _ACTRESS:
							compared = StringUtils.compareIgnoreCase(f2.getActress().toString(), f1.getActress().toString());
							break;
						case _RELEASE:
							compared = StringUtils.compareIgnoreCase(f2.getRelease(), f1.getRelease());
							break;
						case _LASTMODIFIED:
							compared = FlayUtils.compareLong(f2.getModified(), f1.getModified());
							break;
						case _LENGTH:
							compared = FlayUtils.compareLong(f2.getLength(), f1.getLength());
							break;
						case _PLAY:
							compared = FlayUtils.compareInteger(f2.getVideo().getPlay(), f1.getVideo().getPlay());
							break;
						case _RANK:
							compared = FlayUtils.compareInteger(f2.getVideo().getRank(), f1.getVideo().getRank());
							break;
						case _LASTACCESS:
							compared = FlayUtils.compareLong(f2.getVideo().getLastAccess(), f1.getVideo().getLastAccess());
							break;
						case _SCORE:
							compared = FlayUtils.compareInteger(calcScore(f2), calcScore(f1));
							break;
						default:
							throw new FlayException("sort is not implemented by " + order);
					}
				}
				return compared;
			}).toList();
		} else {
			return list;
		}
	}

	private int calcScore(Flay flay) {
		return flay.getVideo().getRank() * flayground.getScore().getRankPoint()
				+ flay.getVideo().getPlay() * flayground.getScore().getPlayPoint()
				+ (flay.getFiles().get(Flay.FileType.SUBTITLES).size() > 0 ? 1 : 0) * flayground.getScore().getSubtitlesPoint();
	}

	@Override
	public Flay getFlay(String opus) {
		return flaySource.get(opus).orElseThrow(() -> new FlayNotfoundException(opus));
	}

	@Override
	public Flay updateFlay(String opus, Flay flay) {
		if (!opus.equals(flay.getOpus())) {
			throw new FlayException("Opus cannot be modified");
		}
		Flay found = flaySource.get(opus).orElseThrow(() -> new FlayNotfoundException(opus));
		Flay merged = mergeFlay(found, flay);

		log.info("changed video={}, files={}", merged.isChangedVideo(), merged.isChangedFiles());
		// set file
		if (merged.isChangedFiles()) {
			final String newFilename = flay.getFullname();
			log.info("newFilename {}", newFilename);

			Map<FileType, List<File>> newFiles = new HashMap<>();

			for (Entry<FileType, List<File>> entry : merged.getFiles().entrySet()) {
				FileType fileType = entry.getKey();
				List<File> fileList = entry.getValue();
				int fileCount = fileList.size();

				if (fileCount > 0) {
					List<File> newFileList = new ArrayList<>();
					int count = 0;
					for (File file : fileList) {
						String filename = newFilename;
						if (fileCount > 1) {
							filename += ++count;
						}
						File destFile = new File(file.getParent(), filename + "." + FilenameUtils.getExtension(file.getName()));
						try {
							FileUtils.moveFile(file, destFile);
							log.info("moveFile {} -> {}", file, destFile);
						} catch (FileExistsException e) {
							log.warn("file exists {}", destFile);
						} catch (IllegalArgumentException | IOException e) {
							throw new FlayException("file rename error", e);
						}
						newFileList.add(destFile);
					}
					newFiles.put(fileType, newFileList);
				}
			}

			merged.setFiles(newFiles);
		}
		// set video
		if (merged.isChangedVideo()) {
			Video updatedVideo = infoSourceVideo.update(merged.getVideo());

			History history;
			try {
				history = History.getInstance(opus, History.Action.UPDATE, objectMapper.writeValueAsString(updatedVideo));
			} catch (JsonProcessingException e) {
				throw new FlayException("fail to convert video", e);
			}
			historySource.save(history);
		}

		// reset changed
		merged.setChangedFiles(false);
		merged.setChangedVideo(false);

		return flaySource.update(merged);
	}

	@Async
	@Override
	public void callPlayer(String opus) {
		Flay flay = flaySource.get(opus).orElseThrow(FlayNotfoundException::new);
		execCommand(flayground.getPlayerApp(), flay.getFiles().get(Flay.FileType.MOVIE).get(0));
		historySource.save(History.getInstance(flay.getOpus(), History.Action.PLAY, flay.getFullname()));
	}

	@Async
	@Override
	public void callEditorOfSubtitles(String opus) {
		Flay flay = flaySource.get(opus).orElseThrow(FlayNotfoundException::new);
		execCommand(flayground.getEditorApp(), flay.getFiles().get(Flay.FileType.SUBTITLES).get(0));
	}

	@Override
	public Collection<Actress> listActress(Faram faram) {
		List<Actress> list = infoSourceActress.list();
		// TODO filter
		// name like Karen and birth start 2000 or body include C or height > 160 or debut = 2020
		// order
		final List<ORDER> orders = faram.getOrder();
		if (orders.size() > 0) {
			return list.stream().sorted((a1, a2) -> {
				int compared = 0;
				for (ORDER order : orders) {
					if (compared != 0) {
						break;
					}
					switch (order) {
						case NAME:
							compared = StringUtils.compareIgnoreCase(a1.getName(), a2.getName());
							break;
						case LOCALNAME:
							compared = StringUtils.compareIgnoreCase(a1.getLocalName(), a2.getLocalName());
							break;
						case BIRTH:
							compared = StringUtils.compareIgnoreCase(a1.getBirth(), a2.getBirth());
							break;
						case BODY:
							compared = StringUtils.compareIgnoreCase(a1.getBody(), a2.getBody());
							break;
						case HEIGHT:
							compared = FlayUtils.compareInteger(a1.getHeight(), a2.getHeight());
							break;
						case DEBUT:
							compared = FlayUtils.compareInteger(a1.getDebut(), a2.getDebut());
							break;
						case FAVORITE:
							compared = BooleanUtils.compare(a1.isFavorite(), a2.isFavorite());
							break;
						case _NAME:
							compared = StringUtils.compareIgnoreCase(a2.getName(), a1.getName());
							break;
						case _LOCALNAME:
							compared = StringUtils.compareIgnoreCase(a2.getLocalName(), a1.getLocalName());
							break;
						case _BIRTH:
							compared = StringUtils.compareIgnoreCase(a2.getBirth(), a1.getBirth());
							break;
						case _BODY:
							compared = StringUtils.compareIgnoreCase(a2.getBody(), a1.getBody());
							break;
						case _HEIGHT:
							compared = FlayUtils.compareInteger(a2.getHeight(), a1.getHeight());
							break;
						case _DEBUT:
							compared = FlayUtils.compareInteger(a2.getDebut(), a1.getDebut());
							break;
						case _FAVORITE:
							compared = BooleanUtils.compare(a2.isFavorite(), a1.isFavorite());
							break;
						default:
							throw new FlayException("sort is not implemented by " + order);
					}
				}
				return compared;
			}).toList();
		} else {
			return list;
		}
	}

	@Override
	public Actress createActress(Actress actress) {
		return infoSourceActress.create(actress);
	}

	@Override
	public Actress getActress(String name) {
		return infoSourceActress.get(name);
	}

	@Override
	public Actress updateActress(String name, Actress actress) {
		Actress localActress = infoSourceActress.get(name);
		if (localActress.getName().equals(actress.getName())) {
			return infoSourceActress.update(actress);
		} else {
			throw new FlayException("actress name is not equals");
		}
	}

	@Override
	public Collection<Studio> listStudio(Faram faram) {
		List<Studio> list = infoSourceStudio.list();
		// TODO filter
		// name = S1 or company = S1
		// order
		final List<ORDER> orders = faram.getOrder();
		if (orders.size() > 0) {
			return list.stream().sorted((s1, s2) -> {
				int compared = 0;
				for (ORDER order : orders) {
					if (compared != 0) {
						break;
					}
					switch (order) {
						case NAME:
							compared = StringUtils.compareIgnoreCase(s1.getName(), s2.getName());
							break;
						case COMPANY:
							compared = StringUtils.compareIgnoreCase(s1.getCompany(), s2.getCompany());
							break;
						case HOMEPAGE:
							compared = FlayUtils.compareURL(s1.getHomepage(), s2.getHomepage());
							break;
						case _NAME:
							compared = StringUtils.compareIgnoreCase(s2.getName(), s1.getName());
							break;
						case _COMPANY:
							compared = StringUtils.compareIgnoreCase(s2.getCompany(), s1.getCompany());
							break;
						case _HOMEPAGE:
							compared = FlayUtils.compareURL(s2.getHomepage(), s1.getHomepage());
							break;
						default:
							throw new FlayException("sort is not implemented by " + order);
					}
				}
				return compared;
			}).toList();
		} else {
			return list;
		}
	}

	@Override
	public Studio getStudio(String name) {
		return infoSourceStudio.get(name);
	}

	@Override
	public Studio updateStudio(String name, Studio studio) {
		Studio localStudio = infoSourceStudio.get(name);
		if (localStudio.getName().equals(studio.getName())) {
			return infoSourceStudio.update(studio);
		} else {
			throw new FlayException("studio name is not equals");
		}
	}

	private Flay mergeFlay(Flay found, Flay flay) {
		try {
			return merge(found, flay);
		} catch (Exception e) {
			throw new FlayException("fail to merge", e);
		}
	}

	@SuppressWarnings("unchecked")
	private <T> T merge(T local, T remote) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		Class<?> clazz = local.getClass();
		String className = clazz.getSimpleName();
		Object merged = clazz.getDeclaredConstructor().newInstance();

		for (Field field : clazz.getDeclaredFields()) {
			int mod = field.getModifiers();
			if (Modifier.isFinal(mod)) {
				continue;
			}

			field.setAccessible(true);
			Object localValue = field.get(local);
			Object remoteValue = field.get(remote);

			String modifier = (mod == 0) ? "" : Modifier.toString(mod);
			String fieldType = field.getType().getSimpleName();
			String fieldName = field.getName();

			System.out.format("%-10s %10s [%9s] %10s = %s : %s%n", className, modifier, fieldType, fieldName, localValue, remoteValue);

			switch (fieldName) {
				case "video":
					if (remoteValue != null && !((Video) localValue).equals((Video) remoteValue)) {
						field.set(merged, merge(localValue, remoteValue));
						Flay flay = (Flay) merged;
						flay.setChangedVideo(true); // changed video
					} else {
						field.set(merged, localValue);
					}
					break;
				case "studio":
					Studio localStudio = (Studio) localValue;
					Studio remoteStudio = (Studio) remoteValue;
					if (remoteStudio != null && StringUtils.isNotBlank(remoteStudio.getName()) && !localStudio.getName().equals(remoteStudio.getName())) {
						Studio studio = infoSourceStudio.get(remoteStudio.getName());
						field.set(merged, studio);
						Flay flay = (Flay) merged;
						flay.setChangedFiles(true); // changed files
					} else {
						field.set(merged, localValue);
					}
					break;
				case "actress":
					List<Actress> remoteActressList = (List<Actress>) remoteValue;
					if (remoteActressList != null && remoteActressList.size() > 0) {
						List<Actress> actressList = new ArrayList<>();
						for (Actress remoteActress : remoteActressList) {
							if (StringUtils.isNotBlank(remoteActress.getName())) {
								Actress actress = infoSourceActress.get(remoteActress.getName());
								actressList.add(actress);
							}
						}
						if (actressList.size() > 0) {
							field.set(merged, actressList);
							Flay flay = (Flay) merged;
							flay.setChangedFiles(true); // changed files
						} else {
							field.set(merged, localValue);
						}
					} else {
						field.set(merged, localValue);
					}
					break;
				case "tags":
					List<Tag> removeTags = (List<Tag>) remoteValue;
					if (removeTags != null && removeTags.size() > 0) {
						List<Tag> tagList = new ArrayList<>();
						for (Tag remoteTag : removeTags) {
							Tag tag = infoSourceTag.get(remoteTag.getId());
							tagList.add(tag);
						}
						if (tagList.size() > 0) {
							field.set(merged, tagList);
						} else {
							field.set(merged, localValue);
						}
					} else {
						field.set(merged, localValue);
					}
					break;
				case "title":
				case "release":
					if (remoteValue != null && !localValue.equals(remoteValue)) {
						field.set(merged, remoteValue);
						Flay flay = (Flay) merged;
						flay.setChangedFiles(true); // changed files
					} else {
						field.set(merged, localValue);
					}
					break;
				case "instance":
				case "opus":
				case "files":
					field.set(merged, localValue); // merge 제외. 원본 유지
					break;
				case "lastAccess":
					field.set(merged, System.currentTimeMillis());
					break;
				case "changedVideo":
				case "changedFiles":
					break;
				default:
					field.set(merged, (remoteValue != null) ? remoteValue : localValue);
			}
		}

		return (T) merged;
	}

	private void execCommand(File... files) {
		List<String> command = Stream.of(files).map(File::getAbsolutePath).toList();
		log.info("exec {}", command);
		try {
			Process process = new ProcessBuilder(command).redirectOutput(Redirect.DISCARD).redirectError(Redirect.INHERIT).start();
			log.info("process {}", process.info());
		} catch (IOException e) {
			throw new FlayException("fail to exec", e);
		}
	}

}
