DataBinding
==

###### created by Kimjiwon on 2017.12.20
----

>### DataBinding?

- Java 코드에서 view에 접근할 때 필요한 작업인 findViewById() 메소드로 변수와 view를 연결하는 작업을 줄여주어 java 코드를 좀 더 눈으로 확인하기 좋게(직관적으로) 만들어 준다.
- xml상에 변수를 직접 생성하고, java 코드 에서 데이터 객체를 생성하면서 xml에 바로 값을 대입해 줄 수 있다.
- 컴파일 시 작동하며, 레이아웃 파일에서 작성한 표현식을 처리하고 애플리케이션에서 코드를 생성.
- 코드의 로직과 레이아웃의 분리가 가능하여 MVVM 디자인 패턴 구현 및 테스트 코드 작성시에도 도움을 준다.

</br>
---

>### 1. Data 객체 생성.

#### 일반적인 데이터 객체
- DataBinding시 데이터에 쉽게 접근하기 위해, public 변수와 생성자가 존재하는 데이터 객체(POJO 형태) 또는 JavaBeans 객체를 사용할 수 있다.

```java
// POJO 형태의 데이터 객체
public class UserData{
	public String userName;
    public int userAge;
    
    public UserData(String userName, int userAge){
    	this.userName = userName;
        this.userAge = userAge;
    }
}
```
```java
// JavaBeans 데이터 객체
public class UserData {
    private String userName;
    private int userAge;

    public UserData(String userName, int userAge) {
        this.userName = userName;
        this.userAge = userAge;
    }
    
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public int getUserAge() {
        return userAge;
    }
    public void setUserAge(int userAge) {
        this.userAge = userAge;
    }
}
```
</br>

#### Observable Object / Observable fields를 이용한 데이터 객체
- 데이터 객체의 변화를 바로 적용하기 위해서는 Observable 인터페이스를 구현하는 클래스를 구현 할 수 있다.
- getter에 @bindable 어노테이션을 이용하면 컴파일 중에 BR클래스 파일에 해당 항목을 생성 하고, setter에서 데이터가 변경될 때, notifyPropertyChanged(BR.target) 메서드를 사용하여 데이터가 변경되었다고 알려줌으로써 속성을 변경 할 수 있다.

```java
// BaseObservable을 상속받아 구현한 데이터 객체
public class ObservableUser extends BaseObservable{
    private String userName;
    private int userAge;

    @Bindable
    public String getUserName() {
        return userName;
    }
    @Bindable
    public int getUserAge() {
        return userAge;
    }

    public void setUserName(String userName) {
        this.userName = userName;
        notifyPropertyChanged(BR.userName);
    }
    public void setUserAge(int userAge) {
        this.userAge = userAge;
        notifyPropertyChanged(BR.userAge);
    }
}
```
</br>

- ObservableField와 같은 Observable 객체를 사용할 수도 있다.
- Observable객체는 set/get 메서드를 사용하여 데이터에 접근한다.

```java
// Observable 객체를 사용한 데이터 객체
public class ObservableUser{
    public ObservableField<String> observableFirstName = new ObservableField<>();
    public ObservableField<String> observableLastName = new ObservableField<>();
}
```
</br>
#### Observable Collection
- 동적인 구조를 가진 데이터를 사용하는 앱에서는 Observable 컬렉션을 사용하여 데이터에 접근하는것이 유리하다.
- key가 참조타입(String과 같은)일 때는 ObservableArrayMap을, key가 정수(int)일 경우에는 ObservableArrayList가 유용하다.
(ObservableArrayList를 사용할 때는 바로 숫자를 사용하는 것 보다 상수 변수를 사용하는 것이 가독성을 높이기 좋다.)

- xml상에서 컬렉션의 값에 접근 하고자 할 때는 [’키값 혹은 정수/상수값’]를 이용하여 접근한다.


</br>
---

>### 2. DataBinding 사용.

