### Verifying App Behavior on the Android Runtime(ART)(ART에서의 앱 동작 확인)

### 안드로이드 컴파일러

안드로이드의 컴파일러에는 Dalvik과 ART가 있습니다. </br>

#### Dalvik

일반적인 컴파일 언어는 CPU의 아키텍쳐와 플랫폼의 환경에 맞추어 기계어로 컴파일 되지만, 자바의 경우 기본적으로 한가지 CPU의 아키텍쳐나 환경에 맞추는 것이 아닌 바이트코드로 컴파일 되기 때문에 이를 실행하기 위해서는 자바 가상 머신이 필요합니다.</br>
이렇게 하는 이유는 자바는 바이트코드 하나만으로 여러 아키텍쳐나 플랫폼에서 작동시키게 위함 입니다.</br>
안드로이드 또한 자바 언어를 사용하기 떄문에 VM이 필수적인데, 기존 JVM을 사용할 수 있지만 라이선스 문제 때문에 구글에서 달빅VM을 따로 개발해서 안드로이드에서 사용했습니다.</br>

달빅은 초기엔 앱이 구동되는 중간에 실시간으로 자바 코드를 CPU에 맞게 변환하는 방식으로 구동되었고, 2.2버전 이후에 JIT(Just-In-Time) 컴파일러가 추가되었습니다.</br>

JIT 컴파일러는 실행 시점에 소스코드를 번역하기 때문에, 설치는 빠르지만 실행 시점에 느리며 번역한 정보들을 메모리에 올려야 하기 때문에 메모리를 많이 사용하게 됩니다.</br>

#### ART

ART는 기존 달빅VM의 한계점을 해결하기 위해 구글에서 새로 개발하였으며, 안드로이드 5.0부터 정식으로 채택되었습니다.</br>

ART는 AOT(Ahead-Of-Time) 컴파일러가 기본적으로 적용이 되며 프로그램 최초 실행 시가 아닌 주로 설치 시 한번에 전체를 변환해두고 저장한 뒤 프로그램이 실행될 때 변환된 코드를 읽어 구동합니다.</br>

그 때문에 설치가 느리고 번역한 파일을 따로 저장해두기 때문에 용량을 많이 차지하지만 실행 시점에 미리 번역해둔 파일을 실행하기 때문에 빠르게 실행 가능합니다.</br>

안드로이드 누가(7.0)이후의 ART VM 에서는 JIT와 AOT를 모두 탑재하였습니다.</br>
최초 설치 시에는 JIT를 사용하여 설치 시간과 용량을 적게 소모한 후 차후 기기를 사용하지 않을 때나 충전 중일 경우 컴파일을 조금씩 하여, 자주 사용되는 앱을 AOT방식으로 전환하는 방법으로 구동됩니다.</br>

### ART에서의 앱 동작

이처럼 컴파일러가 다르기 때문에 달빅에서 작동하는 일부 기법들이 ART에서 작동하지 않을 수 있습니다.</br>
아래에서는 기존 앱을 ART와 호환되도록 마이그레이션 할 때의 주의 사항을 알아봅니다.</br>


#### 가비지 컬렉션(GC) 문제 해결

달빅에서는 가비지 컬렉션을 위해 System.gc() 메서드를 명시적으로 호출하는 것이 유용합니다.</br>
하지만 ART에서는 이 작업이 불필요 하며, 특히 GC_FOR_ALLOC(메모리 요청 시 남아있는 힙 공간이 부족하여 가비지 컬렉션을 통해 여유공간을 확보하겠다) 유형의 발생을 예방하거나 단편화를 줄이기 위해 가비지 컬렉션을 호출하는 경우 더욱 불필요합니다.</br>

ART에서 개선된 가비지 컬렉터에는 달빅의 가비지 컬렉터를 능가하는 몇 가지의 향상된 기능들이 포함됩니다.

1. Mark and Sweep 알고리즘을 병렬로 처리 함으로써 가비지 컬렉션시 발생하는 지연을 두번에서 한번으로 줄였습니다.
2. 최근 할당 된 수명이 짧은 개체들을 빠르게 가비지 컬렉션 처리 할 수 있습니다.
3. 힙이 꽉차기 전에 미리 GC를 실행하기 때문에 앱이 중지되는 문제가 개선됩니다.

