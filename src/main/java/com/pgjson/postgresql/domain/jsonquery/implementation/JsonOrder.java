package com.pgjson.postgresql.domain.jsonquery.implementation;

import com.pgjson.postgresql.domain.jsonquery.JsonQueryFragment;
import com.pgjson.postgresql.domain.jsonquery.model.ParametrizedValue;

import java.util.List;

/**
 * Implementation order by expression for json/jsonb column type.
 *
 * @author vedadzornic
 */
public class JsonOrder implements JsonQueryFragment {

	private JsonProperty property;
	private boolean asc = false;

	public JsonOrder(JsonProperty property) {
		this.property = property;
	}

	public JsonOrder desc() {
		this.asc = false;
		return this;
	}

	public JsonOrder asc() {
		this.asc = true;
		return this;
	}

	/**
	 * Creates ORDER BY sql fragment.
	 *
	 * @return ex: ORDER BY (property.toSqlString()) ASC/DESC
	 */
	@Override
	public String toSqlString() {
		StringBuilder result = new StringBuilder();
		result.append("ORDER BY ");
		result.append(property.toSqlString());
		result.append(asc ? " ASC" : " DESC");
		return result.toString();
	}

	@Override
	public List<ParametrizedValue> getParametrizedValues() {
		return property.getParametrizedValues();
	}
}
