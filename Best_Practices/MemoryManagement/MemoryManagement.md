### Memory Management

---
### Overview of Android Memory Management(안드로이드 메모리 관리 개요)

ART(Android Runtime)과 달빅 가상머신은 페이징과 메모리 매핑을 이용하여 메모리를 관리합니다.</br>
이것은 애플리케이션 메모리는 RAM에 상주하기 때문에, 새 개체를 할당하거나 메모리 매핑 된 페이지에 접근하는 호출을 수행할 수 없다는 것을 의미합니다.</br>
앱에서 메모리를 해제하는 유일한 방법은 현재 앱에서 참조하고있는 객체를 해제하여 가비지 컬렉터가 메모리를 재활용하도록 하는 것 입니다.</br>
하지만 한가지 예외사항으로는 시스템이 해당 메모리를 다른 곳에서 사용하려는 경우 코드 수정 없이 매핑 된 모든 파일을 RAM에서 페이징 할 수 있습니다.</br>

#### Garbage collection(가비지 컬렉션)

ART 또는 달빅 가상머신처럼 메모리가 관리되는 환경에서는 각 메모리 할당을 추적합니다.</br>
프로그램에서 메모리를 더 이상 사용하지 않는다고 판단하면, 프로그래머의 개입 없이 메모리를 힙으로 되돌립니다.</br>
이러한 메모리 관리 환경에서 사용되지 않는 메모리를 회수하는 메커니즘을 가비지 컬렉션 이라고 합니다.</br>
가비지 컬렉션은 목표를 갖습니다.</br>

- 미래에 사용되지 않을 데이터 객체 찾기
- 찾은 객체의 자원을 재활용하기

안드로이드의 메모리 힙은 객체의 수명과 크기에 따라 할당되는 메모리 버킷이 정해집니다.</br>
최근에 할당된 객체는 Young 세대에 속하며, 오랜 기간동안 활성화된 객체는 older 세대에 할당되어 영구 세대로 전환됩니다.</br>

힙에는 각 객체가 사용할수 있는 메모리의 양에 대한 상한선이 있습니다.</br>
메모리가 채워지기 시작하면 시스템은 가비지 컬렉션 이벤트를 발생시켜 메모리를 확보합니다.</br>
가비지 컬렉션이 수행되는 시간은 실행중인 객체의 수와 각 세대들에 따라 다릅니다.</br>

하지만 가비지 컬렉션은 빠르지만 앱의 성능에 영향을 미칠 수 있습니다.</br>
일반적으로 코드 내에서는 가비지 컬렉션 이벤트가 발생하는 시점을 제어하지 않습니다.</br>
시스템에는 가비지 컬렉션을 수행할 시기를 결정하는 기준이 있고, 이 기준이 충족되면 시스템은 프로세스 실행을 중지하고 가비지 컬렉션을 시작합니다.</br>
가비지 컬렉션이 애니메이션 처리중 또는 음악 재생중에 발생하게 되면 처리 시간이 더 늘어날 수 있습니다.</br>
이 처리시간 증가는 16ms(효율적이고 부드러운 프레임 렌더링을 위해 권장되는 값) 임계 값을 초과하여 앱의 코드 실행을 잠재적으로 지연시킵니다.</br>

또한 코드의 흐름이 가비지 컬렉션 이벤트를 더 자주 발생시키거나, 동작시간이 오래 지속될 수 있습니다.</br>
예를들어 알파 블렌딩 애니메이션의 각 프레임에서 for 루프의 가장 안쪽 부분에 여러 객체를 할당하게 되면, 많은 객체로 인해 메모리 힙을 과열시킵니다.</br>
이러한 상황들에서 가비지 컬렉션이 더 자주일어나고 앱의 성능을 저하시킬 수 있습니다.</br>

#### Sharing Memory(공유 메모리)

안드로이드에서는 프로세스간에 RAM 페이지를 공유하려 합니다.</br>

- 각 앱 프로세스는 Zygote라는 기존 프로세스에서 분기됩니다.
Zygote 프로세스는 시스템이 부팅되고 공통된 프레임워크 코드와 리소스를 로드할 때 시작됩니다.
새로운 응용프로그램 프로세스를 시작할 때 시스템은 Zygote 프로세스를 포크한 다음 새 프로세스에서 응용프로그램의 코드를 로드하고 실행합니다.
이를 통해 프레임워크 코드 및 리소스에 할당된 대부분의 RAM 페이지를 다른 모든 응용프로그램 프로세스에서 공유되도록 합니다.

