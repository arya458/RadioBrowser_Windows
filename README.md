# RadioBrowser Windows

A modern desktop application for browsing and streaming radio stations, built with Kotlin Multiplatform and Compose Desktop.

![RadioBrowser Windows](docs/assets/screenshot.png)

## Features

- 🌍 **Global Radio Stations**: Access thousands of radio stations from around the world
- 🔍 **Smart Search**: Find your favorite stations with advanced search filters
- ❤️ **Favorites**: Save and organize your favorite radio stations
- 🎨 **Modern UI**: Beautiful and intuitive interface built with Compose Desktop
- 🌓 **Dark/Light Theme**: Switch between dark and light themes for comfortable viewing
- 🎵 **High-Quality Audio**: Stream radio stations with optimal audio quality
- ⚡ **Fast & Reliable**: Built with Kotlin Multiplatform for optimal performance

## Tech Stack

- **Kotlin Multiplatform**: For cross-platform development
- **Compose Desktop**: For modern, declarative UI
- **Koin**: For dependency injection
- **Ktor**: For network requests
- **SQLite**: For local data storage
- **KLite/VLC**: For media playback

## Prerequisites

- Windows 10 or later
- Java Runtime Environment (JRE) 11 or later
- VLC Media Player or KLite Codec Pack installed

## Installation

1. Download the latest release from the [Releases](https://github.com/arya458/RadioBrowser_Windows/releases) page
2. Run the installer
3. Follow the installation wizard
4. Launch RadioBrowser Windows

## Development Setup

1. Clone the repository:
```bash
git clone https://github.com/arya458/RadioBrowser_Windows.git
cd RadioBrowser_Windows
```

2. Open the project in your preferred IDE (IntelliJ IDEA recommended)

3. Build the project:
```bash
./gradlew build
```

4. Run the application:
```bash
./gradlew :composeApp:run
```

## Project Structure

```
RadioBrowser_Windows/
├── composeApp/
│   ├── src/
│   │   ├── commonMain/        # Shared code
│   │   ├── desktopMain/       # Desktop-specific code
│   │   └── resources/         # Application resources
│   └── build.gradle.kts       # Build configuration
├── docs/                      # Documentation
└── README.md                  # This file
```

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request. For major changes, please open an issue first to discuss what you would like to change.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- [Radio Browser API](https://www.radio-browser.info/) for providing the radio station data
- [Compose Desktop](https://www.jetbrains.com/lp/compose-desktop/) for the UI framework
- [Kotlin](https://kotlinlang.org/) for the programming language
- [VLC](https://www.videolan.org/) and [KLite](https://codecguide.com/download_kl.htm) for media playback

## Author

**Aria Danesh**
- GitHub: [arya458](https://github.com/arya458)
- LinkedIn: [Aria Danesh](https://www.linkedin.com/in/aria-danesh-574ab8162/)

## Support

If you encounter any issues or have suggestions, please open an issue in the GitHub repository.