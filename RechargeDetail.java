package com.example.TOPUPer;

import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Shiva Sunar on 12/3/2015.
 */
class RechargeDetail {
    public static final String
            TriedSMS = "00",
            SMSSent = "10",
            SMSSuccessSent = "11";

    public final String originTimeStamp;
    public final String id;
    public final String mobile;
    public String amount;
    public String status;
    public String latestStamp;

    public RechargeDetail(String _time, String _id, String _mobile, String _amount, String _status, String _latestStamp) {
        originTimeStamp = _time;
        id = _id;
        mobile = _mobile;
        amount = _amount;
        status = _status;
        latestStamp = _latestStamp;
    }

    public RechargeDetail(Element element) {
        id = element.siblingElements().get(0).select("input").attr("value").trim();
        String tempMobile = element.siblingElements().get(5).text();
        String tempAmount = element.siblingElements().get(6).text();

        originTimeStamp = Checker.timeStamp();
        mobile = tempMobile.substring(tempMobile.length() - 10, tempMobile.length()).trim();
        tempAmount = tempAmount.substring(0, tempAmount.length() - 3).trim();
        status = TriedSMS;
        latestStamp = Checker.timeStamp();

        if(isNCell(mobile)){
            amount=String.valueOf(Integer.valueOf(tempAmount) - 10);
            System.out.println("this is ncell:amount"+amount);
        }
        else
            amount=tempAmount;
        System.out.println(this.toString());
    }

    public boolean inList(List<RechargeDetail> rechargeDetailList) {
        for (RechargeDetail rechargeDetail : rechargeDetailList) {
            if (id.contains(rechargeDetail.id))
                return true;
        }
        return false;
    }

    public void renew(){
        if(this.status==RechargeDetail.TriedSMS)
            this.status=RechargeDetail.SMSSent;
        else if(this.status==RechargeDetail.SMSSent)
            this.status=RechargeDetail.SMSSuccessSent;

        this.latestStamp=Checker.timeStamp();
    }

    private static boolean isNCell(String mobileNumber) {
        String mobile = mobileNumber.substring(0, 4);
        List<String> ncellList = new ArrayList<>(
                Arrays.asList("98140", "98150", "98170", "98240", "98249", "98049", "98079", "98060", "98149", "98159",
                        "98169", "98179", "98160", "98040", "98190", "98110", "98113", "98123", "98009", "98043",
                        "98070", "98073", "98053", "98143", "98153", "98163", "98173", "98193", "98104", "98105",
                        "98047", "98077", "98059", "98147", "98157", "98167", "98177", "98197", "98199", "98117",
                        "98247", "98257", "98267", "98048", "98076", "98078", "98148", "98158", "98168", "98178",
                        "98198", "98196", "98176", "98120", "98121", "98008", "98096", "98042", "98068", "98071",
                        "98072", "98142", "98152", "98162", "98172", "98192", "98111", "98112", "98118", "98122",
                        "98091", "98092", "98211", "98212", "98218", "9803", "9808", "9813", "9818", "98100", "98101",
                        "98102", "98103", "98166", "98241", "98251", "98261", "98041", "98065", "98066", "98067",
                        "98058", "98141", "98151", "98161", "98171", "98191", "98129", "98007", "98214", "98215",
                        "98219", "98044", "98069", "98074", "98075", "98054", "98144", "98154", "98164", "98174",
                        "98194", "98175", "98114", "98115", "98119", "98061", "98051", "98052", "98062", "98095",
                        "98097", "98098", "98128", "98108", "98109", "98063", "98093", "98195", "98124", "98125",
                        "98005", "98224", "98225", "98045", "98145", "98155", "98165", "98046", "98146", "98156",
                        "98116", "98126", "98006", "98246", "98256", "98127", "98106", "98107", "9805740", "98064",
                        "98094"));

        for (String ncell : ncellList) {
            if (mobile.contains(ncell))
                return true;
            System.out.println("mobile="+mobile+"don't contains"+ncellList);//todo comment

        }
        return false;
    }

    public static boolean isSkyOrUTL(RechargeDetail rechargeDetail) {
        List<String> skyUTLList = new ArrayList<>(Arrays.asList(
                "974"
        ));
        String mobile = rechargeDetail.mobile.substring(0, 4);
        for (String skyUTL : skyUTLList)
            if (mobile.contains(skyUTL))
                return true;
        return false;
    }

    @Override
    public String toString() {
        return
                this.originTimeStamp + " " +
                        this.id + " " +
                        this.mobile + " " +
                        this.amount + " " +
                        this.status + " " +
                        this.latestStamp;
    }
}