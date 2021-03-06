package top.yokey.shopnc.activity.home;

import androidx.appcompat.widget.Toolbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import top.yokey.base.base.BaseHttpListener;
import top.yokey.base.bean.BaseBean;
import top.yokey.base.bean.ChatListBean;
import top.yokey.base.model.MemberChatModel;
import top.yokey.base.util.JsonUtil;
import top.yokey.shopnc.R;
import top.yokey.shopnc.adapter.ChatListAdapter;
import top.yokey.shopnc.base.BaseActivity;
import top.yokey.shopnc.base.BaseApplication;
import top.yokey.shopnc.view.PullRefreshView;

/**
 * @author MapStory
 * @ qq 1002285057
 * @ Project https://gitee.com/MapStory/ShopNc-Android
 */

public class ChatListActivity extends BaseActivity {

    private Toolbar mainToolbar;
    private PullRefreshView mainPullRefreshView;

    private ChatListAdapter mainAdapter;
    private ArrayList<ChatListBean> mainArrayList;

    @Override
    public void initView() {

        setContentView(R.layout.activity_recycler_view);
        mainToolbar = findViewById(R.id.mainToolbar);
        mainPullRefreshView = findViewById(R.id.mainPullRefreshView);

    }

    @Override
    public void initData() {

        setToolbar(mainToolbar, "消息列表");

        mainArrayList = new ArrayList<>();
        mainAdapter = new ChatListAdapter(mainArrayList);
        mainPullRefreshView.getRecyclerView().setAdapter(mainAdapter);
        mainPullRefreshView.setCanLoadMore(false);

    }

    @Override
    public void initEven() {

        mainPullRefreshView.setOnClickListener(view -> {
            if (mainPullRefreshView.isFailure()) {
                getChatList();
            }
        });

        mainPullRefreshView.setOnRefreshListener(new PullRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getChatList();
            }

            @Override
            public void onLoadMore() {

            }
        });

        mainAdapter.setOnItemClickListener(new ChatListAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position, ChatListBean chatListBean) {
                BaseApplication.get().startChatOnly(getActivity(), chatListBean.getUId(), "");
            }

            @Override
            public void onDelete(int position, ChatListBean chatListBean) {

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        getChatList();
    }

    //自定义方法

    private void getChatList() {

        mainPullRefreshView.setLoading();

        MemberChatModel.get().getUserList(new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                try {
                    mainArrayList.clear();
                    String data = JsonUtil.getDatasString(baseBean.getDatas(), "list");
                    JSONObject jsonObject = new JSONObject(data);
                    Iterator iterator = jsonObject.keys();
                    while (iterator.hasNext()) {
                        String key = iterator.next().toString();
                        String value = jsonObject.getString(key);
                        mainArrayList.add(JsonUtil.json2Bean(value, ChatListBean.class));
                    }
                    mainPullRefreshView.setComplete();
                } catch (JSONException e) {
                    mainPullRefreshView.setComplete();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String reason) {
                mainPullRefreshView.setFailure();
            }
        });

    }

}
