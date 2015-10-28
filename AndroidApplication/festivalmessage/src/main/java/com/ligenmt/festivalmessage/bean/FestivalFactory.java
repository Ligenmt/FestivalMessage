package com.ligenmt.festivalmessage.bean;

import android.content.Context;
import android.content.SharedPreferences;

import com.ligenmt.festivalmessage.dao.FestivalDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenov0 on 2015/10/6.
 */
public class FestivalFactory {

    private static FestivalFactory instance;

    private List<Festival> festivals = new ArrayList<>();
    private List<Message> messages = new ArrayList<>();

    private FestivalFactory(){

    //    initFestivals();
    //    initMessages();

    }

    private void initMessages() {
        messages.add(new Message(0, 1, "迎新的春联贴起，吉祥的鞭炮响起，喜庆的灯笼挂起，美味的饺子捞起，团圆的酒杯举起，祝福的话儿说起，羊年平安顺利走起，祝新年愉快!"));
        messages.add(new Message(1, 1, "迎新的春联贴起，吉祥的鞭炮响起，喜庆的灯笼挂起，美味的饺子捞起，团圆的酒杯举起，祝福的话儿说起，羊年平安顺利走起，祝新年愉快!"));
        messages.add(new Message(2, 1, "迎新的春联贴起，吉祥的鞭炮响起，喜庆的灯笼挂起，美味的饺子捞起，团圆的酒杯举起，祝福的话儿说起，羊年平安顺利走起，祝新年愉快!"));
        messages.add(new Message(3, 2, "迎新的春联贴起，吉祥的鞭炮响起，喜庆的灯笼挂起，美味的饺子捞起，团圆的酒杯举起，祝福的话儿说起，羊年平安顺利走起，祝新年愉快!"));
        messages.add(new Message(4, 2, "迎新的春联贴起，吉祥的鞭炮响起，喜庆的灯笼挂起，美味的饺子捞起，团圆的酒杯举起，祝福的话儿说起，羊年平安顺利走起，祝新年愉快!"));
        messages.add(new Message(5, 2, "迎新的春联贴起，吉祥的鞭炮响起，喜庆的灯笼挂起，美味的饺子捞起，团圆的酒杯举起，祝福的话儿说起，羊年平安顺利走起，祝新年愉快!"));
    }

    private void initFestivals() {
//        festivals.add(new Festival(0, "元旦节"));
//        festivals.add(new Festival(1, "春节"));
//        festivals.add(new Festival(2, "清明节"));
//        festivals.add(new Festival(3, "端午节"));
//        festivals.add(new Festival(4, "七夕节"));
//        festivals.add(new Festival(5, "中秋节"));
//        festivals.add(new Festival(6, "国庆节"));
    }

    public static FestivalFactory getInstance() {

        if(instance == null) {
            synchronized (FestivalFactory.class) {
                if(instance == null) {
                    instance = new FestivalFactory();
                }
            }
        }
        return  instance;
    }

    //返回一个副本，免得其中内容被更改
    public List<Festival> getFestivalsFromDao(Context context) {
        FestivalDao dao = new FestivalDao(context);
        festivals = dao.enumFestival();
        return new ArrayList<Festival>(festivals);
    }

    public List<Message> getMessages() { return  new ArrayList<Message>(messages); }

    public Festival getFestivalById(int id) {
        for(Festival f : festivals) {
            if(f.getId() == id) {
                return  f;
            }
        }
        return null;
    }

    public List<Message> getMessagesByFestivalId(Context context, int fid) {
        FestivalDao dao = new FestivalDao(context);
        List<Message> msgs = new ArrayList<>();
        for(Message msg : messages) {
            if(msg.getFestivalId() == fid) {
                msgs.add(msg);
            }
        }
        return msgs;
    }
}
