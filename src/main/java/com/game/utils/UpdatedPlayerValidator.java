package com.game.utils;

import com.game.entity.Player;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;
import java.util.stream.Stream;

@Component
public class UpdatedPlayerValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return Player.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Player player = (Player) o;

        if (Objects.nonNull(player.getId()) && player.getId() < 1) {
            errors.reject("not valid id");
        }

        if (Objects.nonNull(player.getName()) && (player.getName().isEmpty() || player.getName().length() > 12)) {
            errors.reject("not valid name");
        }
        if (Objects.nonNull(player.getTitle()) && (player.getTitle().isEmpty() || player.getTitle().length() > 30)) {
            errors.reject("not valid title");
        }
        if (Objects.nonNull(player.getBirthday()) && player.getBirthday().getTime() < 0) {
            errors.reject("not valid birthday");
        }
        if (Objects.nonNull(player.getExperience()) && (player.getExperience() < 0 || player.getExperience() > 10_000_000)) {
            errors.reject("not valid ");
        }
    }

    public boolean playerIsEmpty (Player updatedPlayer) {
        return Stream.of(updatedPlayer.getId(), updatedPlayer.getName(), updatedPlayer.getTitle(),
                        updatedPlayer.getRace(), updatedPlayer.getProfession(), updatedPlayer.getLevel(),
                        updatedPlayer.getExperience(), updatedPlayer.getBanned(),
                        updatedPlayer.getUntilNextLevel(), updatedPlayer.getBirthday())
                .allMatch(Objects::isNull);
    }
}
