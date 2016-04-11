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
 * Models the ANSI SQL <tt>UPPER</tt> function.
 *
 * @author Steve Ebersole
 */
public class UpperFunction
		extends ParameterizedFunctionExpression<String>
		implements Serializable {
	public static final String UPPER_NAME = "upper";
	private final Expression<String> expression;

	public UpperFunction(CriteriaBuilderImpl criteriaBuilder, Expression<String> expression) {
		super( criteriaBuilder, String.class, UPPER_NAME);
		this.expression = expression;
	}

	@Override
	protected boolean isStandardJpaFunction() {
		return true;
	}

	@Override
	public org.hibernate.sqm.query.expression.Expression visitExpression(CriteriaVisitor visitor) {
		return visitor.visitFunction(
				UPPER_NAME,
				criteriaBuilder().consumerContext().getDomainMetamodel().getBasicType( getJavaType() ),
				this.expression
		);

	}
}
