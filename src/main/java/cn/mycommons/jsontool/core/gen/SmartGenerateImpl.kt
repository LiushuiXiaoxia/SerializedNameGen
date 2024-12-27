package cn.mycommons.jsontool.core.gen

import cn.mycommons.jsontool.core.IGenerateRule
import java.util.*

/**
 * hello_world --> helloWorld
 */
class SmartGenerateImpl : IGenerateRule {

    override fun gen(name: String): String {
        val list = NameSpiltUtil.split(name)
        if (list.isNotEmpty()) {
            val sb = StringBuilder()
            sb.append(list[0].lowercase())
            for (i in 1 until list.size) {
                val s = list[i].replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                sb.append(s)
            }
            return sb.toString()
        }
        return name
    }
}