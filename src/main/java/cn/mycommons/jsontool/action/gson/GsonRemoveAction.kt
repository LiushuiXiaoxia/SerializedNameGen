package cn.mycommons.jsontool.action.gson

import cn.mycommons.jsontool.action.base.AddAction
import cn.mycommons.jsontool.core.JsonType

open class GsonRemoveAction : AddAction() {

    override fun getJsonType(): JsonType = JsonType.Gson
}