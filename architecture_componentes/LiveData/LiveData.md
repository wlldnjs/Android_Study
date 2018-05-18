### LiveData

LiveData는 observable data holder 클래스 입니다.

일반적인 옵저버블과 달리 LiveData는 생명주기를 인식하기 때문에 다른 액티비티나 프래그먼트같은 컴포넌트 또는 서비스의 생명주기에 반응합니다.

이러한 반응은 LiveData가 활성상태의 생명주기의 앱 컴포넌트 옵저버들만 업데이트하도록 합니다.

LiveData는 현재 생명주기의 상태가 STARTED 또는 RESUMED일 때, 옵저버(Observer 클래스)가 활성 상태라고 판단합니다.

LiveData는 활성화 상태의 옵저버에게만 업데이트 상태를 알려주며, 비활성 상태의 옵저버에는 변경사항에 대한 알림이 표시되지 않습니다.

LifecycleOwner 인터페이스를 구현한 객체와 쌍을 이루는 옵저버를 등록할 수 있으며, 쌍을 이루어 등록함으로써 Lifecycle객체가 DESTROYED상태로 변할 때 해당 옵저버가 삭제되게 합니다.

이는 액티비티와 프래그먼트의 생명주기가 destroyed될 떄 옵저버가 해제되기 때문에 LiveData 객체를 안전하게 관찰할 수 있고 메모리릭에 대한 우려도 사라집니다.


### The advantages of using LiveData(LiveData 사용의 이점)

LiveData를 사용하면 아래와 같은 이점이 있습니다.

#### UI를 데이터 상태와 일치시킵니다.

LiveData는 옵저버 패턴을 따르기 때문에 생명주기의 상태가 변경될 때 옵저버 객체에 상태가 변경되었다고 알려줍니다.

그렇기 때문에 앱의 데이터가 변경될 때마다 UI를 업데이트 하는것이 아닌 옵저버가 변경사항이 있을 때마다 변경시켜주게할 수 있습니다.


#### 메모리릭이 일어나지 않습니다.

옵저버는 Lifecycle 객체에 바인딩 되기 때문에 생명주기가 destroyed될 때 함께 정리됩니다.

#### 액티비티 중지로인한 충돌이 일어나지 않습니다.

옵저버의 생명주기가 비활성화일 때(액티비티가 백스택에 들어갔을 때와 같은) 옵저버는 LiveData 이벤트 정보를 수신하지 않습니다.

#### 필요없는 수동 생명주기 관리

UI컴포넌트는 연관된 데이터를 관찰만 할 뿐 stop/resume을 신경쓰지 않아도 됩니다.

LiveData를 이용하면 관찰하는 동안 생명주기가 변경될 때 자동으로 관리해줍니다.

#### 항상 최신의 데이터 유지

생명주기가 비활성화 상태에서 활성화 상태로 전환되면 최신의 데이터를 불러옵니다(예를들어 백그라운드 상태의 액티비티가 포그라운드 상태로 전환).

#### 구성(Configuration) 변경에 따른 적절한 대응

액티비티또는 프래그먼트의 구성이 변경되어(예를들어 디바이스 회전) 재생성되면 즉시 사용 가능한 최신 데이터를 받아옵니다.

#### 리소스 공유

싱글턴 패턴을 이용해 시스템 서비스를 래핑하여 LiveData객체가 공유될 수 있도록 확장할 수 있습니다. 
LiveData 오브젝트가 시스템 서비스에 한번 연결되면, 리소스가 필요한 모든 옵저버는 LiveData 객체를 볼 수 있습니다



### Work with LiveData objects

LiveData 객체를 사용하기위해 다음과 같은 단계를 따르십시오.

