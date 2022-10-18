package com.chroneed.chroneedapp.utilities;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;

import com.airbnb.lottie.LottieAnimationView;
import com.chroneed.chroneedapp.R;

public class MyProgressDialog {

    Context context;
    LottieAnimationView lottieAnimationView;
    private Dialog dialog;


    public MyProgressDialog(Context context, int RawResAnimation) {
        String s="";
        try {
            this.context = context;
            dialog = new Dialog(context);
            dialog.setContentView(R.layout.layout_progress_dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            lottieAnimationView = dialog.findViewById(R.id.lt_progress);
            dialog.setCancelable(false);
            lottieAnimationView.setAnimation(RawResAnimation);
        } catch (Exception ex) {
           s  = ex.getMessage();
        }

    }

    public void showDialog() {
        dialog.show();
    }

    public void closeDialog() {
        dialog.dismiss();
    }

}