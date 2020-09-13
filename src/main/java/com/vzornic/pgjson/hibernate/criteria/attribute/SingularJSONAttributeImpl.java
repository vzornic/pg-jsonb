package com.vzornic.pgjson.hibernate.criteria.attribute;

import com.vzornic.pgjson.postgresql.domain.jsonquery.implementation.JsonProperty;
import com.vzornic.pgjson.postgresql.domain.jsonquery.model.CastType;
import org.hibernate.metamodel.model.domain.internal.AbstractAttribute;
import org.hibernate.metamodel.model.domain.internal.AbstractManagedType;
import org.hibernate.metamodel.model.domain.internal.BasicTypeImpl;
import org.hibernate.metamodel.model.domain.spi.SimpleTypeDescriptor;

import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.Type;
import java.io.Serializable;

/**
 * Singular json attribute implementation. Requires jsonPath and class type to work properly.
 * The following types are supported: String, Integer, Long, Float, Double, Short, Boolean
 *
 * @param <X>
 * @param <Y>
 */
public class SingularJSONAttributeImpl<X, Y> extends AbstractAttribute<X,Y> implements SingularAttribute<X, Y>, Serializable {
	private final boolean isOptional;
	private final Type<Y> attributeType;
	private final JsonProperty jsonPath;
	private final Class<Y> javaType;

	public SingularJSONAttributeImpl(Attribute<X,Y> attribute, String jsonPath, Class<Y> type) {
		super((AbstractManagedType) attribute.getDeclaringType(),attribute.getName(),
				attribute.getPersistentAttributeType(),
				new BasicTypeImpl<Y>(type, Type.PersistenceType.BASIC), attribute.getJavaMember());
		this.isOptional = true;
		this.attributeType = new BasicTypeImpl<Y>(type, Type.PersistenceType.BASIC);
		this.jsonPath = new JsonProperty(getName(), jsonPath).hql();
		this.javaType = type;

		if (!String.class.isAssignableFrom(getJavaType())) {
			if (Float.class.isAssignableFrom(getJavaType())) {
				this.jsonPath.cast(CastType.NUMERIC);
			} else if (Double.class.isAssignableFrom(getJavaType())) {
				this.jsonPath.cast(CastType.DOUBLE_PRECISION);
			} else if (Long.class.isAssignableFrom(getJavaType())) {
				this.jsonPath.cast(CastType.BIGINT);
			} else if (Integer.class.isAssignableFrom(getJavaType())) {
				this.jsonPath.cast(CastType.INTEGER);
			} else if (Boolean.class.isAssignableFrom(getJavaType())) {
				this.jsonPath.cast(CastType.BOOLEAN);
			} else if (Short.class.isAssignableFrom(getJavaType())) {
				this.jsonPath.cast(CastType.SMALLINT);
			} else {
				throw new RuntimeException();//TODO
			}
		}
	}

	/**
	 * JSON attribute cannot be identifier
	 *
	 * @return false
	 */
	@Override
	public boolean isId() {
		return false;
	}

	/**
	 * JSON attribute cannot be version
	 *
	 * @return false
	 */
	@Override
	public boolean isVersion() {
		return false;
	}

	@Override
	public boolean isOptional() {
		return isOptional;
	}

	@Override
	public Type<Y> getType() {
		return attributeType;
	}

	@Override
	public Class<Y> getJavaType() {
		return javaType;
	}

	/**
	 * JSON attribute cannot be association
	 *
	 * @return false
	 */
	@Override
	public boolean isAssociation() {
		return false;
	}

	@Override
	public boolean isCollection() {
		return false;
	}

	@Override
	public BindableType getBindableType() {
		return BindableType.SINGULAR_ATTRIBUTE;
	}

	@Override
	public Class<Y> getBindableJavaType() {
		return attributeType.getJavaType();
	}


	public String getFullPath() {
		return jsonPath.toSqlString();
	}

	public String getFullPath(String alias) {
		return jsonPath.alias(alias).toSqlString();
	}

	@Override
	public SimpleTypeDescriptor<?> getKeyGraphType() {
		return null;
	}
}