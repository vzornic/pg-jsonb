package com.pgjson.postgresql.domain.jsonquery.implementation.conditions;

import com.pgjson.postgresql.domain.jsonquery.implementation.JsonProperty;
import com.pgjson.postgresql.domain.jsonquery.JsonQueryFragment;
import com.pgjson.postgresql.domain.jsonquery.model.ParametrizedValue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Implementation for In and NOT IN conditions.
 *
 * @author vedadzornic
 */
public class InJsonCondition implements JsonQueryFragment {

	private JsonProperty jsonProperty;
	private List<ParametrizedValue> values = new ArrayList<>();
	private String operator = " IN ";
	private boolean lowerCase = false;
	private boolean ignoreValues = false;

	public InJsonCondition(JsonProperty jsonProperty, List<ParametrizedValue> values) {
		this.jsonProperty = jsonProperty;
		this.values.addAll(values);
	}

	public InJsonCondition ignoreValues() {
		this.ignoreValues = true;
		return this;
	}

	@Override
	public String toSqlString() {
		StringBuilder result = new StringBuilder();
		if (lowerCase) {
			result.append("lower(");
		}
		result.append(jsonProperty.toSqlString());

		if (lowerCase) {
			result.append(")");
		}
		result.append(operator);

		result.append(" ( ");

		Iterator<ParametrizedValue> iterator = values.iterator();
		while (iterator.hasNext()) {
			ParametrizedValue value = iterator.next();
			result.append(JsonbConditionUtils.createValue(value, ignoreValues));
			if (iterator.hasNext()) {
				result.append(",");
			}
		}
		result.append(" ) ");
		return result.toString();
	}

	public InJsonCondition not() {
		operator = " NOT IN ";
		return this;
	}

	@Override
	public List<ParametrizedValue> getParametrizedValues() {
		List<ParametrizedValue> result = new ArrayList<>();
		result.addAll(jsonProperty.getParametrizedValues());
		result.addAll(values);
		return result;
	}
}
