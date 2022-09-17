package com.ferry.bukukassaya;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;


public class CashFlowActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    Button kembali;
    ArrayList<String> tipe, nominal, tanggal, keterangan;
    private DBHelper DB;
    Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_flow);
        kembali         = (Button)        findViewById(R.id.btnKembali);
        recyclerView    = (RecyclerView) findViewById(R.id.recyclerViewCashFlow);
        DB              = new DBHelper(this);
        tipe            = new ArrayList<>();
        nominal         = new ArrayList<>();
        tanggal         = new ArrayList<>();
        keterangan      = new ArrayList<>();
        recyclerView    = findViewById(R.id.recyclerViewCashFlow);
        adapter         = new Adapter(this, tipe, nominal, keterangan, tanggal);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getCashFlowData();
    }

    @SuppressLint("StaticFieldLeak")
    private void getCashFlowData() {
        Cursor cursor = DB.showCashFlow();
        if (cursor.getCount()==0){
            Toast.makeText(this, "Tidak ada data", Toast.LENGTH_SHORT).show();
        }else{
            while (cursor.moveToNext()){
                tanggal.add(cursor.getString(1));
                tipe.add(cursor.getString(2));
                nominal.add(cursor.getString(3));
                keterangan.add(cursor.getString(4));

            }
        }

        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}