package io.github.boldijar.cosasapp.base;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * @author Paul
 * @since 2018.10.13
 */
public abstract class FastAdapter<Type, Holder extends FastAdapter.AbstractHolder> extends RecyclerView.Adapter<Holder> {

    private List<Type> mItems = new ArrayList<>();

    public void clear() {
        mItems.clear();
        notifyDataSetChanged();
    }

    public void add(List<Type> items) {
        mItems.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return (Holder) createHolder(parent);
    }

    protected abstract AbstractHolder createHolder(ViewGroup parent);

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.bind(mItems.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void addItem(Type item) {
        mItems.add(item);
        notifyItemInserted(mItems.size() - 1);
    }

    public void removeItem(Type item) {
        for (int i = 0; i < mItems.size(); i++) {
            Type anItem = mItems.get(i);
            if (anItem.equals(item)) {
                mItems.remove(anItem);
                notifyItemRemoved(i);
                return;
            }
        }
    }

    public List<Type> getItems() {
        return mItems;
    }

    public static class AbstractHolder<T> extends RecyclerView.ViewHolder {

        public AbstractHolder(ViewGroup parent, int layoutId) {
            super(LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false));
            ButterKnife.bind(this, itemView);
        }

        public void bind(T item) {
        }

        public void bind(T item, int position) {
            bind(item);
        }

    }
}
