package com.example.n60otaupdatedemoand.Ui.Mine.Adapter;

import android.content.Context;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.n60otaupdatedemoand.R;
import com.example.remoteupgradesdk.bean.UpdateVehicleTasksResBean;

import java.util.List;

import androidx.annotation.Nullable;

public class HistoryAdapter extends BaseQuickAdapter<UpdateVehicleTasksResBean.ResultBean, BaseViewHolder> {
    Context context;

    public HistoryAdapter(Context context, int layoutResId, @Nullable List<UpdateVehicleTasksResBean.ResultBean> data) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, UpdateVehicleTasksResBean.ResultBean item) {
        helper.setText(R.id.titleTv, "0" + item.getIndex()).setText(R.id.tasekId, item.getTaskCarId()).setText(R.id.des, item.getDescription()).setText(R.id.endTime, item.getUpgradeDate());
        TextView stateTv = (TextView) helper.getView(R.id.desState);
        switch (item.getStatus()) {
            case 0:
                stateTv.setText("升级失败");
                break;
            case 1:
                stateTv.setText("升级成功");
                break;
            case -1:
                stateTv.setText("升级未完成");
                break;
        }

    }
}