package cn.mycommons.jsontool.core.gen

import cn.mycommons.jsontool.core.IGenerateRule

/**
 * abcxyz --> Abcxyz
 */
class FirstUppercaseGenerateImpl : IGenerateRule {

    override fun gen(name: String): String = name.capitalize()
}