package com.example.n60otaupdatedemoand.Ui.Mine.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.example.library.base.BaseAppFragment;
import com.example.library.widget.ProgressDialogView;
import com.example.n60otaupdatedemoand.App.JApp;
import com.example.n60otaupdatedemoand.R;
import com.example.n60otaupdatedemoand.Ui.Mine.Activity.HistoryActivity;
import com.example.n60otaupdatedemoand.Ui.Mine.Activity.UpdateMessageActivity;
import com.example.remoteupgradesdk.bean.VerIformationBean;
import com.example.remoteupgradesdk.bean.WebStateBean;
import com.example.remoteupgradesdk.interfaces.ResponseCallback;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingFragment extends BaseAppFragment {


    @BindView(R.id.cverTv)
    TextView cverTv;
    @BindView(R.id.updateBtn)
    Button updateBtn;
    @BindView(R.id.historyBtn)
    Button historyBtn;
    @BindView(R.id.setVin)
    Button setVin;

    ProgressDialog dialog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_setting;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        setTitle("OTA升级服务");
        isShowBack(false);

        sharedPreferences= getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        ProgressDialogView.show(getContext(), "系统提示", "信息加载中。。。", false);
        JApp.remoteUpdateManage.getVersionInformation(sharedPreferences.getString("vin","LBQTEST2019012593"), JApp.uDate, new ResponseCallback<VerIformationBean>() {
            @Override
            public void onSuccess(VerIformationBean bean) {
                ProgressDialogView.dismiss();
                cverTv.setText(bean.getResult().getCVer().toString());
            }

            @Override
            public void onError(String msg) {
                ToastUtils.showShort(msg);
            }
        });
        dialog = new ProgressDialog(getContext());
        dialog.setTitle("系统提示");
        dialog.setMessage("升级资源检查中,请稍后...");
        dialog.setCancelable(true);
        dialog.setIndeterminate(false);
    }

    @OnClick({R.id.updateBtn, R.id.historyBtn,R.id.setVin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.updateBtn:
                ProgressDialogView.show(getContext(), "系统提示", "升级资源检查中,请稍后...", false);
                JApp.remoteUpdateManage.queryState(sharedPreferences.getString("vin","LBQTEST2019012593"), JApp.taskId, JApp.uDate, new ResponseCallback<WebStateBean>() {
                    @Override
                    public void onSuccess(WebStateBean bean) {
                        if (bean.getResult().getStatus() == -1) {
                            ProgressDialogView.dismiss();
                            ToastUtils.showShort("暂无更新");
                        } else {
                            ProgressDialogView.dismiss();
                            startActivity(new Intent(getActivity(), UpdateMessageActivity.class));
                        }

                    }

                    @Override
                    public void onError(String msg) {
                        ToastUtils.showShort(msg);
                    }
                });
                break;
            case R.id.historyBtn:
                startActivity(new Intent(getActivity(), HistoryActivity.class));
                break;
            case R.id.setVin:
                AlertDialog.Builder builder= new AlertDialog.Builder(getContext());
                View viewVin= LayoutInflater.from(getContext()).inflate(R.layout.dialog_vin_set_layout, null);
                TextView cancel =viewVin.findViewById(R.id.choosepage_cancel);
                TextView sure =viewVin.findViewById(R.id.choosepage_sure);
                final EditText edittext =viewVin.findViewById(R.id.choosepage_edittext);
                final Dialog dialog= builder.create();
                dialog.show();
                dialog.getWindow().setContentView(viewVin);
                dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                sure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), "Vin码配置成功", Toast.LENGTH_SHORT).show();
                        editor.putString("vin",edittext.getText().toString());
                        editor.commit();
                        dialog.dismiss();
                    }
                });


                break;
        }
    }
}
