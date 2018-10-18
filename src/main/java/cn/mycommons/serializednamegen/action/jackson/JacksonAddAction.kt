package cn.mycommons.serializednamegen.action.jackson

import cn.mycommons.serializednamegen.action.base.AddAction
import cn.mycommons.serializednamegen.core.JsonType

class JacksonAddAction : AddAction() {

    override fun getJsonType(): JsonType = JsonType.Jackson
}