package cn.mycommons.jsontool.core.gen

import cn.mycommons.jsontool.core.IGenerateRule

/**
 * hello_world --> helloWorld
 */
class SmartGenerateImpl : IGenerateRule {

    override fun gen(name: String): String {
        val list = NameSpiltUtil.split(name)
        if (list.size > 0) {
            val sb = StringBuilder()
            sb.append(list[0].toLowerCase())
            for (i in 1 until list.size) {
                sb.append(list[i].capitalize())
            }
            return sb.toString()
        }
        return name
    }
}