### 다양한 화면에 맞는 디자인

안드로이드는 작은 스마트폰 화면부터 큰 TV화면까지 다양한 화면 크기를 가진 기기에서 구동됩니다.</br>
그렇기 때문에 최대한 모든 기기에 호환되게 디자인하여 많은 사용자가 사용할 수 있게 하는것이 중요합니다.</br>
더 나아가 여러 화면에 최적화된 사용자 인터페이스를 구현하는 방법까지 고려해야 합니다.</br>

#### 다양한 화면 크기 지원

> #### wrap_content와 match_parent

레이아웃이 다양한 화면 크기에 유연하게 적용시키려면 wrap_content와 match_parent를 사용합니다.</br>
wrap_content는 해당 뷰의 콘텐트에 맞는 최소 크기로 설정되게 되고, match_parent는 상위 뷰의 크기에 맞춰 구성요소를 확장합니다.</br>

    * fill_parent : match_parent와 동일, API버전 8 이하에서 사용, 지금은 사용되지않는다.

``` xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    <LinearLayout android:layout_width="match_parent" 
                  android:id="@+id/linearLayout1"  
                  android:gravity="center"
                  android:layout_height="50dp">
        <ImageView android:id="@+id/imageView1" 
                   android:layout_height="wrap_content"
                   android:layout_width="wrap_content"
                   android:src="@drawable/logo"
                   android:paddingRight="30dp"
                   android:layout_gravity="left"
                   android:layout_weight="0" />
        <View android:layout_height="wrap_content" 
              android:id="@+id/view1"
              android:layout_width="wrap_content"
              android:layout_weight="1" />
        <Button android:id="@+id/categorybutton"
                android:background="@drawable/button_bg"
                android:layout_height="match_parent"
                android:layout_weight="0"
                android:layout_width="120dp"
                style="@style/CategoryButtonStyle"/>
    </LinearLayout>

    <fragment android:id="@+id/headlines" 
              android:layout_height="fill_parent"
              android:name="com.example.android.newsreader.HeadlinesFragment"
              android:layout_width="match_parent" />
</LinearLayout>
```

wrap_content와 match_parent를 잘 활용하면 세로모드와 가로모드로 전환할 때 구성요소의 크기가 자동으로 변경되는 것을 확인 할 수 있다.</br>

