package com.pgjson.hibernate.restrictions;

import com.pgjson.hibernate.expressions.BetweenJSONBExpression;
import com.pgjson.hibernate.expressions.InJSONBExpression;
import com.pgjson.hibernate.expressions.NullJSONBExpression;
import com.pgjson.hibernate.expressions.SimpleJSONBExpression;
import com.pgjson.postgresql.domain.jsonquery.model.CastType;
import org.hibernate.criterion.MatchMode;

import java.util.List;

/**
 * Expressions factory class.
 *
 * @author vedadzornic
 */
public class JSONBRestrictions {

	public static SimpleJSONBExpression eq(String property, String jsonPath, String value) {
		return new SimpleJSONBExpression(property, jsonPath, value, "=");
	}

	public static SimpleJSONBExpression iEq(String property, String jsonPath, String value) {
		return new SimpleJSONBExpression(property, jsonPath, value, "=").ignoreCase();
	}

	public static SimpleJSONBExpression ne(String property, String jsonPath, String value) {
		return new SimpleJSONBExpression(property, jsonPath, value, "<>");
	}

	public static SimpleJSONBExpression iNe(String property, String jsonPath, String value) {
		return new SimpleJSONBExpression(property, jsonPath, value, "<>").ignoreCase();
	}

	public static SimpleJSONBExpression like(String property, String jsonPath, String value, MatchMode mode) {
		return new SimpleJSONBExpression(property, jsonPath, mode.toMatchString(value), " LIKE ");
	}

	public static SimpleJSONBExpression iLike(String property, String jsonPath, String value, MatchMode mode) {
		return new SimpleJSONBExpression(property, jsonPath, mode.toMatchString(value), " ILIKE ");
	}

	public static SimpleJSONBExpression gt(String property, String jsonPath, String value) {
		return new SimpleJSONBExpression(property, jsonPath,value, ">").cast(CastType.BIGINT);
	}

	public static SimpleJSONBExpression ge(String property, String jsonPath, String value) {
		return new SimpleJSONBExpression(property, jsonPath, value, ">=").cast(CastType.BIGINT);
	}

	public static SimpleJSONBExpression lt(String property, String jsonPath, String value) {
		return new SimpleJSONBExpression(property, jsonPath, value, "<").cast(CastType.BIGINT);
	}

	public static SimpleJSONBExpression le(String property, String jsonPath, String value) {
		return new SimpleJSONBExpression(property, jsonPath, value, "<=").cast(CastType.BIGINT);
	}

	public static BetweenJSONBExpression between(String property, String jsonPath, Object low, Object high) {
		return new BetweenJSONBExpression(property, jsonPath, low, high);
	}

	public static BetweenJSONBExpression between(String property, String jsonPath, Object low, Object high, CastType castType) {
		return new BetweenJSONBExpression(property, jsonPath, low, high).cast(castType);
	}

	public static BetweenJSONBExpression notBetween(String property, String jsonPath, Object low, Object high) {
		return new BetweenJSONBExpression(property, jsonPath, low, high).not();
	}

	public static BetweenJSONBExpression notBetween(String property, String jsonPath, Object low, Object high, CastType castType) {
		return new BetweenJSONBExpression(property, jsonPath, low, high).cast(castType).not();
	}

	public static InJSONBExpression in(String property, String jsonPath, List<Object> values) {
		return new InJSONBExpression(property, jsonPath, values);
	}

	public static InJSONBExpression in(String property, String jsonPath, List<Object> values, CastType castType) {
		return new InJSONBExpression(property, jsonPath, values).cast(castType);
	}

	public static InJSONBExpression notIn(String property, String jsonPath, List<Object> values) {
		return new InJSONBExpression(property, jsonPath, values).not();
	}

	public static InJSONBExpression notIn(String property, String jsonPath, List<Object> values, CastType castType) {
		return new InJSONBExpression(property, jsonPath, values).cast(castType).not();
	}

	public static NullJSONBExpression isNull(String property, String jsonPath) {
		return new NullJSONBExpression(property, jsonPath, true);
	}

	public static NullJSONBExpression isNotNull(String property, String jsonPath) {
		return new NullJSONBExpression(property, jsonPath, false);
	}

}
