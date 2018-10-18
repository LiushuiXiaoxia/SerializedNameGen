package cn.mycommons.jsontool.action.jackson

import cn.mycommons.jsontool.action.base.RemoveAction
import cn.mycommons.jsontool.core.JsonType

open class JacksonRemoveAction : RemoveAction() {

    override fun getJsonType(): JsonType = JsonType.Jackson
}