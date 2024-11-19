package com.studio.order.utils;

import androidx.recyclerview.widget.RecyclerView;

public abstract class HideButtonOnScrollListener extends RecyclerView.OnScrollListener {
    private static final int HIDE_THRESHOLD = 20; // Adjust this value as needed
    private int scrolledDistance = 0;
    private boolean buttonVisible = true;

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if (scrolledDistance > HIDE_THRESHOLD && buttonVisible) {
            onHide();
            buttonVisible = false;
            scrolledDistance = 0;
        } else if (scrolledDistance < -HIDE_THRESHOLD && !buttonVisible) {
            onShow();
            buttonVisible = true;
            scrolledDistance = 0;
        }

        if ((buttonVisible && dy > 0) || (!buttonVisible && dy < 0)) {
            scrolledDistance += dy;
        }
    }

    public abstract void onHide();

    public abstract void onShow();
}
