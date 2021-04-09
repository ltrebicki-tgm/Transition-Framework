package com.example.transitionsek;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.ChangeBounds;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Scene;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Scene scene_anfang = null;
    private Scene scene1 = null;
    private Scene scene2 = null;
    private Scene scene3 = null;

    private String current_choice = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Spinner Adapter
        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinner, android.R.layout.simple_spinner_item);
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

    /**
     * Überschriebene Listener Methode wenn ein Item des Spinners selektiert wird.
     * Das ausgewählte Item wird in ein Attribut gespeichert, um später die gewünschte Transition ermitteln zu können.
     * @param parent die AdapterView, wo die Selektierung stattgefunden hat
     * @param view die View, in der AdapterView benutzt wurde
     * @param position die Position der View im Adapter
     * @param id die ID des ausgewählten Items
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        current_choice = parent.getItemAtPosition(position).toString();
    }
    /**
     * Callback Methode die aufgerufen wird, wenn die Auswahl von der View verschwindet.
     * Dies ist beispielsweise der Fall, wenn der Adapter leer wird.
     * @param parent die AdapterView, die nun kein Item mehr erhält
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // nicht benötigt
    }
    /**
     * Wechselt zu Scene 1 mit der ausgewählten Transition
     * @param view das View Objekt, das angeklickt wurde
     */
    public void to_scene_1(View view) {
        Transition transition = get_transition_type();
        TransitionManager.go(scene1, transition);
    }
    /**
     * Wechselt zu Scene 2 mit der ausgewählten Transition
     * @param view das View Objekt, das angeklickt wurde
     */
    public void to_scene_2(View view) {
        Transition transition = get_transition_type();
        TransitionManager.go(scene2, transition);
    }
    /**
     * Wechselt zu Scene 3 mit der ausgewählten Transition
     * @param view das View Objekt, das angeklickt wurde
     */
    public void to_scene_3(View view) {
        Transition transition = get_transition_type();
        TransitionManager.go(scene3, transition);
    }

    /**
     * Wechselt zur Beginn scene mit der ausgewählten Transition
     * @param view das View Objekt, das angeklickt wurde
     */
    public void to_scene_beginning(View view) {
        Transition transition = get_transition_type();
        TransitionManager.go(scene_anfang, transition);
    }
    /**
     * Ermittelt die gewünschte Transition die im Spinner ausgewählt wurde.
     * @return eine Subklasse der Transition Klasse, beispielsweise Fade oder Explode
     */
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

}
