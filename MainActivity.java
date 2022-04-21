package com.example.TOPUPer;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {
    private Button startStopButton, closeButton;
    private TextView statusTextView;
    private boolean serviceRunning = false;
    private AsyncLoader asyncLoader = new AsyncLoader();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        addListenerOnButton();
    }

    @Override
    public void onBackPressed() {
        if (closeButton.isEnabled())
            mainClose();
        else
            moveTaskToBack(true);
    }

    private void addListenerOnButton() {
//        LogSaver.deleteAllFile(); //todo
        new Get();
        new Checker(getApplicationContext());
        SMS.activity = this;

        startStopButton = (Button) findViewById(R.id.buttonStartStop);
        statusTextView = (TextView) findViewById(R.id.textView);
        closeButton = (Button) findViewById(R.id.buttonClose);

        startStopButton.setText(Get.button_start);
        closeButton.setText(Get.button_close);
        setTitle(Get.title);


        startStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (startStopButton.getText().equals(Get.button_start))
                    mainStart();
                else if (startStopButton.getText().equals(Get.button_stop))
                    mainStop();
            }
        });

        closeButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mainClose();
                return false;
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), Get.button_close_toast, Toast.LENGTH_LONG).show();
            }
        });

    }

    private void mainStart() {
        startStopButton.setText(Get.button_stop);
        if (!serviceRunning) {
            if (Loader.initialized == null)
                Loader.initialized = true;

            if (checkPreRequisite()) {
                statusTextView.setText(Get.service_started);
                closeButton.setEnabled(false);
                asyncLoader = new AsyncLoader();
                Loader.runLoader = true;
                asyncLoader.execute();
                serviceRunning = true;
            } else {
                startStopButton.setText(Get.button_start);
            }

        } else
            statusTextView.setText(Get.service_already_running + statusTextView.getText());


    }

    private void mainStop() {
        if (serviceRunning) {
            serviceRunning = false;
            Loader.runLoader = false;
            addToStatusTextView(Get.trying_to_stop);
            startStopButton.setEnabled(false);
            if (!(Loader.loaderThread == null))
                Loader.loaderThread.interrupt();
        } else
            statusTextView.setText(Get.no_running_service);

        startStopButton.setText(Get.button_start);
    }

    private void mainClose() {
        this.finish();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);

    }

    private void addToStatusTextView(String record) {

        statusTextView.setText(Checker.time() + " " + record + "\n" + statusTextView.getText());
        if (statusTextView.getText().length() > 1000)
            statusTextView.setText(statusTextView.getText().toString().substring(0, 500));

        if (record.contains(Get.word_in_service_stopped)) {
            startStopButton.setEnabled(true);
            closeButton.setEnabled(true);
        }
    }

    private boolean checkPreRequisite() {
        if (!Checker.internetWorking() && Checker.simWorking()) {
            addToStatusTextView(Get.internet_off_error);
            return false;
        }
        if (Checker.internetWorking() && !Checker.simWorking()) {
            addToStatusTextView(Get.no_sim_error);
            return false;
        }
        if (!Checker.internetWorking() && !Checker.simWorking()) {
            addToStatusTextView(Get.internet_sim_error);
            return false;
        }

        return true;

    }


    public class AsyncLoader extends AsyncTask<Void, String, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            if (Checker.statusOK()) {
                while (serviceRunning) {
                    Loader.runService(this);
                }
                doProgress(Get.service_stopped);
                return null;
            } else {
                finish();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            addToStatusTextView(values[0]);
        }

        public void doProgress(String record) {
            publishProgress(record);
        }

    }

}
