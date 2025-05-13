package cn.mycommons.jsontool.core.gen

import cn.mycommons.jsontool.core.IGenerateRule

/**
 * ABC --> abc
 */
class LowercaseGenerateImpl : IGenerateRule {

    override fun gen(name: String): String = name.lowercase()
}