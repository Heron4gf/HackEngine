package it.unicam.ids2026.hackhub.managers;

import it.unicam.ids2026.hackhub.Hackathon;
import it.unicam.ids2026.hackhub.roles.Mentore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.util.Collection;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public class MentorManager {
    private Collection<Mentore> listaMentori;


    public Collection<Mentore> getMentoriDisponibili(@NonNull Hackathon h) {
        return listaMentori.stream().filter(m -> !h.getMentori().contains(m)).collect(Collectors.toList());
    }
}
