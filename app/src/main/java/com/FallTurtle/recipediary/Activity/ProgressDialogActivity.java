package com.FallTurtle.recipediary.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import com.FallTurtle.recipediary.databinding.ActivityProgressDialogBinding;

public class ProgressDialogActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        ActivityProgressDialogBinding binding = ActivityProgressDialogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Drive code
        CheckTypesTask task = new CheckTypesTask();
        task.execute();
    }

    private class CheckTypesTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog asyncDialog = new ProgressDialog(
                ProgressDialogActivity.this);

        @Override
        protected void onPreExecute() {
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage("로딩중입니다..");

            // show dialog
            asyncDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                Thread.sleep(1000); // 1초 지속

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            asyncDialog.dismiss();

            finish();
            super.onPostExecute(result);
        }
    }
}