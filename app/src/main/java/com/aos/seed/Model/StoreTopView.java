package com.aos.seed.Model;

public class StoreTopView {
    String data;
    int viewType;

    public StoreTopView(String data, int viewType) {
        this.data = data;
        this.viewType = viewType;
    }

    public String getData() {
        return data;
    }

    public int getViewType() {
        return viewType;
    }
}
