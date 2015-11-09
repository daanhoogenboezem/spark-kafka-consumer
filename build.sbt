import AssemblyPlugin.assemblySettings

lazy val meta = """META.INF(.)*""".r

lazy val root = (project in file(".")).
  settings(
    name := "spark-kafka-consumer",
    version := "1.0",
    scalaVersion := "2.10.6",
    libraryDependencies ++= Seq(
          "org.apache.spark" %% "spark-streaming-kafka" % "1.5.1",
          "org.json4s" %% "json4s-jackson" % "3.3.0",
          "org.apache.spark" %% "spark-streaming" % "1.5.1" % "provided",
          "org.apache.spark" %% "spark-core" % "1.5.1" % "provided")
  )

assemblyMergeStrategy in assembly := {
  case meta(_) | "UnusedStubClass.class"
    => MergeStrategy.discard
  case x
    => MergeStrategy.first
}
