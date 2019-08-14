package com.vzornic.pgjson.postgresql.domain.jsonquery.model;

/**
 * @author vedadzornic
 */
public enum CastType {
	NUMERIC("numeric"),
	BOOLEAN("boolean"),
	INTEGER("int"),
	BIGINT("bigint"),
	DOUBLE_PRECISION("double precision"),
	SMALLINT("smallint");

	private String type;

	CastType(String type) {
		this.type = type;
	}

	public String getCastType() {
		return type;
	}
}
