package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.repository.PlayerCriteriaRepository;
import com.game.repository.PlayerRepository;
import com.game.utils.PlayerValidator;
import com.game.utils.UpdatedPlayerValidator;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@Service
public class PlayerService {
    final private PlayerRepository playerRepository;
    final private PlayerCriteriaRepository criteriaRepository;
    final private PlayerValidator playerValidator;
    final private UpdatedPlayerValidator updatedPlayerValidator;

    public PlayerService(PlayerRepository playerRepository, PlayerCriteriaRepository criteriaRepository,
                         PlayerValidator playerValidator, UpdatedPlayerValidator updatedPlayerValidator) {
        this.playerRepository = playerRepository;
        this.criteriaRepository = criteriaRepository;
        this.playerValidator = playerValidator;
        this.updatedPlayerValidator = updatedPlayerValidator;
    }

    public List<Player> findAllPlayersWithFilters(String name, String title,
                                                  Race race, Profession profession,
                                                  Long after, Long before, Boolean banned,
                                                  Integer minExperience, Integer maxExperience,
                                                  Integer minLevel, Integer maxLevel,
                                                  Integer pageNumber, Integer pageSize, PlayerOrder order) {

        List<Player> players = criteriaRepository.findAllWithFilters(
                name, title, race, profession, after, before, banned,
                minExperience, maxExperience, minLevel, maxLevel);

        PagedListHolder<Player> playerPagedListHolder = new PagedListHolder<>(players,
                new MutableSortDefinition(order.getFieldName(), true, true));
        playerPagedListHolder.setPage(pageNumber);
        playerPagedListHolder.setPageSize(pageSize);

        return playerPagedListHolder.getPageList();
    }

    public Integer getCount(String name, String title,
                            Race race, Profession profession,
                            Long after, Long before, Boolean banned,
                            Integer minExperience, Integer maxExperience,
                            Integer minLevel, Integer maxLevel) {
        return criteriaRepository.findAllWithFilters(name, title, race, profession, after, before, banned,
                minExperience, maxExperience, minLevel, maxLevel).size();
    }

    public Player save(Player player, BindingResult bindingResult) {
        playerValidator.validate(player, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (Objects.isNull(player.getBanned())) {
            player.setBanned(false);
        }
        return playerRepository.save(experienceAndLevelCalculations(player));
    }

    private Player experienceAndLevelCalculations(Player player) {
        int currLevel = (int) ((Math.sqrt(2500 + 200 * player.getExperience()) - 50) / 100);
        int untilNextLevel = (50 * (currLevel + 1) * (currLevel + 2)) - player.getExperience();
        player.setLevel(currLevel);
        player.setUntilNextLevel(untilNextLevel);
        return player;
    }

    public Player getPlayerById(Long id) {
        if (id < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return playerRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Player updatePlayer(Long id, Player updatedPlayer, BindingResult bindingResult) {

        Player player = getPlayerById(id);

        if (updatedPlayerValidator.playerIsEmpty(updatedPlayer)) {
            return player;
        }

        updatedPlayerValidator.validate(updatedPlayer, bindingResult);

        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        if (Objects.nonNull(updatedPlayer.getName())) {
            player.setName(updatedPlayer.getName());
        }
        if (Objects.nonNull(updatedPlayer.getTitle())) {
            player.setTitle(updatedPlayer.getTitle());
        }
        if (Objects.nonNull(updatedPlayer.getRace())) {
            player.setRace(updatedPlayer.getRace());
        }
        if (Objects.nonNull(updatedPlayer.getProfession())) {
            player.setProfession(updatedPlayer.getProfession());
        }
        if (Objects.nonNull(updatedPlayer.getBirthday())) {
            player.setBirthday(updatedPlayer.getBirthday());
        }
        if (Objects.nonNull(updatedPlayer.getBanned())) {
            player.setBanned(updatedPlayer.getBanned());
        }
        if (Objects.nonNull(updatedPlayer.getExperience())) {
            player.setExperience(updatedPlayer.getExperience());
        }

        experienceAndLevelCalculations(player);

        return playerRepository.save(player);
    }

    public void delete(Long id) {
        getPlayerById(id);
        playerRepository.deleteById(id);
    }
}
