[![Build](https://github.com/OlegCheban/WaterMarkIt/actions/workflows/mvn.yml/badge.svg)](https://github.com/OlegCheban/WaterMarkIt/actions/workflows/mvn.yml)
[![Ask DeepWiki](https://deepwiki.com/badge.svg)](https://deepwiki.com/OlegCheban/WaterMarkIt)
[![javadoc](https://img.shields.io/badge/javadoc-1.3.3-brightgreen.svg)](https://javadoc.io/doc/io.github.watermark-lab/WaterMarkIt/latest/index.html)
![GitHub code size in bytes](https://img.shields.io/github/languages/code-size/OlegCheban/WaterMarkIt)
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

- **Trademarks**: A capability to add the trademark symbol ® to text-based watermarks.

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
    <version>1.3.3</version>
</dependency>
```

**For Gradle**, add the following to your `build.gradle`:
```kotlin
implementation 'io.github.watermark-lab:WaterMarkIt:1.3.3'
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

While WaterMarkIt is primarily a Java library targeting Java 11 for maximum compatibility, we selectively use Kotlin in specific areas to enhance code quality and developer experience:

### Use Cases

- **Test Code**: Kotlin's concise syntax and powerful testing features make our test suite more readable and maintainable
- **Data Classes**: Java 11 lacks records (introduced in Java 14+), so we use Kotlin's data classes for immutable value objects
- **Exception Classes**: Custom exceptions benefit from Kotlin's concise class declarations

### For Contributors

When contributing to WaterMarkIt:
- **Use Java** for all public APIs and core library functionality
- **Use Kotlin** for test classes, internal data structures, and utility classes where it provides clear benefits

This approach gives us the best of both worlds: rock-solid Java compatibility with modern development conveniences where they matter most.

## Dependencies 
- **Apache PDFBox**: [Apache PDFBox](https://pdfbox.apache.org/) - A Java library for working with PDF documents.
- **JAI Image I/O**: [JAI Image I/O](https://github.com/jai-imageio/jai-imageio-core) - Image I/O library for Java, supporting various image formats.
- **commons-logging**: [Apache Commons Logging](https://commons.apache.org/proper/commons-logging/) - A simple logging facade for Java.

## Contributing

We welcome contributions from the community! If you'd like to contribute to WaterMarkIt, please read our [Contributing Guide](CONTRIBUTING.md) for details on how to get started, our coding standards, and the pull request process.

Your contributions help make WaterMarkIt better for everyone!
