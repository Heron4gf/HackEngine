package it.unicam.ids2026.api.events;


import it.unicam.ids2026.core.roles.team.Utente;
import lombok.NonNull;

public interface UserChangeListener extends Listener {

    void notifyUserChange(@NonNull Utente utente);

}
