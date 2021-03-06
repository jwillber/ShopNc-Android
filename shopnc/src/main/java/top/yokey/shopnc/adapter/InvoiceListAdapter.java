package top.yokey.shopnc.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

import top.yokey.base.bean.InvoiceBean;
import top.yokey.shopnc.R;
import top.yokey.shopnc.base.BaseViewHolder;

/**
 * 适配器
 *
 * @author MapStory
 * @ qq 1002285057
 * @ project https://gitee.com/MapStory/ShopNc-Android
 */

public class InvoiceListAdapter extends RecyclerView.Adapter<InvoiceListAdapter.ViewHolder> {

    private final ArrayList<InvoiceBean> arrayList;
    private OnItemClickListener onItemClickListener;

    public InvoiceListAdapter(ArrayList<InvoiceBean> arrayList) {
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
        final InvoiceBean bean = arrayList.get(position);

        holder.titleTextView.setText(bean.getInvTitle());
        holder.titleTextView.append(" ");
        holder.titleTextView.append(bean.getInvContent());

        holder.deleteImageView.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onDelete(positionInt, bean);
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
        View view = LayoutInflater.from(group.getContext()).inflate(R.layout.item_list_invoice, group, false);
        return new ViewHolder(view);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {

        this.onItemClickListener = listener;

    }

    public interface OnItemClickListener {

        void onClick(int position, InvoiceBean bean);

        void onDelete(int position, InvoiceBean bean);

    }

    class ViewHolder extends BaseViewHolder {

        @ViewInject(R.id.mainLinearLayout)
        private LinearLayoutCompat mainLinearLayout;
        @ViewInject(R.id.titleTextView)
        private AppCompatTextView titleTextView;
        @ViewInject(R.id.deleteImageView)
        private AppCompatImageView deleteImageView;

        private ViewHolder(View view) {
            super(view);
        }

    }

}
