package cn.mycommons.jsontool.action.fastjson

import cn.mycommons.jsontool.action.base.AddAction
import cn.mycommons.jsontool.core.JsonType

class FastJsonAddAction : AddAction() {

    override fun getJsonType(): JsonType = JsonType.FastJson
}