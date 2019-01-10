package com.android.htec.widgettest;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class OnNextWeekListener  extends RecyclerView.OnScrollListener {

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            int itemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
            if (itemPosition != -1) {
                onNewWeek(itemPosition);
            }

        }
    }

    public abstract void onNewWeek(int weekItemPosition);
}