Best Practices for Performance
===

###### created by Kimjiwon on 2018.01.16
----

>### 성능 향상을 위한 방법들.

앱을 빌드할 때, 부드럽고 빠르며, 적은양의 배터리를 소모하도록 구성하는 방법을 알아보자.

- 아래와 같은 목차를 가진다.

1. 성능 향상 팁
2. 레이아웃 성능 향상
3. 배터리 성능 최적화

</br>

---

>### 성능 향상 팁

효율적인 코드를 작성하기 위한 두가지 기본적인 규칙이 있다.

- 사용하지 않는 코드를 작성하지 말것.
- 가능하다면 메모리를 할당하지 말것.

앱을 최적화 할 때에 가장 까다로운점은 여러 타입의 하드웨어에서 실행될 수 있다는 점이다. 
여러 버전의 가상머신은 각각 다른 프로세서를 사용하기 때문에 속도의 차이가 있다.
앱이 다양한 기기에서 제대로 작동하기 위해서는 모든 수준에서 효율적으로 최적화 해야 한다.

#### 불필요한 객체 생성 피하기
불필요한 객체 생성은 메모리 낭비를 가져오게 되고, 결국 가비지 컬렉터 작업이 실행되게 된다. 가비지 컬렉터 연산은 많은 메모리를 사용하기 때문에 성능저하를 일으킨다.

불필요한 객체생성을 피하기 위한 예:

- 어떤 함수가 String을 반환하고, 그 반환값이 항상 StringBuffer에 추가된다면, 메서드를 수정하여 내부에서 StringBuffer에 추가하도록 한다.
- 특정 String 데이터에서 문자열을 추출하려면 새로 문자열을 생성하는것 보다 원본 데이터에 subString() 메서드를 사용하는것이 효율적이다.
- Integer, Double 등의 Wrapper 배열을 이용하고 있다면 int, double 과 같은 기본타입을 사용하라.
- (Object1, Object2)와 튜플 형태의 배열을 사용중이라면, Object1[], Object2[]와 같은 2개의 배열로 변경 하는것이 좋다.(API 제작과 같은 성능보다 디자인 패턴이 중요할 때는 예외)

결과적으로, 불필요한 객체 생성을 피하는 것은 가비지 컬렉터의 호출을 줄이고 성능향상에 도움이 된다.

#### static 메서드 사용
객체의 필드 값에 접근하지 않는다면 메서드를 static으로 만드는게 좋다.
메서드를 static 지정하게 되면 속도가 15~20% 빨라질 뿐 아니라, 메서드를 호출할 때 객체의 상태를 변경할수 없다는 것을 메서드 시그니처에서 알 수 있기 때문에 좋은 방법이다.

#### 상수를 static final로 사용

``` java
static int intVal = 42;
static String strVal = "Hello, world!";
```

상수를 선언하기 위해 다음과 같이 final 키워드 없이 static 필드를 이용하게 되면, 컴파일러가 이 값들을 해당 변수에 할당하고, 이 변수들을 검색테이블에 저장한다. 나중에 이 변수들을 이용하려면, 컴파일러는 이 검색테이블을 통해 변수를 검색하여 값을 얻어오게 된다.
이를 개선하기 위해서는 final 키워드를 사용한다.

``` java
static final int intVal = 42;
static final String strVal = "Hello, world!";
```

다음과 같이 final 필드를 함께 이용할 경우, 이 값들은 dex 파일의 static 영역에 함꼐 저장된다. 이 경우, 변수를 이용하려면 컴파일러는 검색테이블을 거치지 않고 바로 값에 접근한다. 그렇기에 일반적으로는 static final 필드가 변수 접근속도가 빠르다.

#### 향상된 for문 사용
향상된 for문(for-each문)은 자바 1.5버전부터 지원하는데, 일반 배열과 Collection 객체에서 사용할 수 있다.
Collection에서 향상된 for문을 사용하게 되면 내부적으로 컴파일러가 Iterator를 사용하는 방법으로 바꿔서 컴파일하며, 일반 배열에서는 많은 성능차이가 날 수 있다.

