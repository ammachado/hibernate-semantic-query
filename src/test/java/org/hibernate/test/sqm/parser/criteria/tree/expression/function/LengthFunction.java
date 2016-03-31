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
 * Models the ANSI SQL <tt>LENGTH</tt> function.
 *
 * @author Steve Ebersole
 */
public class LengthFunction
		extends ParameterizedFunctionExpression<Integer>
		implements Serializable {
	public static final String LENGTH_NAME = "length";

	@Override
	protected boolean isStandardJpaFunction() {
		return true;
	}

	public LengthFunction(CriteriaBuilderImpl criteriaBuilder, Expression<String> value) {
		super( criteriaBuilder, Integer.class, LENGTH_NAME, value );
	}

	@Override
	public org.hibernate.sqm.query.expression.Expression visitExpression(CriteriaVisitor visitor) {
		return visitor.visitFunction(
				LENGTH_NAME,
				criteriaBuilder().consumerContext().getDomainMetamodel().getBasicType( getJavaType() ),
				getArgumentExpressions()
		);
	}
}
