package com.example.TOPUPer;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Shiva Sunar on 11/21/2015.
 * this class as a webpage fetcher is specifically designed for the TOPUPer App.
 */
public class Page {
    private static int webRequestTry = 0, maxTry = 5;
    private static boolean webResponseOK = false;
    private static Map<String, String> cookies = new HashMap<String, String>();
    private static Document emptyDocument = Jsoup.parse("");
    private static String loginURL="http://************//login";
    private static String reloadURL="http://**********//reload";

    public static boolean login() {
        cookies.clear();
        Connection connection = Jsoup.connect(loginURL);
        for (Map.Entry<String, String> cookie : cookies.entrySet()) {
            connection.cookie(cookie.getKey(), cookie.getValue());
        }

        Connection.Response response;
        webRequestTry = 0;
        webResponseOK = false;
        while (!webResponseOK && webRequestTry < maxTry) {
            try {
                response = connection
                        .data("name", "**********")
                        .data("password", "**********")
                        .data("submit_login", "Login")
                        .method(Connection.Method.POST)
                        .execute();
                if (response.statusCode() == 200) {
                    cookies.putAll(response.cookies());
                    webResponseOK = true;
                    return true;

                }
            } catch (Exception e) {
                webRequestTry++;
//                e.printStackTrace();
            }
        }
        System.out.println("Web Request Unsuccessful at PAGE.LOGIN().");
        return false;
    }

    public static Document get() {

        Connection connection = Jsoup.connect(reloadURL);
        for (Map.Entry<String, String> cookie : cookies.entrySet()) {
            connection.cookie(cookie.getKey(), cookie.getValue());
        }

        Connection.Response response;
        webRequestTry = 0;
        webResponseOK = false;
        while (!webResponseOK && webRequestTry < maxTry) {
            try {
                response = connection.execute();
                if (response.statusCode() == 200) {
                    cookies.putAll(response.cookies());
                    webResponseOK = true;
                    return response.parse();
                }
            } catch (Exception e) {
                webRequestTry++;
//                e.printStackTrace();
            }

        }
        System.out.println("Web Request Unsuccessful at PAGE.GET().");
        login();
        return emptyDocument;
    }

    public static boolean submitSuccess(RechargeDetail r) {

        Connection connection = Jsoup.connect(reloadURL);
        for (Map.Entry<String, String> cookie : cookies.entrySet()) {
            connection.cookie(cookie.getKey(), cookie.getValue());
        }

        Connection.Response response;
        webRequestTry = 0;
        webResponseOK = false;
        while (!webResponseOK && webRequestTry < maxTry) {
            try {
                response = connection
                        .data("id", r.id)
                        .data("refno", "Successful")
                        .method(Connection.Method.POST)
                        .execute();
                if (response.statusCode() == 200) {
                    cookies.putAll(response.cookies());
                    webResponseOK = true;
                    return true;
                }

            } catch (Exception e) {
                webRequestTry++;
//                e.printStackTrace();
            }
        }
        System.out.println("Web Request Unsuccessful at PAGE.SUBMITSUCCESS().");
        return false;
    }

    public static Document getSimpleSite(String url) {
        Connection connection = Jsoup.connect(url);
        Connection.Response response;
        webRequestTry = 0;
        webResponseOK = false;
        while (!webResponseOK && webRequestTry < maxTry) {
            try {
                response = connection.execute();
                if (response.statusCode() == 200) {
                    webResponseOK = true;
                    return response.parse();
                }
            } catch (Exception e) {
                webRequestTry++;
//                e.printStackTrace();
            }
        }
        System.out.println("Web Request Unsuccessful at PAGE.GETSIMPLESITE().");
        return emptyDocument;
    }




    private static List<Element> DocumentParser(Document document) {
        List<Element> notRechargedForms = new ArrayList<Element>();
        try {
            Elements formElements = document.select("#form");
            for (Element form : formElements)
                if (form.siblingElements().size() == 9)
                    notRechargedForms.add(form);
        } catch (Exception e) {
            System.out.println("Error in Page.DocumentParser.");
            e.printStackTrace();
        }

        return notRechargedForms;
    }

    public static List<RechargeDetail> rechargeDetailLists(Document document) {
        List<Element> notRechargedForms = DocumentParser(document);
        List<RechargeDetail> rechargeDetailList = new ArrayList<RechargeDetail>();
        for (Element element : notRechargedForms)
            rechargeDetailList.add(new RechargeDetail(element));

        return rechargeDetailList;
    }

}
