package com.uiresource.messenger.recylcerchat;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.uiresource.messenger.R;

public class HolderLogging extends RecyclerView.ViewHolder {

    private Button btnSi, btnNo;

    public HolderLogging(View v) {
        super(v);

        btnNo = (Button) v.findViewById(R.id.btnNo);
        btnSi = (Button) v.findViewById(R.id.btnSi);

    }

    public Button getBtnNo() {
        return btnNo;
    }

    public Button getBtnSi() {
        return btnSi;
    }

    public void setBtnNo(Button btnNo) {
        this.btnNo = btnNo;
    }

    public void setBtnSi(Button btnSi) {
        this.btnSi = btnSi;
    }
}
