package org.hibernate.test.query.parser.criteria.select;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by johara on 09/03/16.
 */
@Entity
public class Country {
    @Id
    private long id;

    private String currency;

    private Long population;

    public Country() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Long getPopulation() {
        return population;
    }

    public void setPopulation(Long population) {
        this.population = population;
    }
}
