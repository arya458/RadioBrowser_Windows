# Stream Player

A simple and efficient stream player written in Go that can play various audio streams using FFplay.

## Features

- Play various audio stream formats (MP3, AAC, etc.)
- Volume control
- Stream monitoring and error handling
- Cross-platform support (Windows, macOS, Linux)
- Graceful shutdown handling

## Prerequisites

1. Go 1.16 or later
2. FFplay (part of FFmpeg)

### Installing FFmpeg

#### Windows
1. Download FFmpeg from https://ffmpeg.org/download.html
2. Extract the archive
3. Add the FFmpeg bin directory to your system PATH

#### macOS
```bash
brew install ffmpeg
```

#### Linux
```bash
sudo apt-get install ffmpeg  # Ubuntu/Debian
sudo dnf install ffmpeg      # Fedora
```

## Building

```bash
cd StreamPlayer
go build -o streamplayer
```

## Usage

Basic usage:
```bash
./streamplayer -url "http://example.com/stream.mp3"
```

Options:
- `-url`: The URL of the stream to play (required)
- `-volume`: Volume level (0-100, default: 100)
- `-device`: Audio device to use (optional)
- `-format`: Stream format (mp3, aac, etc., default: mp3)

Example with options:
```bash
./streamplayer -url "http://example.com/stream.mp3" -volume 80 -format mp3
```

## Integration with Radio Browser

This player can be used as a backend for the Radio Browser application. The Radio Browser will call this player with the appropriate stream URL when a station is selected.

## Error Handling

The player includes built-in error handling for:
- Invalid URLs
- Stream connection issues
- FFplay errors
- System signals (Ctrl+C)

## License

MIT License 