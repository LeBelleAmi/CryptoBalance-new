package com.lebelle.cryptobalance.model;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lebelle.cryptobalance.R;

/**
 * Created by Omawumi Eyekpimi on 12-Oct-17.
 */

public class DividerItemDecorator extends RecyclerView.ItemDecoration{

    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;

    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

    Context mContext;
    Drawable mDivider;
    private int mOrientation;
    public DividerItemDecorator(Context mContext, int orientation ){
        this.mContext = mContext;
        mDivider = ContextCompat.getDrawable(mContext, R.drawable.divider);
        setOrientation(orientation);
    }

    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
            throw new IllegalArgumentException("invalid orientation");
        }
        mOrientation = orientation;
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {

        if (mOrientation == VERTICAL_LIST) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    public void drawVertical(Canvas c, RecyclerView parent) {
        int dividerLeft = (int)mContext.getResources().getDimension(R.dimen.list_left_margin);
        int dividerRight = parent.getWidth() - (int)mContext.getResources().getDimension(R.dimen.activity_horizontal_margin);
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount - 1; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int dividerTop = child.getBottom() + params.bottomMargin;
            int dividerBottom = dividerTop + mDivider.getIntrinsicHeight();
            mDivider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom);
            mDivider.draw(c);
        }
    }

    public void drawHorizontal(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }


}
