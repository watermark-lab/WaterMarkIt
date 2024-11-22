package com.markit;

import com.markit.api.WatermarkPosition;
import com.markit.api.WatermarkService;
import com.markit.api.WatermarkingMethod;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import java.awt.*;
import java.io.IOException;
import java.util.concurrent.Executors;

public class WatermarkApplication {
	public static void main(String[] args) throws IOException {
		try (var document = new PDDocument()) {
			document.addPage(new PDPage());
			document.addPage(new PDPage());
			document.addPage(new PDPage());

			WatermarkService.textBasedWatermarker(
							Executors.newFixedThreadPool(
									Runtime.getRuntime().availableProcessors()
							)
					)
					.watermark(document)
					.withText("CONFIDENTIAL").size(20)
					.method(WatermarkingMethod.OVERLAY)
					.position(WatermarkPosition.TOP_LEFT)
					.color(Color.RED)
					.and()
					.withText("Copyright © 2024").size(10)
					.method(WatermarkingMethod.OVERLAY)
					.position(WatermarkPosition.BOTTOM_LEFT)
					.color(Color.BLACK)
					.opacity(0.5f)
					.and()
					.withText("Your Company Name").size(200)
					.method(WatermarkingMethod.DRAW)
					.position(WatermarkPosition.CENTER)
					.dpi(300f)
					.rotation(25)
					.addTrademark()
					.color(Color.BLUE)
					.apply();
		}

	}
}
