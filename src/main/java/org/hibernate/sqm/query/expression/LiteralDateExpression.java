/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: Apache License, Version 2.0
 * See the LICENSE file in the root directory or visit http://www.apache.org/licenses/LICENSE-2.0
 */
package org.hibernate.sqm.query.expression;

import org.hibernate.sqm.SemanticQueryWalker;
import org.hibernate.sqm.domain.BasicType;

import java.util.Date;

/**
 * @author Steve Ebersole
 */
public class LiteralDateExpression extends AbstractLiteralExpressionImpl<Date> {
	public LiteralDateExpression(Date value, BasicType<Date> typeDescriptor) {
		super( value, typeDescriptor );
	}

	@Override
	public <T> T accept(SemanticQueryWalker<T> walker) {
		return walker.visitLiteralDateExpression( this );
	}

	@Override
	protected void validateInferredType(Class javaType) {
		if ( !Compatibility.areAssignmentCompatible( javaType, Date.class ) ) {
			throw new TypeInferenceException( "Date literal is not convertible to inferred type [" + javaType + "]" );
		}
	}
}
