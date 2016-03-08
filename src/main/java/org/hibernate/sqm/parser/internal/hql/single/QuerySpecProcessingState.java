/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: Apache License, Version 2.0
 * See the LICENSE file in the root directory or visit http://www.apache.org/licenses/LICENSE-2.0
 */
package org.hibernate.sqm.parser.internal.hql.single;

import org.hibernate.sqm.parser.internal.hql.path.FromElementLocator;
import org.hibernate.sqm.parser.internal.hql.path.ResolutionContext;
import org.hibernate.sqm.query.from.FromClause;

/**
 * @author Steve Ebersole
 */
public interface QuerySpecProcessingState extends FromElementLocator, ResolutionContext {
	QuerySpecProcessingState getParent();
	FromClause getFromClause();
}
