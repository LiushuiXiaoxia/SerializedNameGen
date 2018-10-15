package cn.mycommons.serializednamegen.core.impl

import cn.mycommons.serializednamegen.core.IFileModify
import com.intellij.openapi.project.Project
import org.jetbrains.kotlin.lexer.KtModifierKeywordToken
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.psi.psiUtil.getValueParameters
import org.jetbrains.kotlin.resolve.ImportPath
import java.util.*

/**
 * KotlinImpl
 * Created by xiaqiulei on 2016-12-06.
 */
class KotlinImpl(private val project: Project,
                 private val psiFile: KtFile,
                 private val psiClass: KtClass?,
                 private val classBody: KtClassBody?) : IFileModify {

    private val parameterList: ArrayList<KtParameter> = ArrayList()
    private val propertyList: ArrayList<KtProperty> = ArrayList()

    override fun modify() {
        init()
        if (propertyList.isNotEmpty() || parameterList.isNotEmpty()) {
            addImport() // 修改文件和类
            addAnnotation() // 生成方法
        }
    }

    private fun init() {
        // 遍历所有字段

        if (psiClass != null) {
            psiClass.getProperties().forEach {
                propertyList.add(it)
            }
            parameterList.addAll(psiClass.getValueParameters())
        }

        if (classBody != null) {
            classBody.properties.forEach {
                propertyList.add(it)
            }
//            parameterList.addAll(classBody.getValueParameters())
        }
    }

    private fun addImport() {
        // 添加importSerializedName
        addImportIfNeed("com.google.gson.annotations.SerializedName")
    }

    private fun addImportIfNeed(import: String) {
        val psiFactory = KtPsiFactory(project)
        val importDirective = psiFactory.createImportDirective(ImportPath(FqName(import), false))
        if (psiFile.importDirectives.none { it.importPath == importDirective.importPath }) {
            psiFile.importList?.add(importDirective)
        }
    }

    private fun addAnnotation() {
        val psiFactory = KtPsiFactory(project)
        for (property in propertyList) {
            val annotation = property.annotations.find {
                it.text.contains("@SerializedName")
            }
            if (annotation == null) {
                val name = property.name
                val entry = psiFactory.createAnnotationEntry("""@SerializedName("$name")""")
                property.addAnnotationEntry(entry)
                entry.add(psiFactory.createNewLine())
            }
        }

        for (parameter in parameterList) {
            val annotation = parameter.annotations.find {
                it.text.contains("@SerializedName")
            }
            if (annotation == null) {
                val name = parameter.name
                val entry = psiFactory.createAnnotationEntry("""@SerializedName("$name")""")
                parameter.addAnnotationEntry(entry)
                entry.add(psiFactory.createNewLine())
            }
        }
    }
}