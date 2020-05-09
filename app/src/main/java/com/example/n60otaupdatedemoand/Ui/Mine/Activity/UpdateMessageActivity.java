package com.example.n60otaupdatedemoand.Ui.Mine.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.example.library.base.BaseAppActivity;
import com.example.library.widget.AlertDialogView;
import com.example.library.widget.ProgressDialogView;
import com.example.library.widget.listener.ChooseOptionCallBack;
import com.example.n60otaupdatedemoand.App.JApp;
import com.example.n60otaupdatedemoand.R;
import com.example.remoteupgradesdk.bean.CurrentVehicleTaskResBean;
import com.example.remoteupgradesdk.bean.UpdateConfirInterfaceResBean;
import com.example.remoteupgradesdk.bean.UpdateProgressBean;
import com.example.remoteupgradesdk.bean.UpdateResultBean;
import com.example.remoteupgradesdk.bean.WebStateBean;
import com.example.remoteupgradesdk.interfaces.ResponseCallback;

import butterknife.BindView;
import butterknife.OnClick;

public class UpdateMessageActivity extends BaseAppActivity implements View.OnClickListener {

    CurrentVehicleTaskResBean.ResultBean currentVehicleTaskResBean;
    WebStateBean.ResultBean webStateBean;
    SharedPreferences sharedPreferences;
    @BindView(R.id.tasekId)
    TextView tasekId;
    @BindView(R.id.taskType)
    TextView taskType;
    @BindView(R.id.stateTv)
    TextView stateTv;
    @BindView(R.id.nVerTV)
    TextView nVerTV;
    @BindView(R.id.sizeTV)
    TextView sizeTV;
    @BindView(R.id.timeTV)
    TextView timeTV;
    @BindView(R.id.starTV)
    TextView starTV;
    @BindView(R.id.desTV)
    TextView desTV;
    @BindView(R.id.progress)
    ProgressBar progress;
    @BindView(R.id.progressTV)
    TextView progressTV;
    @BindView(R.id.line1)
    LinearLayout line1;
    @BindView(R.id.startBtn)
    Button startBtn;


