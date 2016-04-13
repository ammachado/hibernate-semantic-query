/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.test.sqm.parser.criteria.tree.expression;


import org.hibernate.sqm.NotYetImplementedException;
import org.hibernate.sqm.parser.criteria.spi.CriteriaVisitor;
import org.hibernate.sqm.query.expression.Expression;
import org.hibernate.sqm.query.select.AliasedExpressionContainer;
import org.hibernate.test.sqm.parser.criteria.tree.CriteriaBuilderImpl;

import java.io.Serializable;

/**
 * TODO : javadoc
 *
 * @author Steve Ebersole
 */
public class EntityTypeExpression<T> extends ExpressionImpl<T> implements Serializable {
    private final String identificationVariable;
    private final String attributeName;

    public EntityTypeExpression(CriteriaBuilderImpl criteriaBuilder, Class<T> javaType, String identificationVariable, String attributeName) {
        super(criteriaBuilder, javaType);
        this.identificationVariable = identificationVariable;
        this.attributeName = attributeName;
    }

    @Override
    public Expression visitExpression(CriteriaVisitor visitor) {
        if (this.attributeName != null) {
            return visitor.visitEntityType(this.identificationVariable, this.attributeName);
        } else {
            return visitor.visitEntityType(this.identificationVariable);
        }
    }

    @Override
    public void visitSelections(CriteriaVisitor visitor, AliasedExpressionContainer container) {
        throw new NotYetImplementedException();
    }
}
