package jk.kamoru.flayground.source;

import java.io.File;
import java.util.List;
import jk.kamoru.flayground.domain.Info;

public abstract class InfoSourceAdapter<T extends Info<K>, K> implements InfoSource<T, K> {

	List<T> list;

	/**
	 * json 정보 파일
	 * @return
	 */
	abstract File getInfoFile();

	/**
	 * 도메인 생성
	 * @param key
	 * @return
	 */
	abstract T newInstance(K key);

	/**
	 * 도메인 저장
	 */
	abstract void save();

	@Override
	public List<T> list() {
		return list;
	}

	@Override
	public T get(K key) {
		for (T t : list) {
			if (t.getKey().equals(key)) {
				return t;
			}
		}
		throw new InfoNotfoundException(key);
	}

	@Override
	public T getOrNew(K key) {
		try {
			return get(key);
		} catch (InfoNotfoundException e) {
			T newInstance = newInstance(key);
			list.add(newInstance);
			save();
			return newInstance;
		}
	}

	@Override
	public T create(T create) {
		try {
			get(create.getKey());
			throw new IllegalStateException("duplicated key " + create.getKey());
		} catch (InfoNotfoundException e) {
			list.add(create);
			save();
			return create;
		}
	}

	@Override
	public T update(T update) {
		T t = get(update.getKey());
		list.remove(t);
		list.add(update);
		save();
		return update;
	}

	@Override
	public void delete(T delete) {
		T t = get(delete.getKey());
		list.remove(t);
		save();
	}

	@Override
	public boolean contains(K key) {
		for (T t : list) {
			if (t.getKey().equals(key)) {
				return true;
			}
		}
		return false;
	}

}
