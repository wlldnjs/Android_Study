### LifeCycle-Aware Components

LifeCycle-Aware 컴포넌트는 액티비티 또는 프래그먼트의 LifeCycle이 변경될 때 응답함으로써 작업을 수행합니다.

일반적인 패턴은 액티비티 및 프래그먼트의 LifeCycle 메서드에 동작을 구현하는 것 입니다.

하지만 이 패턴은 코드의 비정상 동작 및 오류를 야기합니다.

LifeCycle-Aware 컴포넌트를 이용하면 생명주기 메서드에 있는 코드 종속성을 제거할 수 있습니다.

android.arch.lifecycle 패키지는 LifeCycle-Aware 컴포넌트를 만들수 있는 클래스 및 인터페이스를 제공하며, 액티비티 및 프래그먼트의 생명주기에 따라 자동으로 동작을 조정할 수 있습니다.

안드로이드 프레임워크에 정의된 대부분의 앱 구성 요소에는 생명주기가 있습니다.

생명주기는 운영체제 또는 프로세스에서 실행중인 프레임워크 코드에 의해 관리됩니다.

생명주기는 안드로이드 동작의 핵심이며 어플리케이션은 생명주기를 따라야 합니다.

그렇지 않으면 메모리릭이 발생하거나 어플리케이션이 중지될수 있습니다,


아래는 화면에 디바이스의 위치를 보여주는 예제입니다.

``` java
class MyLocationListener {
    public MyLocationListener(Context context, Callback callback) {
        // ...
    }

    void start() {
        // connect to system location service
    }

    void stop() {
        // disconnect from system location service
    }
}


class MyActivity extends AppCompatActivity {
    private MyLocationListener myLocationListener;

    @Override
    public void onCreate(...) {
        myLocationListener = new MyLocationListener(this, (location) -> {
            // update UI
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        myLocationListener.start();
        // manage other components that need to respond
        // to the activity lifecycle
    }

    @Override
    public void onStop() {
        super.onStop();
        myLocationListener.stop();
        // manage other components that need to respond
        // to the activity lifecycle
    }
}
```

위의 예제는 정상적으로 보이지만, 실제 앱에서는 수명주기의 현재 상테에 대한 응답으로 수많은 UI및 컴포넌트들을 관리하는 호출이 발생합니다.

하지만 여러 컴포넌트를 관리하면 onStart()와 onStop()과 같은 생명주기 메서드에 상당한 양의 코드가 저장되기 때문에 유지관리가 어려워집니다.

또한 항상 액티비티 또는 프래그먼트가 중지되기 전에 컴포넌트가 시작되지 않습니다.

특히 onStart()처럼 구성을 체크해야하는 작업을 수행해야 할 때 더욱 그렇습니다.

이로 인해 onStop() 메서드가 onStart() 전에 끝나는 경쟁 조건이 발생하여 컴포넌트가 필요한 것보다 오래 유지됩니다.

``` java
// onStart() 메서드가 종료되기 전에 액티비티가 종료되어 onStop()메서드가 먼저 호출되는 경우의 문제 
class MyActivity extends AppCompatActivity {
    private MyLocationListener myLocationListener;

    public void onCreate(...) {
        myLocationListener = new MyLocationListener(this, location -> {
            // update UI
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Util.checkUserStatus(result -> {
            // what if this callback is invoked AFTER activity is stopped?
            if (result) {
                myLocationListener.start();
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        myLocationListener.stop();
    }
}
```

android.arch.lifecycle 패키지는 이러한 문제를 탄력적이고 독립적인 방법으로 해결하는 데 도움이되는 클래스와 인터페이스를 제공합니다.

---

### LifeCycle

Lifecycle은 컴포넌트(액티비티 또는 프래그먼트)의 생명주기 상태에 대한 정보를 가지고 있으며, 다른 객체에 상태를 알려줍니다.

또한 두개의 주요 열거형을 이용하여 관련 컴포넌트의 생명주기를 추적합니다.


#### Event

lifecycle event는 프레임워크와 Lifecycle 클래스에서 전달되는 이벤트입니다.

이러한 이벤트는 액티비티 및 프래그먼트의 콜백 이벤트에 매핑됩니다.


#### State

아래는 Lifecycle 객체가 추적하는 컴포넌트의 상태입니다.

