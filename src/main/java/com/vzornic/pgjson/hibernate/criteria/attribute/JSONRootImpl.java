package com.vzornic.pgjson.hibernate.criteria.attribute;

import com.vzornic.pgjson.hibernate.criteria.attribute.path.SingularJSONAttributePath;
import org.hibernate.query.criteria.internal.path.RootImpl;

import javax.persistence.criteria.Path;
import javax.persistence.metamodel.Attribute;

/**
 * Wrapper for {@link RootImpl} to support json queries.
 *
 * @param <X>
 */
public class JSONRootImpl<X> extends RootImpl<X> {

	private RootImpl<X> original;
	private String attributeName; //TODO validation that attribute exist and is json type

	public JSONRootImpl(RootImpl<X> original, String attributeName) {
		super(original.criteriaBuilder(), original.getEntityType());
		this.original = original;
		this.attributeName = attributeName;
	}

	public <Y> Path<Y> get(String jsonPath, Class<Y> type) {
		if ( ! canBeDereferenced() ) {
			throw illegalDereference();
		}

		final Attribute attribute = locateAttribute( attributeName );

			SingularJSONAttributeImpl<X, Y> jsonAttribute = new SingularJSONAttributeImpl<X, Y>(attribute, jsonPath, type);

			return new SingularJSONAttributePath<Y>(
					criteriaBuilder(),
					type,
					getPathSourceForSubPaths(),
					jsonAttribute);

	}

	public Path<String> get(String jsonPath) {
		return get(jsonPath, String.class);
	}

	public RootImpl<X> getOriginal() {
		return original;
	}
}
