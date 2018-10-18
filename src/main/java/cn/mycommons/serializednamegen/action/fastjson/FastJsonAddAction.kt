package cn.mycommons.serializednamegen.action.fastjson

import cn.mycommons.serializednamegen.action.base.AddAction
import cn.mycommons.serializednamegen.core.JsonType

class FastJsonAddAction : AddAction() {

    override fun getJsonType(): JsonType = JsonType.FastJson
}