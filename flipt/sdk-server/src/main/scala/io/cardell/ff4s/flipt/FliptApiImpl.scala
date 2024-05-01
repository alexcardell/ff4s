/*
 * Copyright 2023 Alex Cardell
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.cardell.ff4s.flipt

import org.http4s.circe.CirceEntityCodec._
import cats.effect.Concurrent
import org.http4s.client.Client
import org.http4s.Request
import org.http4s.Method
import org.http4s.Uri
import io.cardell.ff4s.flipt.model.BooleanEvaluationResponse
import io.cardell.ff4s.flipt.model.VariantEvaluationResponse
import io.cardell.ff4s.flipt.model.BatchEvaluationRequest
import io.cardell.ff4s.flipt.model.BatchEvaluationResponse
import io.circe.Decoder

protected[flipt] class FliptApiImpl[F[_]: Concurrent](
    client: Client[F],
    baseUri: Uri
) extends FliptApi[F] {

  override def evaluateBoolean(
      request: EvaluationRequest
  ): F[BooleanEvaluationResponse[Unit]] = {
    val req = Request[F](
      method = Method.POST,
      uri = baseUri / "evaluate" / "v1" / "boolean"
    ).withEntity(request)

    client.expect[BooleanEvaluationResponse[Unit]](req)
  }

  override def evaluateVariant[A: Decoder](
      request: EvaluationRequest
  ): F[VariantEvaluationResponse[A]] = {
    val req = Request[F](
      method = Method.POST,
      uri = baseUri / "evaluate" / "v1" / "variant"
    ).withEntity(request)

    client.expect[VariantEvaluationResponse[A]](req)
  }

  override def evaluateBatch[A: Decoder](
      request: BatchEvaluationRequest
  ): F[BatchEvaluationResponse[A]] = {
    val req = Request[F](
      method = Method.POST,
      uri = baseUri / "evaluate" / "v1" / "batch"
    ).withEntity(request)

    client.expect[BatchEvaluationResponse[A]](req)
  }

}
