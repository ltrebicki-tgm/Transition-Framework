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

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Scene scene_anfang = null;
    private Scene scene1 = null;
    private Scene scene2 = null;
    private Scene scene3 = null;

    private Spinner spinner;
    private String current_choice = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Spinner Adapter
        spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


        // Scene Root sowie Erstellung der Scenes
        ViewGroup sceneRoot = (ViewGroup) findViewById(R.id.scene_root);
        scene_anfang = Scene.getSceneForLayout(sceneRoot, R.layout.scene_anfang, this);
        scene1 = Scene.getSceneForLayout(sceneRoot, R.layout.scene_eins, this);
        scene2 = Scene.getSceneForLayout(sceneRoot, R.layout.scene_zwei, this);
        scene3 = Scene.getSceneForLayout(sceneRoot, R.layout.scene_drei, this);

        scene_anfang.enter();

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        current_choice = parent.getItemAtPosition(position).toString();
        //Toast.makeText(getApplicationContext(), current_choice, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public void to_scene_1(View view) {
        Transition transition = get_transition_type();
        transition.setDuration(1000);
        Log.d("debug",transition+"");
        TransitionManager.go(scene1, transition);
    }

    public void to_scene_2(View view) {
        Transition transition = get_transition_type();
        Log.d("debug",transition+"");
        TransitionManager.go(scene2, transition);
    }

    public void to_scene_3(View view) {
        Transition transition = get_transition_type();
        Log.d("debug",transition+"");
        TransitionManager.go(scene3, transition);
    }

    public void to_scene_beginning(View view) {
        Transition transition = get_transition_type();
        Log.d("debug",transition+"");
        TransitionManager.go(scene_anfang, transition);
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
            case "Slide":
                return new Slide(Gravity.END);
        }
        return new AutoTransition();
    }

}
