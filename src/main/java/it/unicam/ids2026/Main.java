package it.unicam.ids2026;

import it.unicam.ids2026.hackhub.HackHub;
import it.unicam.ids2026.hackhub.hackathon.Hackathon;
import it.unicam.ids2026.hackhub.hackathon.data.DatiHackathon;
import it.unicam.ids2026.hackhub.hackathon.data.Intervallo;
import it.unicam.ids2026.hackhub.roles.staff.Giudice;
import it.unicam.ids2026.hackhub.roles.staff.Mentore;
import it.unicam.ids2026.hackhub.roles.staff.Organizzatore;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

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

            DatiHackathon dati = new DatiHackathon(nome, luogo, maxTeam, regolamento);
            Organizzatore org = new Organizzatore("Admin", "User", UUID.randomUUID());
            Giudice giudice = new Giudice("Giudice", "Uno", UUID.randomUUID());
            Intervallo iscrizioni = new Intervallo(LocalDate.now(), LocalDate.now().plusDays(10));
            Intervallo durata = new Intervallo(LocalDate.now().plusDays(11), LocalDate.now().plusDays(13));

            Hackathon hackathon = hackHub.creaHackathon(org, dati, giudice, iscrizioni, durata);
            System.out.println("Hackathon creato con ID: " + hackathon.getId());

            System.out.println("\n--- Aggiunta Mentore ---");
            System.out.print("Nome Mentore: ");
            String nomeMentore = scanner.nextLine();
            System.out.print("Cognome Mentore: ");
            String cognomeMentore = scanner.nextLine();

            Mentore mentore = new Mentore(nomeMentore, cognomeMentore, UUID.randomUUID());

            hackHub.aggiungiMentori(hackathon, List.of(mentore));

            System.out.println("Mentore aggiunto con successo all'hackathon " + hackathon.getDatiHackathon().nome());

        } catch (Exception e) {
            System.err.println("Errore: " + e.getMessage());
        }

    }
}