### Custom View

안드로이드는 기본 레이아웃 클래스인 View와 ViewGroup에 기반하여 UI를 구축할 수 있는 정교하고 강력한 컴포넌트 모델을 제공합니다.
먼저 플랫폼은 Widgets, Layout이라고 불리는 기존의 View와 ViewGroup의 다양한 서브클래스를 포함하고 있습니다.

일부 사용 가능한 위젯의 종류로써는 Button, TextView, EditText, ListView, CheckBox, RadioButton, Gallery, Spinner가 있으며, 더 특별한 목적의 위젯에는 AutoCompleteTextView, ImageSwitcher, TextSwitcher이 있습니다.
사용 가능한 레이아웃은 LinearLayout, FrameLayout, RelativeLayout 등이 있습니다.

만약 사전에 생성된 위젯 또는 레이아웃에 사용자가 필요한 기능을 추가하고 싶다면, View의 서브클래스를 생성하여 메서드를 재정의 할 수 있습니다. 
자신만의 View 서브클래스를 만드는 것으로서 화면 요소의 기능 또는 보이는 것들의 정밀한 제어가 가능합니다.

#### 기본적인 접근법

1. 기존의 View 클래스 또는 다른 서브클래스를 상속받습니다.
2. 부모클래스로부터 상속받은 메서드의 일부를 재정의한다. 재정의 해야하는 부모클래스의 메서드는 "on"으로 시작하며, onDraw(), onMeasure(), onKeyDown() 등이 있습니다.
3. 새로 생성한 extension 클래스를 사용합니다.

    * 클래스의 상속이 해당 Activity의 Inner 클래스로 정의될 수 도 있지만, 범용적으로 사용하기 위해서는 따로 생성하는 것이 좋습니다.

#### 새로운 사용자 정의 컴포넌트

완전하게 커스터마이징된 컴포넌트는 사용자가 원하는 외형을 가지는 그래픽 요소를 만드는데 사용될 수 있습니다.

1. 보통 View를 확장하여 새로운 자식 컴포넌트를 만듭니다.
2. XML로부터 속성과 파라미터들을 가져올 수 있는 생성자를 만들 수 있으며, 자신만의 속성과 파라미터를 사용할 수도 있습니다.
3. 자신만의 이벤트 리스너, 프로퍼티 접근자 및 수정자를 만들 수 있으며, 컴포넌트 클래스가 좀 더 세련되어지도록 구성할 수 있습니다.
4. onMeasure()와 onDraw() 둘다 기본적인 동작을 가지고 있지만, 기본적으로 onDraw()는 아무런 동작을 하지 않고 onMeasure()는 항상 100x100의 크기를 설정하게 됩니다.
5. 다른 on...() 메서드는 필요에 따라 재정의 될 수 있습니다.

#### onDraw()와 onMeasure() 메서드의 확장

onDraw() 메서드는 사용자가 원하는것을 만들 수 있는 캔버스를 제공해줍니다.
캔버스에 그려질 수 있는 것은 2D그래픽, 기본 또는 커스텀 컴포넌트, 스타일이 지정된 텍스트 등등 이 있습니다.

    * 3D 그래픽은 지원하지 않으며, 3D그래픽을 그리고 싶다면 View 대신 SurfaceView를 상속받아야 하고 별도의 Thread에서 그려져야 합니다.

onMeasure() 메서드는 컴포넌트와 컨테이너 사이의 계약관계를 표현하는 중요한 부분이며, 포함된 부분들에 대한 계측이 효율적이고 정확하게 전달되도록 재정의 되어야 합니다.
만약 onMeasuer() 메서드로 전달되어져야 할 사항들과 이미 계산되어진 width와 height를 가지고 있다면, setMeasuredDimension() 메서드를 호출하여야 합니다.
재정의된 onMeasure() 메서드에서 이러한 메서드를 호출받지 못한다면 Measurement Time에서 예외가 발생하게 됩니다.

High Level에서 보았을 때 onMeasure() 메서드는 다음과 같은 일을 하는것으로 보여집니다.

