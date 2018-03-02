package com.andrody.stt;
import android.content.ActivityNotFoundException;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity
{

    protected static final int RESULT_SPEECH = 1;

    private EditText Text;
    private Button Copy, Share, Speak;
    String copy_content;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Text = (EditText) findViewById(R.id.txtText);
        Speak = (Button) findViewById(R.id.Speak);
        Copy = (Button) findViewById(R.id.copy);
        Share = (Button) findViewById(R.id.share);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        Speak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar-SA");
                try {
                    startActivityForResult(intent, RESULT_SPEECH);
                    Text.setText("");
                } catch (ActivityNotFoundException a) {
                    Toast t = Toast.makeText(getApplicationContext(), "Unfortunately, this device does not support talk",
                            Toast.LENGTH_SHORT);
                    t.show();
                }
            }
        });


        Copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copy_content = Text.getText().toString();
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                clipboard.setText(copy_content);
                Toast.makeText(getApplicationContext(), "Copies successfully :)", Toast.LENGTH_SHORT).show();
            }
        });



        Share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copy_content = Text.getText().toString();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, copy_content);
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "Select the application:"));
            }

        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case RESULT_SPEECH: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    Text.setText(text.get(0));
                }
                break;
            }

        }
    }
}
