package com.shtoone.smarthelmet.features.addressbookfeature.contactfeature

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.liangfeng.treeviewdemo.treeviewdemo.R
import com.unnamed.b.atv.model.TreeNode

/**
 * 树形图条目视图
 * Created by mzf on 2018/4/21.
 * Email:liangfeng093@gmail.com
 */
class IconTreeItemHolder : TreeNode.BaseNodeViewHolder<IconTreeItemHolder.IconTreeItem> {

    val TAG = this.javaClass.name

    constructor(context: Context?) : super(context)

    var tvNameTreeView: TextView? = null
    var tvChildCount: TextView? = null
    var ivArrow: ImageView? = null
    var ivLocation: ImageView? = null
    var ivTrack: ImageView? = null
    var rlContainer: RelativeLayout? = null


    override fun createNodeView(node: TreeNode?, item: IconTreeItem?): View {

        var view = LayoutInflater.from(context)?.inflate(R.layout.item_tree_view, null, false)
        view?.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        tvNameTreeView = view?.findViewById(R.id.tvNameTreeView)
        tvChildCount = view?.findViewById(R.id.tvChildCount)
        ivArrow = view?.findViewById(R.id.ivArrow)
        ivLocation = view?.findViewById(R.id.ivLocation)
        ivTrack = view?.findViewById(R.id.ivTrack)
        rlContainer = view?.findViewById(R.id.rlContainer)
        tvNameTreeView?.text = item?.title

        if (node?.isLeaf!! && "1"?.equals(item?.type)) {
            ivArrow?.visibility = View.GONE
            ivTrack?.visibility = View.VISIBLE
            tvNameTreeView?.textSize = 14f
            tvChildCount?.visibility = View.GONE

        } else {
            ivLocation?.visibility = View.GONE
            ivArrow?.visibility = View.VISIBLE
            ivTrack?.visibility = View.GONE
            tvNameTreeView?.textSize = 16f
            tvChildCount?.visibility = View.VISIBLE
            tvChildCount?.text = "（" + item?.childCount + "）"
        }
        if ("0"?.equals(item?.pid)) {
            tvNameTreeView?.textSize = 18f
        }

        //设置缩进
        if (node?.level > 0) {
            rlContainer?.setPadding((node?.level!! - 1) * 100, view?.paddingTop!!, view?.paddingRight, view?.paddingBottom)
        } else {
            rlContainer?.setPadding(node?.level!! * 100, view?.paddingTop!!, view?.paddingRight, view?.paddingBottom)
        }

        if (node?.isExpanded) {//是否展开
            ivArrow?.setImageResource(R.mipmap.item_arrow_down)
        } else {
            ivArrow?.setImageResource(R.mipmap.item_arrow)
        }


        ivTrack?.setOnClickListener {
        }
        ivLocation?.setOnClickListener {
            Log.e(TAG, ">>>>>>>获取定位1:" + item?.title)
            Log.e(TAG, ">>>>>>>获取定位2:" + item?.userName)
        }

        return view!!
    }


    /**
     * 树形图条目对应的实体类
     */
    class IconTreeItem {
        var id: String? = ""
        var pid: String? = ""
        var title = ""
        var userName: String? = ""
        var childCount = 0
        var type = ""

        constructor(title: String, id: String?, userName: String?, pid: String?, childCount: Int, type: String) {
            this.title = title
            this.id = id
            this.userName = userName
            this.pid = pid
            this.childCount = childCount
            this.type = type
        }
    }
}