1. 재정의된 onMeasure() 메서드는 width와 Height 수치와 함께 호출합니다(View.onMeasure(int, int)).
2. 사용자 컴포넌트의 onMeasure() 메서드는 컴포넌트를 표현하도록 요구되어지는 Width와 height를 계산하여야 한다.
만약 Width와 Height가 부모의 width, Height를 초과한다면 클리핑, 스크롤링, 예외처리 등을 선택하여 처리할 수 있습니다.
3. Width와 Height가 계산되고 나면, 계산된 측정값을 setMeasuredDimension(int width, int height) 메서드를 통해 호출합니다.

#### Compound Controls

기존의 Constrols(View)의 그룹들을 하나로 합치려면 Compound Component를 생성하는 것이 적합합니다.
간단히 말해 여러 View들을 하나의 논리적인 그룹으로 합치는 것 입니다.
예를들어 한줄의 EditText와 ListView로 이루어진 콤보 박스가 있습니다.

Compound Component 만들기

1. 일반적으로 레이아웃부터 시작하기 때문에 Layout 클래스를 상속하여 구현합니다.
액티비티와 마찬가지로 포함된 컴포넌트들을 생성하기 위해 XML에 기반한 선언방식을 사용하거나, 코드상에서 프로그램적으로 
값들을 선언할 수 있습니다.
2. 새로운 클래스에 대한 생성자는 부모클래스에 정의된 파라미터들을 가질 수 있고, 파라미터들을 부모클래스의 생성자로 넘겨줄 수 있으며, 이후 새로운 컴포넌트에서 사용할 다른 뷰들을 셋업할 수 있습니다.
3. 포함되어있는 View에 이벤트 리스너를 생성할 수 있습니다.
4. 접근자(accessors) 및 수정자(modifiers)에 대한 커스텀 Properties를 생성할 수 있습니다.
5. Layout을 상속받는 경우, onDraw()와 onMeasure() 메서드가 기본적으로 잘 동작하기 때문에 다른 동작이 필요하지 않다면 재정의할 필요 없습니다. 
6. onKeyDown()과 같은 "on...()"의 다른 Methods을 재정의할 수 있습니다.

요약하자면, Custom View에 Layout을 상속받아 사용하는 것은 다음과 같은 이점들이 있습니다.

- 액티비티 화면에서와 마찬가지로 XML을 사용하여 Layout을 정하거나 프로그램 코드상에서 Layout으로 그것들을 포함할 수 있습니다.
- onDraw()와 onMeasure() 메서드는 적당한 동작들을 가지기 때문에 재정의할 필요 없습니다.
- 복잡한 Compound View를 구성하고 단일 컴포넌트처럼 재사용할 수 있습니다.

#### 뷰 타입 변경

사용자가 원하는 동작과 매우 유사하게 동작하는 컴포넌트가 이미 있다면, 해당 컴포넌트를 간단히 상속받아 바꾸고 싶은 동작을 재정의할 수 있습니다.
새로운 커스텀 컴포넌트를 통해 추가 하고싶은 많은 기능들을 넣을 수 있지만, 기존의 특정 View 계층에 좀 더 특화되어있는 클래스에서 시작한다면 사용자가 원하는 것을 행하는 많은 메서드들을 자유롭게 얻을 수 있습니다.

1. The Definition

클래스는 다음과 같이 정의됩니다.

``` java
public static class MyEditText extends EditText
```

- EditText를 상속받아 구현한 것으로, 이러한 경우 EditText는 커스터마이즈 하기위해 선택한 뷰입니다.
- 구현을 끝마쳤을 때, 새로운 클래스는 기존 EditText 뷰를 대체할 수 있습니다.

2. Class Initialization

XML 레이아웃 파일로부터 인플레이트 될 때 생성자의 파라미터들과 함께 생성됩니다.
생성자에서는 그것들을 사용함과 동시에 부모 클래스의 생성자로 파라미터들을 넘겨주어야 합니다.

3. Overridden Methods

