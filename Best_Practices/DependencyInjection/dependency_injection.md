# Dependency Injection


# 개요

Dependency Injection(의존성 주입, DI)는 프로그래밍에 많이 쓰이는 Android 개발에 적합한 기법 입니다.

DI의 원칙을 잘 따르면, 좋은 앱 아키텍처를 위한 기반을 다질 수 있습니다.

DI를 구현하면 다음과 같은 이점이 있습니다.

- 코드 재사용 가능
- 리팩터링의 편의성
- 테스트의 편의성

## Dependency Injection(의존성 주입)의 기본 사항

---

### Dependency Injection이란?

클래스들은 흔히 다른 클래스 참조가 필요합니다.

만약 Car라는 클래스에서 Engine이라는 클래스 참조가 필요하다면, 참조해야 하는 클래스를 dependency라고 합니다.

클래스가 객체를 얻는 방법은 세가지가 있습니다.

1. 클래스의 생성자에서 필요한 객체의 인스턴스를 생성 및 초기화합니다.
2. 다른 곳에서 객체를 가져옵니다.
    - 몇몇 안드로이드 API들은(Context의 getters, getSystemService()) 이러한 방식으로 동작합니다.
3. 매개변수로 객체를 제공받습니다.
    - 앱에서 클래스 생성시 또는 함수로 dependencies가 필요한 곳에 제공할 수 있습니다.

이때 세번째 방법이 Dependency Injection입니다.

Dependency Injection를 사용하면, 클래스에서 인스턴스를 자체적으로 생성하지 않고 dependencies를 제공받아서 사용할 수 있습니다.

Dependency Injection없이 코드 내부에서 dependency를 생성하는 예제는 다음과 같습니다.

```kotlin
class Car {

    private val engine = Engine()

    fun start() {
        engine.start()
    }
}

fun main(args: Array) {
    val car = Car()
    car.start()
}
```

![https://developer.android.com/images/training/dependency-injection/1-car-engine-no-di.png](https://developer.android.com/images/training/dependency-injection/1-car-engine-no-di.png)

이 예제는 Dependency Injection을 사용하지 않고, Car 클래스 생성 시 자체적으로 Engine 인스턴스를 생성하는 방식입니다.

이 방식은 다음과 같은 문제가 생길 수 있습니다.

- Car과 Engine 클래스는 높은 결합도를 가집니다.
    - Car 인스턴스는 항상 하나의 Engine 타입만 고정으로 사용하기 때문에 상속(subclasses)이나 구현(implementations)을 사용하기 어렵습니다.
    - 만약 Engine 클래스를 다른 클래스로 대체하려면 기존 Car 클래스를 재사용 하는것이 아닌 다른 유형의 Car 클래스를 새로 생성해야 합니다.
- Engine 객체와 높은 dependency를 가지고 있는것은 테스트를 더욱 어렵게 만듭니다.
    - Car 객체는 실제 Engine 인스턴스를 사용하기 때문에 다른 테스트 케이스들에서 [test double](https://en.wikipedia.org/wiki/Test_double)을 이용하여 Engine 객체를 수정할 수 없게 됩니다.
        - Test Double에 대한 참고 자료: [https://beomseok95.tistory.com/295](https://beomseok95.tistory.com/295)

Dependency Injection를 사용하게 된다면 코드는 다음과 같게 변합니다.

Car 인스턴스는 객체 생성 시 자체적으로 Engine 객체를 생성하는 것이 아닌, Engine 객체를 매개변수로 받습니다.

```kotlin
class Car(private val engine: Engine) {
    fun start() {
        engine.start()
    }
}

fun main(args: Array) {
    val engine = Engine()
    val car = Car(engine)
    car.start()
}
```

![https://developer.android.com/images/training/dependency-injection/1-car-engine-di.png](https://developer.android.com/images/training/dependency-injection/1-car-engine-di.png)

main 함수에서 Car 객체를 사용합니다.

하지만 Car 객체는 Engine 객체에 종속적이기 때문에, Car 객체를 생성하기 전에 먼저 Engine 객체를 생성하여 그를 이용하여 Car 객체를 구성합니다.

이 방법은 다음과 같은 이점이 있습니다.

- Car 객체의 재사용 가능
    - Engine을 구현한 다양한 객체를 Car로 전달할 수 있습니다.
    - 예를들어 Engine 객체를 상속받아 구현한 자식 클래스 ElectronicEngine 객체가 있을 때, DI를 사용하여 추가 변경 없이 Car 객체에서 변경된 Engine 또는 ElectronicEngine를 전달할 수 있습니다.
- Car 객체의 테스트 편의성
    - 테스트 더블을 이용하여 다양한 시나리오를 테스트할 수 있습니다.
    - 예를들어 FakeEngine이라는 Engine의 테스트 더블을 생성하여 다양한 테스트들을 구성할 수 있습니다.

안드로이드에서 Dependency Injection을 사용하는 두가지 주요 방법은 다음과 같습니다.

- 생성자 삽입(Constructor Injection)
    - 클래스의 dependencies를 생성자에 전달합니다.
- 필드 삽입 또는 setter 삽입(Field Injection or Setter Injection)
    - Activity 또는 Fragment와 같은 특정 안드로이드 프레임워크 클래스들은 시스템에서 인스턴스화 하기 때문에 생성자 삽입이 불가능하기 때문에, 클래스가 생성된 후 필드 삽입을 통해 인스턴스화 시킵니다.

```kotlin
class Car {
    lateinit var engine: Engine

    fun start() {
        engine.start()
    }
}

fun main(args: Array) {
    val car = Car()
    car.engine = Engine()
    car.start()
}
```

> Dependency Injection은 일반 코드가 특정 코드의 실행을 제어하는 Inversion of Control을 기반으로 합니다.

### Dependency Injection 자동화

이전 예에서는 라이브러리 없이 다양한 클래스의 dependencies를 직접 생성, 제공, 관리 했습니다. 

이를 직접 dependency injection, 또는 수동 dependency injection이라고 합니다.

Car 예제에서는 dependency가 하나밖에 없었지만, 클래스와 dependency가 많아지면 수동으로 dependency injection하는 작업이 번거로울 수 있습니다.

또한 수동 dependency injection은 다음과 같은 문제점들도 있습니다.

## Dependency Injection의 대안

---
