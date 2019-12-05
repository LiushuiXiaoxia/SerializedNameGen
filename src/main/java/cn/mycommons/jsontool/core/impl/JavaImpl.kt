package cn.mycommons.jsontool.core.impl

import cn.mycommons.jsontool.core.FileType
import cn.mycommons.jsontool.core.IFileModify
import cn.mycommons.jsontool.core.IGenerateRule
import cn.mycommons.jsontool.core.JsonType
import cn.mycommons.jsontool.core.gen.OriginGenerateImpl
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
               private val psiClass: PsiClass,
               private val jsonType: JsonType,
               private val generateRule: IGenerateRule = OriginGenerateImpl()) : IFileModify {

    private val fields: ArrayList<PsiField> = ArrayList()

    override fun addAnnotation() {
        findField()
        if (fields.isNotEmpty()) {
            addImport() // 修改文件和类
            doAddAnnotation() // 生成方法
        }
    }

    private fun findField() {
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
            if (it.findSingleImportStatement(jsonType.annotationName) == null) {
                val factory = PsiElementFactory.SERVICE.getInstance(project)
                val statement = factory.createImportStatementOnDemand(jsonType.classPackage)
                styleManager.shortenClassReferences(it.add(statement))
            }
        }
    }

    private fun doAddAnnotation() {
        for (field in fields) {
            val annotation = field.annotations.find {
                it.text.contains(jsonType.annotationName)
            }
            if (annotation == null) {
                val name = generateRule.gen(field.name)
                field.modifierList?.addAnnotation(jsonType.genAnnotationText(name, FileType.JavaFile))
            }
        }
    }

    override fun removeAnnotation() {
        findField()
        if (fields.isNotEmpty()) {
            removeImport()
            doRemoveAnnotation()
        }
    }

    private fun removeImport() {
        // 删除 import SerializedName
        psiFile.importList?.let { it ->
            val statementBase = it.findSingleImportStatement(jsonType.annotationName)
            statementBase?.delete()
        }
    }

    private fun doRemoveAnnotation() {
        for (field in fields) {
            val annotation = field.annotations.find {
                it.text.contains(jsonType.annotationName)
            }
            annotation?.delete()
        }
    }
}