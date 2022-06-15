package com.tp.youngreview;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ReviewActivity extends AppCompatActivity {

    private String review_NAME;
    private String USER_NAME;

    private ListView review_view;
    private EditText review_edit;
    private Button review_send;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        // 위젯 ID 참조
        review_view = (ListView) findViewById(R.id.review_view);
        review_edit = (EditText) findViewById(R.id.review_edit);
        review_send = (Button) findViewById(R.id.review_sent);

        // 로그인 화면에서 받아온 채팅방 이름, 유저 이름 저장
        Intent intent = getIntent();
        review_NAME = intent.getStringExtra("reviewName");
        USER_NAME = intent.getStringExtra("userName");

        // 채팅 방 입장
        openreview(review_NAME);

        // 메시지 전송 버튼에 대한 클릭 리스너 지정
        review_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (review_edit.getText().toString().equals(""))
                    return;

                reviewDTO review = new reviewDTO(USER_NAME, review_edit.getText().toString()); //reviewDTO를 이용하여 데이터를 묶는다.
                databaseReference.child("review").child(review_NAME).push().setValue(review); // 데이터 푸쉬
                review_edit.setText(""); //입력창 초기화

            }
        });
    }

    private void addMessage(DataSnapshot dataSnapshot, ArrayAdapter<String> adapter) {
        reviewDTO reviewDTO = dataSnapshot.getValue(reviewDTO.class);
        adapter.add(reviewDTO.getUserName() + " : " + reviewDTO.getMessage());
    }

    private void removeMessage(DataSnapshot dataSnapshot, ArrayAdapter<String> adapter) {
        reviewDTO reviewDTO = dataSnapshot.getValue(reviewDTO.class);
        adapter.remove(reviewDTO.getUserName() + " : " + reviewDTO.getMessage());
    }

    private void openreview(String reviewName) {
        // 리스트 어댑터 생성 및 세팅
        final ArrayAdapter<String> adapter

                = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
        review_view.setAdapter(adapter);

        // 데이터 받아오기 및 어댑터 데이터 추가 및 삭제 등..리스너 관리
        databaseReference.child("review").child(reviewName).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                addMessage(dataSnapshot, adapter);
                Log.e("LOG", "s:"+s);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                removeMessage(dataSnapshot, adapter);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}