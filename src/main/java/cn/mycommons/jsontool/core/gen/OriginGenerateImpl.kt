package cn.mycommons.jsontool.core.gen

import cn.mycommons.jsontool.core.IGenerateRule

/**
 * 不转换
 * abc --> abc
 * ABC --> ABC
 */
class OriginGenerateImpl : IGenerateRule {

    override fun gen(name: String): String = name
}