새로 정의된 클래스에서 onDraw()를 재정의하여 다른 다른 동작을 하게 된다면, 메서드가 끝나기 전에 부모클래스의 onDraw()가 호출됩니다. 
부모클래스의 onDraw()는 자식 클래스에서의 동작이 끝난 후 동작하게 됩니다.

4. Use the Custom Component

``` xml
<view
  class="com.android.notepad.NoteEditor$MyEditText" 
  id="@+id/note"
  android:layout_width="fill_parent"
  android:layout_height="fill_parent"
  android:background="@android:drawable/empty"
  android:padding="10dip"
  android:scrollbars="vertical"
  android:fadingEdge="vertical" />
```

위와같은 xml파일이 있을 때, 사용자 컴포넌트는 일반적인 뷰와 함께 생성되고, Full Package를 사용하여 정의됩니다.
또한 이너클래스는 $기호를 통해 참조할 수 있습니다.
만약 사용자 뷰 컴포넌트가 이너클래스로 정의되어 있지 않다면, XML Element Name으로 컴포넌트를 정의하여 사용할 수 있습니다.

---

### 커스텀 뷰 클래스 생성

커스텀 뷰는 사용하기 쉬운 인터페이스로 특정 기능 셋을 캡슐화하고, CPU및 메모리르 효율적으로 사용해야 합니다.
또한 다음과 같은 사항을 준수해야 합니다.

- 안드로이드 표준 준수
- 안드로이드 XML 레이아웃에서 작동하는 Custom Styleable 속성 제공
- 접근하기 쉬운 이벤트 보내기
- 여러 안드로이드 플랫폼과의 호환

안드로이드 프레임워크는 이러한 요구 사항을 충족하는 뷰를 만드는데 도움이되는 기본 클래스와 XML태그를 제공합니다.

#### Subclass a View

안드로이드 프레임워크에 정의된 모든 뷰 클래스들은 View를 상속받습니다.
커스텀 뷰 또한 뷰를 직접 상속받거나 뷰의 서브클래스들을 상속받아 구현할 수 있습니다.

안드로이드 스튜디오와 뷰가 상호작용 하기 위해서는 최소한 Context와 AttributeSet 객체를 파라미터로 가지는 생성자를 제공해야 하며, 이를 통해 레이아웃 편집기가 뷰의 인스턴스를 만들고 편집할 수 있게 도와줍니다.

``` java
class PieChart extends View {
    public PieChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
```

    * View와 ViewGroup의 차이 : 안드로이드 레퍼런스에 따르면 뷰그룹은 다른 뷰들을 포함할 수 있는 특수한 뷰로써, 레이아웃 및 뷰 컨테이너의 기본 클래스입니다(뷰를 그룹으로 묶어서 관리하기 위해 주로 사용).

#### Define Custom Attributes

뷰에 사용자 인터페이스를 추가하기 위해서는 이를 XML요소에 지정하고 속성을 통해 모양과 동작을 제어해야 합니다.
잘 작성된 커스텀 뷰는 XML을 통해 값을 추가하고 스타일을 지정할 수 있습니다.
커스텀 뷰에서 이 동작들을 사용하기 위해서는 아래와 같은 절차를 따릅니다.

- <declare-styleable> 리소스 요소에 뷰의 커스텀 속성을 정의
- XML 레이아웃의 속성값 지정
- 런타임 시 속성값 검색
- 검색된 속성값을 뷰에 적용

커스텀 속성을 정의하기 위해서는 <declare-styleable> 리소스를 프로젝트에 추가해야 합니다.
이 리소스는 res/values/attrs.xml 파일에 저장하는 것이 일반적입니다.

``` xml
<resources>
   <declare-styleable name="PieChart">
       <attr name="showText" format="boolean" />
       <attr name="labelPosition" format="enum">
           <enum name="left" value="0"/>
           <enum name="right" value="1"/>
       </attr>
   </declare-styleable>
</resources>
```

위의 코드에는 PieChart라는 이름의 declare-styleable에 showText와 labelPosition 이라는 두개의 커스텀 속성이 정의되어 있습니다.
styleable entity의 이름은 일반적으로 custom view를 정의하는 클래스 이름과 같습니다.

