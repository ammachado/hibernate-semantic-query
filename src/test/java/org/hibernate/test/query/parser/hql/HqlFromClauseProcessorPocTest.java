/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: Apache License, Version 2.0
 * See the LICENSE file in the root directory or visit http://www.apache.org/licenses/LICENSE-2.0
 */
package org.hibernate.test.query.parser.hql;

import org.hibernate.sqm.domain.DomainMetamodel;
import org.hibernate.sqm.domain.SingularAttribute;
import org.hibernate.sqm.parser.SemanticQueryInterpreter;
import org.hibernate.sqm.parser.internal.ImplicitAliasGenerator;
import org.hibernate.sqm.query.JoinType;
import org.hibernate.sqm.query.SelectStatement;
import org.hibernate.sqm.query.from.FromClause;
import org.hibernate.sqm.query.from.FromElementSpace;
import org.hibernate.sqm.query.from.RootEntityFromElement;

import org.hibernate.test.query.parser.ConsumerContextImpl;
import org.hibernate.test.sqm.domain.EntityTypeImpl;
import org.hibernate.test.sqm.domain.ExplicitDomainMetamodel;
import org.hibernate.test.sqm.domain.StandardBasicTypeDescriptors;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Initial work on a "from clause processor"
 *
 * @author Steve Ebersole
 */
public class HqlFromClauseProcessorPocTest {
	@Test
	public void testSimpleFrom() throws Exception {
		final SelectStatement selectStatement = interpret( "select a.b from Something a" );

		final FromClause fromClause1 = selectStatement.getQuerySpec().getFromClause();
		assertNotNull( fromClause1 );
		assertEquals( 1, fromClause1.getFromElementSpaces().size() );
		FromElementSpace space1 = fromClause1.getFromElementSpaces().get( 0 );
		assertNotNull( space1 );
		assertNotNull( space1.getRoot() );
		assertEquals( 0, space1.getJoins().size() );
		RootEntityFromElement root = space1.getRoot();
		assertNotNull( root );
		assertThat( root.getIdentificationVariable(), is( "a") );
	}

	private SelectStatement interpret(String query) {
		return (SelectStatement) SemanticQueryInterpreter.interpret( query, buildConsumerContext() );
	}

	private ConsumerContextImpl buildConsumerContext() {
		return new ConsumerContextImpl( buildMetamodel() );
	}

	private DomainMetamodel buildMetamodel() {
		ExplicitDomainMetamodel metamodel = new ExplicitDomainMetamodel();
		EntityTypeImpl somethingEntityType = metamodel.makeEntityType( "com.acme.Something" );
		EntityTypeImpl somethingElseEntityType = metamodel.makeEntityType( "com.acme.SomethingElse" );
		EntityTypeImpl associatedEntityType = metamodel.makeEntityType( "com.acme.Related" );

		somethingEntityType.makeSingularAttribute(
				"b",
				SingularAttribute.Classification.BASIC,
				StandardBasicTypeDescriptors.INSTANCE.STRING
		);
		somethingEntityType.makeSingularAttribute(
				"basic",
				SingularAttribute.Classification.BASIC,
				StandardBasicTypeDescriptors.INSTANCE.LONG
		);
		somethingEntityType.makeSingularAttribute(
				"entity",
				SingularAttribute.Classification.BASIC,
				associatedEntityType
		);

		associatedEntityType.makeSingularAttribute(
				"basic1",
				SingularAttribute.Classification.BASIC,
				StandardBasicTypeDescriptors.INSTANCE.LONG
		);
		associatedEntityType.makeSingularAttribute(
				"basic2",
				SingularAttribute.Classification.BASIC,
				StandardBasicTypeDescriptors.INSTANCE.LONG
		);

		return metamodel;
	}

