package com.verbbox.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Box.
 */
@Entity
@Table(name = "box")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Box implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(min = 1)
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Size(min = 2)
    @Column(name = "first_language", nullable = false)
    private String firstLanguage;

    @NotNull
    @Size(min = 2)
    @Column(name = "second_language", nullable = false)
    private String secondLanguage;

    @Column(name = "description")
    private String description;

    @Column(name = "created")
    private ZonedDateTime created;

    @OneToOne
    @JoinColumn(unique = true)
    private Circle sharedWith;

    @OneToMany(mappedBy = "box")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Verb> verbs = new HashSet<>();

    @OneToMany(mappedBy = "box")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Play> plays = new HashSet<>();

    @ManyToOne
    private User owner;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Box name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstLanguage() {
        return firstLanguage;
    }

    public Box firstLanguage(String firstLanguage) {
        this.firstLanguage = firstLanguage;
        return this;
    }

    public void setFirstLanguage(String firstLanguage) {
        this.firstLanguage = firstLanguage;
    }

    public String getSecondLanguage() {
        return secondLanguage;
    }

    public Box secondLanguage(String secondLanguage) {
        this.secondLanguage = secondLanguage;
        return this;
    }

    public void setSecondLanguage(String secondLanguage) {
        this.secondLanguage = secondLanguage;
    }

    public String getDescription() {
        return description;
    }

    public Box description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public Box created(ZonedDateTime created) {
        this.created = created;
        return this;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public Circle getSharedWith() {
        return sharedWith;
    }

    public Box sharedWith(Circle circle) {
        this.sharedWith = circle;
        return this;
    }

    public void setSharedWith(Circle circle) {
        this.sharedWith = circle;
    }

    public Set<Verb> getVerbs() {
        return verbs;
    }

    public Box verbs(Set<Verb> verbs) {
        this.verbs = verbs;
        return this;
    }

    public Box addVerb(Verb verb) {
        this.verbs.add(verb);
        verb.setBox(this);
        return this;
    }

    public Box removeVerb(Verb verb) {
        this.verbs.remove(verb);
        verb.setBox(null);
        return this;
    }

    public void setVerbs(Set<Verb> verbs) {
        this.verbs = verbs;
    }

    public Set<Play> getPlays() {
        return plays;
    }

    public Box plays(Set<Play> plays) {
        this.plays = plays;
        return this;
    }

    public Box addPlay(Play play) {
        this.plays.add(play);
        play.setBox(this);
        return this;
    }

    public Box removePlay(Play play) {
        this.plays.remove(play);
        play.setBox(null);
        return this;
    }

    public void setPlays(Set<Play> plays) {
        this.plays = plays;
    }

    public User getOwner() {
        return owner;
    }

    public Box owner(User user) {
        this.owner = user;
        return this;
    }

    public void setOwner(User user) {
        this.owner = user;
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
        Box box = (Box) o;
        if (box.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), box.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Box{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", firstLanguage='" + getFirstLanguage() + "'" +
            ", secondLanguage='" + getSecondLanguage() + "'" +
            ", description='" + getDescription() + "'" +
            ", created='" + getCreated() + "'" +
            "}";
    }
}
