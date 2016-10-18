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
import org.hibernate.test.sqm.parser.criteria.tree.expression.LiteralExpression;
import org.junit.Test;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import java.util.Calendar;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Steve Ebersole
 */
public class NumericalTestCase extends AbstractCriteriaSmokeTests {


	@Test
	public void LiteralExpressionTest() {
		final ConsumerContext consumerContext = new ConsumerContextImpl( buildMetamodel() );
		final CriteriaBuilderImpl criteriaBuilder = new CriteriaBuilderImpl( consumerContext );

		// Build a simple criteria ala `select e from Entity e`
		final CriteriaQueryImpl<Object> criteria = (CriteriaQueryImpl<Object>) criteriaBuilder.createQuery();
		Root root = criteria.from( "com.acme.Entity2" );
		Path basic1 = root.get("basic1");

		Expression sumExpression = criteriaBuilder.sum(new LiteralExpression<Integer>(criteriaBuilder, new Integer(1)));

		criteria.multiselect( sumExpression, basic1, root);

		// now ask the interpreter to convert the criteria into SQM...
		final SelectStatement sqm = SemanticQueryInterpreter.interpret( criteria, consumerContext );
		assertThat( sqm.getQuerySpec().getFromClause().getFromElementSpaces().size(), is(1) ) ;
	}

}
