package cn.mycommons.serializednamegen.action.gson

import cn.mycommons.serializednamegen.action.base.BaseAction
import cn.mycommons.serializednamegen.core.FileType
import cn.mycommons.serializednamegen.core.IFileModify
import cn.mycommons.serializednamegen.core.impl.JavaImpl
import cn.mycommons.serializednamegen.core.impl.KotlinImpl
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiJavaFile
import org.jetbrains.kotlin.idea.refactoring.fqName.getKotlinFqName
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtClassBody
import org.jetbrains.kotlin.psi.KtFile

class GsonAddAction : BaseAction() {

    override fun doModify(project: Project, psiFile: PsiFile, fileType: FileType, clazz: PsiElement) {
        val fileModify: IFileModify? = when (fileType) {
            FileType.JavaFile -> {
                JavaImpl(project, psiFile as PsiJavaFile, clazz as PsiClass)
            }
            FileType.KotlinFile -> {
                when (clazz) {
                    is KtClass -> KotlinImpl(project, psiFile as KtFile, clazz, null)
                    is KtClassBody -> KotlinImpl(project, psiFile as KtFile, null, clazz)
                    else -> null
                }
            }
        }
        fileModify?.addAnnotation()

        // 处理内部类
        clazz.children
                .filter {
                    println("PsiFile2 -> $psiFile")
                    println("it2 -> $it")
                    it is PsiClass
                            || it is KtClass
                            // || it is KtClassBody
                }
                .forEach {
                    print("it->${it.getKotlinFqName()}")
                    doModify(project, psiFile, fileType, it)
                }
    }
}