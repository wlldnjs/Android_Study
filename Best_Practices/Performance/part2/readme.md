>### Sending Operations to Multiple Threads

멀티 스레드 환경을 사용하면 장기적인 작업이나, 데이터 집약적인 작업을 할 때 속도와 효율성이 향상됩니다.

#### 스레드에서 실행할 코드 지정

이 과정에서는 runnable.run()방식으로 코드를 별도의 스레드에서 클래스를 구현하는 방법을 보여 줍니다.</br>
또한 다른 개체에 실행 파일을 전달하여 스레드에 연결하고 실행할 수도 있습니다.</br>
특정 작업을 수행하는 하나 이상의 실행 가능한 개체를 Task라고도 합니다.</br>
스레드 및 Runable은 기본 클래스이며, 단독으로 사용이 제한됩니다.</br>
대신에, 이들은 HandlerThread, Asynktask및 IntentService와 같은 안드로이드 클래스의 기초가 됩니다.</br>
또한 스레드와 runnable은 ThreadPoolExecutor의 기초가 됩니다.</br>
ThreadPoolExecutor는 자동으로 스레드 및 태스크 대기 열을 관리하며 여러 스레드를 병렬로 실행할 수도 있습니다. </br>

#### 실행 가능한 클래스 정의

Runnable 코드의 간단한 예시.

``` java
public class PhotoDecodeRunnable implements Runnable {
    ...
    @Override
    public void run() {
        /*
         * Code you want to run on the thread goes here
         */
        ...
    }
    ...
}
```

#### run() 메서드 Implement

알아두어야 할 주의사항은 Runable은 UI스레드에서는 작동하지 않으며, 또한 View를 직접 변경 할 수도 없습니다.</br>

run()메서드를 시작하기 전에, 백그라운드 우선순위를 설정하기 위해 THREAD_PRIORITY_BACKGROUND와 함께 Process.setThreadPriority()메서드를 호출해야 합니다.</br>
이 접근을 통해 Runable 스레드와 UI 스레드의 자원 경쟁을 줄일 수 있습니다.</br>

또한 현재 실행중인 Runable 스레드에 접근하기 위해서 Thread.currentThread()메서드를 사용할 수 있습니다.


``` java
class PhotoDecodeRunnable implements Runnable {
...
    /*
     * Defines the code to run for this task.
     */
    @Override
    public void run() {
        // Moves the current Thread into the background
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
        ...
        /*
         * Stores the current Thread in the PhotoTask instance,
         * so that the instance
         * can interrupt the Thread.
         */
        mPhotoTask.setImageDecodeThread(Thread.currentThread());
        ...
    }
...
}
```

#### 여러 스레드에 대한 관리자 생성

기본적으로 한번에 하나의 작업만 할때는 IntentService를 사용하는것이 좋지만, 
동시에 여러 작업을 실행할때는 ThreadPoolExecutor를 사용하여 스레드풀의 대기열에 작업을 추가하여 사용하는것이 좋습니다.</br>
- IntentService : 스레드 종류중 하나로써 비동기로 실행되는 스레드. 여러번 실행되면 queue방식으로 처리.
스레드풀에서의 작업은 병렬로 실행될 수 있으나, synchronized를 사용하여 하나의 작업공간에 두개의 스레드가 접근할 수 없게 막을수 있습니다.</br>

#### 스레드 풀 클래스 정의

한정된 CPU와 네트워크 리소스로 인해, 하나의 스레드풀 인스턴스를 static으로 선언하여 사용가능 합니다.

``` java
public class PhotoManager {
    ...
    static  {
        ...
        // Creates a single static instance of PhotoManager
        sInstance = new PhotoManager();
    }
    ...
```

private 속성의 생성자를 사용할 수 있다.
- 생성자를 private로 생성하게 되면, 싱글톤으로 동작하기 때문에 synchronized 블록을 통해 엑세스를 방지하지 않아도 된다.