    private boolean isbool = false;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            JApp.remoteUpdateManage.queryState(sharedPreferences.getString("vin", "LBQTEST2019012593"), JApp.taskId, JApp.uDate, new ResponseCallback<WebStateBean>() {
                @Override
                public void onSuccess(WebStateBean bean) {
                    webStateBean = bean.getResult();
                    switch (bean.getResult().getStatus()) {
                        case 0:
                            stateTv.setText(R.string.upgrade_tasks);
                            break;
                        case 1:
                            stateTv.setText(R.string.user_download);
                            break;
                        case 2:
                            stateTv.setText(R.string.user_agree);
                            break;
                        case 3:
                            stateTv.setText(R.string.user_no_agree);
                            break;
                        case 4:
                            stateTv.setText(R.string.download_file);
                            break;
                        case 5:
                            stateTv.setText(R.string.download_success);
                            break;
                        case 6:
                            stateTv.setText(R.string.download_erro);
                            break;
                        case 7:
                            stateTv.setText(R.string.user_update);
                            break;
                        case 8:
                            stateTv.setText(R.string.user_agree_update);
                            break;
                        case 9:
                            stateTv.setText(R.string.user_no_agree_update);
                            break;
                        case 10:
                            if (isbool == false) {
                                ProgressDialogView.dismiss();
                                line1.setVisibility(View.VISIBLE);
                                stateTv.setText(R.string.updateing);
                                JApp.remoteUpdateManage.queryUpdateProgress(sharedPreferences.getString("vin", "LBQTEST2019012593"), JApp.taskId, JApp.uDate, new ResponseCallback<UpdateProgressBean>() {
                                    @Override
                                    public void onSuccess(UpdateProgressBean bean) {
                                        progress.setProgress(bean.getResult().getProgress());
                                        progressTV.setText(bean.getResult().getProgress() + "%");
                                    }

                                    @Override
                                    public void onError(String msg) {
                                        ToastUtils.showShort(msg);
                                    }
                                });
                            }
                            isbool = true;
                            break;
                        case 11:
                            line1.setVisibility(View.GONE);
                            stateTv.setText(R.string.update_ending);
                            JApp.remoteUpdateManage.queryUpdateResult(sharedPreferences.getString("vin", "LBQTEST2019012593"), JApp.taskId, JApp.uDate, new ResponseCallback<UpdateResultBean>() {
                                @Override
                                public void onSuccess(UpdateResultBean bean) {
                                    switch (bean.getResult().getResult()) {
                                        case 0:
                                            showDialog("升级结束，升级失败！！！");
                                            break;
                                        case 1:
                                            showDialog("升级结束,升级成功！！！");
                                            break;
                                    }
                                }

                                @Override
                                public void onError(String msg) {

                                }
                            });
                            handler.removeCallbacks(runnable);
                            break;
                    }

                    switch (bean.getResult().getResultCode()) {
                        case 5354:
                            showDialog("升级失败");
                            handler.removeCallbacks(runnable);
                            break;
                        case 5352:
                            showDialog("安装失败，回滚成功");
                            handler.removeCallbacks(runnable);
                            break;
                        case 5351:
                            showDialog("安装失败，回滚失败");
                            handler.removeCallbacks(runnable);
                            break;
                        case 5355:
                            showDialog("回滚成功版本不匹配");
                            handler.removeCallbacks(runnable);
                            break;
                        case 5311:
                            showDialog("下载时出现一般错误");
                            handler.removeCallbacks(runnable);
                            break;
                        case 5310:
                            showDialog("下载时出现网络错误");
                            handler.removeCallbacks(runnable);
                            break;
                        case 5400:
                            showDialog("用户取消下载");
                            handler.removeCallbacks(runnable);
                            break;
                        case 5320:
                            showDialog("升级包校验失败");
                            handler.removeCallbacks(runnable);
                            break;
                        case 5333:
                            showDialog("升级包所在存储空间不足");
                            handler.removeCallbacks(runnable);
                            break;

                    }
                }

                @Override
                public void onError(String msg) {
                    ToastUtils.showShort(msg);
                }
            });

        }
    };
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            handler.sendEmptyMessage(0x123);
            handler.postDelayed(runnable, 2000);
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_update_message;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        setTitle("系统升级");
        setBackClick(this);
        sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        JApp.remoteUpdateManage.getCarUpdateTask(sharedPreferences.getString("vin", "LBQTEST2019012593"), JApp.taskId, JApp.uDate, new ResponseCallback<CurrentVehicleTaskResBean>() {
            @Override
            public void onSuccess(CurrentVehicleTaskResBean bean) {
                currentVehicleTaskResBean = bean.getResult();
                JApp.taskId = bean.getResult().getTaskCarId();
                initView();
            }

            @Override
            public void onError(String msg) {
                ToastUtils.showShort(msg);
            }
        });
        handler.post(runnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        JApp.taskId = "";
    }

    private void showDialog(String msg) {
        AlertDialogView.getInstance(this).show("系统提示", msg, "确认", null, new ChooseOptionCallBack() {
            @Override
            public void chooseOption(int var1) {
                switch (var1) {
                    case 1:
                        finish();
                        break;
                }
            }
        });
    }

    private void initView() {
        tasekId.setText(currentVehicleTaskResBean.getTaskCarId());
        switch (currentVehicleTaskResBean.getTaskType()) {
            case 0:
                taskType.setText("标准升级");
                break;
            case 1:
                taskType.setText("强制升级");
                break;
        }
        nVerTV.setText(currentVehicleTaskResBean.getNVer());
        sizeTV.setText(currentVehicleTaskResBean.getSize() + "");
        timeTV.setText(currentVehicleTaskResBean.getDuration() + "");
        starTV.setText(currentVehicleTaskResBean.getTime());
        desTV.setText(currentVehicleTaskResBean.getDescription());
    }

    @Override
    public void onClick(View v) {
        finish();
    }


    @OnClick(R.id.startBtn)
    public void onViewClicked() {

        if (webStateBean != null && webStateBean.getStatus() == 7) {
            JApp.remoteUpdateManage.confirmUpgrade(JApp.taskId, JApp.uDate, 1, 0, new ResponseCallback<UpdateConfirInterfaceResBean>() {
                @Override
                public void onSuccess(UpdateConfirInterfaceResBean bean) {
                    if (bean.getCode() != 1) {
                        ProgressDialogView.dismiss();
                        ToastUtils.showShort("确认升级发送失败，请重试");
                    }

                }

                @Override
                public void onError(String msg) {
                    ProgressDialogView.dismiss();
                    ToastUtils.showShort(msg);

                }
            });
            ProgressDialogView.show(this, "系统提示", "车机准备中，请稍后。。。", false);
        } else {
            ToastUtils.showShort("请确认车机当前状态后再点击！！！");
        }
    }

}
