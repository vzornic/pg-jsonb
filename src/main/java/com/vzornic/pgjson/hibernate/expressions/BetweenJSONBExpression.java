package com.vzornic.pgjson.hibernate.expressions;

import com.vzornic.pgjson.postgresql.domain.jsonquery.implementation.conditions.BetweenJsonCondition;
import com.vzornic.pgjson.postgresql.domain.jsonquery.model.CastType;
import com.vzornic.pgjson.postgresql.domain.jsonquery.model.ParametrizedValue;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.engine.spi.TypedValue;

/**
 * @author vedadzornic
 */
public class BetweenJSONBExpression extends BasicJSONBExpression {
	private ParametrizedValue low;
	private ParametrizedValue high;
	private boolean not = false;


	public BetweenJSONBExpression(String property, String jsonPath, Object low, Object high) {
		super(property, jsonPath);
		this.low = new ParametrizedValue(low);
		this.high = new ParametrizedValue(high);
	}

	public BetweenJSONBExpression cast(CastType castType) {
		this.castType = castType;
		low.applyCast(castType);
		high.applyCast(castType);
		return this;
	}

	public BetweenJSONBExpression not() {
		this.not = true;
		return this;
	}

	@Override
	public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
		final String[] columns = criteriaQuery.findColumns(property, criteria);
		if (columns.length == 1) {
			if (not) {
				return new BetweenJsonCondition(buildProperty(columns[0]), low, high)
						.ignoreValues()
						.not()
						.toSqlString();
			} else {
				return new BetweenJsonCondition(buildProperty(columns[0]), low, high)
						.ignoreValues()
						.toSqlString();
			}
		} else {
			throw new UnsupportedOperationException("Json expressions must be used on single column properties");
		}
	}

	@Override
	public TypedValue[] getTypedValues(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
		return new TypedValue[] {low.getTypedValue(), high.getTypedValue()};
	}
}