1. 특정 타입의 데이터를 보유할 LiveData 인스턴스 생성.
2. LiveData 객체가 가진 데이터가 변할 때 어떻게 동작할지 제어하는 onChanged() 메서드를 정의한 Observer객체를 생성. 
대부분 Observer 객체는 액티비티나 프래그먼트의 UI컨트롤러에서 생성합니다.
3. observe() 메서드를 이용하여 옵저버 객체를 LiveData 객체에 붙입니다.
observe() 메서드는 LifecycleOwner 객체를 받으며, Observer 객체가 LiveData의 데이터 변화를 감지합니다. 

```
 * Note : observeForever(Observer) 메소드를 이용하여 연관된 LifecycleOwner 객체에 상관없이 옵저버를 등록할 수 있습니다. 
이 경우, 옵저버는 항상 활성상태이고, 항상 상태가 변경될 때 알림을 받게 됩니다. 
또한 removeObserver(Observer) 메서드를 이용하면 등록된 옵저버를 제거할 수 있습니다.
```

LiveData 객체에 저장된 값이 업데이트 되면, 현재 붙어있는 활성화 상태의 LifecycleOwner들에게 트리거 됩니다.

LiveData UI컨트롤러의 옵저버가 변경된 사항을 알아차리게 할 수 있습니다. 
LiveData가 가지고 있는 데이터가 변경되면, UI는 자동으로 화면을 갱신합니다.



### Create LiveData objects

LiveData는 Collections(List와 같은)을 포함하여 모든 데이터와 함께 사용될수 있는 wrapper클래스 입니다.

일반적으로 ViewModel 객체 안에 저장되며 getter 메서드를 통해 접근하도록 구현합니다.

``` java
public class NameViewModel extends ViewModel {

// Create a LiveData with a String
private MutableLiveData<String> mCurrentName;

    public MutableLiveData<String> getCurrentName() {
        if (mCurrentName == null) {
            mCurrentName = new MutableLiveData<String>();
        }
        return mCurrentName;
    }

// Rest of the ViewModel...
}
```

첫 초기화 시점에는 LiveData 객체에 데이터가 설정되지 않습니다.

```
 * Note : 다음과 같은 이유 때문에, UI를 업데이트 하는 LiveData 객체는 액티비티나 프래그먼트가 아닌 ViewModel에 저장해야 합니다.
    - 액티비티나 프래그먼트 내부에 코드가 집중되는 것을 피하기 위함. UI컨트롤러는 주어진 데이터만 잘 보여주면 되기 때문에 데이터의 상태를 저장하지 않아도 됩니다.
    - LiveData를 특정 액티비티나 프래그먼트의 객체 종속시키지 않고, 구성변경으로부터 LiveData 인스턴스가 살아있도록 하기 위해.
```


#### Observe LiveData objects

대부분의 경우 OnCreate() 메서드가 LiveData를 관찰하기 가장 적절합니다.

- onResume() 메서드와 같이 중복으로 호출되지 않음을 보장합니다.
- Activity나 Fragment가 활성화가 되자마자 화면에 표시할 데이터가 있는지를 확인할 수 있습니다. 
LiveData가 observed상태로 설정되어 있을 때, 앱 컴포넌트가 STARTED 상태가 되면 LiveData로부터 가장 최근의 데이터를 받습니다.

일반적으로, LiveData는 데이터의 변화가 있을 때에만, 활성화된 옵저버에게만 데이터가 변경되었음을 알립니다. 

이 동작의 예외케이스로는, 옵저버의 상태가 비활성에서 활성으로 바뀔 때 데이터 변경 알림을 받는것 입니다. 

또한, 옵저버의 상태가 비활성화에서 활성화로 두번째로 변경되면, 마지막 활성화 상태 이후로 값의 변경이 있을 때에만 업데이트 알림을 받습니다.

아래의 코드는 어떻게 LiveData를 Observing하는지 보여줍니다.

