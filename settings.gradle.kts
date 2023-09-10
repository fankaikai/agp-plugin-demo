pluginManagement {
    repositories {
        maven {
            url = uri("/Volumes/Karl/workspace/android/agp-plugin-demo/repo")
            isAllowInsecureProtocol = true
        }
        google()
        mavenCentral()
        gradlePluginPortal()
    }

    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "com.fkk.tracker"){
                useModule("com.fkk.plugin:tracker-plugin:1.0.1")
            }
        }
    }
}
dependencyResolutionManagement {
//    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven {
            url = uri("/Volumes/Karl/workspace/android/agp-plugin-demo/repo")
            isAllowInsecureProtocol = true
        }
        google()
        mavenCentral()
    }
}

rootProject.name = "agp-plugin-demo"
include(":app")
include(":tracker-plugin")
