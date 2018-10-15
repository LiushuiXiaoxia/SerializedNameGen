package cn.mycommons.serializednamegen.core.impl

import cn.mycommons.serializednamegen.core.IFileModify
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElementFactory
import com.intellij.psi.PsiField
import com.intellij.psi.PsiJavaFile
import com.intellij.psi.codeStyle.JavaCodeStyleManager
import java.util.*

/**
 * JavaImpl
 * Created by xiaqiulei on 2016-12-06.
 */
class JavaImpl(private val project: Project,
               private val psiFile: PsiJavaFile,
               private val psiClass: PsiClass) : IFileModify {

    private val fields: ArrayList<PsiField> = ArrayList()

    override fun modify() {
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
        val styleManager = JavaCodeStyleManager.getInstance(psiClass.project)

        // 添加importSerializedName
        psiFile.importList?.let { it ->
            if (it.findSingleImportStatement("SerializedName") == null) {
                val factory = PsiElementFactory.SERVICE.getInstance(project)
                val statement = factory.createImportStatementOnDemand("com.google.gson.annotations")
                styleManager.shortenClassReferences(it.add(statement))
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