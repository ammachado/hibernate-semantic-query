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

import javax.persistence.criteria.CriteriaBuilder.Trimspec;
import javax.persistence.criteria.Expression;
import java.io.Serializable;

/**
 * Models the ANSI SQL <tt>TRIM</tt> function.
 *
 * @author Steve Ebersole
 * @author Brett Meyer
 */
public class TrimFunction
        extends BasicFunctionExpression<String>
        implements Serializable {
    public static final String TRIM_NAME = "trim";
    public static final Trimspec DEFAULT_TRIMSPEC = Trimspec.BOTH;
    public static final char DEFAULT_TRIM_CHAR = ' ';

    private final Trimspec trimspec;
    private final Expression<Character> trimCharacter;
    private final Expression<String> trimSource;

    public TrimFunction(
            CriteriaBuilderImpl criteriaBuilder,
            Trimspec trimspec,
            Expression<Character> trimCharacter,
            Expression<String> trimSource) {
        super(criteriaBuilder, String.class, TRIM_NAME);
        this.trimspec = trimspec;
        this.trimCharacter = trimCharacter;
        this.trimSource = trimSource;
    }

    public TrimFunction(
            CriteriaBuilderImpl criteriaBuilder,
            Trimspec trimspec,
            char trimCharacter,
            Expression<String> trimSource) {
        super(criteriaBuilder, String.class, TRIM_NAME);
        this.trimspec = trimspec;
        this.trimCharacter = new LiteralExpression<Character>(criteriaBuilder, trimCharacter);
        this.trimSource = trimSource;
    }

    public TrimFunction(
            CriteriaBuilderImpl criteriaBuilder,
            Expression<String> trimSource) {
        this(criteriaBuilder, DEFAULT_TRIMSPEC, DEFAULT_TRIM_CHAR, trimSource);
    }

    public TrimFunction(
            CriteriaBuilderImpl criteriaBuilder,
            Expression<Character> trimCharacter,
            Expression<String> trimSource) {
        this(criteriaBuilder, DEFAULT_TRIMSPEC, trimCharacter, trimSource);
    }

    public TrimFunction(
            CriteriaBuilderImpl criteriaBuilder,
            char trimCharacter,
            Expression<String> trimSource) {
        this(criteriaBuilder, DEFAULT_TRIMSPEC, trimCharacter, trimSource);
    }

    public TrimFunction(
            CriteriaBuilderImpl criteriaBuilder,
            Trimspec trimspec,
            Expression<String> trimSource) {
        this(criteriaBuilder, trimspec, DEFAULT_TRIM_CHAR, trimSource);
    }

    public Expression<Character> getTrimCharacter() {
        return trimCharacter;
    }

    public Expression<String> getTrimSource() {
        return trimSource;
    }

    public Trimspec getTrimspec() {
        return trimspec;
    }

    @Override
    public org.hibernate.sqm.query.expression.Expression visitExpression(CriteriaVisitor visitor) {

        //TODO: Trimspec
        return visitor.visitFunction(
                TRIM_NAME,
                criteriaBuilder().consumerContext().getDomainMetamodel().getBasicType(getJavaType()),
                this.trimCharacter,
                this.trimSource

        );
    }
}
