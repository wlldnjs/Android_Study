* ### Java Generic

  #### 제네릭이란?

  - Java 5부터 도입
  - 컬렉션, 람다식, 스트림, NIO에서 사용
  - 타입 파라미터 사용 가능

     ``` java
     public interface Map<K, V>
     ```

     ``` kotlin
     abstract class BaseViewHolder<T>(itemView: View): RecyclerView.ViewHolder(itemView) {
         abstract fun bind(item: T)
     
         open fun bind(item: T, selected: Boolean) {
     
         }
     }
     ```

  - 컴파일 시 강한 타입체크 가능

      - 제네릭을 사용하면 컴파일시 제네릭 코드에 대해 강하게 타입 체크를 하기 때문에, 실행 이전에 에러를 사전 방지할 수 있다. 
  - 타입변환(Casting) 제거 가능
     - 제네릭으로 타입을 명시하여 해당 타입만 사용 가능하기 때문에 불필요한 타입 변환을 방지하여 성능 향상을 도모할수 있다.
     ``` java
      // 비 제네릭 코드
      List list = new ArrayList();
      list.add("hello");
      String str = (String)list.get(0);
      
      // 제네릭 코드
      List<String> list = new ArrayList<String>();
      list.add("hello");
      String str = list.get(0);  // 제네릭으로 String type을 지정했기 때문에 타입 변환을 하지 않는다.
     ```

  ---

  

  #### 제네릭 타입(class\<T>, interface\<T>)

  - 타입을 파라미터로 가지며, 클래스 또는 인터페이스 뒤에 <타입 파라미터>가 붙는다.

  ```
  public class 클래스명<T> { ... }   
   
  public interface 인터페이스명<T> { ... }
  ```

  - 일반적으로 제네릭 타입 파라미터는 아래와 같은 규칙을 따른다.

  ```
  - E: 요소(Element)
  - K: 키(Key)
  - N: 숫자(Number)
  - T: 타입(Type)
  - V: 값(Value)
  - S, U, V: 두번째, 세번째, 네번째에 선언된 타입
  ```

  - 실제 구현하는 코드에서는 타입 파라미터 자리에 구체적인 타입을 지정해 주어야 한다.

  ``` java
  public class Box<T> {
    private T t;
   
    public T get() { return t; }
   
    public void set(T t) { this.t = t }
  }
  ```

  ``` java
  // String 객체일 경우
  Box<String> box = new Box<String>();
  
  public class Box<String> {
    private String t;
   
    public String get() { return t; }
   
    public void set(String t) { this.t = t }
  }
  ```

  ``` java
  // Integer 객체일 경우
  Box<Integer> box = new Box<Integer>();
  
  public class Box<Integer> {
    private Integer t;
   
    public Integer get() { return t; }
   
    public void set(Integer t) { this.t = t }
  }
  ```

  - 제네릭을 사용하여 클래스를 설계할 때 타입파라미터로 설계를 해 두고, 실제로 사용할 때 구체적인 클래스를 지정해 줌으로써 컴파일러가 클래스를 재구성해준다. 따라서, 전혀 타입변환이 생기지 않는다.
  - 실제 사용시 구체적인 클래스를 지정해 줌으로써 컴파일러가 클래스를 재구성 했을 때, 강한타입 체크를 하게 되므로 사전에 컴파일 에러를 방지한다.
  - 결과적으로 제네릭을 사용하는것이 애플리케이션 성능을 좋게 만들 수 있다.

  

  ※ 제네릭을 사용하지 않았을 때 예시

  ```java
  public class Box {
    private Object object;
   
    public Object get() { return object; }
   
    public void set(Object object) { this.object = object }
  }
  
  ```

  ```java
  // 비 제네릭 코드
  Box box = new Box();
  box.set("김지원");		// String -> Object 자동 타입 변환
  String name = (String) box.get();	  // Object -> String 강제 타입 변환
  
  // 제네릭 코드
  Box<String> box = new Box<String>();
  box.set("김지원");
  String name = box.get();	  // String으로 타입이 지정되어 있기 때문에 타입변환 없음
  ```

  

  ---

  

  #### 멀티타입 파라미터

  - 두개 이상의 타입 파라미터를 선언하여 사용할 수 있다.

  ``` java
  class<K,V,...> { ... }
  interface<K,V,...> { ... }
  
  Product<TV, String> product = new Product<Tv,String>();
  // 자바7부터는 중복된 타입 파라미터 생략 가능 
  Product<TV, String> product = new Product<>();
  ```

  ---

  

  #### 제네릭 메소드

  - 매개변수 타입과 리턴 타입으로 타입 파라미터를 갖는 메소드를 말한다.

  - 제네릭 메소드 선언 방법
      - 리턴 타입 앞에 "< >"기호를 추가하고 타입 파라미터를 기술한다.

      - 타입 파라미터를 리턴타입(Box<T>)과 매개변수(T)에 사용한다.
      ``` java
      public <타입파라미터,...> 리턴타입 메소드명(매개변수,...) { ... }
       
      public <T> Box<T> boxing(T t) { ... }
      ```

  - 제네릭 메소드를 호출하는 두가지 방법
      ``` java
      // 1. 리턴타입 변수 = <구체적 타입> 메소드명(매개값);    // 명시적으로 구체적 타입 지정
      Box<Integer> box = <Integer>boxing(100); // 타입 파라미터를 명시적으로 Integer로 지정
      
      // 2. 리턴타입 변수 = 메소드명(매개값);  // 매개값을 보고 구체적 타입을 추정    
      Box<Integer> box = boxing(100);  // 타입 파라미터를 Integer로 추정
      ```

  - 일반적으로 매개값을 넣어줌으로 컴파일러가 유추하게 만들어주는 두번째 방법을 사용한다.

  

  ##### 예제

  ``` java
  public class Pair<K, V> {
    private K key;
    private V value;
   
    public Pair(K key, V value) {
      this.key = key;
      this.value = value;
    }
    public void setKey(K key) {
      this.key = key;
    }
   
    public void setValue(V value) {
      this.value = value;
    }
   
    public K getKey() {
      return key;
    }
   
    public V getValue() {
      return value;
    }
  }
  ```

  ``` java
  public class Util {
    public static <K, V> boolean compare(Pair<K, V> p1, Pair<K, V> p2) {
      boolean keyCompare, valueCompare;
   
      keyCompare = p1.getKey().equals(p2.getKey());
      valueCompare = p1.getValue().equals(p2.getValue());
   
      return keyCompare && valueCompare;
    }
  }
  ```

  ``` java
  public class CompareMethodEx {
    public static void main(String[] args) {
      Pair<String, Integer> p1 = new Pair<>("이승용", 3);
      Pair<String, Integer> p2 = new Pair<>("이승용", 3);
   
      boolean result = Util.compare(p1, p2);
      System.out.println(result);
   
      Pair<String, String> p3 = new Pair<>("송호식", "김지원");
      Pair<String, String> p4 = new Pair<>("송호식", "김지훈");
   
      result = Util.compare(p3, p4);
      System.out.println(result);
    }
  }
  
  ```

  

  ##### 결과 값

  ```
  true
  false
  ```

  ---

  

  #### 타입 파라미터 제한

  - 타입 파라미터에 지정되는 구체적인 타입을 제한할 필요가 있을 경우 => 상속 및 구현 관계를 이용해서 타입을 제한

  ``` java
  public <T extends 상위타입> 리턴타입 메소드(매개변수, ...) { ... }
  ```

  - 상위 타입은 클래스 뿐만 아니라 인터페이스도 가능하다. 인터페이스라고 해서 extends 대신 implements를 사용하지 않는다.

  - 상위타입이거나 하위 또는 구현 클래스만 지정할 수 있다.

  - 메소드의 중괄호 {} 안에서 타입 파라미터 변수로 사용 가능한 것은 상위 타입의 멤버(필드, 메소드)로 제한된다.


  ```java
  public <T extends Number> int compare(T t1, T t2) {
      double v1 = t1.doubleValue();
      double v2 = t2.doubleValue();
         
      return Double.compare(v1, v2);
  }
  ```

  

  ##### 예제

  ``` java
  public class Util {
    public static <T extends Number> int compare(T t1, T t2) {
      double v1 = t1.doubleValue();
      double v2 = t2.doubleValue();
   
      // Double.compare(v1, v2) : v1이 작으면 -1, v2가 크면 1, 같으면 0
      return Double.compare(v1, v2);
    }
  }
  
  public class BoundedTypeParameterEx {
    public static void main(String[] args) {
      // String srt = Util.compare("a","b");  // 불가능
        
      int result1 = Util.compare(8, 4);	// int -> Integer 자동 Boxing
      System.out.println(result1);
   
      result1 = Util.compare(3.5, 8);		// double -> Double 자동 Boxing
      System.out.println(result1);
    }
  }
  ```

  

  ##### 결과 값

  ```
  -1
  1
  ```

  

  ---

  

  #### 와일드카드 타입

  - 제네릭 타입을 매개변수나 리턴타입으로 사용할 때 타입 파라미터를 제한할 목적
  - <T extends 상위 타입 또는 인터페이스>는 제네릭 타입의 클래스나 메소드를 선언할 때 제한을 한다.

  - 와일드카드 타입의 세가지 형태

  ``` java
  /** 제네릭타입<?>
  타입 파라미터를 대치하는 구체적인 타입으로 모든 클래스나 인터페이스 타입이 올 수 있다. */
  public static void registerCourse(Course<?> course) {
  
  /** 제네릭타입<? extends 상위타입>
  타입 파라미터를 대치하는 구체적인 타입으로 상위 타입이나, 그 상위 타입의 하위 타입만 올 수 있다. 따라서, 상위 클래스 제한 이라고 한다. */
  public static void registerCourseStudent(Course<? extends Student> course) {
  
  /** 제네릭타입<? super 하위타입>
  타입 파라미터를 대치하는 구체적인 타입으로 하위 타입이나, 그 하위 타입의 상위 타입이 올 수 있다. 따라서, 하위 클래스 제한 이라고 한다. */
  public static void registerCourseWorker(Course<? super Worker> course) {
  ```

  

  ##### 예제

  ``` java
  public class Course<T> {
    private String name;
    private T[] students;
   
    public Course(String name, int capacity) {
      this.name = name;
      // 타입 파라미터로 배열 생성할때는 new T[n] 형태가 아닌 (T[])(new Object[n]) 형태로 생성해야 한다
      students = (T[]) (new Object[capacity]);
    }
   
    public String getName() {
      return name;
    }
   
    public T[] getStudents() {
      return students;
    }
   
    // 배열의 빈공간에 수강생을 추가하는 메소드
    public void add(T t) {
      for (int i = 0; i < students.length; i++) {
        if (students[i] == null) {
          students[i] = t;
          break;
        }
      }
    }
  }
  ```

  ``` java
  public class Person {
    private String name;
   
    public Person(String name) {
      this.name = name;
    }
   
    public String getName() {
      return name;
    }
   
    @Override
    public String toString() {
      return name;
    }
  }
  ```

  ``` java
  public class Student extends Person {
    public Student(String name) {
      super(name);
    }
  }
  ```

  ``` java
  public class Worker extends Person {
    public Worker(String name) {
      super(name);
    }
  }
  ```

  ``` java
  public class HighStudent extends Student {
    public HighStudent(String name) {
      super(name);
    }
  }
  ```

  ``` java
  public class WildCardEx {
    public static void registerCourse(Course<?> course) {
      System.out.println(course.getName() + " 수강생 : " + Arrays.toString(course.getStudents()));
    }
   
    public static void registerCourseStudent(Course<? extends Student> course) {
      System.out.println(course.getName() + " 수강생 : " + Arrays.toString(course.getStudents()));
    }
   
    public static void registerCourseWorker(Course<? super Worker> course) {
      System.out.println(course.getName() + " 수강생 : " + Arrays.toString(course.getStudents()));
    }
   
    public static void main(String[] args) {
      Course<Person> personCourse = new Course<>("일반인 과정", 5);
      personCourse.add(new Person("일반인"));
      personCourse.add(new Worker("직장인"));
      personCourse.add(new Student("학생"));
      personCourse.add(new HighStudent("고등학생"));
   
      Course<Worker> workerCourse = new Course<>("직장인 과정", 5);
      workerCourse.add(new Worker("직장인"));
   
      Course<Student> studentCourse = new Course<>("학생 과정", 5);
      studentCourse.add(new Student("학생"));
      studentCourse.add(new HighStudent("고등학생"));
   
      Course<HighStudent> highStudentCourse = new Course<>("고등학생 과정", 5);
      highStudentCourse.add(new HighStudent("고등학생"));
   
      // 모든 과정 등록 가능
      registerCourse(personCourse);
      registerCourse(workerCourse);
      registerCourse(studentCourse);
      registerCourse(highStudentCourse);
   
      System.out.println();
   
      // 학생 과정만 등록 가능
      // registerCourseStudent(personCourse);  // 불가능
      // registerCourseStudent(workerCourse);  // 불가능
      registerCourseStudent(studentCourse);
      registerCourseStudent(highStudentCourse);
   
      System.out.println();
   
      // 직장인과 인반인 과정만 등록 가능
      registerCourseWorker(personCourse);
      registerCourseWorker(workerCourse);
      // registerCourseWorker(studentCourse);  // 불가능
      // registerCourseWorker(highStudentCourse);  // 불가능
    }
  }
  ```

  

  ```java
  // 결과값
  일반인 과정 수강생: [일반인, 직장인, 학생, 고등학생, null]
  직장인 과정 수강생: [직장인, null, null, null, null]
  학생 과정 수강생: [학생, 고등학생, null, null, null]
  고등학생 과정 수강생: [고등학생, null, null, null, null]
  
  학생 과정 수강생: [학생, 고등학생, null, null, null]
  고등학생 과정 수강생: [고등학생, null, null, null, null]
  
  일반인 과정 수강생: [일반인, 직장인, 학생, 고등학생, null]
  직장인 과정 수강생: [직장인, null, null, null, null]
  ```

  ---

  

  #### 제네릭 타입의 상속과 구현

  - 제네릭 타입도 다른 타입과 마찬가지로 부모 클래스가 될 수 있다. 

  ``` java
  // Product<T, M> 제네릭 타입을 상속해서 ChildProduct<T, M> 타입을 정의
  public class ChildProduct<T, M> extends Product<T, M> { ... }
  ```

  - 자식 제네릭 타입은 추가적으로 타입 파라미터를 가질 수 있다.

  ``` java
  public class ChildProduct<T, M, C> extends Product<T, M> { ... }
  ```


  ``` java
  public class Product<T, M> {
      private T kind;
      private M model;
   
      public T getKind() {
          return kind;
      }
   
      public void setKind(T kind) {
          this.kind = kind;
      }
   
      public M getModel() {
          return model;
      }
   
      public void setModel(M model) {
          this.model = model;
      }
   
  }
  ```

  ``` java
  public class ChildProduct<T, M, C> extends Product<T, M> {
      private C company;
   
      public C getCompany() {
          return company;
      }
   
      public void setCompany(C company) {
          this.company = company;
      }
   
  }
  ```

  - 제네릭 인터페이스를 구현한 클래스도 제네릭 타입이 된다.

  ``` java
  public interface Storage<T> {
      public void add(T item, int index);
   
      public T get(int index);
  }
  ```

  ``` java 
  public class StorageImpl<T> implements Storage<T> {
   
      private T[] array;
   
      public StorageImpl(int capacity) {
          this.array = (T[]) (new Object[capacity]);
      }
   
      @Override
      public void add(T item, int index) {
          array[index] = item;
      }
   
      @Override
      public T get(int index) {
          return array[index];
      }
   
  }
  ```

  #### 참고 자료

  * 이것이 자바다[신용권의 Java 프로그래밍 정복] - 한빛미디어
  * [제네릭(Generic)](https://onsil-thegreenhouse.github.io/programming/java/2018/02/17/java_tutorial_1-21/) for Github
  * [제네릭(Generic) - 제한된 타입 파라미터, 와일드카드 타입](http://palpit.tistory.com/668) for palpit Vlog

  ---
---

