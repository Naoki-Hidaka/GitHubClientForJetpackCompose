dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "GitHubClientForJetpackCompose"
include(
    ":app",
    ":ui",
    ":ui:view",
    ":ui:viewModel",
    ":data",
    ":data:entity",
    ":data:repository",
    ":data:api",
    ":data:usecase",
    ":testing"
)
