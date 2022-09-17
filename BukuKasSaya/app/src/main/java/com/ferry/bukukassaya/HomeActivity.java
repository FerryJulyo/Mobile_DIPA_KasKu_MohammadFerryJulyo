package com.ferry.bukukassaya;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {
    ImageButton pengeluaran, pemasukan, cashFlow, pengaturan;
    private DBHelper DB;
    TextView masuk,keluar;
    LineChart chart;
    LineDataSet lineDataSet = new LineDataSet(null,null), lineDataSet2 = new LineDataSet(null,null);
    ArrayList<ILineDataSet> dataSets = new ArrayList<>(), dataSets2 = new ArrayList<>();
    LineData lineData, lineData2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        chart       =   (LineChart) findViewById(R.id.cfChart);
        pengeluaran =   (ImageButton) findViewById(R.id.uangKeluar);
        pemasukan   =   (ImageButton) findViewById(R.id.uangMasuk);
        cashFlow    =   (ImageButton) findViewById(R.id.cashFlow);
        pengaturan  =   (ImageButton) findViewById(R.id.pengaturan);
        masuk       =   (TextView) findViewById(R.id.pemasukan);
        keluar      =   (TextView) findViewById(R.id.pengeluaran);
        DB              = new DBHelper(this);

        lineDataSet.setValues(getDataChartMasuk());
        lineDataSet.setColor(Color.GREEN);
        lineDataSet.setLabel("Pemasukan");
        dataSets.clear();
        dataSets.add(lineDataSet);

        lineDataSet2.setValues(getDataChartKeluar());
        lineDataSet2.setLabel("Pengeluaran");
        lineDataSet2.setColor(Color.RED);
        dataSets.add(lineDataSet2);
        lineData = new LineData(dataSets);

        chart.clear();
        chart.setData(lineData);
        chart.invalidate();

        pemasukan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),PemasukanActivity.class);
                startActivity(intent);
            }
        });

        pengeluaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),PengeluaranActivity.class);
                startActivity(intent);
            }
        });

        cashFlow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),CashFlowActivity.class);
                startActivity(intent);
            }
        });

        pengaturan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),SettingActivity.class);
                startActivity(intent);
            }
        });
        showTotalKeluar();
        showTotalMasuk();

        masuk.addTextChangedListener(new TextWatcher() {
            private  String setTextMasuk;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().equals(setTextMasuk)){
                    masuk.removeTextChangedListener(this);
                    String replace = charSequence.toString().replaceAll("[Rp ]","");
                    if (!replace.isEmpty()){
                        setTextMasuk = formatRupiah(Double.parseDouble(replace));
                    }else {
                        masuk.setText(setTextMasuk);
                        masuk.addTextChangedListener(this);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private  String formatRupiah(Double number){
        Locale localeID = new Locale("IND", "ID");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(localeID);
        String formatRupiah = numberFormat.format(number);
        String[] split = formatRupiah.split(",");
        int length = split[0].length();
        return split[0].substring(0,2)+". "+split[0].substring(2,length);
    }

    private void showTotalKeluar(){
        Cursor cursor = DB.showTotalUangKeluar();
        StringBuilder stringBuilder = new  StringBuilder();

        while (cursor.moveToNext()){
            stringBuilder.append("Pengeluaran : Rp ").append(cursor.getInt(0));
        }
        keluar.setText(stringBuilder);
    }

    private void showTotalMasuk(){
        Cursor cursor = DB.showTotalUangMasuk();
        StringBuilder stringBuilder = new  StringBuilder();

        while (cursor.moveToNext()){
            stringBuilder.append("Pemasukan : Rp ").append(cursor.getInt(0));
        }
        masuk.setText(stringBuilder);
    }

    private ArrayList<Entry> getDataChartMasuk(){
        ArrayList<Entry> dataChart = new ArrayList<>();
        @SuppressLint("Recycle")
        Cursor cursor = DB.getWritableDatabase().rawQuery("SELECT strftime('%d', tanggal) as dataBulanan, nominal FROM cashFLow where strftime('%m',tanggal) = strftime('%m',date('now')) AND strftime('%Y',tanggal) = strftime('%Y',date('now')) AND tipe = 'masuk' ORDER BY dataBulanan ASC", null);

        for (int i=0; i<cursor.getCount();i++){
            cursor.moveToNext();
            dataChart.add(new Entry(cursor.getFloat(0), cursor.getFloat(1)));
        }return dataChart;
    }

    private ArrayList<Entry> getDataChartKeluar(){
        ArrayList<Entry> dataChart = new ArrayList<>();
        @SuppressLint("Recycle")
        Cursor cursor = DB.getWritableDatabase().rawQuery("SELECT strftime('%d', tanggal) as dataBulanan, nominal FROM cashFLow where strftime('%m',tanggal) = strftime('%m',date('now')) AND strftime('%Y',tanggal) = strftime('%Y',date('now')) AND tipe = 'keluar' ORDER BY dataBulanan ASC", null);

        for (int i=0; i<cursor.getCount();i++){
            cursor.moveToNext();
            dataChart.add(new Entry(cursor.getFloat(0), cursor.getInt(1)));
        }return dataChart;
    }
}