``` java
public class PhotoManager {
    ...
    /**
     * Constructs the work queues and thread pools used to download
     * and decode images. Because the constructor is marked private,
     * it's unavailable to other classes, even in the same package.
     */
    private PhotoManager() {
    ...
    }
```

스레드풀 클래스에서 메서드를 호출하여 작업 실행.
- 스레드풀의 큐에 작업을 추가하는 메서드를 정의하여 사용한다.


``` java
public class PhotoManager {
    ...
    // Called by the PhotoView to get a photo
    static public PhotoTask startDownload(
        PhotoView imageView,
        boolean cacheFlag) {
        ...
        // Adds a download task to the thread pool for execution
        sInstance.
                mDownloadThreadPool.
                execute(downloadTask.getHTTPDownloadRunnable());
        ...
    }
```

핸들러 인스턴스의 생성자에서 UI스레드와 연결
- 핸들러 인스턴스는 handlerMessage() 메서드를 통해 앱에서 UI객체의 메서드를 안전하게 호출할 수 있도록 도와줍니다.


``` java
private PhotoManager() {
    ...
        // Defines a Handler object that's attached to the UI thread
        mHandler = new Handler(Looper.getMainLooper()) {
            /*
             * handleMessage() defines the operations to perform when
             * the Handler receives a new Message to process.
             */
            @Override
            public void handleMessage(Message inputMessage) {
                ...
            }
        ...
        }
    }
```


#### 스레드 풀 매개변수 지정

ThreadPoolExecutor 객체를 인스턴스화 하여 스레드풀을 정의 할 수 있습니다.

풀의 사이즈와 최대 크기를 설정
- 스레드풀의 스레드 수는 주로 디바이스의 코어 수에 따라 다르며, 시스템 환경에서 확인 할 수 있습니다.


``` java
public class PhotoManager {
...
    /*
     * Gets the number of available cores
     * (not always the same as the maximum number of cores)
     */
    private static int NUMBER_OF_CORES =
            Runtime.getRuntime().availableProcessors();
}
```

일부 디바이스에서는 시스템에 따라 코어가 비활성화 될 수 있기 때문에 availableProcessors() 메서드에서 반환되는 활성 코어는 총 코어보다 적을 수 있습니다.

활성 시간과 시간단위
- 스레드가 종료되기 전 대기 상대로 유지되는 기간. 이 기간은 TimeUnit단위로 정의됩니다.

Task Queue
- ThreadPoolExecutor가 runable 객체를 가져오는 수신 대기 열입니다. 
- 스레드에서 Task를 시작하면 ThreadPoolExecutor가 대기 열에서 선입선출 방식으로 runnable 객체를 가져와 스레드에 연결합니다. 
- BlockingQueue 인터페이스를 구현하는 queue 클래스를 사용하여 스레드 풀을 생성할 때 이 Task Queue 객체를 implements할 수 있습니다. 

#### 스레드풀 생성

스레드풀을 생성하기 위해서 ThreadPoolExecutor()를 호출하여 스레드풀 매니저를 인스턴스화 합니다.</br>
이것은 제한된 크기의 스레드 그룹을 생성하고 관리하지만 코어의 수에 따라 초기, 최대 풀 크기가 지정됩니다.</br>

```java
private PhotoManager() {
        ...
        // Sets the amount of time an idle thread waits before terminating
        private static final int KEEP_ALIVE_TIME = 1;
        // Sets the Time Unit to seconds
        private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
        // Creates a thread pool manager
        mDecodeThreadPool = new ThreadPoolExecutor(
                NUMBER_OF_CORES,       // Initial pool size
                NUMBER_OF_CORES,       // Max pool size
                KEEP_ALIVE_TIME,
                KEEP_ALIVE_TIME_UNIT,
                mDecodeWorkQueue);
    }
```

#### 스레드 풀에서 스레드에 대한 작업 실행

