# Progetto IDS 2025–2026

**Autori:**
- Aldo Giambuzzi
- Manuel Pasquini
- Andrea Biondini

---

## HackHub

HackHub è una piattaforma per la gestione centralizzata degli hackathon, sviluppata come applicazione Spring Boot con interfaccia REST API.

### Come funziona

Un **Hackathon** è gestito da un Organizzatore e coinvolge:
- **Team** di partecipanti che si iscrivono durante il periodo di registrazione
- **Mentori** che forniscono supporto ai team
- **Giudici** che valutano i progetti submessi

### Ciclo di vita dell'hackathon

L'hackathon segue uno stato machine con quattro fasi:
1. **ISCRIZIONE** - I team si registrano
2. **IN_CORSO** - L'hackathon è attivo, i team lavorano e possono richiedere supporto
3. **VALUTAZIONE** - I giudici valutano le sottomissioni
4. **CONCLUSO** - L'hackathon termina con i risultati finali

---

## Struttura del Progetto

```
src/
├── main/
│   ├── java/it/unicam/ids2026/
│   │   ├── HackEngineApplication.java      # Entry point Spring Boot
│   │   ├── core/
│   │   │   ├── hackathon/
│   │   │   │   ├── Hackathon.java       # Entity principale hackathon
│   │   │   │   ├── data/                # DatiHackathon, Intervallo
│   │   │   │   └── status/              # State pattern (StatoIscrizione, StatoInCorso, etc.)
│   │   │   ├── roles/
│   │   │   │   ├── User.java           # Interfaccia base utente
│   │   │   │   ├── team/               # Team, Iscrizione, Invito, Utente
│   │   │   │   └── staff/              # Organizzatore, Giudice, Mentore
│   │   │   ├── managers/               # Business logic
│   │   │   │   ├── HackathonManager.java
│   │   │   │   ├── TeamManager.java
│   │   │   │   ├── UserManager.java
│   │   │   │   ├── InviteManager.java
│   │   │   │   └── StaffManager.java
│   │   │   ├── events/                 # Sistema eventi publish/subscribe
│   │   │   └── supportRequest/          # Sistema richieste supporto
│   │   └── api/
│   │       ├── controller/              # REST API Controllers
│   │       ├── dto/                     # Request/Response DTOs
│   │       ├── events/                  # Event listeners
│   │       └── external/                # Integrazione calendario
│   └── resources/
│       └── static/
│           └── openapi.yaml             # Specifica OpenAPI
└── test/
    └── java/it/unicam/ids2026/
        ├── unit/                        # Unit tests (Mockito)
        ├── slice/                       # Slice tests (@WebMvcTest)
        └── integration/                 # Integration tests (@SpringBootTest)
```

---

## API REST

La specifica OpenAPI completa è disponibile in `src/main/resources/static/openapi.yaml`.

### Endpoint principali

#### Utenti
| Metodo | Endpoint | Descrizione |
|--------|----------|-------------|
| GET | `/api/users` | Lista tutti gli utenti |
| POST | `/api/users` | Crea un nuovo utente |
| GET | `/api/users/{id}` | Restituisce un utente specifico |

#### Hackathon
| Metodo | Endpoint | Descrizione |
|--------|----------|-------------|
| GET | `/api/hackathons` | Lista tutti gli hackathon |
| POST | `/api/hackathons` | Crea un nuovo hackathon |
| GET | `/api/hackathons/{id}` | Restituisce un hackathon specifico |
| GET | `/api/hackathons/joinable` | Hackathon aperti alle iscrizioni |
| POST | `/api/hackathons/{id}/avanza-stato` | Avanza lo stato |
| POST | `/api/hackathons/{id}/chiudi-sottomissioni` | Chiude le sottomissioni |

#### Team
| Metodo | Endpoint | Descrizione |
|--------|----------|-------------|
| POST | `/api/teams` | Crea un nuovo team |
| GET | `/api/teams/{nome}` | Restituisce un team specifico |
| POST | `/api/teams/{nome}/esci` | Utente esce dal team |
| POST | `/api/teams/{nome}/iscrizione` | Iscrive un team all'hackathon |

#### Inviti
| Metodo | Endpoint | Descrizione |
|--------|----------|-------------|
| POST | `/api/invites` | Invia un invito |
| GET | `/api/invites/casella/{utenteId}` | Casella inviti di un utente |
| POST | `/api/invites/accetta` | Accetta un invito |
| POST | `/api/invites/rifiuta` | Rifiuta un invito |

### Codici di risposta
- **200** - Operazione riuscita
- **201** - Risorsa creata
- **400** - Richiesta non valida
- **404** - Risorsa non trovata
- **409** - Conflitto di stato

---

## Tecnologie

- **Java 21** con **Spring Boot 4**
- **State Pattern** per gestire le fasi dell'hackathon
- **Observer Pattern** per il sistema eventi
- **Jakarta Validation** per la validazione dei DTO
- **JUnit 5** + **Mockito** per i test

---

## Comandi

### Eseguire l'applicazione
```bash
# Compila ed esegue
./mvnw spring-boot:run

# Oppure con Maven installato
mvn spring-boot:run
```

### Eseguire i test
```bash
# Tutti i test
./mvnw test

# Solo test unitari
./mvnw test -Dtest="it.unicam.ids2026.unit.*"

# Solo test di integrazione
./mvnw test -Dtest="it.unicam.ids2026.integration.*"

# Con reporting dettagliato
./mvnw test -Dsurefire.reportFormat=verbose
```

### Build
```bash
# Compila il progetto
./mvnw compile

# Crea JAR
./mvnw package

# Skippa i test durante il build
./mvnw package -DskipTests
```

### Accesso
L'applicazione è accessibile su: `http://localhost:8080`
- API base: `http://localhost:8080/api`
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`
- OpenAPI YAML: `http://localhost:8080/v3/api-docs.yaml`

---

## Iterazioni

- **Iterazione 1**  
  [Codice](https://github.com/Heron4gf/HackEngine/tree/iterazione1) | [Visual Paradigm](https://github.com/Heron4gf/HackEngine/blob/iterazione1/VisualParadigm/progetto_ids_2025_2026_iterazione1.vpp)

- **Iterazione 2**  
  [Codice](https://github.com/Heron4gf/HackEngine/tree/iterazione2) | [Visual Paradigm](https://github.com/Heron4gf/HackEngine/blob/iterazione2/VisualParadigm/progetto_ids_2025_2026_iterazione2.vpp)

- **Iterazione 3**  
  [Codice](https://github.com/Heron4gf/HackEngine/tree/iterazione3) | [Visual Paradigm](https://github.com/Heron4gf/HackEngine/blob/iterazione3/VisualParadigm/progetto_ids_2025_2026_iterazione3.vpp)
- **Iterazione 4**
- [Codice](https://github.com/Heron4gf/HackEngine/tree/iterazione4) | [Visual Paradigm](https://github.com/Heron4gf/HackEngine/blob/iterazione4/VisualParadigm/progetto_ids_2025_2026_iterazione4.vpp)