- 대부분의 정적 데이터는 프로세스로 메모리 매핑됩니다.</br>
이를통해 프로세스간에 데이터를 공유할 수 있으며 필요할 때 페이지 아웃 할 수 있습니다.

- 많은 곳에서 안드로이드는 명시적으로 할당된 공유 메모리 영역을(ashmem 또는 gralloc과 함께) 사용하여 프로세스에 동일한 동적 RAM을 공유합니다.

이처럼 공유 메모리를 광범위하게 사용하기 때문에 앱의 메모리 사용량을 결정할 때 주의가 필요합니다.</br>

#### Allocating and Reclaiming App Memory(앱 메모리 할당 및 재확보)

각 앱 프로세스에 대해 달빅 힙은 단일 가상 메모리 범위로 제한됩니다.</br>
이는 논리적인(Logical) 힙 크기를 정의하며, 필요에 따라 증가할 수 있지만 시스템이 각 응용프로그램에 대해 정의하는 한도까지만 가능합니다.</br>

하지만 힙의 논리 크기는 힙에 사용 된 실제 메모리 양과 같지 않습니다.</br>
앱의 힙을 검사할 때 안드로이드에서 Proportional Set Size(PSS)라는 값을 계산합니다.</br>
이 값은 램을 공유하는 응용프로그램의 수와 비례합니다.</br>

    '''
    PSS는 프로세스간에 공유된 메모리를 고려하여 커널이 계산한 수치입니다. 
하나의 메모리 페이지 영역에 관하여, 해당 페이지를 공유하는 프로스세의 수를 기반으로 해당 메모리 영역을 나누어 메모리 사용량을 계산합니다. 
그리고 프로세스간의 PSS 값을 비교해보면 상대적으로 어떤 프로세스가 어느정도의 메모리를 사용하는지 대략적인 상황을 확인 할 수 있습니다.
    '''

달빅 힙은 힙의 논리적인 크기를 줄이지 않고, 안드로이드는 힙의 끝에 사용되지 않는 공간이 있을때만 논리적 힙의 크기를 줄입니다.</br>
그리고 시스템은 힙에 사용되는 실제 메모리를 줄일 수 있습니다.</br>

#### Restricting App Memory(앱 메모리 제한)

기능적인 멀티태스킹 환경을 유지하기 위해 안드로이드는 각 앱의 힙 크기를 엄격하게 제한합니다.</br>
힙의 크기 제한은 디바이스가 사용할 수 있는 램의 크기에 따라 다릅니다.</br>
응용프로그램이 힙의 용량보다 많은 메모리를 할당하려 시도하게 된다면 OutOfMemoryError가 발생하게 됩니다.</br>

시스템에서 현재 디바이스에서 사용할 수 있는 힙의 크기를 정의할 수 있습니다(캐시에 보관할 데이터의 양을 결정하는 경우).</br>
이때 getMemoryClass() 메서드를 호출하여 각 수치들을 확인할 수 있으며, 힙에서 사용할 수 있는 단위를 MB의 정수로 나타내 줍니다.</br>

#### Switching apps(앱 전환)

사용자가 앱을 전환하면 안드로이드는 LRU(Least-Resently-used)캐시에서 백그라운드 앱을 유지합니다.</br>
예를들어 사용자가 앱을 처음 실행하게 되면 프로세스가 생성됩니다.</br>
그 후, 사용자가 앱을 다른 앱으로 스위칭 할 때 프로세스를 종료하지 않고 프로세스를 캐시에 유지합니다.</br>
그렇게 하면 사용자가 나중에 앱으로 다시 돌아올 때 시스템은 프로세스를 재사용 하기 때문에 앱을 더 빠르게 전환할 수 있습니다.</br>

앱에 캐시에 저장된 프로세스가 있고 현재 필요하지 않은 메모리가 할당되어 있으면 사용자가 사용하지 않는 동안에도 해당 앱이 시스템의 전반적인 성능에 영향을 줍니다.</br>
시스템의 메모리가 부족하게 되면 가장 최근에 사용되지 않은 프로세스가 LRU 캐시에서 종료됩니다.</br>
또한 시스템은 메모리와 램을 확보하기 위해 종료할 수 있는 프로세스를 알려줍니다.</br>
    ``` 
    * 시스템이 LRU 캐시에서 프로세스를 종료할 때 상향식으로 동작하며, 어떤 프로세스가 더 많은 메모리를 사용하는지 고려합니다. 
또한 소비하는 메모리가 적을수록 프로세스가 빠르게 재개할 수 있습니다.
    ```