- 먼저 build.gradle(Module:app) 파일의 android 속성 내부에 dataBinding 요소를 추가.

```java
android{
	//...//
    dataBinding{
    	enabled = true
    }
}
```

</br>

- 데이터 바인딩의 레이아웃 파일은 layout 태그를 최상위 요소(Root tag)로 시작하여 그 뒤에 data요소와 view요소가 포함 됨.
```xml
<!--기존 layout style-->
<?xml version="1.0" encoding="utf-8"?>
<android.support.constraint.ConstraintLayout
    xmlns:android="http://schemas.android.com/apk/res/android" 
    android:layout_width="match_parent"
    android:layout_height="match_parent">

</android.support.constraint.ConstraintLayout>
```
```xml
<!--DataBinding을 사용하는 layout style-->
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android">
    <data>
        
    </data>
    
    <android.support.constraint.ConstraintLayout

        android:layout_width="match_parent"
        android:layout_height="match_parent">

    </android.support.constraint.ConstraintLayout>
</layout>
```
</br>
- 이후 파스칼 방식으로 레이아웃의 이름이 변경된 뒤 접미사로 Binding이 붙은 상태로 패키지 아래  databinding패키지에 자동적으로 클래스가 생성(생성이 이루어지지 않는다면 Rebuild).
- 레이아웃의 data 요소의 class속성을 통해 바인딩 클래스의 이름 또는 위치를 변경할 수 있다.
- class 속성에 이름만 입력하게 되면, 바인딩 클래스의 이름을 변경하게되고 위치.이름으로 지정하게 되면 해당 위치에 지정한 클래스명으로 클래스가 생성된다.

```xml
<!--Binding 클래스의 이름을 ContactItem으로 변경-->
<data
	class="ContactItem">
<data>
<!--또는 Binding 클래스의 이름을 ContactItem으로 변경하면서 생성위치를 변경-->
<data 
	class="kimjiwon.sigongeducation.databindingex.ContactItem">
</data>
```

</br>

- layout 요소 내부의 data요소에는 variable 속성을 사용하여 변수를 생성 할 수 있다.

```xml
<!--UserData클래스를 user라는 변수명으로 사용-->
<data>
	<variable
    	name="user"
        type="kimjiwon.sigongeducation.databindingex1.UserData"/>
</data>
```

</br>

- data요소에 import요소를 사용하여 자바의 Class를 쉽게 참조 할 수 있다.(alias 속성으로 별명 설정 가능)

```xml
<data>
    <import type="android.view.View"/>
    <import type="javax.swing.text.View"
        alias="swingView"/>
</data>
```

</br>

- 괄호를 사용해야 할 경우에는 \&lt;(<) , \&gt;(>)를 사용한다.

```xml
<data>
    <import type="java.util.ArrayList"/>
	<import type="kimjiwon.sigongeducation.databindingex1.UserData"/>
    <variable
        name="userList"
        type="ArrayList&lt;UserData&gt;"/>
</data>
```

- view 요소에서 data요소의 속성을 사용하고자 할 때는  “@{ }” 구문을 이용하여 사용할 수 있다.
- data binding 관점에서 볼 때, POJO형태와 JavaBean형태 둘 다 동일하기 때문에 특정 variable로 설정한 변수명.변수(예를들어 @{user.name})로 접근 가능하다.
```xml
<!--user라는 이름으로 선언한 변수의 userName을 text에 적용-->
<TextView
	android:layout_width="wrap_content"
	android:layout_height="wrap_content" 
	android:text="@{user.userName}" />
```

</br>

- Binding을 구성하기 위해서 inflate할 때 DataBindingUtil을 이용하여 간단하게 Binding을 생성.
- Binding을 생성 한 후 레이아웃에서 생성한 변수를 binding와 해당 클래스를 연결해주는 작업을 해야 함.

