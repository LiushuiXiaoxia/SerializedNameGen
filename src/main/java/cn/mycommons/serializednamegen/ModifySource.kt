package cn.mycommons.serializednamegen

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElementFactory
import com.intellij.psi.PsiField
import com.intellij.psi.PsiJavaFile
import java.util.*

/**
 * ModifySource
 * Created by xiaqiulei on 2016-12-06.
 */
class ModifySource(private val project: Project, private val javaFile: PsiJavaFile, private val psiClass: PsiClass) {

    private val fields: ArrayList<PsiField> = ArrayList()

    fun modify() {
        init()
        if (fields.isNotEmpty()) {
            addImport() // 修改文件和类
            addAnnotation() // 生成方法
        }
    }

    private fun init() {
        // 遍历所有字段
        for (field in psiClass.fields) {
            val list = field.modifierList
            if (list != null && !list.hasModifierProperty("static")) {
                fields.add(field)
            }
        }
    }

    private fun addImport() {
        // 添加importSerializedName
        javaFile.importList?.let { it ->
            if (it.findSingleImportStatement("SerializedName") == null) {
                val factory = PsiElementFactory.SERVICE.getInstance(project)
                it.add(factory.createImportStatementOnDemand("com.google.gson.annotations"))
            }
        }
    }

    private fun addAnnotation() {
        for (field in fields) {
            val annotation = field.annotations.find {
                it.text.contains("@SerializedName")
            }
            if (annotation == null) {
                val name = field.name
                field.modifierList?.addAnnotation("""SerializedName("$name")""")
            }
        }
    }
}