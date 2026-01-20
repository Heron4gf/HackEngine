package it.unicam.ids2026.hackhub.managers;

import it.unicam.ids2026.hackhub.roles.User;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor
public class UserManager {
    private final Set<User> users;
}