스레드에서 작업을 시작하려면 ThreadPoolExecutor.execute()의 Runnable을 거쳐야 합니다.</br>
이 호출로 스레드 풀의 작업 큐에 작업을 추가하고, 스레드가 사용 가능해지면 매니저는 가장 오래 기다린 작업부터 실행합니다.</br>

``` java
public class PhotoManager {
    public void handleState(PhotoTask photoTask, int state) {
        switch (state) {
            // The task finished downloading the image
            case DOWNLOAD_COMPLETE:
            // Decodes the image
                mDecodeThreadPool.execute(
                        photoTask.getPhotoDecodeRunnable());
            ...
        }
        ...
    }
    ...
}
```

작업을 종료하려면 작업 스레드에 인터럽트를 걸어야 합니다.</br>
스레드에 인터럽트 하기 위해서는 Thread.interrupt() 메서드를 호출하면 됩니다.</br>
스레드 객체는 시스템에 의해 컨트롤 되기 때문에 외부 앱에서 접근 가능합니다.</br>
그렇기 때문에 스레드에 접근하기 전에 synchronized 블록을 통해 접근을 제어하는것이 좋습니다.</br>

``` java
public class PhotoManager {
    public static void cancelAll() {
        /*
         * Creates an array of Runnables that's the same size as the
         * thread pool work queue
         */
        Runnable[] runnableArray = new Runnable[mDecodeWorkQueue.size()];
        // Populates the array with the Runnables in the queue
        mDecodeWorkQueue.toArray(runnableArray);
        // Stores the array length in order to iterate over the array
        int len = runnableArray.length;
        /*
         * Iterates over the array of Runnables and interrupts each one's Thread.
         */
        synchronized (sInstance) {
            // Iterates over the array of tasks
            for (int runnableIndex = 0; runnableIndex < len; runnableIndex++) {
                // Gets the current thread
                Thread thread = runnableArray[taskArrayIndex].mThread;
                // if the Thread exists, post an interrupt to it
                if (null != thread) {
                    thread.interrupt();
                }
            }
        }
    }
    ...
}
```

대부분의 경우 Thread.interrupt() 메서드를 호출하는순간 스레드는 즉시 중지됩니다.</br>
하지만 CPU 또는 네트워크 작업은 중지되지 않습니다.</br>
시스템이 락에 걸리지 않게 하려면 작업 전에 인터럽트 요청을 처리해야 합니다.</br>

``` java
/*
 * Before continuing, checks to see that the Thread hasn't
 * been interrupted
 */
if (Thread.interrupted()) {
    return;
}
...
// Decodes a byte array into a Bitmap (CPU-intensive)
BitmapFactory.decodeByteArray(
        imageBuffer, 0, imageBuffer.length, bitmapOptions);
...
```

#### UI스레드와 통신
안드로이드의 애플리케이션을 실행하면 시스템은 메인 액티비티를 메모리로 올려 프로세스로 만들며, 이 때 메인 스레드가 자동으로 생성됩니다. </br>
메인 스레드는 안드로이드의 주요 컴퍼넌트를 실행하는 곳이자 UI를 그리거나 갱신하는 일을 담당할 수 있는 유일한 스레드이므로 UI 스레드라고도 불립니다.</br>
다른 스레드에서는 UI작업에 접근 할 수 없기 때문에 Handler를 이용하여 UI스레드로 데이터를 전달하여 처리하여야 합니다.</br>

#### UI스레드에서 핸들러 정의

핸들러는 스레드를 관리하는 안드로이드 시스템의 프레임워크중 하나입니다.</br>
핸들러 객체는 메시지를 수신하고, 메세지를 처리합니다.</br>
핸들러를 UI 스레드에 연결하면 UI스레드에서 메세지가 처리됩니다.</br>

