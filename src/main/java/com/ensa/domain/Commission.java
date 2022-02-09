package com.ensa.domain;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Commission.
 */
@Entity
@Table(name = "commission")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Commission implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "id_agent")
    private Long idAgent;

    @Column(name = "date_retrait")
    private LocalDate dateRetrait;

    @Column(name = "value")
    private Double value;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Commission id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdAgent() {
        return this.idAgent;
    }

    public Commission idAgent(Long idAgent) {
        this.setIdAgent(idAgent);
        return this;
    }

    public void setIdAgent(Long idAgent) {
        this.idAgent = idAgent;
    }

    public LocalDate getDateRetrait() {
        return this.dateRetrait;
    }

    public Commission dateRetrait(LocalDate dateRetrait) {
        this.setDateRetrait(dateRetrait);
        return this;
    }

    public void setDateRetrait(LocalDate dateRetrait) {
        this.dateRetrait = dateRetrait;
    }

    public Double getValue() {
        return this.value;
    }

    public Commission value(Double value) {
        this.setValue(value);
        return this;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Commission)) {
            return false;
        }
        return id != null && id.equals(((Commission) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Commission{" +
            "id=" + getId() +
            ", idAgent=" + getIdAgent() +
            ", dateRetrait='" + getDateRetrait() + "'" +
            ", value=" + getValue() +
            "}";
    }
}
