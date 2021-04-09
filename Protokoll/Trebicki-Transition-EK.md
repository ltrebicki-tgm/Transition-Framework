**Autor:** Lukas Trebicki
**Datum:** 29.03.2021
**Repository:** [Transition-Framework](https://github.com/ltrebicki-tgm/Transition-Framework)

# MEDT10 APP Transition Framework

# Aufgabenstellung

Befasst euch mit dem Kapitel "Animate layout changes using a transition" aus der [Android Developer Dokumentation](https://developer.android.com/training/transitions).

Erarbeitet euch das Thema und erstellt ein Beispielprojekt mit eigenständigen, beispielhaften Umsetzungen.

Erstellt ein Dokument über den theoretischen Hintergrund und protokolliert dabei auch eure Arbeit. Das erstellte PDF legt ihr entweder auch im Repo ab oder ladet es hier hoch.

# Theorie

Das Transition Framework von Android ermöglicht das Animierender UI mithilfe eines Start- und Endlayouts. Man wählt den Typ der Animation (Fade/Size) und den Rest macht das Framework von alleine.

Folgende Features sind enthalten:

- **Group-level animations**: Eine oder mehr Animationen auf alle Views in einer View Hierarchie
- **Built-in animations**: Vorerstellte Animationen für häufige Animationen wie einen Fade-Out oder eine Bewegung
- **Resource file support**: View Hierarchien und Animationen von Resource Files laden
- **Lifecycle callbacks**: Gibt Callbacks zurück die Kontrolle über die Animation und deren Hierarchie Änderungs Prozess enthalten

Der Prozess einer Animation zwischen zwei Layouts läuft folgendermaßen ab:

1. Ein ``Scene`` Objekt für das Start- und Endlayout erstellen. Das Startlayout wird aber oft automatisch vom aktuellen Layout bestimmt.
2. Ein ``Transition`` Objekt erstellen um den Typ der Animation zu bestimmen.
3. ``TransitionManager.go()`` aufrufen und es wird automatisch die Animation gestartet um die Layouts zu tauschen.

## Szene erstellen

Szenen speichern den State einer View Hierarchie mitsamt den Views und deren Eigenschaften. 

### Szene aus einem layout resource erstellen

Diese Möglichkeit sollte genutzt werden, wenn die View Hierarchie meist statisch ist. Um eine ``Scene`` Instanz von einem layout resource file zu erstellen, muss die Rootszene als ``ViewGroup`` Instanz hergestellt und dann die ```Scene.getSceneForLayout()`` Funktion aufgerufen werden.

Hier habe ich mich erstmal ziemlich nach dem Beispiel gerichtet, abgesehen davon, dass ich die Layouts etc. geändert habe.

``activity_main.xml``

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    ...
    tools:context=".MainActivity">
    <TextView
        ... />
    <FrameLayout
        android:id="@+id/scene_root"
        ...>
    </FrameLayout>
</androidx.constraintlayout.widget.ConstraintLayout>
```

``scene_anfang.xml``

```XML
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    ...>
    <TextView
        ...
        android:text="Text Line 1"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <TextView
        ...
        android:text="Text Line 2"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent" />
</androidx.constraintlayout.widget.ConstraintLayout>
```

````scene_ende.xml````

```XML
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    ...>
    <TextView
        ...
        android:text="Text Line 2"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <TextView
        ...
        android:text="Text Line 1"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent" />
</androidx.constraintlayout.widget.ConstraintLayout>
```

### Szene in Code erstellen

Diese Möglichkeit wird genutzt, wenn die View Hierarchien direkt im Code geändert oder generiert werden. Eine Scene wird mit dem ``Scene(sceneRoot, viewHierarchy)`` Konstruktor erstellt. Das ist das selbe, als würde man ``Scene.getSceneForLayout()`` aufrufen, nachdem man das Layout file inflated (gerendert) hat.

```Java
Scene mScene;

// Obtain the scene root element
sceneRoot = (ViewGroup) someLayoutElement;

// Obtain the view hierarchy to add as a child of
// the scene root when this scene is entered
viewHierarchy = (ViewGroup) someOtherLayoutElement;

// Create a scene
mScene = new Scene(sceneRoot, mViewHierarchy);
```

### Scene actions erstellen

Das Framework ermöglich es, benutzerdefinierte scene actions zu definieren, die das System ausführt wenn eine Scene betreten oder verlassen wird. Meistens ist das nicht nötig, da das Framework die Änderungen zwischen diesen Scenes sowieso automatisch animiert.

Zwei Anwendungsfälle:

- Views sollen animiert werden, welche nicht in der selben Hierarchie sind. Dann können Views für die Start- und Endscene mit Exit und Entry scene actions animiert werden.
- Views animieren, die das Transition Framework nicht automatisch animieren kann, zum Beispiel ``ListView`` Objekte.

Um eigene scene actions zu übergeben, müssen die actions als ``Runnable`` Objekt definiert und den Funktionen``Scene.setExitAction()`` oder ``Scene.setEnterAction()`` übergeben werden. Das Framework ruft die ``setExitAction()`` Funktion auf der Startscene vor der Transition animation und die ``setEnterAction`` Funktion auf der Endscene nach der Transition animation auf.

## Transition anwenden

Der Typ der Animation zwischen Scenes wird mit einem ``Transition`` Objekt festgelegt. Dies funktioniert mit mehreren bereits vorhandenen Subklassen wie ``AutoTransition`` oder ``Fade``. Es ist auch möglich, seine [eigene Klasse](https://developer.android.com/training/transitions/custom-transitions) zu überschreiben.

### Transition erstellen

#### Transition per resource file

Mit dieser Möglichkeit kann man die Transition-Definition einfach modifizieren, ohne den Code anzufassen.

Mit den folgenden Schritten wird eine bereits vorhandene transition in einem resource file eingebunden:

1. Das `res/transition/` Verzeichnis zum Projekt hinzufügen.
2. Ein neues XML resource file in diesem Verzeichnis erstellen.
3. Das entsprechende XML-Tag hinzufügen.

 So kann man die ``Fade`` Transition festlegen:

``res/transition/fade_transition.xml``

```xml
<fade xmlns:android="http://schemas.android.com/apk/res/android" />
```

Im Code:

```java
Transition fadeTransition =
        TransitionInflater.from(this).
        inflateTransition(R.transition.fade_transition);
```

#### Transition per code

Sinnvoll, wenn man Transition Objekte dynamisch erstellen und simple, eingebaute Transitions verwenden möchte. Indem man den public Konstruktor einer Subklasse der Transition Klasse aufruft, kann man wie im folgenden Beispiel eine Instanz der ``Fade ``Transition erstellen.

```Java
Transition fadeTransition = new Fade();
```

#### Transition anwenden

Für gewöhnlich wird eine Transition angewandt, um verschiedene View Hierarchien nach einem Event zu ändern. Beispiel: Der\*Die Benutzer\*in tippt in einer Suchleiste etwas ein. Schickt er das ab, lässt die App das Suchfenster verschwinden und blendet die Suchergebnisse ein.

Um eine Scene zu ändern während einer Transition-Anwendung muss die Funktion ``TransitionManger.go()`` mit der Endscene und der Transition aufgerufen werden:

```Java
TransitionManager.go(endingScene, fadeTransition);
```

# Umsetzung

Die Scenes werden in einem Framelayout getauscht. Über diesem befindet sich ein Spinner zum Auswählen der gewünschten Transition:

``activity_main.xml``

```XML
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <FrameLayout
        android:id="@+id/scene_root"
        ...
    </FrameLayout>

    <Spinner
        android:id="@+id/spinner"
        ... />
    
</androidx.constraintlayout.widget.ConstraintLayout>
```

Die Scenes an sich sind sich grundsätzlich ziemlich ähnlich, sie bestehen aus einem Button und TextViews. Als Beispiel wird die Anfangsscene herangezogen:

``scene_anfang.xml``

```XML
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/scene_container"
    android:layout_width="match_parent"
    android:layout_height="match_parent" >

    <TextView
        android:id="@+id/text_view2"
        ... />

    <TextView
        android:id="@+id/text_view1"
        ... />

    <Button
        android:id="@+id/button"
        android:layout_width="200dp"
        android:layout_height="200dp"
        android:background="#F4511E"
        android:onClick="to_scene_1"
        ... />
</androidx.constraintlayout.widget.ConstraintLayout>
```

In der ``onCreate`` Methode werden der Spinner sowie die Scenes aus den resource files erstellt.

```Java
@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Spinner Adapter
        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinner, 		android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter); // Adapter verknüpfen
        spinner.setPrompt("Transition wählen"); // Überschrift im Dialog
        spinner.setOnItemSelectedListener(this); // Listener setzen
        // Scene Root sowie Erstellung der Scenes
        ViewGroup sceneRoot = (ViewGroup) findViewById(R.id.scene_root);
        scene_anfang = Scene.getSceneForLayout(sceneRoot, R.layout.scene_anfang, this);
        scene1 = Scene.getSceneForLayout(sceneRoot, R.layout.scene_eins, this);
        scene2 = Scene.getSceneForLayout(sceneRoot, R.layout.scene_zwei, this);
        scene3 = Scene.getSceneForLayout(sceneRoot, R.layout.scene_drei, this);
        scene_anfang.enter(); // Um von Anfang an eine Scene im Framelayout zu haben
    }
```

Das Array ``R.array.spinner`` wird mithilfe eines in der strings.xml erstellten Arrays befüllt.

``strings.xml``

```XML
<resources>
    <string name="app_name">TransitionsEK</string>
    <string-array name="spinner">
        <item>Auto Transition</item>
        <item>Change Bounds</item>
        <item>Fade</item>
        <item>Explode</item>
        <item>Slide (Random)</item>
    </string-array>
</resources>
```

In der Listener Methode ``onItemSelected`` wird der Text der gewünschten, angeklickten Transition gespeichert. Wechselt man nun zu einer Scene, im Code wird dies mithilfe des ersten Scene-Wechsels verdeutlicht, wird mithilfe des Strings ein Objekt der gewollten Transition gespeichert. Mit dieser Transition wird nun in die nächste Szene gewechselt. 

```Java
@Override
public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
	current_choice = parent.getItemAtPosition(position).toString();
}

public void to_scene_1(View view) {
    Transition transition = get_transition_type();
	TransitionManager.go(scene1, transition);
}

public Transition get_transition_type() {
    switch(current_choice) {
        case "Auto Transition":
            return new AutoTransition();
        case "Change Bounds":
            return new ChangeBounds();
        case "Fade":
            return new Fade();
        case "Explode":
            return new Explode();
        case "Slide (Random)":
            // Zufällige Richtung
            int[] gravity_arr = {Gravity.START, Gravity.END, Gravity.TOP, Gravity.BOTTOM};
            return new Slide(gravity_arr[new Random().nextInt(gravity_arr.length)]);
    }
    return new AutoTransition();
}
```