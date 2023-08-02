package io.cardell.ff4s.flipt

import cats.Monad
import cats.MonadThrow
import cats.effect.kernel.Concurrent
import cats.syntax.all._
import fliptapi.FliptOpenapiService
import org.http4s.Uri
import org.http4s.client.Client
import smithy4s.http4s.SimpleRestJsonBuilder

import io.cardell.ff4s.FlagCase
import io.cardell.ff4s.Flags
import io.cardell.ff4s.Key

protected class FliptApiFlags[F[_]: Monad](
    fliptApi: FliptOpenapiService[F],
    namespace: String
) extends Flags[F] {

  def get(key: Key): F[FlagCase] =
    fliptApi
      .flagsServiceGet(namespace, key)
      .map(_.body.enabled)
      .map {
        case true  => FlagCase.On()
        case false => FlagCase.Off
      }

}

object FliptFlagsClient {

  def apply[F[_]: Concurrent](
      client: Client[F],
      uri: Uri,
      namespace: String
  ): F[Flags[F]] = {
    val maybeService = SimpleRestJsonBuilder(FliptOpenapiService)
      .client[F](client)
      .uri(uri)
      .make

    MonadThrow[F]
      .fromEither(maybeService)
      .map(new FliptApiFlags[F](_, namespace))
  }

}
