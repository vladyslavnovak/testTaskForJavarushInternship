package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.repository.PlayerCriteriaRepository;
import com.game.repository.PlayerRepository;
import com.game.utils.PlayerValidator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PlayerService {
    final private PlayerRepository playerRepository;
    final private PlayerCriteriaRepository criteriaRepository;
    final private PlayerValidator playerValidator;

    public PlayerService(PlayerRepository playerRepository, PlayerCriteriaRepository criteriaRepository, PlayerValidator playerValidator) {
        this.playerRepository = playerRepository;
        this.criteriaRepository = criteriaRepository;
        this.playerValidator = playerValidator;
    }

    /*public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }*/

    /*public List<Player> filterBySpec(String name, String title,
                                     Race race, Profession profession,
                                     Long after, Long before, Boolean banned,
                                     Integer minExperience, Integer maxExperience,
                                     Integer minLevel, Integer maxLevel, Integer pageNumber, Integer pageSize, PlayerOrder order) {


        return null;

    }*/

    public List<Player> getPlayers(String name, String title,
                                   Race race, Profession profession,
                                   Long after, Long before, Boolean banned,
                                   Integer minExperience, Integer maxExperience,
                                   Integer minLevel, Integer maxLevel, Integer pageNumber, Integer pageSize, PlayerOrder order) {
        return criteriaRepository.findAllWithFilters(name, title, race, profession, after, before, banned,
                minExperience, maxExperience, minLevel, maxLevel, pageNumber, pageSize, order);
    }

    public List<Player> getPlayersWithStreams(String name, String title,
                                              Race race, Profession profession,
                                              Long after, Long before, Boolean banned,
                                              Integer minExperience, Integer maxExperience,
                                              Integer minLevel, Integer maxLevel, Integer pageNumber, Integer pageSize, PlayerOrder order) {
        List<Player> players = playerRepository.findAll();

        if (Objects.nonNull(name)) {
            players = players.stream().filter(player -> player.getName().contains(name)).collect(Collectors.toList());
        }
        if (Objects.nonNull(title)) {
            players = players.stream().filter(player -> player.getTitle().contains(title)).collect(Collectors.toList());
        }
        if (Objects.nonNull(race)) {
            players = players.stream().filter(player -> player.getRace().equals(race)).collect(Collectors.toList());
        }
        if (Objects.nonNull(profession)) {
            players = players.stream().filter(player -> player.getProfession().equals(profession)).collect(Collectors.toList());
        }
        if (Objects.nonNull(after)) {
            players = players.stream().filter(player -> player.getBirthday().after(new Date(after))).collect(Collectors.toList());
        }
        if (Objects.nonNull(before)) {
            players = players.stream().filter(player -> player.getBirthday().before(new Date(before))).collect(Collectors.toList());
        }
        if (Objects.nonNull(banned)) {
            players = players.stream().filter(player -> player.getBanned().equals(banned)).collect(Collectors.toList());
        }
        if (Objects.nonNull(minExperience)) {
            players = players.stream().filter(player -> player.getExperience() >= minExperience).collect(Collectors.toList());
        }
        if (Objects.nonNull(maxExperience)) {
            players = players.stream().filter(player -> player.getExperience() <= maxExperience).collect(Collectors.toList());
        }
        if (Objects.nonNull(minLevel)) {
            players = players.stream().filter(player -> player.getLevel() >= minLevel).collect(Collectors.toList());
        }
        if (Objects.nonNull(maxLevel)) {
            players = players.stream().filter(player -> player.getLevel() <= maxLevel).collect(Collectors.toList());
        }

        if (Objects.nonNull(order)) {
            players.sort((player1, player2) -> {
                switch (order) {
                    case ID:
                        player1.getId().compareTo(player2.getId());
                    case NAME:
                        player1.getName().compareTo(player2.getName());
                    case LEVEL:
                        player1.getLevel().compareTo(player2.getLevel());
                    case BIRTHDAY:
                        player1.getBirthday().compareTo(player2.getBirthday());
                    case EXPERIENCE:
                        player1.getExperience().compareTo(player2.getExperience());
                    default:
                        return 0;
                }
            });
        }

        Integer page = pageNumber == null ? 0 : pageNumber;
        Integer size = pageSize == null ? 3 : pageSize;

        int from = page * size;
        int to = from + size;

        if (to > players.size()) {
            to = players.size();
        }
        players = players.subList(from, to);
        return players;
    }

    public Integer getCount(String name, String title,
                            Race race, Profession profession,
                            Long after, Long before, Boolean banned,
                            Integer minExperience, Integer maxExperience,
                            Integer minLevel, Integer maxLevel) {
        List<Player> players = playerRepository.findAll();

        if (Objects.nonNull(name)) {
            players = players.stream().filter(player -> player.getName().contains(name)).collect(Collectors.toList());
        }
        if (Objects.nonNull(title)) {
            players = players.stream().filter(player -> player.getTitle().contains(title)).collect(Collectors.toList());
        }
        if (Objects.nonNull(race)) {
            players = players.stream().filter(player -> player.getRace().equals(race)).collect(Collectors.toList());
        }
        if (Objects.nonNull(profession)) {
            players = players.stream().filter(player -> player.getProfession().equals(profession)).collect(Collectors.toList());
        }
        if (Objects.nonNull(after)) {
            players = players.stream().filter(player -> player.getBirthday().after(new Date(after))).collect(Collectors.toList());
        }
        if (Objects.nonNull(before)) {
            players = players.stream().filter(player -> player.getBirthday().before(new Date(before))).collect(Collectors.toList());
        }
        if (Objects.nonNull(banned)) {
            players = players.stream().filter(player -> player.getBanned().equals(banned)).collect(Collectors.toList());
        }
        if (Objects.nonNull(minExperience)) {
            players = players.stream().filter(player -> player.getExperience() >= minExperience).collect(Collectors.toList());
        }
        if (Objects.nonNull(maxExperience)) {
            players = players.stream().filter(player -> player.getExperience() <= maxExperience).collect(Collectors.toList());
        }
        if (Objects.nonNull(minLevel)) {
            players = players.stream().filter(player -> player.getLevel() >= minLevel).collect(Collectors.toList());
        }
        if (Objects.nonNull(maxLevel)) {
            players = players.stream().filter(player -> player.getLevel() <= maxLevel).collect(Collectors.toList());
        }
        return players.size();
    }

    @Transactional
    public Player save(Player player, BindingResult bindingResult) {
        playerValidator.validate(player, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (Objects.isNull(player.getBanned())) {
            player.setBanned(false);
        }
        if (Objects.nonNull(player.getId())) {
            player.setId(null);
        }
        return playerRepository.save(expAndLevelCalculations(player));
    }

    private Player expAndLevelCalculations(Player player) {
        Integer currLevel = (int) ((Math.sqrt(2500 + 200 * player.getExperience()) - 50) / 100);
        Integer untilNextLevel = (50 * (currLevel + 1) * (currLevel + 2)) - player.getExperience();
        player.setLevel(currLevel);
        player.setUntilNextLevel(untilNextLevel);
        return player;
    }

    public Player getPlayerById(Integer id) {
        if (id < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Optional<Player> playerOptional = playerRepository.findById(id.longValue());
        if (!playerOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return playerOptional.get();
    }
}
