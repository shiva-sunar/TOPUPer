package com.example.TOPUPer;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.telephony.SmsManager;
import android.text.format.Time;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shiva Sunar on 12/3/2015.
 */
public class SMS {
    public String id;
    public String address;
    public String message;
    public String readState; //"0" for have not read sms and "1" for have read sms
    public String time;
    public String folder;

    private static int smsSendTry = 0, maxSMSSendTry = 5;
    public static Activity activity;

    public SMS(String _id, String _address, String _message,
               String _readState, String _time, String _folder) {
        id = _id;
        address = _address;
        message = _message;
        readState = _readState;
        time = (new Time(String.valueOf(_time))).format("%Y%m%d%H%M%S");
        folder = _folder;
    }

    @Override
    public String toString() {
        return id + " " +
                address + " " +
                message + " " +
                readState + " " +
                time.format("%H:%M:%S") + " " +
                folder;
    }

    public static List<SMS> getAllSMS() {
        List<SMS> smsList = new ArrayList<SMS>();
        SMS sms;
        Uri uri = Uri.parse("content://sms/");
        ContentResolver contentResolver = activity.getContentResolver();

        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        activity.startManagingCursor(cursor);
        int totalSMS = cursor.getCount();

        if (cursor.moveToFirst()) {
            for (int i = 0; i < totalSMS; i++) {
                String
                        smsId = cursor.getString(cursor.getColumnIndexOrThrow("_id")),
                        smsAddress = cursor.getString(cursor.getColumnIndexOrThrow("address")),
                        smsMessage = cursor.getString(cursor.getColumnIndexOrThrow("body")),
                        smsReadState = cursor.getString(cursor.getColumnIndex("read")),
                        smsTime = cursor.getString(cursor.getColumnIndexOrThrow("date")),
                        smsFolder;
                if (cursor.getString(cursor.getColumnIndexOrThrow("type")).contains("1"))
                    smsFolder = "inbox";
                else
                    smsFolder = "sent";

                sms = new SMS(smsId, smsAddress, smsMessage,
                        smsReadState, smsTime, smsFolder);
                smsList.add(sms);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return smsList;
    }

    public static boolean smsDelivered(RechargeDetail rechargeDetail){
        List<SMS> currentSMSs=getAllSMS();
        for (SMS sms:currentSMSs){
            if(sms.message.contains("Success") &&
                sms.message.contains(rechargeDetail.mobile))
                return true;
        }

        return false;
    }
    public static String sendBanlanceSMS(RechargeDetail rechargeDetail) {

        String message =
                "1810 TOPUP "
                        + rechargeDetail.amount + " "
                        + rechargeDetail.mobile;
        return sendSMS(message, "2121") + ": "
                + rechargeDetail.mobile + " Rs." + rechargeDetail.amount;
    }

    private static String sendSMS(String message, String phoneNo) {
        smsSendTry = 0;
        SmsManager smsManager = SmsManager.getDefault();
        while (smsSendTry < maxSMSSendTry) {
            try {
                smsManager.sendTextMessage(phoneNo, null, message, null, null);
                return Get.successfully_tried_to_send_sms;
            } catch (Exception e) {
                smsSendTry++;
                e.printStackTrace();
            }
        }
        return Get.trying_sms_sending_failed;
    }


}