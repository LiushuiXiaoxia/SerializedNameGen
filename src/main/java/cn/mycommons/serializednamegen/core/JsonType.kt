package cn.mycommons.serializednamegen.core

/**
 * JsonType <br>
 * Created by xiaqiulei on 2018-10-18.
 */
enum class JsonType(val classPackage: String, val annotationName: String) {

    Gson("com.google.gson.annotations", "SerializedName") {
        override fun genAnnotationText(fieldNam: String): String {
            return """$annotationName("$fieldNam")"""
        }
    },
    Jackson("com.fasterxml.jackson.annotationName", "JsonProperty") {
        override fun genAnnotationText(fieldNam: String): String {
            return """$annotationName("$fieldNam")"""
        }
    },
    FastJson("com.alibaba.fastjson.annotationName", "JSONField") {
        override fun genAnnotationText(fieldNam: String): String {
            return """$annotationName(name = "$fieldNam")"""

        }
    };

    override fun toString(): String {
        return "JsonType(classPackage='$classPackage', annotationName='$annotationName')"
    }

    abstract fun genAnnotationText(fieldNam: String): String

    fun getFullName() = "$classPackage.$annotationName"
}