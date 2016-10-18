/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: Apache License, Version 2.0
 * See the LICENSE file in the root directory or visit http://www.apache.org/licenses/LICENSE-2.0
 */
package org.hibernate.test.sqm.parser.criteria.select;

import org.hibernate.sqm.ConsumerContext;
import org.hibernate.sqm.SemanticQueryInterpreter;
import org.hibernate.sqm.query.SelectStatement;
import org.hibernate.test.sqm.ConsumerContextImpl;
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
public class LiteralTestCase extends AbstractCriteriaSmokeTests {


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

		// Enum literal:
		Expression<Color> red = criteriaBuilder.literal(Color.RED);
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
//		criteria.multiselect( t, f, i1, i2, d, empty, jpa, today, time, red, root);
		criteria.multiselect( t, f, i1, i2, d, empty, jpa, today, time, root);

		// now ask the interpreter to convert the criteria into SQM...
		final SelectStatement sqm = SemanticQueryInterpreter.interpret( criteria, consumerContext );
		assertThat( sqm.getQuerySpec().getFromClause().getFromElementSpaces().size(), is(1) ) ;
	}


	public enum Color {
		RED,
		BLUE,
		GREEN
	}

}