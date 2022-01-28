package com.ensa.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TransactionType.
 */
@Entity
@Table(name = "transaction_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TransactionType implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "type")
    private String type;

    @Column(name = "plafond_transaction")
    private Double plafondTransaction;

    @Column(name = "plafond_annuel")
    private Double plafondAnnuel;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TransactionType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return this.type;
    }

    public TransactionType type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getPlafondTransaction() {
        return this.plafondTransaction;
    }

    public TransactionType plafondTransaction(Double plafondTransaction) {
        this.setPlafondTransaction(plafondTransaction);
        return this;
    }

    public void setPlafondTransaction(Double plafondTransaction) {
        this.plafondTransaction = plafondTransaction;
    }

    public Double getPlafondAnnuel() {
        return this.plafondAnnuel;
    }

    public TransactionType plafondAnnuel(Double plafondAnnuel) {
        this.setPlafondAnnuel(plafondAnnuel);
        return this;
    }

    public void setPlafondAnnuel(Double plafondAnnuel) {
        this.plafondAnnuel = plafondAnnuel;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TransactionType)) {
            return false;
        }
        return id != null && id.equals(((TransactionType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransactionType{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", plafondTransaction=" + getPlafondTransaction() +
            ", plafondAnnuel=" + getPlafondAnnuel() +
            "}";
    }
}
