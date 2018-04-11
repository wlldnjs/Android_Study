### Launch-Time Performance(앱 실행 속도 최적화)

사용자는 앱의 반응 및 로드가 빠르길 바랍니다.
시작 시간이 느린 앱은 기대를 충족시키지 못하며, 사용자를 실망시키게 됩니다.
이러한 경험들은 사용자들이 플레이 스토어에서 나쁜 평가를 하게 되고, 결국 앱을 사용하지 않는 것에 까지 이릅니다.

이번 파트에서는 앱의 실행 시간을 최적화 하는데 도움을 주는 정보를 제공합니다.
먼저 launch process의 내부구조부터 시작하여, 시작 성능을 어떻게 프로파일링 할지, 마지막으로 보편적인 starup-time 이슈들에 대한 해결방안을 제시합니다.


---

#### Launch Internals(내부적인 실행)

앱 실행은 3가지 상태 중 하나로 시작합니다.
각 상태(cold start, warm start, lukewarm start)들은 사용자가 앱을 보는데 얼마나 걸리는지 영향을 줍니다.
Cold start는 앱의 처음부터 시작시키며, 다른 상태들은 앱을 백그라운드에서 포그라운드로 가져와서 시작시킵니다.
최적화를 할 때는 항상 cold start라는 가정으로 하는것이 좋은데, 그 이유는 cold start엣 최적화를 하면 warm, lukewarm 상태에서의 성능도 함께 향상시킬 수 있기 때문입니다.

또한 빠른 앱 실행을 위한 최적화를 위해서는 시스템 레벨과 앱 레벨에서 어떤 일이 일어나는지 이해하는 것과, 각 상태에서 어떤 상호작용이 일어나는지 이해하는 것이 중요합니다.

#### Cold start

Cold Start란 앱이 처음부터 시작되는 것을 뜻합니다. 
시스템의 프로세스는 앱의 프로세스가 생성되면 프로세스를 생성합니다. 
앱이 디바이스 부팅이후 처음 실행되는 경우 또는 시스템이 앱을 kill한 후 처음 실행되는 경우와 같은 상황에 Cold Start가 일어납니다. 
이런류의 Start는 다른 상태들 보다 시스템 및 응용프로그램이 수행해야 할 작업이 더 많기 때문에, Startup Time을 최소화 하는데 가장 큰 문제가 됩니다. 
Cold Start의 시작에서, 시스템은 아래 3가지 Task를 수행합니다. 

1. 앱을 로딩하고 실행한다. 
2. 실행 직후 빈 화면(blank starting window)를 표시한다. 
3. 앱 프로세스를 생성한다. 

시스템이 앱 프로세스를 생성하면, 앱 프로세스가 다음 단계를 담당합니다.

1. 앱 객체를 생성한다. 
2. 메인 Thread를 생성한다. 
3. 메인 Activity를 생성한다.
4. View들을 Inflate한다.
5. Screen을 표출한다. 
6. 초기 draw를 수행한다.

한번 앱 프로세스가 최초 Draw를 완료하면, 시스템 프로세스가 현재 보여지고 있는 Background Window를 main Activity와 교체합니다. 
이때부터 사용자는 앱을 사용할 수 있습니다.
아래의 그림은 시스템과 앱 프로세스가 서로간에 작업을 처리하는 방법을 보여줍니다.

