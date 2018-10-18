package cn.mycommons.serializednamegen.action.jackson

import cn.mycommons.serializednamegen.action.base.AddAction
import cn.mycommons.serializednamegen.core.JsonType

open class JacksonRemoveAction : AddAction() {

    override fun getJsonType(): JsonType = JsonType.Jackson
}