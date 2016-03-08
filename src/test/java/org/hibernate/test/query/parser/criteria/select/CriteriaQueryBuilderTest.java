package org.hibernate.test.query.parser.criteria.select;

import junit.framework.Assert;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.xpath.XPath;
import org.hibernate.hql.internal.ast.tree.SelectExpression;
import org.hibernate.sqm.SemanticQueryInterpreter;
import org.hibernate.sqm.domain.DomainMetamodel;
import org.hibernate.sqm.parser.internal.hql.HqlParseTreeBuilder;
import org.hibernate.sqm.parser.internal.hql.antlr.HqlParser;
import org.hibernate.sqm.path.FromElementBinding;
import org.hibernate.sqm.query.QuerySpec;
import org.hibernate.sqm.query.SelectStatement;
import org.hibernate.sqm.query.expression.MapEntryFunction;
import org.hibernate.sqm.query.from.FromClause;
import org.hibernate.sqm.query.from.FromElementSpace;
import org.hibernate.sqm.query.from.RootEntityFromElement;
import org.hibernate.sqm.query.select.SelectClause;
import org.hibernate.sqm.query.select.Selection;
import org.hibernate.test.query.parser.ConsumerContextImpl;
import org.hibernate.test.sqm.domain.EntityTypeImpl;
import org.hibernate.test.sqm.domain.ExplicitDomainMetamodel;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;


import java.util.Collection;

import static junit.framework.Assert.*;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * Created by johara on 23/02/16.
 */
public class CriteriaQueryBuilderTest extends CriteriaQueryBuilderAbstractTest {

    public CriteriaQueryBuilderTest() {
        super();
    }

    final ConsumerContextImpl consumerContext = new ConsumerContextImpl(buildMetamodel());

    @Test
    public void simpleCriteriaQueryTest() {

        final String PERSISTENCE_UNIT_NAME = "Books";
        EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

        EntityManager em = factory.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<Book> q = cb.createQuery(Book.class);
        Root<Book> b = q.from(Book.class);

        q.select(b);

        // Create String path and parameter expressions:
/*
        Expression<String> author = b.get("author");

        ParameterExpression<Integer> p = cb.parameter(Integer.class);
        q.orderBy( cb.desc( b.get( "id" ) ) );
        Predicate n2 = cb.isNotNull(author);


*/

        SelectStatement criteriaQuerySelect = null;

        criteriaQuerySelect = SemanticQueryInterpreter.interpret(q, consumerContext);

        checkStatement(criteriaQuerySelect);

    }

    @Test
    public void simpleJPQLTest() {

        final String QUERY = "select b from Book b";


        SelectStatement statement = (SelectStatement) SemanticQueryInterpreter.interpret( QUERY , consumerContext );


        checkStatement(statement);
    }


    private void checkStatement(SelectStatement selectStatement){
        assertNotNull(selectStatement);

        assertNotNull(selectStatement.getQuerySpec());
        assertNotNull(selectStatement.getQuerySpec().getSelectClause());
        assertNotNull(selectStatement.getQuerySpec().getFromClause());
        assertNull(selectStatement.getQuerySpec().getWhereClause());

        FromClause from = selectStatement.getQuerySpec().getFromClause();
        assertEquals(1, from.getFromElementSpaces().size());

        FromElementSpace fromElementSpace = from.getFromElementSpaces().get(0);

        assertThat(
                fromElementSpace.getRoot(),
                instanceOf( RootEntityFromElement.class )
        );

        assertEquals("<uid:1>", fromElementSpace.getRoot().getUniqueIdentifier());

        SelectClause selectClause = selectStatement.getQuerySpec().getSelectClause();

        assertTrue(selectClause.isDistinct() == false);

        assertEquals(1,selectClause.getSelections().size());

        Selection selection = selectClause.getSelections().get(0);

//TODO: look at why different aliases are generated for CriteriaQuery vs JPQL - does it matter?
//        assertEquals("<gen:0>", selection.getAlias());

        assertThat(
                selection.getExpression(),
                instanceOf( RootEntityFromElement.class )
        );

        assertEquals(fromElementSpace.getRoot(), selection.getExpression());

//        SelectExpression selectExpression = selection.getExpression()

    }
/*
    @Test
    public void joinCriteriaQuerTest() {

        final String PERSISTENCE_UNIT_NAME = "Books";
        EntityManagerFactory factory = Persistence.createEntityManagerFactory( PERSISTENCE_UNIT_NAME );

        EntityManager em = factory.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<Book> q = cb.createQuery( Book.class );
        Root<Book> book = q.from( Book.class );

//        Join<Book, Author> authorJoin = book.join( Book_.author );

        q.select( book ).orderBy( cb.desc( book.get( "id" ) ) );


        QuerySpec criteriaQuerySpec = null;

        SelectStatement criteriaQuerySelect = null;

        criteriaQuerySelect = SemanticQueryInterpreter.interpret( q, consumerContext );

        assertNotNull( criteriaQuerySpec );

    }
*/


    private DomainMetamodel buildMetamodel() {
        ExplicitDomainMetamodel metamodel = new ExplicitDomainMetamodel();
        EntityTypeImpl entityType = metamodel.makeEntityType("org.hibernate.test.query.parser.criteria.select.Book");
        return metamodel;
    }

}