![Cold 앱 런칭의 중요한 부분의 시각화](https://developer.android.com/topic/performance/images/cold-launch.png)

성능 이슈는 앱과 Activity 생성동안 일어날 수 있습니다.


#### Application creation

앱이 실행될 때, blank starting window는 앱의 첫번째 draw를 완료할 때 까지 스크린에 남아있게 됩니다. 
이 시점에서, 시스템 프로세스는 앱의 Starting window를 교체해서, 사용자가 앱과 상호작용 할 수 있게 합니다. 
만약 앱이 Application.oncreate()를 overloaded 했다면, 시스템은 앱의 onCreate() method를 호출합니다. 
그 후에 앱은 UI thread(Main Thread)를 생성하고, Main Activity를 생성하는 작업을 수행합니다. 
이 때부터, 시스템레벨 그리고 앱 레벨의 프로세스는 app lifecycle stages에 의해 진행됩니다. 


#### Activity creation

앱 프로세스가 액티비티를 생성한 이후, 액티비티는 아래 작업을 수행합니다.

1. Value 초기화
2. 생성자 호출 
3. 현재 Activity의 Lifecycle에 적합한 Activity.onCreate()와 같은 Callback 호출

일반적으로, onCreate() method는 오버헤드가 가장 큰 작업(View를 loading/Inflate하고, 액티비티의 실행에 필요한 객체들을 초기화 하는 작업)을 수행하기 때문에, load Time에 큰 영향을 줄 수 있습니다.
 

#### Warm start

앱의 Warm Start는 Cold Start에 비해 훨씬 간단하고 오버헤드도 적습니다. 
Warm start에서는, 모든 시스템은 액티비티를 foreground로 가져옵니다. 
만약 앱의 모든 액티비티가 memory에 있다면, 앱은 객체의 생성, 레이아웃 Inflation, 랜더링의 반복을 피할 수 있습니다. 
그러나, onTrimMemory()와 같은 메모리리 trimming 이벤트로 인해 일부 메모리가 제거된 경우, 그런 객체는 Warm start event에 대한 응답으로 재 생성해야 합니다. 
Warm Start는 cold start와 동일한 On-Screen Behavior 시나리오를 따릅니다.(앱이 Activity의 랜더링을 종료할 때 까지 시스템 프로세스가 blank sceen를 표출)


#### Lukewarm start

Lukewarm start는 Cold Start동안 일어나는 동작의 일부를 포함하며, warm start보다 낮은 오버헤드를 가집니다. 
Lukewarm start로 간주 될 수 있는 아래와 같은 많은 잠재적 상태들이 있습니다.

- 사용자는 앱을 종료(back out)했다가 다시 실행시킨다. 프로스세스가 계속 실행되었을 수도 있지만, 앱에서 onCreate() 호출을 통해 처음부터 다시 Activity를 만들어야 한다. 
- 시스템이 메모리에서 앱을 삭제한 후 사용자가 다시 실행한다. 프로셋와 Activity는 재시작 되지만, Tack는 onCreate()를 통해 전달된 Saved Instance state bundle을 통해 약간의 이득을 취할 수 있다.


#### Profiling Launch Performance
시작시간 성능을 정확하게 측정하려면, 앱을 시작하는데 걸리는 시간을 나타내는 metric을 추적할 수 있습니다(디버그 상태에서는 시간이 제대로 측정되지 않을 수 있으므로 디버그 상태가 아닐 때 측정해야 합니다.).
 
#### Time to initial display

Android 4.4버전 부터 로그캣의 출력라인에 Displayed값이 포함 됩니다. 
이 값은 프로세스의 시작시점 부터, Activity의 Drawing을 마친 시점 까지 경과시간을 나타낸다. 
이 시간은 아래 일련의 이벤트를 포함합니다.

1. 프로세스의 시작
2. 객체 초기화
3. 액티비티 생성 및 초기화
4. 레이아웃 Inflate
5. 최초 앱 Draw

해당 로그는 아래와 유사한 형태로 보여집니다.

```
ActivityManager: Displayed com.android.myexample/.StartupTiming: +3s534ms
```

만약 Command line이나 terminal에서 로그캣을 tracking하고 있다면, 간단하게 경과 시간을 찾을 수 있습니다. 
Android Studio에서 경과시간을 찾기 위해서는, 로그캣 화면에서 반드시 필터를 disable 해야 합니다. 
필터를 disable 하는 이유는 앱이 아닌 시스템 서버가 로그를 제공하기 때문입니다.
설정을 한 후에는, 쉽게 시간을 확인 할 수 있습니다. 

![필터를 disable하고, Disabled 값을 로그캣에서 찾는다](https://developer.android.com/topic/performance/images/displayed-logcat.png)


로그캣은 모든 resource들이 로드되고 디스플레이 될때까지의 시간을 나타내지 않습니다. 
레이아웃 파일에서 참조되지 않거나, 앱이 객체 초기화의 일부로 만드는 리소스는 inline process이기 때문에 제외 됩니다.
또한 ADB Shell Activity Manager Command를 통해 실행중인 앱을 실행하여 초기 표시시간을 측정할 수도 있습니다.

```
adb [-d|-e|-s <serialNumber>] shell am start -S -W
com.example.app/.MainActivity
-c android.intent.category.LAUNCHER
-a android.intent.action.MAIN
```

앞에서와 같이 Displayed값은 로그캣 아웃풋으로 나온다. 터미널 Window에서는 아래와 같이 표시 됩니다.

```
Starting: Intent
Activity: com.example.app/.MainActivity
ThisTime: 2044
TotalTime: 2044
WaitTime: 2054
Complete
```

-c와 -a 인자는 optional이며, Intent에 대해 <category>와 <action>을 지정할 수 있도록 합니다.
  

#### Time to full display

reportFullyDrawn() 를 사용하면 앱의 런칭부터 모든 리소스와 View 계층의 display까지의 경과 시간을 측정할 수 있습니다. 
앱이 Lazy loading 하는 경우 유용 하며, 앱은 window의 초기 drawing을 block하지 않고 비동기적으로 리소스를 로드하고 view 계층구조를 업데이트 합니다. 
만약 lazy loading으로 인해 앱의 초기 display가 모든 리소스를 포함하지 않는다면, 별도로 로딩의 완료를 알려주어 리소스와 view를 display해야 합니다. 
예를들어, UI가 모두 로드되어 일부 Test가 그려졌을지라도, 아직 앱이 네트워크로부터 가져와야 하는 이미지들은 display 되지 않았을 수 있습니다.
이 문제를 해결하기 위해, 우리는 수동으로 reportFullyDrawn()를 호출해 시스템에게 액티비티가 lazy loading을 끝냈다고 알려줄 수 있습니다. 
이 method를 사용하면, 로그캣에는 앱 객체를 생성후 reportFullyDrawn()를 호출할때 까지의 경과 시간이 표시됩니다.

```
system_process I/ActivityManager: Fully drawn {package}/.MainActivity: +1s54ms
```

만약 생각한것보다 display time이 늦다면, 이process에서 병목구간이 어디인지 찾아야 합니다.  


#### Identifying bottlenecks

병목구간을 확인하기 위해 Android Studio의 Method Tracer 툴과 Inline tracing이라는 두가지 좋은 방법이 있습니다.  
만약 Method Tracer Tool에 접근할 수 없거나 로그정보를 얻기위해 tool을 적절한 시간에 시작할 수 없다면, 앱 activity의 onCreate() Method안의 inline tracing을 통해 비슷한 정보를 얻을 수 있습니다.  


#### Common Issues

종종 앱의 시작성능에 영향을 미치는 여러가지 문제들이 있는데 주로 앱/Activity 객체의 초기화 및 화면 로딩과 관련이 있습니다.

#### Heavy app initialization

코드가 Application 객체를 override하고, 객체를 초기화 할 때 무거운 작업이나 복잡한 로직을 수행하면 실행 성능에 문제가 될 수 있습니다. 
만약 Application subclass가 아직 할 필요 없는 초기화 작업을 진행한다면 앱은 Startup 동안 시간을 낭비할 수 있습니다. 
몇몇의 초기화들은 불필요합니다. 
예를 들면, 앱이 Intent로 시작된 경우에 Main Activity를 위한 상태정보 초기화하는 것입니다. 
Intent를 사용하면 앱은 이전에 초기화된 state 데이터의 일부만 사용하기 때문입니다. 

앱 초기화중 다른 문제는 영향력이 있거나 많은 GC이벤트 또는 초기화와 동시에 진행되는 disk I/O와 같이 초기화 프로세스를 자주 blocking하는 것들을 포함합니다. 
GC는 특히 달빅 runtime의 고려사항입니다. 
Art runtime은 GC를 동시에 수행하여, 해당 작업의 영향을 최소화 합니다. 


### Diagnosing the problem(문제 진단)

method tracing 또는 inline tracing를 사용해서 문제를 진단 할 수 있다. 

#### Method tracing

Method Tracer 도구를 실행하면 Application.OnCreate() Method가 com.example.customApplication.onCreate Method를 호출하게 됩니다. 
만약 툴이 특정 method들이 실행을 완료하는데 오랜 시간이 걸리는 것을 보여준다면, 어떤 작업이 문제를 일으키는지 확인해야 합니다. 

#### Inline tracing

inline tracing를 사용해서 아래 부분들을 확인할 수 있습니다.

- 앱의 초기 onCreate() 함수 
- 앱이 초기화 하는 singleton 객체들
- Bottleneck이 될 수 있는 Disk I/O, deserialization, 타이트한 loop


#### Solutions to the problem

불필요한 초기화나 Disk I/O에 문제가 있는지 여부와 관계없이 이 솔루션은 lazy initializing을 요구하며, 즉각적으로 필요한 객체만 초기화합니다. 
예를 들어 Global static 객체를 생성하는 것 보다는 앱이 객체에 처음 액세스 할때만 객체를 초기화 하는 Singleton pattern으로 변경하는 것이 유리합니다. 
또한 Dagger같은  DI(dependency injection) framework를 사용하여 객체 및 종속성을 처음으로 주입할 때 생성하는 것을 고려하십시오. 


#### Heavy activity initialization

Activity 생성은 종종 높은 오버헤드 작업을 많이 수반합니다. 
성능개선을 위해 이 작업을 최적화할 방법들이 있습니다.

- 크고 복잡한 레이아웃의 Inflate
- 디스크 혹은 네트워크 I/O로 인한 Screen Drawing의 Blocking
- 비트맵의 Loading / Decoding
- VectorDrawable 객체의 Rasterizing 
- 액티비티의 다른 서브시스템의 초기화 

#### Diagnosing the problem (문제 진단)

이 경우에도 Method tracing과 Inline tracing이 유용할 수 있습니다.


#### Method tracing

Method Tracer tool을 실행할 때, 앱의 Application 서브클래스 생성자 및 com.example.customApplication.onCreate() 메소드를 주의깊게 살펴봅니다. 
만약 툴이 이러한 method들의 실행을 완료하는데 오랜 시간이 걸림을 보여준다면, 거기서 어떤 작업이 일어나는지 더 자세히 알아봐야 합니다.


#### Inline tracing

inline tracing 을 사용해서 아래 부분들을 조사하십시오

- 앱의 초기 onCreate() 함수 
- 글로벌 Singleton 객체의 초기화 
- Bottleneck이 될 수 있는 Disk I/O, deserialization, 타이트한 loop


#### Solutions to the problem (문제 해결)

많은 잠재적 병목구간이 있지만, 보편적인 두가지 문제점과 해결책은 아래와 같습니다. 

- View 계층구조가 복잡할수록 Inflate 하는데 오랜 시간이 걸리는데, 이 이슈를 해결하기 위해 아래와 같은 조치를 할 수 있습니다.
    - 중복, 중첩 레이아웃을 줄여서 view 계층을 Flat하게 만들어 준다. 
    - Launch동안 보여질 필요가 없는 UI 부분들은 Inflate하지 않는다. 대신에 마치 하위 계층들을 위한 Placeholder와 같은 ViewStub 객체를 사용해서 앱이 적절한 시간에 inflate 할 수 있게 하자.

- 모든 리소스를 Main Thread에서 초기화 하면, Startup이 느려질 수 있다. 아래 방법들을 통해 이 문제를 해결할 수 있습니다.
    - 모든 리소스의 초기화를 다른 Thread에서 lazy하게 처리할 수 있다. 
    - 앱의 view들을 로드하고 display 하고 나서, 비트맵이나 다른 리소스들에 의존하는 시각적 속성들을 나중에 업데이트 하라.


#### Themed launch screens

앱의 로딩을 특정 테마로 하고 싶을 때, 앱의 시작 화면을 기본 시스템 테마 대신 전용 테마로 지정 할 수 있습니다. 
이렇게 하면 액티비티가 느리게 시작되는 것을 숨길 수 있습니다.

테마가 입혀진 시작화면을 구현하는 보편적인 방법은 windowDisablePreview 테마속성을 이용하여 시스템의 Black Screen을 끄는 것입니다. 
그러나 이러한 방법은 preview window를 숨기지 않는 앱들보다 startup이 느려질 수도 있습니다. 
또한 사용자가 액티비티 런칭되는 동안 아무 피드백 없이 기다려야 하기 때문에, 사용자에게 이 앱이 정상적으로 동작하는 것인지 의문을 품게 만들 수 있습니다.


#### Diagnosing the problem (문제 진단)

사용자가 앱을 실행할 때 느린 응답 때문에 문제가 생길 수 있습니다. 
이러한 경우에 스크린이 멈추게 되거나 인풋에 대해 응답이 멈추게 됩니다.


#### Solutions to the problem

Preview window를 Disable 하는 것 보다 일반적인 Material Design 패턴을 따를 것을 권장합니다. 
액티비티의 windowBackground 테마 속성을 이용하여 Starting Activity에 간단한 커스텀 Drawable을 설정할 수 있습니다. 
예를들면, 아래와 같이 새로운 drawable 파일을 생성해 layout XML과 앱의 Manifest에서 참조하게 할 수 있다. 

Layout XML file:

``` xml
<layer-list xmlns:android="http://schemas.android.com/apk/res/android" android:opacity="opaque">
  <!-- The background color, preferably the same as your normal theme -->
  <item android:drawable="@android:color/white"/>
  <!-- Your product logo - 144dp color version of your app icon -->
  <item>
    <bitmap
      android:src="@drawable/product_logo_144dp"
      android:gravity="center"/>
  </item>
</layer-list>
```

Manifest file:

``` xml
<activity ...
android:theme="@style/AppTheme.Launcher" />
```

기본테마로 전환하기 가장 쉬운 방법은 super.onCreate() 와 setContentView() 의 호출 전에 setTheme(R.style.AppTheme)를 호출해 주는 것입니다. 

``` java
public class MyMainActivity extends AppCompatActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    // Make sure this is before calling super.onCreate
    setTheme(R.style.Theme_MyApp);
    super.onCreate(savedInstanceState);
    // ...
  }
}
```



</br>

---
#### 원문 출처 :

 * [Launch-Time Performance](https://developer.android.com/topic/performance/launch-time.html) for Android Developers
 
#### 참고 자료 :


</br>

---