```java
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // DataBindingUtil의 setContentView() 메서드를 이용하여 binding 후, user 객체를 생성하여 binding한 객체에 set.
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        User user = new User("김지원", 26);
        binding.setUser(user);
    }
}
```
</br>
---

> ### 3. 이벤트 처리와 Listener Binding. 

- 데이터 바인딩을 이용한 이벤트 처리는 아래와 같은 두가지 방법으로 처리할 수 있다.
	1. 메서드 참조
		- 해당 이벤트와 동일한 매개변수를 갖는 메서드를 호출할 때 사용한다.
		- 컴파일 과정에서 식이 처리되기 때문에 메서드가 존재하지 않거나 이름이 올바르게 지정되지 않았을 경우 컴파일 오류가 발생한다.
		
	2. 리스너 바인딩
		- 반환 값은 원래의 리스너와 일치해야 하며, 추가적인 매개변수를 포함 할 수 있다.
		- 람다식으로 구성되며, 이벤트 발생 시 처리된다.
		
</br>
#### 메서드 참조
- 리스너를 구현한 class를 variable에 등록하고 해당 이벤트에 바인딩식(변수명::메서드)을 입력하여 리스너를 연결한다.

```java
// 이벤트를 담당할 클래스에 onClickHandler 이벤트 생성.
public class MyHandlers {
    public void onClickHandler(View view) {
        Toast.makeText(view.getContext(), "clickEvent", Toast.LENGTH_SHORT).show();
    }
}
```
```xml
<!--Textview의 onClick이벤트에 Handler클래스의 onClickHandler메서드 적용-->
<TextView
	android:id="@+id/textview"
	android:layout_width="wrap_content"
    android:layout_height="wrap_content"
	android:onClick="@{handler::onClickHandler}"/>
```

 </br>

#### 리스너 바인딩
- 이벤트 발생 시 리스너 바인딩을 사용하면 임의의 데이터 바인딩 식을 실행 할 수 있다.
- 기존 리스너의 매개변수를 사용할 수 있고, 매개변수를 사용한다면 기존의 리스너의 모든 매개변수를 식에 기입해주어야 한다.
- 메서드 참조 시 메서드의 매개변수가 이벤트 리스너의 매개변수와 일치해야 함.
- 반환값은 리스너의 예상 반환값과 일치해야 함(void는 void, boolean은 boolean).

</br>
1. 레이아웃에서 Textview 클릭 시 handlers로 선언한 변수의 onClickHandler 메서드 실행(단순 메서드 실행).

```java
public void onClickHandler(User user){
    Log.d("userData",user.firstName +"," +user.lastName);
}
```

```xml
<TextView
	android:layout_width="wrap_content"
	android:layout_height="wrap_content"
	android:onClick="@{() -> handlers.onClickHandler(user)}"/>
```

</br>
2. 레이아웃에서 TextView 클릭 시 기존 onClick 속성과 user객체 동시에 전달.

```java
public void onClicked(View view, User user){
    Toast.makeText(view.getContext(), user.firstName +", " +user.lastName, Toast.LENGTH_SHORT).show();
}
```

```xml
<TextView
	android:layout_width="wrap_content"
	android:layout_height="wrap_content"
    android:onClick="@{(view) -> handlers.onClicked(view,user)}"/>
```

</br>
3. 두개 이상의 매개변수를 포함한 리스너 바인딩.

```java
public void onChecked(User user, boolean checked){
    Log.d("checked",user.userName +checked);
}
```

```xml
<CheckBox
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:onCheckedChanged="@{(checkBox,isChecked) -> handlers.onChecked(user,isChecked)}"
	/>
```

</br>
4. 동일한 반환 값을 갖는 리스너 바인딩.

```java
public boolean onLongClicked(View view, User user){
    Toast.makeText(view.getContext(), "longClick:" +user.lastName, Toast.LENGTH_SHORT).show();
    return true;
}
```

```xml
<TextView
	android:layout_width="wrap_content"
	android:layout_height="wrap_content"
	android:onLongClick="@{(view) -> handlers.onLongClicked(view,user)}"
	/>
```