``` java
static class Foo {
    int mSplat;
}

Foo[] mArray = ...

public void zero() {
    int sum = 0;
    for (int i = 0; i < mArray.length; ++i) {
        sum += mArray[i].mSplat;
    }
}

public void one() {
    int sum = 0;
    Foo[] localArray = mArray;
    int len = localArray.length;

    for (int i = 0; i < len; ++i) {
        sum += localArray[i].mSplat;
    }
}

public void two() {
    int sum = 0;
    for (Foo a : mArray) {
        sum += a.mSplat;
    }
}
```

zero() 메서드는 매 루프마다 mArray.length를 계속 접근하기 때문에 가장 느리다.
one() 메서드는 멤버변수의 검색을 피하기 위해서 로컬변수에 대입해 이용하고 있고, length도 매번 불리지 않도록 메모리에 캐싱해두었기 때문에 zero() 메서드보다 빠르다.
two() 메서드는 JIT가 없는 디바이스에서는 앞의 두 메서드보다 빠르고, JIT가 있는 디바이스에서는 one() 메서드와 거의 같은 성능을 보인다.


#### Private 이너클래스에서의 private 패키지 접근을 고려해라.

``` java
public class Foo {
    private class Inner {
        void stuff() {
            Foo.this.doStuff(Foo.this.mValue);
        }
    }

    private int mValue;

    public void run() {
        Inner in = new Inner();
        mValue = 27;
        in.stuff();
    }

    private void doStuff(int value) {
        System.out.println("Value is " + value);
    }
}
```

위의 예시에서 private inner클래스인 Inner는 outer클래스인 Foo의 private 필드와 메서드를 사용하고 있다.
이는 자바의 문법에서 가능하며, 실행값도 우리가 예상하는대로 Value is 27이 출력될 것 이다.
하지만 가상머신은 Foo와 Foo$Inner를 전혀 별개의 클래스로 판단하고, 자바문법이 허용했지만 Inner에서의 private 필드와 메서드의 접근이 잘못되었다고 인식한다. 즉, 가상머신은 이를 해결하기 위해 다음과 같은 합성(Synthetic) 메서드를 내부적으로 자동 생성한다.

``` java
/*package*/ static int Foo.access$100(Foo foo) {
    return foo.mValue;
}
/*package*/ static void Foo.access$200(Foo foo, int value) {
    foo.doStuff(value);
}
```

이 static 메서드는 Inner에서 mValue를 접근하거나, doStuff() 메서드를 호출할 때마다 호출된다. 이는 당연히 필드를 직접 엑세스하는 것보다 느리다. accessor 메서드의 생성을 피하려면, Foo의 멤버/메서드를 private이 아닌 package 접근제어자로 변경해주면 된다.

#### 부동소수점 사용을 피하라
안드로이드 디바이스에서는 부동소수점 연산은 정수 연산보다 약 2배정도 느리다. 속도 관점에서 보면, double과 float은 별 차이가 없다. 그러므로, 어차피 부동소수점 연산을 해야하고 메모리의 제약이 덜하다면, 2배 더 정밀한 표현이 가능한 double을 이용하는 것이 낫다.


#### 라이브러리를 잘 알고 사용하자.
먼저 코드를 구현하기 전에, 시스템에서 지원하는 라이브러리 중에 구현하고자 하는 동작을 지원하는지 확인하여 가급적 시스템이 제공하는 라이브러리를 이용하도록 한다. 이 라이브러리에서 제공하는 메서드들은 JIT 등, 컴파일러에 최적화되어 있어서, 대부분의 경우 유저가 직접 구현한 방법보다 빠르거나 같다(예를들어 System.arraycopy()).

#### Native 메서드를 주의해서 사용하라.
대부분의 경우 NDK를 사용할 필요가 없을 정도로 JAVA에서 최적화를 진행할 수 있다. 그러나, 메모리, 파일 디스크립터 등의 네이티브 자원을 할당해야 하거나, 아키텍쳐별로 컴파일을 따로 해줘야 할 필요가 있는 등의 이유가 있다면, NDK를 이용한 네이티브 코드를 이용하는 것도 고려해 볼 수 있다.

