plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies{
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.javax.inject)
}