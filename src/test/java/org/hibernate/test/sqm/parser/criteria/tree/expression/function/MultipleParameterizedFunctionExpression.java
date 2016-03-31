/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.test.sqm.parser.criteria.tree.expression.function;

import org.hibernate.test.sqm.parser.criteria.tree.CriteriaBuilderImpl;

import javax.persistence.criteria.Expression;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Support for functions with parameters.
 *
 * @author Steve Ebersole
 */
public abstract class MultipleParameterizedFunctionExpression<X>
		extends ParameterizedFunctionExpression<X> {

	private final List<Expression<?>> argumentExpressions;

	public MultipleParameterizedFunctionExpression(
			CriteriaBuilderImpl criteriaBuilder,
			Class<X> javaType,
			String functionName,
			List<Expression<?>> argumentExpressions) {
		super( criteriaBuilder, javaType, functionName );
		this.argumentExpressions = argumentExpressions;
	}

	public MultipleParameterizedFunctionExpression(
			CriteriaBuilderImpl criteriaBuilder,
			Class<X> javaType,
			String functionName,
			Expression<?>... argumentExpressions) {
		super( criteriaBuilder, javaType, functionName );
		this.argumentExpressions = Arrays.asList( argumentExpressions );
	}

	public List<Expression<?>> getArgumentExpressions() {
		return argumentExpressions;
	}

}
