package com.example.n60otaupdatedemoand.Ui.Mine.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.library.base.BaseAppActivity;
import com.example.library.widget.EmptyViewHelper;
import com.example.n60otaupdatedemoand.App.JApp;
import com.example.n60otaupdatedemoand.R;
import com.example.n60otaupdatedemoand.Ui.Mine.Adapter.HistoryAdapter;
import com.example.remoteupgradesdk.bean.UpdateVehicleTasksResBean;
import com.example.remoteupgradesdk.interfaces.ResponseCallback;

import java.util.List;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryActivity extends BaseAppActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    @BindView(R.id.rv)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    HistoryAdapter adapter;
    private int currentPage = 1;
    List<UpdateVehicleTasksResBean.ResultBean> result;
    SharedPreferences sharedPreferences;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_history;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        setTitle("历史升级记录");
        sharedPreferences= getSharedPreferences("data", Context.MODE_PRIVATE);
        initData();
        setBackClick(this);
    }

    private void initData() {
//        if (!NetworkUtils.isConnected()) {
//            ToastUtils.showShort("网络未连接");
//            return;
//        }
        refreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN);
        refreshLayout.setOnRefreshListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new HistoryAdapter(this, R.layout.item_list_history, null);
        adapter.isFirstOnly(true);
        adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        adapter.setEmptyView(new EmptyViewHelper(this).setContentView(R.mipmap.ic_no_order, "暂无信息记录"));
        recyclerView.setAdapter(adapter);
//        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
//            @Override
//            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
//            }
//        });
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                currentPage++;
                getHistoryMessage(currentPage);
            }
        }, recyclerView);

        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
                onRefresh();
            }
        });
    }
    private void getHistoryMessage(final int page) {
        setRefreshing(false);
        JApp.remoteUpdateManage.getHistoryUpdate(sharedPreferences.getString("vin","18888888888888886"), page, 10, JApp.uDate, new ResponseCallback<UpdateVehicleTasksResBean>() {
            @Override
            public void onSuccess(UpdateVehicleTasksResBean bean) {
                result = bean.getResult();
                if (result!=null){
                    if (page > 0) {
                        if (result.size() > 0) {
                            adapter.addData(result);
                            adapter.loadMoreComplete();
                        } else {
                            currentPage = page - 1;
                            adapter.loadMoreEnd();
                        }
                    } else {
                        if (result.size() > 0) {
                            adapter.setNewData(result);
                        } else {
                            adapter.setNewData(null);
                            adapter.setEmptyView(new EmptyViewHelper(HistoryActivity.this).setContentView(R.mipmap.ic_no_order, "暂无信息记录"));
                        }
                    }
                }

            }

            @Override
            public void onError(String msg) {
                setRefreshing(false);
                ToastUtils.showShort(msg);
            }
        });
    }

    public void setRefreshing(final boolean refreshing) {
        if (refreshLayout != null)
            refreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    if (refreshLayout != null)
                        refreshLayout.setRefreshing(refreshing);
                }
            });
    }

    @Override
    public void onRefresh() {
        currentPage = 1;
        getHistoryMessage(currentPage);
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
