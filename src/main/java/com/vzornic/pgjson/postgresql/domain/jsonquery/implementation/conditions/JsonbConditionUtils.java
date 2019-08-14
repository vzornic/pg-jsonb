package com.vzornic.pgjson.postgresql.domain.jsonquery.implementation.conditions;

import com.vzornic.pgjson.postgresql.domain.jsonquery.model.ParametrizedValue;

/**
 * Utility methods for jsonb conditions
 *
 * @author vedadzornic
 */
public class JsonbConditionUtils {

	/**
	 * Method creates sql string for {@link ParametrizedValue}.
	 * In case ignoreValues is true, method will replace value with key(':key') or with '?' in case key is null.
	 *
	 * @param value
	 * @param ignoreValues
	 * @return
	 */
	public static String createValue(ParametrizedValue value, boolean ignoreValues) {
		StringBuilder result = new StringBuilder();
		if (!ignoreValues) {
			result.append(value.getValue());
		} else {
			if (value.getKey() != null) {
				result.append(":").append(value.getKey());
			} else {
				result.append("?");
			}
		}
		return result.toString();
	}
}
