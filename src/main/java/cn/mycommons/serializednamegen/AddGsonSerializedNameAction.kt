package cn.mycommons.serializednamegen

import cn.mycommons.serializednamegen.core.FileType
import cn.mycommons.serializednamegen.core.IFileModify
import cn.mycommons.serializednamegen.core.impl.JavaImpl
import cn.mycommons.serializednamegen.core.impl.KotlinImpl
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiJavaFile
import org.jetbrains.kotlin.idea.refactoring.fqName.getKotlinFqName
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtClassBody
import org.jetbrains.kotlin.psi.KtFile

open class AddGsonSerializedNameAction : AnAction() {

    override fun update(event: AnActionEvent?) {
        super.update(event)

        if (event != null) {
            event.presentation.isVisible = isFileOk(event)
        }
    }

    private fun isFileOk(e: AnActionEvent): Boolean {
        val virtualFile = e.getData(PlatformDataKeys.VIRTUAL_FILE)
        val psiFile = e.getData(LangDataKeys.PSI_FILE)

        val isVFOk = virtualFile?.name?.toLowerCase()?.endsWith("java") ?: false
        val isPsiOk = psiFile?.name?.toLowerCase()?.endsWith("java") ?: false

        val isKtVFOk = virtualFile?.name?.toLowerCase()?.endsWith("kt") ?: false
        val isKtPsiOk = psiFile?.name?.toLowerCase()?.endsWith("kt") ?: false

        val isJava = isVFOk && isPsiOk
        val isKt = isKtVFOk && isKtPsiOk
        return isJava || isKt
    }

    override fun actionPerformed(event: AnActionEvent) {
        try {
            doAction(event)
        } catch (e: Exception) {
            e.printStackTrace()
            val project = event.getData(PlatformDataKeys.PROJECT)
            Messages.showMessageDialog(project, e.message, "Warning", Messages.getWarningIcon())
        }
    }

    private fun doAction(event: AnActionEvent) {
        var msg: String? = null
        do {
            val psiFile = event.getData(LangDataKeys.PSI_FILE)
            if (psiFile == null) {
                msg = "No file"
                break
            }

            println("psiFile = $psiFile")

            var fileType: FileType?

            if (psiFile is PsiJavaFile) {
                fileType = FileType.JavaFile
            } else if (psiFile is KtFile) {
                fileType = FileType.KotlinFile
            } else {
                msg = "is not java or kotlin file"
                break
            }

            val project = event.getData(PlatformDataKeys.PROJECT)

            if (project != null) {
                psiFile.children
                        .filter {
                            println("PsiFile1 -> $psiFile")
                            println("it1 -> $it")
                            it is PsiClass || it is KtClass
                        }
                        .forEach { modify(project, psiFile, fileType, it) }
            }
        } while (false)

        println("msg = $msg")

        if (msg != null && msg.isNotEmpty()) {
            throw RuntimeException(msg)
        }
    }

    open fun modify(project: Project, psiFile: PsiFile, fileType: FileType, clazz: PsiElement) {
        // 处理内部类
        clazz.children
                .filter {
                    println("PsiFile2 -> $psiFile")
                    println("it2 -> $it")
                    it is PsiClass
                            || it is KtClass
//                            || it is KtClassBody
                }
                .forEach {
                    print("it->${it.getKotlinFqName()}")
                    modify(project, psiFile, fileType, it)
                }

        WriteCommandAction.runWriteCommandAction(project) {
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
            fileModify?.modify()
        }
    }
}