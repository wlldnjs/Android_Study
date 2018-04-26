### ConstraintLayout_v1.1.0_release_note

2018년 4월 12일에 발표되었으며 새로운 최적화를 통해 레이아웃 성능을 향상시켰습니다.

1.0 버전에 비해 크게 바뀐 내용은 새로운 helper object(Barrier), 복잡한 체인 지원, dimension constraints, circular constraints 입니다.


#### Circular positioning(원형 위치 지정)

특정 위젯에 제약조건을 적용하여 기준 위젯에 대해 각도와 거리를 적용 할 수 있습니다.

[원형 포지셔닝](https://developer.android.com/reference/android/support/constraint/resources/images/circle2.png)

그렇게 하면 원 위에 위젯을 배치 할 수 있으며 아래와 같은 속성을 사용할 수 있습니다.

- layout_constraintCircle : 참조할 위젯의 ID
- layout_constraintCircleRadius : 기준 위젯의 중앙과 다른 위젯의 중앙의 거리.
- layout_constraintCircleAngle : 위젯들 사이의 각도 (각도는 0에서 360까지)

``` xml
<ImageView
        android:id="@+id/standard_point"
        android:layout_width="30dp"
        android:layout_height="30dp"
        android:src="@mipmap/ic_launcher"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <View
        android:id="@+id/black_dot"
        android:layout_width="15dp"
        android:layout_height="15dp"
        android:background="#000000"
        app:layout_constraintCircle="@+id/standard_point"
        app:layout_constraintCircleAngle="30"
        app:layout_constraintCircleRadius="70dp"
        />
```

#### Barrier
여러 뷰의 가장자리의 위치에 만드는 가상의 뷰로써, 한 면을(가로 또는 세로) 지정하여 여러 뷰 그룹들을 참조하는 가이드 라인을 만들 수 있습니다.
즉 참조되는 뷰 그룹들 중 가장 큰 너비를 기준으로 가이드라인이 생성되며, 크기가 줄거나 늘어나면 자동으로 이동하여 너비를 맞춥니다.

- barrierDirection : 배리어의 위치
- constraint_referenced_ids : 참조되는 뷰의 id들

``` xml
<android.support.constraint.Barrier
        android:id="@+id/barrier"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:barrierDirection="end"
        app:constraint_referenced_ids="button_top, button_bottom" />
```

#### Group

여러 뷰들을 한번에 묶는 기능입니다.
이는 여러 뷰들을 한번에 보이거나 사라지게 해야 할 때 그룹을 사용하여 손쉽게 적용할 수 있습니다.
이는 Flat 계층의 구조를 가진 레이아웃이 성능을 크게 향상시킵니다.

``` xml
<android.support.constraint.Group
        android:id="@+id/group"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:constraint_referenced_ids="black_dot, red_dot, blue_dot" />
```

#### Placeholder

화면에 내용을 동적으로 설정할 때 사용되며 id를 전달함으로써 손쉽게 구현할 수 있습니다.
Xml 상에서 placeholder를 생성하고 java 코드 상에서 placeholder.setContentId(viewId) 메서드를 이용하여 화면을 동적으로 변화시킬 수 있습니다.
해당 뷰와 placeholder가 동일한 화면에 있는경우 Visible 상태가 자동으로 GONE 상태로 설정 됩니다.

``` xml
<!-- xml -->

    <ImageView
        android:id="@+id/image_android_round"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:src="@mipmap/ic_launcher_round"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <ImageView
        android:id="@+id/image_android_square"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:src="@mipmap/ic_launcher"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <android.support.constraint.Placeholder
        android:id="@+id/placeholder"
        android:layout_width="match_parent"
        android:layout_height="100dp"
        app:content="@+id/image_android_round"
        app:layout_constraintBottom_toBottomOf="parent" />
```

``` java
@Override
    public void onClick(View v) {
        placeholder.setContentId(v.getId());
    }
```


#### dimension constraints(치수 제약조건)

> #### Percent size

부모 뷰의 크기에 비례하여 뷰의 크기를 결정하는 방법.

1. 부모 뷰에 맞출 속성은 match_constraint(0dp)로 선언한 후 layout_constraint[Width/Height]_default 속성으로 대상 축을 지정합니다.

	- spread : constraint 영역에 맞춤(기본 값)
	- wrap : 뷰의 크기에 맞춤
	- percent : constraint[Width/Height]_percent 속성에 선언한 비율에 맞춤


</br>

---

#### 참고 자료 :

 * [ConstraintLayout](https://developer.android.com/reference/android/support/constraint/ConstraintLayout.html) for Android Developer
 * [ConstraintLayout 1.1.0](https://android.jlelse.eu/whats-new-in-constraint-layout-1-1-0-acfe30cfc7be) for Medium

</br>

---
