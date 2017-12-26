package kimjiwon.sigongeducation.mvvmdesignpatternex.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import kimjiwon.sigongeducation.mvvmdesignpatternex.R;
import kimjiwon.sigongeducation.mvvmdesignpatternex.databinding.ActivityMainBinding;
import kimjiwon.sigongeducation.mvvmdesignpatternex.viewmodel.UserViewModel;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        UserViewModel viewModel = new UserViewModel();
        binding.setViewModel(viewModel);
    }
}
