/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: Apache License, Version 2.0
 * See the LICENSE file in the root directory or visit http://www.apache.org/licenses/LICENSE-2.0
 */
package org.hibernate.sqm.parser.hql.internal.path;

import org.hibernate.sqm.domain.AnyType;
import org.hibernate.sqm.domain.Attribute;
import org.hibernate.sqm.domain.EmbeddableType;
import org.hibernate.sqm.domain.EntityType;
import org.hibernate.sqm.domain.IdentifiableType;
import org.hibernate.sqm.domain.ManagedType;
import org.hibernate.sqm.domain.PluralAttribute;
import org.hibernate.sqm.domain.SingularAttribute;
import org.hibernate.sqm.domain.Type;
import org.hibernate.sqm.parser.common.ResolutionContext;
import org.hibernate.sqm.path.AttributeBinding;
import org.hibernate.sqm.path.Binding;
import org.hibernate.sqm.query.expression.AttributeReferenceSqmExpression;
import org.hibernate.sqm.query.from.Downcast;
import org.hibernate.sqm.query.from.SqmFrom;
import org.hibernate.sqm.query.from.SqmAttributeJoin;

import org.jboss.logging.Logger;

/**
 * @author Steve Ebersole
 */
public class PathResolverBasicImpl extends AbstractPathResolverImpl {
	private static final Logger log = Logger.getLogger( PathResolverBasicImpl.class );

	public PathResolverBasicImpl(ResolutionContext context) {
		super( context );
	}

	protected boolean shouldRenderTerminalAttributeBindingAsJoin() {
		return false;
	}

	@Override
	public boolean canReuseImplicitJoins() {
		return true;
	}

	@Override
	public Binding resolvePath(String... pathParts) {
		return resolvePath( (EntityType) null, pathParts );
	}

	@Override
	public Binding resolvePath(Binding lhs, String... pathParts) {
		return resolvePath( lhs, null, pathParts );
	}

	private String[] sansFirstElement(String[] pathParts) {
		assert pathParts.length > 1;

		final String[] result = new String[pathParts.length - 1];
		System.arraycopy( pathParts, 1, result, 0, result.length );
		return result;
	}

	@Override
	public Binding resolvePath(EntityType subclassIndicator, String... pathParts) {
		assert pathParts.length > 0;

		// The given pathParts indicate either:
		//		* a dot-identifier sequence whose root could either be
		//			* an identification variable
		//			* an attribute name exposed from a FromElement
		//		* a single identifier which could represent:
		//			*  an identification variable
		//			* an attribute name exposed from a FromElement

		if ( pathParts.length > 1 ) {
			// we had a dot-identifier sequence...

			// see if the root is an identification variable
			final SqmFrom identifiedFromElement = context().getFromElementLocator()
					.findFromElementByIdentificationVariable( pathParts[0] );
			if ( identifiedFromElement != null ) {
				validatePathRoot( identifiedFromElement );
				return resolvePath( identifiedFromElement, subclassIndicator, sansFirstElement( pathParts ) );
			}

			// otherwise see if the root might be the name of an attribute exposed from a FromElement
			final SqmFrom root = context().getFromElementLocator().findFromElementExposingAttribute( pathParts[0] );
			if ( root != null ) {
				validatePathRoot( root );
				return resolvePath( root, subclassIndicator, pathParts );
			}
		}
		else {
			// we had a single identifier...

			// see if the identifier is an identification variable
			final SqmFrom identifiedFromElement = context().getFromElementLocator()
					.findFromElementByIdentificationVariable( pathParts[0] );
			if ( identifiedFromElement != null ) {
				return resolveFromElementAliasAsTerminal( identifiedFromElement );
			}

			// otherwise see if the identifier might be the name of an attribute exposed from a FromElement
			final SqmFrom root = context().getFromElementLocator().findFromElementExposingAttribute( pathParts[0] );
			if ( root != null ) {
				// todo : consider passing along subclassIndicator
				return resolveTerminalAttributeBinding( root, pathParts[0] );
			}
		}

		return null;
	}

	protected void validatePathRoot(SqmFrom root) {
	}

	@Override
	public Binding resolvePath(
			Binding lhs,
			EntityType subclassIndicator,
			String... pathParts) {
		final Binding terminalLhs = resolveAnyIntermediateAttributePathJoins(
				lhs,
				pathParts
		);
		return resolveTerminalAttributeBinding( terminalLhs, pathParts[pathParts.length-1] );
	}

	protected AttributeBinding resolveTerminalAttributeBinding(
			Binding lhs,
			String terminalName) {
		final Attribute attribute = resolveAttributeDescriptor( lhs, terminalName );
		if ( shouldRenderTerminalAttributeBindingAsJoin() && isJoinable( attribute ) ) {
			log.debugf(
					"Resolved terminal attribute-binding [%s.%s ->%s] as attribute-join",
					lhs.asLoggableText(),
					terminalName,
					attribute
			);
			return buildAttributeJoin(
					// see note in #resolveTreatedTerminal regarding cast
					(SqmFrom) lhs,
					attribute,
					null
			);
		}
		else {
			log.debugf(
					"Resolved terminal attribute-binding [%s.%s ->%s] as attribute-reference",
					lhs.asLoggableText(),
					terminalName,
					attribute
			);
			final AttributeReferenceSqmExpression attributeReference = new AttributeReferenceSqmExpression( lhs, attribute, null );
			context().getParsingContext().registerAttributeReference( attributeReference );
			return attributeReference;
		}
	}

	private boolean isJoinable(Attribute attribute) {
		if ( attribute instanceof PluralAttribute ) {
			return true;
		}
		else {
			final Type singularAttributeType = ( (SingularAttribute) attribute ).getType();
			return singularAttributeType instanceof ManagedType
					|| singularAttributeType instanceof AnyType;
		}
	}

	protected Binding resolveFromElementAliasAsTerminal(SqmFrom aliasedFromElement) {
		log.debugf( "Resolved terminal as from-element alias : %s", aliasedFromElement.getIdentificationVariable() );
		return aliasedFromElement;
	}

	protected Binding resolveTreatedTerminal(
			ResolutionContext context,
			Binding lhs,
			String terminalName,
			EntityType subclassIndicator) {
		final Attribute joinedAttribute = resolveAttributeDescriptor( lhs, terminalName );
		log.debugf( "Resolved terminal treated-path : %s -> %s", joinedAttribute, subclassIndicator );
		final SqmAttributeJoin join = buildAttributeJoin(
				// todo : just do a cast for now, but this needs to be thought out (Binding -> SqmFrom)
				//		^^ SqmFrom specifically needed mainly needed for "FromElementSpace"
				//		but perhaps that resolution could be delayed
				(SqmFrom) lhs,
				joinedAttribute,
				subclassIndicator
		);

		join.addDowncast( new Downcast( subclassIndicator ) );

		return new TreatedFromElementBinding( join, subclassIndicator );
	}
}
