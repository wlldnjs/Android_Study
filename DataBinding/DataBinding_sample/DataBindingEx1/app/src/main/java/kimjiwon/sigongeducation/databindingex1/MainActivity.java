package kimjiwon.sigongeducation.databindingex1;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import kimjiwon.sigongeducation.databindingex1.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // activity_main.xml에 databinding식을 작성함으로써 ActivityMainBinding클래스 자동 생성.
        // MainActivity와 activity_main을 연결.
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        // 레이아웃 상의 userList 변수에 값을 세팅해주는 작업.
        ObservableArrayList<UserData> userList = new ObservableArrayList<>();
        binding.setUserList(userList);

        // recyclerview adapter를 생성하여 레이아웃의 userdata_recyclerview에 adapter를 세팅.
        UserAdapter adapter = new UserAdapter();
        binding.userdataRecyclerview.setAdapter(adapter);

        // 더미 데이터
        userList.add(new UserData("김지원", 26, R.drawable.image1));
        userList.add(new UserData("이동수", 27, R.drawable.image2));
        userList.add(new UserData("김민수", 29, R.drawable.image3));

        Handlers handlers = new Handlers();
        binding.setHandlers(handlers);
    }
}
