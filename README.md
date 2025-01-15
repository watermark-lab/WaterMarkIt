[![Build](https://github.com/OlegCheban/WaterMarkIt/actions/workflows/mvn.yml/badge.svg)](https://github.com/OlegCheban/WaterMarkIt/actions/workflows/mvn.yml)
[![javadoc](https://img.shields.io/badge/javadoc-1.3.0-brightgreen.svg)](https://javadoc.io/doc/io.github.watermark-lab/WaterMarkIt/latest/index.html)
[![Code climate](https://api.codeclimate.com/v1/badges/0cd17315421a1bec3587/maintainability)](https://codeclimate.com/github/OlegCheban/WaterMarkIt/maintainability)
[![Hits-of-Code](https://hitsofcode.com/github/OlegCheban/WaterMarkIt?branch=master)](https://hitsofcode.com/github/OlegCheban/WaterMarkIt/view?branch=master)
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg?style=flat-square)](https://makeapullrequest.com)
[![License](https://img.shields.io/badge/license-MIT-green.svg)](https://github.com/OlegCheban/WaterMarkIt/blob/master/LICENSE)
# WaterMarkIt

A lightweight Java library for adding watermarks to various file types, including PDFs and images. The library was developed to address the challenge of creating watermarks that cannot be easily removed from PDF files. Many PDF editors allow users to edit even secured files, and when a watermark is added as a separate layer, it can be easily removed.  

## Features

- **DSL**: Provides a user-friendly way to configure and apply watermarks with ease.

- **Types of Watermarks**:
  - Text-based watermarks
  - Image-based watermarks

- **Customizable Watermarks**: Customize various aspects of your watermark, including:
  - Text
  - Color
  - Size
  - Position
  - Rotation
  - Opacity
  - Trademark
  - DPI
 
- **Supported Formats**:
  - PDF
  - JPEG
  - PNG
  - TIFF
  - BMP
 
- **Unremovable Watermarks**: Ensures that watermarks applied to PDF files are designed to be unremovable. The library provides the `WatermarkingMethod.DRAW` method to address the issue, whereas the `WatermarkingMethod.OVERLAY` one adds a separate layer that can be easily removed.

- **Multithreading**: Leverages a thread pool for efficient watermarking. Particularly useful for the `WatermarkingMethod.DRAW` approach and multi-page files such as PDFs, enabling parallel watermarking with a separate thread for each page.

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
    <version>1.3.0</version>
</dependency>
```

**For Gradle**, add the following to your `build.gradle`:
```kotlin
implementation 'io.github.watermark-lab:WaterMarkIt:1.3.0'
```

### Usage

```java
WatermarkService.create(
                //using a thread pool here isn't mandatory; use it only when necessary
                Executors.newFixedThreadPool(
                        Runtime.getRuntime().availableProcessors()
                )
        )
        .watermarkPDF(readFileFromClasspathAsBytes("ITIL.pdf")
           .withImage(readFileFromClasspathAsBytes("logo.png"))
               .position(WatermarkPosition.CENTER)
               .opacity(0.2f)
           .and()
           .withText("WaterMarkIt")
               .addTrademark()
               .color(Color.BLUE)
               .watermark()
                   .size(110)
                   .position(WatermarkPosition.TILED)
                       .adjust(35, 0)
                   .opacity(0.1f)
                   .rotation(25)
           .and()
           .withText(LocalDateTime.now().toString())
               .watermark()
                   .position(WatermarkPosition.TOP_RIGHT)
                       .adjust(0, -30)
                   .size(50)
        .apply()
```
![Screenshot](https://github.com/user-attachments/assets/5d573ee8-ddf3-4204-8c33-502099bb39eb)

#### Watermarking conditions 
```java
// skip the first page (the page index starts from 0)
WatermarkService.create()
    .watermarkPDF(document)
        .withText("Text-based Watermark")
            .pageFilter(index -> index >= 1)
    .apply()
```

```java
// don't add a watermark for the owner of the file; the owner has access to the original file.
WatermarkService.create()
    .watermarkPDF(document)
        .withText("Text-based Watermark")
            .when(!isOwner)
    .apply()
```

```java
// Apply watermark only if the document has more than 3 pages
WatermarkService.create()
    .watermarkPDF(document)
        .withText("Text-based Watermark")
            .documentFilter(document -> document.getNumberOfPages() > 3)
    .apply()  
```

### Dependencies 
- **Apache PDFBox**: [Apache PDFBox](https://pdfbox.apache.org/) - A Java library for working with PDF documents.
- **JAI Image I/O**: [JAI Image I/O](https://github.com/jai-imageio/jai-imageio-core) - Image I/O library for Java, supporting various image formats.
- **commons-logging**: [Apache Commons Logging](https://commons.apache.org/proper/commons-logging/) - A simple logging facade for Java.