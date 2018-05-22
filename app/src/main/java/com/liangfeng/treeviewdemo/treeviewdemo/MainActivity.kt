package com.liangfeng.treeviewdemo.treeviewdemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ImageView
import com.google.gson.Gson
import com.shtoone.smarthelmet.data.module.NodeBean
import com.shtoone.smarthelmet.features.addressbookfeature.contactfeature.IconTreeItemHolder
import com.shtoone.smarthelmet.features.addressbookfeature.contactfeature.OrgNode
import com.shtoone.smarthelmet.features.addressbookfeature.contactfeature.OrgResponseBean
import com.unnamed.b.atv.model.TreeNode
import com.unnamed.b.atv.view.AndroidTreeView
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import kotlinx.android.synthetic.main.activity_main.*

/**
 *
 * https://github.com/bmelnychuk/AndroidTreeView
 */
class MainActivity : AppCompatActivity() {
    val TAG = this.javaClass.name
    var treeView: AndroidTreeView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var json: String = resources?.getString(R.string.json_data)!!
        var gson = Gson()
        var bean = gson?.fromJson<OrgResponseBean>(json, OrgResponseBean::class.java)
        var dataList: MutableList<OrgNode> = bean?.obj!!

        Log.e(TAG, ">>>>>>>size:" + dataList?.size)

        treeView = AndroidTreeView(this, root)
        treeView?.setDefaultAnimation(true)
        treeView?.setDefaultViewHolder(IconTreeItemHolder::class.java)

        treeView?.setDefaultNodeClickListener(object : TreeNode.TreeNodeClickListener {
            override fun onClick(node: TreeNode?, any: Any?) {
                if (!node?.isExpanded!!) {
                    node?.viewHolder?.view?.findViewById<ImageView>(R.id.ivArrow)?.setImageResource(R.mipmap.item_arrow_down)
                } else {
                    node?.viewHolder?.view?.findViewById<ImageView>(R.id.ivArrow)?.setImageResource(R.mipmap.item_arrow)
                }
            }
        })

        dealWithData(dataList)
        //0是根节点id
        showNode(root, "0")//递归绘制树形图
        container?.addView(treeView?.view)//将树形图添加到父容器
    }

    /**
     * rootNodes(list集合)
     * Map<id,talkList<String>>  list是子节点id集合
     */
    var idListMaps = mutableMapOf<String, MutableList<String>>()
    var allNodeMap = mutableMapOf<String, NodeBean>()
    var root: TreeNode = TreeNode.root()

    fun dealWithData(allObjs: MutableList<OrgNode>) {
        Observable.fromIterable(allObjs)
                .flatMap(object : Function<OrgNode, Observable<NodeBean>> {
                    //使用flatMap处理数据
                    override fun apply(curObj: OrgNode): Observable<NodeBean> {
                        var node = NodeBean()
                        node?.realname = curObj?.name
                        node?.pid = curObj?.pid
                        node?.id = curObj?.id
                        node?.childCount = curObj?.cnt
                        node?.type = curObj?.type
                        if ("1".equals(curObj?.type))
                            node?.isMember = true
                        else
                            node?.isMember = false
                        return Observable.just(node)
                    }
                })
                .subscribe(object : Consumer<NodeBean> {
                    override fun accept(node: NodeBean) {
                        if (node?.pid?.equals("0")!!) {//node为根节点的子节点
                            node?.level = 0
                            if (idListMaps?.get(node?.pid!!) == null) {//根节点在node后面
                                var cIds1 = mutableListOf<String>()//创建子节点id集合
                                cIds1?.add(node?.id!!)//当前节点id添加到集合
                                idListMaps?.put(node?.pid!!, cIds1)
                                allNodeMap?.put(node?.id!!, node)
                            }
                            if (idListMaps?.get(node?.id!!) == null) {//当前节点idListMaps中不存在
                                var cIds2 = mutableListOf<String>()
                                idListMaps?.put(node?.id!!, cIds2)
                                allNodeMap?.put(node?.id!!, node)
                            }
                        } else {//根节点有pid
                            var pid = node?.pid
                            var cIds = idListMaps?.get(pid)//map中存在，则node为子节点---> pid=id(父节点的子节点集合)
                            if (cIds != null) {//
                                cIds?.add(node?.id!!)
                            } else {
                                var cIds = mutableListOf<String>()
                                cIds?.add(node?.id!!)//子节点id集合
                                idListMaps?.put(node?.pid!!, cIds)//保存id和子节点的关系
                            }
                            allNodeMap?.put(node?.id!!, node)
                        }
                    }
                })
    }

    /**
     * 递归添加节点
     * @param pNode 父节点
     * @param id 节点id
     */
    fun showNode(pNode: TreeNode, id: String) {
        idListMaps?.get(id)?.forEach {
            var nodeBean = allNodeMap?.get(it)
            var node = TreeNode(IconTreeItemHolder.IconTreeItem(nodeBean?.realname!!, nodeBean?.id, nodeBean?.name, nodeBean?.pid, nodeBean?.childCount!!, nodeBean?.type!!))//当前节点
            pNode?.addChild(node)
            showNode(node, it)
        }
    }
}

