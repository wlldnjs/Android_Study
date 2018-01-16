package kimjiwon.sigongeducation.recyclerviewex2.view;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

import kimjiwon.sigongeducation.recyclerviewex2.R;
import kimjiwon.sigongeducation.recyclerviewex2.databinding.ActivityMainBinding;
import kimjiwon.sigongeducation.recyclerviewex2.model.ButtonModel;
import kimjiwon.sigongeducation.recyclerviewex2.model.LectureModel;
import kimjiwon.sigongeducation.recyclerviewex2.viewmodel.LectureViewModel;

public class MainActivity extends AppCompatActivity {
    private LectureViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        viewModel = new LectureViewModel();
        binding.setViewModel(viewModel);

        LectureAdapter adapter = new LectureAdapter(binding.lectureRecyclerView);
        binding.lectureRecyclerView.setAdapter(adapter);

        viewModel.btnModel = new ButtonModel(R.mipmap.ic_launcher,R.mipmap.ic_launcher_round);

        addItem();

    }

    protected void addItem() {
        for (int i = 1; i < 90; i++) {
            viewModel.lectureList.add(new LectureModel(i, i + "번째 강의", 20 + i, i * 2));
        }
    }

    @BindingAdapter("item")
    public static void setItem(RecyclerView recyclerView, ArrayList<LectureModel> lectureList) {
        LectureAdapter adapter = (LectureAdapter) recyclerView.getAdapter();
        adapter.setItem(lectureList);
    }

    @BindingAdapter("onTouchBtn")
    public static void onTouchBtn(final ImageView imageView, final ButtonModel btnModel){
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    imageView.setImageResource(btnModel.downBtn);

                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    imageView.setImageResource(btnModel.upBtn);
                }

                return false;
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "버튼이 눌렸어요", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
