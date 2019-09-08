package com.vzornic.pgjson.hibernate.criteria.attribute.path;

import com.vzornic.pgjson.hibernate.criteria.attribute.JSONRootImpl;
import com.vzornic.pgjson.hibernate.criteria.attribute.SingularJSONAttributeImpl;
import org.hibernate.query.criteria.internal.CriteriaBuilderImpl;
import org.hibernate.query.criteria.internal.PathSource;
import org.hibernate.query.criteria.internal.compile.RenderingContext;
import org.hibernate.query.criteria.internal.path.SingularAttributePath;

import javax.persistence.metamodel.IdentifiableType;
import javax.persistence.metamodel.ManagedType;

/**
 * AttributePath specifying postgress JSON fields. It allows to select a single field inside json data field.
 *
 * @param <X>
 */
public class SingularJSONAttributePath<X>  extends SingularAttributePath<X> {
	public SingularJSONAttributePath(CriteriaBuilderImpl criteriaBuilder, Class<X> javaType, PathSource pathSource, SingularJSONAttributeImpl<?, X> attribute) {
		super(criteriaBuilder, javaType, pathSource, attribute);
	}

	private ManagedType<X> resolveManagedType(SingularJSONAttributeImpl<?, X> attribute) {
		return (IdentifiableType<X>) attribute.getType();
	}

	@Override
	public void prepareAlias(RenderingContext renderingContext) {
		super.prepareAlias(renderingContext);
	}

	@Override
	public String render(RenderingContext renderingContext) {
		SingularJSONAttributeImpl attribute = (SingularJSONAttributeImpl) getAttribute();
		JSONRootImpl<?> jsonRoot = (JSONRootImpl<?>) getPathSource();
		PathSource<?> source = jsonRoot.getOriginal();
		if ( source != null ) {
			source.prepareAlias( renderingContext );
			return  attribute.getFullPath(source.getPathIdentifier());
		}
		else {
			return attribute.getFullPath();
		}
	}

	@Override
	public String renderProjection(RenderingContext renderingContext) {
		return super.renderProjection(renderingContext);
	}

}
