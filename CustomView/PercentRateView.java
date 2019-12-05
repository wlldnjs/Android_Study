
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;

public class PercentRateView extends ConstraintLayout {
    private LayoutPercentrateBinding mBinding;
    private PercentRateViewModel mViewModel;

    public PercentRateView(Context context) {
        super(context);
        initView(context);
    }

    public PercentRateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        getAttrs(attrs);
    }

    public PercentRateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        getAttrs(attrs, defStyleAttr);
    }

    private void initView(Context context) {
        mBinding = LayoutPercentrateBinding.inflate(LayoutInflater.from(context), this, false);
        mViewModel = new PercentRateViewModel();
        mBinding.setViewModel(mViewModel);
        addView(mBinding.getRoot());
    }

    private void getAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.PercentageRateView);
        setTypeArray(typedArray);
    }

    private void getAttrs(AttributeSet attrs, int defStyle) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.PercentageRateView, defStyle, 0);
        setTypeArray(typedArray);
    }

    private void setTypeArray(TypedArray typedArray) {
        int percentColor = typedArray.getColor(R.styleable.PercentageRateView_percentColor, ContextCompat.getColor(getContext(), android.R.color.transparent));
        mBinding.viewProgressRate.setBackgroundColor(percentColor);

        Drawable percentColorBackground = typedArray.getDrawable(R.styleable.PercentageRateView_percentBackground);
        if (null != percentColorBackground) {
            mBinding.viewProgressRate.setBackground(percentColorBackground);
        }

        int backgroundColor = typedArray.getColor(R.styleable.PercentageRateView_backgroundColor, ContextCompat.getColor(getContext(), R.color.cyonMidGray05));
        mBinding.viewProgressRateBackground.setBackgroundColor(backgroundColor);

        String textString = typedArray.getString(R.styleable.PercentageRateView_textString);
        mViewModel.text.set(textString);

        float textSize = typedArray.getDimension(R.styleable.PercentageRateView_textSize, getResources().getDimension(R.dimen.percentage_rate_view_text));
        mBinding.text.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

        float subTextSize = typedArray.getDimension(R.styleable.PercentageRateView_subTextSize, getResources().getDimension(R.dimen.percentage_rate_view_text_sub));
        mBinding.textSub.setTextSize(TypedValue.COMPLEX_UNIT_PX, subTextSize);

        float percentSize = typedArray.getDimension(R.styleable.PercentageRateView_percentSize, getResources().getDimension(R.dimen.percentage_rate_view_percent_text));
        mBinding.textPercent.setTextSize(TypedValue.COMPLEX_UNIT_PX, percentSize);

        boolean percentTextEnabled = typedArray.getBoolean(R.styleable.PercentageRateView_percentTextEnabled, true);
        mBinding.textPercent.setVisibility(percentTextEnabled ? View.VISIBLE : View.GONE);

        typedArray.recycle();
        mBinding.executePendingBindings();
    }

    public void setPercent(int percent) {
        mViewModel.percent.set(percent);
        mViewModel.guidePercent.set(percent / 100f);
    }

    public void setText(String text) {
        mViewModel.text.set(text);
    }

    public void setSubText(String text) {
        mViewModel.subText.set(text);
    }

    public void setGradientResourceBySubjectDiv(String subjectDiv) {
        if (Constants.SUBJECT_KOREAN.equals(subjectDiv)) {
            mBinding.viewProgressRate.setBackgroundResource(R.drawable.shape_gradient_marineblue03_violet06);
            mBinding.viewProgressRateBackground.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.riverblue02));
        } else if (Constants.SUBJECT_MATH.equals(subjectDiv)) {
            mBinding.viewProgressRate.setBackgroundResource(R.drawable.shape_gradient_flame03_pink05);
            mBinding.viewProgressRateBackground.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.flame02));
        } else if (Constants.SUBJECT_SOCIETY.equals(subjectDiv)) {
            mBinding.viewProgressRate.setBackgroundResource(R.drawable.shape_gradient_olive04_mandarin02);
            mBinding.viewProgressRateBackground.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.olive03));
        } else if (Constants.SUBJECT_ENGLISH.equals(subjectDiv)) {
            mBinding.viewProgressRate.setBackgroundResource(R.drawable.shape_gradient_violet05_purple04);
            mBinding.viewProgressRateBackground.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.violet03));
        } else if (Constants.SUBJECT_SCIENCE.equals(subjectDiv)) {
            mBinding.viewProgressRate.setBackgroundResource(R.drawable.shape_gradient_babygreen03_babygreen02);
            mBinding.viewProgressRateBackground.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.bluegreen00));
        } else if (Constants.SUBJECT_HISTORY.equals(subjectDiv)) {
            mBinding.viewProgressRate.setBackgroundResource(R.drawable.shape_gradient_cocoa01_coffee03);
            mBinding.viewProgressRateBackground.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.coffee02));
        } else if (Constants.SUBJECT_SPECIAL.equals(subjectDiv)) {
            mBinding.viewProgressRate.setBackgroundResource(R.drawable.shape_gradient_marineblue03_violet06);
            mBinding.viewProgressRateBackground.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.blue_down00));
        } else {
            mBinding.viewProgressRate.setBackgroundResource(R.drawable.shape_gradient_pink04_orange01);
            mBinding.viewProgressRateBackground.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.pink03));
        }
    }

    public void setResourceBySubjectDiv(String subjectDiv) {
        if (Constants.SUBJECT_KOREAN.equals(subjectDiv)) {
            mBinding.viewProgressRate.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.marineblue03));
        } else if (Constants.SUBJECT_MATH.equals(subjectDiv)) {
            mBinding.viewProgressRate.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.flame03));
        } else if (Constants.SUBJECT_SOCIETY.equals(subjectDiv)) {
            mBinding.viewProgressRate.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.olive04));
        } else if (Constants.SUBJECT_ENGLISH.equals(subjectDiv)) {
            mBinding.viewProgressRate.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.violet05));
        } else if (Constants.SUBJECT_SCIENCE.equals(subjectDiv)) {
            mBinding.viewProgressRate.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.babygreen03));
        } else if (Constants.SUBJECT_HISTORY.equals(subjectDiv)) {
            mBinding.viewProgressRate.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.cocoa01));
        } else if (Constants.SUBJECT_SPECIAL.equals(subjectDiv) || Constants.SUBJECT_ENGLISH_LISTEN.equals(subjectDiv)) {
            mBinding.viewProgressRate.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.mainblue03));
        } else {
            mBinding.viewProgressRate.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.pink04));
        }
    }
}