#### 성능에 대한 오해.
JIT가 없는 디바이스에서는, 인터페이스 타입의 변수를 선언하고 그 인터페이스의 메서드를 이용하는 것이 실제 타입의 변수를 선언하고 메서드를 이용하는 것보다 조금 느린 것이 사실이다.(약 6%) 그러나 JIT가 있는 디바이스에서는 성능차이가 없다. 그러므로 6%의 얼마안되는 성능차이를 커버하고자 OOP의 안티패턴인 실제타입 변수를 선언하는 것은 재고해볼 필요가 있다.

#### 항상 측정할 것.
최적화를 시작하기 전에 문제를 어떻게 해결할 것인지 미리 파악해야 한다.
또한 Traceview는 프로파일링에 도움이 될것이지만 Traceview 사용중에는 JIT가 비활성화 되기 때문에 속도가 약간 저하될 수 있다.

</br>

---

>### 레이아웃 최적화
레이아웃은 사용자가 앱을 사용할 때 가장 직접적인 영향을 끼친다. 레이아웃이 잘못 구현된다면, 메모리 부족으로 UI가 느려질 수 있다. 안드로이드 SDK의 툴을 사용한다면 레이아웃 성능 문제를 개선 할 수 있다.

#### 레이아웃 계층 최적화
각 위젯과 레이아웃은 초기화와 레이아웃 그리기가 필요하다.
한 예로 LinearLayout은 깊은 계층 구조를 가질 수 있다. 하지만 계층구조가 깊어질수록 layout_weight를 두번씩 측정해야 하기 때문에 많은 메모리가 필요할 수 있다. 이것은 반복적으로 레이아웃이 인플레이트 될 때 중요하다(Listview, GridView 등).

#### 레이아웃 확인
안드로이드 SDK도구에는 앱 실행중에 레이아웃을 분석할 수 있는데 Hierarchy Viewer라는 도구가 포함되어있다.

Hierarchy Viewer의 특징은 다음과 같다.
- 레이아웃 성능의 병목현상을 찾는데 도움이 된다.
- 사용함으로써 레이아웃의 계층구조를 쉽게 확인 할 수 있고, 각 블럭을 측정하여 신호등같은 불빛으로 각 레이아웃의 잠재적 문제를 파악 할 수 있다.

#### 레이아웃 수정
중첩된 LinearLayout은 속도가 느려지기 때문에 병합을 이용하여 성능을 향상시킬 수 있다.
RelativeLayout은 계층이 2단계이기 때문에 좀 더 빠른 속도를 가진다.
일부 복잡한 레이아웃에서는 layout_weight 속성으로 인해 UI를 두번의 Measure pass 때문에 속도가 느려질 수 있다. 이 때 가중치 사용 여부를 고려해보아야 한다.

- Measure pass?
layout_weight는 단순히 비율을 나누어 공간을 차지하는 것이 아니다.
내 부모의 view가 그려지고 나서 남은 공간이 얼마만큼인지, 다른 View들이 그려지고 나서 다시한번 남은공간도 계산하고 그리고나서 남는 공간에 weight에 맞게 가져가려고 또 계산하게된다.

---

>### 배터리 성능 최적화

좋은 앱일수록 배터리에 영향을 주어선 안된다.
네트워크 요청을 일괄 처리하거나, 연결이 해제되었을 때 백그라운드 서비스 업데이트를 비활성화 하는 등 배터리가 낮을 때 업데이트 비율을 낮추는 등의 조치를 취하면 사용자 환경을 유지하면서 앱이 배터리에 미치는 영향을 최소화 할 수 있다.

#### 잠자기 모드 및 앱 대기모드 최적화
안드로이드는 6.0버전(API 23)부터 두가지의 절전기능을 도입.
사용자는 기기가 충전중이 아닐 때 앱의 작동방식을 관리하여 배터리의 수명을 조절 할 수 있다.
- 잠자기 모드 : 기기를 오랫동안 사용하지 않을 때 앱의 백그라운드에서의 CPU활동 및 네트워크 작업을 지연시켜 배터리 소모량을 줄인다.
- 앱 대기모드 : 최근 사용자와 상호작용이 없는 앱의 백그라운드 네트워크 액티비티를 지연시킨다.

