package com.gmail.juanbarbosa.custom;

import java.util.HashMap;

public class ThreadContext {
	public static final String DOMAIN = "domain";
	public static final String COOKIE_TEST = "cookie_test";
	
	private static final ThreadLocal<HashMap<String, Object>> threadContext = ThreadLocal.withInitial(HashMap::new);

	public static <T> void set(String key, T value) {
		threadContext.get().put(key, value);
	}

	public static Object get(String key) {
		return threadContext.get().get(key);
	}

	public static void clear(String key) {
		threadContext.get().remove(key);
	}
}
