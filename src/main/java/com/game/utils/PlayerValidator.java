package com.game.utils;

import com.game.entity.Player;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;

@Component
public class PlayerValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return Player.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Player player = (Player) o;

        if (Objects.isNull(player.getName()) || player.getName().isEmpty() || player.getName().length() > 12) {
            errors.reject("not valid name");
        }
        if (Objects.isNull(player.getTitle()) || player.getTitle().isEmpty() || player.getTitle().length() > 30) {
            errors.reject("not valid title");
        }
        if (Objects.isNull(player.getRace())) {
            errors.reject("race should not be null");
        }
        if (Objects.isNull(player.getProfession())) {
            errors.reject("profession should not be null");
        }
        if (Objects.isNull(player.getBirthday()) || player.getBirthday().getTime() < 0) {
            errors.reject("not valid birthday");
        }
        if (Objects.isNull(player.getExperience()) || player.getExperience() < 0 || player.getExperience() > 10_000_000) {
            errors.reject("not valid ");
        }
    }
}
