package org.hibernate.test.query.parser.criteria.select;

import org.hibernate.sqm.domain.DomainMetamodel;
import org.hibernate.sqm.parser.SemanticQueryInterpreter;
import org.hibernate.sqm.query.QuerySpec;
import org.hibernate.sqm.query.SelectStatement;
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


import static org.junit.Assert.assertNotNull;

/**
 * Created by johara on 23/02/16.
 */
public class CriteriaQueryBuilderTest extends CriteriaQueryBuilderAbstractTest {

    public CriteriaQueryBuilderTest() {
        super();
    }

    final ConsumerContextImpl consumerContext = new ConsumerContextImpl( buildMetamodel() );

    @Test
    public void simpleCriteriaQuerTest() {

        final String PERSISTENCE_UNIT_NAME = "Books";
        EntityManagerFactory factory = Persistence.createEntityManagerFactory( PERSISTENCE_UNIT_NAME );

        EntityManager em = factory.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<Book> q = cb.createQuery( Book.class );
        Root<Book> b = q.from( Book.class );

        // Create String path and parameter expressions:
        Expression<String> author = b.get("author");

        ParameterExpression<Integer> p = cb.parameter(Integer.class);
        q.select( b );
        q.orderBy( cb.desc( b.get( "id" ) ) );
        Predicate n2 = cb.isNotNull(author);


        SelectStatement criteriaQuerySelect = null;


        criteriaQuerySelect = SemanticQueryInterpreter.interpret( q, consumerContext );

        assertNotNull( criteriaQuerySelect );

    }

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



    private DomainMetamodel buildMetamodel() {
        ExplicitDomainMetamodel metamodel = new ExplicitDomainMetamodel();
        EntityTypeImpl entityType = metamodel.makeEntityType( "org.hibernate.test.query.parser.criteria.select.Book" );
        return metamodel;
    }

}
