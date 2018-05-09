### SwipeRefreshLayout

사용자가 수직으로 스와이프 할 때 새로고침(refresh)할 수 있게 도와주는 레이아웃 입니다.</br>
Android support library v4에 포함되어 있습니다.</br>
OnRefreshListener를 추가하여 새로고침이 실행되었을 때 동작하게 될 행동을 지정할 수 있습니다.</br>
setRefreshing 메서드를 통해 시각적인 효과를 지정할 수 있습니다.</br>

SwipeRefreshLayout을 구현하기 위해서는 새로고침이 실행될 뷰를 SwipeRefreshLayout의 하위에 위치해야 합니다.</br>

``` xml
<android.support.v4.widget.SwipeRefreshLayout
    android:id="@+id/swipe_refresh"
    android:layout_width="match_parent"
    android:layout_height="match_parent" >

        <android.support.v7.widget.RecyclerView
    android:id="@+id/recycler_view"
            android:layout_width="match_parent"
            android:layout_height="match_parent" />

</android.support.v4.widget.SwipeRefreshLayout>
```

해당 뷰의 영역을 위에서 아래로 당기면 SwipeRefreshLayout의 OnRefreshListener의 인터페이스를 통하여 onRefresh함수가 호출됩니다.</br>

``` java
refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
    @Override
    public void onRefresh() {
        // 새로고침 코드 작성
refreshLayout.setRefreshing(false); // 코드 작성 완료 후에 setRefreshing를 false로 세팅하여 로딩 화면을 종료
    }});
```

setColorSchemaResources 메서드를 사용하면 새로고침 아이콘의 색상을 변경할 수 있습니다.</br>

``` java
// 하나의 색상을 넣으면 하나의 색으로 고정, 여러 색을 넣으면 한바퀴 돌때마다 색이 변함.
mSwipeRefreshLayout.setColorSchemeResources(
    android.R.color.holo_blue_bright,
    android.R.color.holo_green_light,
    android.R.color.holo_orange_light,
    android.R.color.holo_red_light
);
```


---

#### 참고 자료 :

* [SwipeRefreshLayout](https://developer.android.com/reference/android/support/v4/widget/SwipeRefreshLayout) for Android Developer
* [Supporting Swipe-to-Refresh](https://developer.android.com/training/swipe/) for Android Developer

</br>

---
