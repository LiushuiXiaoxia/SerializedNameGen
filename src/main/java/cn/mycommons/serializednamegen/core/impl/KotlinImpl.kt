package cn.mycommons.serializednamegen.core.impl

import cn.mycommons.serializednamegen.core.IFileModify
import cn.mycommons.serializednamegen.core.JsonType
import com.intellij.openapi.project.Project
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
                 private val classBody: KtClassBody?,
                 private val jsonType: JsonType) : IFileModify {

    private val parameterList: ArrayList<KtParameter> = ArrayList()
    private val propertyList: ArrayList<KtProperty> = ArrayList()

    override fun addAnnotation() {
        findField()
        if (propertyList.isNotEmpty() || parameterList.isNotEmpty()) {
            addImport() // 修改文件和类
            doAddAnnotation() // 生成方法
        }
    }

    private fun findField() {
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
        val import = jsonType.getFullName()
        val psiFactory = KtPsiFactory(project)
        val importDirective = psiFactory.createImportDirective(ImportPath(FqName(import), false))
        if (psiFile.importDirectives.none { it.importPath == importDirective.importPath }) {
            psiFile.importList?.add(importDirective)
        }
    }

    private fun doAddAnnotation() {
        val psiFactory = KtPsiFactory(project)
        for (property in propertyList) {
            val annotation = property.annotationEntries.find {
                it.text.contains(jsonType.annotationName)
            }
            if (annotation == null) {
                val name = property.name
                val entry = psiFactory.createAnnotationEntry(jsonType.genAnnotationText(name!!))
                property.addAnnotationEntry(entry)
                entry.add(psiFactory.createNewLine())
            }
        }

        for (parameter in parameterList) {
            val annotation = parameter.annotationEntries.find {
                it.text.contains(jsonType.annotationName)
            }
            if (annotation == null) {
                val name = parameter.name
                val entry = psiFactory.createAnnotationEntry(jsonType.genAnnotationText(name!!))
                parameter.addAnnotationEntry(entry)
                entry.add(psiFactory.createNewLine())
            }
        }
    }

    override fun removeAnnotation() {
        findField()
        if (propertyList.isNotEmpty() || parameterList.isNotEmpty()) {
            doRemoveImport()
            doRemoveAnnotation()
        }
    }

    private fun doRemoveImport() {
        val psiFactory = KtPsiFactory(project)
        val s = jsonType.getFullName()
        val importDirective = psiFactory.createImportDirective(ImportPath(FqName(s), false))
        val none = psiFile.importDirectives.find { it.importPath == importDirective.importPath }
        none?.delete()
    }

    private fun doRemoveAnnotation() {
        for (property in propertyList) {
            for (entry in property.annotationEntries) {
                if (entry.text.contains("@${jsonType.annotationName}")) {
                    entry.delete()
                }
            }
        }

        for (parameter in parameterList) {
            for (entry in parameter.annotationEntries) {
                if (entry.text.contains("@${jsonType.annotationName}")) {
                    entry.delete()
                    break
                }
            }
        }
    }
}