package org.hibernate.test.query.parser.criteria.select;

import org.hibernate.sqm.SemanticQueryInterpreter;
import org.hibernate.sqm.domain.DomainMetamodel;
import org.hibernate.sqm.domain.SingularAttribute;
import org.hibernate.sqm.path.FromElementBinding;
import org.hibernate.sqm.query.SelectStatement;
import org.hibernate.sqm.query.select.Selection;
import org.hibernate.test.query.parser.ConsumerContextImpl;
import org.hibernate.test.sqm.domain.EntityTypeImpl;
import org.hibernate.test.sqm.domain.ExplicitDomainMetamodel;
import org.hibernate.test.sqm.domain.StandardBasicTypeDescriptors;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Created by johara on 12/02/16.
 */
public class SimpleCriteriaSQMBuilderTest {
    private ConsumerContextImpl consumerContext;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUpContext() {
        consumerContext = new ConsumerContextImpl(buildMetamodel());
    }

    private DomainMetamodel buildMetamodel() {
        ExplicitDomainMetamodel metamodel = new ExplicitDomainMetamodel();

        EntityTypeImpl entity2Type = metamodel.makeEntityType("com.acme.Entity2");
        entity2Type.makeSingularAttribute(
                "basic1",
                SingularAttribute.Classification.BASIC,
                StandardBasicTypeDescriptors.INSTANCE.LONG
        );

        EntityTypeImpl entityType = metamodel.makeEntityType("com.acme.Entity");
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

        EntityTypeImpl legType = metamodel.makeEntityType("com.acme.Leg");

        EntityTypeImpl tripType = metamodel.makeEntityType("com.acme.Trip");
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

    @Test
    public void testSimpleAliasSelection() {

        CriteriaBuilder cb = getCriteriaBuilder();

        CriteriaQuery<Entity2> cq = cb.createQuery(Entity2.class);

        CriteriaQuery criteriaQuery = cb.createQuery();

        SelectStatement statement = interpret(criteriaQuery);
        assertEquals(1, statement.getQuerySpec().getSelectClause().getSelections().size());
        Selection selection = statement.getQuerySpec().getSelectClause().getSelections().get(0);
        assertThat(selection.getExpression(), instanceOf(FromElementBinding.class));
    }

    private CriteriaBuilder getCriteriaBuilder() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("test");

        EntityManager em = factory.createEntityManager();

        return em.getCriteriaBuilder();
    }

    private SelectStatement interpret(CriteriaQuery criteriaQuery) {
        return (SelectStatement) SemanticQueryInterpreter.interpret(criteriaQuery, consumerContext);
    }
}
