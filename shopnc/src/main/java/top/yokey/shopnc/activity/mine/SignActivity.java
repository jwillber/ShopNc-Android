package top.yokey.shopnc.activity.mine;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import com.scrollablelayout.ScrollableLayout;

import java.util.ArrayList;

import top.yokey.base.base.BaseHttpListener;
import top.yokey.base.base.BaseSnackBar;
import top.yokey.base.bean.BaseBean;
import top.yokey.base.bean.SignLogBean;
import top.yokey.base.model.MemberSigninModel;
import top.yokey.base.util.JsonUtil;
import top.yokey.shopnc.R;
import top.yokey.shopnc.adapter.SignLogListAdapter;
import top.yokey.shopnc.base.BaseActivity;
import top.yokey.shopnc.view.PullRefreshView;

/**
 * @author MapStory
 * @ qq 1002285057
 * @ Project https://gitee.com/MapStory/ShopNc-Android
 */

public class SignActivity extends BaseActivity {

    private Toolbar mainToolbar;
    private ScrollableLayout mainScrollableLayout;
    private AppCompatTextView signTextView;
    private PullRefreshView mainPullRefreshView;

    private int pageInt;
    private SignLogListAdapter mainAdapter;
    private ArrayList<SignLogBean> mainArrayList;

    @Override
    public void initView() {

        setContentView(R.layout.activity_mine_sign);
        mainToolbar = findViewById(R.id.mainToolbar);
        mainScrollableLayout = findViewById(R.id.mainScrollableLayout);
        signTextView = findViewById(R.id.signTextView);
        mainPullRefreshView = findViewById(R.id.mainPullRefreshView);

    }

    @Override
    public void initData() {

        setToolbar(mainToolbar, "每日签到");

        pageInt = 1;
        mainArrayList = new ArrayList<>();
        mainAdapter = new SignLogListAdapter(mainArrayList);
        mainScrollableLayout.getHelper().setCurrentScrollableContainer(mainPullRefreshView.getRecyclerView());
        mainPullRefreshView.getRecyclerView().setAdapter(mainAdapter);

        checkSignin();
        signinList();

    }

    @Override
    public void initEven() {

        signTextView.setOnClickListener(view -> signinAdd());

        mainPullRefreshView.setOnClickListener(view -> {
            if (mainPullRefreshView.isFailure()) {
                pageInt = 1;
                signinList();
            }
        });

        mainPullRefreshView.setOnRefreshListener(new PullRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageInt = 1;
                signinList();
            }

            @Override
            public void onLoadMore() {
                signinList();
            }
        });

        mainAdapter.setOnItemClickListener((position, signLogBean) -> {

        });

    }

    //自定义方法

    private void checkSignin() {

        BaseSnackBar.get().showHandler(mainToolbar);

        MemberSigninModel.get().checkSignin(new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                String signin = JsonUtil.getDatasString(baseBean.getDatas(), "points_signin");
                signTextView.setText("签到\n");
                signTextView.append("+" + signin + " 积分");
                signTextView.setEnabled(true);
            }

            @Override
            public void onFailure(String reason) {
                signTextView.setText(reason);
                signTextView.setEnabled(false);
            }
        });

    }

    private void signinAdd() {

        signTextView.setEnabled(false);
        BaseSnackBar.get().show(mainToolbar, "签到中...");

        MemberSigninModel.get().signinAdd(new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                signTextView.setEnabled(false);
                signTextView.setText("已签到");
                pageInt = 1;
                signinList();
            }

            @Override
            public void onFailure(String reason) {
                signTextView.setEnabled(true);
                BaseSnackBar.get().show(mainToolbar, reason);
            }
        });

    }

    private void signinList() {

        mainPullRefreshView.setLoading();

        MemberSigninModel.get().signinList(pageInt + "", new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                if (pageInt == 1) {
                    mainArrayList.clear();
                }
                if (baseBean.isHasmore()) {
                    pageInt++;
                }
                String data = JsonUtil.getDatasString(baseBean.getDatas(), "signin_list");
                mainArrayList.addAll(JsonUtil.json2ArrayList(data, SignLogBean.class));
                mainPullRefreshView.setComplete();
            }

            @Override
            public void onFailure(String reason) {
                mainPullRefreshView.setFailure();
            }
        });

    }

}
