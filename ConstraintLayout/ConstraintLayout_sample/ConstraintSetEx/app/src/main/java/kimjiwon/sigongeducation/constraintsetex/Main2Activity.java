package kimjiwon.sigongeducation.constraintsetex;

import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.transition.TransitionManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Main2Activity extends AppCompatActivity {
    protected boolean tabState = false;
    protected ImageView bottomTabBtn, tabIcon;

    ConstraintSet closeMenuBoxConstraintSet;
    ConstraintSet openMenuBoxConstraintSet;
    ConstraintLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        bottomTabBtn = findViewById(R.id.bottom_tab_btn);
        tabIcon = findViewById(R.id.tab_btn);
        rootLayout = findViewById(R.id.constraint2);

        closeMenuBoxConstraintSet = new ConstraintSet();
        closeMenuBoxConstraintSet.clone(rootLayout);

        openMenuBoxConstraintSet = new ConstraintSet();
        openMenuBoxConstraintSet.clone(rootLayout);
        openMenuBoxConstraintSet.clear(R.id.bottom_tab, ConstraintSet.TOP);
        openMenuBoxConstraintSet.connect(R.id.bottom_tab, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);

        bottomTabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabState = !tabState;
                TransitionManager.beginDelayedTransition(rootLayout);
                if (tabState) {
                    openMenuBoxConstraintSet.applyTo(rootLayout);
                    tabIcon.setImageResource(R.drawable.today_ic_down);
                } else {
                    closeMenuBoxConstraintSet.applyTo(rootLayout);
                    tabIcon.setImageResource(R.drawable.today_ic_up);
                }
            }
        });
    }
}
