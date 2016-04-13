/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.test.sqm.parser.criteria.tree.expression.function;

import org.hibernate.sqm.parser.criteria.spi.CriteriaVisitor;
import org.hibernate.test.sqm.parser.criteria.tree.CriteriaBuilderImpl;

import javax.persistence.criteria.Expression;
import java.io.Serializable;

/**
 * Models the ANSI SQL <tt>ABS</tt> function.
 *
 * @author Steve Ebersole
 */
public class AbsFunction<N extends Number>
		extends ParameterizedFunctionExpression<N>
		implements Serializable {

	private final Expression expression;
	public static final String ABS_NAME = "abs";

	public AbsFunction(CriteriaBuilderImpl criteriaBuilder, Expression expression) {
		super( criteriaBuilder, expression.getJavaType(), ABS_NAME);
		this.expression = expression;
	}

	@Override
	protected boolean isStandardJpaFunction() {
		return true;
	}

	@Override
	public org.hibernate.sqm.query.expression.Expression visitExpression(CriteriaVisitor visitor) {
		return visitor.visitFunction(
				ABS_NAME,
				criteriaBuilder().consumerContext().getDomainMetamodel().getBasicType( getJavaType() ),
				this.expression
		);
	}
}
