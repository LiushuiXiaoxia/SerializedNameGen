package cn.mycommons.serializednamegen.core.impl

import cn.mycommons.serializednamegen.core.IFileModify
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElementFactory
import org.jetbrains.kotlin.lexer.KtModifierKeywordToken
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtProperty
import org.jetbrains.kotlin.psi.KtPsiFactory
import java.util.*

/**
 * KotlinImpl
 * Created by xiaqiulei on 2016-12-06.
 */
class KotlinImpl(private val project: Project,
                 private val psiFile: KtFile,
                 private val psiClass: KtClass) : IFileModify {

    private val propertyList: ArrayList<KtProperty> = ArrayList()

    override fun modify() {
        init()
        if (propertyList.isNotEmpty()) {
            addImport() // 修改文件和类
            addAnnotation() // 生成方法
        }
    }

    private fun init() {
        // 遍历所有字段
        psiClass.getProperties().forEach {
            if (it.modifierList?.hasModifier(KtModifierKeywordToken.keywordModifier("static")) == true) {
                propertyList.add(it)
            }
        }
    }

    private fun addImport() {
        // 添加importSerializedName
        psiFile.importList?.imports?.let { it ->
            val find = it.find { item ->
                println("item = $item")
                item.name?.contains("SerializedName") ?: false
            }
            if (find == null) {
                val factory = PsiElementFactory.SERVICE.getInstance(project)
                psiFile.add(factory.createImportStatementOnDemand("com.google.gson.annotations"))
            }
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
    }
}