</br>

---

### Manage Your App's Memory(앱 메모리 관리)

RAM(Random-Access Memory)은 모든 소프트웨어 개발 환경에서 중요한 리소스 이지만, 특히 메모리가 제한되어있는 모바일 운영체제에서 더욱 중요합니다.</br>
ART(Android Runtime)과 Dalvik 가상머신 모두 늘 가비지 컬렉션을 수행하지만 앱이 메모리를 할당하고 해제하는것을 무시할 수는 없습니다.</br>
일반적으로 static 멤버 변수의 객체 참조를 유지함으로써 발생하는 메모리 누수가 발생하지 않도록 해야하며 Reference 객체를 적절한 생명주기 콜백에 맞춰 정의해주어야 합니다.</br>

#### Monitor Available Memory and Memory Usage(사용 가능한 메모리 및 메모리 사용량 모니터링)

앱에서 메모리 사용문제를 해결하기 위해서는 먼저 해당 메모리의 사용 문제를 찾아야 합니다.</br>
안드로이드 스튜디오에서 지원하는 Memory Profiler를 이용하면 다음과 같은 방법으로 메모리 문제를 찾고 진단할 수 있습니다.</br>

1. 앱이 시간 경과에 따라 메모리를 할당하는 방법을 실시간 그래프로(사용중인 메모리, 할당된 자바 객체 수, 가비지 컬렉션 발생 등) 확인할 수 있습니다.
2. 앱이 실행되고 가비지 컬렉션이 동작하는 동안 자바 힙의 스냅샷을 만듭니다.
3. 앱의 메모리 할당 현황을 기록하고 할당된 모든 개체를 검사하며, 각 할당에 대한 stack trace를 통해 안드로이드 스튜디오 에디터를 이용하여 해당 코드로 이동합니다.

#### Release memory in response to events(이벤트의 응답으로 메모리 해제)

안드로이드는 여러 방법으로 앱에서 메모리를 회수하거나 필요한 경우 메모리를 확보하기 위해 앱을 완전히 종료시킬 수 있습니다.</br>
시스템 메모리의 균형을 유지하고 앱의 프로세스를 중단해야 하는 상황을 피하려면 액티비티 클래스에 ComponentCallbacks2 인터페이스를 구현함으로써 피할 수 있습니다.</br>
onTrimMemory() 콜백 메서드를 사용하면 앱이 활성화 또는 백그라운드에 있을 때 메모리 관련 이벤트를 수신하여 앱 수명주기 또는 시스템 이벤트에 의해 객체를 해제할 수 있습니다.</br>

``` java
import android.content.ComponentCallbacks2;
// Other import statements ...

public class MainActivity extends AppCompatActivity
    implements ComponentCallbacks2 {

    // Other activity code ...

    /**
     * 메모리 상태를 체크하여 시스템에서 메모리 정리가 필요할때 호출.
     * @param level the memory-related event that was raised.
     */
    public void onTrimMemory(int level) {

        // Determine which lifecycle or system event was raised.
        switch (level) {

            case ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN:

                /*
                   앱의 UI가 보이지 않게 되었을 때 호출.
                */

                break;

            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_MODERATE:
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW:
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL:

                /*
                   앱이 실행중일때 호출.
                */

                break;

            case ComponentCallbacks2.TRIM_MEMORY_BACKGROUND:
            case ComponentCallbacks2.TRIM_MEMORY_MODERATE:
            case ComponentCallbacks2.TRIM_MEMORY_COMPLETE:

                /*
                   앱의 프로세스가 LRU 목록에 있는 경우 호출
                */

                break;

            default:
                /*
                  일반적인 메모리 부족 메시지 출력
                */
                break;
        }
    }
}
```

onTrimMemory() 콜백 메서드는 API 14레벨 이상부터 적용되며, 이전 버전에서 사용하게될때는 onLowMemory() 메서드의 콜백이 TRIM_MEMORY_COMPLETE 값과 거의 동일하게 동작합니다.</br>

#### Check how much memory you should use(사용해야 하는 메모리의 양 확인)

여러 프로세스를 실행하기 위해 안드로이드에서는 각 앱의 힙 크기를 제한합니다.</br>
힙의 최대 크기는 디바이스가 사용할수 있는 RAM의 크기에 따라 다릅니다.</br>
응용프로그램이 할당된 최대 힙의 크기보다 많은 메모리를 할당받고자 한다면 시스템에서 OutOfMemoryError가 발생합니다.</br>