``` java
public class NameActivity extends AppCompatActivity {

    private NameViewModel mModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Other code to setup the activity...

        // Get the ViewModel.
        mModel = ViewModelProviders.of(this).get(NameViewModel.class);


        // Create the observer which updates the UI.
        final Observer<String> nameObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String newName) {
                // Update the UI, in this case, a TextView.
                mNameTextView.setText(newName);
            }
        };

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        mModel.getCurrentName().observe(this, nameObserver);
    }
}
```

observe() 메서드의 파라미터로 nameObserver를 전달하면 뷰모델에 저장된 mCurrentName이 변경될 때 onChanged() 메서드를 통하여 바뀐 값이 바로 적용됩니다.

만약 mCurrentName에 LiveData가 설정되어 있지 않으면 onChanged() 메서드는 호출되지 않습니다.


### Update LiveData objects

LiveData에는 저장된 데이터를 업데이트할 public 메서드가 없습니다.

MutableLiveData 클래스는 setValue(T)와 postValue(T) public 메서드를 제공하며, LiveData를 수정하고자 할 때, 이 메서드들을 사용해야 합니다.

일반적으로 수정가능한 MutableLiveData는 ViewModel 내부에서 사용되고, 옵저버에게는 수정 불가능한 LiveData로 를 제공합니다.

옵저버를 연결한 후에, 다음의 예제와 같이 LiveData 객체의 값을 변경할 수 있습니다.

``` java
mButton.setOnClickListener(new OnClickListener() {
    @Override
    public void onClick(View v) {
        String anotherName = "John Doe";
        mModel.getCurrentName().setValue(anotherName);
    }
});
```

이 예제에서 setValue(T) 메서드를 호출하면 옵저버에서 바뀐 값인 'John Doe' 과 함께 onChanged() 메서드를 호출합니다.. 

이 예제에서는 버튼클릭을 보여줬지만, setValue()과 postValue() 메서드는 네트워크 요청, 데이터베이스 조회 등 다양한 상황에서 데이터를 변경하고 싶을 때 호출될 수 있습니다. 

setValue()와 postValue() 메서드가 호출된 다음에는 옵저버를 트리거하여 UI를 갱신하도록 해야합니다.

```
* Note : 메인스레드에서 LiveData를 갱신하려면 반드시 setValue(T) 메서드를 호출해야 하며, 만약 worker 스레드에서 LiveData를 갱신하려면 postValue(T) 메서드를 호출해야 합니다.
```


### Extend LiveData

옵저버의 상태가 STARTED 또는 RESUMED일 때 LiveData는 옵저버를 활성 상태로 판단합니다.

``` java
public class StockLiveData extends LiveData<BigDecimal> {
    private StockManager mStockManager;

    private SimplePriceListener mListener = new SimplePriceListener() {
        @Override
        public void onPriceChanged(BigDecimal price) {
            setValue(price);
        }
    };

    public StockLiveData(String symbol) {
        mStockManager = new StockManager(symbol);
    }

    @Override
    protected void onActive() {
        mStockManager.requestPriceUpdates(mListener);
    }

    @Override
    protected void onInactive() {
        mStockManager.removeUpdates(mListener);
    }
}
```

이 예제에는 다음의 중요한 메서드들에 포함되어 있습니다.
- onActivity() 메서드는 LiveData 인스턴스가 활성 옵저버를 가지고 있을 때 호출된다.
- onInactive() 메서드는 LiveData 인스턴스가 활성화된 옵저버를 가지고있지 않을 때 호출된다.
- setValue(T) 메서드는 LiveData 인스턴스의 값을 갱신하고, 변화에 대하여 활성 옵저버에게 알려준다.


StockLiveData 클래스는 다음과 같이 생성할 수 있습니다.

``` java
public class MyFragment extends Fragment {
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LiveData<BigDecimal> myPriceListener = ...;
        myPriceListener.observe(this, price -> {
            // Update the UI.
        });
    }
}
```


---

#### 참고 자료 :

* [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) for Android Developer

</br>

---
