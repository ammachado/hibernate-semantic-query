/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.test.sqm.parser.criteria.tree.expression.function;


import org.hibernate.sqm.parser.criteria.spi.CriteriaVisitor;
import org.hibernate.test.sqm.parser.criteria.tree.CriteriaBuilderImpl;
import org.hibernate.test.sqm.parser.criteria.tree.expression.LiteralExpression;

import javax.persistence.criteria.Expression;
import java.io.Serializable;

/**
 * Models the ANSI SQL <tt>SUBSTRING</tt> function.
 *
 * @author Steve Ebersole
 */
public class SubstringFunction
        extends BasicFunctionExpression<String>
        implements Serializable {
    public static final String SUBSTRING_NAME = "substring";

    private final Expression<String> value;
    private final Expression<Integer> start;
    private final Expression<Integer> length;

    public SubstringFunction(
            CriteriaBuilderImpl criteriaBuilder,
            Expression<String> value,
            Expression<Integer> start,
            Expression<Integer> length) {
        super(criteriaBuilder, String.class, SUBSTRING_NAME);
        this.value = value;
        this.start = start;
        this.length = length;
    }

    @SuppressWarnings({"RedundantCast"})
    public SubstringFunction(
            CriteriaBuilderImpl criteriaBuilder,
            Expression<String> value,
            Expression<Integer> start) {
        this(criteriaBuilder, value, start, (Expression<Integer>) null);
    }

    public SubstringFunction(
            CriteriaBuilderImpl criteriaBuilder,
            Expression<String> value,
            int start) {
        this(
                criteriaBuilder,
                value,
                new LiteralExpression<Integer>(criteriaBuilder, start)
        );
    }

    public SubstringFunction(
            CriteriaBuilderImpl criteriaBuilder,
            Expression<String> value,
            int start,
            int length) {
        this(
                criteriaBuilder,
                value,
                new LiteralExpression<Integer>(criteriaBuilder, start),
                new LiteralExpression<Integer>(criteriaBuilder, length)
        );
    }

    public Expression<Integer> getLength() {
        return length;
    }

    public Expression<Integer> getStart() {
        return start;
    }

    public Expression<String> getValue() {
        return value;
    }

    @Override
    public org.hibernate.sqm.query.expression.Expression visitExpression(CriteriaVisitor visitor) {
        return visitor.visitFunction(
                SUBSTRING_NAME,
                criteriaBuilder().consumerContext().getDomainMetamodel().getBasicType(getJavaType()),
                this.value,
                this.start,
                this.length

        );
    }
}
