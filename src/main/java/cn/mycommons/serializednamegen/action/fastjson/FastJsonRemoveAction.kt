package cn.mycommons.serializednamegen.action.fastjson

import cn.mycommons.serializednamegen.action.base.RemoveAction
import cn.mycommons.serializednamegen.core.JsonType

open class FastJsonRemoveAction : RemoveAction() {

    override fun getJsonType(): JsonType = JsonType.FastJson
}