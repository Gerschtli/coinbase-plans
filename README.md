# Coinbase Plans

## Run tests

```sh
./gradlew clean build
```

Following environment variables are necessary:

* `APP_COINBASE_AUTH_API_KEY`
* `APP_COINBASE_AUTH_PASSPHRASE`
* `APP_COINBASE_AUTH_SECRET`

These infos need to be created manually on <https://public.sandbox.pro.coinbase.com/> with View and Trade permissions.
Furthermore, there need to be some amount of EUR. Can be easily transferred only via the web UI.

## Update gdax lib

```sh
cd gdax-java
./gradlew clean build shadowJar
cp build/libs/coinbase-pro-java-0.11.0-all.jar ../libs
```

*Note*: Run with java 11
