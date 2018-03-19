package client.kimjiwon.customviewex.customview;

import android.view.View;
import android.widget.Toast;

/**
 * Created by kimjiwon on 2018. 3. 15..
 */

public class CheckCustomViewModel {
    private int mSelected = 0;

    public void onClickStarButton(View view) {
        CheckCustomView checkCustomView = (CheckCustomView)view;
        increaseSelectedNumber();
        Toast.makeText(checkCustomView.getContext(), "" + mSelected + "번째 아이템 체크", Toast.LENGTH_SHORT).show();
        checkCustomView.setSelected(mSelected);
    }

    private void increaseSelectedNumber() {
        mSelected++;
        if (mSelected >= 3) {
            mSelected = 0;
        }
    }
}