메모리 부족 현상을 피하기 위해서는 시스템 쿼리를 통하여 현재 디바이스의 힙의 크기를 결정할 수 있습니다.</br>
시스템 쿼리를 호출하기 위해서는 getMemoryInfo() 메서드를 사용합니다.</br>
메서드를 호출하면 반환값으로 사용가능한 메모리, 총 메모리 등을 포함한 디바이스의 현재 메모리 상태에 대한 정보를 제공하는 ActivityManager.MemoryInfo 객체가 반환됩니다.</br>
또한 ActivityManager.MemoryInfo 객체의 lowMemory boolean 변수를 통해 메모리가 부족한지 여부를 알 수 있습니다.</br>

``` java
public void doSomethingMemoryIntensive() {

    // Before doing something that requires a lot of memory,
    // check to see whether the device is in a low memory state.
    ActivityManager.MemoryInfo memoryInfo = getAvailableMemory();

    if (!memoryInfo.lowMemory) {
        // Do memory intensive work ...
    }
}

// Get a MemoryInfo object for the device's current memory status.
private ActivityManager.MemoryInfo getAvailableMemory() {
    ActivityManager activityManager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
    ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
    activityManager.getMemoryInfo(memoryInfo);
    return memoryInfo;
}
```

</br>

---

### Use More Memory-Efficient Code Constructs(메모리 효율이 더 뛰어난 코드 구성 사용)

일부 안드로이드의 기능, 자바의 클래스 및 코드 구조는 다른 것들보다 많은 메모리를 사용될 수 있습니다.</br>
보다 효율적인 대안을 선택하여 앱에서 사용되는 메모리의 양을 최소화 할 수 있습니다.</br>

#### Use services sparingly(서비스를 아껴서 사용)

사용하지 않는 서비스를 실행상태로 두는 것은 안드로이드 앱에서 치명적인 실수 중 하나입니다.</br>
앱의 백그라운드에서 작업을 수행해야하는 서비스가 필요한 경우 작업이 끝난 후 메모리 누수를 방지하기 위해 서비스를 중지해야 합니다.</br>
서비스가 시작되게 되면 시스템은 해당 서비스가 실행되는 프로세스를 유지하려 합니다.</br>
이는 서비스에서 사용되는 RAM을 다른 프로세스에서 사용할 수 없기 때문에 다른 프로세스에서 사용될 메모리가 적어지게 됩니다.</br>
이렇게 되면 시스템이 LRU 캐시에 유지할 수 있는 프로세스가 줄어들게 되어 응용프로그램을 전환할 때 효율적이지 않습니다.</br>
또한 메모리가 부족하여 앱을 더 이상 유지할 수 없게 되는 경우 앱이 중지 될 수 있습니다.</br>

#### Use optimized data containers(최적화 된 데이터 컨테이너 사용)

일부 클래스들은 모바일 디바이스에서 최적화되지 않았습니다.</br>
예를들어 HashMap은 모든 매핑에 대해 별도의 항목 객체가 필요하기 때문에 일반적인 구현은 메모리가 관리에 비효율적일 수 있습니다.</br>
SparseArray 클래스의 구성요소들(SparseBooleanArray, LongSparseArray 등)은 시스템에서 autobox이 필요하지 않으며, 매핑의 항목에 별도의 항목객체를 생성하지 않기때문에 모바일 환경에서 더 효율적입니다.</br>

#### Be careful with code abstractions(코드 추상화에 주의)

추상화는 코드 유연성 및 유지관리를 향상시킬 수 있기 때문에 추상화를 많이 사용하는 경우가 있습니다.</br>
하지만 추상화에는 많은 양의 메모리가 필요합니다.</br>
일반적으로 실행되는 코드의 양이 많아지기 때문에 코드를 메모리에 매핑하는데 더 많은 시간과 RAM이 필요하게 됩니다.</br>
그러므로 추상화가 큰 이점을 제공하지 않는다면 이를 피하는것이 좋습니다.</br>

#### Use nano protobufs for serialized data(직렬화 데이터에 나노 프로토콜 버퍼 사용)

