package com.verbbox.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A VerbHistory.
 */
@Entity
@Table(name = "verb_history")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class VerbHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "front_back_yes")
    private Integer frontBackYes;

    @Column(name = "back_front_yes")
    private Integer backFrontYes;

    @Column(name = "front_back_no")
    private Integer frontBackNo;

    @Column(name = "back_front_no")
    private Integer backFrontNo;

    @ManyToOne
    private Play user;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getFrontBackYes() {
        return frontBackYes;
    }

    public VerbHistory frontBackYes(Integer frontBackYes) {
        this.frontBackYes = frontBackYes;
        return this;
    }

    public void setFrontBackYes(Integer frontBackYes) {
        this.frontBackYes = frontBackYes;
    }

    public Integer getBackFrontYes() {
        return backFrontYes;
    }

    public VerbHistory backFrontYes(Integer backFrontYes) {
        this.backFrontYes = backFrontYes;
        return this;
    }

    public void setBackFrontYes(Integer backFrontYes) {
        this.backFrontYes = backFrontYes;
    }

    public Integer getFrontBackNo() {
        return frontBackNo;
    }

    public VerbHistory frontBackNo(Integer frontBackNo) {
        this.frontBackNo = frontBackNo;
        return this;
    }

    public void setFrontBackNo(Integer frontBackNo) {
        this.frontBackNo = frontBackNo;
    }

    public Integer getBackFrontNo() {
        return backFrontNo;
    }

    public VerbHistory backFrontNo(Integer backFrontNo) {
        this.backFrontNo = backFrontNo;
        return this;
    }

    public void setBackFrontNo(Integer backFrontNo) {
        this.backFrontNo = backFrontNo;
    }

    public Play getUser() {
        return user;
    }

    public VerbHistory user(Play play) {
        this.user = play;
        return this;
    }

    public void setUser(Play play) {
        this.user = play;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        VerbHistory verbHistory = (VerbHistory) o;
        if (verbHistory.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), verbHistory.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "VerbHistory{" +
            "id=" + getId() +
            ", frontBackYes='" + getFrontBackYes() + "'" +
            ", backFrontYes='" + getBackFrontYes() + "'" +
            ", frontBackNo='" + getFrontBackNo() + "'" +
            ", backFrontNo='" + getBackFrontNo() + "'" +
            "}";
    }
}
