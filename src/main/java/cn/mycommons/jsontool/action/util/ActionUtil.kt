package cn.mycommons.jsontool.action.util

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.actionSystem.PlatformDataKeys

fun isFileOk(e: AnActionEvent): Boolean {
    val virtualFile = e.getData(PlatformDataKeys.VIRTUAL_FILE)
    val psiFile = e.getData(LangDataKeys.PSI_FILE)

    val isVFOk = virtualFile?.name?.lowercase()?.endsWith("java") ?: false
    val isPsiOk = psiFile?.name?.lowercase()?.endsWith("java") ?: false

    val isKtVFOk = virtualFile?.name?.lowercase()?.endsWith("kt") ?: false
    val isKtPsiOk = psiFile?.name?.lowercase()?.endsWith("kt") ?: false

    val isJava = isVFOk && isPsiOk
    val isKt = isKtVFOk && isKtPsiOk
    return isJava || isKt
}