#### 잠자기 모드
사용자가 플러그를 뽑고 화면이 꺼진 채로 기기를 일정 기간 동안 정지 상태로 두면 기기는 잠자기 모드작동. 
잠자기 모드에서 시스템은 앱이 네트워크 서비스와 CPU 사용량이 많은 서비스에 액세스하는 것을 제한하여 배터리를 절약.
또한 앱이 네트워크에 액세스하지 못하도록 하고 작업, 동기화 및 표준 알람을 지연.

시스템은 앱에서 지연된 액티비티를 완료할 수 있도록 주기적으로 잠깐 동안 잠자기 모드를 종료. 
유지관리 기간 동안 시스템은 보류 중인 동기화, 작업 및 알람을 모두 실행하고 앱이 네트워크에 액세스할 수 있도록 허용.

유지관리 기간이 끝나면 시스템이 다시 잠자기 모드로 들어가면서 네트워크 액세스가 정지되고 작업, 동기화 및 알람이 지연된다. 
시간이 지날수록 시스템은 유지관리 기간의 횟수를 줄입니다. 
기기가 충전기에 연결되어 있지 않은 상태에서 비활성 기간이 길어지는 경우에 배터리 소모량을 낮출 수 있기 때문입니다.

사용자가 기기를 움직이거나 화면을 켜거나 충전기를 연결하여 기기를 활성화하면 시스템은 잠자기 모드를 종료하고 모든 앱은 정상적인 액티비티로 돌아갑니다.

#### 잠자기 모드 사용 시 제한사항
- 네트워크 액세스 정지
- 시스템에서의 wake locks, wifi 스캔, 동기화 어탭터, JobScheduler 무시
- 표준 AlarmManager 알람(setExact() 및 setWindow() 포함)은 다음 유지관리 기간으로 연기



### 배터리 상태 모니터링 
업데이트가 배터리 수명에 미치는 영향을 줄이기 위해 백그라운드 업데이트 빈도를 변경할 때는 현재 배터리 수준과 충전 상태부터 확인하는 것이 좋다.

애플리케이션 업데이트 수행으로 배터리 수명이 영향을 받는 정도는 기기의 배터리 수준과 충전 상태에 따라 달라진다.
기기가 AC 전원에서 충전될 때 업데이트 수행이 미치는 영향은 무시할 만한 수준이므로, 대부분 기기가 콘센트에 연결되어 있으면 새로고침 비율을 최대화할 수 있다. 
반대로 기기가 방전되고 있으면 업데이트 비율을 낮추는 것이 배터리 수명 연장에 좋다.

마찬가지로 배터리 충전 수준을 검사하고, 배터리 충전량이 거의 소모되었을 때 업데이트 빈도를 낮추거나 심지어 중단할 수도 있다.

#### 현재 충전상태 확인
현재 충전 상태를 확인하는 작업부터 시작한다. BatteryManager는 충전 상태를 포함하는 접착식 Intent에서 모든 배터리와 충전 세부정보를 브로드캐스트한다.

접착식 인텐트이므로 BroadcastReceiver를 등록할 필요가 없다. 
다음 스니펫에서와 같이 registerReceiver를 호출하여 null을 수신기로 전달하면 현재 배터리 상태 인텐트가 반환된다.
여기서 실제 BroadcastReceiver 객체를 전달할 수도 있지만 나중 세션에서 업데이트를 처리할 것이므로 필요하지 않다.

``` java
IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
Intent batteryStatus = context.registerReceiver(null, ifilter);
```

또한. USB나 일반 충전기로 충전할 때 어떤것으로 충전할지 여부를 확인할 수 있다.

``` java
// Are we charging / charged?
int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                     status == BatteryManager.BATTERY_STATUS_FULL;

// How are we charging?
int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
```

### 도킹 상태와 유형 모니터링
Android 기기는 여러 가지 종류의 도크에 도킹할 수 있습니다. 여기에는 차량/홈 도크와 디지털/아날로그 도크가 포함된다. 
많은 도크가 도킹된 기기에 전원을 제공하므로 통상 도킹 상태는 충전 상태와 밀접하게 연결되어 있다.

휴대폰의 도킹 상태가 업데이트 비율에 미치는 영향은 앱에 따라 다릅니다. 예컨대 데스크톱 도크에 있을 때는 스포츠 센터 앱의 업데이트 빈도를 높이고 기기가 차량에 도킹되어 있을 때는 업데이트를 완전히 비활성화할 수 있다. 
반대로 백그라운드 서비스가 교통 상황을 업데이트 중이라면 차량에 도킹되어 있을 때 업데이트를 최대화할 수도 있다.

