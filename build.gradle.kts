// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "7.1.3" apply false
    id("com.android.library") version "7.1.3" apply false
    id("org.jetbrains.kotlin.android") version "1.5.21" apply false
    id("org.sonarqube") version "3.3"
    jacoco
}

tasks {
    register<Delete>("clean") {
        delete(rootProject.buildDir)
    }
}
sonarqube {
    properties {
        property("sonar.projectKey", "musooff_Topik_AYB-ZFlS8-xhCYxQvxYf")
        property("sonar.projectName", "Topik")
        property("sonar.host.url", "https://fb41-221-141-140-219.jp.ngrok.io")
        property("sonar.login", "2c1ea640cc3e441d11150f66391ab6cdaa41aee6")
    }
}

jacoco {
    toolVersion = "0.8.7"
}