	@Test
	public void testMultipleSpaces() throws Exception {
		final SelectStatement selectStatement = interpret( "select a.b from Something a, SomethingElse b" );

		final FromClause fromClause1 = selectStatement.getQuerySpec().getFromClause();
		assertNotNull( fromClause1 );
//		assertEquals( 0, fromClause1.getChildFromClauses().size() );
		assertEquals( 2, fromClause1.getFromElementSpaces().size() );
		FromElementSpace space1 = fromClause1.getFromElementSpaces().get( 0 );
		FromElementSpace space2 = fromClause1.getFromElementSpaces().get( 1 );
		assertNotNull( space1.getRoot() );
		assertEquals( 0, space1.getJoins().size() );
		assertNotNull( space2.getRoot() );
		assertEquals( 0, space2.getJoins().size() );

		assertNotNull( space1.getRoot() );
		assertThat( space1.getRoot().getIdentificationVariable(), is( "a")  );

		assertNotNull( space2.getRoot() );
		assertThat( space2.getRoot().getIdentificationVariable(), is( "b")  );
	}

	@Test
	public void testImplicitAlias() throws Exception {
		final SelectStatement selectStatement = interpret( "select b from Something" );

		final FromClause fromClause1 = selectStatement.getQuerySpec().getFromClause();
		assertNotNull( fromClause1 );
//		assertEquals( 0, fromClause1.getChildFromClauses().size() );
		assertEquals( 1, fromClause1.getFromElementSpaces().size() );
		FromElementSpace space1 = fromClause1.getFromElementSpaces().get( 0 );
		assertNotNull( space1 );
		assertNotNull( space1.getRoot() );
		assertEquals( 0, space1.getJoins().size() );
		assertTrue( ImplicitAliasGenerator.isImplicitAlias( space1.getRoot().getIdentificationVariable() ) );
	}

	@Test
	public void testCrossJoin() throws Exception {
		final SelectStatement selectStatement = interpret( "select a.b from Something a cross join SomethingElse b" );

		final FromClause fromClause1 = selectStatement.getQuerySpec().getFromClause();
		assertNotNull( fromClause1 );
//		assertEquals( 0, fromClause1.getChildFromClauses().size() );
		assertEquals( 1, fromClause1.getFromElementSpaces().size() );
		FromElementSpace space1 = fromClause1.getFromElementSpaces().get( 0 );
		assertNotNull( space1 );
		assertNotNull( space1.getRoot() );
		assertEquals( 1, space1.getJoins().size() );
	}

	@Test
	public void testSimpleImplicitInnerJoin() throws Exception {
		simpleJoinAssertions(
				interpret( "select a.b from Something a join a.entity c" ),
				JoinType.INNER
		);
	}

	private void simpleJoinAssertions(SelectStatement selectStatement, JoinType joinType) {
		final FromClause fromClause1 = selectStatement.getQuerySpec().getFromClause();
		assertNotNull( fromClause1 );
//		assertEquals( 0, fromClause1.getChildFromClauses().size() );
		assertEquals( 1, fromClause1.getFromElementSpaces().size() );
		FromElementSpace space1 = fromClause1.getFromElementSpaces().get( 0 );
		assertNotNull( space1 );
		assertNotNull( space1.getRoot() );
		assertEquals( 1, space1.getJoins().size() );
	}

	@Test
	public void testSimpleExplicitInnerJoin() throws Exception {
		simpleJoinAssertions(
				interpret( "select a.basic from Something a inner join a.entity c" ),
				JoinType.INNER
		);
	}

	@Test
	public void testSimpleExplicitOuterJoin() throws Exception {
		simpleJoinAssertions(
				interpret( "select a.basic from Something a outer join a.entity c" ),
				JoinType.LEFT
		);
	}

	@Test
	public void testSimpleExplicitLeftOuterJoin() throws Exception {
		simpleJoinAssertions(
				interpret( "select a.basic from Something a left outer join a.entity c" ),
				JoinType.LEFT
		);
	}

	@Test
	public void testAttributeJoinWithOnClause() throws Exception {
		SelectStatement selectStatement = interpret( "select a from Something a left outer join a.entity c on c.basic1 > 5 and c.basic2 < 20 " );

		final FromClause fromClause1 = selectStatement.getQuerySpec().getFromClause();
		assertNotNull( fromClause1 );
//		assertEquals( 0, fromClause1.getChildFromClauses().size() );
		assertEquals( 1, fromClause1.getFromElementSpaces().size() );
		FromElementSpace space1 = fromClause1.getFromElementSpaces().get( 0 );
		assertNotNull( space1 );
		assertNotNull( space1.getRoot() );
		assertEquals( 1, space1.getJoins().size() );
	}
}
