package top.yokey.shopnc.activity.choose;

import android.content.Intent;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;

import org.xutils.view.annotation.ContentView;

import java.util.ArrayList;

import top.yokey.base.base.BaseHttpListener;
import top.yokey.base.base.BaseSnackBar;
import top.yokey.base.bean.BaseBean;
import top.yokey.base.bean.InvoiceBean;
import top.yokey.base.model.MemberInvoiceModel;
import top.yokey.base.util.JsonUtil;
import top.yokey.shopnc.R;
import top.yokey.shopnc.activity.mine.InvoiceAddActivity;
import top.yokey.shopnc.adapter.InvoiceListAdapter;
import top.yokey.shopnc.base.BaseActivity;
import top.yokey.shopnc.base.BaseApplication;
import top.yokey.shopnc.base.BaseConstant;
import top.yokey.shopnc.view.PullRefreshView;

/**
 * @author MapStory
 * @ qq 1002285057
 * @ Project https://gitee.com/MapStory/ShopNc-Android
 */

@ContentView(R.layout.activity_recycler_view)
public class InvoiceActivity extends BaseActivity {

    private Toolbar mainToolbar;
    private AppCompatImageView toolbarImageView;
    private PullRefreshView mainPullRefreshView;

    private long exitTimeLong;
    private InvoiceListAdapter mainAdapter;
    private ArrayList<InvoiceBean> mainArrayList;

    @Override
    public void initView() {

        setContentView(R.layout.activity_recycler_view);
        mainToolbar = findViewById(R.id.mainToolbar);
        toolbarImageView = findViewById(R.id.toolbarImageView);
        mainPullRefreshView = findViewById(R.id.mainPullRefreshView);

    }

    @Override
    public void initData() {

        setToolbar(mainToolbar, "发票信息");
        toolbarImageView.setImageResource(R.drawable.ic_action_add);

        exitTimeLong = 0L;
        mainArrayList = new ArrayList<>();
        mainAdapter = new InvoiceListAdapter(mainArrayList);
        mainPullRefreshView.getRecyclerView().setAdapter(mainAdapter);

    }

    @Override
    public void initEven() {

        toolbarImageView.setOnClickListener(view -> BaseApplication.get().start(getActivity(), InvoiceAddActivity.class));

        mainPullRefreshView.setOnRefreshListener(new PullRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getInvoice();
            }

            @Override
            public void onLoadMore() {
                getInvoice();
            }
        });

        mainAdapter.setOnItemClickListener(new InvoiceListAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position, InvoiceBean invoiceBean) {
                Intent intent = new Intent();
                intent.putExtra(BaseConstant.DATA_ID, invoiceBean.getInvId());
                intent.putExtra(BaseConstant.DATA_CONTENT, invoiceBean.getInvTitle() + " " + invoiceBean.getInvContent());
                BaseApplication.get().finishOk(getActivity(), intent);
            }

            @Override
            public void onDelete(int position, InvoiceBean invoiceBean) {
                delInvoice(invoiceBean.getInvId());
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        getInvoice();
    }

    @Override
    public void onReturn() {

        if (System.currentTimeMillis() - exitTimeLong > BaseConstant.TIME_EXIT) {
            BaseSnackBar.get().showClickReturn(mainToolbar);
            exitTimeLong = System.currentTimeMillis();
        } else {
            super.onReturn();
        }

    }

    //自定义方法

    private void getInvoice() {

        mainPullRefreshView.setLoading();

        MemberInvoiceModel.get().invoiceList(new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                mainArrayList.clear();
                String data = JsonUtil.getDatasString(baseBean.getDatas(), "invoice_list");
                mainArrayList.addAll(JsonUtil.json2ArrayList(data, InvoiceBean.class));
                mainPullRefreshView.setComplete();
            }

            @Override
            public void onFailure(String reason) {
                BaseSnackBar.get().show(mainToolbar, reason);
            }
        });

    }

    private void delInvoice(String invId) {

        BaseSnackBar.get().showHandler(mainToolbar);

        MemberInvoiceModel.get().invoiceDel(invId, new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                getInvoice();
            }

            @Override
            public void onFailure(String reason) {
                BaseSnackBar.get().show(mainToolbar, reason);
            }
        });

    }

}
