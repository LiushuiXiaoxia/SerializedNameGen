package cn.mycommons.jsontool.action.fastjson

import cn.mycommons.jsontool.action.base.RemoveAction
import cn.mycommons.jsontool.core.JsonType

open class FastJsonRemoveAction : RemoveAction() {

    override fun getJsonType(): JsonType = JsonType.FastJson
}