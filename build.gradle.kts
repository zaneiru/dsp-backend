

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.3.21"
    id("org.jetbrains.kotlin.kapt") version "1.3.21"
    id("org.springframework.boot") version "2.1.4.RELEASE" apply false
    id("org.jetbrains.kotlin.plugin.spring") version "1.3.21" apply false
    id("com.gorylenko.gradle-git-properties") version "1.5.1" apply false
}

allprojects {
    repositories {
        // 요게 없으면 Cannot resolve external dependency org.jetbrains.kotlin:kotlin-compiler-embeddable:1.3.21 because no repositories are defined. 발생
        jcenter() // mavenCentral 인건 상관없네.
    }
}

subprojects {
    apply(plugin = "kotlin") // 요부분을 apply { plugin("kotlin")} -> apply(plugin="kotlin")
    apply(plugin = "kotlin-kapt")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
    apply(plugin = "com.gorylenko.gradle-git-properties")

    group = "io.honeymon.boot"
    version = "1.0.0"

    dependencies {
        compileOnly("com.fasterxml.jackson.module:jackson-module-kotlin")
        compileOnly("org.jetbrains.kotlin:kotlin-reflect")
        compileOnly("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
        compileOnly("org.springframework.boot:spring-boot-starter-logging")

        /**
         * @see <a href="https://kotlinlang.org/docs/reference/kapt.html">Annotation Processing with Kotlin</a>
         */
        //kapt("org.springframework.boot:spring-boot-configuration-processor")
        compileOnly("org.springframework.boot:spring-boot-configuration-processor")

        testCompileOnly("org.springframework.boot:spring-boot-starter-test")
    }

    tasks {
        compileKotlin {
            kotlinOptions {
                freeCompilerArgs = listOf("-Xjsr305=strict")
                jvmTarget = "14"
            }
            dependsOn(processResources) // kotlin 에서 ConfigurationProperties
        }


        compileTestKotlin {
            kotlinOptions {
                freeCompilerArgs = listOf("-Xjsr305=strict")
                jvmTarget = "14"
            }
        }
    }
}


project("common") {
    dependencies {
        compileOnly("org.springframework.boot:spring-boot-starter-data-jpa")

        runtimeOnly("com.h2database:h2")
    }

//    tasks {
//        getByName<BootJar>("bootJar") {
//            enabled = false
//        }
//        getByName<Jar>("jar") {
//            enabled = true
//        }
//    }

//    val jar: Jar by tasks
//    val bootJar: BootJar by tasks
//
//    bootJar.enabled = false
//    jar.enabled = true
}

project(":advertiser-api") {
    dependencies {
        compileOnly(project(":common"))

        compileOnly("de.codecentric:spring-boot-admin-starter-server:2.1.4")
        compileOnly("org.springframework.boot:spring-boot-starter-web")

        // Spring Boot Admin-api 대신 클라우드 환경에서 사용한다면!
//        compile("org.springframework.cloud:spring-cloud-starter-netflix-eureka-api")
    }
}
