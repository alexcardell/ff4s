// addSbtPlugin("org.portable-scala" % "sbt-scalajs-crossproject" % "1.2.0")
// addSbtPlugin("org.portable-scala" % "sbt-scala-native-crossproject" % "1.2.0")

addSbtPlugin("com.eed3si9n" % "sbt-projectmatrix" % "0.9.1")

// add also the following for Scala.js support
addSbtPlugin("org.scala-js" % "sbt-scalajs" % "1.11.0")
// addSbtPlugin("org.scala-native" % "sbt-scala-native" % "0.4.7")

addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.4.6")
addSbtPlugin("ch.epfl.scala" % "sbt-scalafix" % "0.10.4")

addSbtPlugin("com.github.sbt" % "sbt-ci-release" % "1.5.10")

addSbtPlugin(
  "com.disneystreaming.smithy4s" % "smithy4s-sbt-codegen" % "0.17.13"
)