만약 어떤 런타임을 사용하는지 확인하려면, System.getProperty("java.vm.version") 메서드를 호출합니다.</br>
"2.0.0" 이상이라면 ART를 사용하는 것이며, 그 이전 버전이라면 달빅을 사용하는 것입니다.</br>

```
04-20 17:42:45.031 22916-22916/co.kr.schooling.schooling D/java.vm.version: 2.1.0
```

또한 메모리 관리를 개선하기 위해 Android Open-Source Project(AOSP)에서 compacting 가비지 컬렉터를 개발중 입니다.</br>
때문에 간결한 가비지 컬렉터와 호환되지 않는 기술들은 사용을 피하는게 좋습니다(객체 인스턴스 데이터에 포인터를 저장하는 등).</br>


#### JNI 문제 예방

ART의 JNI는 달빅보다 좀 더 엄격합니다.</br>
CheckJNI 모드를 사용하면 일반적인 문제들을 잡아낼 수 있습니다.</br>


#### 오류 처리

ART에서 JNI는 달빅에서는 발생하지 않는 여러 경우의 오류를 유발합니다.(위에서 말한 것처럼, CheckJNI로 테스트하면 이러한 경우의 오류를 많이 잡아낼 수 있습니다.)</br>

예를 들어, ProGuard와 같은 도구에 의해 메서드가 제거되어서 더 이상 존재하지 않는 메서드와 함께 RegisterNatives가 호출된 경우, ART에서 NoSuchMethodError가 발생합니다.</br>

```
08-12 17:09:41.082 13823 13823 E AndroidRuntime: FATAL EXCEPTION: main
08-12 17:09:41.082 13823 13823 E AndroidRuntime: java.lang.NoSuchMethodError:
    no static or non-static method
    "Lcom/foo/Bar;.native_frob(Ljava/lang/String;)I"
08-12 17:09:41.082 13823 13823 E AndroidRuntime:
    at java.lang.Runtime.nativeLoad(Native Method)
08-12 17:09:41.082 13823 13823 E AndroidRuntime:
    at java.lang.Runtime.doLoad(Runtime.java:421)
08-12 17:09:41.082 13823 13823 E AndroidRuntime:
    at java.lang.Runtime.loadLibrary(Runtime.java:362)
08-12 17:09:41.082 13823 13823 E AndroidRuntime:
    at java.lang.System.loadLibrary(System.java:526)
```

또한 메서드가 없이 RegisterNatives가 호출된 경우 ART에서 logcat에 오류가 기록됩니다.</br>

```
W/art     ( 1234): JNI RegisterNativeMethods: attempt to register 0 native
methods for <classname>
```

또한 JNI 함수인 GetFieldID() 또는 GetStaticFieldID()에 null이 반환되는 대신 NoSuchFieldError가 발생합니다. </br>
또한 GetMethodID() 및 GetStaticMethodID() 메서드 에서도 NoSuchMethodError가 발생합니다. </br>
이 경우 처리되지 않은 예외(또는 네이티브 코드의 Java 호출자에 발생한 예외)로 인해 CheckJNI가 실패할 수 있습니다. </br>
이것은 CheckJNI 모드에서 ART 호환 앱을 테스트할 때 특히 중요합니다.</br>

JNI 사양에 따라 ART에서는 서브클래스를 사용하는 대신 method's declaring class(CallNonvirtualVoidMethod()와 같은 JNI 메서드)를 사용해야 합니다.</br>


#### 스택 크기 문제 예방

달빅에서는 네이티브 코드와 자바 코드에 별도의 스택을 사용합니다.</br>
기본 자바 스택의 크기는 32KB이며 기본 네이티브 코드의 스택 크기는 1MB입니다.</br>
ART에는 더 향상된 지역성(locality)를 위해 통합 스택이 존재합니다.</br>
일반적으로 ART 스레드 스택의 크기는 달빅과 동일해야 하지만, 스택의 크기를 명시적으로 설정하는 경우에는 ART에서 실행중인 앱에 대해 이 값을 다시 설정해야 합니다.</br>

