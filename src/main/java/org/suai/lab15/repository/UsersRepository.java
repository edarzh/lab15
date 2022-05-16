package org.suai.lab15.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UsersRepository {
	private final Map<String, String> users = new ConcurrentHashMap<>();

	public void add(String name, String password) {
		users.put(name, password);
	}

	public boolean verify(String name, String password) {
		String value = users.get(name);
		if (value != null) {
			return value.equals(password);
		}
		return false;
	}

	public Map<String, String> get() {
		return users;
	}
}