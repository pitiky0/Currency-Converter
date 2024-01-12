# Currency Converter App

## Overview

The Currency Converter App is a simple Android application that allows users to convert currency values based on real-time exchange rates.

## Features

- **Currency Conversion:** Convert currency from one denomination to another.
- **Real-time Exchange Rates:** Fetches the latest exchange rates from a reliable API.

## Screenshots

<div style="display:flex; flex-direction:row;">
  <img src="/screenshots/screenshot1.png" alt="App Screenshot 1" width="200"/>
  <img src="/screenshots/screenshot2.png" alt="App Screenshot 2" width="200"/>
</div>

## Prerequisites

Before running the app, make sure you have:

- Android Studio installed.
- An active internet connection.

## How to Use

1. Launch the app on your Android device or emulator.
2. Enter the amount in the "From" currency field.
3. Select the source currency from the dropdown.
4. Select the target currency from the second dropdown.
5. Press the "Convert" button to see the converted amount.

## Dependencies

The app uses the following external libraries:

- [CardView](https://developer.android.com/jetpack/androidx/releases/cardview) for enhanced card layout.
- [Spinner](https://developer.android.com/guide/topics/ui/controls/spinner) for dropdown selection.
- [Gson](https://github.com/google/gson) for JSON parsing.

## How to Build

To build and run the app locally:

1. Clone this repository.
2. Open the project in Android Studio.
3. Build and run the app on an Android emulator or physical device.

## Credits

This app uses exchange rate data from [ExchangeRate-API](https://www.exchangerate-api.com/).

## License

This project is licensed under the MIT License - see the [LICENSE](/LICENSE) file for details.
