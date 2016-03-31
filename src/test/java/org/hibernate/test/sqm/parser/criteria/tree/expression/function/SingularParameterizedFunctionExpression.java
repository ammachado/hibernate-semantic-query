/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.test.sqm.parser.criteria.tree.expression.function;

import org.hibernate.test.sqm.parser.criteria.tree.CriteriaBuilderImpl;

import javax.persistence.criteria.Expression;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Support for functions with parameters.
 *
 * @author Steve Ebersole
 */
public abstract class SingularParameterizedFunctionExpression<X>
        extends ParameterizedFunctionExpression<X> {

    private final Expression<?> argumentExpression;

    public SingularParameterizedFunctionExpression(
            CriteriaBuilderImpl criteriaBuilder,
            Class<X> javaType,
            String functionName,
            Expression<?> argumentExpression) {
        super(criteriaBuilder, javaType, functionName);
        this.argumentExpression = argumentExpression;
    }

    public Expression<?> getArgumentExpression() {
        return argumentExpression;
    }

}