- 자바에서 스레드의 생성자에 명시적으로 스택의 크기를 지정하는 코드를 고려하라. </br> StackOverflowError가 발생하는 경우 스택의 크기를 늘릴 필요가 있다.
- C/C++의 경우 JNI를 통해 자바 코드를 실행하는 스레드에 pthread_attr_setstack() 및 pthread_attr_setstacksize() 메서드 사용을 고려하라. </br> 만약 pthread 크기가 너무 작을 때 JNI AttachCurrentThread() 메서드를 호출하려 하면 다음과 같은 오류가 나타난다.

```
F/art: art/runtime/thread.cc:435]
    Attempt to attach a thread with a too-small stack (16384 bytes)
```

</br>

#### Object 모델 변경

달빅에서 서브클래스에서 package-private 메서드를 재정의 할 수 있도록 잘못 허용하였지만, ART에서는 경고 메시지가 출력됩니다.</br>

```
Before Android 4.1, method void com.foo.Bar.quux()
would have incorrectly overridden the package-private method in
com.quux.Quux
```

만약 다른 패키지에서 클래스의 메서드를 재정의 하려는 경우엔, 해당 메서드를 public 또는 protected로 선언해야 합니다.</br>


#### AOT 컴파일 문제 해결

ART의 AOT Java 컴파일은 모든 표준 Java 코드에서 작동해야 합니다.</br>
컴파일은 ART의 dex2oat 도구로 수행됩니다.</br>
이때 dex2oat에 다음과 같은 문제가 발생할 수 있습니다.</br>

- ART는 설치 시에 Dalvik보다 더 엄격한 바이트코드 검사를 수행합니다. </br>
Android 빌드 도구에서 생성된 코드는 문제가 되지 않지만, 일부 후처리 도구(특히 난독화를 수행하는 도구)에서 생성될 수 있는 잘못된 파일들은 달빅에서는 허용되지만 ART에서는 거부됩니다. </br>
이의 해결 방법으로는 툴을 최신버전으로 업데이트 하고 DEX 파일을 재생성함으로써 문제를 해결할 수 있습니다.</br>

- ART verifier(확인자)에 의해 발생되는 일반적인 문제는 다음과 같습니다.</br>
        - 잘못된 제어 흐름</br>
        - 불균형인 moniterenter/moniterexit</br>
        - 길이가 0인 파라미터 타입의 리스트 크기</br>

```
* dex 파일 에는 Dalvik 클래스로 바뀐 모든 Java 클래스가 포함됩니다.
최근에는 안드로이드 런타임(ART)와 미리 컴파일해주는 AOT(ahead-of-time) compilation으로 전체 전환되거나, 
부분적으로 just-in-time(JIT) 방식을 사용하므로 이들 몇몇은 같은 방식으로 사용되지 않을 수도 있지만, 
해당 형식이 고정돼 있으므로 모두 APK에 있어야 합니다. 
```

일부 앱의 경우 /system/framework, /data/dalvik-cache 또는 DexClassLoader의 최적화된 출력 디렉터리에서 설치된 .odex 파일 형식에 종속성이 있습니다.</br>
AOT에서는 dex 파일을 컴파일하여 oat포멧의 ELF 바이너리 형태의 파일(ART에서 설치 시점에 번역하여 컴파일한 파일)로 만들어 앱을 구동합니다. </br>

</br>

---
#### 원문 출처 :

* [Verifying App Behavior on the Android Runtime(ART)](https://developer.android.com/guide/practices/verifying-apps-art.html) for Android Developers

</br>

---
#### 참고 자료 :
* [ART vs Dalvik](https://software.intel.com/en-us/android/articles/art-vs-dalvik-introducing-the-new-android-x86-runtime) for Intel Software Developer
* [안드로이드 런타임](https://namu.wiki/w/%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C%20%EB%9F%B0%ED%83%80%EC%9E%84) for 나무위키
* [안드로이드 어플리케이션 실행 과정](http://sanseolab.tistory.com/32) for Tistory
* [Classes.dex에 대한 자료](https://academy.realm.io/kr/posts/jon-reeve-reverse-engineering-is-not-just-for-hackers-android/) for Realm
---
