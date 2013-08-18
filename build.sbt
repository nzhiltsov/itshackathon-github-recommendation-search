import AssemblyKeys._

name := "GitHub Issue Recommendation Search"

version := "0.1"

organization := "ru.ksu.niimm.cll"

scalaVersion := "2.9.2"

resolvers += "Concurrent Maven Repo" at "http://conjars.org/repo"

resolvers ++= Seq(
  "snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
  "releases"  at "http://oss.sonatype.org/content/repositories/releases",
  "Twitter Maven" at "http://maven.twttr.com",
   "Twitter SVN Maven" at "https://svn.twitter.biz/maven-public",
   "Clojars Repository" at "http://clojars.org/repo",
   "Restlet" at "http://maven.restlet.org"
)


libraryDependencies += "org.scala-tools.testing" % "specs_2.9.2" % "1.6.9" % "test"

libraryDependencies += "org.scalatest" %% "scalatest" % "1.8" % "test"

libraryDependencies += "junit" % "junit" % "4.8" % "test"

libraryDependencies += "net.sf.jopt-simple" % "jopt-simple" % "4.3"

libraryDependencies += "org.apache.commons" % "commons-compress" % "1.4.1"

libraryDependencies += "org.apache.commons" % "commons-lang3" % "3.1"

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.0.9"

libraryDependencies += "org.jsoup" % "jsoup" % "1.7.2"

// Apache Solr dependencies

libraryDependencies += "org.apache.solr" % "solr" % "4.4.0"

parallelExecution in Test := false

seq(assemblySettings: _*)

mainClass in (Compile, run) := Some("com.twitter.scalding.Tool")

// Uncomment if you don't want to run all the tests before building assembly
// test in assembly := {}

// Janino includes a broken signature, and is not needed:
excludedJars in assembly <<= (fullClasspath in assembly) map { cp =>
  val excludes = Set("jsp-api-2.1-6.1.14.jar", "jsp-2.1-6.1.14.jar",
    "jasper-compiler-5.5.12.jar", "janino-2.5.16.jar")
  cp filter { jar => excludes(jar.data.getName)}
}

// Some of these files have duplicates, let's ignore:
mergeStrategy in assembly <<= (mergeStrategy in assembly) { (old) =>
  {
    case s if s.endsWith(".class") => MergeStrategy.last
    case s if s.endsWith("project.clj") => MergeStrategy.concat
    case s if s.endsWith(".html") => MergeStrategy.last
    case x => old(x)
  }
}
