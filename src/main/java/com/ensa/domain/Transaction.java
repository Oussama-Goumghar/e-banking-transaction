package com.ensa.domain;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * A Transaction.
 */
@Entity
@Table(name = "transaction")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Transaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "reference")
    private String reference;

    @Column(name = "montant")
    private Double montant;

    @Column(name = "date_emission")
    private LocalDate dateEmission;

    @Column(name = "status")
    private String status;

    @Column(name = "pin")
    private Integer pin;

    @Column(name = "notify")
    private Boolean notify;

    @Column(name = "login_agent")
    private String loginAgent;

    @Column(name = "num_client")
    private String numClient;

    @Column(name = "nom_benificiair")
    private String nomBenificiair;

    @Column(name = "prenom_benificiair")
    private String prenomBenificiair;

    @Column(name = "telephone_benificiair")
    private String telephoneBenificiair;

    @ManyToOne
    private TransactionType transactionType;

    @ManyToOne
    private Motif motif;

    @ManyToOne
    private Frait frait;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Transaction id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReference() {
        return this.reference;
    }

    public Transaction reference(String reference) {
        this.setReference(reference);
        return this;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Double getMontant() {
        return this.montant;
    }

    public Transaction montant(Double montant) {
        this.setMontant(montant);
        return this;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public LocalDate getDateEmission() {
        return this.dateEmission;
    }

    public Transaction dateEmission(LocalDate dateEmission) {
        this.setDateEmission(dateEmission);
        return this;
    }

    public void setDateEmission(LocalDate dateEmission) {
        this.dateEmission = dateEmission;
    }

    public String getStatus() {
        return this.status;
    }

    public Transaction status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getPin() {
        return this.pin;
    }

    public Transaction pin(Integer pin) {
        this.setPin(pin);
        return this;
    }

    public void setPin(Integer pin) {
        this.pin = pin;
    }

    public Boolean getNotify() {
        return this.notify;
    }

    public Transaction notify(Boolean notify) {
        this.setNotify(notify);
        return this;
    }

    public void setNotify(Boolean notify) {
        this.notify = notify;
    }

    public String getLoginAgent() {
        return this.loginAgent;
    }

    public Transaction loginAgent(String loginAgent) {
        this.setLoginAgent(loginAgent);
        return this;
    }

    public void setLoginAgent(String loginAgent) {
        this.loginAgent = loginAgent;
    }

    public String getNumClient() {
        return this.numClient;
    }

    public Transaction numClient(String numClient) {
        this.setNumClient(numClient);
        return this;
    }

    public void setNumClient(String numClient) {
        this.numClient = numClient;
    }

    public String getNomBenificiair() {
        return this.nomBenificiair;
    }
    public Transaction nomBenificiair(String nomBenificiair) {
        this.setNomBenificiair(nomBenificiair);
        return this;
    }
    public void setNomBenificiair(String nomBenificiair) {
        this.nomBenificiair = nomBenificiair;
    }

    public String getPrenomBenificiair() {
        return this.prenomBenificiair;
    }
    public Transaction prenomBenificiair(String prenomBenificiair) {
        this.setPrenomBenificiair(prenomBenificiair);
        return this;
    }
    public void setPrenomBenificiair(String prenomBenificiair) {
        this.prenomBenificiair = prenomBenificiair;
    }

    public String getTelephoneBenificiair() {
        return this.telephoneBenificiair;
    }
    public Transaction telephoneBenificiair(String telephoneBenificiair) {
        this.setTelephoneBenificiair(telephoneBenificiair);
        return this;
    }
    public void setTelephoneBenificiair(String telephoneBenificiair) {
        this.telephoneBenificiair = telephoneBenificiair;
    }

    public TransactionType getTransactionType() {
        return this.transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public Transaction transactionType(TransactionType transactionType) {
        this.setTransactionType(transactionType);
        return this;
    }

    public Motif getMotif() {
        return this.motif;
    }

    public void setMotif(Motif motif) {
        this.motif = motif;
    }

    public Transaction motif(Motif motif) {
        this.setMotif(motif);
        return this;
    }

    public Frait getFrait() {
        return this.frait;
    }

    public void setFrait(Frait frait) {
        this.frait = frait;
    }

    public Transaction frait(Frait frait) {
        this.setFrait(frait);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Transaction)) {
            return false;
        }
        return id != null && id.equals(((Transaction) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Transaction{" +
            "id=" + getId() +
            ", reference='" + getReference() + "'" +
            ", montant=" + getMontant() +
            ", dateEmission='" + getDateEmission() + "'" +
            ", status='" + getStatus() + "'" +
            ", pin=" + getPin() +
            ", notify='" + getNotify() + "'" +
            ", loginAgent=" + getLoginAgent() +
            ", numClient=" + getNumClient() +
            ", nomBenificiair=" + getNomBenificiair() +
            ", prenomBenificiair=" + getPrenomBenificiair() +
            ", telephoneBenificiair=" + getTelephoneBenificiair() +
            "}";
    }
}
