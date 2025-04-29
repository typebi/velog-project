plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "velog-project"

include("api-server", "kafka-streams")
include("kafka-streams")