핸들러 객체를 생성할 때 스레드풀에 생성하여 전역변수에 저장하고, Handler(Looper) 생성자로 인스턴스화 하여 함께 UI스레드에 접근합니다.</br>
핸들러를 Looper인스턴스를 기반으로 생성하면, 핸들러는 Looper와 같은 스레드에서 실행됩니다.</br>

``` java
private PhotoManager() {
...
    // Defines a Handler object that's attached to the UI thread
    mHandler = new Handler(Looper.getMainLooper()) {
    ...
```

핸들러를 생성하면 handleMessage() 메서드가 오버라이드 됩니다.

``` java
        /*
         * handleMessage() defines the operations to perform when
         * the Handler receives a new Message to process.
         */
        @Override
        public void handleMessage(Message inputMessage) {
            // Gets the image task from the incoming Message object.
            PhotoTask photoTask = (PhotoTask) inputMessage.obj;
            ...
        }
    ...
    }
}
```

#### Task에서 UI스레드로 데이터 전달

백그라운드 스레드에서 실행중인 작업에서 UI스레드로 전달하려면, 참조데이터와 UI를 태스크객체에 담아야 합니다.</br>
그 후 태스크 객체와 상태 코드를 인스턴스화 된 핸들러에 전달합니다.</br>

#### 태스크 객체에 데이터 저장

Photo 태스크는 ImageView와 Bitmap을 가지고 있지만 UI 스레드가 아니기 때문에 UI객체에 접근할 수 없다.

``` java
// A class that decodes photo files into Bitmaps
class PhotoDecodeRunnable implements Runnable {
    ...
    PhotoDecodeRunnable(PhotoTask downloadTask) {
        mPhotoTask = downloadTask;
    }
    ...
    // Gets the downloaded byte array
    byte[] imageBuffer = mPhotoTask.getByteBuffer();
    ...
    // Runs the code for this task
    public void run() {
        ...
        // Tries to decode the image buffer
        returnBitmap = BitmapFactory.decodeByteArray(
                imageBuffer,
                0,
                imageBuffer.length,
                bitmapOptions
        );
        ...
        // Sets the ImageView Bitmap
        mPhotoTask.setImage(returnBitmap);
        // Reports a status of "completed"
        mPhotoTask.handleDecodeState(DECODE_STATE_COMPLETED);
        ...
    }
    ...
}
...
```

객체로 상태 전달하기 위해 handleState 객체에 데이터 저장

``` java
public class PhotoTask {
    ...
    // Gets a handle to the object that creates the thread pools
    sPhotoManager = PhotoManager.getInstance();
    ...
    public void handleDecodeState(int state) {
        int outState;
        // Converts the decode state to the overall state.
        switch(state) {
            case PhotoDecodeRunnable.DECODE_STATE_COMPLETED:
                outState = PhotoManager.TASK_COMPLETE;
                break;
            ...
        }
        ...
        // Calls the generalized state method
        handleState(outState);
    }
    ...
    // Passes the state to PhotoManager
    void handleState(int state) {
        /*
         * Passes a handle to this task and the
         * current state to the class that created
         * the thread pools
         */
        sPhotoManager.handleState(this, state);
    }
    ...
}
```

데이터를 UI 스레드로 전달합니다.

``` java
public class PhotoManager {
    ...
    // Handle status messages from tasks
    public void handleState(PhotoTask photoTask, int state) {
        switch (state) {
            ...
            // The task finished downloading and decoding the image
            case TASK_COMPLETE:
                /*
                 * Creates a message for the Handler
                 * with the state and the task object
                 */
                Message completeMessage =
                        mHandler.obtainMessage(state, photoTask);
                completeMessage.sendToTarget();
                break;
            ...
        }
        ...
    }
```

