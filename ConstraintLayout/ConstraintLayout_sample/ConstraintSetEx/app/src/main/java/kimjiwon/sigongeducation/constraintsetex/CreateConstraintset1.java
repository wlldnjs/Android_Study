package kimjiwon.sigongeducation.constraintsetex;

import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class CreateConstraintset1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_constraintset1);

//        // ConstraintSet 객체 생성.
//        ConstraintSet constraintSet1 = new ConstraintSet();
//        // connect 메서드의 매개변수에 값 입력(1. 제약조건 설정할 View의 id, 2. 제약조건을 설정할 방향, 3. target View의 id, 4. 제약조건을 설정할 방향, 5. margin 값(생략 가능).
//        constraintSet1.connect(R.id.textview1,ConstraintSet.START,ConstraintSet.PARENT_ID,ConstraintSet.START,6);
//
//
//        // ConstraintSet 객체 생성.
//        ConstraintSet constraintSet2 = new ConstraintSet();
//        // clone 메서드의 매개변수에 Context와 복제할 Constraint의 Layout입력.
//        constraintSet2.clone(this, R.layout.activity_create_constraintset1);
//
//
//        // ConstraintLayout 변수 생성 후 Layout에 있는 ConstraintLayout의 id와 연결.
//        ConstraintLayout constraintLayout = findViewById(R.id.constraint);
//        // ConstraintSet 객체 생성.
//        ConstraintSet constraintSet3 = new ConstraintSet();
//        // clone 메서드의 매개변수에 복제할 ConstraintLayout 입력
//        constraintSet3.clone(constraintLayout);
//
//        // ConstraintSet의 특정 id의 모든 제약조건을 제거한다.
//        // clear 메서드의 매개변수에 제약조건을 제거할 View의 id를 입력.
//        constraintSet1.clear(R.id.menuBar);
//        // ConstraintSet의 특정 id의 특정 제약조건을 제거한다.
//        // clear 메서드의 매개변수에 제약조건을 제거할 View의 id와 제거할 제약조건을 입력.
//        constraintSet1.clear(R.id.menuBar,ConstraintSet.RIGHT);
//
//        // ConstraintSet을 적용할 ConstraintLayout 생성.
//        ConstraintLayout rootLayout = findViewById(R.id.constraint);
//        // 새로운 ConstraintSet을 생성.
//        ConstraintSet constraintSet4 = new ConstraintSet();
//        // ConstraintSet에 새로운 제약조건을 적용
//        constraintSet4.clone(constraintSet3);
//        // rootLayout에 새로 지정한 constraintSet4의 제약조건을 적용
//        constraintSet4.applyTo(rootLayout);
    }
}
