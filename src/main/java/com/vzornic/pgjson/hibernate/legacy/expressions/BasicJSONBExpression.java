package com.vzornic.pgjson.hibernate.legacy.expressions;

import com.vzornic.pgjson.postgresql.domain.jsonquery.implementation.JsonProperty;
import com.vzornic.pgjson.postgresql.domain.jsonquery.model.CastType;
import org.hibernate.criterion.Criterion;

/**
 * @author vedadzornic
 */
public abstract class BasicJSONBExpression implements Criterion {
	protected final String property;
	protected final String jsonPath;
	protected CastType castType;

	public BasicJSONBExpression(String property, String jsonPath) {
		this.property = property;
		this.jsonPath = jsonPath;
	}

	protected JsonProperty buildProperty(String property) {
		JsonProperty jsonProperty = new JsonProperty(property, jsonPath);
		if (castType != null) jsonProperty.cast(castType);
		return jsonProperty;
	}

	public abstract BasicJSONBExpression cast(CastType castType);
}