``` java
 private PhotoManager() {
        ...
            mHandler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message inputMessage) {
                    // Gets the task from the incoming Message object.
                    PhotoTask photoTask = (PhotoTask) inputMessage.obj;
                    // Gets the ImageView for this task
                    PhotoView localView = photoTask.getPhotoView();
                    ...
                    switch (inputMessage.what) {
                        ...
                        // The decoding is done
                        case TASK_COMPLETE:
                            /*
                             * Moves the Bitmap from the task
                             * to the View
                             */
                            localView.setImageBitmap(photoTask.getImage());
                            break;
                        ...
                        default:
                            /*
                             * Pass along other messages from the UI
                             */
                            super.handleMessage(inputMessage);
                    }
                    ...
                }
                ...
            }
            ...
    }
...
}
```

----

> ### 앱의 응답성 유지

앱을 사용하는 동안 상당한 시간동안 멈춰있거나, 또는 입력된 값을 처리하는데 오랜 시간이 걸릴 때가 있습니다.</br>
이 때, 가장 좋지 않은 응답은 “애플리케이션 응답 없음(ANR)” 대화상자가 나타나는 것 입니다.</br>
안드로이드는 일정 시간동안 응답하지 않는 응용프로그램에 대해서 ANR 대화상자를 표시합니다.</br>
이를 통해 사용자에게 앱을 종료할수 있게 하지만, 좋은 응답성을 가진 프로그램을 설계할 때는 ANR 대화상자를 표시해선 안됩니다.</br>

#### ANR을 유발하는 요인

응용프로그램이 UI스레드에서 일부 I/O 작업을 차단하거나, 정교한 메모리 내장구조로 인해 계산에 많은 시간이 들 수 있습니다.</br>
이처럼 앱이 잠재적으로 오랜 시간이 걸리는 작업을 수행할 때 UI 스레드에서의 작업이 아닌 다른 작업 스레드를 통해 대부분의 작업을 수행해야 합니다.</br>
그렇게 하면 메인 스레드인 UI 스레드가 지연없이 계속 실행 되고 시스템에서 응답이 없다고 판단하지 않아 응답성을 향상 시킬 수 있습니다.</br>
안드로이드에서 애플리케이션 응답은 Activity Manager 및 Window Manager 시스템 서비스에 의해 모니터링 되며, 다음 조건 중 하나를 감지하면 특정 응용프로그램에 대한 ANR 대화상자를 표시합니다.
- 5초 이내에 입력 이벤트에 대한 응답 없음(키 또는 화면 터치 이벤트).
- BroadcastReceiver가 10초 이내에 실행 완료 되지 않았을 때.

#### ANR을 피할수 있는 방법

안드로이드 응용프로그램은 일반적으로 하나의 스레드. 즉, UI스레드 또는 메인 스레드에서 실행됩니다.</br>
이것이 의미하는 내용은 UI스레드에서의 긴 작업은 이벤트 또는 인텐트 브로드캐스트를 처리를 한번에 할 수 없기 때문에 ANR 대화상자를 유발 할 수 있다는 점 입니다.</br>
따라서 UI 스레드에서는 가능한 한 작은 작업을 수행해야 하고, 네트워크나 데이터베이스 연산같은 장기 실행 연산 또는 비트맵 크기 조정과 같은 많은 계산 비용이 드는 작업은 onCreate() 나 onResume()과 같은 생명주기에 다른 작업 스레드에서 실행하는 것이 좋습니다.</br>
만약 더 긴 작업을 처리하기 위해 다른 작업자 스레드를 만들 때 가장 효과적인 방법은 AsyncTask 클래스를 사용하는 것이 좋습니다.</br>
AsyncTask 클래스를 상속받아서 doInBacground() 메서드를 구현하기만 하면 백그라운드에서 실행이 되게 됩니다.</br>
사용자에게 진행 상황을 알려주고 싶다면 onProgressUpdate()를 콜백 메서드로 갖는 publishProgress() 메서드를 호출하면 됩니다.</br>
onProgressUpdate() 메서드를 UI 스레드에서 사용함으로써 사용자에게 알려줄 수 있습니다.</br>

