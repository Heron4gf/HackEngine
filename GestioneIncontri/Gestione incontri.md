# Gestione incontri

**Incontro 1** definizione di alcuni attori e stesura dei requisiti con alcune relative descrizioni

**Incontro 2** definizione dei casi d’uso che comprendono gli attori sistema di pagamento e calendar descrizione ancora parziale ( parte da questa iterazione visual paradigm)

**Incontro 3** pulizia delle descrizioni e introduzione dell’attore Membro del team e Membro dello staff con realativo caso d’uso

**Incontro 4** ridefiniti i casi separandoli da quelli che ne racchiudono  più di uno contemporaneamente e aggiunto l’attore tempo 

**Incontro 5** ripristino del corso delle iterazioni, pulizia del grafico e redifinizione dei casi d’uso tempo

**Incontro 6** sviluppo del diagramma delle classi d’analisi **** del primo caso d’uso crea Hackathon

**Incontro 7** implementazione  del diagramma delle classi d’analisi del secondo  caso d’uso aggiungi mentore (Nel digramma delle classi di progetto probabilmente implementeremo un gestore delle sottomisioni visto che Hackathon rischierebbe di avere troppe responsabilità).

Incontro 8 Prima definizione del sequence diagram del caso d' uso Crea Hackaton e inserimento delle cardinalità nel diagramma delle classi d’analisi 

Incontro 9 Ristrutturazione del Sequence diagram del caso d' uso Crea Hackathon e aggiunta delle sequenza alternative, inoltre aggiunta di un ulteriore sequenza alternativa all' interno del Flow of Events del caso d' uso Crea Hackathon 

**Incontro 10** aggiunta delle sequenze alternative nel caso d' uso Aggiunta mentori  e  ristrutturazione del diagramma delle classi d’analisi, infine creazione del diagramma di sequenza del caso d’uso aggiunta mentori

**Incontro 11** ristrutturazione del diagramma delle classi di progetto in particolare focus su HackHub con i patter Singleton per essere unico e il facede

**Incontro 11** ristrutturazione del diagramma delle classi di progetto in particolare focus su Hackathon 

**Incontro 12** Creazione della repo git con relativo inizio alla programmazione della prima integrazione 

**Incontro 13** completato il codice relativo alla prima iterazione e committato insieme al relativo file visual paradigm 

**Incontro 14** inizio seconda iterazione con scelta di approfondimento dei casi d’uso: Iscrizione all' Hackathon, entra in team crea team ed esci dal team con pre e post condizioni

**Incontro 15** ragionamento sul flusso di eventi e creazione del diagramma delle classi d’analisi con relative associazioni e attribuiti delle classi introdotte

**Incontro 16** Creazioni dei diagrammi di sequenza dell' iterazione 2 con relative sequenze alternative (da modificare il diagramma relativo ad Entra in team)

**Incontro 17** primo ragionamento sul diagramma delle classi di progetto con definizione di alcuni accorgimenti trovabili nelle note utili

**Incontro 18** Controllo più approfondito per quanto riguarda i diagramma di sequenza dei casi d’uso Entra in team e crea team aggiunte anche nelle note utili le motivazioni dei pattern scelti nelle prime iterazioni 

**Incontro 19** Refactoring del diagramma di progetto dell’iterazione 2 con aggiunta del pattern STATE (la spiegazione si trova in note utili) per gestire gli stati dell' Hackathon 

**Incontro 20** sviluppo del codice e ridefinizione del diagramma delle classi di progetto seguendo l’approccio Anemico che permette di garantire il principio di singola responsabilità seguendo il pattern MVC, nel codice abbiamo implementato invita nel team separatamente dal caso d’uso crea team poiché ci aspettiamo che gli inviti al momento della creazione verranno richiamati dal front end nella apposita sezione dell' back end senza mischiare la logica di creazione con quella di invito  fine ITERAZIONE 2

**Incontro 21** inizio Iterazione 3 con scelta dei casi d’uso  Invita nel team, Invia sottomissione Aggiorna sottomissione, Chiusura sottomissione da vedere nel dettaglio 

**Incontro 22**  di approccio al progetto e Refactoring in Iterazione 3 dei diagrammi di sequenza (Aggiungi mentori)

**Incontro 23** Refactoring completato dei diagrammi di sequenza dei casi d’uso Aggiungi mentori, Crea Hackathon; focus sul codice per il Refactoring della gestione della lista utenti e modifica della classe MentorMenager in staff manager 

**Incontro 24** Completata la revisione del diagramma di Sequenza Crea Hackathon con I relativi aggiustamenti e Refactoring del diagramma di Esci dal Team

**Incontro 25** Refactoring completo del diagramma di sequenza di iscrizione Hackathon, Refactoring anche del diagramma Entra in team( da rivedere la prossima volta per controllare se giusto)

**Incontro 26** Refactoring del diagramma di sequenza invita nel team con scelta di rendere il caso d'uso dell’invito per un singolo utente (guardare nelle note utili per il motivo) 

