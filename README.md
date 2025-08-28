# JumpPad Plugin für Folia 1.21.5

Ein vollständig konfigurierbares JumpPad Plugin mit Region-basierter Funktionalität, speziell entwickelt für Folia Server.

## Features

- ✅ **Region-basierte JumpPads**: Erstelle eigene Regionen mit individuellen JumpPad-Einstellungen
- ✅ **Vollständig konfigurierbar**: Jede Region kann eigene Blöcke, Sprungstärke und Sounds haben
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
- `/jumppad setstrength <region> <stärke>` - Setze die Sprungstärke (z.B. 1.5)
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
/jumppad setstrength meine_region 2.0
/jumppad setsound meine_region ENTITY_SLIME_SQUISH
```

### 3. JumpPads verwenden

Platziere den konfigurierten Block (Standard: Slime Block) in deiner Region. Spieler werden automatisch katapultiert, wenn sie auf den Block treten!

## Konfiguration

### config.yml

```yaml
# Standard-Einstellungen für neue Regionen
jumppad:
  default-block: SLIME_BLOCK
  default-strength: 1.5
  default-sound: ENTITY_SLIME_SQUISH

# Nachrichten (MiniMessage Format)
messages:
  enabled: true
  jump: "<green>Du wurdest von einem JumpPad katapultiert!"
  region-created: "<green>Region '<region>' wurde erfolgreich erstellt!"
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

## Verfügbare Sounds

Beliebte Jump-Sounds:
- `ENTITY_SLIME_SQUISH` (Standard)
- `ENTITY_PLAYER_HURT`
- `BLOCK_NOTE_BLOCK_PLING`
- `ENTITY_FIREWORK_ROCKET_LAUNCH`
- `ENTITY_ENDERMAN_TELEPORT`

## Technische Details

### Folia-Kompatibilität
Das Plugin ist vollständig für Folia optimiert und nutzt:
- `GlobalRegionScheduler` für Thread-sichere Operationen
- Effiziente Region-Abfragen ohne Performance-Einbußen
- Asynchrone Event-Verarbeitung

### Performance
- **Cooldown-System**: Verhindert Spam-Jumping (1 Sekunde Cooldown)
- **Optimierte Region-Abfragen**: Cached Boundary-Berechnungen
- **Minimale Server-Belastung**: Nur aktive Bewegungen werden verarbeitet

## Beispiel-Setup

```
# 1. Sprungzone erstellen
/jumppad pos1
/jumppad pos2
/jumppad create spawn_jump

# 2. Konfigurieren
/jumppad setblock spawn_jump EMERALD_BLOCK
/jumppad setstrength spawn_jump 3.0
/jumppad setsound spawn_jump ENTITY_FIREWORK_ROCKET_LAUNCH

# 3. Emerald-Blöcke in der Region platzieren - fertig!
```

## Support

Bei Problemen oder Fragen:
1. Überprüfe die Server-Logs auf Fehlermeldungen
2. Stelle sicher, dass Folia 1.21.5+ verwendet wird
3. Verwende `/jumppad reload` nach Konfigurationsänderungen

## Lizenz

Dieses Plugin ist Open Source und kann frei verwendet und modifiziert werden.