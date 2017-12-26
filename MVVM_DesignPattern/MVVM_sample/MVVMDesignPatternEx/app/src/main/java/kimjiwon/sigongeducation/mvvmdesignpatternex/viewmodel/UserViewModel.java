package kimjiwon.sigongeducation.mvvmdesignpatternex.viewmodel;

import android.content.Intent;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.databinding.adapters.TextViewBindingAdapter;
import android.text.Editable;
import android.util.Log;
import android.view.View;

import kimjiwon.sigongeducation.mvvmdesignpatternex.model.User;
import kimjiwon.sigongeducation.mvvmdesignpatternex.view.SecondActivity;

/**
 * Created by kimjiwon on 2017. 12. 22..
 */

public class UserViewModel {
    private User model;

    public final ObservableField<String> userName = new ObservableField<>();
    public final ObservableInt userNumber = new ObservableInt();

    public UserViewModel() {
        model = new User();
        model.setUserNumber(0);
        userName.set("변경 전 텍스트");
        userNumber.set(model.getUserNumber());
    }

    public TextViewBindingAdapter.AfterTextChanged setEditTextData = new TextViewBindingAdapter.AfterTextChanged() {
        @Override
        public void afterTextChanged(Editable s) {
            model.setEditTextData(s.toString());
        }
    };

    public void changeUserName() {
        model.setUserName();
        userName.set(model.getUserName());
    }

    public void incNumber() {
        int number = model.getUserNumber();
        number++;
        Log.d("number값", "" + number);
        model.setUserNumber(number);
        userNumber.set(number);
    }

    public void intentActivity(View view) {
        view.getContext().startActivity(new Intent(view.getContext(), SecondActivity.class));
    }
}
