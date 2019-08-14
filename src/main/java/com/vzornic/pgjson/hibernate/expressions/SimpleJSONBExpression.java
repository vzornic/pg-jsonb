package com.vzornic.pgjson.hibernate.expressions;

import com.vzornic.pgjson.postgresql.domain.jsonquery.implementation.conditions.SimpleJsonCondition;
import com.vzornic.pgjson.postgresql.domain.jsonquery.model.CastType;
import com.vzornic.pgjson.postgresql.domain.jsonquery.model.ParametrizedValue;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.engine.spi.TypedValue;

/**
 * Expressions used to create basic comparisons in postgresql using jsonb column.
	 * Operations supported with simple expressions are : ( =, like, ilike, >, <, >=, <= )
 *
 * This class allows options to cast column value via {@link CastType}..
 * Also allows to ignore case.
 *
 * @author vedadzornic
 */
public class SimpleJSONBExpression extends BasicJSONBExpression {
	private final String operator;
	private final ParametrizedValue value;
	private boolean ignoreCase = false;

	public SimpleJSONBExpression(String property, String jsonPath, Object value, String operator) {
		super(property, jsonPath);
		this.value = new ParametrizedValue(value);
		this.operator = operator;
	}

	public SimpleJSONBExpression ignoreCase() {
		ignoreCase = true;
		this.value.lowerCase();
		return this;
	}

	public SimpleJSONBExpression cast(CastType castType) {
		this.castType = castType;
		value.applyCast(castType);
		return this;
	}

	@Override
	public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
		final String[] columns = criteriaQuery.findColumns(property, criteria);
		if (columns.length == 1) {
			SimpleJsonCondition condition = new SimpleJsonCondition(buildProperty(columns[0]), value, operator)
					.ignoreValues();
			if (ignoreCase) {
				condition.ignoreCase();
			}
			return condition.toSqlString();
		}
		throw new UnsupportedOperationException("Json expressions must be used on single column properties");
	}

	@Override
	public TypedValue[] getTypedValues(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
		return new TypedValue[] { value.getTypedValue()};
	}

}
