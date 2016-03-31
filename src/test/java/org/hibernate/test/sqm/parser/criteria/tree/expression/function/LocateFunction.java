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
import java.util.ArrayList;
import java.util.List;

/**
 * Models the ANSI SQL <tt>LOCATE</tt> function.
 *
 * @author Steve Ebersole
 */
public class LocateFunction
		extends BasicFunctionExpression<Integer>
		implements Serializable {
	public static final String LOCATE_NAME = "locate";

	private final Expression<String> pattern;
	private final Expression<String> string;
	private final Expression<Integer> start;

	public LocateFunction(
			CriteriaBuilderImpl criteriaBuilder,
			Expression<String> pattern,
			Expression<String> string,
			Expression<Integer> start) {
		super( criteriaBuilder, Integer.class, LOCATE_NAME);
		this.pattern = pattern;
		this.string = string;
		this.start = start;
	}

	public LocateFunction(
			CriteriaBuilderImpl criteriaBuilder,
			Expression<String> pattern,
			Expression<String> string) {
		this( criteriaBuilder, pattern, string, null );
	}

	public LocateFunction(CriteriaBuilderImpl criteriaBuilder, String pattern, Expression<String> string) {
		this(
				criteriaBuilder,
				new LiteralExpression<String>( criteriaBuilder, pattern ),
				string,
				null
		);
	}

	public LocateFunction(CriteriaBuilderImpl criteriaBuilder, String pattern, Expression<String> string, int start) {
		this(
				criteriaBuilder,
				new LiteralExpression<String>( criteriaBuilder, pattern ),
				string,
				new LiteralExpression<Integer>( criteriaBuilder, start )
		);
	}

	public Expression<String> getPattern() {
		return pattern;
	}

	public Expression<Integer> getStart() {
		return start;
	}

	public Expression<String> getString() {
		return string;
	}

	@Override
	public org.hibernate.sqm.query.expression.Expression visitExpression(CriteriaVisitor visitor) {

		List<Expression<?>> expressionList = new ArrayList<Expression<?>>();
		expressionList.add(pattern);
		expressionList.add(string);
		expressionList.add(start);
		return visitor.visitFunction(
				LOCATE_NAME,
				criteriaBuilder().consumerContext().getDomainMetamodel().getBasicType( getJavaType() ),
				expressionList
		);
	}
}
