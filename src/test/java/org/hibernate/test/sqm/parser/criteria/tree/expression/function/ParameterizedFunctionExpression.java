/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.test.sqm.parser.criteria.tree.expression.function;

import org.hibernate.test.sqm.parser.criteria.tree.CriteriaBuilderImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Support for functions with parameters.
 *
 * @author Steve Ebersole
 */
public abstract class ParameterizedFunctionExpression<X>
		extends BasicFunctionExpression<X>
		implements FunctionExpression<X> {

	public static List<String> STANDARD_JPA_FUNCTION_NAMES = Arrays.asList(
			// 4.6.17.2.1
			"CONCAT",
			"SUBSTRING",
			"TRIM",
			"UPPER",
			"LOWER",
			"LOCATE",
			"LENGTH",
			//4.6.17.2.2
			"ABS",
			"SQRT",
			"MOD",
			"SIZE",
			"INDEX",
			// 4.6.17.2.3
			"CURRENT_DATE",
			"CURRENT_TIME",
			"CURRENT_TIMESTAMP"
	);

	private final boolean isStandardJpaFunction;

	public ParameterizedFunctionExpression(
			CriteriaBuilderImpl criteriaBuilder,
			Class<X> javaType,
			String functionName) {
		super( criteriaBuilder, javaType, functionName );
		this.isStandardJpaFunction = STANDARD_JPA_FUNCTION_NAMES.contains( functionName.toUpperCase(Locale.ROOT) );
	}

	protected boolean isStandardJpaFunction() {
		return isStandardJpaFunction;
	}

	protected  static int properSize(int number) {
		return number + (int)( number*.75 ) + 1;
	}


}
