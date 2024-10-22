# ups-monitor

## Datenstruktur
- Status: POWER_ON, POWER_OFF, NO_CONNECTION, ALARM, SHUTDOWN
- Ups: id, name, address, community
- UsvResponse: ups, status, seit wann
- Server: id, name, address, username, password, lastShutdown
- Event: id, status, restlaufzeit, entity

-
* USVen als Karten anzeigen, analoge Ansicht für Server
* Farben grün für Strom, rot für kein Strom, gelb/blau/braun für keine Verbindung (Hintergrundfarbe oder zusätzliches Element)
* (evtl. Farbe von Benutzer wählen lassen und in DB speichern)
* Karteninhalt: Name, angeschlossene Server, wie lange besteht aktueller Status
* Balken mit Batterierestlaufzeit bzw. Restkapazität (bei Stromausfall regelmäßig kontrollieren, sonst letzten Wert nehmen)
* (Bei Ausfall Helligkeit des Farbtons oder Größe der Farbfläche abhängig von Restlaufzeit, bei Verbindungsabbruch von Dauer, bei Netzverbindung von Kapazität)

### Endpunkte
* / GET alle USV aus DB
* / POST USV hinzufügen
* /id GET einzelne USV aus DB
* /id PUT einzelne USV bearbeiten
* /id DELETE USV löschen
* /monitor GET alle USV mit aktuellem Status
* /monitor/id GET einzelne USV mit allen SNMP-Daten
* /server GET alle Server aus DB
* /server POST Server hinzufügen
* /server/id PUT,DELETE

## MVP
* USVen hinzufügen, (editieren), löschen -> zunächst nur Adresse als Eingabe
* Server hinzufügen, löschen, zu USV zuordnen
* Button für Überwachungslauf stößt Zufalls-Simulation an (startMonitoring, stopMonitoring)
* Hintergrundfarbe USV von grün auf rot bei Stromausfall
* zunächst einheitlich Shutdown bei Restlaufzeit 5 min -> Anzeige Event + Rotfärbung

### zweiter Schritt
* mehr Felder bearbeiten -> Auf Detailspage zu jedem Feld Edit-Button, der Input-Feld und Save-Button anzeigt
* Bei Server Restlaufzeit für Shutdown eintragen
* jedes Event speichern: Stromausfall, Netzwerkunterbrechung, Wiederherstellung, Alarm + Shutdown
* Shutdown von eingegebener Restlaufzeit abhängig machen

### weitere Funktionen
* Ladezustand bzw. Ausfalldauer optisch in Übersicht hervorheben
* Login fürs Editieren
* Anfangsansicht neutral oder farbig abhängig von Status überwachungAktiv im Backend