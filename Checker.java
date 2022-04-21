package com.example.TOPUPer;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.format.Time;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Shiva Sunar on 11/26/2015.
 */
public class Checker {
    public static Boolean status;
    private static Context loaderContext;

    public Checker(){
    }

    public Checker(Context context) {
        loaderContext=context;
    }

    public static boolean statusOK() {
        
        // removed code
        //**********
        //status = cond1 && cond2;

        return status;
    }

    public static boolean internetWorking() {
        return new Checker().internet();
    }

    public static boolean simWorking() {
        return new Checker().sim();
    }

    public static String timeStamp(){
        Time now = new Time();
        now.setToNow();
        return now.format("%Y%m%d%H%M%S");
    }
    public static String readableTimeStamp(){
        Time now = new Time();
        now.setToNow();
        return now.format("%Y/%m/%d-%H:%M:%S");
    }
    public static String time(){
        Time now = new Time();
        now.setToNow();
        return now.format("%H:%M:%S");
    }
    public static String date(){
        Time now = new Time();
        now.setToNow();
        return now.format("%Y%m%d");
    }

    public static boolean timeStampExpired(String t){
        return Long.valueOf(Checker.timeStamp())>(Long.valueOf(t)+60*60);
    }

    private boolean internet() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                loaderContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();

    }

    private boolean sim() {
        TelephonyManager telephonyManager = (TelephonyManager) loaderContext.getSystemService(Context.TELEPHONY_SERVICE);
        boolean simReady = (telephonyManager.getSimState() == TelephonyManager.SIM_STATE_READY);
        return simReady;
    }
}


class Get {
    public static boolean isEnglish;
    public static String
            title,
            button_start,
            button_stop,
            button_close,
            button_close_toast,
            service_started,
            internet_sim_error,
            internet_off_error,
            no_sim_error,
            internet_down_temporarily,
            service_already_running,
            trying_to_stop,
            no_running_service,
            service_stopped,
            word_in_service_stopped,
            no_new_number,
            sleeping,
            sleeping_exception,
            logged_in,
            login_failed,
            data_fetched,
            data_fetching_failed,
            successfully_tried_to_send_sms,
            trying_sms_sending_failed,
            word_in_successfully_tried_sms_message,
            previously_failed_failed,
            previously_failed_success,
            money_sent_no_websuccess,
            long_bar,
            sky_utl_submitted;

    public Get() {
        languageEnglish();
    }

