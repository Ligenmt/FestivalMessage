package com.ligenmt.festivalmessage;

import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.ligenmt.festivalmessage.bean.FestivalFactory;
import com.ligenmt.festivalmessage.dao.FestivalDao;
import com.ligenmt.festivalmessage.fragment.FestivalFragment;
import com.ligenmt.festivalmessage.fragment.RecordFragment;

public class MainActivity extends AppCompatActivity {

    private TabLayout mTablayout;
    private ViewPager mViewPager;
    SharedPreferences sp;

    private String[] mTitles = new String[]{"节日短信","发送记录"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        boolean firstRun = sp.getBoolean("firstrun", true);
        if(firstRun) {
            fistRunInit();
        }

        initViews();

    }



    private void initViews() {
        mTablayout = (TabLayout) findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                Fragment fragment = null;
                switch (position) {
                    case 1:
                        fragment = new RecordFragment();
                        break;
                    case 0:
                        fragment = new FestivalFragment();
                        break;
                    default:
                        break;
                }
                return fragment;
            }

            @Override
            public int getCount() {
                return mTitles.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mTitles[position];
            }
        });
        //ViewPager与TabLayout关联
        mTablayout.setupWithViewPager(mViewPager);
    }

    /**
     * 首次启动
     */
    private void fistRunInit() {
        FestivalDao dao = new FestivalDao(this);
        dao.addFestival("元旦节", "01-01");
        dao.addFestival("春节", "");
        dao.addFestival("情人节","02-14");
        dao.addFestival("清明节","");
        dao.addFestival("端午节","");
        dao.addFestival("中秋节","");
        dao.addFestival("母亲节","");
        dao.addFestival("重阳节","");
        dao.addFestival("圣诞节", "12-25");
        dao.addFestival("自定义","");

        initMessages(1, R.array.yuandan, dao);
        initMessages(2, R.array.chunjie, dao);
        initMessages(3, R.array.qingrenjie, dao);
        initMessages(4, R.array.qingmingjie, dao);
        initMessages(5, R.array.duanwujie, dao);
        initMessages(6, R.array.zhongqiujie, dao);
        initMessages(7, R.array.muqinjie, dao);
        initMessages(8, R.array.chongyangjie, dao);
        initMessages(9, R.array.shengdanjie, dao);
        initMessages(10, R.array.mytext, dao);

        dao.addMessage(2, "又一年过去，有迷茫有惊喜。有了你，难题也变容易;相信你，决策凿凿有力;跟随你，必将再夺佳绩;感谢你，一路扶持提携。新春佳节，祝你羊年大吉!");
        dao.addMessage(2, "玉龙乘雪去，金羊携春来。瑞气腾九域，淑景遍八方。户户门结彩，家家烛放光。梅竹传喜讯，花酒醉东风。春节无限美，人人笑开颜。祝你羊年发，好运无限延。");
        dao.addMessage(2, "用思念做一件温暖的新衣，用惦记写一对富贵的春联，用牵挂作一幅平安的年画，用友谊做一盘快乐的糖果，把这些用真挚的问候打包送给你，提前祝你新年快乐，羊年大吉!");
        dao.addMessage(2, "迎新的春联贴起，吉祥的鞭炮响起，喜庆的灯笼挂起，美味的饺子捞起，团圆的酒杯举起，祝福的话儿说起，羊年平安顺利走起，祝新年愉快!");
        dao.addMessage(2, "辞旧迎新羊年到，吉祥吹响集结号，好运财运常关照，财富幸福不离弃。衷心祝愿好朋友，新年幸福，平安吉祥，数着票子品美酒，载歌载舞合家欢。");
        dao.addMessage(2, "思悠悠，情满心头;情柔柔，遥念好友;痛饮两盏淡酒，喜看羊年露新头，任相思沾染满襟袖，让幸福轻握君手;新春到，愿你在新的一年里逍遥游，乐无无忧!");
        dao.addMessage(2, "昨夜醉酒难宿，酿得短词一首，今日赠朋友，聊表新春祝福，欢度，欢度，开启快乐洪流。财神陪你上路，爱神向你献舞，锦绣好前途，福喜频频招手，加油，加油，羊年大展宏图。");
        dao.addMessage(2, "羊头抬，羊尾摆，羊身盘踞八方财;羊鳞闪，羊身迈，山舞银羊祥云开;羊眼圆，羊嘴帅，羊衔瑞草添精彩;龙归去，羊年到，千家万户乐淘淘。祝新春快乐!");
        sp.edit().putBoolean("firstrun", false).commit();
    }

    /**
     * 初始化内置短信
     * @param fid
     * @param resourceId
     * @param dao
     */
    private void initMessages(int fid, int resourceId, FestivalDao dao) {
        TypedArray typedArray = getResources().obtainTypedArray(resourceId);
        for(int i=0; i<typedArray.length(); i++) {
            String text = typedArray.getString(i);
            dao.addMessage(fid, text);
        }
        typedArray.recycle();
    }
}
