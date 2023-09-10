package com.fkk.plugin

import com.android.build.api.instrumentation.FramesComputationMode
import com.android.build.api.instrumentation.InstrumentationScope
import com.android.build.api.variant.AndroidComponentsExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.ByteArrayOutputStream
import kotlin.jvm.Throws

/**
 * @author fan.kaikai
 * @date 2023/9/8
 *
 **/
class TrackerPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        val androidComponentsExtension = target.extensions.getByType(AndroidComponentsExtension::class.java)
        androidComponentsExtension.onVariants {
            it.instrumentation.apply {
                println("registerTrackerTransform in ${it.name}")
                transformClassesWith(
                    TrackerTransform::class.java, InstrumentationScope.PROJECT
                ) { params ->
                    params.userInfo.set(getUserInfo(target))
                }
                setAsmFramesComputationMode(
                    FramesComputationMode.COPY_FRAMES
                )
            }
        }
    }


    @Throws(Exception::class)
    fun getUserInfo(project: Project): String {
        val username = ByteArrayOutputStream()
        project.exec {
            commandLine("git", "config", "--get", "user.name")
            standardOutput = username
        }

        val email = ByteArrayOutputStream()
        project.exec {
            commandLine("git", "config", "--get", "user.email")
            standardOutput = email
        }

        return "git config: user.name=${username.toString().trim()},user.email=${email.toString().trim()}"
    }


}