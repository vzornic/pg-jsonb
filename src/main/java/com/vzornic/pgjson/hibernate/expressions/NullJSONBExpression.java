package com.vzornic.pgjson.hibernate.expressions;

import com.vzornic.pgjson.postgresql.domain.jsonquery.implementation.conditions.NullJsonCondition;
import com.vzornic.pgjson.postgresql.domain.jsonquery.model.CastType;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.engine.spi.TypedValue;

public class NullJSONBExpression extends BasicJSONBExpression {
	private final boolean isNull;

	public NullJSONBExpression(String property, String jsonPath, boolean isNull) {
		super(property, jsonPath);
		this.isNull = isNull;
	}


	public NullJSONBExpression cast(CastType castType) {
		this.castType = castType;
		return this;
	}

	@Override
	public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
		final String[] columns = criteriaQuery.findColumns(property, criteria);
		if (columns.length == 1) {
			NullJsonCondition condition = new NullJsonCondition(buildProperty(columns[0]), isNull);
			return condition.toSqlString();
		}
		throw new UnsupportedOperationException("Json expressions must be used on single column properties");
	}

	@Override
	public TypedValue[] getTypedValues(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
		return new TypedValue[] {};
	}

}