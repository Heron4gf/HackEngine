# Progetto IDS 2025–2026

**Autori:**
- Aldo Giambuzzi
- Manuel Pasquini
- Andrea Biondini

---

## HackHub

HackHub è una piattaforma per la gestione centralizzata degli hackathon, sviluppata come applicazione Spring Boot con interfaccia Swing.

### Come funziona

Un **Hackathon** è gestito da un Organizzatore e coinvolge:
- **Team** di partecipanti che si iscrivono durante il periodo di registrazione
- **Mentori** che forniscono supporto ai team
- **Giudici** che valutano i progetti submessi

### Ciclo di vita dell'hackathon

L'hackathon segue uno stato machine con quattro fasi:
1. **Iscrizione** - I team si registrano
2. **In Corso** - L'hackathon è attivo, i team lavorano e possono richiedere supporto
3. **In Valutazione** - I giudici valutano le sottomissioni
4. **Concluso** - L'hackathon termina con i risultati finali

### Struttura del codice

```
src/main/java/it/unicam/ids2026/
├── HackHubApplication.java       # Entry point Spring Boot
├── Main.java                     # Avvio applicazione
├── core/
│   ├── HackHub.java              # Entity principale hackathon
│   ├── hackathon/
│   │   ├── data/                 # DatiHackathon, Sottomissione, Valutazione, Intervallo
│   │   └── status/               # State pattern (StatoIscrizione, StatoInCorso, etc.)
│   ├── roles/
│   │   ├── User.java             # Interfaccia base utente
│   │   ├── team/                 # Team, Iscrizione, Invito, Utente
│   │   └── staff/                # Organizzatore, Giudice, Mentore, MembroStaff
│   ├── managers/                 # HackathonManager, TeamManager, UserManager, etc.
│   └── supportRequest/           # Sistema richieste supporto con risposte
└── api/
    ├── external/                # Integrazione calendario
    └── events/                   # Sistema eventi publish/subscribe
```

### Tecnologie

- **Java** con **Spring Boot**
- **State Pattern** per gestire le fasi dell'hackathon
- **Observer Pattern** per il sistema eventi

---

## Iterazioni

- **Iterazione 1**  
  [Codice](https://github.com/Heron4gf/HackEngine/tree/iterazione1) | [Visual Paradigm](https://github.com/Heron4gf/HackEngine/blob/iterazione1/VisualParadigm/progetto_ids_2025_2026_iterazione1.vpp)

- **Iterazione 2**  
  [Codice](https://github.com/Heron4gf/HackEngine/tree/iterazione2) | [Visual Paradigm](https://github.com/Heron4gf/HackEngine/blob/iterazione2/VisualParadigm/progetto_ids_2025_2026_iterazione2.vpp)

- **Iterazione 3**  
  [Codice](https://github.com/Heron4gf/HackEngine/tree/iterazione3) | [Visual Paradigm](https://github.com/Heron4gf/HackEngine/blob/iterazione3/VisualParadigm/progetto_ids_2025_2026_iterazione3.vpp)
