# Prefixes

Prefixes ist ein Plugin, dass das Erscheinungsbild der Spielernamen verändert.

## Teams

Über Teams werden den Spielern die Displaynames verändert,
dabei wird unterschieden in Präfix, Suffix und den Namen des Spielers.

### teams.json

Über die Teams.json Datei werden die Teams festgelegt.
Jedes Team hat, zur eindeutigen Identifikation, eine UUID (`id`).
Über die ``priorityValue`` wird die Rangfolge in der Playerlist (Tablist) festgelegt, dabei gilt, je
niedriger, desto wichtiger.
Mit ``color`` wird die Farbe des Nametags gesetzt. Es stehen folgende Farben zur Auswahl:
``BLACK``
``DARK_BLUE``
``DARK_GREEN``
``DARK_AQUA``
``DARK_RED``
``DARK_PURPLE``
``GOLD``
``GRAY``
``DARK_GRAY``
``BLUE``
``GREEN``
``AQUA``
``RED``
``LIGHT_PURPLE``
``YELLOW``
``WHITE``
``MAGIC``
``BOLD``
``STRIKETHROUGH``
``UNDERLINE``
``ITALIC``

``Tab`` verändert das Design der Tablist, `scoreboard` bestimmt das Aussehen des Namens über dem
Spieler und `chat` das Erscheinungsbild im Chat.
Mit den `useCustomColors` wird bestimmt, ob das persönliche Farbdesign des Spielers berücksichtigt
wird oder nicht.
Mit ``permission`` wird bestimmt welche Berechtigung ein Spieler braucht, um in dieses Team zu
kommen.
Nach dem Plugin start, werden standardmäßig zwei Teams als Beispiel erstellt.

<br>

In ``defaultTeam`` wird eingetragen, welches Team als Standard gesetzt werden soll, wenn der Spieler
nicht die nötige Berechtigung für ein anderes Team hat. Eingetragen wird die `id` des Teams.

## Colors

Farben sind anpassungen an der färbung des Namens.

### colors.json

Über ``type`` wird festgelegt, von welchem Typ (Muster) eine Farbe ist. Nach dem Pluginstart wird
für jeden Type Color ein Mustereintrag in der Config erstellt. Es stehen folgende Typen zur Auswahl:

`multi_color_prefix` verschieden Farben werden nacheinander in einem bestimmten
Abstand (`skipAfter`) wiederholt.

``one_color_prefix`` eine Farbe, die den ganzen Nametag einfärbt.

``prefix_name_suffix`` drei Farben, die jeweils für den Präfix, Namen und Suffix getrennt gesetzt
werden.

Unter ``data`` wird die Konfiguration abhängig von `type` eingetragen.
<br>

Über ``displayname`` wird bestimmt, wie der Displayname des Items bei `/color` sein soll.

Mit ``permission`` wird die Berechtigung festgelegt, die ein Spieler braucht, um diese Farbe zu
verwenden.

Die ``uuid`` ist die Identifikation der Farbe, sie muss für jede Farbe einzigartig sein.
UUID's können unter https://www.uuidgenerator.net/ generiert werden.

Die restlichen Konfigurationen sind abhängig vom ``type``:

#### multi_color_prefix

``colors`` ist eine Stringliste mit den Farben, die verwendet werden sollen.
``skipAfter`` ist die Anzahl an Buchstaben, nach der eine neue Farbe gewählt werden soll.

#### one_color_prefix

``color`` ist die Farbe, mit der der Nametag gefärbt werden soll.

#### prefix_name_suffix

``prefixColor`` ist die Farbe, mit der der Präfix gefärbt werden soll.
``nameColor`` ist die Farbe, mit der der Name gefärbt werden soll.
``suffixColor`` ist die Farbe, mit der der Suffix gefärbt werden soll.

## config.yml

In der ``config.yml`` wird unter `database` eine Datenbank verbindung eingetragen.
In ``updatetime`` werden die Sekunden eingetragen, nach denen die Teams und Farben neu geladen werden
sollen.

## API für Entwickler

Jeder Spieler bekommt beim Betreten eine ``PlayerData`` geladen.
Die ``PlayerData`` wird in ``PlayerDataHolder.class`` gespeichert und kann daraus geladen werden.
An die Instanz des ``PlayerDataHolder`` kommt man über `Prefixes.getInstance().get(PlayerDataHolder.class)`.

Mit ``PlayerData.setCustomTeam(Team team)`` kann man einem Spieler ein CustomTeam setzen.
Das CustomTeam wird priorisiert vor dem normalen Team angezeigt, 
dadurch kann beispielsweise der Teamname in einem Spielmodus angezeigt werden.

``PrefixUpdateTask.class`` erbt von Runnable und kann durch  `Prefixes.getInstance().get(PrefixUpdateTask.class)` aufgerufen werden.
Über `PrefixUpdateTask.run()` können die Nametags aktualisiert werden.

## Colorcodes

Colorcodes werden unterstützt und mit dem ``&`` Zeichen eingeleitet. Es werden sowohl die klassischen Colorcodes:
`a`, `b`, `c`, `d`, `e`, `f`, `k`, `l`, `m`, `n`, `o`, `r`, `1`, `2`, `3`, `4`, `5`, `6`, `7`, `8`, `9`, als auch die modernen Hex Codes unterstützt, die mit  einem `&` und `#` eingeleitet werden (z.B.: `&#EDB5FF`)

## Befehl /color

Der Befehl /color öffnet das Farbauswahl Inventar. In diesem Inventar kann eine Farbe ausgewählt oder wieder gelöscht werden.
Es werden nur die Farben angezeigt, für die eine Berechtigung vorhanden ist.