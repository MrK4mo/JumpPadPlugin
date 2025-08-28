# JumpPad Plugin für Folia 1.21.5

Ein vollständig konfigurierbares JumpPad Plugin mit Region-basierter Funktionalität, speziell entwickelt für Folia Server. Das Plugin unterstützt sowohl vertikale als auch horizontale Sprungbewegungen!

## Features

- ✅ **Region-basierte JumpPads**: Erstelle eigene Regionen mit individuellen JumpPad-Einstellungen
- ✅ **Vollständig konfigurierbar**: Jede Region kann eigene Blöcke, Sprungstärke, Vorwärtsstärke und Sounds haben
- ✅ **Vorwärts-Sprung**: Spieler werden nicht nur nach oben, sondern auch in ihre Blickrichtung katapultiert
- ✅ **MiniMessage Support**: Moderne Chat-Formatierung mit Farben und Formatierungen
- ✅ **Folia-kompatibel**: Optimiert für die neue Folia Server-Software
- ✅ **Performance-optimiert**: Effiziente Region-Abfragen und Cooldown-System
- ✅ **Benutzerfreundlich**: Intuitive Befehle und Tab-Completion

## Installation

1. Lade das Plugin in den `plugins` Ordner deines Folia Servers
2. Starte den Server neu
3. Die `config.yml` und `regions.yml` werden automatisch erstellt

## Befehle

### Grundlegende Befehle
- `/jumppad pos1` - Setze die erste Position für eine neue Region
- `/jumppad pos2` - Setze die zweite Position für eine neue Region
- `/jumppad create <regionname>` - Erstelle eine neue JumpPad Region
- `/jumppad delete <regionname>` - Lösche eine existierende Region
- `/jumppad list` - Zeige alle verfügbaren Regionen an
- `/jumppad info <regionname>` - Zeige detaillierte Informationen einer Region

### Konfigurationsbefehle
- `/jumppad setblock <region> <block>` - Setze den JumpPad-Block für eine Region
- `/jumppad setstrength <region> <stärke>` - Setze die vertikale Sprungstärke (z.B. 1.5)
- `/jumppad setforward <region> <stärke>` - **NEU:** Setze die horizontale Vorwärts-Stärke (z.B. 0.8)
- `/jumppad setsound <region> <sound>` - Setze den Sound-Effekt
- `/jumppad reload` - Lade die Konfiguration neu

## Berechtigungen

- `jumppad.admin` - Erlaubt die Verwaltung von JumpPad-Regionen (Standard: OP)
- `jumppad.use` - Erlaubt die Nutzung von JumpPads (Standard: alle Spieler)

## Verwendung

### 1. Erste Region erstellen

```
1. Gehe zur gewünschten Position und führe aus: /jumppad pos1
2. Gehe zur gegenüberliegenden Ecke und führe aus: /jumppad pos2
3. Erstelle die Region: /jumppad create meine_region
```

### 2. Region konfigurieren

```
/jumppad setblock meine_region SLIME_BLOCK
/jumppad setstrength meine_region 2.0     # Vertikale Sprungkraft
/jumppad setforward meine_region 1.2      # Vorwärts-Schub in Blickrichtung
/jumppad setsound meine_region ENTITY_SLIME_SQUISH
```

### 3. JumpPads verwenden

Platziere den konfigurierten Block (Standard: Slime Block) in deiner Region. Spieler werden automatisch katapultiert, wenn sie auf den Block treten!

**Neu:** Spieler springen jetzt nicht nur nach oben, sondern auch in ihre Blickrichtung vorwärts!

## Sprungmechanik

### Vertikaler Sprung (`setstrength`)
- Bestimmt, wie hoch der Spieler springt
- Werte zwischen 0.5 - 3.0 sind empfohlen
- Standard: 1.5

### Horizontaler Sprung (`setforward`)
- **NEU:** Bestimmt, wie stark der Spieler in Blickrichtung geschoben wird
- 0.0 = kein Vorwärtsschub (nur vertikaler Sprung wie bisher)
- 0.5 = sanfter Vorwärtsschub
- 1.0 = moderater Vorwärtsschub
- 2.0+ = starker Vorwärtsschub
- Standard: 0.8

### Beispiel-Kombinationen
```
# Traditioneller JumpPad (nur nach oben)
/jumppad setstrength spawn_jump 2.0
/jumppad setforward spawn_jump 0.0

# Balancierter JumpPad (oben + vorwärts)
/jumppad setstrength spawn_jump 1.5
/jumppad setforward spawn_jump 0.8

# Kanonen-JumpPad (starker Vorwärtsschub)
/jumppad setstrength spawn_jump 1.0
/jumppad setforward spawn_jump 2.5
```

## Konfiguration

### config.yml

