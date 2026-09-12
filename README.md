[![Maintainability](https://qlty.sh/gh/OlegCheban/projects/WaterMarkIt/maintainability.svg)](https://qlty.sh/gh/OlegCheban/projects/WaterMarkIt)
[![Code Coverage](https://qlty.sh/gh/OlegCheban/projects/WaterMarkIt/coverage.svg)](https://qlty.sh/gh/OlegCheban/projects/WaterMarkIt)
[![Ask DeepWiki](https://deepwiki.com/badge.svg)](https://deepwiki.com/OlegCheban/WaterMarkIt)
[![javadoc](https://img.shields.io/badge/javadoc-1.4.2-brightgreen.svg)](https://javadoc.io/doc/io.github.watermark-lab/WaterMarkIt/latest/index.html)
![GitHub code size in bytes](https://img.shields.io/github/languages/code-size/OlegCheban/WaterMarkIt)
[![License](https://img.shields.io/badge/license-MIT-green.svg)](https://github.com/OlegCheban/WaterMarkIt/blob/master/LICENSE)
# WaterMarkIt

WaterMarkIt is a lightweight, framework-agnostic Java library for adding visual and audible watermarks to PDFs, images, videos, and audio files through a consistent fluent API.

Add text or image overlays to visual content, mix audio clips or locally synthesized speech into audio tracks, and choose how PDF watermarks are applied - including rendering-based processing that makes them significantly harder to remove with standard PDF editors.

## Why WaterMarkIt?

Before implementing watermarking from scratch in a Java application, consider WaterMarkIt when you need visual watermarks for PDFs, images, or videos, or audible watermarks for audio files. The library provides a type-safe fluent API and encapsulates PDF processing, FFmpeg command construction, audio mixing, temporary-resource management, and offline text-to-speech integration. This lets applications add watermarking without maintaining their own media-processing pipeline.

## Features

- **Internal DSL**: Provides a user-friendly way to configure and apply watermarks with ease, while also ensuring type safety at compilation time.

- **Types of Watermarks**:
  - Text-based watermarks
  - Image-based watermarks
  - Audible audio and text-to-speech watermarks

- **Customizable Watermarks**: Customize various aspects of your watermark, including:
  - Font
  - Color
  - Size
  - Position
  - Rotation
  - Opacity
  - DPI

- **Trademarks**: A capability to add the trademark symbol ® to text-based watermarks.

- **Page orientation support**: Full support for both portrait and landscape orientations.

- **Supported Formats**:
  - PDF
  - Images (JPEG, PNG, etc.)
  - Videos (MP4, MOV, AVI, MKV, etc)
  - Audio (WAV, MP3, FLAC, M4A/AAC, OGG, Opus, and AIFF)
  
- **Drawn Watermarks**: The library provides the `WatermarkingMethod.DRAW` method to add watermarks to PDF files that can't be easily removed. This mode generates an image from a PDF page, applies watermarks to the image, and replaces all layers of the page with the modified image.

- **Multithreading**: Leverages a thread pool for efficient watermarking. Particularly useful for the `WatermarkingMethod.DRAW` method and multi-page files such as PDFs, enabling parallel watermarking with a separate thread for each page.

## Getting Started

### Prerequisites

- Java 11 or higher
- Maven or Gradle

### Installation

**For Maven**, add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>io.github.watermark-lab</groupId>
    <artifactId>WaterMarkIt</artifactId>
    <version>1.4.2</version>
</dependency>
```

**For Gradle**, add the following to your `build.gradle`:
```kotlin
implementation 'io.github.watermark-lab:WaterMarkIt:1.4.2'
```

### Usage

```java
WatermarkService.create(
                //use a thread pool when necessary - for instance, for large PDFs with many pages
                Executors.newFixedThreadPool(
                        Runtime.getRuntime().availableProcessors()
                )
        )
        .watermarkPDF(new File("path/to/file.pdf"))
           .withImage(new File("path/to/watermark.png"))
           .position(WatermarkPosition.CENTER).end()
           .opacity(20)
        .and()
           .withText("WaterMarkIt")
               .font(Font.ARIAL)
               .bold()
               .color(Color.BLUE)
               .addTrademark()
               .end()           
           .position(WatermarkPosition.TILED)
               .adjust(35, 0)
               .horizontalSpacing(10)
               .end()
           .opacity(10)
           .rotation(25)
           .size(110)
        .and()
           .withText(LocalDateTime.now().toString()).end()
           .position(WatermarkPosition.TOP_RIGHT)
               .adjust(0, -30)
               .end()
           .size(50)
        .apply()
```
![Screenshot](https://github.com/user-attachments/assets/5d573ee8-ddf3-4204-8c33-502099bb39eb)

### Watermarking conditions 
```java
// skip the first page (the page index starts from 0)
WatermarkService.create()
    .watermarkPDF(document)
        .withText("Text-based Watermark").end()
            .pageFilter(index -> index >= 1)
    .apply()
```

```java
// don't add a watermark for the owner of the file; the owner has access to the original file.
WatermarkService.create()
    .watermarkPDF(document)
        .withText("Text-based Watermark").end()
            .enableIf(!isOwner)
    .apply()
```

```java
// Apply watermark only if the document has more than 3 pages
WatermarkService.create()
    .watermarkPDF(document)
        .withText("Text-based Watermark").end()
            .documentFilter(document -> document.getNumberOfPages() > 3)
    .apply()  
```

### Video Watermarking

The library supports adding watermarks to video files using FFmpeg. This feature allows you to apply both text-based and image-based watermarks to various video formats.

#### Prerequisites for Video Watermarking

- **FFmpeg**: The library requires FFmpeg to be installed on your system and available in the system PATH. FFmpeg is used internally to process video files and apply watermarks.

  **Installation:**
  - **Windows**: Download from [FFmpeg official website](https://ffmpeg.org/download.html) or use package managers like Chocolatey (`choco install ffmpeg`)
  - **macOS**: Use Homebrew (`brew install ffmpeg`)
  - **Linux**: Use your distribution's package manager (e.g., `sudo apt install ffmpeg` on Ubuntu)

```java
WatermarkService.create()
    .watermarkVideo(videoFile)
        .withText("WaterMarkIt")
            .color(Color.RED)
            .end()
        .opacity(50)
        .position(WatermarkPosition.CENTER).end()
        .size(30)
    .and()
        .withImage(logoFile)
        .position(WatermarkPosition.BOTTOM_RIGHT).end()
        .size(8)
    .apply();
```

### Audio Watermarking

Audio watermarks are audible layers mixed into the original audio stream. FFmpeg must be
installed and available on the system `PATH`.

Mix an audio-file watermark at 25% of its original (unity) gain:

```java
byte[] result = WatermarkService.create()
    .watermarkAudio(source)
        .withAudio(watermark)
        .volume(25)
    .apply();
```

Generate an offline spoken watermark and start it after 15 seconds:

```java
import com.markit.audio.tts.FreeTtsVoice;

byte[] result = WatermarkService.create()
    .watermarkAudio(source)
        .withText("WaterMarkIt")
            .voice(FreeTtsVoice.KEVIN)
            .end()
        .volume(20)
        .startAt(Duration.ofSeconds(15))
    .apply();
```

Multiple watermark layers are mixed in one FFmpeg invocation:

```java
byte[] result = WatermarkService.create()
    .watermarkAudio(source)
        .withAudio(chime)
        .volume(25)
        .startAt(Duration.ofSeconds(3))
    .and()
        .withText("Property of WaterMarkIt")
            .end()
        .volume(15)
        .startAt(Duration.ofSeconds(20))
    .apply();
```

`volume` controls only watermark gain (`0` is silent, `100` is unity). Source gain is
not normalized, so loud mixes may clip. Output duration matches the source and delayed
watermarks are truncated at its end.

File sources preserve WAV, MP3, FLAC, M4A/AAC, OGG, Opus, and AIFF containers.
Unknown extensions and `byte[]` sources produce WAV output; FFmpeg detects input bytes
by content.

Text uses offline FreeTTS with the US-English `FreeTtsVoice.KEVIN_16` voice by default.
Typed constants `KEVIN` (8 kHz) and `KEVIN_16` (16 kHz) prevent invalid voice names.
Custom voices and engines are supported through the `TextToSpeechVoice` and
`TextToSpeechEngine` SPI without network services.

## Extensibility and Customization

The library uses Java's ServiceLoader mechanism to load implementations of various services. You can override the services that implement the `Prioritizable` interface.

1. Create your own implementation of the desired service interface
2. Implement the `getPriority()` method to return a value higher than the default implementation
3. Register your implementation in the `META-INF/services` directory

```java
public class CustomPdfWatermarker implements DrawPdfWatermarker {
    @Override
    public int getPriority() {
        // Return a value higher than default to take precedence
        return Prioritizable.DEFAULT_PRIORITY + 1;
    }
    
    @Override
    public void watermark(PDDocument document, int pageIndex, List<WatermarkAttributes> attrs) throws IOException {
        // Custom watermarking implementation
    }
}
```

Then create a file `META-INF/services/com.markit.pdf.draw.DrawPdfWatermarker` containing:
```
com.example.CustomPdfWatermarker
```

## Why Kotlin?

While WaterMarkIt is primarily a Java library targeting Java 11 for better compatibility, we selectively use Kotlin in specific areas to enhance code quality and developer experience:

### Use Cases

- **Test Code**: Kotlin's concise syntax and powerful testing features make our test suite more readable and maintainable
- **Data Classes**: Java 11 lacks records (introduced in Java 14+), so we use Kotlin's data classes for immutable value objects
- **Enums**: Kotlin enums provide cleaner syntax for associating data with enum values
- **Exception Classes**: Custom exceptions benefit from Kotlin's concise class declarations with automatic constructor generation

### For Contributors

When contributing to WaterMarkIt:
- **Use Java** for all public APIs and core library functionality
- **Use Kotlin** for test classes, internal data structures, and utility classes where it provides clear benefits

## Dependencies 
- **Apache PDFBox**: [Apache PDFBox](https://pdfbox.apache.org/) - A Java library for working with PDF documents.
- **JAI Image I/O**: [JAI Image I/O](https://github.com/jai-imageio/jai-imageio-core) - Image I/O library for Java, supporting various image formats.
- **FreeTTS**: [JVoiceXML FreeTTS](https://github.com/JVoiceXML/FreeTTS) - Offline US-English speech synthesis for audible text watermarks.

## Contributing

We welcome contributions from the community! If you'd like to contribute to WaterMarkIt, please read our [Contributing Guide](CONTRIBUTING.md) for details on how to get started, our coding standards, and the pull request process.

Your contributions help make WaterMarkIt better for everyone!
