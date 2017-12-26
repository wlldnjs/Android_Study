package kimjiwon.sigongeducation.databindingex;

import android.databinding.adapters.TextViewBindingAdapter;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

/**
 * Created by kimjiwon on 2017. 12. 19..
 */

public class Handlers {

    // UserData 객체와 ObservableUserData 객체의 값을 변환하는 메서드(onClick)
    public void onChangedData(View view, UserData userData, ObservableUserData obUserData) {
        Log.d("변경 전 User객체 Data", userData.getUserName() + ", " + userData.getUserMemo());
        Log.d("변경 전 ObUser객체 Data", obUserData.obUserName.get() + ", " + obUserData.obUserMemo.get());
        userData.setUserName("kimjiwon");
        userData.setUserMemo("데이터 변경");
        obUserData.obUserName.set("김지원");
        obUserData.obUserMemo.set("아이스크림에듀");
        Toast.makeText(view.getContext(), "데이터 변경! 내용은 로그를 확인하세요.", Toast.LENGTH_SHORT).show();
        Log.d("변경 후 User객체 Data", userData.getUserName() + ", " + userData.getUserMemo());
        Log.d("변경 후 ObUser객체 Data", obUserData.obUserName.get() + ", " + obUserData.obUserMemo.get());
    }

    // ObservableNumberData 객체의 obIntData값을 증가시키는 메서드(onClick)
    public void onCountInc(ObservableNumberData obNumData) {
        int numData = obNumData.obIntData.get();
        numData++;
        obNumData.obIntData.set(numData);
    }

    // ObservableNumberData 객체의 obIntData값을 감소시키는 메서드(onLongClick)
    public boolean onCountDec(ObservableNumberData obNumData) {
        int numData = obNumData.obIntData.get();
        numData--;
        obNumData.obIntData.set(numData);
        return true;
    }

    // ObservableBooleanData 객체의 obBoolData의 값을 true/false로 스위칭 해주는 메서드(onCheckedChanged)
    public void onCheckboxSelect(ObservableBooleanData obBoolData, boolean isChecked) {
        if (isChecked) {
            obBoolData.obBoolData.set(true);
        } else {
            obBoolData.obBoolData.set(false);
        }
    }

    // ObservableNumberData 객체의 obCheckBoxIntData값을 증가시키는 메서드(onCheckedChanged)
    public void onCheckboxSelectIncNum(ObservableNumberData obNumData) {
        int numData = obNumData.obCheckBoxIntData.get();
        numData++;
        obNumData.obCheckBoxIntData.set(numData);
    }

    // afterTextChanged 메서드에서 EditText에 입력된 값을 저장하는데 사용되는 변수
    public String textData = "";

    // EditText에서 입력된 값을 textData 변수에 저장하는 메서드
    public TextViewBindingAdapter.AfterTextChanged afterTextChanged = new TextViewBindingAdapter.AfterTextChanged() {
        @Override
        public void afterTextChanged(Editable s) {
            textData = s.toString();
        }
    };

    // ObservableStringData 객체의 obStringData값을 EditText에 입력되어있는 값으로 변환하는 메서드
    public void onTextChange(ObservableStringData obStringData) {
        obStringData.obStringData.set(textData);
    }
}
