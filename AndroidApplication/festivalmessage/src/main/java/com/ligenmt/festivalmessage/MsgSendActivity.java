package com.ligenmt.festivalmessage;

import android.animation.ObjectAnimator;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.app.AlertDialog.Builder;

import com.ligenmt.festivalmessage.bean.Contact;
import com.ligenmt.festivalmessage.dao.FestivalDao;
import com.ligenmt.festivalmessage.tool.SMSBiz;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MsgSendActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String APP_ID = "wx7963fc7530590472";
    private static final String TAG = "MsgSendActivity";
    private IWXAPI wxApi;

    private FlowLayout mFlowlayout;
    private EditText etContent;
    private LinearLayout llBottomBar;
    private ProgressBar pb;
    private ScrollView sv;

    private ArrayList<Contact> contactsInfo;
    private LayoutInflater mInflater;
    private SendSMSThread thread;

    private boolean isBottomBarShown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_msg);

        initView();
        initReceiver();

    }

    private void initView() {
        mFlowlayout = (FlowLayout) findViewById(R.id.fl_contacts);
        etContent = (EditText) findViewById(R.id.et_content);
        llBottomBar = (LinearLayout) findViewById(R.id.ll_bottom_bar);
        findViewById(R.id.btn_add_contact).setOnClickListener(this);
        findViewById(R.id.btn_send).setOnClickListener(this);
        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.btn_share).setOnClickListener(this);
        pb = (ProgressBar) findViewById(R.id.pb);
        sv = (ScrollView) findViewById(R.id.sv);

        etContent = (EditText) findViewById(R.id.et_content);
        String content = getIntent().getStringExtra("content");
        etContent.setText(content);
        etContent.setSelection(etContent.getText().length());
        pb.setVisibility(View.INVISIBLE);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_contact:
                Intent intent = new Intent(this, ContactActivity.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.btn_send:
                if(contactsInfo == null || contactsInfo.size() == 0) {
                    Toast.makeText(this, "还未选择联系人呢", Toast.LENGTH_SHORT).show();
                    return;
                }
                int count = contactsInfo.size();
                Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                builder.setTitle("提示").setMessage("一共选择了" + count + "人，确定发送吗？");
                builder.setPositiveButton("发送吧！", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        thread = new SendSMSThread();
                        thread.start();
                    }
                }).setNegativeButton("再等等", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
                break;

            case R.id.btn_share:
                if(!isBottomBarShown) {
                    showBottomBar();
                }
                break;
//            case R.id.btn_share_wx:
//                shareToWX();
//                break;
//            case R.id.btn_share_timeline:
//                shareToTimeline();
//                break;
//            case R.id.btn_cancel:
//                hideBottomBar();
//                break;
            case R.id.btn_back:
                finish();
            default:

                break;

        }
    }

    private void shareToTimeline() {
        wxApi = WXAPIFactory.createWXAPI(this, APP_ID);
        wxApi.registerApp(APP_ID);

        WXTextObject textObject2 = new WXTextObject();
        String content2 = etContent.getText().toString();
        textObject2.text = content2;
        WXMediaMessage message2 = new WXMediaMessage();
        message2.mediaObject = textObject2;
        message2.description = content2;

        SendMessageToWX.Req req2 = new SendMessageToWX.Req();
        req2.message = message2;
        req2.transaction = String.valueOf(System.currentTimeMillis());
        req2.scene = SendMessageToWX.Req.WXSceneTimeline;
        wxApi.sendReq(req2);
    }

    private void shareToWX() {
        wxApi = WXAPIFactory.createWXAPI(this, APP_ID);
        //注册到微信
        wxApi.registerApp(APP_ID);

        WXTextObject textObject = new WXTextObject();
        String content = etContent.getText().toString();
        textObject.text = content;
        WXMediaMessage message = new WXMediaMessage();
        message.mediaObject = textObject;
        message.description = content;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.message = message;
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.scene = SendMessageToWX.Req.WXSceneSession;
        wxApi.sendReq(req);
    }

    private PopupWindow pw;
    private LinearLayout llShare;

    private void showBottomBar() {
        pw = new PopupWindow(this);
        pw.setWidth(etContent.getWidth());
        pw.setHeight(etContent.getHeight() * 3 / 4);
        if(llShare == null) {
            llShare = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.layout_share, null, false);
        }
        pw.setContentView(llShare);
        pw.showAtLocation(sv, Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
        pw.setOutsideTouchable(true);

        llShare.findViewById(R.id.btn_share_wx).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareToWX();
            }
        });
        llShare.findViewById(R.id.btn_share_timeline).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareToTimeline();
            }
        });
        llShare.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pw.dismiss();
                isBottomBarShown = false;
            }
        });
        isBottomBarShown = true;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            contactsInfo = data.getParcelableArrayListExtra("contacts");
            if(mInflater == null) {mInflater = LayoutInflater.from(this); }
            mFlowlayout.removeAllViews();
            for(Contact contact : contactsInfo) {
                TextView tv = (TextView) mInflater.inflate(R.layout.item_flowlayout, mFlowlayout, false);
                tv.setText(contact.getName());
                System.out.println("add");
                mFlowlayout.addView(tv);
            }
        }
    }
    private static final int SHOW_PROGRESSBAR = 1;
    private static final int HIDE_PROGRESSBAR = 2;
    private static final String REPLACE_NAME = "#姓名#";

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_PROGRESSBAR:
                    pb.setVisibility(View.VISIBLE);
                    break;
                case HIDE_PROGRESSBAR:
                    pb.setVisibility(View.INVISIBLE);
                    Toast.makeText(MsgSendActivity.this, "发送完成", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    class SendSMSThread extends Thread {

        @Override
        public void run() {
            handler.sendEmptyMessage(SHOW_PROGRESSBAR);
            //发送短信
            SmsManager sm = SmsManager.getDefault();
            SMSBiz smsBiz = new SMSBiz();
            String content = etContent.getText().toString();

            for (Contact contact : contactsInfo) {
                String sender = contact.getNumber();
                //自动替换姓名
                if(content.contains(REPLACE_NAME)) {
                    String name = contact.getName();
                    content = content.replace("", name);
                }
                smsBiz.sendMsg(sender, content, mSendPi, mDeliverPi);
            }
            handler.sendEmptyMessage(HIDE_PROGRESSBAR);
            //保存发送记录
            FestivalDao dao = new FestivalDao(MsgSendActivity.this);

            Date currentDate = new Date();
            String date = new SimpleDateFormat("yyyy年MM月dd日 HH:mm").format(currentDate);
            dao.addRecord(content, date);

          //  finish();
        }
    }

    private static final String ACTION_SEND_MSG = "ACTION_SEND_MSG";
    private static final String ACTION_DELIVER_MSG = "ACTION_DELIVER_MSG";

    private PendingIntent mSendPi;
    private PendingIntent mDeliverPi;
    private BroadcastReceiver mSendReceiver;
    private BroadcastReceiver mDeliverReceiver;

    private void initReceiver() {
        Intent sendIntent = new Intent(ACTION_SEND_MSG);
        mSendPi = PendingIntent.getBroadcast(this, 0, sendIntent, 0);
        Intent deliverIntent = new Intent(ACTION_DELIVER_MSG);
        mDeliverPi = PendingIntent.getBroadcast(this, 0, deliverIntent, 0);
        registerReceiver(mSendReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.e(TAG, "短信发送成功!");
            }
        }, new IntentFilter(ACTION_SEND_MSG));
        registerReceiver(mDeliverReceiver= new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.e(TAG, "短信返回成功!");
            }
        }, new IntentFilter(ACTION_DELIVER_MSG));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mSendReceiver);
        unregisterReceiver(mDeliverReceiver);
    }
}
