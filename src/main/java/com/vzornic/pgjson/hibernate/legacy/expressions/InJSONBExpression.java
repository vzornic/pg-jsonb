package com.vzornic.pgjson.hibernate.legacy.expressions;

import com.vzornic.pgjson.postgresql.domain.jsonquery.implementation.conditions.InJsonCondition;
import com.vzornic.pgjson.postgresql.domain.jsonquery.model.CastType;
import com.vzornic.pgjson.postgresql.domain.jsonquery.model.ParametrizedValue;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.engine.spi.TypedValue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author vedadzornic
 */
public class InJSONBExpression extends BasicJSONBExpression {

	protected List<ParametrizedValue> values = new ArrayList<>();
	private boolean not = false;

	public InJSONBExpression(String property, String jsonPath, List<Object> values) {
		super(property, jsonPath);
		values.forEach(v -> this.values.add(new ParametrizedValue(v)));
	}

	@Override
	public InJSONBExpression cast(CastType castType) {
		this.castType = castType;
		values.forEach(v -> v.applyCast(castType));
		return this;
	}

	public InJSONBExpression not() {
		this.not = true;
		return this;
	}

	@Override
	public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
		final String[] columns = criteriaQuery.findColumns(property, criteria);
		if (columns.length == 1) {
			if (not) {
				return new InJsonCondition(buildProperty(columns[0]), values).ignoreValues().not().toSqlString();
			} else {
				return new InJsonCondition(buildProperty(columns[0]), values).ignoreValues().toSqlString();
			}
		}
		throw new UnsupportedOperationException("Json expressions must be used on single column properties");
	}

	@Override
	public TypedValue[] getTypedValues(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
		return values.stream()
				.map(v -> v.getTypedValue())
				.collect(Collectors.toList())
				.toArray(new TypedValue[values.size()]);
	}
}
