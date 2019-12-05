
import android.databinding.ObservableField;
import android.databinding.ObservableFloat;
import android.databinding.ObservableInt;

public class PercentRateViewModel {
    public ObservableField<String> text = new ObservableField<>();
    public ObservableField<String> subText = new ObservableField<>();
    public ObservableInt percent = new ObservableInt();
    public ObservableFloat guidePercent = new ObservableFloat();
}
