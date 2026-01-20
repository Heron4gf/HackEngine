package it.unicam.ids2026.hackhub.managers;

import it.unicam.ids2026.hackhub.roles.Utente;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor
public class UserManager {
    private final Set<Utente> users;
}
