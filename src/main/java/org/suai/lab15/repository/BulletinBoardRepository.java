package org.suai.lab15.repository;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class BulletinBoardRepository {
	private final List<Map.Entry<String[], String>> bulletinBoard = new CopyOnWriteArrayList<>();

	public void add(String[] header, String text) {
		bulletinBoard.add(new AbstractMap.SimpleImmutableEntry<>(header, text));
	}

	public void remove(int index) {
		bulletinBoard.remove(index);
	}

	public List<Map.Entry<String[], String>> get() {
		return bulletinBoard;
	}
}