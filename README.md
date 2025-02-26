[![Build](https://github.com/OlegCheban/WaterMarkIt/actions/workflows/mvn.yml/badge.svg)](https://github.com/OlegCheban/WaterMarkIt/actions/workflows/mvn.yml)
[![javadoc](https://img.shields.io/badge/javadoc-1.3.1-brightgreen.svg)](https://javadoc.io/doc/io.github.watermark-lab/WaterMarkIt/latest/index.html)
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
  - Color
  - Size
  - Position
  - Rotation
  - Opacity
  - DPI

- **Trademarks**: A capability to add the trademark symbol to text-based watermarks.

- **Page orientation support**: Full support for both portrait and landscape orientations.

- **Supported Formats**:
  - PDF
  - Various image formats (JPEG, PNG, etc.)
  
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
    <version>1.3.1</version>
</dependency>
```

**For Gradle**, add the following to your `build.gradle`:
```kotlin
implementation 'io.github.watermark-lab:WaterMarkIt:1.3.1'
```

### Usage

```java
WatermarkService.create(
                //using a thread pool here isn't mandatory; use it only when necessary
                Executors.newFixedThreadPool(
                        Runtime.getRuntime().availableProcessors()
                )
        )
        .watermarkPDF(new File("path/to/file.pdf"))
           .withImage(new File("path/to/watermark.png"))
           .position(WatermarkPosition.CENTER).end()
           .opacity(0.2f)
        .and()
           .withText("WaterMarkIt")
               .color(Color.BLUE)
               .addTrademark()
               .end()           
           .position(WatermarkPosition.TILED)
               .adjust(35, 0)
               .horizontalSpacing(10)
               .end()
           .opacity(0.1f)
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
            .when(!isOwner)
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

### Extensibility and Customization

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

### Dependencies 
- **Apache PDFBox**: [Apache PDFBox](https://pdfbox.apache.org/) - A Java library for working with PDF documents.
- **JAI Image I/O**: [JAI Image I/O](https://github.com/jai-imageio/jai-imageio-core) - Image I/O library for Java, supporting various image formats.
- **commons-logging**: [Apache Commons Logging](https://commons.apache.org/proper/commons-logging/) - A simple logging facade for Java.