``` java
private class DownloadFilesTask extends AsyncTask<URL, Integer, Long> {
    // Do the long-running work in here
    protected Long doInBackground(URL... urls) {
        int count = urls.length;
        long totalSize = 0;
        for (int i = 0; i < count; i++) {
            totalSize += Downloader.downloadFile(urls[i]);
            publishProgress((int) ((i / (float) count) * 100));
            // Escape early if cancel() is called
            if (isCancelled()) break;
        }
        return totalSize;
    }

    // This is called each time you call publishProgress()
    protected void onProgressUpdate(Integer... progress) {
        setProgressPercent(progress[0]);
    }

    // This is called when doInBackground() is finished
    protected void onPostExecute(Long result) {
        showNotification("Downloaded " + result + " bytes");
    }
}
```

새 인스턴스를 호출하여 이 작업 스레드를 실행할 수 있습니다.

``` java
new DownloadFilesTask().execute(url1, url2, url3);
```

AsyncTask보다 복잡하지만, 새 스레드를 만들거나 핸들러 스레드를 만드는 방법도 있습니다.</br>
만약 그렇게 된다면  Process.setThreadPriority() 메서드의  THREAD_PRIORITY_BACKGROUND를 통해 스레드의 백그라운드 우선순위를 정해주어야합니다.</br>
하지만, 만약 우선순위를 낮게 설정하지 않는다면 기본적으로 UI스레드와 동일한 우선순위를 갖기 때문에 앱의 성능이 저하될 수 있습니다.</br>

만약 Thread 또는 HandlerThread를 사용할 때, 작업자 스레드에서 작업이 완료되기 전에는 UI 스레드에서 Thread.wait() 또는 Thread.sleep()을 호출하면 안됩니다.</br>
작업자 스레드가 완료되는것을 기다리는 대신, 메인 스레드의 핸들러를 이용하여 작업자 스레드의 완료상태를 받는다면 Input timeout으로 인한 ANR 대화상자가 나타나지 않습니다.</br>

BroadcastReceiver에서도 오랜 작업을 피해야 하며, 만약 오랜 작업을 해야한다면 IntentService를 호출하여 사용하는것이 적절합니다.

또한, BroadcastReceiver를 자주 실행하는 것은 다른 앱이 사용해야 할 메모리를 대신 쓰는 것 이기 때문에 적절하지 않습니다.
- StrictMode를 사용함으로써 긴 시간이 걸리는 작업(네트워크 또는 데이터베이스 작업)에 대응 할 수 있습니다.

#### 반응성 강화

일반적으로 100~200ms는 사용자가 앱의 느려짐을 감지하는 임계값입니다.
- 앱에서 사용자 입력에 대한 응답으로 백그라운드에서 작업을 하게 된다면, ProgressBar와 같은 UI를 사용하여 사용자에게 진행상황을 보여줍니다.
- 특히 게임같은 경우는 이동에 대한 계산을 작업자 스레드에서 해야 합니다.
- 앱을 초기 실행할때 시간이 많이 소요된다면 스플래시 화면을 보여주거나 진행상황에 대한 정보를 나타내야 합니다.
- Systrace와 Traceview와 같은 도구를 사용하여 앱의 병목현상을 파악합니다.

----

> ### Android JNI

JNI는 Java Native Interface로 안드로이드 프레임워크에서 자바와 C/C++ 모듈 간의 인터페이스르 가능하게 해줍니다.

- 안드로이드 SDK를 토대로 만든 안드로이드 애플리케이션은 달빅 가상 머신(Dalvik Virtual Machine) 위에서 동작하는 자바 기반의 프로그램입니다.
- 때문에 C/C++로 생성한 애플리케이션에 비해 느린 실행 속도 등 자바가 지닌 여러 한계를 그대로 가지고 있습니다.
- 그래픽 처리나 시그널 프로세싱처럼 CPU의 처리 속도가 중요한 부분에서는 자바보다 C/C++ 같은 네이티브 코드로 작성한 모듈이 훨씬 더 나은 성능을 낼 것입니다.

