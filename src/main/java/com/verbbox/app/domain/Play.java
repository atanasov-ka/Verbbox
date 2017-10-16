package com.verbbox.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Play.
 */
@Entity
@Table(name = "play")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Play implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "progress")
    private Integer progress;

    @Column(name = "last_activiry")
    private LocalDate lastActiviry;

    @ManyToOne
    private Box box;

    @ManyToOne
    private User player;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getProgress() {
        return progress;
    }

    public Play progress(Integer progress) {
        this.progress = progress;
        return this;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public LocalDate getLastActiviry() {
        return lastActiviry;
    }

    public Play lastActiviry(LocalDate lastActiviry) {
        this.lastActiviry = lastActiviry;
        return this;
    }

    public void setLastActiviry(LocalDate lastActiviry) {
        this.lastActiviry = lastActiviry;
    }

    public Box getBox() {
        return box;
    }

    public Play box(Box box) {
        this.box = box;
        return this;
    }

    public void setBox(Box box) {
        this.box = box;
    }

    public User getPlayer() {
        return player;
    }

    public Play player(User user) {
        this.player = user;
        return this;
    }

    public void setPlayer(User user) {
        this.player = user;
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
        Play play = (Play) o;
        if (play.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), play.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Play{" +
            "id=" + getId() +
            ", progress='" + getProgress() + "'" +
            ", lastActiviry='" + getLastActiviry() + "'" +
            "}";
    }
}
