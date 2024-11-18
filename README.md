[![Build](https://github.com/OlegCheban/WaterMarkIt/actions/workflows/mvn.yml/badge.svg)](https://github.com/OlegCheban/WaterMarkIt/actions/workflows/mvn.yml)
[![javadoc](https://img.shields.io/badge/javadoc-1.1.1-brightgreen.svg)](https://javadoc.io/doc/io.github.olegcheban/WaterMarkIt/latest/index.html)
[![Code climate](https://api.codeclimate.com/v1/badges/0cd17315421a1bec3587/maintainability)](https://codeclimate.com/github/OlegCheban/WaterMarkIt/maintainability)
[![Hits-of-Code](https://hitsofcode.com/github/OlegCheban/WaterMarkIt?branch=master)](https://hitsofcode.com/github/OlegCheban/WaterMarkIt/view?branch=master)
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg?style=flat-square)](https://makeapullrequest.com)
[![License](https://img.shields.io/badge/license-MIT-green.svg)](https://github.com/OlegCheban/WaterMarkIt/blob/master/LICENSE)
# WaterMarkIt

A lightweight Java library for adding watermarks to various file types, including PDFs and images. The library was developed to address the challenge of creating watermarks that cannot be easily removed from PDF files. Many PDF editors allow users to edit even secured files, and when a watermark is added as a separate layer, it can be easily removed. The library provides the `WatermarkMethod.DRAW` method to address the issue, whereas the `WatermarkMethod.OVERLAY` one adds a separate layer that can be easily removed.  

## Features

- **DSL**: Provides a user-friendly way to configure and apply watermarks with ease.
- **Unremovable Watermarks**: Ensures that watermarks applied to PDF files are designed to be unremovable.
- **Customizable Watermarks**: Customize various aspects of your watermark, including:
  - Text
  - Color
  - Size
  - Position
  - Rotation
  - Opacity
  - Trademark
  - DPI


- **Multithreading**: Leverages a thread pool for efficient watermarking. Particularly useful for the `WatermarkMethod.DRAW` approach and multi-page files such as PDFs, enabling parallel watermarking with a separate thread for each page.
- **Supported Formats**:
  - PDF
  - JPEG
  - PNG
  - TIFF
  - BMP


- **Types of Watermarks**:
  - Text-based watermarks
  - Image-based watermarks

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
    <version>1.1.1</version>
</dependency>
```

**For Gradle**, add the following to your `build.gradle`:
```kotlin
implementation 'io.github.olegcheban:WaterMarkIt:1.1.1'
```

### Usage

```java
try (var document = new PDDocument()) {
    document.addPage(new PDPage());
    document.addPage(new PDPage());
    document.addPage(new PDPage());
    
    byte[] result =
            WatermarkService.textBasedWatermarker(
                            Executors.newFixedThreadPool(
                                    Runtime.getRuntime().availableProcessors()
                            )
                    )
                    .watermark(document)
                        .withText("CONFIDENTIAL").ofSize(20)
                        .usingMethod(WatermarkMethod.OVERLAY)
                        .atPosition(WatermarkPosition.TOP_LEFT)
                        .inColor(Color.RED)
                    .and()
                        .withText("Copyright © 2024").ofSize(10)
                        .usingMethod(WatermarkMethod.OVERLAY)
                        .atPosition(WatermarkPosition.BOTTOM_LEFT)
                        .inColor(Color.BLACK)
                        .withOpacity(0.5f)
                    .and()
                        .withText("Your Company Name").ofSize(200)
                        .usingMethod(WatermarkMethod.DRAW)
                        .atPosition(WatermarkPosition.CENTER)
                        .withDpi(300f)
                        .rotate(25)
                        .withTrademark()
                        .inColor(Color.BLUE)
                    .apply();
}
```
![Screenshot](https://i.imgur.com/ww4gtmbm.png)

```java
    byte[] result =
            WatermarkService.textBasedWatermarker()
                    .watermark(readFileFromClasspathAsBytes("file.pdf"), FileType.PDF)
                    .withText("WaterMarkIt").ofSize(194)
                    .usingMethod(WatermarkMethod.DRAW)
                    .atPosition(WatermarkPosition.TILED)
                    .inColor(Color.RED)
                    .withOpacity(0.1f)                    
                    .apply();
```
![Screenshot](https://i.imgur.com/EO9AGeum.png)

```java
try (var document = new PDDocument()) {
    document.addPage(new PDPage());    
    
    byte[] result =
            WatermarkService.imageBasedWatermarker()
                    .watermark(document)                    
                    .withImage(readFileFromClasspathAsBytes("logo.png"))                    
                    .withOpacity(0.3f)
                    .apply();
    
}
```


### Dependencies 
- **Apache PDFBox**: [Apache PDFBox](https://pdfbox.apache.org/) - A Java library for working with PDF documents.
- **JAI Image I/O**: [JAI Image I/O](https://github.com/jai-imageio/jai-imageio-core) - Image I/O library for Java, supporting various image formats.
- **commons-logging**: [Apache Commons Logging](https://commons.apache.org/proper/commons-logging/) - A simple logging facade for Java.