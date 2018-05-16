### CoordinatorLayout

CoordinatorLayout은 FrameLayout을 상속받는 클래스로써 아래와 같은 특징을 갖습니다.

- 자식이 행위를 선언하여 터치 이벤트와 관련된 여러 이벤트들을 제어하게 해준다.
- 뷰들간의 의존 관계를 구축할 수 있게 해주는 레이아웃(플로팅 버튼과 스낵바를 같이 띄우는 경우 스낵바가 떠오를 때 플로팅 버튼이 자동으로 올라가도록).

기본 api가 아닌 support design widget입니다.
때문에 사용하기 위해서는 dependencies에 design을 추가해야 합니다.

### CollapsingToolbarLayout

CollapsingToolbarLayout 에 layout_scrollFlags 속성을 사용하여 스크롤시 헤더에 대한 적절한 flag를 설정할 수 있습니다.

- scroll : 스크롤 이벤트에 반응할 모든 view에 반드시 이 플래그를 설정해야 합니다. 그렇지 않으면 화면상단에 고정되어 있게 됩니다.
- enterAlways : 아래쪽 방향으로 스크롤 할때마다 이 뷰가 표시됩니다.
- exitUntilCollapsed : 해당뷰에 minHeight를 정의하고 있으면, Toolbar가 해당 크기 까지만 축소가 됩니다.
- enterAlwaysCollapsed : 해당뷰에 minHeight속성의 크기로 시작해 맨위로 스크롤이 될때만 전체 높이로 확장하게 됩니다

또한 app:contentScrim 속성을 사용하여 최소화 되었을때 툴바의 색상을 변경 할 수 있습니다.

``` xml
<android.support.design.widget.CollapsingToolbarLayout
    android:id="@+id/collapsing_toolbar_layout"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:fitsSystemWindows="true"
    app:contentScrim="?attr/colorPrimary"
    app:expandedTitleMarginEnd="64dp"
    app:expandedTitleMarginStart="48dp"
    app:layout_scrollFlags="scroll|exitUntilCollapsed" >
```

### Toolbar

layout_collapseMode 속성을 사용하여 스크롤이 발생했을 때 Toolbar의 최종 형태를 결정해줍니다.
Toolbar뿐 아니라 TextView또는 ImageView를 같이 사용했을 때도 속성을 추가해줍니다.

- pin : CollapsingToolbarLayout이 완전히 축소되면 툴바는 화면위에 고정됩니다. 
- parallax : 툴바가 축소되는 동안 Parallax모드로 동작하도록 합니다. 옵션으로 layout_collapseParallaxMultipler 속성을 사용하면 transition의 translation Multiplier를 설정할 수 있습니다. 

``` xml
<!-- 본문의 내용을 위로 올리면 image가 작아지면서 사라지고 툴바는 상단에 고정됩니다. -->
<ImageView
    android:id="@+id/image_view"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:scaleType="centerCrop"
    android:fitsSystemWindows="true"
    android:src="@drawable/bar_img_png"
    app:layout_collapseMode="parallax" />

<android.support.v7.widget.Toolbar
    android:id="@+id/toolbar"
    android:layout_width="match_parent"
    android:layout_height="?attr/actionBarSize"
    app:layout_collapseMode="pin"
    app:popupTheme="@style/AppTheme.PopupOverlay" />
```

### Behavior

스크롤링 이벤트중 가장 중요한 속성으로써 이 속성을 지정해주어야 CoordinatorLayout과 함께 스크롤링 시 동작하게 됩니다.

``` xml
<android.support.v7.widget.RecyclerView
android:id="@+id/recycler_view"
android:layout_width="match_parent"
android:layout_height="match_parent"
app:layout_behavior="@string/appbar_scrolling_view_behavior" />
```

---

#### 참고 자료 :

* [CoordinatorLayout](https://developer.android.com/reference/android/support/design/widget/CoordinatorLayout) for Android Developer
* [CoordinatorLayout과 Behavior](http://www.kmshack.kr/tag/coordinatorlayout/) for 꿈꾸는 개발자의 로그
* [CoordinatorLayout example code](https://github.com/codepath/android_guides/wiki/Handling-Scrolls-with-CoordinatorLayout) for GitHub(codepath)
* [CoordinatorLayout 활용](http://freehoon.tistory.com/38) for Tistory(freehoon)

</br>
---
