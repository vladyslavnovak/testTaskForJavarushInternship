package com.game.controller;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.PlayerService;
import com.game.utils.InvalidRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest")
public class PlayerController {
    private PlayerService playerService;


    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/players")
    public List<Player> getAllPlayers(@RequestParam(required = false) String name,
                                      @RequestParam(required = false) String title,
                                      @RequestParam(required = false) Race race,
                                      @RequestParam(required = false) Profession profession,
                                      @RequestParam(required = false) Long after,
                                      @RequestParam(required = false) Long before,
                                      @RequestParam(required = false) Boolean banned,
                                      @RequestParam(required = false) Integer minExperience,
                                      @RequestParam(required = false) Integer maxExperience,
                                      @RequestParam(required = false) Integer minLevel,
                                      @RequestParam(required = false) Integer maxLevel,
                                      @RequestParam(required = false) PlayerOrder order,
                                      @RequestParam(required = false) Integer pageNumber,
                                      @RequestParam(required = false) Integer pageSize) {
        return playerService.getPlayersWithStreams(name, title,
                race, profession,
                after, before, banned,
                minExperience, maxExperience,
                minLevel, maxLevel, pageNumber, pageSize, order);
        /*return new ResponseEntity<>(playerService.getPlayers(name, title,
                race, profession,
                after, before, banned,
                minExperience, maxExperience,
                minLevel, maxLevel, pageNumber, pageSize, order), HttpStatus.OK);*/
    }

    @GetMapping("/players/count")
    public Integer getCount(@RequestParam(required = false) String name,
                            @RequestParam(required = false) String title,
                            @RequestParam(required = false) Race race,
                            @RequestParam(required = false) Profession profession,
                            @RequestParam(required = false) Long after,
                            @RequestParam(required = false) Long before,
                            @RequestParam(required = false) Boolean banned,
                            @RequestParam(required = false) Integer minExperience,
                            @RequestParam(required = false) Integer maxExperience,
                            @RequestParam(required = false) Integer minLevel,
                            @RequestParam(required = false) Integer maxLevel) {
        return playerService.getCount(name, title,
                race, profession,
                after, before, banned,
                minExperience, maxExperience,
                minLevel, maxLevel);
    }

    @PostMapping("/players")
    public ResponseEntity<Player> create(@RequestBody Player player, BindingResult bindingResult) {
        return new ResponseEntity<>(playerService.save(player, bindingResult), HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<HttpStatus> exceptionHandler(InvalidRequestException e) {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
