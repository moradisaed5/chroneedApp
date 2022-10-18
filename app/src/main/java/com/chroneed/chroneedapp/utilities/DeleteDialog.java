package com.chroneed.chroneedapp.utilities;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import com.chroneed.chroneedapp.R;

public class DeleteDialog {
    Context context;
    private Dialog dialog;

    public DeleteDialog(Context context) {
        this.context = context;
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.layout_dialog_delete);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
    }

    public void showDialog() {
        dialog.show();
    }

    public void closeDialog() {
        dialog.dismiss();
    }
}