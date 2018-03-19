package client.kimjiwon.customviewex.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import client.kimjiwon.customviewex.R;
import client.kimjiwon.customviewex.databinding.LayoutCheckCustomViewBinding;

/**
 * Created by kimjiwon on 2018. 3. 15..
 */

public class CheckCustomView extends ConstraintLayout {
    private LayoutCheckCustomViewBinding mBinding;
    private int mSelected = 0;

    public CheckCustomView(Context context) {
        super(context);
        initializeView(context, null);
    }

    public CheckCustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeView(context, attrs);
    }

    public CheckCustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeView(context, attrs);
    }

    private void initializeView(Context context, @Nullable AttributeSet attrs) {
        mBinding = LayoutCheckCustomViewBinding.inflate(LayoutInflater.from(context), this, false);
        addView(mBinding.getRoot());

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CheckCustomView);
            try {
                mSelected = typedArray.getInteger(0, 0);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                typedArray.recycle();
            }
        }
    }

    public void setSelected(int selected) {
        mSelected = selected;
        switch (mSelected) {
            case 0:
                mBinding.mark1.setImageResource(R.drawable.ic_hit_s);
                mBinding.mark2.setImageResource(R.drawable.ic_miss_s);
                mBinding.mark3.setImageResource(R.drawable.ic_miss_s);
                break;
            case 1:
                mBinding.mark1.setImageResource(R.drawable.ic_miss_s);
                mBinding.mark2.setImageResource(R.drawable.ic_hit_s);
                mBinding.mark3.setImageResource(R.drawable.ic_miss_s);
                break;
            case 2:
                mBinding.mark1.setImageResource(R.drawable.ic_miss_s);
                mBinding.mark2.setImageResource(R.drawable.ic_miss_s);
                mBinding.mark3.setImageResource(R.drawable.ic_hit_s);
                break;
        }
    }

    public int getSelected() {
        return mSelected;
    }

}
