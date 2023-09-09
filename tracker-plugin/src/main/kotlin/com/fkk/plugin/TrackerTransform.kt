package com.fkk.plugin

import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import com.android.build.api.instrumentation.InstrumentationParameters
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

/**
 * @author fan.kaikai
 * @date 2023/9/9
 *
 **/

private const val HOOK_CLASS = "com.fkk.bussiness.ModuleInit"

private const val HOOK_METHOD = "dispatchInit"

private const val LOG = "com/fkk/bussiness/LogUtil"

abstract class TrackerTransform : AsmClassVisitorFactory<TrackerInfo> {

    override fun createClassVisitor(classContext: ClassContext, nextClassVisitor: ClassVisitor): ClassVisitor {
        if (HOOK_CLASS == classContext.currentClassData.className) {
            println("命中HOOK_CLASS=${classContext.currentClassData.className}")
            return TrackerClassVisitor(nextClassVisitor, parameters.get().userInfo.get())
        }
        return nextClassVisitor
    }

    override fun isInstrumentable(classData: ClassData): Boolean {
        return true
    }
}

class TrackerClassVisitor(nextClassVisitor: ClassVisitor, private val userInfo: String) : ClassVisitor(Opcodes.ASM9, nextClassVisitor) {

    override fun visitMethod(
        access: Int, name: String?, descriptor: String?, signature: String?, exceptions: Array<out String>?
    ): MethodVisitor {
        val visitMethod = cv.visitMethod(access, name, descriptor, signature, exceptions)
        return if (HOOK_METHOD == name) {
            println("命中Method:${name}")
            TrackerMethodVisitor(visitMethod, userInfo)
        } else {
            visitMethod
        }
    }

}

class TrackerMethodVisitor(nextMethodVisitor: MethodVisitor, private val userInfo: String) :
    MethodVisitor(Opcodes.ASM9, nextMethodVisitor) {

    override fun visitInsn(opcode: Int) {
        if (opcode == Opcodes.RETURN) {
            println("方法执行结束，插入LogUtil")
            mv.visitLdcInsn("ASM Insert Info=====> $userInfo")
            mv.visitMethodInsn(
                Opcodes.INVOKESTATIC, LOG, "w", "(Ljava/lang/String;)V", false
            )
        }
        super.visitInsn(opcode)
    }
}

interface TrackerInfo : InstrumentationParameters {

    @get:Input
    val userInfo: Property<String>
}