package com.xter.foldablerecyclerview;

import android.content.Context;
import android.widget.TextView;

import com.xter.foldablerecyclerview.adapter.FoldableRecyclerViewAdapter;
import com.xter.foldablerecyclerview.entity.Directory;
import com.xter.foldablerecyclerview.entity.File;

import java.util.List;

public class DirsAdapter extends FoldableRecyclerViewAdapter<Directory, File> {
	public DirsAdapter(Context mContext, int mGroupLayoutRes, int mChildLayoutRes, List<Unit<Directory, File>> mData) {
		super(mContext, mGroupLayoutRes, mChildLayoutRes, mData);
	}

	@Override
	public void onBindView(FoldableRecyclerViewAdapter.FoldableViewHolder holder, int position) {
		if (holder instanceof GroupViewHolder) {
			TextView tvName = holder.getView(R.id.tv_name);
			TextView tvSize = holder.getView(R.id.tv_num);

			Directory directory = (Directory) getItem(position);
			tvName.setText(directory.name);
			tvSize.setText(directory.size+"");
		}

		if (holder instanceof ChildViewHolder) {
			TextView tvName = holder.getView(R.id.tv_child_name);
			TextView tvSize = holder.getView(R.id.tv_child_num);

			File file = (File) getItem(position);
			tvName.setText(file.name);
			tvSize.setText(file.size+"");
		}
	}
}
