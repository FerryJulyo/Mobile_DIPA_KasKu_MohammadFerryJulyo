package com.ferry.bukukassaya;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity {
    EditText inputPass, inputNewPass;
    Button simpan, kembali;
    DBHelper DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        kembali         = (Button)  findViewById(R.id.btnKembali);
        simpan          = (Button)  findViewById(R.id.btnSimpan);
        inputPass       = (EditText) findViewById(R.id.inputPass);
        inputNewPass    = (EditText) findViewById(R.id.inputNewPass);
        DB = new DBHelper(this);

        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentPass = inputPass.getText().toString();
                String newPass = inputNewPass.getText().toString();

                if (newPass.equals("") || currentPass.equals("")) {
                    Toast.makeText(SettingActivity.this, "Tidak boleh ada data yang kosong!!!", Toast.LENGTH_SHORT).show();
                }else{
                    Boolean checkPass = DB.checkPassword(currentPass);
                    if(checkPass){
                        Boolean changePass = DB.changePassword(newPass);
                        if (changePass) {
                            Toast.makeText(SettingActivity.this, "Password Berhasil Diubah", Toast.LENGTH_SHORT).show();
                            emptyInputEditText();
                        }
                    }else {
                        Toast.makeText(SettingActivity.this, "Gagal!!!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
    private void emptyInputEditText() {
        inputPass.setText(null);
        inputNewPass.setText(null);
    }
}