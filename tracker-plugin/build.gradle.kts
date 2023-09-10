plugins {
    `kotlin-dsl`
//    `java-gradle-plugin`
    `maven-publish`
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(gradleApi())
    implementation("com.android.tools.build:gradle:8.1.1")

    val asmVersion = "9.3"
    implementation("org.ow2.asm:asm:$asmVersion")
    implementation("org.ow2.asm:asm-commons:$asmVersion")
    implementation("org.ow2.asm:asm-util:$asmVersion")
}

val sourceJar = tasks.register("sourceJar", Jar::class) {
    archiveClassifier.set("tacker-plugin source")
    from(sourceSets.main.get().allSource)
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("trackerPlugin") {
                from(components["java"])
                groupId = "com.fkk.plugin"
                artifactId = project.name
                version = "1.0.11"

                project.group = this.groupId
                project.version = this.version
                artifact(sourceJar)
            }
        }

        repositories {
            maven {
//                credentials {
//                    username mavenProps.mavenUsername
//                    password mavenProps.mavenPassword
//                }
//                url mavenProps.releasesRepository
                url = uri("/Volumes/Karl/workspace/android/agp-plugin-demo/repo")
                isAllowInsecureProtocol = true
            }
        }
    }
}


