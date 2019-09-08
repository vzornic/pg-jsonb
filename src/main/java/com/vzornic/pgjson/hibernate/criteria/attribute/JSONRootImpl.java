package com.vzornic.pgjson.hibernate.criteria.attribute;

import com.vzornic.pgjson.hibernate.criteria.attribute.path.SingularJSONAttributePath;
import org.hibernate.query.criteria.internal.path.PluralAttributePath;
import org.hibernate.query.criteria.internal.path.RootImpl;

import javax.persistence.criteria.Path;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.MapAttribute;
import javax.persistence.metamodel.PluralAttribute;
import java.util.Map;

/**
 * Wrapper for {@link RootImpl} to support json queries.
 *
 * @param <X>
 */
public class JSONRootImpl<X> extends RootImpl<X> {

	private RootImpl<X> original;

	public JSONRootImpl(RootImpl<X> original) {
		super(original.criteriaBuilder(), original.getEntityType());
		this.original = original;
	}

	public <Y> Path<Y> get(String attributeName, String jsonPath, Class<Y> type) {
		if ( ! canBeDereferenced() ) {
			throw illegalDereference();
		}

		final Attribute attribute = locateAttribute( attributeName );

		if ( attribute.isCollection() ) {
			//TODO plural
			final PluralAttribute<X,Y,?> pluralAttribute = (PluralAttribute<X,Y,?>) attribute;
			if ( PluralAttribute.CollectionType.MAP.equals( pluralAttribute.getCollectionType() ) ) {
				return (PluralAttributePath<Y>) this.<Object,Object, Map<Object, Object>>get( (MapAttribute) pluralAttribute );
			}
			else {
				return (PluralAttributePath<Y>) this.get( (PluralAttribute) pluralAttribute );
			}
		}
		else {
			SingularJSONAttributeImpl<X, Y> jsonAttribute = new SingularJSONAttributeImpl<X, Y>(attribute, jsonPath, type);

			return new SingularJSONAttributePath<Y>(
					criteriaBuilder(),
					type,
					getPathSourceForSubPaths(),
					jsonAttribute);
		}
	}

	public Path<String> get(String attributeName, String jsonPath) {
		return get(attributeName, jsonPath, String.class);
	}

	public RootImpl<X> getOriginal() {
		return original;
	}
}
