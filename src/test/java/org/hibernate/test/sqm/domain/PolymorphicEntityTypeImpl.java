/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: Apache License, Version 2.0
 * See the LICENSE file in the root directory or visit http://www.apache.org/licenses/LICENSE-2.0
 */
package org.hibernate.test.sqm.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.sqm.domain.Attribute;
import org.hibernate.sqm.domain.SQMEntityType;
import org.hibernate.sqm.domain.PolymorphicEntityType;

/**
 * @author Steve Ebersole
 */
public class PolymorphicEntityTypeImpl extends EntityTypeImpl implements PolymorphicEntityType {
	private List<SQMEntityType> implementors = new ArrayList<SQMEntityType>();

	public PolymorphicEntityTypeImpl(Class javaType) {
		super( javaType, null );
	}

	public PolymorphicEntityTypeImpl(String name) {
		super( name, null );
	}

	@Override
	public Collection<SQMEntityType> getImplementors() {
		return implementors;
	}

	public void addImplementor(EntityTypeImpl implementor) {
		implementors.add( implementor );
	}

	public void buildAttributes() {
		// The attribute must exist in all of them.  So we use the first implementor as a driver
		// and collect any attributes that exist across all implementors

		EntityTypeImpl firstImplementor = (EntityTypeImpl) implementors.get( 0 );
		attr_loop:
		for ( Attribute attribute : firstImplementor.getAttributesByName().values() ) {
			for ( SQMEntityType implementor : implementors ) {
				// NOTE : according to JPA the lookup here should throw an exception rather than return null
				// so we either need to adjust this expectation (in main code too) or go back to our own model
				if ( implementor.findAttribute( attribute.getName() ) == null ) {
					break attr_loop;
				}
			}

			super.addAttribute( attribute );
		}
	}
}