커스텀 속성을 정의한 후에는 기존 레이아웃 XML파일에서 사용하는것처럼 속성을 사용할 수 있습니다.
기존과 다른점은 커스텀 속성은 다른 namespace를 갖는 점 입니다.
    * 기존의 http://schemas.android.com/apk/res/android 가 아닌 새로운 네임스페이스로
http://schemas.android.com/apk/res/[your package name]를 정의해서 사용해야 합니다.
또는 http://schemas.android.com/apk/res/res-auto를 사용해도 됩니다.

``` xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
   xmlns:custom="http://schemas.android.com/apk/res/com.example.customviews">
 <com.example.customviews.charting.PieChart
     custom:showText="true"
     custom:labelPosition="left" />
</LinearLayout>
```

#### Apply Custom Attributes

뷰가 XML 레이아웃으로 부터 생성 되었을 때, XML 태그 내의 모든 속성들은 resource bundle로 부터 읽혀져 뷰 생성자의 AttributeSet으로 전달됩니다.
AttributeSet에서 직접 값을 읽을 수도 있지만, 아래와 같은 문제가 있습니다.

- 속성 값 내의 리소스 참조가 해결되지 않음
- 스타일이 적용되지 않음

AttributeSet을 직접 읽는대신, obtainStyledAttributes() 메서드를 이용하여 AttributeSet을 전달하여 문제를 해결할 수 있습니다.
obtainStyledAttributes() 메서드는 참조를 해제하고 스타일이 지정된 TypedArray 값 배열을 전달합니다.

안드로이드 리소스 컴파일러는 res 디렉터리의 각 <declare-styleable> 리소스에 의해 생성된 R.java에 있는 속성 id와 배열의 각 속성에 대한 인덱스를 정의하는 상수 set들을 모두 정의하고, 미리 정의된 상수를 사용하여 TypedArray에서 특성을 읽습니다.

``` java
public PieChart(Context context, AttributeSet attrs) {
   super(context, attrs);
   TypedArray a = context.getTheme().obtainStyledAttributes(
        attrs,
        R.styleable.PieChart,
        0, 0);

   try {
       mShowText = a.getBoolean(R.styleable.PieChart_showText, false);
       mTextPos = a.getInteger(R.styleable.PieChart_labelPosition, 0);
   } finally {
       a.recycle();
   }
}
```

    * TypedArray 객체는 공유 resource이므로 사용 후 반드시 재활용해야 합니다. 이를 위해 try-catch문의 finally에서 recycle()을 호출합니다. 
이 메서드를 통하여 할당되어 있던 메모리를 즉시 반환하여 GC 될 때까지 기다리지 않아도 됩니다.


#### Add Properties and Events

Attributes는 동작 및 모양을 제어하는 강력한 방법이지만, 뷰가 초기화되었을 때만 읽을 수 있습니다. 
동적인 동작을 제공하려면, 각 custom attribute에 대한 getter와 setter 메서드를 정의해야 합니다.

``` java
public boolean isShowText() {
   return mShowText;
}

public void setShowText(boolean showText) {
   mShowText = showText;
   invalidate();
   requestLayout();
}
```

setter에서 invalidate()와 requestLayout()메서드를 호출하는 것은 뷰가 안정적으로 동작하는지 확인하기 위함입니다.
외형에 영향을 주는 속성을 변경한 경우 뷰를 무효화하여 시스템에게 뷰를 다시 그려야 한다는 점을 알려주어야 합니다.
마찬가지로 크기나 모양에 영향을 주는 속성이 변경되었을 때도 새로운 레이아웃을 요청해야 하며, 이러한 메소드 호출을 잊어버린다면 찾기 힘든 버그가 생길 수 있습니다.

    * invalidate() : 이 메서드를 호출하면 해당 뷰 전체가 무효화 됩니다(뷰를 다시 그리도록 요청).
