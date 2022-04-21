package com.example.TOPUPer;

import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shiva Sunar on 11/25/2015.
 */
public class Loader {
    public static Boolean initialized = null;
    private static boolean loggedIn = false;
    private static int loggedInWithSameCookie = 0;
    private static List<RechargeDetail> newRechargeList = new ArrayList<RechargeDetail>();
    private static MainActivity.AsyncLoader asyncLoader;
    public static Thread loaderThread;
    public static boolean runLoader = true;

    public Loader() {
        initialized = true;
    }

    public static void runService(MainActivity.AsyncLoader tempAsyncLoader) {
        asyncLoader = tempAsyncLoader;
        if (Checker.internetWorking() && runLoader) {
            LogSaver.writeToTopUpLog(Get.long_bar);
            if (!loggedIn || loggedInWithSameCookie > 30) {
                loginSite();
                loggedInWithSameCookie = 0;
            }
            if (Checker.internetWorking() && loggedIn && runLoader)
                retryFailedSMS();
            if (Checker.internetWorking() && loggedIn && runLoader)
                checkForFullSuccessful();

            if (Checker.internetWorking() && loggedIn && runLoader) {
                fetchDataFromSite();
                loggedInWithSameCookie++;
                if (Checker.internetWorking() && runLoader) {  //here not only internet working but also SIM-Card Working.
                    if (newRechargeList.size() == 0)
                        recordBroadcastAndSave(Get.no_new_number);
                    else
                        rechargeAllRechargeList(newRechargeList);
                }
            }
        }
        if (runLoader) {
            try {
                asyncLoader.doProgress(Get.sleeping + "\n");
                loaderThread = Thread.currentThread();
                Thread.sleep(60000);
            } catch (Exception e) {
                e.printStackTrace();
                asyncLoader.doProgress(Get.sleeping_exception);
            }
        }
    }

    private static void loginSite() {
        try {
            if (Page.login()) {
                recordBroadcastAndSave(Get.logged_in);
                loggedIn = true;
            } else {
                recordBroadcastAndSave(Get.login_failed);
                loggedIn = false;
            }
        } catch (Exception e) {
            recordBroadcastAndSave(Get.login_failed);
            loggedIn = false;
            e.printStackTrace();
        }
    }

    private static void fetchDataFromSite() {
        if (loggedIn && Checker.internetWorking() && runLoader) {
            try {
                Document rechargeHtmlPage = Page.get();
                if (rechargeHtmlPage.body().hasText())
                    recordBroadcastAndSave(Get.data_fetched);
                else
                    recordBroadcastAndSave(Get.data_fetching_failed);
                newRechargeList = Page.rechargeDetailLists(rechargeHtmlPage);
            } catch (Exception e) {
                recordBroadcastAndSave(Get.data_fetching_failed + " With Exception.");
                e.printStackTrace();
            }
        }
    }

    private static void rechargeAllRechargeList(List<RechargeDetail> rechargeDetailsToRecharge) {
        List<RechargeDetail> failedSMSs = LogSaver
                .rechargeDetailsFromFile(LogSaver.FailedSMS);
        List<RechargeDetail> successfulSMSs = LogSaver
                .rechargeDetailsFromFile(LogSaver.Successful);
        for (RechargeDetail r : rechargeDetailsToRecharge) {
            if (!r.inList(failedSMSs) && !r.inList(successfulSMSs)) {
                dealWithNewData(r);
            }
        }
    }

    private static void dealWithNewData(RechargeDetail r) {
        if(runLoader && Checker.internetWorking())
                if (!RechargeDetail.isSkyOrUTL(r))
                    dealWithNCellNTC(r);
//                else              todo uncomment
//                    dealWithSkyOrUTL(r);

    }

    private static void dealWithNCellNTC(RechargeDetail r) {     //don't check runloader
        if(Page.submitSuccess(r)) {
            String smsMessage = SMS.sendBanlanceSMS(r);
            recordBroadcastAndSave(smsMessage);
            if (smsMessage.contains(Get.word_in_successfully_tried_sms_message)) {
                LogSaver.writeToSuccessful(r);
            } else {
                LogSaver.writeToFailedSMS(r);
            }
        }

    }

    private static void dealWithSkyOrUTL(RechargeDetail r) {    //don't check runloader
        if(Page.submitSuccess(r)) {
            recordBroadcastAndSave(Get.sky_utl_submitted + " " + r.mobile + " " + r.amount);
            LogSaver.writeToSkyUTLList(r.toString());
            //todo get rechargeCard and send PIN to the number
        }
    }

    private static void retryFailedSMS() {
        List<RechargeDetail> FailedSMSs = LogSaver.rechargeDetailsFromFile(LogSaver.FailedSMS);
        for (RechargeDetail fr : FailedSMSs)
            if (runLoader) {
                String smsMessage = SMS.sendBanlanceSMS(fr);
                if (smsMessage.contains(Get.word_in_successfully_tried_sms_message)) {
                    recordBroadcastAndSave(smsMessage);
                    LogSaver.updateFile(LogSaver.FailedSMS, fr.toString(), "");
                    LogSaver.writeToSuccessful(fr);
                }
            }
    }

    private static void checkForFullSuccessful() {
        //todo
        List<RechargeDetail> Successfuls = LogSaver.rechargeDetailsFromFile(LogSaver.Successful);
        for (RechargeDetail s : Successfuls) {
            if (SMS.smsDelivered(s)) {
                LogSaver.updateFile(LogSaver.Successful, s.toString(), "");
                LogSaver.writeToFullSuccessful(s);
            }
//            else if (Checker.timeStampExpired(s.originTimeStamp)) {   //todo
//                LogSaver.updateFile(LogSaver.Successful, s.toString(), "");
//                dealWithNewData(s);
//            }
        }
    }


    private static void recordBroadcastAndSave(String record) {
        System.out.println(Checker.readableTimeStamp() + "\t" + record);
        LogSaver.writeToTopUpLog(Checker.readableTimeStamp() + "\t" + record);
        asyncLoader.doProgress(record);
    }

}