    public static void languageNepali() {
        isEnglish = false;
        title = "टपपर \tप्रोग्रामर : शिव सुनार |";// "TOPUPer By Shiva Sunar.";
        button_start = "सेवा शुरु गर्नुहोस |";//"Start";
        button_stop = "सेवा रोक्नुहोस |";// "Stop";
        button_close = "टपपर बन्द गर्नुहोस |";// "Close";
        button_close_toast = "टपपर बन्द गर्न यो बटन लामो समयसम्म दबाउनुहोस |\nया टपपर ब्याकगराउन्डमा चलाउन ब्याक की दबाउनुहोस|";// "Long Press CLOSE to Close.\nPress BACK Key to Run in Background.";
        service_started = "सेवा शुरु भएको छ |";// "Service Started";

        internet_sim_error = "तपाईंको इन्टरनेट वा सिमले काम गरिरहेको छैन |\nकृपया चेक गरी पुनः प्रयास गर्नुहोस |";// "Your Internet or SIM-Card is Not Available.\n" +"Please Check And Try Again!!!";
        internet_off_error = "तपाईंको वाई-फाई र मोबाइल डाटा अफ गरिएको छ | कृपया अन गरी पुनः प्रयास गर्नुहोस |";//Your Wi-fi and Mobile-Data are Off.\n"Please On Them & Try Again.";
        no_sim_error = "तपाईंको मोबाइलमा सिमकार्ड छैन | सिमकार्ड राखि पुनः प्रयास गर्नुहोस |";//Your SIM-Card is Not Ready.\nPlease Check & Try Again.";
        internet_down_temporarily = "इन्टरनेटमा केही समयदेखि समस्या आएको छ | इन्टरनेटमा जोडिन प्रयास हुँदैछ |";//"Your Internet is Down Temporarily.\nTrying to Connect Again.";

        service_already_running = "सेवा पहिलेबाटै सुचारु छ |";// "Service is Already Running.";
        trying_to_stop = "सेवा रोक्न प्रयास हुँदैछ |";// "Trying to Stop Service.";
        no_running_service = "बन्द गरिनुपर्ने कुनै काम सुचारु छैन |";// "No Running Service to Stop.";
        service_stopped = "सेवा बन्द गरिएको छ |";// "Service Stopped!!!";
        word_in_service_stopped = "सेवा बन्द";// "Service Stopped";
        no_new_number = "रिचार्ज गर्नुपर्ने नयाँ नम्बर आएको छैन |";// "No New Numbers to Recharge.";
        sleeping = "१ मिनेटको लागि स्लिप मोडमा राखिएको छ |";// "Sleeping for a Minute.";
        sleeping_exception = "स्लिप मोडमा केहि समस्या आएको छ |";// "Some Exception while sleeping.";
        logged_in = "लगिन भएको छ |";// "Logged In.";
        login_failed = "लगिनमा समस्या आएको छ |";// "Login Failed!!!";
        data_fetched = "डाटा तानिएको छ |";// "Data Fetched.";
        data_fetching_failed = "डाटा तान्नमा समस्या आएको छ |";// "Data Fetching Failed!!!";

        successfully_tried_to_send_sms = "एसएमएस पठाइएको छ |";// "SMS Sent.";
        trying_sms_sending_failed = "एसएमएस पठाउनमा समस्या आएको छ |";// "SMS Sending Failed.";
        word_in_successfully_tried_sms_message = "पठाइएको";// "Sent";
        previously_failed_failed = "पहिले पठाउन नसकिएको वेबरिक्वेस्ट फेरी पठाउन सकिएन |";// "Previously Failed Submitting Failed Again: ";
        previously_failed_success = "पहिले पठाउन नसकिएको वेबरिक्वेस्ट अहिले पठाइएको छ |";// "Successfully Submitted Previously Unsuccessful: ";
        money_sent_no_websuccess = "रिचार्ज गरियो तर वेबरिक्वेस्ट गएन |";// "Money Sent But No WebSuccess: ";

        long_bar = "============================================================================================";

    }

    public static void languageEnglish() {
        isEnglish = true;
        title = "TOPUPer By Shiva Sunar.";
        button_start = "Start";
        button_stop = "Stop";
        button_close = "Close";
        button_close_toast = "Long Press CLOSE to Close.\nPress BACK Key to Run in Background.";
        service_started = "Service Started";

        internet_sim_error = "Your Internet or SIM-Card is Not Available.\n" +
                "Please Check And Try Again!!!";
        internet_off_error = "Your Wi-fi and Mobile-Data are Off.\n" +
                "Please On Them & Try Again.";
        no_sim_error = "Your SIM-Card is Not Ready.\n" +
                "Please Check & Try Again.";
        internet_down_temporarily = "Your Internet is Down Temporarily.\nTrying to Connect Again.";

        service_already_running = "Service is Already Running.";
        trying_to_stop = "Trying to Stop Service.";
        no_running_service = "No Running Service to Stop.";
        service_stopped = "Service Stopped!!!";
        word_in_service_stopped = "Service Stopped";
        no_new_number = "No New Numbers to Recharge.";
        sleeping = "Sleeping for a Minute.";
        sleeping_exception = "Sleep was Interrupted!!!";
        logged_in = "Logged In.";
        login_failed = "Login Failed!!!";
        data_fetched = "Data Fetched.";
        data_fetching_failed = "Data Fetching Failed!!!";

        successfully_tried_to_send_sms = "SMS Sent.";
        trying_sms_sending_failed = "SMS Sending Failed.";
        word_in_successfully_tried_sms_message = "Sent";
        previously_failed_failed = "Previously Failed Submitting Failed Again: ";
        previously_failed_success = "Successfully Submitted Previously Unsuccessful: ";
        money_sent_no_websuccess = "Money Sent But No WebSuccess: ";
        sky_utl_submitted="Sky or UTL Websuccess Sent!!!\n" +
                "Send Recharge PIN Manually:";

        long_bar = "===================================================================";

    }
}