**Incontro 27** Controllo completo di invita nel team con analisi al attribuito Maxmembri,Refactoring completo di crea team con separazione della logica dal caso d’uso invita nel team, abbiamo deciso che l’information Expert di Entra in team è l'invite manager e non l’utente, valutata anche la decisione di rendere l’eliminazione dell’invito correlata a lo scioglimento di un team qualora avvenisse con inviti ancora pendenti, infine completato entra in team

**Incontro 28** Aggiunto in esci dal team  l’opt in cui andiamo a mostrare la rimozione degli inviti una volta che un team non esiste più, abbiamo scelto anche di utilizzare il pattern Observer per limitare l’accoppiamento tra Invite manager e Team Manager poiché abbiamo individuato che Invite manager ha la responsabilità di rimuovere gli inviti, operazione necessaria dopo l’eliminazione del team (responsabilità di Team Manager) per evitare quindi di fare orchestrare ad Hack Hub le chiamate abbiamo optato per l’utilizzo del pattern.

**Incontro 29** creazione dei diagrammi di sequenza crea sottomissione, aggiorna sottomissione e chiusura sottomissione, analisi e Refactoring dello stato implementando il pattern state in maniera propria aggiungendo i metodi di delega.

**Incontro 30,** abbiamo modificato il diagramma di classi di progetto secondo i diagrammi di sequenza crea,chiudi e aggiorna sottomissione. Abbiamo optato, infatti di usare una mappa per gestire le sottomissioni legate dalla coppia team-sottomissione abbiamo optato a questa scelta perché ci permette  di rappresentare relazioni in modo naturale e  ci aiuta a recuperare un valore conoscendo la sua chiave.

**Incontro 31,** abbiamo lavorato sul diagramma delle classi di progetto, in particolare sui casi d’uso: Crea Team, Entra in Team, Esci dal Team, Aggiunta mentori, iscrivi Hackathon. Abbiamo introdotto, quindi l’event publisher, usato seguendo il pattern Observer per notificare l’avvenimento dell’operazione di uscita dal team.

Abbiamo rimosso MentorMenager e abbiamo aggiunto staff Manager che utilizza User Manager per restituire una lista di mentori

**Incontro 32** Refactoring completo del UML sulla base di tutti i sequence diagram con un primo approccio sul codice 

I**ncontro 33** Revisione di tutto il diagramma UML e Sviluppo  del relativo codice per ogni nuova aggiunta in particolare Refactoring del codice sull’utilizzo del pattern State e introduzione dell' observer con relative classi e interfacce 

**Incontro 33 (time box 1 ora)** Abbiamo deciso di progettare **InviteManager** come un listener, al quale viene iniettato **EventPublisher** tramite **TeamManager**. Inoltre, è stata apportata una modifica alla classe *InviteManager* a livello di codice. Infine, si è scelto di implementare il validatore in una fase successiva, ovvero dopo l’integrazione di **Spring Boot**. (guardare note utili)

**Incontro 34**  ha avuto inizio la quarta iterazione del progetto, focalizzata sulla selezione e definizione dei casi d’uso principali. In particolare, sono stati individuati i seguenti casi d’uso:

- *Avanzare una richiesta di supporto*
- *Valutare una sottomissione*
- *Inserire la propria disponibilità*
- *Prendere in carico una richiesta*

Per ciascun caso d’uso sono stati analizzati e documentati i relativi *flow of events*, nonché le condizioni di *pre-condizione* e *post-condizione*, al fine di garantire una descrizione completa e coerente del comportamento del sistema.

**Incontro 35** Strutturato il modello delle classi d’analisi con l’aggiunta dei casi d’uso dell' iterazione 4 (aggiunta delle classi Richiesta supporto, Risposta, Valutazione e le associazioni  relative) poi abbiamo valutato che nel caso d’uso valuta segnalazione l’utente può essere bannato.

**Incontro 36:** Introduzione del diagramma di sequenza del caso d’uso “Valuta Sottomissioni”, revisione del diagramma di sequenza relativo al caso d’uso “Prende in carico una richiesta” e “inserisci disponibilità”

**Incontro 37**: sviluppo del UML sulla base dei casi d’uso “valuta sottomissione” e “avanza richiesta di supporto”

**Incontro 38** aggiunta al diagramma UML le classi relative ai casi d’uso”inserisci disponibilità” e “prendi in carico una richiesta”(disponibilità, DefaultCalendar) e le relativa interfaccia per implementare il Wrapper(vedere note utili per maggiori dettagli) ; 

**Incontro 39** inizio iterazione 5 con la scelta dei casi d’uso “proclama vincitore”, “eroga premio”, “consulta Hackathon”, “segnala violazione”

**Incontro 40** revisione del caso d’uso eroga premio e primo approccio al diagramma delle classi d’analisi con introduzione della classe violazione associata a team e al mentore

**Incontro 41** completati i i sequence diagram dei casi d’uso “Proclama vincitore” e “Eroga Premio”con le relative sequenze alternative 

**Incontro 42** completati i i sequence diagram dei casi d’uso “Proclama vincitore” e “Eroga Premio”con le relative sequenze alternative 

**Incontro 43** rewiew del codice introduzione della classe Violazione in linea con il diagramma UML,

