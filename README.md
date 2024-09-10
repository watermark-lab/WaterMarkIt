# WaterMarkIt

A lightweight Java library for adding unremovable watermarks to various file types, including PDFs and images. WaterMarkIt was developed to address the challenge of creating watermarks that cannot be easily removed, providing a robust solution for document and image protection.

## Features

- **Fluent API**: Provides a user-friendly way to configure and apply watermarks.
- **Unremovable Watermarks**: Designed to create watermarks that cannot be easily removed, ensuring the protection of your documents and images.
- **Support for Multiple File Types**: Includes support for PDF and image files.
- **Customizable Watermarks**: Add text, color, and trademarks with customizable DPI settings.
- **Asynchronous Operations**: Use an `Executor` for asynchronous watermarking.

## Getting Started

### Prerequisites

- Java 11 or higher
- Maven

### Installation

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>io.github.olegcheban</groupId>
    <artifactId>WaterMarkIt</artifactId>
    <version>1.0.0</version>
</dependency>
```
### Usage

Here’s a quick example of how to use the WaterMarkIt library:

```java
import io.github.olegcheban.WatermarkService;
import com.markit.services.impl.FileType;
import org.apache.pdfbox.pdmodel.PDDocument;
import java.io.IOException;
import java.util.concurrent.Executors;

public class WatermarkExample {
    public static void main(String[] args) throws IOException {
        WatermarkService watermarkService = 
                WatermarkService.create(
                    Executors.newFixedThreadPool(
                            Runtime.getRuntime().availableProcessors()
                    )
                );

        PDDocument document = new PDDocument();
        document.addPage(new PDPage());

        byte[] result = watermarkService
                .file(document)
                .fileType(FileType.PDF)
                .watermarkText("Sample Watermark")
                .color(new Color(255, 0, 0))                
                .trademark()
                .dpi(150f)
                .apply();

        document.close();
    }
}
```
### API Reference

- WatermarkService.create(): Creates a new instance of WatermarkService.
- file(byte[] sourceImageBytes): Sets the source file to be watermarked.
- file(PDDocument pdDocument): Sets the source PDF document to be watermarked.
- fileType(FileType fileType): Specifies the type of the file (e.g., PDF, image).
- watermarkText(String watermarkText): Sets the text for the watermark.
- dpi(float dpi): Specifies the resolution of the watermark.
- color(Color color): Sets the color of the watermark.
- trademark(): Adds a trademark symbol to the watermark.
- apply(): Applies the watermark and returns the watermarked file as a byte array.


### License
This project is licensed under the MIT License - see the LICENSE file for details.

## Additional Resources

### Documentation

- **Apache PDFBox**: [Apache PDFBox](https://pdfbox.apache.org/) - A Java library for working with PDF documents.
- **JAI Image I/O**: [JAI Image I/O](https://github.com/jai-imageio/jai-imageio-core) - Image I/O library for Java, supporting various image formats.

### Tools

- **Maven**: [Apache Maven](https://maven.apache.org/) - Build automation tool used for managing project dependencies.

### Other Libraries

- **commons-logging**: [Apache Commons Logging](https://commons.apache.org/proper/commons-logging/) - A simple logging facade for Java.

## TODO

- **Compression Feature**: Implement compression to reduce the file size of watermarked documents (PDF).
- **Overlay Watermarking Method**: Develop an overlay watermarking method for PDFs that is less resource-consuming and creates removable watermarks.
- **Logo-Based Watermark**: Create a feature to apply a watermark based on a logo file instead of just simple text.
- **Watermark Positions**: Add functionality for different watermark positions including:
    - **CENTER**: Place the watermark in the center of the document.
    - **CORNER**: Place the watermark in one of the corners.
    - **FILLED**: Watermark fills the entire area.