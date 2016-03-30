/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.test.sqm.parser.criteria.tree.expression;


import org.hibernate.sqm.parser.criteria.spi.CriteriaVisitor;
import org.hibernate.sqm.query.select.AliasedExpressionContainer;
import org.hibernate.test.sqm.parser.criteria.tree.CriteriaBuilderImpl;

import javax.persistence.criteria.Expression;
import java.io.Serializable;

/**
 * A string concatenation.
 *
 * @author Steve Ebersole
 */
public class ConcatExpression extends ExpressionImpl<String> implements Serializable {
	private Expression<String> string1;
	private Expression<String> string2;

	public ConcatExpression(
			CriteriaBuilderImpl criteriaBuilder,
			Expression<String> expression1,
			Expression<String> expression2) {
		super( criteriaBuilder, String.class );
		this.string1 = expression1;
		this.string2 = expression2;
	}

	public ConcatExpression(
			CriteriaBuilderImpl criteriaBuilder,
			Expression<String> string1, 
			String string2) {
		this( criteriaBuilder, string1, wrap( criteriaBuilder, string2) );
	}

	private static Expression<String> wrap(CriteriaBuilderImpl criteriaBuilder, String string) {
		return new LiteralExpression<String>( criteriaBuilder, string );
	}

	public ConcatExpression(
			CriteriaBuilderImpl criteriaBuilder,
			String string1,
			Expression<String> string2) {
		this( criteriaBuilder, wrap( criteriaBuilder, string1), string2 );
	}

	public Expression<String> getString1() {
		return string1;
	}

	public Expression<String> getString2() {
		return string2;
	}

	//FQN: do we want a different classname?
	@Override
	public org.hibernate.sqm.query.expression.Expression visitExpression(CriteriaVisitor visitor) {

		//Hard code to string type?
		return visitor.visitConcat(
				string1,
				string2,
				criteriaBuilder().consumerContext().getDomainMetamodel().getBasicType( getJavaType() )
		);
	}

	@Override
	public void visitSelections(CriteriaVisitor visitor, AliasedExpressionContainer container) {
		container.add( visitExpression( visitor ), getAlias() );
	}
}
