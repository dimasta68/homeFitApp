package com.hardcoding.homework;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;

public class Report extends AppCompatActivity {
    CheckBox checkBoxreport;
    Button btnReport;
    Map<Integer, Integer> hashMap = new HashMap<Integer, Integer>();
    int pos;
    int complite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        Paper.init(getApplicationContext());
        if (Paper.book().read("complite") != null) {
            complite = Paper.book().read("complite");
        }
        if(Paper.book().read("hashMap")!=null){
            hashMap= Paper.book().read("hashMap");
        }
        checkBoxreport = (CheckBox) findViewById(R.id.checkBoxReport);
        btnReport = (Button) findViewById(R.id.btnReport);
        Intent intent = getIntent();
        pos=intent.getIntExtra("id", 0);
        Log.d("debug","report position id"+pos);
        String title = intent.getStringExtra("title");
        checkBoxreport.setText(title);
        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBoxreport.isChecked()) {
                    complite++;
                    Paper.book().write("complite", complite);
                    hashMap.put(pos, complite);
                    Paper.book().write("hashMap", hashMap);
                    Log.d("debug","report"+hashMap.get(pos));
                    Toast.makeText(Report.this, "success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Report.this, "отметьте выполненное задание ", Toast.LENGTH_SHORT).show();
                }
                if (hashMap != null) {
                    for (int j = 0; j < hashMap.size(); j++) {
                        hashMap.get(j);
                        Log.d("debug","hashMap Print"+hashMap.get(pos)+ " positions ==="+pos);
                    }}

                for (Integer key : hashMap.keySet()) {
                    Log.d("debug","key"+key);
                }
            }
        });
    }
}