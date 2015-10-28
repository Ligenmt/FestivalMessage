package com.ligenmt.festivalmessage.tool;

import android.app.PendingIntent;
import android.telephony.SmsManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenov0 on 2015/10/13.
 * 发送短信
 */
public class SMSBiz {

    public int sendMsg(String number, String text, PendingIntent sentPi, PendingIntent deliverPi) {

        SmsManager sm = SmsManager.getDefault();
        ArrayList<String> contents = sm.divideMessage(text);
        for(String content : contents) {
            sm.sendTextMessage(number, null, content, sentPi, deliverPi);
        }
        return contents.size();
    }

    public int sendMsg(List<String> numbers, String text, PendingIntent sentPi, PendingIntent deliverPi) {

        int result = 0;
        for(String number : numbers) {
            int count = sendMsg(number, text, sentPi, deliverPi);
            result += count;
        }
        return result;
    }
}
