package com.ligenmt.festivalmessage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ligenmt.festivalmessage.dao.FestivalDao;

/**
 * 新增祝福语界面
 */
public class AddMessageActivity extends AppCompatActivity implements View.OnClickListener{
    
    private EditText etContent;
    private Button btnSave;
    private Button btnClear;
    private Button btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_message);
        etContent = (EditText) findViewById(R.id.et_content);
        btnSave = (Button) findViewById(R.id.btn_save);
        btnClear = (Button) findViewById(R.id.btn_clear);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnSave.setOnClickListener(this);
        btnClear.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_save:
                if(etContent.getText().toString().trim() == "") {
                    Toast.makeText(this,"你还啥都没写呢", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent data = new Intent();
                data.putExtra("content", etContent.getText().toString());
                setResult(RESULT_OK, data);
                finish();
                break;
            case R.id.btn_clear:
                etContent.setText("");
                break;
            case R.id.btn_cancel:
                setResult(RESULT_CANCELED);
                finish();
                break;
        }
    }
}
