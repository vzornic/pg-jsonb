package com.vzornic.pgjson.postgresql.domain.jsonquery;

import com.vzornic.pgjson.postgresql.domain.jsonquery.model.ParametrizedValue;

import java.util.List;

/**
 * This is interface that all json fragments implement.
 * Has method which retrieves sql string and method for retrieving parameters.
 *
 * @author vedadzornic
 */
public interface JsonQueryFragment {

	/**
	 * Method creates string sql fragment.
	 *
	 * @return sql string
	 */
	String toSqlString();

	/**
	 * Returns list of parametrized values.
	 *
	 * @return list of parametrized values
	 */
	List<ParametrizedValue> getParametrizedValues();

}
