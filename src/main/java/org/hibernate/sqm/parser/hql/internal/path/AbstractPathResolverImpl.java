/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: Apache License, Version 2.0
 * See the LICENSE file in the root directory or visit http://www.apache.org/licenses/LICENSE-2.0
 */
package org.hibernate.sqm.parser.hql.internal.path;

import java.util.Locale;

import org.hibernate.sqm.domain.Attribute;
import org.hibernate.sqm.domain.Bindable;
import org.hibernate.sqm.domain.SQMEntityType;
import org.hibernate.sqm.domain.IdentifierDescriptor;
import org.hibernate.sqm.domain.IdentifierDescriptorSingleAttribute;
import org.hibernate.sqm.domain.ManagedType;
import org.hibernate.sqm.domain.PluralAttribute;
import org.hibernate.sqm.domain.SingularAttribute;
import org.hibernate.sqm.parser.QueryException;
import org.hibernate.sqm.parser.SemanticException;
import org.hibernate.sqm.parser.common.ResolutionContext;
import org.hibernate.sqm.path.AttributeBindingSource;
import org.hibernate.sqm.query.JoinType;
import org.hibernate.sqm.query.from.FromElement;
import org.hibernate.sqm.query.from.QualifiedAttributeJoinFromElement;

/**
 * Template support for PathResolver implementations
 *
 * @author Steve Ebersole
 */
public abstract class AbstractPathResolverImpl implements PathResolver {
	private final ResolutionContext context;

	public AbstractPathResolverImpl(ResolutionContext context) {
		this.context = context;
	}

	protected ResolutionContext context() {
		return context;
	}

	protected AttributeBindingSource resolveAnyIntermediateAttributePathJoins(
			AttributeBindingSource lhs,
			String[] pathParts) {
		// build joins for any intermediate path parts
		for ( int i = 0, max = pathParts.length-1; i < max; i++ ) {
			lhs = buildIntermediateAttributeJoin( lhs, pathParts[i] );
		}
		return lhs;
	}

	protected AttributeBindingSource buildIntermediateAttributeJoin(
			AttributeBindingSource lhs,
			String pathPart) {
		final Attribute joinedAttributeDescriptor = resolveAttributeDescriptor( lhs, pathPart );
		validateIntermediateAttributeJoin( lhs, joinedAttributeDescriptor );

		return buildAttributeJoin( lhs.getFromElement(), joinedAttributeDescriptor, null );
	}

	protected QualifiedAttributeJoinFromElement buildAttributeJoin(
			FromElement lhsFromElement,
			Attribute joinedAttributeDescriptor,
			SQMEntityType subclassIndicator) {
		return context().getFromElementBuilder().buildAttributeJoin(
				lhsFromElement.getContainingSpace(),
				null,
				joinedAttributeDescriptor,
				subclassIndicator,
				lhsFromElement.asLoggableText() + '.' + joinedAttributeDescriptor.getName(),
				getIntermediateJoinType(),
				lhsFromElement,
				areIntermediateJoinsFetched()
		);
	}

	protected void validateIntermediateAttributeJoin(AttributeBindingSource lhs, Attribute joinedAttributeDescriptor) {
		if ( !SingularAttribute.class.isInstance( joinedAttributeDescriptor ) ) {
			throw new SemanticException(
					String.format(
							Locale.ROOT,
							"Attribute [%s -> %s] is plural, cannot be used in path expression",
							lhs.asLoggableText(),
							joinedAttributeDescriptor.getName()
					)
			);
		}
		else {
			// make sure it is Bindable
			final SingularAttribute singularAttribute = (SingularAttribute) joinedAttributeDescriptor;
			if ( singularAttribute.getAttributeTypeClassification() == SingularAttribute.Classification.BASIC ) {
				throw new SemanticException(
						String.format(
								Locale.ROOT,
								"Basic SingularAttribute [%s -> %s] cannot be used in path expression",
								lhs.asLoggableText(),
								joinedAttributeDescriptor.getName()
						)
				);
			}
		}
	}

	protected JoinType getIntermediateJoinType() {
		return JoinType.LEFT;
	}

	protected boolean areIntermediateJoinsFetched() {
		return false;
	}

	protected Attribute resolveAttributeDescriptor(AttributeBindingSource lhs, String attributeName) {
		final ManagedType managedType = resolveManagedType( lhs.getBoundModelType(), lhs.asLoggableText() );
		final Attribute attributeDescriptor = managedType.findAttribute( attributeName );
		if ( attributeDescriptor != null ) {
			return attributeDescriptor;
		}

		if ( managedType instanceof SQMEntityType ) {
			final SQMEntityType entityType = (SQMEntityType) managedType;
			final IdentifierDescriptor entityIdDescriptor = entityType.getIdentifierDescriptor();
			final String referableIdAttributeName = entityIdDescriptor.getReferableAttributeName();

			if ( "id".equals( attributeName )
					|| ( referableIdAttributeName != null && referableIdAttributeName.equals( attributeName ) ) ) {
				if ( entityIdDescriptor instanceof IdentifierDescriptorSingleAttribute ) {
					return ( (IdentifierDescriptorSingleAttribute) entityIdDescriptor ).getIdAttribute();
				}
				else {
					return new PseudoIdAttributeImpl( entityType );
				}
			}
		}

		throw new SemanticException(
				"Name [" + attributeName + "] is not a valid attribute from type [" +
						managedType + " (" + lhs.asLoggableText() + ")]"
		);
	}

	protected ManagedType resolveManagedType(Bindable bindable, String path) {
		if ( bindable instanceof ManagedType ) {
			return (ManagedType) bindable;
		}
		else if ( bindable instanceof SingularAttribute ) {
			final SingularAttribute singularAttribute = (SingularAttribute) bindable;
			if ( !ManagedType.class.isInstance( singularAttribute.getBoundType() ) ) {
				throw new SemanticException(
						"Expecting a ManagedType reference, but referenced SingularAttribute [" + path + "] type was not ManagedType : "
								+ singularAttribute.getBoundType()
				);
			}
			return (ManagedType) singularAttribute.getBoundType();
		}
		else if ( bindable instanceof PluralAttribute ) {
			final PluralAttribute pluralAttribute = (PluralAttribute) bindable;
			if ( !ManagedType.class.isInstance( pluralAttribute.getBoundType() ) ) {
				throw new SemanticException(
						"Expecting a ManagedType reference, but referenced SingularAttribute type was not ManagedType : "
								+ pluralAttribute.getBoundType()
				);
			}
			return (ManagedType) pluralAttribute.getBoundType();
		}
		else {
			throw new QueryException( "Could not interpret Bindable; unexpected type : " + bindable );
		}

	}
}
