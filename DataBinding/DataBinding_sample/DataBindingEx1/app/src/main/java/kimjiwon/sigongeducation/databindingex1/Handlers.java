package kimjiwon.sigongeducation.databindingex1;

import android.databinding.ObservableArrayList;
import android.util.Log;

/**
 * Created by kimjiwon on 2017. 12. 21..
 */

public class Handlers {
    public void onButtonClicked(ObservableArrayList<UserData> observableArrayList){
        observableArrayList.add(new UserData("샘플데이터", 10 ,R.drawable.image1));
        Log.d("유저데이터 수", ""+observableArrayList.size());

    }
}
