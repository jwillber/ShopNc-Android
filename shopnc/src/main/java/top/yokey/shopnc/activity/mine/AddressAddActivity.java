package top.yokey.shopnc.activity.mine;

import android.content.Intent;
import android.text.TextUtils;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import top.yokey.base.base.BaseHttpListener;
import top.yokey.base.base.BaseSnackBar;
import top.yokey.base.base.BaseToast;
import top.yokey.base.bean.BaseBean;
import top.yokey.base.model.MemberAddressModel;
import top.yokey.base.util.TextUtil;
import top.yokey.shopnc.R;
import top.yokey.shopnc.activity.choose.AreaActivity;
import top.yokey.shopnc.base.BaseActivity;
import top.yokey.shopnc.base.BaseApplication;
import top.yokey.shopnc.base.BaseConstant;

/**
 * @author MapStory
 * @ qq 1002285057
 * @ Project https://gitee.com/MapStory/ShopNc-Android
 */

public class AddressAddActivity extends BaseActivity {

    private Toolbar mainToolbar;
    private AppCompatEditText nameEditText;
    private AppCompatEditText mobileEditText;
    private AppCompatEditText areaEditText;
    private AppCompatEditText addressEditText;
    private SwitchCompat defaultSwitch;
    private AppCompatTextView saveTextView;

    private String cityId;
    private String areaId;
    private String areaInfo;

    @Override
    public void initView() {

        setContentView(R.layout.activity_mine_address_add);
        mainToolbar = findViewById(R.id.mainToolbar);
        nameEditText = findViewById(R.id.nameEditText);
        mobileEditText = findViewById(R.id.mobileEditText);
        areaEditText = findViewById(R.id.areaEditText);
        addressEditText = findViewById(R.id.addressEditText);
        defaultSwitch = findViewById(R.id.defaultSwitch);
        saveTextView = findViewById(R.id.saveTextView);

    }

    @Override
    public void initData() {

        setToolbar(mainToolbar, "添加地址");

        cityId = "";
        areaId = "";
        areaInfo = "";

    }

    @Override
    public void initEven() {

        areaEditText.setOnClickListener(view -> BaseApplication.get().start(getActivity(), AreaActivity.class, BaseConstant.CODE_AREA));

        saveTextView.setOnClickListener(view -> addAddress());

    }

    @Override
    public void onActivityResult(int req, int res, Intent intent) {
        super.onActivityResult(req, res, intent);
        if (res == RESULT_OK) {
            switch (req) {
                case BaseConstant.CODE_AREA:
                    cityId = intent.getStringExtra("city_id");
                    areaId = intent.getStringExtra("area_id");
                    areaInfo = intent.getStringExtra("area_info");
                    areaEditText.setText(areaInfo);
                    break;
                default:
                    break;
            }
        }
    }

    //自定义方法

    private void addAddress() {

        BaseApplication.get().hideKeyboard(getActivity());

        String name = nameEditText.getText().toString();
        String phone = mobileEditText.getText().toString();
        String address = addressEditText.getText().toString();
        String isDefault = defaultSwitch.isChecked() ? "1" : "0";

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(address)) {
            BaseSnackBar.get().show(mainToolbar, "请输入完整的信息！");
            return;
        }

        if (!TextUtil.isMobile(phone)) {
            BaseSnackBar.get().show(mainToolbar, "手机号码格式不正确！");
            return;
        }

        if (TextUtils.isEmpty(cityId)) {
            BaseSnackBar.get().show(mainToolbar, "请选择区域！");
            return;
        }

        saveTextView.setEnabled(false);
        saveTextView.setText("添加中...");

        MemberAddressModel.get().addressAdd(name, phone, cityId, areaId, address, areaInfo, isDefault, new BaseHttpListener() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                saveTextView.setEnabled(true);
                saveTextView.setText("保存地址");
                BaseToast.get().showSuccess();
                BaseApplication.get().finish(getActivity());
            }

            @Override
            public void onFailure(String reason) {
                saveTextView.setEnabled(true);
                saveTextView.setText("保存地址");
            }
        });

    }

}
