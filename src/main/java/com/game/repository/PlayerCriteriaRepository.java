package com.game.repository;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Repository
public class PlayerCriteriaRepository {
    private EntityManager entityManager;
    private CriteriaBuilder criteriaBuilder;

    public PlayerCriteriaRepository(LocalContainerEntityManagerFactoryBean entityManager) {
        this.entityManager = entityManager.getNativeEntityManagerFactory().createEntityManager();
        this.criteriaBuilder = this.entityManager.getCriteriaBuilder();
    }

    public List<Player> findAllWithFilters(String name, String title,
                                           Race race, Profession profession,
                                           Long after, Long before, Boolean banned,
                                           Integer minExperience, Integer maxExperience,
                                           Integer minLevel, Integer maxLevel) {

        CriteriaQuery<Player> criteriaQuery = criteriaBuilder.createQuery(Player.class);
        Root<Player> playerRoot = criteriaQuery.from(Player.class);
        Predicate predicate = getPredicate(name, title, race, profession, after, before,
                banned, minExperience, maxExperience, minLevel, maxLevel, playerRoot);
        criteriaQuery.where(predicate);
        TypedQuery<Player> typedQuery = entityManager.createQuery(criteriaQuery);
        return typedQuery.getResultList();
    }

    private Predicate getPredicate(String name, String title, Race race, Profession profession,
                                   Long after, Long before, Boolean banned,
                                   Integer minExperience, Integer maxExperience,
                                   Integer minLevel, Integer maxLevel, Root<Player> playerRoot) {

        List<Predicate> predicates = new ArrayList<>();

        if (Objects.nonNull(name)) {
            predicates.add(criteriaBuilder.like(playerRoot.get("name"), "%" + name + "%"));
        }
        if (Objects.nonNull(title)) {
            predicates.add(criteriaBuilder.like(playerRoot.get("title"), "%" + title + "%"));
        }
        if (Objects.nonNull(race)) {
            predicates.add(criteriaBuilder.equal(playerRoot.get("race"), race));
        }
        if (Objects.nonNull(profession)) {
            predicates.add(criteriaBuilder.equal(playerRoot.get("profession"), profession));
        }
        if (Objects.nonNull(after)) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(playerRoot.get("birthday"), new Date(after)));
        }
        if (Objects.nonNull(before)) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(playerRoot.get("birthday"), new Date(before)));
        }
        if (Objects.nonNull(banned)) {
            predicates.add(criteriaBuilder.equal(playerRoot.get("banned"), banned));
        }
        if (Objects.nonNull(minExperience)) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(playerRoot.get("experience"), minExperience));
        }
        if (Objects.nonNull(maxExperience)) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(playerRoot.get("experience"), maxExperience));
        }
        if (Objects.nonNull(minLevel)) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(playerRoot.get("level"), minLevel));
        }
        if (Objects.nonNull(maxLevel)) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(playerRoot.get("level"), maxLevel));
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}