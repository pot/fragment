plugins {
    `java-gradle-plugin`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.typesafe:config:1.4.3")
    implementation("team.unnamed:creative-api:1.7.3")
    implementation("team.unnamed:creative-serializer-minecraft:1.7.3")
    implementation("com.palantir.javapoet:javapoet:0.6.0")
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter("5.11.1")
        }

        val functionalTest by registering(JvmTestSuite::class) {
            dependencies {
                implementation(project())
            }

            targets {
                all {
                    testTask.configure { shouldRunAfter(test) }
                }
            }
        }
    }
}

gradlePlugin {
    val fragment by plugins.creating {
        id = "dev.nateweisz.fragment"
        implementationClass = "dev.nateweisz.fragment.FragmentPlugin"
    }
}

gradlePlugin.testSourceSets.add(sourceSets["functionalTest"])

tasks.named<Task>("check") {
    // Include functionalTest as part of the check lifecycle
    dependsOn(testing.suites.named("functionalTest"))
}
