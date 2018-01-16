package kimjiwon.sigongeducation.recyclerviewex2.viewmodel;

import android.databinding.ObservableArrayList;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import kimjiwon.sigongeducation.recyclerviewex2.model.ButtonModel;
import kimjiwon.sigongeducation.recyclerviewex2.model.LectureModel;

/**
 * Created by kimjiwon on 2018. 1. 2..
 */

public class LectureViewModel extends RecyclerView.OnScrollListener {
    public final ObservableArrayList<LectureModel> lectureList;
    public ButtonModel btnModel;

    public LectureViewModel() {
        lectureList = new ObservableArrayList<>();
    }

    public void onBtnClick(View view){
        Log.d("버튼 클릭","클릭클릭");
    }
}
