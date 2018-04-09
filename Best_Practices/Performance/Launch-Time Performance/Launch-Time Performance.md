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





</br>

---
#### 원문 출처 :

 * [Launch-Time Performance](https://developer.android.com/topic/performance/launch-time.html) for Android Developers
 
#### 참고 자료 :


</br>

---
