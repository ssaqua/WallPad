<h1 align="center"><img src="/app/src/main/res/mipmap-xxxhdpi/ic_launcher.png" align="top" height="40">WallPad</h1>

<h4 align="center">Android app for discovering new wallpapers for your device powered by the Bing Image Search API</h4>

A simple but non-trival demo application showcasing usage of some Android Architecture Components and testing techniques.

<img src="https://user-images.githubusercontent.com/5179255/64071719-2ce74d80-ccd4-11e9-950a-27b97dcaec04.gif" height="480"/>

## Development

Supply your own API keys for the [Azure Cognitive Services Search APIs](https://azure.microsoft.com/en-us/services/cognitive-services/bing-image-search-api/).

Place them into `keys.properties` in the project root like so:

```
bingDebugKey="..."
bingReleaseKey="..."
```

## Test

`./gradlew testDebug` for local unit tests with output to `app/build/reports/tests/testDebugUnitTest`

`./gradlew connectedCheck` for instrumented tests with output to `app/build/reports/androidTests/connected/`

## License

    Copyright 2019 ssaqua

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
