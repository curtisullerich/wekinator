plugins {
  id("java")
  id("com.github.johnrengelman.shadow") version "6.0.0"
  application
}

dependencies {
  listOf(
    "commons-cli-1.4.jar",
    "FastDTW1.1.0.jar",
    "javaosc.jar",
    "JSON.jar",
    "swing-worker-1.2.jar",
    "weka.jar",
    "xmlpull-1.1.3.1.jar",
    "xpp3_min-1.1.4c.jar",
    "xstream-1.4.18.jar",
    "absolutelayout/AbsoluteLayout.jar",
    "swing-layout/swing-layout-1.0.4.jar",
  ).forEach {
    implementation(files("lib/$it"))
  }
}

// run ./gradlew shadowJar to generate build/libs/wekinator-all.jar. This is a
// jar that contains all dependencies, so it can be moved to another system and
// run with java -jar.
project.setProperty("mainClassName", "wekimini.WekiMiniRunner") // for shadowJar

// This puts the entire java source tree into the src/main/resources folder of
// the output jar. This is because resource files like icon images are mixed in
// with the java source, and this one-line change allows the project to build
// without moving a bunch of files and changing a bunch of hardcoded filepaths
// in code. (At least, I think that's what this does.)
sourceSets { main { resources { srcDirs("src/main/java") } } }
