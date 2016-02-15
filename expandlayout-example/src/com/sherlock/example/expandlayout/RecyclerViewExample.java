package com.sherlock.example.expandlayout;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sherlock.expandlayout.ExpandableLayout;
import com.sherlock.expandlayout.ExpandableLayout.OnExpandListener;

public class RecyclerViewExample extends Activity {

	private List<Item> mItems;
	private RecyclerView mRecyclerView;
	private MyAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_recyclerview);

		mItems = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			mItems.add(new Item());
		}
		mRecyclerView = (RecyclerView) this.findViewById(R.id.recyclerview);
		mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
		mAdapter = new MyAdapter(mItems);
		mRecyclerView.setAdapter(mAdapter);
	}

	private OnClickListener mOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			MyViewHolder holder = (MyViewHolder) v.getTag();
			boolean result = holder.expandableLayout.toggleExpansion();
			Item item = mItems.get(holder.getAdapterPosition());
			item.isExpand = result ? !item.isExpand : item.isExpand;
		}
	};

	private OnExpandListener mOnExpandListener = new OnExpandListener() {

		private boolean isScrollingToBottom = false;

		@Deprecated
		@Override
		public void onToggle(ExpandableLayout view, View child,
				boolean isExpanded) {
		}

		@Override
		public void onExpandOffset(ExpandableLayout view, View child,
				float offset, boolean isExpanding) {
			final MyViewHolder holder = (MyViewHolder) view.getTag();
			if (holder.getAdapterPosition() == mItems.size() - 1) {
				if (!isScrollingToBottom) {
					isScrollingToBottom = true;
					mRecyclerView.postDelayed(new Runnable() {

						@Override
						public void run() {
							isScrollingToBottom = false;
							mRecyclerView.scrollToPosition(holder
									.getAdapterPosition());
						}
					}, 100);
				}
			}
		}
	};

	private class MyAdapter extends
			RecyclerView.Adapter<RecyclerView.ViewHolder> {
		private List<Item> items;

		public MyAdapter(List<Item> infos) {
			this.items = infos;
		}

		@Override
		public int getItemCount() {
			return items.size();
		}

		@Override
		public void onBindViewHolder(RecyclerView.ViewHolder holder,
				int position) {
			MyViewHolder viewHolder = (MyViewHolder) holder;
			Item item = items.get(position);
			viewHolder.expandableLayout.setExpanded(item.isExpand, false);
		}

		@Override
		public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
				int viewType) {
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			View itemView = inflater.inflate(R.layout.item_listview, parent,
					false);
			MyViewHolder holder = new MyViewHolder(itemView);
			holder.imageView.setOnClickListener(mOnClickListener);
			holder.imageView.setTag(holder);
			holder.expandableLayout.setTag(holder);
			holder.expandableLayout.setOnExpandListener(mOnExpandListener);
			return holder;
		}
	}

	static class MyViewHolder extends RecyclerView.ViewHolder {
		ExpandableLayout expandableLayout;
		ImageView imageView;

		public MyViewHolder(View itemView) {
			super(itemView);
			expandableLayout = (ExpandableLayout) itemView
					.findViewById(R.id.expandlayout);
			imageView = (ImageView) itemView.findViewById(R.id.imageview);
		}
	}


	static class Item {
		boolean isExpand;
	}
}