# Beitragsrichtlinien für Orbit

Vielen Dank für dein Interesse, zu Orbit beizutragen! Wir freuen uns über jeden Beitrag, der hilft, dieses Projekt zu verbessern.

## Inhaltsverzeichnis

- [Code of Conduct](#code-of-conduct)
- [Wie kann ich beitragen?](#wie-kann-ich-beitragen)
    - [Fehler melden](#fehler-melden)
    - [Funktionen vorschlagen](#funktionen-vorschlagen)
    - [Code beitragen](#code-beitragen)
- [Pull-Request-Prozess](#pull-request-prozess)
- [Entwicklungsumgebung einrichten](#entwicklungsumgebung-einrichten)
- [Stilrichtlinien](#stilrichtlinien)
    - [Git Commit Messages](#git-commit-messages)
    - [Java-Stilrichtlinien](#java-stilrichtlinien)
    - [Dokumentation](#dokumentation)
- [Testrichtlinien](#testrichtlinien)

## Code of Conduct

Dieses Projekt und alle Teilnehmer unterliegen einem Verhaltenskodex. Durch die Teilnahme wird erwartet, dass du diesen Code einhältst. Bitte melde inakzeptables Verhalten an die Projektbetreuer.

## Wie kann ich beitragen?

### Fehler melden

- **Vor dem Melden eines Fehlers** überprüfe bitte die [Issue-Liste](https://github.com/habsgleich/orbit/issues), um sicherzustellen, dass der Fehler nicht bereits gemeldet wurde.
- Verwende die [Issue-Vorlage](.github/ISSUE_TEMPLATE/bug_report.md), falls vorhanden.
- Beschreibe den Fehler so detailliert wie möglich, einschließlich Schritten zur Reproduktion, erwartetem Verhalten und tatsächlichem Verhalten.
- Füge Kontext hinzu (Betriebssystem, JDK-Version, Datenbanksystem etc.).

### Funktionen vorschlagen

- **Überprüfe die Issue-Liste**, um zu sehen, ob deine Idee bereits vorgeschlagen wurde.
- Beschreibe die Funktionalität detailliert, erkläre, warum sie nützlich wäre und wie sie implementiert werden könnte.
- Erkläre, wie diese Funktion mit der bestehenden Architektur harmonieren würde.

### Code beitragen

1. Forke das Repository und erstelle einen Zweig von `main`.
2. Implementiere deine Änderungen mit entsprechenden Tests.
3. Stelle sicher, dass alle Tests bestehen.
4. Aktualisiere die Dokumentation, wenn nötig.
5. Reiche einen Pull Request ein.

## Pull-Request-Prozess

1. Stelle sicher, dass dein Code den Stilrichtlinien entspricht und alle Tests bestehen.
2. Aktualisiere die README.md oder andere Dokumentation mit Details zu Änderungen an der Schnittstelle.
3. Der Pull Request wird überprüft, sobald er von mindestens einem anderen Entwickler genehmigt wurde.
4. Nach Genehmigung führt ein Projektbetreuer den Pull Request zusammen.

## Entwicklungsumgebung einrichten

```bash
# Repository klonen
git clone https://github.com/habsgleich/orbit.git
cd orbit

# Baue das Projekt mit Gradle
./gradlew build

# Tests ausführen
./gradlew test
```

## Stilrichtlinien

### Git Commit Messages

- Verwende die Gegenwartsform ("Add feature" nicht "Added feature")
- Verwende den Imperativ ("Move cursor to..." nicht "Moves cursor to...")
- Begrenze die erste Zeile auf 72 Zeichen oder weniger
- Referenziere Issues und Pull Requests nach der ersten Zeile
- Bei größeren Änderungen:
    - Beginne die Commit-Nachricht mit dem Typ: feat, fix, docs, style, refactor, test, chore

### Java-Stilrichtlinien

- Verwende aussagekräftige Variablen- und Methodennamen
- Schreibe Kommentare für komplexe Algorithmen und öffentliche APIs
- Vermeide unnötige Kommentare für selbsterklärenden Code
- Halte Methoden kurz und fokussiert auf eine einzige Aufgabe
- Halte Klassen Single-Responsibility-Prinzip konform

### Dokumentation

- Alle öffentlichen Klassen und Methoden sollten mit JavaDoc dokumentiert werden
- Erkläre in Kommentaren "warum", nicht "was" (der Code zeigt bereits "was")
- Halte die Dokumentation aktuell, wenn sich der Code ändert

## Testrichtlinien

- Schreibe Unit-Tests für alle neuen Funktionen und Fehlerbehebungen
- Strebe eine Testabdeckung von mindestens 80% an
- Teste sowohl positive als auch negative Szenarien
- Verwende aussagekräftige Namen für Testmethoden, die beschreiben, was getestet wird

---

Wir freuen uns auf deine Beiträge! Wenn du Fragen hast oder Hilfe benötigst, zögere nicht, ein Issue zu eröffnen oder uns direkt zu kontaktieren.