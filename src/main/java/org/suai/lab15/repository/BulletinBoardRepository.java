package org.suai.lab15.repository;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class BulletinBoardRepository {
	private final List<Map.Entry<String, String>> bulletinBoard = new CopyOnWriteArrayList<>();

	public void add(String name, String text) {
		bulletinBoard.add(new AbstractMap.SimpleImmutableEntry<>(name, text));
	}

	public List<Map.Entry<String, String>> get() {
		return bulletinBoard;
	}
}