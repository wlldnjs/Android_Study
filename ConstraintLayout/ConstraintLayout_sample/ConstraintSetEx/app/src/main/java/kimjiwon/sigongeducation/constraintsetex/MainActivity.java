package kimjiwon.sigongeducation.constraintsetex;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.support.transition.TransitionManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    protected boolean menuState = false;

    protected Button menuBtn, keypadBtn, changeViewBtn;
    protected ImageButton studyHelperBtn;

    ConstraintSet closeMenuBarConstraintSet;
    ConstraintSet openMenuBarConstraintSet;
    ConstraintLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rootLayout = findViewById(R.id.constraint);

        menuBtn = findViewById(R.id.menuBtn);
        studyHelperBtn = findViewById(R.id.studyHelperBtn);
        keypadBtn = findViewById(R.id.keypadBtn);
        changeViewBtn = findViewById(R.id.changeViewBtn);

        menuBtn.setOnClickListener(this);
        changeViewBtn.setOnClickListener(this);

        closeMenuBarConstraintSet = new ConstraintSet();
        closeMenuBarConstraintSet.clone(rootLayout);

        openMenuBarConstraintSet = new ConstraintSet();
        openMenuBarConstraintSet.clone(rootLayout);
        openMenuBarConstraintSet.clear(R.id.menuBar, ConstraintSet.LEFT);
        openMenuBarConstraintSet.connect(R.id.menuBar, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT);

    }

    @Override
    public void onClick(View v) {
        TransitionManager.beginDelayedTransition(rootLayout);
        closeMenuBarConstraintSet.applyTo(rootLayout);
        switch (v.getId()) {
            case R.id.menuBtn:
                menuState = !menuState;
                menuStateChange();
                break;

            case R.id.changeViewBtn:
                menuState = false;
                menuStateChange();
                startActivity(new Intent(getApplicationContext(),Main2Activity.class));
                break;
        }
    }

    protected void menuStateChange(){
        if (menuState) {
            menuBtn.setBackgroundColor(Color.parseColor("#222222"));
            studyHelperBtn.setBackgroundColor(Color.WHITE);
            keypadBtn.setBackgroundColor(Color.parseColor("#fffde8"));
            openMenuBarConstraintSet.applyTo(rootLayout);
        } else {
            menuBtn.setBackgroundColor(Color.parseColor("#444444"));
            studyHelperBtn.setBackgroundColor(Color.parseColor("#f2f2f2"));
            keypadBtn.setBackgroundColor(Color.parseColor("#fcf368"));
            closeMenuBarConstraintSet.applyTo(rootLayout);
        }
    }
}
