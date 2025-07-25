pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "GiftRegister"
include (":app")
include (":pitheme")
include (":authentication")
include (":welcome")
include (":register")
include (":PiModel")
include (":PiNavigation")
include (":PiCloud")
include (":PiNetwork")
include (":PiAnalytic")