Successivamente sviluppo delle transazioni tramite l’interfaccia IPaymentMethod, defaultPaymentMethod e infine Transaction

**Incontro 44** rewiew del codice nello specifico WinnigManager considerando i casi limite presi in considerazione e definizione dei metodi nella classe state InValutazione E Concluso Ristrutturazione anche del Sequence diagram Proclama Vincitore e rewiew del Sequence Diagram Crea Hackathon  e correzione di alcune inesattezze in eroga premio 

**Incontro 45** Durante l’incontro sono state revisionate tutte le classi del package *data* e gli stati, includendo il metodo *aggiungi vincitore*. La classe *Hackathon* è stata aggiornata con gli opportuni attributi e metodi relativi alla gestione delle transazioni, mentre la classe *Events* è risultata conforme al diagramma delle classi.

È stata inoltre implementata la funzionalità “Valuta Sottomissione”, che prevede il recupero delle iscrizioni dell’Hackathon contenenti le sottomissioni, la costruzione di una mappa tra *Team* e *Sottomissione* e l’assegnazione delle valutazioni a tutte le sottomissioni, con successivo salvataggio dell’Hackathon aggiornato. L’approccio sfrutta il riferimento alla sottomissione presente nell’oggetto *Iscrizione*, permettendo di modificare direttamente l’oggetto puntato senza intervenire sugli altri dati dell’iscrizione.

Infine, è stato revisionato il sequence diagram “Avanza richiesta di supporto” con l’aggiunta del relativo *Repository*, e sono stati corretti alcuni metodi introducendo il *Repository* anche nel caso d’uso “Prendi in carico una richiesta”.

**Incontro 46:**

- Revisionati I Repository aggiunti ai Sequence Diagram
- delineati i metodi ed endpoint da inserire sui controller nel codice

**Incontro 47:**

Durante l’incontro è stato implementato nel *User Controller* un *factory method*. Sono stati inoltre aggiunti i metodi mancanti nei *Repository* e collegati ai rispettivi *Manager*. Il codice relativo ai *Controller* è stato sistemato e sono stati corretti i refusi presenti.

Infine, è stata effettuata la revisione attinente ai repository dei diagrammi di sequenza relativi ai casi d’uso “Invia Sottomissione” e “Aggiorna Sottomissione”.

**Incontro 48:**

Aggiunta dei controller su Visual Paradigm, ricontrollati i DTO e i Controller nel codice, aggiunta dei repository nel diagramma di sequenza relativo al caso d’uso “Invita nel team”, rimozione del metodo “aggiungiMentori” di StaffManager. Scrittura dei test di SubmissionController in SubmissionControllerTest. Revisione dei controller e dei manager in modo che non vi sia accoppiamento tra essi.

**Incontro 49** aggiunta dei Repository nei sequence Crea Team, Entra in team e esci dal team. In entra in team abbiamo optato per realizzare un sequence leggermente diverso dal codice poiché si entrerebbe nella sequenza di accetta e rifiuta invito e tutte le volte si ripeterebbe il recupero dell' invito. 

**Incontro 50** 

Corretti i Sequence diagram  dei casi d’uso “Aggiunta Mentori", "Invita nel Team” inoltre aggiunta la parte dei DTO Request nei diagrammi di classi di progetto 

**Incontro 51**

Aggiunti i DTO Response ai modelli delle classi di progetto e Refactoring del diagramma "Inserisci Disponibilità” infine aggiunte le composizione nelle classi annidate nei Relativi DTO

**Incontro 52**

Completate definitivamente le cardinalità dei DTO. Discussione sul metodo migliore per rappresentare i controller introdotti da Spring Boot nei Sequence Diagram e definizione dell’approccio da seguire.

**Incontro 53**

Completate definitivamente le classi di Progetto, comprese associazioni e cardinalità. Revisionato il codice e introdotti test dedicati ai principali casi d’uso.

**Incontro 54**

Decisione di togliere HackHub dai diagrammi di sequenza, risistemazione del diagramma delle classi di progetto

**Incontro 55**

Effettuata la risistemazione grafica del diagramma delle classi di Progetto, riorganizzando la disposizione di classi e associazioni. Aggiornati i Sequence Diagram relativi ai casi d’uso: avanzamento richiesta supporto, aggiunta mentori, consultazione hackathon, creazione team, aggiornamento sottomissione, chiusura sottomissione e creazione hackathon.

Inoltre aggiunto il salvataggio del team come dal Sequence Cream team utilizzando il pattern observer.

**Incontro 56**

Completato il Diagramma delle Classi di Progetto con i relativi Listener. Aggiunti Team Change e Hackathon Change. Sistemati i casi d’uso “proclama vincitore” e “valuta sottomissione”. Aggiornati i Sequence Diagram relativi a “segnala violazione” ed “eroga premio”. Verificata inoltre la conformità del codice rispetto ai diagrammi realizzati.

**Incontro 57**

Controllo e allineamento incrociato dei casi d’uso tra il codice e il relativo diagramma

**Incontro 58**

Test degli end point e caricamento di tutti gli artifatti nella sezione apposita del file contenente il codice