package it.unicam.ids2026;

import it.unicam.ids2026.core.HackHub;
import it.unicam.ids2026.core.hackathon.Hackathon;
import it.unicam.ids2026.core.hackathon.data.DatiHackathon;
import it.unicam.ids2026.core.hackathon.data.Intervallo;
import it.unicam.ids2026.core.roles.staff.Giudice;
import it.unicam.ids2026.core.roles.staff.Mentore;
import it.unicam.ids2026.core.roles.staff.Organizzatore;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        HackHub hackHub = HackHub.getInstance();

        System.out.println("=== HACKHUB CLI ===");

        try {
            System.out.print("Inserisci nome Hackathon: ");
            String nome = scanner.nextLine();

            System.out.print("Inserisci luogo: ");
            String luogo = scanner.nextLine();

            System.out.print("Numero massimo team: ");
            int maxTeam = Integer.parseInt(scanner.nextLine());

            System.out.print("Regolamento: ");
            String regolamento = scanner.nextLine();

            DatiHackathon dati = new DatiHackathon(nome, luogo, BigDecimal.TEN, Currency.getInstance("EUR"),maxTeam,
                    regolamento);
            Organizzatore org = new Organizzatore( "Admin", "User");
            Giudice giudice = new Giudice("Giudice", "Uno");
            Intervallo iscrizioni = new Intervallo(LocalDateTime.now(), LocalDateTime.now().plusDays(10));
            Intervallo durata = new Intervallo(LocalDateTime.now().plusDays(11), LocalDateTime.now().plusDays(13));

            Hackathon hackathon = hackHub.getHackathonManager().creaHackathon(org, dati, giudice, iscrizioni, durata);
            System.out.println("Hackathon creato con ID: " + hackathon.getId());

            System.out.println("\n--- Aggiunta Mentore ---");
            System.out.print("Nome Mentore: ");
            String nomeMentore = scanner.nextLine();
            System.out.print("Cognome Mentore: ");
            String cognomeMentore = scanner.nextLine();

            Mentore mentore = new Mentore(nomeMentore, cognomeMentore);

            hackHub.getHackathonManager().aggiungiMentori(hackathon, List.of(mentore));

            System.out.println("Mentore aggiunto con successo all'hackathon " + hackathon.getDatiHackathon().nome());

        } catch (Exception e) {
            System.err.println("Errore: " + e.getMessage());
        }

    }
}