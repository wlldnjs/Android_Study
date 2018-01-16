package kimjiwon.sigongeducation.recyclerviewex2.view;

import android.databinding.ObservableBoolean;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import kimjiwon.sigongeducation.recyclerviewex2.databinding.LectureProgressItemBinding;
import kimjiwon.sigongeducation.recyclerviewex2.databinding.LectureRecyclerItemBinding;
import kimjiwon.sigongeducation.recyclerviewex2.model.LectureModel;

/**
 * Created by kimjiwon on 2018. 1. 2..
 */

public class LectureAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // 총 데이터.
    private ArrayList<LectureModel> totalLectureList;
    // recycleView에 보여지는 데이터
    private ArrayList<LectureModel> lectureList;
    // adapter가 존재하는 recyclerView
    private RecyclerView recyclerView;
    // 더 읽을 데이터가 있는지 판별하는 변수. true면 존재, false면 없음.
    public final ObservableBoolean isEndData = new ObservableBoolean(false);

    // viewType으로 지정될 상수.
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    // 현재 보여지고있는 페이지의 번호. 한 페이지에 20개의 데이터가 보여진다.
    private int pageNumber = 0;

    // Adapter의 생성자. 생성시 recyclerView 객체를 받아 ScrollListener를 달아준다.
    public LectureAdapter(RecyclerView recyclerView) {
        totalLectureList = new ArrayList<>();
        lectureList = new ArrayList<>();
        this.recyclerView = recyclerView;
        this.recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // canScrollVertically 메서드를 사용하여 스크롤이 어디에 위치하였는지 판단.
                if (!recyclerView.canScrollVertically(1)) {
                    Log.d("recyclerView", "하단");
                    addItemByPageNum();
                }
            }
        });
    }

    // viewType에 따라 다른 뷰홀더를 바인딩.
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            LectureRecyclerItemBinding binding = LectureRecyclerItemBinding.
                    inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new LectureViewHolder(binding);
        } else if (viewType == TYPE_FOOTER) {
            LectureProgressItemBinding binding = LectureProgressItemBinding.
                    inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new FooterViewHolder(binding);
        }

        return null;
    }

    // 홀더의 타입에 따라 다른 데이터를 바인드.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof LectureViewHolder) {
            ((LectureViewHolder) holder).bind(lectureList.get(position));
        } else if (holder instanceof FooterViewHolder) {
            ((FooterViewHolder) holder).bind(isEndData);
        }
    }

    // footer를 추가하기 위해 lectureList의 크기보다 하나 더해줌.
    @Override
    public int getItemCount() {
        return lectureList.size() + 1;
    }

    // 마지막 포지션의 데이터에 TYPE_FOOTER viewType 지정
    @Override
    public int getItemViewType(int position) {
        if (position >= lectureList.size()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    // 초기에 데이터를 세팅해주는 부분.
    // 처음 데이터가 20개를 넘지 않으면 모두 보여주고, 20개가 넘는다면 20개만 출력.
    public void setItem(ArrayList<LectureModel> lectureList) {
        if (lectureList == null) {
            return;
        }
        totalLectureList = lectureList;
        if (totalLectureList.size() < 20) {
            this.lectureList.addAll(lectureList);
            isEndData.set(true);
            notifyDataSetChanged();
        } else {
            addItemByPageNum();
        }

    }

    // pageNumber 변수의 값에 따라 총 데이터에서 20개씩 데이터를 나눠 보여줌.
    private void addItemByPageNum() {
        if (isEndData.get() == false) {
            for (int i = (pageNumber * 20); i < (pageNumber * 20 + 20); i++) {
                // 더이상 보여줄 데이터가 없다면 isEndData를 true로 바꾸고 break
                if (i >= totalLectureList.size()) {
                    isEndData.set(true);
                    break;
                }
                this.lectureList.add(totalLectureList.get(i));
            }
            pageNumber++;
            notifyDataSetChanged();
        }
    }


    public static class LectureViewHolder extends RecyclerView.ViewHolder {
        LectureRecyclerItemBinding lectureRecyclerItemBinding;

        public LectureViewHolder(LectureRecyclerItemBinding binding) {
            super(binding.getRoot());
            lectureRecyclerItemBinding = binding;
        }

        private void bind(LectureModel lectureModel) {
            lectureRecyclerItemBinding.setModel(lectureModel);
        }
    }

    public static class FooterViewHolder extends RecyclerView.ViewHolder {
        LectureProgressItemBinding lectureProgressItemBinding;

        public FooterViewHolder(LectureProgressItemBinding binding) {
            super(binding.getRoot());
            lectureProgressItemBinding = binding;
        }

        private void bind(ObservableBoolean isEndData) {
            lectureProgressItemBinding.setIsEndData(isEndData);
        }
    }
}
