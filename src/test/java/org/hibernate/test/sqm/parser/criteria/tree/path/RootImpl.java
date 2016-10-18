/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: Apache License, Version 2.0
 * See the LICENSE file in the root directory or visit http://www.apache.org/licenses/LICENSE-2.0
 */
package org.hibernate.test.sqm.parser.criteria.tree.path;

import org.hibernate.sqm.domain.SQMEntityType;
import org.hibernate.sqm.parser.criteria.spi.CriteriaVisitor;
import org.hibernate.sqm.parser.criteria.spi.path.RootImplementor;
import org.hibernate.sqm.query.expression.Expression;
import org.hibernate.sqm.query.select.AliasedExpressionContainer;
import org.hibernate.test.sqm.parser.criteria.tree.CriteriaBuilderImpl;
import org.hibernate.test.sqm.parser.criteria.tree.PathSource;

import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import java.io.Serializable;

/**
 * Hibernate implementation of the JPA {@link Root} contract
 *
 * @author Steve Ebersole
 */
	public class RootImpl<X> extends AbstractFromImpl<X,X> implements RootImplementor<X>, Serializable {
	private final SQMEntityType<X> entityType;
	private final boolean allowJoins;

	public RootImpl(CriteriaBuilderImpl criteriaBuilder, EntityType<X> entityType) {
		this( criteriaBuilder, (SQMEntityType) entityType, true );
	}

	public RootImpl(CriteriaBuilderImpl criteriaBuilder, SQMEntityType entityType, boolean allowJoins) {
//		super( criteriaBuilder, entityType.getJavaType() );
		super( criteriaBuilder, null );
		this.entityType = entityType;
		this.allowJoins = allowJoins;
	}

	@Override
	public SQMEntityType getEntityType() {
		return entityType;
	}

	@Override
	public javax.persistence.metamodel.EntityType<X> getModel() {
//		throw new UnsupportedOperationException(  );
		return (EntityType) getMappedEntityType();
	}

	@Override
	protected FromImplementor<X, X> createCorrelationDelegate() {
		return new RootImpl<X>( criteriaBuilder(), getEntityType() );
	}

//	@Override
//	public RootImpl<X> correlateTo(CriteriaSubqueryImpl subquery) {
//		return (RootImpl<X>) super.correlateTo( subquery );
//	}

	@Override
	protected boolean canBeJoinSource() {
		return allowJoins;
	}

	@Override
	@SuppressWarnings("ThrowableResultOfMethodCallIgnored")
	protected RuntimeException illegalJoin() {
		return allowJoins ? super.illegalJoin() : new IllegalArgumentException( "UPDATE/DELETE criteria queries cannot define joins" );
	}

	@Override
	@SuppressWarnings("ThrowableResultOfMethodCallIgnored")
	protected RuntimeException illegalFetch() {
		return allowJoins ? super.illegalFetch() : new IllegalArgumentException( "UPDATE/DELETE criteria queries cannot define fetches" );
	}

	@Override
	public String getPathIdentifier() {
		return getAlias();
	}

	@Override
	public <T extends X> RootImpl<T> treatAs(Class<T> treatAsType) {
		return new TreatedRoot<T>( this, treatAsType );
	}

	@Override
	public Expression visitExpression(CriteriaVisitor visitor) {
		return visitor.visitRoot( this );
	}

	@Override
	public void visitSelections(CriteriaVisitor visitor, AliasedExpressionContainer container) {
		container.add( visitExpression( visitor ), getAlias() );
	}

	public javax.persistence.metamodel.EntityType<X> getMappedEntityType() {
		return (javax.persistence.metamodel.EntityType<X>) entityType;
	}

	public static class TreatedRoot<T> extends RootImpl<T> {
		private final RootImpl<? super T> original;
		private final Class<T> treatAsType;

		public TreatedRoot(RootImpl<? super T> original, Class<T> treatAsType) {
			super(
					original.criteriaBuilder(),
					original.criteriaBuilder().consumerContext().getDomainMetamodel().resolveEntityType( treatAsType )
			);
			this.original = original;
			this.treatAsType = treatAsType;
		}

		@Override
		public String getAlias() {
			return original.getAlias();
		}

		protected String getTreatFragment() {
			return "treat(" + original.getAlias() + " as " + treatAsType.getName() + ")";
		}

		@Override
		public String getPathIdentifier() {
			return getTreatFragment();
		}

		@Override
		protected PathSource getPathSourceForSubPaths() {
			return this;
		}
	}

}
