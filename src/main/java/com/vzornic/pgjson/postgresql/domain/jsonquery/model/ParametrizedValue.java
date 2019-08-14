package com.vzornic.pgjson.postgresql.domain.jsonquery.model;

import com.sun.istack.internal.Nullable;
import org.hibernate.engine.spi.TypedValue;
import org.hibernate.type.BooleanType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.FloatType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.ShortType;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;

/**
 * Class used to set parameters in sql query.
 * If key is null, value should replace '?' inside query fragment.
 *
 * @author vedadzornic
 */
public class ParametrizedValue {
	/**
	 * Key for parameter. Might be null
	 */
	@Nullable
	private String key;

	/**
	 * Value
	 */
	private Object value;
	private CastType castType;
	private boolean lowerCase;


	public ParametrizedValue(String key, Object value) {
		this.key = key;
		this.value = value;
	}

	public ParametrizedValue(Object value) {
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public Object getValue() {
		return value;
	}

	public ParametrizedValue lowerCase() {
		if (value instanceof String) {
			this.value = this.value.toString().toLowerCase();
		}
		return this;
	}

	public ParametrizedValue applyCast(CastType castType) {
		switch (castType) {
			case BIGINT:
				value = Long.parseLong(value.toString());
				break;
			case BOOLEAN:
				value = Boolean.parseBoolean(value.toString());
				break;
			case DOUBLE_PRECISION:
				value = Double.parseDouble(value.toString());
				break;
			case INTEGER:
				value = Integer.parseInt(value.toString());
				break;
			case NUMERIC:
				value = Float.parseFloat(value.toString());
				break;
			case SMALLINT:
				value = Short.parseShort(value.toString());
				break;
		}
		this.castType = castType;
		return this;
	}

	public TypedValue getTypedValue() {
		return new TypedValue(getType(), value);
	}

	private Type getType() {
		if (castType != null) {
			switch (castType) {
				case BIGINT:
					return new LongType();
				case BOOLEAN:
					return new BooleanType();
				case DOUBLE_PRECISION:
					return new DoubleType();
				case INTEGER:
					return new IntegerType();
				case NUMERIC:
					return new FloatType();
				case SMALLINT:
					return new ShortType();
			}
		}
		return new StringType();
	}

}
