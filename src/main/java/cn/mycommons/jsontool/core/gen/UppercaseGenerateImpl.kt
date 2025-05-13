package cn.mycommons.jsontool.core.gen

import cn.mycommons.jsontool.core.IGenerateRule

/**
 * abc --> ABC
 */
class UppercaseGenerateImpl : IGenerateRule {

    override fun gen(name: String): String = name.uppercase()
}