</br>
---

> ### 4. Layout Details


#### 식 언어

- 일반적인 식 언어는 Java식과 동일하지만 this, super, new 연산자는 사용할 수 없다.
	- 참고할만한 연산자
		- null 병합 연산자(??) - 왼쪽 피연산자가 null이 아니면 왼쪽 null이면 오른쪽 값 선택.
		- 문자열 리터럴 - 식 내부에서 따옴표를 사용해야 할 때는 작은따옴표(’’)를 이용한다.
		- 리소스 접근 - @연산자로 리소스에 접근 할 수 있다(예를들어 @dimen/, @string/).

</br>
#### Include

- include를 사용하면서 include된 레이아웃으로 데이터를 전달하고자 할 때, app 네임스페이스를 이용하여 간단하게 데이터를 전달 할 수 있다.

```xml
<!--include될 레이아웃에 userName과 userMemo변수 생성후 TextView에 적용.-->
	<data>
    	<variable
        	name="userName"
    	 	type="String" />
    	<variable
        	name="userMemo"
        	type="String" /> 
	</data>
<!--...-->
	<TextView
        android:id="@+id/includeUserName"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="20dp"
        android:text="@{userName}"
        android:textSize="16sp"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/includeTextview" />
    <TextView
        android:id="@+id/includeUserMemo"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="@{userMemo}"
        android:textSize="16sp"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/includeUserName" />
```
```xml
<!--부모View에서 include요소의 속성에 변수명으로 접근하여 데이터를 전달할 수 있음.-->
<include
    layout="@layout/include_item"
    android:layout_width="wrap_content"
    android:layout_height="match_parent"
    app:layout_constraintRight_toRightOf="parent"
    app:layout_constraintTop_toTopOf="parent"
    app:userMemo="@{userData.userMemo}"
    app:userName="@{userData.userName}" />
```


</br>
#### BindingAdapter

- 바인딩 된 변수 값이 변경될 때,  해당 바인더 클래스는 그 변수와 관련된 Setter를 호출.
- 일부 사용자 지정 바인딩 로직이 필요할 때 @BindingAdapter  어노테이션을 이용하여 새로운 특성의 Setter를 사용자가 지정할 수 있다.
- 속성 앞의 네임스페이스는 영향을 주지 않는다(예를들어 ‘android:’, ‘app:’).

```java
// BindingAdapter를 이용하여 Custom Setter 생성.
@BindingAdapter("image")
public static void bindImage(ImageView imageView, int resid) {
    imageView.setImageResource(resid);
}
```
```xml
<!--Custom된 image 속성을 사용.-->
<ImageView
	android:id="@+id/list_item_user_image"
	android:layout_width="60dp"
	android:layout_height="60dp"
	android:layout_marginLeft="5dp"
	app:image="@{userData.userImage}"
	app:layout_constraintBottom_toBottomOf="parent"
	app:layout_constraintLeft_toLeftOf="parent"
	app:layout_constraintTop_toTopOf="parent" />
```



</br>
---
#### 참고 자료:

 * [DataBinding library](https://developer.android.com/topic/libraries/data-binding/index.html) for Android Developers
 * [DataBinding Guide](https://medium.com/@devyunsy/android-data-binding-guide-1-40b1a0a2dd62) for Medium(SeungyongYun)
 * [RecyclerView with DataBinding](https://medium.com/@futureofdev/%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C-what-is-databinding-recyclerview-e67abb855788) for Medium(Nahyung Kim, elice dev)
 * [DataBinding Lambda](http://blog.unsignedusb.com/2017/08/android-databinding-5-listener-callback.html) for Software Developer USB


> #### 예제 소스
> - DataBindingEx : DataBinding을 이용한 리스너 바인딩 위주의 예제.
> - DataBindingEx1 : DataBinding을 이용하여 RecyclerView를 구성한 예제.

</br>
---