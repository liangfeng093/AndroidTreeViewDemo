package com.shtoone.smarthelmet.data.module

/**
 * 班组
 * Created by mzf on 2018/1/8.
 * Email:liangfeng093@gmail.com
 */
class NodeBean {

    var id: String? = ""
    var name: String? = ""
    var pid: String? = ""
    var realname: String? = ""
    var childCount: Int? = 0
    var type: String? = ""

    var level = 0//退格数
    var position = 0//位置

    var isSelect = false
    var isMember: Boolean = false
    override fun toString(): String {
        return "NodeBean(id=$id,pid=$pid,realname=$realname,isSelect=$isSelect,isMember=$isMember,position=$position, level=$level)"
    }


}