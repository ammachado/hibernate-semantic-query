/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: Apache License, Version 2.0
 * See the LICENSE file in the root directory or visit http://www.apache.org/licenses/LICENSE-2.0
 */
package org.hibernate.sqm.parser.internal.hql;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.sqm.parser.internal.AliasRegistry;
import org.hibernate.sqm.parser.internal.FromElementBuilder;
import org.hibernate.sqm.parser.internal.ParsingContext;
import org.hibernate.sqm.parser.internal.hql.path.FromElementLocator;
import org.hibernate.sqm.query.from.FromClause;
import org.hibernate.sqm.query.from.FromElement;
import org.hibernate.sqm.query.from.FromElementSpace;
import org.hibernate.sqm.query.from.JoinedFromElement;

import org.jboss.logging.Logger;

/**
 * Models the state related to parsing a query spec.  As a "linked list" to account for
 * subqueries
 *
 * @author Steve Ebersole
 * @author Andrea Boriero
 */
public class QuerySpecProcessingStateStandardImpl implements QuerySpecProcessingState {
	private static final Logger log = Logger.getLogger( QuerySpecProcessingStateStandardImpl.class );

	private final QuerySpecProcessingState parent;

	private final ParsingContext parsingContext;
	private final FromClause fromClause;

	private final FromElementBuilder fromElementBuilder;

	private Map<String,FromElement> fromElementsByPath = new HashMap<String, FromElement>();

	public QuerySpecProcessingStateStandardImpl(ParsingContext parsingContext) {
		this( parsingContext, null );
	}

	public QuerySpecProcessingStateStandardImpl(ParsingContext parsingContext, QuerySpecProcessingState parent) {
		this.parent = parent;

		this.parsingContext = parsingContext;
		this.fromClause = new FromClause();

		if ( parent == null ) {
			this.fromElementBuilder = new FromElementBuilder( parsingContext, new AliasRegistry() );
		}
		else {
			this.fromElementBuilder = new FromElementBuilder(
					parsingContext,
					new AliasRegistry( parent.getFromElementBuilder().getAliasRegistry() )
			);
		}
	}

	public QuerySpecProcessingState getParent() {
		return parent;
	}

	public FromClause getFromClause() {
		return fromClause;
	}

	@Override
	public ParsingContext getParsingContext() {
		return parsingContext;
	}

	@Override
	public FromElementBuilder getFromElementBuilder() {
		return fromElementBuilder;
	}

	@Override
	public FromElement findFromElementByIdentificationVariable(String identificationVariable) {
		return fromElementBuilder.getAliasRegistry().findFromElementByAlias( identificationVariable );
	}

	@Override
	public FromElement findFromElementExposingAttribute(String name) {
		FromElement found = null;
		for ( FromElementSpace space : fromClause.getFromElementSpaces() ) {
			if ( space.getRoot().resolveAttribute( name ) != null ) {
				if ( found != null ) {
					throw new IllegalStateException( "Multiple from-elements expose unqualified attribute : " + name );
				}
				found = space.getRoot();
			}

			for ( JoinedFromElement join : space.getJoins() ) {
				if ( join.resolveAttribute( name ) != null ) {
					if ( found != null ) {
						throw new IllegalStateException( "Multiple from-elements expose unqualified attribute : " + name );
					}
					found = join;
				}
			}
		}

		if ( found == null ) {
			if ( parent != null ) {
				log.debugf( "Unable to resolve unqualified attribute [%s] in local FromClause; checking parent" );
				found = parent.findFromElementExposingAttribute( name );
			}
		}

		return found;
	}

	@Override
	public FromElementLocator getFromElementLocator() {
		return this;
	}
}
