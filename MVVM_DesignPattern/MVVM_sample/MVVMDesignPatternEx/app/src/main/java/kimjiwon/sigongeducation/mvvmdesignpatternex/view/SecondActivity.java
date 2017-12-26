package kimjiwon.sigongeducation.mvvmdesignpatternex.view;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import kimjiwon.sigongeducation.mvvmdesignpatternex.R;
import kimjiwon.sigongeducation.mvvmdesignpatternex.databinding.ActivitySecondBinding;
import kimjiwon.sigongeducation.mvvmdesignpatternex.viewmodel.UserViewModel;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySecondBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_second);

        UserViewModel viewModel = new UserViewModel();
        binding.setViewModel(viewModel);
    }
}