protobuf(프로토콜 버퍼)는 언어 중립적이며 플랫폼 중립적인 확장성있는 메커니즘으로 XML과 유사하지만 크기가 작고 빠르며 단순 구조화된 데이터를 직렬화 하기 위해 구글에서 고안한 것입니다.</br>
데이터에 프로토콜 버퍼를 사용하려 한다면, 클아이언트측 코드에서 항상 나노 protobufs를 사용해야 합니다.</br>
일반적인 protobufs는 매우 장황한 코드를 생성하기 때문에 RAM 사용량 증가, APK 크기 증가 및 실행 속도 저하와 같은 문제들을 일으킬 수 있습니다.</br>

#### Avoid memory churn(메모리 변동 피하기)

가비지 컬렉션 이벤트는 일반적으로 앱 성능에 영향을 미치지 않지만 짧은시간동안 자주 발생하는 가비지 컬렉션 이벤트는 프레임 저하를 일으킬 수 있습니다.</br>
시스템이 가비지 컬렉션하는 시간이 길어질수록 오디오 렌더링 또는 스트리밍같은 다른 작업들을 수행하는데 오랜 시간이 걸립니다.</br>

종종 메모리 변동으로 인해 많은 가비지 컬렉션 이벤트가 동작할 수 있습니다.</br>
예를들어 for 루프 내에서 여러 임시 객체를 할당하거나 onDraw()에 Paint 또는 Bitmap 객체를 생성할 때 많은 메모리를 사용하기 때문에 가비지 컬렉션 이벤트가 동작할 수 있습니다.</br>
안드로이드 스튜디오의 메모리 프로파일러 기능을 사용하면 메모리 변동이 많은 위치를 찾을 수 있습니다.</br>


</br>

---

### Remove Memory-Intensive Resources and Libraries(메모리 집약적 인 리소스 및 라이브러리 제거)

일부 리소스와 라이브러리는 메모리를 혼란스럽게 할 수 있습니다.</br>
코드에서 중복되거나 불필요한 컴포넌트, 리소스, 라이브러리를 제거하여 앱의 성능을 향상시킬 수 있습니다.</br>


#### Reduce overall APK size(전체 APK 크기 줄이기)

앱의 전체 크기를 줄임으로써 앱의 메모리 사용량을 크게 줄일 수 있습니다.</br>
비트맵 크기, 리소스, 애니메이션, 서드파티 라이브러리 등은 모두 APK의 크기에 관여합니다.</br>
안드로이드 스튜디오와 SDK에는 리소스 및 외부 종속성의 크기를 줄이는데 도움이되는 여러 도구들을 제공합니다.</br>

#### Use Dagger 2 for dependency injection(의존성 주입을 위해 대거 2 사용)

dependency injection 프레임워크는 작성한 코드들을 단순화 시키고 테스트 및 기타 요소 변경에 유용한 환경을 제공합니다.</br>
Dagger2 프레임워크는 컴파일 시에 구현되기 때문에 런타임 코스트와 메모리가 필요하지 않습니다.</br>

리플렉션을 사용하는 다른 dependency injection 프레임 워크는 코드에서 주석을 검색하여 프로세스를 초기화하는 경향이 있습니다.</br>
이 프로세스는 훨씬 더 많은 CPU 사이클과 RAM을 필요로 할 수 있으며 앱이 시작될 때 눈에 띄게 느려짐이 발생할 수 있습니다.</br>

#### Be careful about using external libraries(외부 라이브러리 사용에 주의)

모바일 환경을 위해 작성되지 않은 외부 라이브러리들은 모바일 클라이언트에서 작업할 때 비 효율적일 수 있습니다.</br>
때문에 모바일 환경으로 최적화 해야할 수도 있습니다.</br>
외부 라이브러리를 사용할 때 코드 크기와 램 공간 측면에서 라이브러리를 분석해야 합니다.</br>

</br>

---
#### 원문 출처 :

 * [Overview of Android Memory Management](https://developer.android.com/topic/performance/memory-overview.html) for Android Developers
 * [Manage Your App's Memory](https://developer.android.com/topic/performance/memory.html) for Android Developers
 
#### 참고 자료 :
 * [안드로이드 메모리 관리](http://www.hardcopyworld.com/ngine/android/index.php/archives/135) for Hard Copy Android
 * [세그먼테이션과 페이징](https://m.blog.naver.com/s2kiess/220149980093) for 네이버 블로그(우동)
 * [안드로이드 메모리 프로파일러](https://developer.android.com/studio/profile/memory-profiler.html) for Android Developers
 * [Java Garbage Collection](http://d2.naver.com/helloworld/1329) for Never D2(이상민 - NHN 생산성혁신랩)
</br>

---