```yaml
# Standard-Einstellungen für neue Regionen
jumppad:
  default-block: SLIME_BLOCK
  default-strength: 1.5           # Vertikale Sprungkraft
  default-forward-strength: 0.8   # NEU: Horizontale Vorwärts-Kraft
  default-sound: ENTITY_SLIME_SQUISH

# Nachrichten (MiniMessage Format)
messages:
  enabled: true
  jump: "<green>Du wurdest von einem JumpPad katapultiert!"
  # ... weitere Nachrichten
```

### MiniMessage Formatierung

Das Plugin unterstützt vollständig MiniMessage-Formatierung:

- `<green>Text</green>` - Grüner Text
- `<red>Text</red>` - Roter Text
- `<bold>Text</bold>` - Fetter Text
- `<italic>Text</italic>` - Kursiver Text
- `<gradient:blue:green>Text</gradient>` - Gradient-Effekt

## Verfügbare Blöcke

Beliebte JumpPad-Blöcke:
- `SLIME_BLOCK` (Standard)
- `MAGMA_BLOCK`
- `HAY_BLOCK`
- `SPONGE`
- `EMERALD_BLOCK`
- `REDSTONE_BLOCK`
- `DIAMOND_BLOCK`

## Verfügbare Sounds

Beliebte Jump-Sounds:
- `ENTITY_SLIME_SQUISH` (Standard)
- `ENTITY_FIREWORK_ROCKET_LAUNCH` (für Kanonen-JumpPads)
- `BLOCK_NOTE_BLOCK_PLING`
- `ENTITY_PLAYER_HURT`
- `ENTITY_ENDERMAN_TELEPORT`
- `ENTITY_GENERIC_EXPLODE`

## Technische Details

### Sprung-Berechnung
Das Plugin berechnet den Sprungvektor basierend auf:
1. **Vertikale Komponente**: Direkt nach oben (Y-Achse)
2. **Horizontale Komponente**: In die Blickrichtung des Spielers (X/Z-Achse)
3. **Richtungsvektor**: Wird automatisch normalisiert und mit der Vorwärts-Stärke multipliziert

### Folia-Kompatibilität
Das Plugin ist vollständig für Folia optimiert und nutzt:
- `GlobalRegionScheduler` für Thread-sichere Operationen
- Effiziente Region-Abfragen ohne Performance-Einbußen
- Asynchrone Event-Verarbeitung

### Performance
- **Cooldown-System**: Verhindert Spam-Jumping (1 Sekunde Cooldown)
- **Optimierte Region-Abfragen**: Cached Boundary-Berechnungen
- **Minimale Server-Belastung**: Nur aktive Bewegungen werden verarbeitet
- **Richtungsberechnung**: Effiziente Vektor-Mathematik für Vorwärtssprünge

## Beispiel-Setups

### 1. Spawn-JumpPad (ausgewogen)
```
/jumppad create spawn_jump
/jumppad setblock spawn_jump EMERALD_BLOCK
/jumppad setstrength spawn_jump 1.8
/jumppad setforward spawn_jump 1.0
/jumppad setsound spawn_jump ENTITY_FIREWORK_ROCKET_LAUNCH
```

### 2. Kanonen-JumpPad (weit schießend)
```
/jumppad create cannon_jump
/jumppad setblock cannon_jump REDSTONE_BLOCK
/jumppad setstrength cannon_jump 1.2
/jumppad setforward cannon_jump 3.0
/jumppad setsound cannon_jump ENTITY_GENERIC_EXPLODE
```

### 3. Hochsprung-JumpPad (nur vertikal)
```
/jumppad create high_jump
/jumppad setblock high_jump SLIME_BLOCK
/jumppad setstrength high_jump 3.5
/jumppad setforward high_jump 0.0
/jumppad setsound high_jump ENTITY_SLIME_SQUISH
```

### 4. Sanfter JumpPad (für Anfänger)
```
/jumppad create gentle_jump
/jumppad setblock gentle_jump HAY_BLOCK
/jumppad setstrength gentle_jump 1.0
/jumppad setforward gentle_jump 0.5
/jumppad setsound gentle_jump BLOCK_NOTE_BLOCK_PLING
```

## Migration von älteren Versionen

Falls du bereits JumpPad-Regionen hast, die vor diesem Update erstellt wurden:
1. Die bestehenden Regionen behalten ihre Funktionalität
2. Neue `forward-strength` wird automatisch auf 0.0 gesetzt (nur vertikaler Sprung)
3. Verwende `/jumppad setforward <region> <wert>` um Vorwärtssprünge zu aktivieren

## Support

Bei Problemen oder Fragen:
1. Überprüfe die Server-Logs auf Fehlermeldungen
2. Stelle sicher, dass Folia 1.21.5+ verwendet wird
3. Verwende `/jumppad reload` nach Konfigurationsänderungen
4. Teste die Sprungrichtung mit verschiedenen `setforward`-Werten

## Lizenz

Dieses Plugin ist Open Source und kann frei verwendet und modifiziert werden.