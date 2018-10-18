package cn.mycommons.jsontool.action.gson

import cn.mycommons.jsontool.action.base.RemoveAction
import cn.mycommons.jsontool.core.JsonType

open class GsonRemoveAction : RemoveAction() {

    override fun getJsonType(): JsonType = JsonType.Gson
}