package cn.mycommons.jsontool.action.jackson

import cn.mycommons.jsontool.action.base.AddAction
import cn.mycommons.jsontool.core.JsonType

open class JacksonRemoveAction : AddAction() {

    override fun getJsonType(): JsonType = JsonType.Jackson
}