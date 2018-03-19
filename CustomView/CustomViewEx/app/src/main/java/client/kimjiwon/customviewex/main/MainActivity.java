package client.kimjiwon.customviewex.main;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import client.kimjiwon.customviewex.R;
import client.kimjiwon.customviewex.customview.CheckCustomViewModel;
import client.kimjiwon.customviewex.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding mainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(MainActivity.this,R.layout.activity_main);
        mainBinding.setViewModel(new CheckCustomViewModel());
    }
}
