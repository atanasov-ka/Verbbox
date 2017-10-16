package com.verbbox.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Verb.
 */
@Entity
@Table(name = "verb")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Verb implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "front", nullable = false)
    private String front;

    @NotNull
    @Column(name = "back", nullable = false)
    private String back;

    @Column(name = "created")
    private LocalDate created;

    @ManyToOne
    private Box box;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFront() {
        return front;
    }

    public Verb front(String front) {
        this.front = front;
        return this;
    }

    public void setFront(String front) {
        this.front = front;
    }

    public String getBack() {
        return back;
    }

    public Verb back(String back) {
        this.back = back;
        return this;
    }

    public void setBack(String back) {
        this.back = back;
    }

    public LocalDate getCreated() {
        return created;
    }

    public Verb created(LocalDate created) {
        this.created = created;
        return this;
    }

    public void setCreated(LocalDate created) {
        this.created = created;
    }

    public Box getBox() {
        return box;
    }

    public Verb box(Box box) {
        this.box = box;
        return this;
    }

    public void setBox(Box box) {
        this.box = box;
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
        Verb verb = (Verb) o;
        if (verb.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), verb.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Verb{" +
            "id=" + getId() +
            ", front='" + getFront() + "'" +
            ", back='" + getBack() + "'" +
            ", created='" + getCreated() + "'" +
            "}";
    }
}
