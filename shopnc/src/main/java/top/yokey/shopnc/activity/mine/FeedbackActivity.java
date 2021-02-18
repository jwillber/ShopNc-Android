package top.yokey.shopnc.activity.mine;

import android.text.TextUtils;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import top.yokey.base.base.BaseHttpListener;
import top.yokey.base.base.BaseSnackBar;
import top.yokey.base.base.BaseToast;
import top.yokey.base.bean.BaseBean;
import top.yokey.base.model.MemberFeedbackModel;
import top.yokey.base.util.JsonUtil;
import top.yokey.shopnc.R;
import top.yokey.shopnc.base.BaseActivity;
import top.yokey.shopnc.base.BaseApplication;

/**
 * @author MapStory
 * @ qq 1002285057
 * @ Project https://gitee.com/MapStory/ShopNc-Android
 */

public class FeedbackActivity extends BaseActivity {

    private Toolbar mainToolbar;
    private AppCompatEditText contentEditText;
    private AppCompatTextView submitTextView;

    @Override
    public void initView() {

        setContentView(R.layout.activity_mine_feedback);
        mainToolbar = findViewById(R.id.mainToolbar);
        contentEditText = findViewById(R.id.contentEditText);
        submitTextView = findViewById(R.id.submitTextView);

    }

    @Override
    public void initData() {

        setToolbar(mainToolbar, "用户反馈");

    }

    @Override
    public void initEven() {

        submitTextView.setOnClickListener(view -> feedback());

    }

    //自定义方法

    private void feedback() {

        BaseApplication.get().hideKeyboard(getActivity());

        String feedback = contentEditText.getText().toString();
        if (TextUtils.isEmpty(feedback)) {
            BaseSnackBar.get().show(mainToolbar, "请输入内容...");
            return;
        }

        submitTextView.setEnabled(false);
        submitTextView.setText("提交中...");

        MemberFeedbackModel.get().feedbackAdd(feedback, new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                if (JsonUtil.isSuccess(baseBean.getDatas())) {
                    BaseToast.get().showSuccess();
                    BaseApplication.get().finish(getActivity());
                } else {
                    submitTextView.setEnabled(true);
                    submitTextView.setText("提 交");
                    BaseSnackBar.get().showFailure(mainToolbar);
                }
            }

            @Override
            public void onFailure(String reason) {
                submitTextView.setEnabled(true);
                submitTextView.setText("提 交");
                BaseSnackBar.get().show(mainToolbar, reason);
            }
        });

    }

}
