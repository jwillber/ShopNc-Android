package top.yokey.shopnc.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

import top.yokey.base.bean.RefundBean;
import top.yokey.shopnc.R;
import top.yokey.shopnc.base.BaseApplication;
import top.yokey.shopnc.base.BaseViewHolder;

/**
 * 适配器
 *
 * @author MapStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/ShopNc-Android
 */

public class RefundListAdapter extends RecyclerView.Adapter<RefundListAdapter.ViewHolder> {

    private final ArrayList<RefundBean> arrayList;
    private OnItemClickListener onItemClickListener;

    public RefundListAdapter(ArrayList<RefundBean> arrayList) {
        this.arrayList = arrayList;
        this.onItemClickListener = null;
    }

    @Override
    public int getItemCount() {

        return arrayList.size();

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final int positionInt = position;
        final RefundBean bean = arrayList.get(position);

        GoodsRefundListAdapter goodsRefundListAdapter = new GoodsRefundListAdapter(bean.getGoodsList());
        BaseApplication.get().setRecyclerView(BaseApplication.get(), holder.mainRecyclerView, goodsRefundListAdapter);

        holder.storeNameTextView.setText(bean.getStoreName());
        holder.timeTextView.setText(bean.getAddTime());
        holder.moneyTextView.setText("￥");
        holder.moneyTextView.append(bean.getRefundAmount());

        if (bean.getAdminState().equals("无")) {
            holder.storeDescTextView.setText(bean.getSellerState());
        } else {
            holder.storeDescTextView.setText(bean.getAdminState());
        }

        goodsRefundListAdapter.setOnItemClickListener((position1, goodsListBean) -> {
            if (onItemClickListener != null) {
                onItemClickListener.onClickGoods(positionInt, position1, goodsListBean);
            }
        });

        holder.mainLinearLayout.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onClick(positionInt, bean);
            }
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup group, int viewType) {
        View view = LayoutInflater.from(group.getContext()).inflate(R.layout.item_list_refund, group, false);
        return new ViewHolder(view);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {

        this.onItemClickListener = listener;

    }

    public interface OnItemClickListener {

        void onClick(int position, RefundBean bean);

        void onClickGoods(int position, int itemPosition, RefundBean.GoodsListBean bean);

    }

    class ViewHolder extends BaseViewHolder {

        @ViewInject(R.id.mainLinearLayout)
        private LinearLayoutCompat mainLinearLayout;
        @ViewInject(R.id.storeNameTextView)
        private AppCompatTextView storeNameTextView;
        @ViewInject(R.id.storeDescTextView)
        private AppCompatTextView storeDescTextView;
        @ViewInject(R.id.mainRecyclerView)
        private RecyclerView mainRecyclerView;
        @ViewInject(R.id.timeTextView)
        private AppCompatTextView timeTextView;
        @ViewInject(R.id.moneyTextView)
        private AppCompatTextView moneyTextView;

        private ViewHolder(View view) {
            super(view);
        }

    }

}
