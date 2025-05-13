package cn.mycommons.jsontool.core.gen

import cn.mycommons.jsontool.core.IGenerateRule

/**
 * helloWorld --> hello_world
 */
class UnderlineGenerateImpl : IGenerateRule {

    override fun gen(name: String): String {
        val list = NameSpiltUtil.split(name)
        if (list.isNotEmpty()) {
            val sb = StringBuilder()
            list.forEach {
                sb.append(it.lowercase()).append("_")
            }
            if (sb.isNotEmpty()) {
                sb.deleteCharAt(sb.length - 1)
            }
            return sb.toString()
        }
        return name
    }
}