package com.xter.foldablerecyclerview.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author XTER
 * @date 2019/8/9
 */
public abstract class FoldableRecyclerViewAdapter<K, V> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private Context mContext;

	/**
	 * 上级布局
	 */
	private int mGroupLayoutRes;
	/**
	 * 下级布局
	 */
	private int mChildLayoutRes;

	/**
	 * 数据
	 */
	private List<Unit<K, V>> mData;

	/**
	 * 点击与长按监听接口
	 */
	public interface OnItemClickLitener {
		void onItemClick(View view, int position);

		void onItemLongClick(View view, int position);
	}


	private OnItemClickLitener itemClickLitener;

	public void setOnItemClickLitener(OnItemClickLitener itemClickLitener) {
		this.itemClickLitener = itemClickLitener;
	}

	public FoldableRecyclerViewAdapter(Context mContext, int mGroupLayoutRes, int mChildLayoutRes, List<Unit<K, V>> mData) {
		this.mContext = mContext;
		this.mGroupLayoutRes = mGroupLayoutRes;
		this.mChildLayoutRes = mChildLayoutRes;
		if (mData == null) {
			this.mData = new ArrayList<>();
		} else {
			this.mData = mData;
		}
	}

	@Override
	public int getItemCount() {
		if (mSize == 0) {
			int totalSize = 0;
			for (Unit unit : mData) {
				totalSize += (unit.folded ? 1 : unit.children.size() + 1);
			}
			mSize = totalSize;
		}
//		System.out.println("itemCount="+mSize);
		return mSize;
	}

	private int mSize = 0;

	@Override
	public int getItemViewType(int position) {
		//通过位置判断type，因为数据传入后顺序不变，可通过数据来判断当前位置是哪一类数据
		int currentPosition = -1;
		for (Unit unit : mData) {
			if (unit.folded) {
				currentPosition = currentPosition + 1;
				if (currentPosition == position) {
					return FoldableViewHolder.GROUP;
				}
			} else {
				//算上group
				currentPosition = currentPosition + 1;
				if (currentPosition == position) {
					return FoldableViewHolder.GROUP;
				}
				//算上children，通过比较大小确定是否是当前Unit中的child
				currentPosition = currentPosition + unit.children.size();
				if (position <= currentPosition) {
					return FoldableViewHolder.CHILD;
				}
			}

		}
		return FoldableViewHolder.GROUP;
	}

	/**
	 * 根据索引返回Unit中的K或V
	 *
	 * @param position 索引
	 * @return K/V
	 */
	public Object getItem(int position) {
		int currentPosition = -1;
		for (Unit unit : mData) {
			if (unit.folded) {
				currentPosition = currentPosition + 1;
				if (currentPosition == position) {
					return unit.group;
				}
			} else {
				//算上group
				currentPosition = currentPosition + 1;
				if (currentPosition == position) {
					return unit.group;
				}
				//算上children，通过计算确定是当前Unit的child的索引
				currentPosition = currentPosition + unit.children.size();
				if (position <= currentPosition) {
					int unitChildIndex = unit.children.size() - 1 - (currentPosition - position);
					return unit.children.get(unitChildIndex);
				}
			}
		}
		return null;
	}

	/**
	 * 根据索引确定返回某个数据集
	 *
	 * @param position 索引
	 * @return Unit
	 */
	private Unit<K, V> getUnit(int position) {
		int currentPosition = -1;
		for (Unit<K, V> unit : mData) {
			//算上group
			currentPosition += unit.folded ? 1 : unit.children.size() + 1;
			if (position <= currentPosition)
				return unit;
		}
		return null;
	}

	@NonNull
	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
		if (viewType == FoldableViewHolder.CHILD) {
			return new ChildViewHolder(LayoutInflater.from(mContext).inflate(mChildLayoutRes, viewGroup, false));
		}
		return new GroupViewHolder(LayoutInflater.from(mContext).inflate(mGroupLayoutRes, viewGroup, false));
	}

	@Override
	public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int position) {

		viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				System.out.println("click="+viewHolder.getAdapterPosition());
				if (viewHolder instanceof GroupViewHolder) {
					Unit<K, V> unit = getUnit(viewHolder.getAdapterPosition());
					unit.folded = !unit.folded;
					mSize = 0;
//					notifyDataSetChanged();//最准确，但数据多时性能有影响
//					notifyItemRangeChanged(viewHolder.getAdapterPosition()+1,getItemCount());//需要考虑到holder的旧索引问题，暂无太好的办法去规避
					if (unit.folded) {
						notifyItemRangeRemoved(viewHolder.getAdapterPosition() + 1, unit.children.size());
					} else {
						notifyItemRangeInserted(viewHolder.getAdapterPosition() + 1, unit.children.size());
					}
				}
				if (itemClickLitener != null) {
					itemClickLitener.onItemClick(viewHolder.itemView, viewHolder.getLayoutPosition());
				}
			}
		});
		viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				if (itemClickLitener != null) {
					itemClickLitener.onItemLongClick(viewHolder.itemView, viewHolder.getLayoutPosition());
				}
				return true;
			}
		});

		onBindView((FoldableViewHolder) viewHolder, position);
	}

	public abstract void onBindView(FoldableViewHolder holder, int position);


	/*------------------ 关于数据的增删改  ------------------*/


	public void add(Unit<K, V> element) {
		mData.add(element);
		mSize = 0;
		notifyDataSetChanged();
	}

	public void add(List<Unit<K, V>> elemList) {
		mData.addAll(elemList);
		mSize = 0;
		notifyDataSetChanged();
	}

	public void remove(Unit<K, V> elem) {
		mData.remove(elem);
		mSize = 0;
		notifyDataSetChanged();
	}

	public void replace(List<Unit<K, V>> elemList) {
		mData.clear();
		mData.addAll(elemList);
		mSize = 0;
		notifyDataSetChanged();
	}

	public void itemRemove(int position) {
		if (position < 0 || position > mSize - 1) {
			return;
		}
		Unit unit = getUnit(position);
		Object item = getItem(position);
		if (item == unit.group) {
			mData.remove(unit);
			if (unit.folded) {
				//折叠状态，只动父级
				mSize = mSize - 1;
			} else {
				mSize = mSize - 1 - unit.children.size();
			}
		} else {
			unit.children.remove(item);
			mSize = mSize - 1;
		}
		notifyDataSetChanged();
	}

	/*------------------ 一些准备工作，定义数据或Holder之类  ------------------*/

	protected static abstract class FoldableViewHolder extends RecyclerView.ViewHolder {

		static final int GROUP = 0;
		static final int CHILD = 1;

		private SparseArray<View> views = new SparseArray<>();
		private View convertView;

		public FoldableViewHolder(@NonNull View itemView) {
			super(itemView);
			this.convertView = itemView;
		}

		@SuppressWarnings("unchecked")
		public <T extends View> T getView(int resId) {
			View v = views.get(resId);
			if (null == v) {
				v = convertView.findViewById(resId);
				views.put(resId, v);
			}
			return (T) v;
		}
	}

	protected static class GroupViewHolder extends FoldableViewHolder {

		public GroupViewHolder(@NonNull View itemView) {
			super(itemView);
		}

	}

	protected static class ChildViewHolder extends FoldableViewHolder {

		public ChildViewHolder(@NonNull View itemView) {
			super(itemView);
		}
	}

	/**
	 * 数据实体，一对多
	 *
	 * @param <K>
	 * @param <V>
	 */
	public static class Unit<K, V> {
		public K group;
		public List<V> children;
		public boolean folded = true;

		public Unit(K group, List<V> children) {
			this.group = group;
			if (children == null) {
				this.children = new ArrayList<>();
			} else {
				this.children = children;
			}
		}

		public Unit(K group, List<V> children, boolean folded) {
			this(group, children);
			this.folded = folded;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			Unit<?, ?> unit = (Unit<?, ?>) o;
			return Objects.equals(group, unit.group);
		}

		@Override
		public int hashCode() {
			return Objects.hash(group);
		}

		@Override
		public String toString() {
			return "Unit{" +
					"group=" + group +
					", children=" + children +
					", folded=" + folded +
					'}';
		}
	}
}
