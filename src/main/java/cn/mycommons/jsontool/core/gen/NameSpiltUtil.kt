package cn.mycommons.jsontool.core.gen

import java.util.*

object NameSpiltUtil {

    fun split(name: String): List<String> {
        if (name.isEmpty()) {
            return ArrayList()
        }
        val list: MutableList<String> = ArrayList()
        var lastBegin = 0
        for (i in name.indices) {
            val c = name[i]
            val isUpper = c in 'A'..'Z' // 是大写字母
            val isUnderLine = c == '_' // 是下划线
            if (isUpper && i - lastBegin > 0) {
                list.add(name.substring(lastBegin, i))
                lastBegin = i
            } else if (isUnderLine) {
                if (i - lastBegin > 1) {
                    list.add(name.substring(lastBegin, i))
                }
                lastBegin = i + 1
            } else if (i == name.length - 1) {
                list.add(name.substring(lastBegin, i + 1))
            }
        }
        return list
    }
}