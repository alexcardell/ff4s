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

import cats.effect.Concurrent
import org.http4s.client.Client
import io.cardell.ff4s.flipt.auth.AuthenticationStrategy
import io.cardell.ff4s.flipt.auth.AuthMiddleware
import org.http4s.Uri
import io.cardell.ff4s.flipt.model.BooleanEvaluationResponse
import io.cardell.ff4s.flipt.model.VariantEvaluationResponse
import io.cardell.ff4s.flipt.model.BatchEvaluationRequest
import io.cardell.ff4s.flipt.model.BatchEvaluationResponse
import io.circe.Decoder
import io.cardell.ff4s.flipt.model.StructuredVariantEvaluationResponse

trait FliptApi[F[_]] {
  def evaluateBoolean(request: EvaluationRequest): F[BooleanEvaluationResponse]
  def evaluateVariant(request: EvaluationRequest): F[VariantEvaluationResponse]
  def evaluateStructuredVariant[A: Decoder](
      request: EvaluationRequest
  ): F[StructuredVariantEvaluationResponse[A]]
  def evaluateBatch(request: BatchEvaluationRequest): F[BatchEvaluationResponse]
}

object FliptApi {
  def apply[F[_]: Concurrent](
      client: Client[F],
      uri: Uri,
      strategy: AuthenticationStrategy
  ): FliptApi[F] =
    new FliptApiImpl[F](AuthMiddleware(client, strategy), uri)
}
