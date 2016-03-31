/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.test.sqm.parser.criteria.tree.expression;

import org.hibernate.test.sqm.parser.criteria.tree.CriteriaBuilderImpl;

/**
 * @author Andrea Boriero
 */
public class CaseLiteralExpression<X> extends LiteralExpression<X> {

    public CaseLiteralExpression(CriteriaBuilderImpl criteriaBuilder,
                                 Class<X> javaType, X literal) {
        super(criteriaBuilder, javaType, literal);
    }
}