주의할 사항은 반드시 UI스레드에서 호출해야 하며, UI스레드가 아닌 스레드에서 호출하려면 postInvalidate()메서드를 사용해야 합니다.

    * requestLayout() : 이 메서드를 호출하면 크기와 위치를 다시 조정하도록 요청합니다(레이아웃을 갱신).

![메서드 호출 순서](http://cfile7.uf.tistory.com/image/211625375716700B04EAB6)

또한 커스텀 뷰는 중요한 이벤트를 전달하기 위한 이벤트 리스너를 지원해야 합니다.
만약 당신이 커스텀 뷰의 유일한 사용자일경우 속성과 이벤트를 공유하는것을 잊을 수 있는데, 뷰의 인터페이스를 신중하게 정의하는데 시간을 많이 할애한다면 향후 유지관리를 쉽게 할 수 있습니다.

#### Design For Accessibility

커스텀 뷰는 넓은 범위의 사용자를 고려하여 지원해야 합니다(터치스크린을 보지 못하거나 사용하지 못하는 사용자들이 포함된다). 
이러한 사용자들을 지원하기 위해서는 다음을 수행해야 합니다.

- android:contentDescription 속성을 사용하여 입력란에 라벨을 지정
- sendAccessibilityEvent()를 호출하여 접근성 이벤트 보내기
- D-pad 및 트랙볼과 같은 대체 컨트롤러를 지원


### Implementing Custom Drawing

#### Override onDraw()

커스텀 뷰를 그릴때 가장 중요한 단계는 onDraw() 메서드를 재정의 하는 단계입니다.
onDraw()의 파라미터는 뷰가 자신을 그릴 때 사용할 수 있는 Canvas 객체이며, Canvas 클래스는 텍스트, 선, 비트맵 등 다른 많은 그래픽 primitive를 그리는 방법을 정의합니다.
이러한 onDraw() 메서드를 이용하여 커스텀 사용자 인터페이스를 만들 수 있습니다.

#### Create Drawing Objects

android.graphics 프레임워크는 두 가지 영역의 그리기로 나뉩니다.

- Canvas에서 무엇을 그릴지.
- Paint에서 어떻게 그릴지.

예를들어 Canvas는 선을 그리는 메서드를 제공하고 Paint는 선의 색상을 정의하는 메서드를 정의합니다.
간단히 말해 Canvas는 화면에 그릴 모양을 정의하고, Paint는 그릴 모양의 색상, 스타일, 글꼴 등을 정의합니다.

따라서, 무언가를 그리기 전에 하나 이상의 Paint 객체를 만들어야 합니다.

``` java
private void init() {
   mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
   mTextPaint.setColor(mTextColor);
   if (mTextHeight == 0) {
       mTextHeight = mTextPaint.getTextSize();
   } else {
       mTextPaint.setTextSize(mTextHeight);
   }

   mPiePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
   mPiePaint.setStyle(Paint.Style.FILL);
   mPiePaint.setTextSize(mTextHeight);

   mShadowPaint = new Paint(0);
   mShadowPaint.setColor(0xff101010);
   mShadowPaint.setMaskFilter(new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL));

   ...
```

객체를 미리 생성하는 것은 중요한 최적화 작업입니다.
뷰는 매우 빈번하게 다시 그려지고, 여러 드로잉 객체들은 값비싼 초기화를 필요로 합니다.
onDraw() 메서드 내에서 드로잉 객체를 만들게 되면 성능이 크게 저하되고, UI가 느리게 보일 수 있습니다.

#### Handle Layout Events

커스텀 뷰를 제대로 그리기 위해서는 크기를 알아야 합니다.
절대로 화면에 보이는 크기로 가정해서는 안되며, 하나의 앱에서만 뷰를 사용하더라도 가로 또는 세로모드, 서로 다른 화면 크기, 다중 화면 밀도, 다양한 종횡비를 처리해야 합니다.

뷰에서 측정을 처리하는 방법들이 있지만, 대부분은 재정의 할 필요 없이 onSizeChanged()만 재정의 해서 사용하면 됩니다.

onSizeChanged() 메서드는 뷰에 처음으로 크기가 할당될 때 호출되며, 뷰의 크기가 변경될 때 다시 호출됩니다.
onSizeChanged() 메서드에서는 뷰의 크기와 관련된 위치, 치수 및 기타값들을 계산해야 합니다.

뷰의 크기가 지정되면 레이아웃 매니저는 모든 크기에 Padding값이 포함되어 있다고 가정하기 때문에 뷰의 크기를 계산할 때 패딩 값을 처리해야 합니다.

``` java
       // ex> PieChart.onSizeChanged()
       // Account for padding
       float xpad = (float)(getPaddingLeft() + getPaddingRight());
       float ypad = (float)(getPaddingTop() + getPaddingBottom());

       // Account for the label
       if (mShowText) xpad += mTextWidth;

       float ww = (float)w - xpad;
       float hh = (float)h - ypad;

       // Figure out how big we can make the pie.
       float diameter = Math.min(ww, hh);
```

만약 뷰의 레이아웃 파라미터를 보다 세밀하게 제어해야 한다면 onMeasure() 메서드를 구현하면 됩니다.
이 메서드의 파라미터는 View.MeasureSpec 값으로, 뷰의 크기가 최대값인지, 일정한 값인지 나타냅니다.

``` java
@Override
protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
   // Try for a width based on our minimum
   int minw = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
   int w = resolveSizeAndState(minw, widthMeasureSpec, 1);

   // Whatever the width ends up being, ask for a height that would let the pie
   // get as big as it can
   int minh = MeasureSpec.getSize(w) - (int)mTextWidth + getPaddingBottom() + getPaddingTop();
   int h = resolveSizeAndState(MeasureSpec.getSize(w) - (int)mTextWidth, heightMeasureSpec, 0);

   setMeasuredDimension(w, h);
}
```

위의 코드에서 중요한 사항은 다음과 같습니다.

- 계산시 뷰의 패딩값이 고려됩니다.
- Helper 메서드의 resolveSizeAndState() 메서드는 최종적인 너비와 높이를 만드는데 사용되며, 이 Helper 메서드는 뷰가 생각하는 크기를 onMeasure() 메서드에 전달된 스펙과 비교하요 적절한 View.MeasureSpec 값을 반환합니다.
- onMeasure() 메서드는 리턴값이 없는 대신, setMeasuredDimension() 메서드를 호출하여 결과를 전달합니다.
만약 이 메서드를 호출하지 않는다면 뷰 클래스에서 런타임 Exception을 발생합니다.

#### Draw!

객체 생성 및 측정 코드를 정의한 후에는 onDraw() 메서드를 구현합니다.
대부분의 뷰에서 몇가지 공통된 작업이 있는데 다음과 같습니다.

- drawText()를 사용하여 텍스트를 그리고, setTypeface()를 호출하여 서체를 지정하며, setColor()를 호출하여 텍스트 색상을 지정합니다.
- drawRect(), drawOval() 그리고 drawArc()를 사용하여 기본 도형을 그립니다. 그 후 setStyle()을 호출하여 도형의 채우기, 윤곽선 또는 두 가지 모두를 변경합니다.
- Path 클래스를 사용하여 더욱 복잡한 모양을 그립니다.
Path 객체에 선과 곡선을 추가하여 모양을 정의한 다음 drawPath()를 사용하여 모양을 그릴 수 있으며, 기본 도형과 마찬가지로 setStyle()에 따라 경로를 채우기, 윤곽선 또는 두 가지 모두를 적용할 수 있습니다.
- LinearGradient 객체를 만들어 gradient 채우기를 정의합니다.
LinearGradient를 사용하여 모양을 채우려면 setShader()를 호출합니다.
- drawBitmap()을 사용하여 비트맵을 그립니다.


</br>

---
#### 원문 출처 :

 * [Custom View](https://developer.android.com/guide/topics/ui/custom-components.html) for Android Developers

#### 참고 자료 :
 * [CustomView와 CustomLayout](http://onecellboy.tistory.com/344) for Tistory(단세포소년)
 
</br>

---
