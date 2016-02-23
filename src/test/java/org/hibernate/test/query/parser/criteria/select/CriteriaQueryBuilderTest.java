package org.hibernate.test.query.parser.criteria.select;

import org.hibernate.sqm.parser.SemanticQueryInterpreter;
import org.hibernate.sqm.query.QuerySpec;
import org.hibernate.sqm.query.SelectStatement;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;


import static org.junit.Assert.assertNotNull;

/**
 * Created by johara on 23/02/16.
 */
public class CriteriaQueryBuilderTest extends CriteriaQueryBuilderAbstractTest {

    public CriteriaQueryBuilderTest() {
        super();
    }

    @Test
    public void simpleCriteriaQuerTest() {

        final String PERSISTENCE_UNIT_NAME = "Books";
        EntityManagerFactory factory = Persistence.createEntityManagerFactory( PERSISTENCE_UNIT_NAME );

        EntityManager em = factory.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Book> q = cb.createQuery( Book.class );
        Root<Book> b = q.from( Book.class );
        q.select( b ).orderBy( cb.desc( b.get( "id" ) ) );


        SelectStatement criteriaQuerySelect = null;

        criteriaQuerySelect = SemanticQueryInterpreter.interpret( q, null );

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

        criteriaQuerySelect = SemanticQueryInterpreter.interpret( q, null );

        assertNotNull( criteriaQuerySpec );

    }



}