![생명주기 상태](https://developer.android.com/images/topic/libraries/architecture/lifecycle-states.png)

클래스는 메서드에 어노테이션을 추가하여 생명주기 상태를 모니터링할 수 있습니다.

그 후 Lifecycle 클래스의 addObserver() 메서드를 호출함과 동시에 사용자의 옵저버를 함께 전달함으로써 옵저버를 추가할 수 있습니다.

``` java
public class MyObserver implements LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void connectListener() {
        ...
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void disconnectListener() {
        ...
    }
}

myLifecycleOwner.getLifecycle().addObserver(new MyObserver());
```

---

### LifecycleOwner

LifecycleOwner는 클래스의 생명주기를 나타내는 싱글 메서드 인터페이스입니다.

클래스에는 getLifecycle() 메서드가 구현되어야 합니다.

전체 응용프로그램 프로세스의 생명주기를 관리하려면 ProcessLifecycleOwner를 참조하십시오.

LifecycleOwner 인터페이스는 Fragment 및 AppCompatActivity와 같은 개별 클래스에서 각자의 Lifecycle을 추상화하고 그것들과 함께 동작하는 컴포넌트를 작성할 수 있습니다.

또한 모든 사용자 정의 응용 프로그램 클래스는 LifecycleOwner 인터페이스를 구현할 수 있습니다.

LifecycleObserver를 구현하는 컴포넌트는 수명주기를 제공 할 수 있으므로 LifecycleOwner를 구현하는 컴포넌트와 원활하게 작동합니다.

수명주기는 옵저버가 볼 수 있도록 등록 할 수 있습니다.


위치 추적의 경우, MyLocationListener 클래스에서 LifecycleObserver를 구현하고, onCreate() 메서드에서 액티비티의 생명주기와 함께 초기화할 수 있습니다.

이 방법을 통해 생명주기 상태가 변경될 때 액티비티 대신 MyLocationListener에서 상태 변경에 따른 로직을 구현할 수 있습니다.

각각의 컴포넌트에 이러한 로직을 작성함으로써 액티비티 및 프래그먼트 로직 관리를 좀 더 쉽게 할 수 있습니다.

``` java
class MyActivity extends AppCompatActivity {
    private MyLocationListener myLocationListener;

    public void onCreate(...) {
        myLocationListener = new MyLocationListener(this, getLifecycle(), location -> {
            // update UI
        });
        Util.checkUserStatus(result -> {
            if (result) {
                myLocationListener.enable();
            }
        });
  }
}
```

일반적인 유스케이스는 Lifecycle이 좋지 않은 상태일 때 특정 콜백 호출을 피하는 것 입니다.

예를들어, 콜백이 액티비티의 상태를 저장한 뒤에 프래그먼트의 트랜지션을 호출하면 크래시가 발생하기 때문에 콜백 호출을 피하려 합니다.

이러한 유스케이스를 쉽게 만들기 위해 Lifecycle 클래스는 다른 객체의 최근 상태를 따릅니다.

``` java
class MyLocationListener implements LifecycleObserver {
    private boolean enabled = false;
    public MyLocationListener(Context context, Lifecycle lifecycle, Callback callback) {
       ...
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    void start() {
        if (enabled) {
           // connect
        }
    }

    public void enable() {
        enabled = true;
        if (lifecycle.getCurrentState().isAtLeast(STARTED)) {
            // connect if not connected
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    void stop() {
        // disconnect if connected
    }
}
```

이 구현을 통해 LocationListener는 생명주기를 완벽하게 인식합니다.

만약 다른 액티비티나 프래그먼트에서 LocationListener를 사용할 경우 초기화만 하면 되며, 모든 설정과 해제 작업은 클래스 자체에서 관리하게 됩니다.

만약 라이브러리가 안드로이드 생명주기에 맞춰 동작하는 클래스를 제공한다면 lifecycle-aware 컴포넌트를 이용하는 것이 좋습니다.

그렇게 되면 라이브러리 사용자는 사용자가 수동으로 생명주기를 관리할 필요 없이 컴포넌트들을 쉽게 통합할 수 있게됩니다.


#### Implementing a custom LifecycleOwner

Support Library 26.1.0 이상의 프래그먼트 및 액티비티에는 이미 LifecycleOwner가 구현되어 있습니다.

LifecycleOwner를 만들려는 사용자 정의 클래스가있는 경우 LifecycleRegistry 클래스를 사용할 수 있지만, 다음 코드 예제와 같이 이벤트를 해당 클래스에 전달해야합니다.

``` java
public class MyActivity extends Activity implements LifecycleOwner {
    private LifecycleRegistry mLifecycleRegistry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLifecycleRegistry = new LifecycleRegistry(this);
        mLifecycleRegistry.markState(Lifecycle.State.CREATED);
    }

    @Override
    public void onStart() {
        super.onStart();
        mLifecycleRegistry.markState(Lifecycle.State.STARTED);
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return mLifecycleRegistry;
    }
}
```

### Best practices for lifecycle-aware components

- UI컨트롤러(액티비티 및 프래그먼트)는 최대한 가볍게 유지해야 합니다. 
그것들은 자신의 데이터를 갖는 대신 ViewModel과 LiveData 객체를 사용하여 변경 사항을 뷰에 반영합니다.
- UI컨트롤러에서 데이터가 변경되었을 때 뷰를 업데이트하거나, 사용자의 액션을 ViewModel에 알려주는 역할을 한다면 데이터 처리를 하는 UI를 따로 작성해야 합니다.
- ViewModel 클래스에 데이터 로직을 작성하세요. 
ViewModel은 UI컨트롤러와 다른 앱들 사이를 연결하는 역할을 해야 합니다. 
하지만 ViewModel은 데이터를 가져오는 역할(예를들어 네트워크를 통해)이 아닌 적절한 컴포넌트를 호출하여 데이터를 호출해야 합니다. 
그 후 결과를 UI컨트롤러에게 전달합니다.
- 데이터 바인딩을 사용하면 뷰와 UI컨트롤러간에 깔끔한 인터페이스를 유지할 수 있습니다. 
이를 통해 액티비티 및 프래그먼트에서 뷰를 업데이트하는 코드를 최소화할 수 있습니다. 
자바 코드에서는 버터나이프와 같은 라이브러리를 사용하여 추상화 코드를 작성할 수 있습니다.
- UI가 복잡한 경우 UI수정을 처리 할 수있는 Presenter 클래스를 만드는 것이 좋습니다. 
이것은 복잡한 작업이지만 UI 컴포넌트를 더 쉽게 테스트 할 수 있습니다.
- 뷰 또는 액티비티의 컨텍스트를 ViewModel에서 참조하지 마십시오. 
만약 ViewModel이 액티비티보다 오래 존재한다면 메모리 누수와 함께 가비지 컬렉터가 제대로 동작하지 않을 수 있습니다.


### Use cases for lifecycle-aware components

Lifecycle-aware 컴포넌트를 사용하면 여러 상황들에서 생명주기를 쉽게 관리할 수 있습니다.

- 대략적인 위치정보와 상세한 위치정보 업데이트 간의 변환 : 
Lifecycle-aware 컴포넌트를 사용하면 앱이 활성화 되어 있을 때 상세한 위치정보 업데이트를 활성화 하고, 앱이 백그라운드 상태에 있을 때 대략적인 위치정보 업데이트를 활성화할 수 있습니다.
Lifecycle-aware 컴포넌트인 LiveData를 이용하면 위치가 변경되었을 때 자동으로 UI를 업데이트할 수 있습니다.

- 비디오 버퍼링 : 
Lifecycle-aware 컴포넌트를 사용하면 비디오 버퍼링을 가능한 빨리 시작할 수 있지만 앱이 완전히 시작될 때 까지 기다려야 합니다.
또한 Lifecycle-aware 컴포넌트를 사용하여 앱이 종료될 때 버퍼링을 종료할 수 있습니다.

- 네트워크 연결 : 
Lifecycle-aware 컴포넌트를 이용하여 앱이 활성화 되어있는 경우(foreground 상태) 네트워크 데이터를 실시간으로 업데이트(스트리밍)할 수 있으며, 앱이 백그라운드로 들어갈 때 자동으로 일시정지 시킬 수 있습니다.

- 애니메이션 드로어블 : 
Lifecycle-aware 컴포넌트를 사용하여 앱이 백그라운드 상태에 있을 때 애니메이션 드로어블을 일시 중지하고 포그라운드 상태일 때 드로어블을 다시 시작할 수 있습니다.


### Handling on stop events

Lifecycle이 AppCompatActivity 또는 프래그먼트에 속해있을 때, 생명주기의 상태가 CREATED와 ON_STOP로 변경되면 AppCompatActivity 또는 프래그먼트의 onSaveInstanceState() 메서드를 호출합니다.

Fragment 또는 AppCompatActivity의 상태가 onSaveInstanceState()를 통해 저장되면 ON_START가 호출 될 때까지 UI가 변경되지 않는 것으로 간주됩니다. 

상태를 저장 한 후에 UI를 수정하려고하면 앱의 탐색 상태가 불일치하게 되며, 저장 후에 FragmentTransaction을 실행하면 FragmentManager에서 예외를 발생시킵니다. 

LiveData를 이용하면 옵저버의 생명주기가 적어도 STARTED가 아니면 옵저버를 호출하지 않음으로써 예외를 방지합니다.

또한 화면 뒤에서는 옵저버를 호출하기 전에 isAtLeast() 메서드를 호출합니다.

AppCompatActivity의 onStop()메서드는 onSaveInstanceState()메서드 뒤에 호출되기 때문에 생명주기의 상태가 CREATED가 되기 전엔 UI 상태변경이 허용되지 않게 됩니다.


---

#### 참고 자료 :

* [LifeCycle](https://developer.android.com/topic/libraries/architecture/lifecycle) for Android Developer

</br>

---
