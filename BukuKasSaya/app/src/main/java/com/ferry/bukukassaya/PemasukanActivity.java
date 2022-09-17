package com.ferry.bukukassaya;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PemasukanActivity extends AppCompatActivity {

    final Calendar myCalendar= Calendar.getInstance();
    EditText tanggal, nominal, keterangan;
    ImageButton tanggalImg;
    Button  simpan, kembali;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pemasukan);
        tanggal         =   (EditText)      findViewById(R.id.tanggal);
        nominal         =   (EditText)      findViewById(R.id.nominal);
        keterangan      =   (EditText)      findViewById(R.id.keterangan);
        tanggalImg      =   (ImageButton)   findViewById(R.id.gmbrTanggal);
        simpan          =   (Button)        findViewById(R.id.btnSimpan);
        kembali         =   (Button)        findViewById(R.id.btnKembali);
        DB = new DBHelper(this);

        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };

        tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(PemasukanActivity.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        tanggalImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(PemasukanActivity.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputTipe = "masuk";
                String inputTanggal = tanggal.getText().toString();
                String inputNominal = nominal.getText().toString();
                String inputKet = keterangan.getText().toString();

                if (inputTanggal.equals("") || inputNominal.equals("")) {
                    Toast.makeText(PemasukanActivity.this, "Tanggal atau Nominal tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }else{
                    Boolean checkInsertData = DB.insertCashFlow(inputTipe, inputTanggal, inputNominal, inputKet);
                    if (checkInsertData) {
                        Toast.makeText(PemasukanActivity.this, "Data Berhasil Ditambah", Toast.LENGTH_SHORT).show();
                        emptyInputEditText();
                    } else {
                        Toast.makeText(PemasukanActivity.this, "Data Gagal Ditambah", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void updateLabel(){
        String myFormat="yyyy-MM-dd";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        tanggal.setText(dateFormat.format(myCalendar.getTime()));
    }

    private void emptyInputEditText() {
        nominal.setText(null);
        keterangan.setText(null);
    }

}