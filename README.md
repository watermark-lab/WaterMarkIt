[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg?style=flat-square)](https://makeapullrequest.com)
[![Build](https://github.com/OlegCheban/WaterMarkIt/actions/workflows/mvn.yml/badge.svg)](https://github.com/OlegCheban/WaterMarkIt/actions/workflows/mvn.yml)
[![javadoc](https://javadoc.io/badge2/io.github.olegcheban/WaterMarkIt/javadoc.svg)](https://javadoc.io/doc/io.github.olegcheban/WaterMarkIt/latest/com/markit/services/WatermarkService.html)
# WaterMarkIt

A lightweight Java library for adding watermarks to various file types, including PDFs and images. WaterMarkIt was developed to address the challenge of creating watermarks that cannot be easily removed, providing a robust solution for document and image protection.

## Features

- **DSL**: Provides a user-friendly way to configure and apply watermarks.
- **Unremovable Watermarks**: Designed to watermark PDF files in a way that the watermark cannot be removed.
- **Customizable Watermarks**: Add text, color, size, position and trademarks with customizable DPI settings.
- **Asynchronous Operations**: Use an `Executor` for asynchronous watermarking.

## Getting Started

### Prerequisites

- Java 11 or higher
- Maven or Gradle

### Installation

**For Maven**, add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>io.github.olegcheban</groupId>
    <artifactId>WaterMarkIt</artifactId>
    <version>1.1.0</version>
</dependency>
```

**For Gradle**, add the following to your `build.gradle`:
```kotlin
implementation 'io.github.olegcheban:WaterMarkIt:1.1.0'
```

### Usage

Here’s a quick example of how to use the WaterMarkIt library:

```java
var watermarkService =
        WatermarkService.create(
                Executors.newFixedThreadPool(
                        Runtime.getRuntime().availableProcessors()
                )
        );

try (var document = new PDDocument()) {
    document.addPage(new PDPage());
    byte[] result = 
            watermarkService
                .file(document, FileType.PDF)
                .text("Sample Watermark")
                .textSize(50)
                .position(WatermarkPosition.CENTER)
                .method(WatermarkMethod.DRAW)    
                .color(Color.BLUE)
                .trademark()
                .dpi(150f)
                .apply();
}
```
You can override any services. For instance, instead of using DefaultImageWatermarker, you can implement your own service.
```java
// Overriding the default image watermarking behavior
WatermarkService.create(
        (sourceImageBytes, fileType, watermarkText, watermarkColor, trademark) -> {
            // Custom logic to add a watermark to the image
            return sourceImageBytes;
        }, 
        new DefaultPdfWatermarker(),
        new DefaultOverlayPdfWatermarker(),
        new DefaultWatermarkPdfService()
);
```

### License
This project is licensed under the MIT License - see the LICENSE file for details.

### Libraries

- **Apache PDFBox**: [Apache PDFBox](https://pdfbox.apache.org/) - A Java library for working with PDF documents.
- **JAI Image I/O**: [JAI Image I/O](https://github.com/jai-imageio/jai-imageio-core) - Image I/O library for Java, supporting various image formats.
- **commons-logging**: [Apache Commons Logging](https://commons.apache.org/proper/commons-logging/) - A simple logging facade for Java.