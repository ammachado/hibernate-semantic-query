/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.test.sqm.parser.criteria.tree.expression;

import org.hibernate.sqm.NotYetImplementedException;
import org.hibernate.sqm.parser.criteria.spi.CriteriaVisitor;
import org.hibernate.sqm.query.expression.UnaryOperationExpression;
import org.hibernate.sqm.query.select.AliasedExpressionContainer;
import org.hibernate.test.sqm.parser.criteria.tree.CriteriaBuilderImpl;

import javax.persistence.criteria.Expression;
import java.io.Serializable;


/**
 * Models unary arithmetic operation (unary plus and unary minus).
 *
 * @author Steve Ebersole
 */
public class UnaryArithmeticOperation<T> 
		extends ExpressionImpl<T>
		implements UnaryOperatorExpression<T>, Serializable {

	public static enum Operation {
		UNARY_PLUS, UNARY_MINUS
	}

	private final Operation operation;
	private final Expression<T> operand;

	@SuppressWarnings({ "unchecked" })
	public UnaryArithmeticOperation(
			CriteriaBuilderImpl criteriaBuilder,
			Operation operation,
			Expression<T> operand) {
		super( criteriaBuilder, (Class)operand.getJavaType() );
		this.operation = operation;
		this.operand = operand;
	}

	public Operation getOperation() {
		return operation;
	}

	@Override
	public Expression<T> getOperand() {
		return operand;
	}

	@Override
	public org.hibernate.sqm.query.expression.Expression visitExpression(CriteriaVisitor visitor) {
		return visitor.visitUnaryOperation(getMappedOperation(this.operation), this.getOperand());
	}

	@Override
	public void visitSelections(CriteriaVisitor visitor, AliasedExpressionContainer container) {
		throw new NotYetImplementedException();
	}

	private UnaryOperationExpression.Operation getMappedOperation(Operation operation){
		switch( operation) {
			case UNARY_MINUS:
				return UnaryOperationExpression.Operation.MINUS;
			case UNARY_PLUS:
				return UnaryOperationExpression.Operation.PLUS;
			default:
				return null;
		}

	}

}
