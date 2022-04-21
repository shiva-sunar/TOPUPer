package com.example.TOPUPer;

import android.os.Environment;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shiva Sunar on 11/24/2015.
 */
public class LogSaver {
    public final static String
    SD=Environment.getExternalStorageDirectory().getPath(),
    TopUpLog=SD+"/TopUpLog.txt",
    FailedSMS=SD+"FailedSMS.txt",
    Successful=SD+"/Successful.txt",
    FullSuccessful=SD+"/FullSuccessful.txt",
    SkyUTL=SD+"/SkyUTL.txt";

    public static void deleteAllFile(){
//        deleteFile(TopUpLog);
        deleteFile(FailedSMS);
        deleteFile(Successful);
        deleteFile(FullSuccessful);
        deleteFile(SkyUTL);
    }


    public static void writeToTopUpLog(String logRecord) {
        writeToFile(logRecord, TopUpLog);
    }

    public static void writeToFailedSMS(RechargeDetail rechargeDetail) {
        writeToFile(rechargeDetail.toString(),FailedSMS);
    }

    public static void writeToSuccessful(RechargeDetail rechargeDetail) {
        writeToFile(rechargeDetail.toString(),Successful);
    }

    public static void writeToFullSuccessful(RechargeDetail rechargeDetail) {
        writeToFile(Checker.readableTimeStamp() +" "
                +rechargeDetail.id +" "
                +rechargeDetail.mobile +" "
                +rechargeDetail.amount
                ,FullSuccessful);
    }

    public static void writeToSkyUTLList(String logRecord) {
        writeToFile(logRecord,SkyUTL);
    }

    private static boolean deleteFile(String filePath) {
        File deletingFile = new File(filePath);
        boolean deleted = false;
        if (deletingFile.exists()) {
            try {
                deleted = deletingFile.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return deleted;
    }

    public static List<RechargeDetail> rechargeDetailsFromFile(String filePath) {
        List<RechargeDetail> rechargeDetails = new ArrayList<RechargeDetail>();
        List<String> stringLinesFromFile = listOfLinesFromFile(filePath);
        for (String line : stringLinesFromFile) {
            try {
                System.out.println(line);//todo comment this
                if (line.contains(" 98")) {
                    String[] words = line.split(" ");
                    RechargeDetail recharge = new RechargeDetail(
                            words[0],
                            words[1],
                            words[2],
                            words[3],
                            words[4],
                            words[5]);
                    rechargeDetails.add(recharge);
                }
            }catch (Exception e){e.printStackTrace();}

        }
        return rechargeDetails;
    }

    private static List<String> listOfLinesFromFile(String filePath) {
        List<String> lineList = new ArrayList<String>();
        String textFile = readFromFile(filePath);
        String[] splittedText = textFile.split("\r\n");
        for (String line : splittedText)
            lineList.add(line.trim());
        return lineList;
    }

    private static void writeToFile(String logRecord, String filePath) {
        if (isExternalStorageWritable()) {
            renameLargeFile(filePath);
            try {
                FileWriter fileWriter;          //writing at end
                fileWriter = new FileWriter(filePath, true);
                fileWriter.write(logRecord + "\r\n");
                fileWriter.flush();
                fileWriter.close();
            } catch (Exception e) {
                e.printStackTrace();
//              do something
            }
        } else
            System.out.println("SD-Card not Writable.");
    }

    public static void updateFile(String filePath,String oldText,String newText){  //todo
        String fileContent=readFromFile(filePath);
        fileContent=fileContent.replace(oldText,newText);
        if(deleteFile(filePath))
            writeToFile(fileContent, filePath);
    }

    private static String readFromFile(String filePath) {
        if (isExternalStorageWritable()) {
            File file = new File(filePath);
            StringBuilder text = new StringBuilder("");
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                while ((line = br.readLine()) != null) {
                    text.append(line);
                    text.append("\r\n");
                }
                br.close();
                return text.toString();
            } catch (FileNotFoundException e){
                System.out.println("Exception File:"+filePath+"Don't Exit");
            }
            catch (IOException e) {
                //todo add proper error handling here
                e.printStackTrace();
            }
        } else
            System.out.println("SD-Card not Readable.");
        return "";
    }

    private static void renameLargeFile(String filePath) {
        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            if (file.length() > 1024000) {
                String oldName = String.valueOf(filePath);
                String newName = String.valueOf(oldName.replace(".txt", Checker.date() + ".txt"));
                File newFile = new File(newName);
                file.renameTo(newFile);
            }
        }

    }

    private static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }


}
