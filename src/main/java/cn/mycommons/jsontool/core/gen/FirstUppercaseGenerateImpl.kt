package cn.mycommons.jsontool.core.gen

import cn.mycommons.jsontool.core.IGenerateRule
import java.util.*

/**
 * abcxyz --> Abcxyz
 */
class FirstUppercaseGenerateImpl : IGenerateRule {

    override fun gen(name: String): String {
        return name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    }
}