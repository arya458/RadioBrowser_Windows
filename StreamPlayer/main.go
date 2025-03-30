package main

import (
	"bufio"
	"flag"
	"fmt"
	"io"
	"net/http"
	"os"
	"os/exec"
	"os/signal"
	"path/filepath"
	"runtime"
	"syscall"
	"time"
)

var (
	url      = flag.String("url", "", "Stream URL to play")
	volume   = flag.Int("volume", 100, "Volume level (0-100)")
	device   = flag.String("device", "", "Audio device to use")
	format   = flag.String("format", "mp3", "Stream format (mp3, aac, etc.)")
	player   *exec.Cmd
	stopChan = make(chan struct{})
)

func main() {
	flag.Parse()

	if *url == "" {
		fmt.Println("Please provide a stream URL using -url flag")
		os.Exit(1)
	}

	// Handle interrupt signals
	sigChan := make(chan os.Signal, 1)
	signal.Notify(sigChan, syscall.SIGINT, syscall.SIGTERM)

	// Start the stream
	go playStream()

	// Wait for interrupt signal
	<-sigChan
	stopStream()
}

func playStream() {
	// Determine the appropriate player command based on the OS
	var cmd *exec.Cmd
	switch runtime.GOOS {
	case "windows":
		cmd = exec.Command("ffplay", "-nodisp", "-autoexit", "-volume", fmt.Sprintf("%d", *volume), *url)
	case "darwin":
		cmd = exec.Command("ffplay", "-nodisp", "-autoexit", "-volume", fmt.Sprintf("%d", *volume), *url)
	case "linux":
		cmd = exec.Command("ffplay", "-nodisp", "-autoexit", "-volume", fmt.Sprintf("%d", *volume), *url)
	default:
		fmt.Printf("Unsupported operating system: %s\n", runtime.GOOS)
		os.Exit(1)
	}

	// Set up pipes for communication
	stdout, err := cmd.StdoutPipe()
	if err != nil {
		fmt.Printf("Error setting up stdout pipe: %v\n", err)
		os.Exit(1)
	}

	stderr, err := cmd.StderrPipe()
	if err != nil {
		fmt.Printf("Error setting up stderr pipe: %v\n", err)
		os.Exit(1)
	}

	// Start the player
	if err := cmd.Start(); err != nil {
		fmt.Printf("Error starting player: %v\n", err)
		os.Exit(1)
	}

	player = cmd

	// Monitor the stream
	go monitorStream(stdout, stderr)

	// Wait for the player to finish
	if err := cmd.Wait(); err != nil {
		fmt.Printf("Player finished with error: %v\n", err)
	}
}

func monitorStream(stdout, stderr io.ReadCloser) {
	// Monitor stdout
	go func() {
		scanner := bufio.NewScanner(stdout)
		for scanner.Scan() {
			line := scanner.Text()
			fmt.Printf("Player output: %s\n", line)
		}
	}()

	// Monitor stderr
	go func() {
		scanner := bufio.NewScanner(stderr)
		for scanner.Scan() {
			line := scanner.Text()
			fmt.Printf("Player error: %s\n", line)
		}
	}()

	// Monitor the stream URL
	go func() {
		for {
			select {
			case <-stopChan:
				return
			default:
				resp, err := http.Get(*url)
				if err != nil {
					fmt.Printf("Error checking stream: %v\n", err)
					time.Sleep(5 * time.Second)
					continue
				}
				resp.Body.Close()
				time.Sleep(30 * time.Second)
			}
		}
	}()
}

func stopStream() {
	if player != nil {
		close(stopChan)
		player.Process.Kill()
		player.Wait()
	}
}

// Helper function to get the executable path
func getExecutablePath() string {
	ex, err := os.Executable()
	if err != nil {
		return ""
	}
	return filepath.Dir(ex)
}

// Helper function to check if a URL is valid
func isValidURL(url string) bool {
	resp, err := http.Get(url)
	if err != nil {
		return false
	}
	defer resp.Body.Close()
	return resp.StatusCode == http.StatusOK
}
