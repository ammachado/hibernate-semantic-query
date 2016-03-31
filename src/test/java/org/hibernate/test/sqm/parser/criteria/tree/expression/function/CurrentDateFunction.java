/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.test.sqm.parser.criteria.tree.expression.function;


import org.hibernate.sqm.parser.criteria.spi.CriteriaVisitor;
import org.hibernate.sqm.query.expression.Expression;
import org.hibernate.test.sqm.parser.criteria.tree.CriteriaBuilderImpl;

import java.io.Serializable;
import java.sql.Date;

/**
 * Models the ANSI SQL <tt>CURRENT_DATE</tt> function.
 *
 * @author Steve Ebersole
 */
public class CurrentDateFunction
		extends BasicFunctionExpression<Date>
		implements Serializable {
	public static final String CURRENT_DATE_NAME = "current_date";

	public CurrentDateFunction(	CriteriaBuilderImpl criteriaBuilder) {
		super( criteriaBuilder, Date.class, CURRENT_DATE_NAME);
	}

	@Override
	public Expression visitExpression(CriteriaVisitor visitor) {
		//TODO: check what to pass to visitor when the expression is null
		return visitor.visitFunction(
				CURRENT_DATE_NAME,
				criteriaBuilder().consumerContext().getDomainMetamodel().getBasicType( getJavaType() )
		);
	}
}
