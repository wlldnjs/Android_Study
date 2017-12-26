package kimjiwon.sigongeducation.databindingex1;

import android.databinding.ObservableArrayList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import kimjiwon.sigongeducation.databindingex1.databinding.UserRecyclerItemBinding;

/**
 * Created by kimjiwon on 2017. 12. 19..
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    // UserData의 List.
    ObservableArrayList<UserData> userList = new ObservableArrayList<>();

    // ViewHolder 생성 recyclerItem Binding클래스와 recyclerView를 연결.
    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        UserRecyclerItemBinding binding = UserRecyclerItemBinding.
                inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new UserViewHolder(binding);
    }

    // 각각의 View에 해당 position의 UserData를 바인딩.
    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        UserData userData = userList.get(position);
        holder.bind(userData);
    }

    // UserList의 크기.
    @Override
    public int getItemCount() {
        return userList.size();
    }

    // AdapterBinding에서 사용. 레이아웃에 정의된 UserList를 Adapter로 가져오기 위함.
    public void setItem(ObservableArrayList<UserData> userList) {
        if (userList == null) {
            return;
        }
        this.userList = userList;
        notifyDataSetChanged();   // 데이터 갱신을 위함.
    }

    // 이너클래스로 ViewHolder 클래스 생성.
    public class UserViewHolder extends RecyclerView.ViewHolder {
        // recyclerview의 아이템 레이아웃의 Binding클래스.
        UserRecyclerItemBinding binding;

        // 레이아웃상에 이미 변수를 생성하고 변수를 바인딩 하고있기 때문에 기존과 다르게 Viewholder 생성.
        public UserViewHolder(UserRecyclerItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        // 레이아웃상의 변수에 데이터를 넣기위해 bind 매서드 생성.
        private void bind(UserData userData) {
            binding.setUserData(userData);
//            binding.setVariable(BR.userData,userData);    // 위와 동일.
        }
    }
}
