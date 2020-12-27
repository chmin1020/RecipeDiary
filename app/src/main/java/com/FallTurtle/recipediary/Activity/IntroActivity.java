package com.FallTurtle.recipediary.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;

import com.FallTurtle.recipediary.R;

public class IntroActivity extends AppCompatActivity {

    private class CheckTypesTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                Thread.sleep(2000); // 2초 지속
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            finish();
            super.onPostExecute(result);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        CheckTypesTask task = new CheckTypesTask();
        task.execute();
    }

    public void OnBackPressed(){ } //인트로 액티비티에서는 아무것도 안 한다.
}
