package com.tp.youngreview;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewFragment extends Fragment {

    EditText user_review;
    EditText user_edit;
    Button user_next;
    ListView review_list;


    public ReviewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {    //기생충
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_review, container, false);

        user_review = rootView.findViewById(R.id.user_review);
        user_edit = rootView.findViewById(R.id.user_edit);
        user_next = rootView.findViewById(R.id.user_next);
        review_list = rootView.findViewById(R.id.review_list);


        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        user_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user_edit.getText().toString().equals("") || user_review.getText().toString().equals(""))
                    return;

                Intent intent = new Intent(getActivity(), ReviewActivity.class);
                intent.putExtra("reviewName", user_review.getText().toString());
                intent.putExtra("userName", user_edit.getText().toString());
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 101 && resultCode == getActivity().RESULT_OK){
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("sFile", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit(); //사용자가 입력한 데이터 저장
        //sharedPreferences.edit().clear().commit(); //초기화

        String str_name = user_review.getText().toString();
        String str_birth = user_edit.getText().toString();
        //String signature = BitampToString(paintBoard.mBitmap);


        editor.putString("name", str_name);
        editor.putString("birth", str_birth);
        //editor.putString("signature",signature);

        File signatureFile = new File(getActivity().getExternalFilesDir(null),"signature.png");

    }
}
