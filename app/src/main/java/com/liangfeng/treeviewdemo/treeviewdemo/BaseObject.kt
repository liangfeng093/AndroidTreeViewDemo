package com.shtoone.smarthelmet.data.module.base

/**
 * Created by mzf on 2018/1/18.
 * Email:liangfeng093@gmail.com
 */
open class BaseObject<T> {
    var attributes: String = ""
    var total: Int? = null
    var obj: T? = null
    var rows: MutableList<String>? = null
    var success: String = ""
    var msg: String = ""
    override fun toString(): String {
        return "BaseObject(obj=$obj)"
    }
}