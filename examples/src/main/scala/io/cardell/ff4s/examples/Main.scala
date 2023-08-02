package io.cardell.ff4s.examples

import cats.MonadThrow
import cats.effect.IO
import cats.effect.IOApp
import cats.syntax.all._
import fliptapi.FliptOpenapiService
import org.http4s.client.Client
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.implicits._
import smithy4s.http4s.SimpleRestJsonBuilder

import io.cardell.ff4s.FlagCase
import io.cardell.ff4s.flipt.FliptFlagsClient

object Main extends IOApp.Simple {
  def log(msg: String) = IO.println(msg)

  val fliptUri = uri"http://localhost:8080"

  def setup(client: Client[IO]): IO[FliptOpenapiService[IO]] = {
    val maybeService = SimpleRestJsonBuilder(FliptOpenapiService)
      .client[IO](client)
      .uri(fliptUri)
      .make

    MonadThrow[IO].fromEither(maybeService)
  }

  val client = EmberClientBuilder.default[IO].build

  def run: IO[Unit] =
    client
      .evalMap(client => setup(client).tupleRight(client))
      .use { case (service, client) =>
        val makeFlags = FliptFlagsClient[IO](
          client,
          fliptUri,
          "default"
        )

        val smithyExample = for {
          res <- service.flagsServiceGet("default", "example-flag-1")
          _ <- log(res.toString)
          _ <- IO(assert(res.body.enabled == true))
          _ <- log("success")
        } yield ()

        val ff4sExample = for {
          flags <- makeFlags
          res <- flags.get("example-flag-1").flatMap {
            case FlagCase.On(_) => IO.pure(3)
            case FlagCase.Off   => IO.pure(0)
          }
          _ <- IO(assert(res == 3))
        } yield ()

        for {
          _ <- smithyExample
          _ <- ff4sExample
        } yield ()
      }
}
