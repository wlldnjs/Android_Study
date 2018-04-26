package client.kimjiwon.constraintlayout110;

import android.os.Bundle;
import android.support.constraint.Placeholder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Placeholder placeholder;
    private ImageView imageRound, imageSquare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageRound = findViewById(R.id.image_android_round);
        imageSquare = findViewById(R.id.image_android_square);
        placeholder = findViewById(R.id.placeholder);

        imageSquare.setOnClickListener(this);
        imageRound.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        placeholder.setContentId(v.getId());
    }
}
