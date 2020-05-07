import sbt._

object Dependencies {
  object Versions {
    val circe = "0.13.0"
    val http4s = "0.21.4"
    val jaeger = "1.2.0"
    val opentracing = "0.33.0"
    val zio = "1.0.0-RC18-2"
    val zioOpenTracing = "0.3.0"
    val zipkin = "2.12.3"

    val scalaTest = "3.1.1"

    val betterMonadicFor = "0.3.1"
    val kindProjector = "0.11.0"
  }
  lazy val openTracing = Seq(
    "io.opentracing" % "opentracing-api" % Versions.opentracing,
    "io.opentracing" % "opentracing-mock" % Versions.opentracing,
    "dev.zio" %% "zio-opentracing" % Versions.zioOpenTracing
  )

  lazy val jaegerTracer = Seq(
    "io.jaegertracing" % "jaeger-core" % Versions.jaeger,
    "io.jaegertracing" % "jaeger-client" % Versions.jaeger,
    "io.jaegertracing" % "jaeger-zipkin" % Versions.jaeger
  )

  lazy val opentracingExample = Seq(
    "io.zipkin.reporter2" % "zipkin-reporter" % Versions.zipkin,
    "io.zipkin.reporter2" % "zipkin-sender-okhttp3" % Versions.zipkin
  )

  lazy val zio = Seq(
    "dev.zio" %% "zio" % Versions.zio,
    "dev.zio" %% "zio-interop-cats" % "2.0.0.0-RC13",
    "dev.zio" %% "zio-test" % Versions.zio % Test,
    "dev.zio" %% "zio-test-sbt" % Versions.zio % Test
  )

  lazy val zipkin = Seq(
    "io.zipkin.reporter2" % "zipkin-reporter" % Versions.zipkin,
    "io.zipkin.reporter2" % "zipkin-sender-okhttp3" % Versions.zipkin
  )

  lazy val http4s = Seq(
    "org.http4s" %% "http4s-core" % Versions.http4s,
    "org.http4s" %% "http4s-blaze-server" % Versions.http4s,
    "org.http4s" %% "http4s-dsl" % Versions.http4s,
    "org.http4s" %% "http4s-circe" % Versions.http4s
  )

  lazy val circe = Seq(
    "io.circe" %% "circe-generic-extras" % Versions.circe
  )

  // Tests
  lazy val scalaTest = "org.scalatest" %% "scalatest" % Versions.scalaTest % Test

  // Compiler
  lazy val betterMonadicFor = "com.olegpy" %% "better-monadic-for" % Versions.betterMonadicFor
  lazy val kindProjector = "org.typelevel" %% "kind-projector" % Versions.kindProjector
}
