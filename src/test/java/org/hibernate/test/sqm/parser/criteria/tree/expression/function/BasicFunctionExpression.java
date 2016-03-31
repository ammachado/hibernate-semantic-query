/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.test.sqm.parser.criteria.tree.expression.function;


import org.hibernate.test.sqm.parser.criteria.tree.CriteriaBuilderImpl;

import java.io.Serializable;

/**
 * Models the basic concept of a SQL function.
 *
 * @author Steve Ebersole
 */
public abstract class BasicFunctionExpression<X>
		extends AbstractFunctionExpression<X>
		implements FunctionExpression<X>, Serializable {

	public BasicFunctionExpression(
			CriteriaBuilderImpl criteriaBuilder,
			Class<X> javaType,
			String functionName) {
		super( criteriaBuilder, functionName, javaType );
	}

	protected  static int properSize(int number) {
		return number + (int)( number*.75 ) + 1;
	}

	public String getFunctionName() {
		return functionName;
	}

	public boolean isAggregation() {
		return false;
	}

}
