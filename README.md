# Starter Kit WebView Android

> **WARNING**: This starter kit is still in development. Do not use it in production.

## Universal Starter Kit

This starter kit is meant to be used with [Starter Kit Laravel](https://github.com/spektasoft/starter-kit-laravel).

## Local Development

1. Download this repository and extract it anywhere in your local environment.

1. Open the project using `Android Studio`.

1. Rename `rootProject.name`, `namespace`, and `applicationId`. You can search for `com.spektasoft.starterkit` and `Starter Kit` for a complete rename.

1. Rename folders based on your `applicationId`.

1. Create the `env.properties` file from the `env.properties.example` located in `app/src/main/assets`:

   - `APP_URL` is your local machine or server `IP Address` instead of `localhost` (e.g.: http://192.168.1.1:8000).

   > **INFO**: `env.properties` without dot (.) as prefix.

   > **DANGER**: This `env.properties` file is **NOT** for storing sensitive data. All variables within can be read as plain text.

1. Run 'app'.

## Attributions

[ComposeWebKit (Dev Atrii)](https://github.com/DevAtrii/ComposeWebKit)
[SuperWebView](https://github.com/roozbehzarei/SuperWebView)

## License

The starter kit is open-sourced software licensed under the [MIT license](LICENSE).
