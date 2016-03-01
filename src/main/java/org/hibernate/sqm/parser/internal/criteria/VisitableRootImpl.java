package org.hibernate.sqm.parser.internal.criteria;

import org.hibernate.jpa.criteria.path.RootImpl;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Selection;
import java.util.Collection;
import java.util.List;

/**
 * Created by johara on 01/03/16.
 */
public class VisitableRootImpl<X> implements VisitableExpressionImplementor<X> {

    final RootImpl<X> rootImpl;

    public VisitableRootImpl(RootImpl<X> rootImpl) {
        this.rootImpl = rootImpl;
    }

    @Override
    public org.hibernate.sqm.query.expression.Expression accept(CriteriaVisitor visitor) {
        return null;
    }

    public RootImpl<X> getRootImpl() {
        return rootImpl;
    }

    @Override
    public Expression as(Class type) {
        return rootImpl.as(type);
    }

    @Override
    public Predicate isNull() {
        return rootImpl.isNull();
    }

    @Override
    public Predicate isNotNull() {
        return rootImpl.isNotNull();
    }

    @Override
    public Predicate in(Object... values) {
        return rootImpl.in(values);
    }

    @Override
    public Predicate in(Expression<?>... values) {
        return rootImpl.in(values);
    }

    @Override
    public Predicate in(Collection<?> values) {
        return rootImpl.in(values);
    }

    @Override
    public Predicate in(Expression<Collection<?>> values) {
        return rootImpl.in(values);
    }

    @Override
    public Selection<X> alias(String name) {
        return rootImpl.alias(name);
    }

    @Override
    public boolean isCompoundSelection() {
        return rootImpl.isCompoundSelection();
    }

    @Override
    public List<Selection<?>> getCompoundSelectionItems() {
        return rootImpl.getCompoundSelectionItems();
    }

    @Override
    public Class<? extends X> getJavaType() {
        return rootImpl.getJavaType();
    }

    @Override
    public String getAlias() {
        return rootImpl.getAlias();
    }
}
