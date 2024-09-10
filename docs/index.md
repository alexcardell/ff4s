# ff4s - Feature Flags For Scala

## Features

- [x] Cross-platform, cross-version Scala feature flagging
- [x] [Flipt](https://flipt.io) client
- [ ] :construction: [OpenFeature](https://openfeature.dev) SDK
- [ ] :construction: Flipt OpenFeature provider
- [ ] LaunchDarkly OpenFeature provider with [Catalyst](https://typelevel.org/catapult)

## Installing

```scala
libraryDependencies += "io.cardell" %%% "ff4s-flipt-server-sdk" % "@VERSION@"
```

## OpenFeature Usage

The OpenFeature SDK adds features like handling default values in case of errors.
Eventually the SDK will cover the full range of the [openfeature](https://openfeature.dev)
specification, like hooks, events, static vs dynamic context.

See `Flipt usage` on how to set up the `FliptApi`. Once done, set up a provider:

```scala mdoc
import cats.effect.IO
import io.cardell.openfeature.OpenFeature
import io.cardell.ff4s.flipt.FliptApi
import io.cardell.openfeature.provider.flipt.FliptProvider

def provider(flipt: FliptApi[IO]) = {
    val featureSdk = OpenFeature[IO](new FliptProvider[IO](flipt, "some-namespace"))

    featureSdk.client.flatMap { featureClient =>
        featureClient.getBooleanValue("boolean-flag", false)
    }
}
```

## Flipt Usage

The Flipt client is bare-bones, using it is not recommended, unless as OpenFeature SDK Provider.

```scala mdoc
import cats.effect.IO
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.Uri
import io.cardell.ff4s.flipt.FliptApi
import io.cardell.ff4s.flipt.EvaluationRequest
import io.cardell.ff4s.flipt.auth.AuthenticationStrategy

val url: Uri = Uri.unsafeFromString("https://flipt.example.com")
val token: String = "token"

val resource = EmberClientBuilder
    .default[IO]
    .build
    .map(client =>
        FliptApi[IO](client, url, AuthenticationStrategy.ClientToken("token"))
    )

resource.use { flipt => 
    for {
        res <- flipt.evaluateBoolean(
            EvaluationRequest(
                namespaceKey = "default",
                flagKey = "my-flag-1",
                entityId = None,
                context = Map.empty,
                reference = None
            )
        )
    } yield res.enabled
}
```

