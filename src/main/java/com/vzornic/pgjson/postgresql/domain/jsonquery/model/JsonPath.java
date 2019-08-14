package com.vzornic.pgjson.postgresql.domain.jsonquery.model;

/**
 * @author vedadzornic
 */
public class JsonPath {

	private String key;
	private PathType type;

	public JsonPath(String key, PathType type) {
		this.key = key;
		this.type = type;
	}

	public String getKey() {
		return key;
	}

	public PathType getType() {
		return type;
	}

	public enum PathType {
		/**
		 * Regular path type
		 */
		STRING,
		/**
		 * In case path is index of array
		 */
		NUMBER;
	}
}
