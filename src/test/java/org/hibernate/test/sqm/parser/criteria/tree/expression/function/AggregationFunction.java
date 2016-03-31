/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.test.sqm.parser.criteria.tree.expression.function;

import org.hibernate.sqm.parser.criteria.spi.CriteriaVisitor;
import org.hibernate.test.sqm.parser.criteria.tree.CriteriaBuilderImpl;
import org.hibernate.test.sqm.parser.criteria.tree.expression.LiteralExpression;

import javax.persistence.criteria.Expression;
import java.io.Serializable;

/**
 * Models SQL aggregation functions (<tt>MIN</tt>, <tt>MAX</tt>, <tt>COUNT</tt>, etc).
 *
 * @author Steve Ebersole
 */
public abstract class AggregationFunction<T>
		extends ParameterizedFunctionExpression<T>
		implements Serializable {

	/**
	 * Constructs an aggregation function with a single literal argument.
	 *
	 * @param criteriaBuilder The query builder instance.
	 * @param returnType The function return type.
	 * @param functionName The name of the function.
	 * @param argument The literal argument
	 */
	@SuppressWarnings({ "unchecked" })
	public AggregationFunction(
			CriteriaBuilderImpl criteriaBuilder,
			Class<T> returnType,
			String functionName,
			Object argument) {
		this( criteriaBuilder, returnType, functionName, new LiteralExpression( criteriaBuilder, argument ) );
	}

	/**
	 * Constructs an aggregation function with a single literal argument.
	 *
	 * @param criteriaBuilder The query builder instance.
	 * @param returnType The function return type.
	 * @param functionName The name of the function.
	 * @param argument The argument
	 */
	public AggregationFunction(
			CriteriaBuilderImpl criteriaBuilder,
			Class<T> returnType,
			String functionName,
			Expression<?> argument) {
		super( criteriaBuilder, returnType, functionName, argument );
	}

	@Override
	public boolean isAggregation() {
		return true;
	}

	@Override
	protected boolean isStandardJpaFunction() {
		return true;
	}

	/**
	 * Implementation of a <tt>COUNT</tt> function providing convenience in construction.
	 * <p/>
	 * Parameterized as {@link Long} because thats what JPA states
	 * that the return from <tt>COUNT</tt> should be.
	 */
	public static class COUNT extends AggregationFunction<Long> {
		public static final String COUNT_NAME = "count";

		private final boolean distinct;

		public COUNT(CriteriaBuilderImpl criteriaBuilder, Expression<?> expression, boolean distinct) {
			super( criteriaBuilder, Long.class, COUNT_NAME , expression );
			this.distinct = distinct;
		}

		public boolean isDistinct() {
			return distinct;
		}

		@Override
		public org.hibernate.sqm.query.expression.Expression visitExpression(CriteriaVisitor visitor) {
			return visitor.visitFunction(
					COUNT_NAME,
					criteriaBuilder().consumerContext().getDomainMetamodel().getBasicType( getJavaType() ),
					getArgumentExpressions()
			);
		}
	}

	/**
     * Implementation of a <tt>AVG</tt> function providing convenience in construction.
     * <p/>
     * Parameterized as {@link Double} because thats what JPA states that the return from <tt>AVG</tt> should be.
	 */
	public static class AVG extends AggregationFunction<Double> {
		public static final String AVG_NAME = "avg";

		public AVG(CriteriaBuilderImpl criteriaBuilder, Expression<? extends Number> expression) {
			super( criteriaBuilder, Double.class, AVG_NAME, expression );
		}

		@Override
		public org.hibernate.sqm.query.expression.Expression visitExpression(CriteriaVisitor visitor) {
			return visitor.visitFunction(
					AVG_NAME,
					criteriaBuilder().consumerContext().getDomainMetamodel().getBasicType( getJavaType() ),
					getArgumentExpressions()
			);
		}
	}

	/**
	 * Implementation of a <tt>SUM</tt> function providing convenience in construction.
	 * <p/>
	 * Parameterized as {@link Number N extends Number} because thats what JPA states
	 * that the return from <tt>SUM</tt> should be.
	 */
	public static class SUM<N extends Number> extends AggregationFunction<N> {
		public static final String SUM_NAME = "sum";

		@SuppressWarnings({ "unchecked" })
		public SUM(CriteriaBuilderImpl criteriaBuilder, Expression<N> expression) {
			super( criteriaBuilder, (Class<N>)expression.getJavaType(), SUM_NAME, expression);
			// force the use of a ValueHandler
			resetJavaType( expression.getJavaType() );
		}

		public SUM(CriteriaBuilderImpl criteriaBuilder, Expression<? extends Number> expression, Class<N> returnType) {
			super( criteriaBuilder, returnType, SUM_NAME, expression);
			// force the use of a ValueHandler
			resetJavaType( returnType );
		}

		@Override
		public org.hibernate.sqm.query.expression.Expression visitExpression(CriteriaVisitor visitor) {
			return visitor.visitFunction(
					SUM_NAME,
					criteriaBuilder().consumerContext().getDomainMetamodel().getBasicType( getJavaType() ),
					getArgumentExpressions()
			);
		}
	}

	/**
	 * Implementation of a <tt>MIN</tt> function providing convenience in construction.
	 * <p/>
	 * Parameterized as {@link Number N extends Number} because thats what JPA states
	 * that the return from <tt>MIN</tt> should be.
	 */
	public static class MIN<N extends Number> extends AggregationFunction<N> {
		public static final String MIN_NAME = "min";

		@SuppressWarnings({ "unchecked" })
		public MIN(CriteriaBuilderImpl criteriaBuilder, Expression<N> expression) {
			super( criteriaBuilder, ( Class<N> ) expression.getJavaType(), MIN_NAME, expression);
		}

		@Override
		public org.hibernate.sqm.query.expression.Expression visitExpression(CriteriaVisitor visitor) {
			return visitor.visitFunction(
					MIN_NAME,
					criteriaBuilder().consumerContext().getDomainMetamodel().getBasicType( getJavaType() ),
					getArgumentExpressions()
			);
		}
	}

	/**
	 * Implementation of a <tt>MAX</tt> function providing convenience in construction.
	 * <p/>
	 * Parameterized as {@link Number N extends Number} because thats what JPA states
	 * that the return from <tt>MAX</tt> should be.
	 */
	public static class MAX<N extends Number> extends AggregationFunction<N> {
		public static final String MAX_NAME = "max";

		@SuppressWarnings({ "unchecked" })
		public MAX(CriteriaBuilderImpl criteriaBuilder, Expression<N> expression) {
			super( criteriaBuilder, ( Class<N> ) expression.getJavaType(), MAX_NAME, expression);
		}

		@Override
		public org.hibernate.sqm.query.expression.Expression visitExpression(CriteriaVisitor visitor) {
			return visitor.visitFunction(
					MAX_NAME,
					criteriaBuilder().consumerContext().getDomainMetamodel().getBasicType( getJavaType() ),
					getArgumentExpressions()
			);
		}
	}

	/**
	 * Models  the <tt>MIN</tt> function in terms of non-numeric expressions.
	 *
	 * @see MIN
	 */
	public static class LEAST<X extends Comparable<X>> extends AggregationFunction<X> {
		public static final String LEAST_NAME = "min";

		@SuppressWarnings({ "unchecked" })
		public LEAST(CriteriaBuilderImpl criteriaBuilder, Expression<X> expression) {
			super( criteriaBuilder, ( Class<X> ) expression.getJavaType(), LEAST_NAME, expression);
		}

		@Override
		public org.hibernate.sqm.query.expression.Expression visitExpression(CriteriaVisitor visitor) {
			return visitor.visitFunction(
					LEAST_NAME,
					criteriaBuilder().consumerContext().getDomainMetamodel().getBasicType( getJavaType() ),
					getArgumentExpressions()
			);
		}
	}

	/**
	 * Models  the <tt>MAX</tt> function in terms of non-numeric expressions.
	 *
	 * @see MAX
	 */
	public static class GREATEST<X extends Comparable<X>> extends AggregationFunction<X> {
		public static final String GREATEST_NAME = "max";

		@SuppressWarnings({ "unchecked" })
		public GREATEST(CriteriaBuilderImpl criteriaBuilder, Expression<X> expression) {
			super( criteriaBuilder, ( Class<X> ) expression.getJavaType(), GREATEST_NAME, expression);
		}

		@Override
		public org.hibernate.sqm.query.expression.Expression visitExpression(CriteriaVisitor visitor) {
			return visitor.visitFunction(
					GREATEST_NAME,
					criteriaBuilder().consumerContext().getDomainMetamodel().getBasicType( getJavaType() ),
					getArgumentExpressions()
			);
		}
	}
}