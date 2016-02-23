package org.hibernate.test.query.parser.criteria.select;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by johara on 23/02/16.
 */
@Entity
public class Book {

    @Id
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