#### 활용
빠른 처리 속도를 요구하는 루틴 작성
- 대개 자바는 네티이브 코드(C/C++등)에 비해 느립니다.
- 따라서 빠른 처리 속도를 요하는 부분은 C/C++로 작성하고 이를 JNI를 통해 자바에서 호출하는 방법으로 속도를 향상시킬 수 있습니다.

하드웨어 제어
- 하드웨어 제어 코드를 C로 작성한 다음 JNI를 통해 자바 레이어와 연결하면 자바에서도 하드웨어 제어가 가능합니다.

기존 C/C++ 프로그램의 재사용
- 이미 기존 코드가 대부분 C/C++로 작성돼 있다면 굳이 자바로 동일한 코드를 다시 작성하기보다는 JNI를 통해 기존 코드를 활용할 수 있습니다.

#### 자바에서 C 라이브러리 함수 호출
1. 자바 코드 작성
- 자바 클래스에 네이티브 메서드 선언
- System.loadLibrary() 메서드를 호출해서 C 라이브러리 로딩

2. 자바 코드 컴파일

3. C 헤더 파일 생성
- 자바 가상 머신에서 함수 매핑 테이블이 필요
- javah라는 툴을 이용하여 자바 네이티브 메서드와 연결될 수 있는 C 함수의 원형 생성(JNI 네이티브 함수 원형이 포함된 헤더 파일을 생성)

4. C 코드 작성
- JNI 네이티브 함수 구현

5. C 공유 라이브러리 생성
- C 공유 라이브러리 빌드

6. 자바 프로그램 실행
- JNI를 통한 네이티브 함수 호출


#### C 프로그램에서 자바 클래스 실행
C/C++에서 Java의 클래스를 이용하기 위해서는 Reflection 기술을 이용해야 합니다.
- Reflection? 구체적인 클래스 타입을 알지 못해도, 그 클래스의 메소드, 타입, 변수들을 접근할 수 있게 도와주는 자바 API

1. 자바 가상 머신에 전달할 옵션값을 생성
2. 자바 가상 머신 생성
3. 실행할 클래스 검색 후 로드
4. 해당 메서드 ID 획득
5. 클래스 메서드의 인자로 넘겨줄 객체 생성
6. 메서드 호출
7. 자바 가상 머신 소멸

#### JNI 사용 팁

JNI 레이어에서 리소스 마샬링을 최소화해야 합니다.
- 마샬링은 한 객체의 메모리에서의 표현방식을 저장 또는 전송에 적합한 다른 데이터 형식으로 변환하는 과정
- JNI레이어를 마샬링은 번번히 일어나지만, 마샬링 해야하는 데이터의 양과 빈도를 최소화하게 설계해야 합니다.

가능한한 Java코드와 C++로 작성된 코드간의 비동기 통신을 피해야 합니다.
- 일반적으로 UI와 동일한 언어로 비동기 업데이트를 진행하여 비동기 UI 업데이트를 단순화 할 수 있습니다. 
- 예를 들어 JNI를 통해 Java 코드의 UI 스레드에서 C ++ 함수를 호출하는 대신 Java 프로그래밍 언어의 두 스레드 사이에서 콜백을 수행하는 것이 좋습니다.

JNI가 접근하는 thread 수를 최소화 합니다.
- Java 및 C ++ 언어 모두에서 스레드 풀을 사용해야하는 경우 개별 작업자 스레드가 아닌 스레드 풀에서 JNI 통신을 유지하십시오.

적절한 JNI 자동 생성 라이브러리를 사용합니다.

</br>

---
#### 원문 출처:

 * [Best Practices for Performance](https://developer.android.com/training/best-performance.html) for Android Developers

</br>

---
