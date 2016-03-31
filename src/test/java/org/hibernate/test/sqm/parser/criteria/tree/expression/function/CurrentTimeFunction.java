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
import java.sql.Time;

/**
 * Models the ANSI SQL <tt>CURRENT_TIME</tt> function.
 *
 * @author Steve Ebersole
 */
public class CurrentTimeFunction
		extends BasicFunctionExpression<Time> 
		implements Serializable {
	public static final String CURRENT_TIME_NAME = "current_time";

	public CurrentTimeFunction(CriteriaBuilderImpl criteriaBuilder) {
		super( criteriaBuilder, Time.class, CURRENT_TIME_NAME );
	}

	@Override
	public Expression visitExpression(CriteriaVisitor visitor) {
		//TODO: check what to pass to visitor when the expression is null
		return visitor.visitFunction(
				CURRENT_TIME_NAME,
				criteriaBuilder().consumerContext().getDomainMetamodel().getBasicType( getJavaType() )
		);
	}
}
