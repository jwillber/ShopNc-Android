package top.yokey.shopnc.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

import top.yokey.base.bean.ClassChildBean;
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

public class ClassChildListAdapter extends RecyclerView.Adapter<ClassChildListAdapter.ViewHolder> {

    private final ArrayList<ClassChildBean> arrayList;
    private OnItemClickListener onItemClickListener;

    public ClassChildListAdapter(ArrayList<ClassChildBean> arrayList) {
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
        final ClassChildBean bean = arrayList.get(position);
        final ClassItemListAdapter classItemListAdapter;

        holder.nameTextView.setText(bean.getGcName());

        classItemListAdapter = new ClassItemListAdapter(bean.getChild());
        BaseApplication.get().setRecyclerView(BaseApplication.get(), holder.mainRecyclerView, classItemListAdapter);
        holder.mainRecyclerView.setLayoutManager(new GridLayoutManager(BaseApplication.get(), 3));

        classItemListAdapter.setOnItemClickListener((position1, childBean) -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(positionInt, bean, childBean);
            }
        });

        holder.nameTextView.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onClick(positionInt, bean);
            }
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup group, int viewType) {
        View view = LayoutInflater.from(group.getContext()).inflate(R.layout.item_list_class_child, group, false);
        return new ViewHolder(view);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {

        this.onItemClickListener = listener;

    }

    public interface OnItemClickListener {

        void onClick(int position, ClassChildBean bean);

        void onItemClick(int position, ClassChildBean classChildBean, ClassChildBean.ChildBean childBean);

    }

    class ViewHolder extends BaseViewHolder {

        @ViewInject(R.id.nameTextView)
        private AppCompatTextView nameTextView;
        @ViewInject(R.id.mainRecyclerView)
        private RecyclerView mainRecyclerView;

        private ViewHolder(View view) {
            super(view);
        }

    }

}
