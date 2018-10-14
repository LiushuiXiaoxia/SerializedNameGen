package cn.mycommons.serializednamegen

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiJavaFile

open class GenAction : AnAction() {

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

            var isJava = false
            var isKotlin = false

            if (psiFile !is PsiJavaFile) {
                msg = "No java file"
                break
            }

            val project = event.getData(PlatformDataKeys.PROJECT)

            if (project != null) {
                psiFile.children
                        .filter { it is PsiClass }
                        .forEach { modify(project, psiFile, it as PsiClass) }
            }
        } while (false)

        println("msg = $msg")

        if (msg != null && msg.isNotEmpty()) {
            throw RuntimeException(msg)
        }
    }

    open fun modify(project: Project, javaFile: PsiJavaFile, clazz: PsiClass) {
        // 处理内部类
        clazz.children
                .filter { it is PsiClass }
                .forEach { modify(project, javaFile, it as PsiClass) }

        WriteCommandAction.runWriteCommandAction(project) {
            ModifySource(project, javaFile, clazz).modify()
        }
    }
}