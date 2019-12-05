import android.support.v7.widget.RecyclerView
import android.view.View

abstract class BaseRecyclerViewAdapter<T, H : RecyclerView.ViewHolder> : RecyclerView.Adapter<RecyclerView.ViewHolder> {
    val INVALID_INDEX = -1

    protected var mOnItemClickListener: OnItemClickListener? = null
    //    OnItemLongClickListener mOnItemLongClickListener;
    protected var mEndlessRecyclerOnScrollListener: EndlessRecyclerOnScrollListener? = null

    protected var mIsSelectedEnable: Boolean = false
    protected var mSelectedPositions: MutableList<Int> = arrayListOf()
    protected var mSelectedPosition = INVALID_INDEX
    protected var mIsMultiSelectedEnable: Boolean = false

    protected var mIsItemClickEnable = true
    protected var mItemClickEnableMax = INVALID_INDEX // -1 no limit

    private var mItemList: MutableList<T>? = null

    constructor()

    constructor(itemList: MutableList<T>) {
        mItemList = itemList
    }

    override fun getItemCount(): Int {
        if (null == mItemList) {
            return 0
        }
        return mItemList!!.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                val layoutPosition = holder.layoutPosition
                if (layoutPosition != RecyclerView.NO_POSITION) {
                    if (mIsItemClickEnable) {
                        if (mItemClickEnableMax == Constants.INVALID_VALUE || mItemClickEnableMax > layoutPosition) {
                            if (mIsSelectedEnable) {
                                notifyItemChanged(mSelectedPosition)
                                mSelectedPosition = layoutPosition
                                notifyItemChanged(mSelectedPosition)
                            } else if (mIsMultiSelectedEnable) {
                                if (mSelectedPositions.contains(layoutPosition)) {
                                    // unselected
                                    mSelectedPositions.remove(Integer.valueOf(layoutPosition))
                                    v.isSelected = false
                                } else {
                                    // selected
                                    mSelectedPositions.add(layoutPosition)
                                    v.isSelected = true
                                }
                                notifyItemChanged(layoutPosition)
                            }
                            if (null != mOnItemClickListener) {
                                mOnItemClickListener!!.onItemClick(holder.itemView, layoutPosition, this@BaseRecyclerViewAdapter)
                            }
                        }
                    }
                }
            }
        })
        when {
            mIsSelectedEnable -> holder.itemView.isSelected = mSelectedPosition == position
            mIsMultiSelectedEnable -> holder.itemView.isSelected = mSelectedPositions.contains(position)
            else -> holder.itemView.isSelected = false
        }
        @Suppress("UNCHECKED_CAST")
        onBindView(holder as H, position)
    }

    fun getItem(position: Int): T? {
        return if (null == mItemList || mItemList?.size!! - 1 < position) {
            null
        } else mItemList!![position]
    }

    fun getItems(): List<T>? {
        return if (null == mItemList) {
            null
        } else mItemList
    }

    fun getItemSize(): Int {
        return if (null == mItemList) {
            Constants.INVALID_INDEX
        } else mItemList!!.size
    }

    fun updateItems(items: MutableList<T>) {
        if (null == mItemList) {
            mItemList = arrayListOf()
        }
        mItemList?.clear()
        mEndlessRecyclerOnScrollListener?.reset(0, false)
        mItemList?.addAll(items)
        notifyDataSetChanged()
        if (null != mEndlessRecyclerOnScrollListener && items.isEmpty()) {
            mEndlessRecyclerOnScrollListener!!.setLoadFinished()
        }
    }

    fun updateItem(item: T, position: Int) {
        if (null == mItemList) {
            mItemList = arrayListOf()
        }
        if (mItemList!!.size > position) {
            mItemList!![position] = item
            notifyItemChanged(position)
        }
    }

    fun addItem(item: T) {
        if (null == mItemList) {
            mItemList = arrayListOf()
        }
        val position = mItemList!!.size
        mItemList!!.add(item)
        notifyItemInserted(position)
    }

    fun addItems(items: MutableList<T>?) {
        val itemCount = items?.size ?: 0
        mEndlessRecyclerOnScrollListener?.let {
            if (itemCount == 0) {
                it.setLoadFinished()
                return
            }
        }
        var positionStart = 0
        if (mItemList == null || items == null) {
            mItemList = items
        } else {
            positionStart = mItemList!!.size
            mItemList!!.addAll(items)
        }
        notifyItemRangeChanged(positionStart, itemCount)
    }

    fun addItems(position: Int, items: List<T>) {
        mItemList?.let {
            it.addAll(position, items)
            notifyItemRangeInserted(position, it.size)
        }
    }

    fun removeItems(position: Int, range: Int) {
        mItemList?.let {
            for (i in 0 until range) {
                it.removeAt(position)
            }
            notifyItemRangeRemoved(position, range)
            notifyItemRangeChanged(0, it.size)
        }
    }

    fun clearItems() {
        mItemList?.let {
            it.clear()
            notifyDataSetChanged()
        }
    }

    fun setItems(items: MutableList<T>?) {
        mItemList = items
        notifyDataSetChanged()
    }

    fun setSelectedEnable(isSelectedEnable: Boolean) {
        mIsSelectedEnable = isSelectedEnable
    }

    fun setItemClickEnable(mIsItemClickEnable: Boolean) {
        this.mIsItemClickEnable = mIsItemClickEnable
    }

    fun setItemClickEnableMax(nItemClickEnableMax: Int) {
        mItemClickEnableMax = nItemClickEnableMax
    }

    fun initSelectedPosition() {
        mSelectedPosition = INVALID_INDEX
    }

    fun setSelectedPosition(position: Int) {
        mSelectedPosition = position
        notifyItemChanged(mSelectedPosition)
    }

    fun setSelectedPositions(positions: MutableList<Int>) {
        mSelectedPositions = positions
        notifyDataSetChanged()
    }

    fun setMultiSelectedEnable(isSelectedEnable: Boolean) {
        mIsMultiSelectedEnable = isSelectedEnable
    }

    fun getSelectedPositions(): MutableList<Int> {
        return mSelectedPositions
    }

    fun getSelectedPosition(): Int {
        return mSelectedPosition
    }

    fun isSelectedAll(): Boolean {
        return mSelectedPositions.size == mItemList?.size
    }

    open fun setSelectAll(isSelectedAll: Boolean) {
        mSelectedPositions.let {
            it.clear()
            if (isSelectedAll) {
                var position = 0
                mItemList?.let { mItemList ->
                    while (position < mItemList.size) {
                        it.add(position)
                        position++
                    }
                }
            }
            notifyDataSetChanged()
        }
    }

    protected abstract fun onBindView(holder: H, position: Int)

    fun getEndlessRecyclerOnScrollListener(): EndlessRecyclerOnScrollListener? {
        return mEndlessRecyclerOnScrollListener
    }

    fun setEndlessRecyclerOnScrollListener(mEndlessRecyclerOnScrollListener: EndlessRecyclerOnScrollListener) {
        this.mEndlessRecyclerOnScrollListener = mEndlessRecyclerOnScrollListener
    }

    fun setOnItemClickListener(mOnItemClickListener: OnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener
    }

//    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
//        mOnItemLongClickListener = onItemLonsetEndlessRecyclerOnScrollListenergClickListener;
//    }

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int, adapter: BaseRecyclerViewAdapter<*, *>)
    }

//    public interface OnItemLongClickListener {
//        void onItemLongClick(View view, int position);
//    }
}