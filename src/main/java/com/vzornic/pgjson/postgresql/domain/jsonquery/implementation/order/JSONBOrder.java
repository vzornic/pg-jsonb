package com.vzornic.pgjson.postgresql.domain.jsonquery.implementation.order;

import com.vzornic.pgjson.postgresql.domain.jsonquery.implementation.JsonProperty;
import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Order;

/**
 * TODO finish order.
 * @author vedadzornic
 */
public class JSONBOrder extends Order {

	private String propertyName;
	private String jsonPath;
	private boolean ascending;


	protected JSONBOrder(String propertyName, boolean ascending) {
		super(propertyName, ascending);
		this.propertyName = propertyName;
		this.ascending = ascending;
	}

	public JSONBOrder(String propertyName, String jsonPath, boolean ascending) {
		super(propertyName, ascending);
		this.propertyName = propertyName;
		this.ascending = ascending;
		this.jsonPath = jsonPath;
	}

	@Override
	public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) {
		final String[] columns = criteriaQuery.getColumnsUsingProjection(criteria, this.propertyName);
		return new JsonProperty(columns[0], jsonPath).toSqlString();
	}
}