![가로모드/세로모드](https://developer.android.com/images/training/layout-hvga.png)

> #### RelativeLayout 사용

중첩된 LinearLayout와 wrap_content/match_parent를 적절히 사용하면 복잡한 레이아웃을 구현할 수 있습니다.</br>
하지만 단순히 선형으로 배치되기 때문에 하위 뷰의 위치를 정확하게 제어할 수 없습니다.</br>
이처럼 일직선이 아닌 다양한 형태로 하위 뷰의 방향을 배치해야 한다면, 구성요소간의 공간을 기준으로 레이아웃을 지정하는 RelativeLayout을 사용하는 것이 좋습니다.</br>

``` xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    <TextView
        android:id="@+id/label"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Type here:"/>
    <EditText
        android:id="@+id/entry"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_below="@id/label"/>
    <Button
        android:id="@+id/ok"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_below="@id/entry"
        android:layout_alignParentRight="true"
        android:layout_marginLeft="10dp"
        android:text="OK" />
    <Button
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_toLeftOf="@id/ok"
        android:layout_alignTop="@id/ok"
        android:text="Cancel" />
</RelativeLayout>
```

아래의 그림은 크기가 다른 화면에서의 배치를 확인 할 수 있습니다.</br>

![이미지](https://developer.android.com/images/training/relativelayout1.png)
![이미지](https://developer.android.com/images/training/relativelayout2.png)

> #### 크기 한정자 사용

애플리케이션은 유연한 레이아웃을 구현하면서 다양한 화면구성을 위해 여러 대체 레이아웃을 제공해야 합니다.</br>
구성 한정자(configuration qualifiers)를 사용함으로써 현재 기기의 구성에 따라 적절한 리소스를 선택하게 도와줍니다.</br>

기본 단일 레이아웃(res/layout/main.xml)

``` xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <fragment android:id="@+id/headlines"
              android:layout_height="fill_parent"
              android:name="com.example.android.newsreader.HeadlinesFragment"
              android:layout_width="match_parent" />
</LinearLayout>
```

두개의 화면을 가진 레이아웃(res/layout-large/main.xml)

``` xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="fill_parent"
    android:layout_height="fill_parent"
    android:orientation="horizontal">
    <fragment android:id="@+id/headlines"
              android:layout_height="fill_parent"
              android:name="com.example.android.newsreader.HeadlinesFragment"
              android:layout_width="400dp"
              android:layout_marginRight="10dp"/>
    <fragment android:id="@+id/article"
              android:layout_height="fill_parent"
              android:name="com.example.android.newsreader.ArticleFragment"
              android:layout_width="fill_parent" />
</LinearLayout>
```

layout-large의 레이아웃은 큰화면으로 분류되는 기기에서 선택되며(예를들어 7인치 이상의 태블릿), 다른 레이아웃은 그보다 작은 기기에서 선택됩니다.</br>
    * 안드로이드 스크린의 크기는 4가지로 분류(X-Large : 10.1인치 이상, large : 5인치 이상, normal : 3인치~ 5인치, small : 3인치 미만)

> #### 최소 너비 한정자 사용

3.2버전 이전 기기에서는 큰화면에 비어있는 공간에 대한 문제가 있었습니다.</br>
이 비어있는 공간에 다양한 레이아웃들을 보여주기 위해 3.2버전 이상부터는 최소 너비 한정자(Smallest-width qualifier)를 통해 해결되었습니다.</br>
최소 너비 한정자는 dp기준으로 특정 최소 너비가 지정된 화면에서 사용가능합니다.</br>
예를들어 최소 너비가 600dp인 7인치 태블릿에서 UI를 두개의 화면으로 표시하고 싶다면, large 크기 한정자를 통해 구현할 수 있지만 sw600dp 한정자를 사용할 수 있습니다.

단일 화면 레이아웃(res/layout/main.xml)
``` xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <fragment android:id="@+id/headlines"
              android:layout_height="fill_parent"
              android:name="com.example.android.newsreader.HeadlinesFragment"
              android:layout_width="match_parent" />
</LinearLayout>
```

두개의 화면 레이아웃(res/layout-sw600dp/main.xml)
``` xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="fill_parent"
    android:layout_height="fill_parent"
    android:orientation="horizontal">
    <fragment android:id="@+id/headlines"
              android:layout_height="fill_parent"
              android:name="com.example.android.newsreader.HeadlinesFragment"
              android:layout_width="400dp"
              android:layout_marginRight="10dp"/>
    <fragment android:id="@+id/article"
              android:layout_height="fill_parent"
              android:name="com.example.android.newsreader.ArticleFragment"
              android:layout_width="fill_parent" />
</LinearLayout>
```
최소 너비가 600dp 이상인 기기는 layout-sw600dp/main.xml(두개의 화면) 레이아웃을 선택하고, 그보다 작은 화면은 layout/main.xml(단일 화면) 레이아웃을 선택합니다.</br>
하지만 3.2버전 이전 기기에서는 sw600dp를 인식하지 못하기 때문에 large 한정자를 사용하여야 합니다.</br>
하지만 large의 레이아웃과 sw600dp의 레이아웃이 같기 때문에 별칭을 사용하여 중복을 피할 수 있습니다.</br>

> #### 레이아웃 별칭 사용

동일한 레이아웃이 존재할 때 별칭 파일을 사용하여 파일이 중복되지 않게 관리 할 수 있습니다.</br>
기존의 두개의 화면 레이아웃을 res/layout/main_twopanes.xml로 정의하고 아래와 같은 large한정자와 sw600dp한정자의 레이아웃을 정의합니다.</br>

res/values-large/layout.xml
``` xml
<resources>
    <item name="main" type="layout">@layout/main_twopanes</item>
</resources>
```

res/values-sw600dp/layout.xml
``` xml
<resources>
    <item name="main" type="layout">@layout/main_twopanes</item>
</resources>
```


> #### 나인패치 비트맵 사용

나인패치는 늘어날 수 있는 비트맵 이미지 입니다.</br>
나인패치를 백그라운드로 설정하게 되면 컨텐츠의 크기에 맞춰 자동적으로 사이즈가 늘어나게 됩니다.</br>
나인패치는 가장자리에 1픽셀의 라인을 포함한는 표준 PNG 이미지로서 반드시 sample.9.png 확장자로 저장되어야 합니다.</br>
다양한 크기가 포함된 구성 요소에 사용할 비트맵을 디자인할 때는 항상 나인 패치를 사용하는것이 좋습니다.</br>


-----

### 다양한 밀도 지원

> #### 밀도에 독립적인 픽셀 사용

레이아웃을 디자인 할 때 피해야할 사항은 간격이나 크기를 지정할 때 픽셀을 사용하는 것 입니다.</br>
픽셀로 크기를 지정하게 되면 화면의 크기에 따라 픽셀의 밀도가 달라지기 때문에, 같은 픽셀이라도 다른 기기에서 나타나는 크기가 달라질 수 있는 문제가 발생합니다.</br>
그렇기 때문에 치수나 크기를 정의할 때는 dp 또는 sp 단위를 사용해야 합니다.</br>

    - dp : 다양한 화면 크기에서 동일한 비율로 출력되도록 하는 픽셀 단위.
    - sp : 사용자가 사용하는 텍스트의 크기에 맞게 조정.

두 뷰 사이에 공백을 지정할 때, px대신 dp 사용.

``` xml
<Button android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="@string/clickme"
    android:layout_marginTop="20dp" />
```

텍스트 크기를 지정할 때 sp사용

``` xml
<Button android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="@string/clickme"
    android:layout_marginTop="20dp" />
```

> #### 선택적인 비트맵 제공

안드로이드는 화면의 밀도가 다양하기 때문에 각각의 밀도에 맞는 비트맵 리소스를 적용해야 합니다.

![dp 이미지](http://blog.rightbrain.co.kr/CMS1/wp-content/uploads/2014/11/0000c.png)

이미지 파일들을 res 디렉터리 하위의 디렉터리들에 넣으면 시스템에서 자동으로 화면 밀도에 따라 알맞은 단위를 선택합니다.


### Implementing Adaptive UI Flows

어플리케이션에 표지되는 레이아웃에 따라 UI의 흐름이 다르게 적용될 수 있습니다.

> #### 현재 레이아웃 확인

먼저 사용자가 현재 보고있는 레이아웃을 확인합니다.</br>
이 코드를 통해 article이라는 id를 가진 레이아웃이 존재하는지 확인할 수 있습니다.</br>

``` java
public class NewsReaderActivity extends FragmentActivity {
    boolean mIsDualPane;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        View articleView = findViewById(R.id.article);
        mIsDualPane = articleView != null &&
                        articleView.getVisibility() == View.VISIBLE;
    }
}
```

> #### 현재 레이아웃에 따른 반응

현재의 레이아웃에 따라 일부 동작은 다른 결과가 나올 수 있습니다.</br>
예를들어 두개의 화면모드일 경우 왼쪽 창에서 항목을 선택하였을 때 오른쪽 화면에 콘텐츠가 표시될 것이며, 
단일 화면 모드일 경우 콘텐츠는 다른 액티비티에 단독 표시될 것입니다.</br>

``` java
@Override
public void onHeadlineSelected(int index) {
    mArtIndex = index;
    if (mIsDualPane) {
        /* display article on the right pane */
        mArticleFragment.displayArticle(mCurrentCat.getArticle(index));
    } else {
        /* start a separate activity */
        Intent intent = new Intent(this, ArticleActivity.class);
        intent.putExtra("catIndex", mCatIndex);
        intent.putExtra("artIndex", index);
        startActivity(intent);
    }
}
```

또한 아래의 코드는 두개의 화면 모드일 경우 탭이 있는 네이게이션 모드를 보여주고, 단일 화면 모드일 경우 스피너가 있는 네비게이션을 보여줍니다.

``` java
final String CATEGORIES[] = { "Top Stories", "Politics", "Economy", "Technology" };

public void onCreate(Bundle savedInstanceState) {
    ....
    if (mIsDualPane) {
        /* use tabs for navigation */
        actionBar.setNavigationMode(android.app.ActionBar.NAVIGATION_MODE_TABS);
        int i;
        for (i = 0; i < CATEGORIES.length; i++) {
            actionBar.addTab(actionBar.newTab().setText(
                CATEGORIES[i]).setTabListener(handler));
        }
        actionBar.setSelectedNavigationItem(selTab);
    }
    else {
        /* use list navigation (spinner) */
        actionBar.setNavigationMode(android.app.ActionBar.NAVIGATION_MODE_LIST);
        SpinnerAdapter adap = new ArrayAdapter(this,
                R.layout.headline_item, CATEGORIES);
        actionBar.setListNavigationCallbacks(adap, handler);
    }
}
```

> #### 다른 액티비티에서 프래그먼트 재사용

여러 화면에 반복적으로 나타나는 레이아웃이 있다면, Fragment의 서브클래스를 사용하여 중복을 피할 수 있습니다.

``` xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="fill_parent"
    android:layout_height="fill_parent"
    android:orientation="horizontal">
    <fragment android:id="@+id/headlines"
              android:layout_height="fill_parent"
              android:name="com.example.android.newsreader.HeadlinesFragment"
              android:layout_width="400dp"
              android:layout_marginRight="10dp"/>
    <fragment android:id="@+id/article"
              android:layout_height="fill_parent"
              android:name="com.example.android.newsreader.ArticleFragment"
              android:layout_width="fill_parent" />
</LinearLayout>
```

아래의 코드로 article 프래그먼트를 재사용 할 수 있습니다.

``` java
ArticleFragment frag = new ArticleFragment();
getSupportFragmentManager().beginTransaction().add(android.R.id.content, frag).commit();
```

> #### 화면 구성 변경

별도의 액티비티를 사용하여 인터페이스를 구현한다면, 인터페이스의 일관성을 유지하기 위해 특정 변화(회전 등)에 반응해야 할 수 있습니다.

``` java
public class ArticleActivity extends FragmentActivity {
    int mCatIndex, mArtIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCatIndex = getIntent().getExtras().getInt("catIndex", 0);
        mArtIndex = getIntent().getExtras().getInt("artIndex", 0);

        // If should be in two-pane mode, finish to return to main activity
        if (getResources().getBoolean(R.bool.has_two_panes)) {
            finish();
            return;
        }
        ...
}
```

-----

### 앱 바 추가하기

앱 바는 액션바라고도 불리며, 사용자에게 친근한 시각적 구조와 대화형 요소를 제공하기 때문에 앱에서 중요한 디자인요소 입니다.</br>
앱 바를 사용하면 다른 안드로이드 앱들과 일관성있게 유지할수 있기때문에 다른 사용자가 쉽게 작동방법을 이해 할 수 있습니다.</br>
주요기능은 다음과 같습니다.
- 앱의 이름을 지정하고, 사용자가 앱의 어느 위치에 있는지 알려주는 기능
- 기능에 접근(검색 등)
- 네비게이션 또는 뷰 전환(탭 또는 드롭다운 메뉴)

### 앱 바 설정

앱 바의 기본적인 형식은 한쪽에 액티비티 제목을 표시하고, 다른 한쪽에는 오버플로 메뉴를 표시하는 것 입니다.

![액션바 이미지](https://developer.android.com/images/training/appbar/appbar_basic.png)

안드로이드 3.0버전 부터는 기본 테마를 사용하는 모든 액티비티에 ActionBar가 앱 바로 제공됩니다.</br>
하지만 앱 바는 버전에 따라 다르게 동작하기 때문에 모든기기에 적용하기 위해서는 Toolbar의 서포트 라이브러리를 사용해야 합니다.</br>

> #### 액티비티에 툴바 추가

1. v7 AppCompat 서포트 라이브러리를 프로젝트에 추가합니다.

2. 액티비티에서 AppCompatActivity를 상속받습니다(앱에서 Toolbar를 사용하는 모든 액티비티에 적용해야 합니다).

``` java
public class MyActivity extends AppCompatActivity {
  // ...
}
```

3. 매니페스트에서 테마의 스타일을 NoActionBar테마중 하나로 변경합니다. 이렇게 하면 기존 제공되는 앱 바가 제거됩니다.

``` xml
<application
    android:theme="@style/Theme.AppCompat.Light.NoActionBar"
    />
```

4. Toolbar를 액티비티의 레이아웃에 추가합니다.

``` xml
<android.support.v7.widget.Toolbar
   android:id="@+id/my_toolbar"
   android:layout_width="match_parent"
   android:layout_height="?attr/actionBarSize"
   android:background="?attr/colorPrimary"
   android:elevation="4dp"
   android:theme="@style/ThemeOverlay.AppCompat.ActionBar"
   app:popupTheme="@style/ThemeOverlay.AppCompat.Light"/>
```

머터리얼 디자인에서는 앱 바의 elevation을 4dp로 권장합니다.</br>
툴바를 앱 바로 사용할 것이기 때문에 액티비티의 최상단에 배치합니다.

5. 액티비티의 onCreate() 메서드에서 setSupportActionBar()메서드를 호출하여 액티비티의 툴바를 전달합니다.
이 메서드는 툴바를 액티비티의 앱 바로 설정합니다.

``` java
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_my);
    Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
    setSupportActionBar(myToolbar);
    }
```

이 기본적인 툴바는 앱의 이름과 오버플로 메뉴가 포함되지만 더 많은 액션을 추가 할 수 있습니다.

> #### 앱 바의 유틸리티 메서드 사용

Toolbar를 액티비티의 앱 바로 설정했다면 v7 AppCompat 서포트 라이브러리의 액션바 클래스의 유틸리티 메서드를 사용 할 수 있습니다.</br>
액션바 클래스의 유틸리티 메서드를 사용하려면, 액티비티의 getSupportActionBar() 메서드를 호출하여 액션바를 반환받고, 해당 액션바를 통해 메서드를 호출할 수 있습니다.</br>

### 추가 및 처리작업

앱바에 사용자 작업을 위한 버튼을 추가할 수 있습니다.</br>
이를 통해 각 상활에 적절한 버튼을 앱 바에 바로 배치할 수 있고, 더 많은 버튼을 추가하고싶다면 오버플로 메뉴에 작업이 추가되도록 지정할 수 있습니다.</br>

> #### 액션 버튼 추가

액션 버튼과 오버플로 메뉴의 아이템들은 XML 메뉴 리소스에 정의됩니다.</br>
액션바에 액션을 추가하려면 res/menu/ 디렉터리에 xml파일을 추가해야 합니다.</br>
액션바에 아이템을 추가하기 위해서는 다음과 같은 코드를 작성해야 합니다.</br>

``` xml
<menu xmlns:android="http://schemas.android.com/apk/res/android" >

    <!-- "Mark Favorite", should appear as action button if possible -->
    <item
        android:id="@+id/action_favorite"
        android:icon="@drawable/ic_favorite_black_48dp"
        android:title="@string/action_favorite"
        app:showAsAction="ifRoom"/>

    <!-- Settings, should always be in the overflow -->
    <item android:id="@+id/action_settings"
          android:title="@string/action_settings"
          app:showAsAction="never"/>

</menu>
```

app:showAsAction 속성은 메뉴를 앱 바에 보이게 할지 지정하는 속성입니다.
- never : 항상 오버플로 메뉴에서만 보이게 설정
- ifRoom : 메뉴를 보일 여유공간이 있을때만 액션바에 아이콘 표시
- always : 항상 표시

> #### 액션에 대한 처리

사용자가 앱 바의 아이템을 선택하면 시스템은 해당 액티비티의 onOptionsItemSelected() 콜백 메서드에 클릭한 메뉴아이템 객체를 전달합니다.</br>
그 후 MenuItem.getItemId() 메서드를 통해 선택된 아이템의 id값을 받아오고, id에 해당하는 값을 <item>요소의 android:id값과 매치합니다.</br>


``` java
@Override
public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
        case R.id.action_settings:
            // User chose the "Settings" item, show the app settings UI...
            return true;

        case R.id.action_favorite:
            // User chose the "Favorite" action, mark the current item
            // as a favorite...
            return true;

        default:
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            return super.onOptionsItemSelected(item);

    }
}
```

### 상위 액션 추가

사용자는 앱을 사용하면서 쉽게 메인 화면으로 돌아갈 수 있습니다.</br>
간단한 방법은 상위 액션 버튼을 제공하는 것 입니다.</br>
사용자가 상위 액션 버튼을 클릭하게 되면 앱은 상위 액티비티로 이동할 것 입니다.</br>

> #### 부모 액티비티 선언

액티비티에서 상위 기능을 제공하려면 먼저 매니페스트에서 android:parentActivityName 속성으로 부모 액티비티를 선언해야 합니다.</br>
android:parentActivityName 속성은 API 16 버전부터 지원되며, 이전 버전에서는 <meta-data> 태그를 이용하여 이름과 값의 쌍을 입력해야 합니다.</br>

``` xml
<application ... >
    ...

    <!-- The main/home activity (it has no parent activity) -->

    <activity
        android:name="com.example.myfirstapp.MainActivity" ...>
        ...
    </activity>

    <!-- A child of the main activity -->
    <activity
        android:name="com.example.myfirstapp.MyChildActivity"
        android:label="@string/title_activity_child"
        android:parentActivityName="com.example.myfirstapp.MainActivity" >

        <!-- Parent activity meta-data to support 4.0 and lower -->
        <meta-data
            android:name="android.support.PARENT_ACTIVITY"
            android:value="com.example.myfirstapp.MainActivity" />
    </activity>
</application>
```

> #### 상위 액션 버튼 활성화

상위 액션 버튼을 활성화 하기 위해서는 액티비티가 생성될 때 앱 바의 setDisplayHomeAsUpEnabled() 메서드를 호출하면 됩니다.

``` java
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_my_child);

    // my_child_toolbar is defined in the layout file
    Toolbar myChildToolbar =
        (Toolbar) findViewById(R.id.my_child_toolbar);
    setSupportActionBar(myChildToolbar);

    // Get a support ActionBar corresponding to this toolbar
    ActionBar ab = getSupportActionBar();

    // Enable the Up button
    ab.setDisplayHomeAsUpEnabled(true);
}
```

--------

### 팝업 메세지 나타내기

종종 사용자가 응답할 때까지 기다리지 않고 사용자에게 퀵 메세지를 보여야 할 때가 있습니다.</br>
예를들어 이메일을 보내거나 파일을 삭제하는 등의 작업을 수행하면, 앱에서 퀵 메세지가 표시되어야 합니다.</br>
사용자는 메세지에 응답할 필요가 없지만 사용자가 확인할 수 있어야하며, 앱을 사용하는데 지장이 없게 해야 합니다.</br>

> #### 팝업 메세지 작성 및 표시

사용자는 SnackBar를 이용하여  사용자에게 간단한 메세지를 표시할 수 있습니다.</br>
SnackBar는 잠시 후 자동으로 사라지며, 사용자가 반응할 필요가 없는 간단한 메세지에 적합합니다.</br>

> #### CoordinatorLayout 사용

스낵바는 뷰에 부착되어 사용됩니다. 일반적인 뷰에서는 기본적인 기능을 수행하지만, CoordinatorLayout에서는 추가적인 기능을 합니다.</br>

- 사용자는 스와이프하여 스낵바를 닫을 수 있습니다.
- 레이아웃에 스낵바가 나타날 때, 기존 UI요소를 이동시킵니다. [스낵바 영상](https://developer.android.com/images/training/snackbar/snackbar_button_move.mp4)

CoordinatorLayout은 FrameLayout의 기능을 제공합니다.</br>
앱에서 이미 FrameLayout을 사용하고 있다면 CoordinatorLayout으로 교체하는 것만으로 스낵바의 모든 기능을 이용할 수 있습니다.</br>
하지만 다른 레이아웃을 사용하고 있다면 가장 간단한 방법으로는 기존 레이아웃 요소를 CoordinatorLayout으로 포장하는 것 입니다.</br>

``` xml
<android.support.design.widget.CoordinatorLayout
    android:id="@+id/myCoordinatorLayout"
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    <!-- Here are the existing layout elements, now wrapped in
         a CoordinatorLayout -->
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical">
        <!-- …Toolbar, other layouts, other elements… -->
    </LinearLayout>
</android.support.design.widget.CoordinatorLayout>
```

> #### 메세지 표시

메세지를 표현할 때는 두가지 단계를 거칩니다.

1.  스낵바 객체와 메세지를 생성합니다.
2. 사용자에게 메세지를 보여줄 때 show() 메서드를 호출합니다.

> #### 스낵바 객체 생성

스낵바를 생성할 때는 Snackbar.make() 메서드를 이용해 스낵바를 생성할 수 있습니다.</br>
또한 스낵바를 생성할 때 보여질 메세지, 어느 뷰에 붙일지, 보여질 시간을 정할 수 있습니다.</br>

``` java
Snackbar mySnackbar = Snackbar.make(view, stringId, duration);
```

- viewId : 스낵바를 어느 뷰에 붙일지 정합니다. 이 메서드는 CoordinatorLayout 또는 window decor's content view 에 도달할 때 까지 계층을 탐색합니다.
- stringId : 표시할 메세지의 id
- duration : 메세지를 표시할 시간. LENGTH_SHORT 또는 LENGTH_LONG으로 지정할 수 있습니다.

> #### 사용자에게 메세지 표시

스낵바를 생성하였다면 show()메서드를 호출함으로써 사용자에게 스낵바를 보여줄 수 있습니다.

``` java
mySnackbar.show();
```

시스템은 동시에 여러 스낵바를 표시할 수 없으므로 스낵바가 나타나 있을 때 스낵바를 호출하면 큐에 저장되고, 스낵바가 만료되거나 사라질 때 스낵바를 표시합니다.</br>
단순히 사용자에게 메세지를 보여주고 스낵바 유틸리티 메서드를 사용하지 않는다면, 스낵바를 나타낸 이후 참조할 필요가 없습니다.</br>
그러므로 보통 생성과 동시에 보여주는 구문을 사용합니다.</br>

``` java
Snackbar.make(findViewById(R.id.myCoordinatorLayout), R.string.email_sent,
                        Snackbar.LENGTH_SHORT)
        .show();
```

### 메세지에 작업 추가

사용자가 스낵바 메세지에 응답하게 할 수 있습니다.</br>
스낵바에 액션을 추가하면 메세지 옆에 버튼을 생성할 수 있으며, 버튼을 클릭하여 동작을 실행할 수 있습니다.</br>

![스낵바 이미지](https://developer.android.com/images/training/snackbar/snackbar_undo_action.png)

스낵바에 액션을 추가하기 위해서는 View.OnClickListener 인터페이스를 implements하는 리스너 객체를 정의해야 합니다.</br>
그러면 시스템은 유저가 액션버튼을 클릭하였을 때 onClick()메서드를 호출합니다.</br>

``` java
public class MyUndoListener implements View.OnClickListener{

    @Override
    public void onClick(View v) {

        // Code to undo the user's last action
    }
}
```

스낵바에 리스너를 붙이려면 SetAction()메서드를 사용합니다(show()메서드를 사용하기 전에 리스너를 붙여야 합니다).

``` java
Snackbar mySnackbar = Snackbar.make(findViewById(R.id.myCoordinatorLayout),
                                R.string.email_archived, Snackbar.LENGTH_SHORT);
mySnackbar.setAction(R.string.undo_string, new MyUndoListener());
mySnackbar.show();
```
