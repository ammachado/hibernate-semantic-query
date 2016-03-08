/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: Apache License, Version 2.0
 * See the LICENSE file in the root directory or visit http://www.apache.org/licenses/LICENSE-2.0
 */
package org.hibernate.sqm.parser.internal.hql.path;

import org.hibernate.sqm.parser.internal.FromElementBuilder;
import org.hibernate.sqm.parser.internal.ParsingContext;

/**
 * Defines a context for performing path resolutions
 *
 * @author Steve Ebersole
 */
public interface ResolutionContext {
	FromElementLocator getFromElementLocator();
	FromElementBuilder getFromElementBuilder();
	ParsingContext getParsingContext();
}
