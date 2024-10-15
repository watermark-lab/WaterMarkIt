[![Build](https://github.com/OlegCheban/WaterMarkIt/actions/workflows/mvn.yml/badge.svg)](https://github.com/OlegCheban/WaterMarkIt/actions/workflows/mvn.yml)
[![javadoc](https://img.shields.io/badge/javadoc-1.1.1-brightgreen.svg)](https://javadoc.io/doc/io.github.olegcheban/WaterMarkIt/latest/index.html)
[![Code climate](https://api.codeclimate.com/v1/badges/0cd17315421a1bec3587/maintainability)](https://codeclimate.com/github/OlegCheban/WaterMarkIt/maintainability)
[![Hits-of-Code](https://hitsofcode.com/github/OlegCheban/WaterMarkIt?branch=master)](https://hitsofcode.com/github/OlegCheban/WaterMarkIt/view?branch=master)
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg?style=flat-square)](https://makeapullrequest.com)
[![License](https://img.shields.io/badge/license-MIT-green.svg)](https://github.com/OlegCheban/WaterMarkIt/blob/master/LICENSE)
# WaterMarkIt

A lightweight Java library for adding watermarks to various file types, including PDFs and images. The library was developed to address the challenge of creating watermarks that cannot be easily removed from PDF files. Many PDF editors allow users to edit even secured files, and when a watermark is added as a separate layer, it can be easily removed.

## Features

- **DSL**: Provides a user-friendly way to configure and apply watermarks.
- **Unremovable Watermarks**: Designed to watermark PDF files in a way that the watermark cannot be removed.
- **Customizable Watermarks**: text, color, size, position, rotation, DPI, etc.
- **Multithreading**: Use an `Executor` for watermarking. This is relevant for multi-page files like PDFs to apply watermarks in parallel.
- **Supported Formats**: PDF, JPEG, PNG, TIFF, BMP.

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

Here’s a quick example of how to use the WaterMarkIt library:

```java
try (var document = new PDDocument()) {
    document.addPage(new PDPage());
    document.addPage(new PDPage());
    document.addPage(new PDPage());
    
    byte[] result =
            WatermarkService.create(
                            Executors.newFixedThreadPool(
                                    Runtime.getRuntime().availableProcessors()
                            )
                    )
                    .watermark(plainDocument)
                        .withText("CONFIDENTIAL").ofSize(20)
                        .usingMethod(WatermarkMethod.OVERLAY)
                        .atPosition(WatermarkPosition.TOP_LEFT)
                        .inColor(Color.RED)
                    .and()
                        .withText("Copyright © 2024").ofSize(10)
                        .usingMethod(WatermarkMethod.OVERLAY)
                        .atPosition(WatermarkPosition.BOTTOM_LEFT)
                        .inColor(Color.BLACK)
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
As a result, the PDF contains three watermarks applied in different positions and using different styles.

![Screenshot](https://i.imgur.com/ww4gtmbm.png)

### Libraries
- **Apache PDFBox**: [Apache PDFBox](https://pdfbox.apache.org/) - A Java library for working with PDF documents.
- **JAI Image I/O**: [JAI Image I/O](https://github.com/jai-imageio/jai-imageio-core) - Image I/O library for Java, supporting various image formats.
- **commons-logging**: [Apache Commons Logging](https://commons.apache.org/proper/commons-logging/) - A simple logging facade for Java.