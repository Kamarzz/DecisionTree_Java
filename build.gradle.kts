plugins {
    id("java")
}

group = "DecisionTree"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("tech.tablesaw:tablesaw-core:0.43.1")
    implementation("org.jfree:jfreechart:1.5.4")
    implementation ("org.apache.commons:commons-csv:1.9.0")
}

tasks.test {
    useJUnitPlatform()
}