도킹 상태는 접착식 Intent로 브로드캐스팅되어서 기기가 도킹되어 있는지, 기기가 도킹되어 있다면 어떤 유형의 도크인지 쿼리할 수 있다.

#### 도킹 연결상태 확인
도킹 상태 세부정보는 ACTION_DOCK_EVENT 작업의 접착식 브로드캐스트에 엑스트라로 포함됩니다. 접착식이므로 BroadcastReceiver를 등록하지 않아도 됩니다. 다음 스니펫에서 보여주듯이 registerReceiver()를 호출하여 브로드캐스트 수신기로 null을 전달하면 된다.

``` java
IntentFilter ifilter = new IntentFilter(Intent.ACTION_DOCK_EVENT);
Intent dockStatus = context.registerReceiver(null, ifilter);
```

EXTRA_DOCK_STATE 엑스트라에서 현재 도킹 상태를 추출할 수 있다.

``` java
int dockState = battery.getIntExtra(EXTRA_DOCK_STATE, -1);
boolean isDocked = dockState != Intent.EXTRA_DOCK_STATE_UNDOCKED;

```

기기가 도킹되어 있을 때는 4가지 상태로 나눠진다.
- 자동차
- 데스크
- 저사양(아날로그) 데스크(api 11이상)
- 고사양(디지털) 데스크

상태를 확인하기 위해서는 아래와 같은 코드를 사용한다.

``` java
boolean isCar = dockState == EXTRA_DOCK_STATE_CAR;
boolean isDesk = dockState == EXTRA_DOCK_STATE_DESK ||
                 dockState == EXTRA_DOCK_STATE_LE_DESK ||
                 dockState == EXTRA_DOCK_STATE_HE_DESK;
```

#### 도크 상태 변화 모니터링
기기를 도킹하거나 도킹을 해제할 때마다 ACTION_DOCK_EVENT 작업이 브로드캐스트됨.
기기의 도크 상태 변화를 모니터링하려면 아래 스니펫과 같이 애플리케이션 매니페스트에서 브로드캐스트 수신기를 등록.

``` xml
<action android:name="android.intent.action.ACTION_DOCK_EVENT"/>
```

### 연결상태 확인 및 모니터링

반복적인 알람과 백그라운드 서비스의 가장 흔한 사용 방법은 인터넷 리소스, 캐시 데이터에서 애플리케이션 데이터의 정기 업데이트를 예약하거나 장기 실행 다운로드를 실행하는 것이다

#### 인터넷 연결상태 확인

```java
ConnectivityManager cm =
        (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
boolean isConnected = activeNetwork != null &&
                      activeNetwork.isConnectedOrConnecting();
```

#### 인터넷 연결 유형 확인
현재 이용 가능한 인터넷 연결 유형을 확인할 수 있다.
기기 연결은 모바일 데이터, WiMAX, Wi-Fi 및 이더넷 연결로 제공될 수 있다. 
아래와 같이 활성 네트워크 유형을 쿼리하면 이용 가능한 대역폭에 기초하여 새로고침 비율을 변경할 수 있다.

```java
boolean isWiFi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
```

업데이트를 비활성화한 뒤에는 연결에 변화가 있는지 수신 대기하고 있다가 인터넷 연결이 설정되면 업데이트를 재개해야 한다.


### 연결 변경 모니터링
ConnectivityManager는 연결 세부정보가 변경되면 CONNECTIVITY_ACTION("android.net.conn.CONNECTIVITY_CHANGE") 작업을 브로드캐스트. 
매니페스트에서 브로드캐스트 수신기를 등록하고 이러한 변화를 수신 대기하고 있다가 그에 따라 백그라운드 업데이트를 재개(또는 정지)할 수 있다.

``` java
<action android:name="android.net.conn.CONNECTIVITY_CHANGE"/>
```

</br>

---
#### 참고 자료:

 * [Best Practices for Performance](https://developer.android.com/training/best-performance.html) for Android Developers


</br>

---