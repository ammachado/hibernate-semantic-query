/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: Apache License, Version 2.0
 * See the LICENSE file in the root directory or visit http://www.apache.org/licenses/LICENSE-2.0
 */
package org.hibernate.sqm.domain;

import javax.persistence.metamodel.EntityType;

/**
 * Models information about an entity
 *
 * @author Steve Ebersole
 */
public interface SQMEntityType<X> extends IdentifiableType, Bindable, EntityType<X> {
	/**
	 * Return the entity name.
	 *
	 * @return entity name
	 */
	String getName();
}
