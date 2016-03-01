/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: Apache License, Version 2.0
 * See the LICENSE file in the root directory or visit http://www.apache.org/licenses/LICENSE-2.0
 */
package org.hibernate.sqm.parser.internal.criteria;

import javax.persistence.criteria.Expression;

/**
 * @author Steve Ebersole
 */
public interface VisitableExpressionImplementor<X> extends Expression<X>, Visitable<org.hibernate.sqm.query.expression.Expression> {
}
