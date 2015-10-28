package com.ligenmt.festivalmessage;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ligenmt.festivalmessage.bean.Message;
import com.ligenmt.festivalmessage.dao.FestivalDao;

import java.util.List;

/**
 * Created by Ligen on 2015/10/6.
 * 选择祝福语界面
 */
public class MsgChooseActivity extends AppCompatActivity {

    private ListView lvMsgs;
    private FloatingActionButton btnAddMessage;
    private FloatingActionButton btnBack;

    private List<Message> msgs;
    private LayoutInflater mInflater;
    private MessageAdapter adapter;
    private FestivalDao dao;
    private int fid;


    int lastVisibleItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_msg);
        init();
        initViews();
        adapter = new MessageAdapter();
        lvMsgs.setAdapter(adapter);
        //转到新增祝福语界面
        btnAddMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MsgChooseActivity.this, AddMessageActivity.class);
                startActivityForResult(intent, 0);
            }
        });
        //返回
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    int mTouchSlop;

    private void init() {
        dao = new FestivalDao(this);
        mInflater = LayoutInflater.from(this);
        fid = getIntent().getIntExtra("festivalid", -1);
        setTitle(dao.findFestivalById(fid).getName());
        mTouchSlop = ViewConfiguration.get(this).getScaledTouchSlop();
    }

    private int mFirstY;
    private boolean isBtnShown = true;
    private Animator mAnimator;
    private AnimatorSet mAnimSet = new AnimatorSet();

    private void initViews() {
        lvMsgs = (ListView) findViewById(R.id.lv_msg);
        btnAddMessage = (FloatingActionButton) findViewById(R.id.btn_add_message);
        btnBack = (FloatingActionButton) findViewById(R.id.btn_back);

        msgs = dao.findMessageByFid(fid);
        lvMsgs.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                int direction = -1;
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mFirstY = (int) motionEvent.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:

                        int currentY = (int) motionEvent.getY();
                        if(currentY - mFirstY > mTouchSlop) {
                            //向下滑,内容往上
                            direction = 0;
                        } else if(mFirstY - currentY > mTouchSlop) {
                            //向上滑,内容往下
                            direction = 1;
                        }
                        if(direction == 1) {
                            if(isBtnShown == true) {
                                btnAnim(1); //隐藏
                                isBtnShown = !isBtnShown;
                            }
                        } else if(direction == 0) {
                            if(isBtnShown == false) {
                                btnAnim(0);  //显示
                                isBtnShown = !isBtnShown;
                            }
                        }

                        break;
                    case MotionEvent.ACTION_UP:

                        break;

                }
                return false;
            }
        });
    }

    private void btnAnim(int flag) {
        if(mAnimSet != null && mAnimSet.isRunning()) {
            mAnimSet.cancel();

        }
        if(flag == 0) {
            Animator anim1 = ObjectAnimator.ofFloat(btnAddMessage, "translationY", btnAddMessage.getTranslationY(), 0);
            Animator anim2 = ObjectAnimator.ofFloat(btnBack, "translationY", btnAddMessage.getTranslationY(), 0);
            mAnimSet.playTogether(anim1, anim2);
        } else {
            Animator anim1 = ObjectAnimator.ofFloat(btnAddMessage, "translationY", btnAddMessage.getTranslationY(), btnAddMessage.getHeight());
            Animator anim2 = ObjectAnimator.ofFloat(btnBack, "translationY", btnAddMessage.getTranslationY(), btnAddMessage.getHeight());
            mAnimSet.playTogether(anim1, anim2);
        }
        mAnimSet.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //完成新增祝福语
        if(resultCode == RESULT_OK) {
            String content = data.getStringExtra("content");
            FestivalDao dao = new FestivalDao(this);
            dao.addMessage(fid, content);
            msgs = dao.findMessageByFid(fid);
            adapter.notifyDataSetChanged();
            lvMsgs.setSelection(msgs.size()-1);
        }
    }


    class MessageAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return msgs.size();
        }

        @Override
        public Message getItem(int i) {
            return msgs.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = mInflater.inflate(R.layout.item_message, parent, false);
            }
            final Message message = getItem(position);
            TextView tvContent = (TextView) convertView.findViewById(R.id.tv_message);
            final String content = message.getContent();
            //转到发送界面
            Button btnSend = (Button) convertView.findViewById(R.id.btn_send);
            tvContent.setText(content);
            btnSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MsgChooseActivity.this, MsgSendActivity.class);
                    intent.putExtra("content", content);
                    startActivity(intent);
                }
            });
            //移除祝福语
            Button btnRemove = (Button) convertView.findViewById(R.id.btn_remove);
            btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MsgChooseActivity.this);
                    builder.setTitle("提示").setMessage("删除这一条吗?");
                    builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dao.deleteMessageById(message.getId());
                            msgs.remove(message);
                            adapter.notifyDataSetChanged();
                            dialogInterface.dismiss();
                        }
                    })
                    .setNegativeButton("否", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    builder.show();
                }
            });
            return convertView;
        }
    }

}
