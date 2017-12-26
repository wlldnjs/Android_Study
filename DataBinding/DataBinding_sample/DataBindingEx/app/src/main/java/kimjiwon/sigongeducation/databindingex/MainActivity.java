package kimjiwon.sigongeducation.databindingex;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import kimjiwon.sigongeducation.databindingex.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Binding 클래스를 이용하여 Activity에 레이아웃 리소스를 적용
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        // binding된 레이아웃에 존재하는 userData변수와 UserData클래스를 연결
        UserData userData = new UserData("김지원", "안녕하세요");
        binding.setUserData(userData);

        // binding된 레이아웃에 존재하는 obUserData변수와 ObservableUserData클래스를 연결
        ObservableUserData observableUserData = new ObservableUserData();
        observableUserData.obUserName.set("Kimjiwon");
        observableUserData.obUserMemo.set("시공교육");
        binding.setObUserData(observableUserData);

        // binding된 레이아웃에 존재하는 handlers변수와 Handler클래스를 연결
        Handlers handlers = new Handlers();
        binding.setHandlers(handlers);

        // binding된 레이아웃에 존재하는 obNumberData변수와 ObservableNumberData클래스를 연결
        ObservableNumberData obNumData = new ObservableNumberData();
        obNumData.obIntData.set(0);
        obNumData.obCheckBoxIntData.set(0);
        binding.setObNumData(obNumData);

        // binding된 레이아웃에 존재하는 obBoolData변수와 ObservableBooleanData클래스를 연결
        ObservableBooleanData obBoolData = new ObservableBooleanData();
        obBoolData.obBoolData.set(false);
        binding.setObBoolData(obBoolData);

        // binding된 레이아웃에 존재하는 obStringData변수와 ObservableStringData클래스를 연결
        ObservableStringData obStringData = new ObservableStringData();
        obStringData.obStringData.set("기본 텍스트");
        binding.setObStringData(obStringData);
    }
}
