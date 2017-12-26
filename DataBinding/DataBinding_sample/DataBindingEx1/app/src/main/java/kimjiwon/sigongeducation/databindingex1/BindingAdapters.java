package kimjiwon.sigongeducation.databindingex1;

import android.databinding.BindingAdapter;
import android.databinding.ObservableArrayList;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

/**
 * Created by kimjiwon on 2017. 12. 19..
 */

public class BindingAdapters {
    // @BindingAdapter를 통해 레이아웃의 UserList변수의 데이터를 가져와 Adapter로 전달주는 역할.
    // 레이아웃 상의 recyclerview의 속성에 xx:item="@{데이터객체}"로 이 메서드에 접근.
    @BindingAdapter("item")
    public static void bindItem(RecyclerView recyclerView, ObservableArrayList<UserData> userList) {
        UserAdapter adapter = (UserAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.setItem(userList);
        }
    }

    // UserData에 저장된 이미지주소값으로 이미지를 변경
    @BindingAdapter("image")
    public static void bindImage(ImageView imageView, int resid) {
        imageView.setImageResource(resid);
    }
}
