package kimjiwon.sigongeducation.constraintlayoutex2;

import android.app.Application;

import com.tsengvn.typekit.Typekit;

/**
 * Created by kimjiwon on 2017. 12. 6..
 */

public class FontApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Typekit.getInstance()
                .addNormal(Typekit.createFromAsset(this,"NanumSquareB.ttf"))
                .addBold(Typekit.createFromAsset(this,"NanumSquareEB.ttf"))
                .addBoldItalic(Typekit.createFromAsset(this,"NanumSquareR.ttf"));

    }

}
