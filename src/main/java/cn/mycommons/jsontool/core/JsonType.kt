package cn.mycommons.jsontool.core

/**
 * JsonType <br>
 * Created by xiaqiulei on 2018-10-18.
 */
enum class JsonType(val classPackage: String, val annotationName: String) {

    Gson("com.google.gson.annotations", "SerializedName") {
        override fun genAnnotationText(fieldName: String?, fileType: FileType): String {
            return if (fileType == FileType.JavaFile) {
                """$annotationName("$fieldName")"""
            } else {
                """@$annotationName("$fieldName")"""
            }
        }
    },
    Jackson("com.fasterxml.jackson.annotation", "JsonProperty") {
        override fun genAnnotationText(fieldName: String?, fileType: FileType): String {
            return if (fileType == FileType.JavaFile) {
                """$annotationName("$fieldName")"""
            } else {
                """@$annotationName("$fieldName")"""
            }
        }
    },
    FastJson("com.alibaba.fastjson.annotation", "JSONField") {
        override fun genAnnotationText(fieldName: String?, fileType: FileType): String {
            return if (fileType == FileType.JavaFile) {
                """$annotationName(name = "$fieldName")"""
            } else {
                """@$annotationName(name = "$fieldName")"""
            }
        }
    };

    override fun toString(): String {
        return "JsonType(classPackage='$classPackage', annotationName='$annotationName')"
    }

    abstract fun genAnnotationText(fieldName: String?, fileType: FileType): String

    fun getFullName() = "$classPackage.$annotationName"
}