/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: Apache License, Version 2.0
 * See the LICENSE file in the root directory or visit http://www.apache.org/licenses/LICENSE-2.0
 */
package org.hibernate.test.sqm.parser.criteria.select;

import org.hibernate.sqm.ConsumerContext;
import org.hibernate.sqm.SemanticQueryInterpreter;
import org.hibernate.sqm.domain.DomainMetamodel;
import org.hibernate.sqm.domain.SingularAttribute;
import org.hibernate.sqm.query.SelectStatement;
import org.hibernate.sqm.query.select.SelectClause;
import org.hibernate.test.sqm.ConsumerContextImpl;
import org.hibernate.test.sqm.domain.EntityTypeImpl;
import org.hibernate.test.sqm.domain.ExplicitDomainMetamodel;
import org.hibernate.test.sqm.domain.StandardBasicTypeDescriptors;
import org.hibernate.test.sqm.parser.criteria.tree.CriteriaBuilderImpl;
import org.hibernate.test.sqm.parser.criteria.tree.CriteriaQueryImpl;
import org.junit.Test;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import java.util.Calendar;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Steve Ebersole
 */
public class AbstractCriteriaSmokeTests {


	@Test
	public void LiteralExpressionTest() {
		final ConsumerContext consumerContext = new ConsumerContextImpl( buildMetamodel() );
		final CriteriaBuilderImpl criteriaBuilder = new CriteriaBuilderImpl( consumerContext );

		// Boolean literals:
		Expression<Boolean> t = criteriaBuilder.literal(true);
		Expression<Boolean> f = criteriaBuilder.literal(Boolean.FALSE);

		// Numeric literals:
		Expression<Integer> i1 = criteriaBuilder.literal(1);
		Expression<Integer> i2 = criteriaBuilder.literal(Integer.valueOf(2));
		Expression<Double> d = criteriaBuilder.literal(3.4);

		// String literals:
		Expression<String> empty = criteriaBuilder.literal("");
		Expression<String> jpa = criteriaBuilder.literal("JPA");

		// Date and Time literals:
		Expression<java.sql.Date> today = criteriaBuilder.literal(new java.sql.Date(Calendar.getInstance().getTime().getTime()));
		Expression<java.sql.Time> time = criteriaBuilder.literal(new java.sql.Time(Calendar.getInstance().getTime().getTime()));
//		Expression<java.sql.Timestamp> now = criteriaBuilder.literal(new java.sql.Timestamp());

//		// Enum literal:
//		Expression<Color> red = cb.literal(Color.RED);
//
//		// Entity Type literal:
//		Expression<Class> type = cb.literal(MyEntity.class);


		// Null Literal
//		Expression n = criteriaBuilder.literal(null);

		Expression<String> strNull = criteriaBuilder.nullLiteral(String.class);
		Expression<Integer> intNull = criteriaBuilder.nullLiteral(Integer.class);


		// Build a simple criteria ala `select e from Entity e`
		final CriteriaQueryImpl<Object> criteria = (CriteriaQueryImpl<Object>) criteriaBuilder.createQuery();
		Root root = criteria.from( "com.acme.Entity2" );
		criteria.multiselect( t, f, i1, i2, d, empty, jpa, today, time, root);

		// now ask the interpreter to convert the criteria into SQM...
		final SelectStatement sqm = SemanticQueryInterpreter.interpret( criteria, consumerContext );
		assertThat( sqm.getQuerySpec().getFromClause().getFromElementSpaces().size(), is(1) ) ;
		SelectClause selectClause = sqm.getQuerySpec().getSelectClause();
		assertThat( selectClause.getSelections().size(), is(10) ) ;
	}

/*
	@Test
	public void ConcatExpressionTest() {
		final ConsumerContext consumerContext = new ConsumerContextImpl( buildMetamodel() );
		final CriteriaBuilderImpl criteriaBuilder = new CriteriaBuilderImpl( consumerContext );

		// Build a simple criteria ala `select e from Entity e`
		final CriteriaQueryImpl<Object> criteria = (CriteriaQueryImpl<Object>) criteriaBuilder.createQuery();
		Root root = criteria.from( "com.acme.Entity" );

		Expression<String> path  = root.get("basic2");

		Expression<String> c3 = criteriaBuilder.concat("concat ", path);

		criteria.select( c3 );

		// now ask the interpreter to convert the criteria into SQM...
		final SelectStatement sqm = SemanticQueryInterpreter.interpret( criteria, consumerContext );
		assertThat( sqm.getQuerySpec().getFromClause().getFromElementSpaces().size(), is(1) ) ;
	}
*/


	protected DomainMetamodel buildMetamodel() {
		ExplicitDomainMetamodel metamodel = new ExplicitDomainMetamodel();

		EntityTypeImpl entity2Type = metamodel.makeEntityType( "com.acme.Entity2" );
		entity2Type.makeSingularAttribute(
				"basic1",
				SingularAttribute.Classification.BASIC,
				StandardBasicTypeDescriptors.INSTANCE.LONG
		);

		EntityTypeImpl entityType = metamodel.makeEntityType( "com.acme.Entity" );
		entityType.makeSingularAttribute(
				"basic",
				SingularAttribute.Classification.BASIC,
				StandardBasicTypeDescriptors.INSTANCE.LONG
		);
		entityType.makeSingularAttribute(
				"basic1",
				SingularAttribute.Classification.BASIC,
				StandardBasicTypeDescriptors.INSTANCE.LONG
		);
		entityType.makeSingularAttribute(
				"basic2",
				SingularAttribute.Classification.BASIC,
				StandardBasicTypeDescriptors.INSTANCE.STRING
		);
		entityType.makeSingularAttribute(
				"basic3",
				SingularAttribute.Classification.BASIC,
				StandardBasicTypeDescriptors.INSTANCE.STRING
		);
		entityType.makeSingularAttribute(
				"basic4",
				SingularAttribute.Classification.BASIC,
				StandardBasicTypeDescriptors.INSTANCE.STRING
		);

		EntityTypeImpl legType = metamodel.makeEntityType( "com.acme.Leg" );

		EntityTypeImpl tripType = metamodel.makeEntityType( "com.acme.Trip" );
		tripType.makeMapAttribute(
				"mapLegs",
				StandardBasicTypeDescriptors.INSTANCE.STRING,
				legType
		);
		tripType.makeListAttribute(
				"collectionLegs",
				StandardBasicTypeDescriptors.INSTANCE.INTEGER,
				legType
		);

		return metamodel;
	}

}
