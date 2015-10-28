package com.ligenmt.festivalmessage;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class FlowLayout extends ViewGroup {
	
	

	private static final String TAG = "FlowLayout";

	public FlowLayout(Context context) {
		this(context, null);
	}

	public FlowLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public FlowLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	//测量子类
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		//容器的宽高
		int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
		int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
		int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
		int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
		
		
		//子View为wrap_content时，计算其宽高
		int width = 0;
		int height = 0;
		//记录每一行的宽高
		int lineWidth = 0;
		int lineHeight = 0;
		
		int cCount = getChildCount();
		
		for(int i=0; i<cCount; i++) {
			View child = getChildAt(i);
			//测量子View的宽高
			measureChild(child, widthMeasureSpec, heightMeasureSpec);
			MarginLayoutParams lp = (MarginLayoutParams)child.getLayoutParams(); //子View的lp是由parent决定的
			
			int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
			int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
			//行宽+子View宽>容器宽，换行
			if(childWidth + lineWidth > sizeWidth) {
				width = Math.max(width, lineWidth);
				//重置行宽
				lineWidth = childWidth;
				//记录行高
				height += lineHeight;
				lineHeight = childHeight;
			} else {
				//未换行
				lineWidth += childWidth;
				lineHeight = Math.max(lineHeight, childHeight);
			}
			//最后一个View
			if(i == cCount-1) {
				//记录行高
				width = Math.max(lineWidth, width);
				height += lineHeight;
			}
		}
		//走完View得到的Width和Height就是wrap_content情况下容器的宽高
		
		//AT_MOST表示wrap_content
		//EXACTLY表示match_parent或精确值
		setMeasuredDimension(modeWidth == MeasureSpec.EXACTLY ? sizeWidth : width,
				modeHeight == MeasureSpec.EXACTLY ? sizeHeight : height);
		
		Log.i(TAG, "width:" + sizeWidth);
		Log.i(TAG, "height:" + sizeHeight);
		System.out.println("onmeasure");
	}
	//存放全部View，一行一行存放
	private List<List<View>> mAllViews = new ArrayList<List<View>>();
	//每一行的高度
	private List<Integer> mLineHeight = new ArrayList<Integer>();
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		
		mAllViews.clear();
		mLineHeight.clear();
		//容器宽度
		int width = getWidth();
		
		int lineWidth = 0;
		int lineHeight = 0;
		List<View> lineViews = new ArrayList<View>();
		
		for(int i=0; i<getChildCount(); i++) {
			View child = getChildAt(i);
			MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
			//getWidth是在测量绘制完成以后才调用
			int childWidth = child.getMeasuredWidth();
			int childHeight = child.getMeasuredHeight();
			//换行
			if(lineWidth + childWidth + lp.leftMargin + lp.rightMargin > width) {
				mLineHeight.add(lineHeight); //记录行高
				mAllViews.add(lineViews);    //记录每行Views
				
				//重置宽高
				lineWidth = 0;
				lineHeight = childHeight + lp.topMargin + lp.bottomMargin;
				
				lineViews = new ArrayList<View>();
			}
			
			lineWidth += childWidth + lp.leftMargin + lp.rightMargin;
			lineHeight = Math.max(lineHeight, childHeight + lp.topMargin + lp.bottomMargin);
			lineViews.add(child);
		}
		
		//处理最后一个结束时
		mLineHeight.add(lineHeight);
		mAllViews.add(lineViews);
		
		//设置布局
		int left = 0;
		int top = 0;
		int line = mAllViews.size(); //几行
		for(int i=0; i<line; i++) {
			lineViews = mAllViews.get(i);
			lineHeight = mLineHeight.get(i);
			//每行几个
			for(int j=0; j<lineViews.size(); j++) {
				View child = lineViews.get(j);
				if(child.getVisibility() == View.GONE) {
					continue;
				}
				//为子View布局
				MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
				int lc = left + lp.leftMargin;
				int tc = top + lp.topMargin;
				int rc = lc + child.getMeasuredWidth();
				int bc = tc + child.getMeasuredHeight();
				child.layout(lc, tc, rc, bc);
				
				left += child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
			}
			left = 0;
			top += lineHeight;
		}
		System.out.println("onlayout");
	}
	
	/**
	 * 与当前ViewGroup对应的LayoutParameters
	 */
	@Override
	public LayoutParams generateLayoutParams(AttributeSet attrs) {
		return new MarginLayoutParams(getContext(), attrs);
	}

}
