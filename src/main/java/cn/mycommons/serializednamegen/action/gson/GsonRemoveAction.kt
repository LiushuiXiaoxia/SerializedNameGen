package cn.mycommons.serializednamegen.action.gson

import cn.mycommons.serializednamegen.action.base.AddAction
import cn.mycommons.serializednamegen.core.JsonType

open class GsonRemoveAction : AddAction() {

    override fun getJsonType(): JsonType = JsonType.Gson
}