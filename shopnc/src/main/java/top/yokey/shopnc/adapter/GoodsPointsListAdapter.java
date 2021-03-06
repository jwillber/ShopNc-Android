package top.yokey.shopnc.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

import top.yokey.base.bean.GoodsPointsListBean;
import top.yokey.shopnc.R;
import top.yokey.shopnc.base.BaseApplication;
import top.yokey.shopnc.base.BaseImageLoader;
import top.yokey.shopnc.base.BaseViewHolder;

/**
 * 适配器
 *
 * @author MapStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/ShopNc-Android
 */

public class GoodsPointsListAdapter extends RecyclerView.Adapter<GoodsPointsListAdapter.ViewHolder> {

    private final boolean isGridModel;
    private final ArrayList<GoodsPointsListBean> arrayList;
    private OnItemClickListener onItemClickListener;

    public GoodsPointsListAdapter(ArrayList<GoodsPointsListBean> arrayList, boolean isGridModel) {
        this.arrayList = arrayList;
        this.isGridModel = isGridModel;
        this.onItemClickListener = null;
    }

    @Override
    public int getItemCount() {

        return arrayList.size();

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final int positionInt = position;
        final GoodsPointsListBean bean = arrayList.get(position);

        int width = BaseApplication.get().getWidth() / 2 - 16;
        BaseImageLoader.get().displayRadius(bean.getPgoodsImage(), holder.mainImageView);
        ViewGroup.LayoutParams layoutParams = holder.mainImageView.getLayoutParams();
        if (isGridModel) {
            layoutParams.width = width;
            //noinspection SuspiciousNameCombination
            layoutParams.height = width;
        }
        holder.mainImageView.setLayoutParams(layoutParams);
        holder.nameTextView.setText(bean.getPgoodsName());
        holder.moneyTextView.setText("积分兑换：");
        holder.moneyTextView.append(bean.getPgoodsPoints());
        holder.moneyTextView.append(" 分");
        holder.stockTextView.setText("库存：");
        holder.stockTextView.append(bean.getPgoodsStorage());

        holder.mainRelativeLayout.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onClick(positionInt, bean);
            }
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup group, int viewType) {
        View view;
        if (isGridModel) {
            view = LayoutInflater.from(group.getContext()).inflate(R.layout.item_list_goods_points, group, false);
        } else {
            view = LayoutInflater.from(group.getContext()).inflate(R.layout.item_list_goods_points_ver, group, false);
        }
        return new ViewHolder(view);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {

        this.onItemClickListener = listener;

    }

    public interface OnItemClickListener {

        void onClick(int position, GoodsPointsListBean bean);

    }

    class ViewHolder extends BaseViewHolder {

        @ViewInject(R.id.mainRelativeLayout)
        private RelativeLayout mainRelativeLayout;
        @ViewInject(R.id.mainImageView)
        private AppCompatImageView mainImageView;
        @ViewInject(R.id.nameTextView)
        private AppCompatTextView nameTextView;
        @ViewInject(R.id.moneyTextView)
        private AppCompatTextView moneyTextView;
        @ViewInject(R.id.stockTextView)
        private AppCompatTextView stockTextView;

        private ViewHolder(View view) {
            super(view);
        }

    }

}
