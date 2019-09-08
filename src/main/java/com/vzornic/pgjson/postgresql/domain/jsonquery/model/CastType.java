package com.vzornic.pgjson.postgresql.domain.jsonquery.model;

/**
 * @author vedadzornic
 */
public enum CastType {
	NUMERIC("numeric", Float.class),
	BOOLEAN("boolean", Boolean.class),
	INTEGER("int", Integer.class),
	BIGINT("bigint", Long.class),
	DOUBLE_PRECISION("double precision", Double.class),
	SMALLINT("smallint", Short.class);

	private String type;
	private Class<?> javaClass;

	CastType(String type, Class<?> javaClass) {
		this.type = type;
		this.javaClass = javaClass;
	}

	public Class<?> getJavaClass() {
		return javaClass;
	}

	public String getCastType() {
		return type;
	}
}
