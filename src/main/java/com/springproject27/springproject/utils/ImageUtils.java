package com.springproject27.springproject.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageUtils {

    public static ByteArrayInputStream getByteArrayInputStreamOfImageConvertedTo(ImageFormat imageFormat, InputStream imageInputStream) throws IOException {
        BufferedImage image = ImageIO.read(imageInputStream);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(image, imageFormat.toString(), os);
        return new ByteArrayInputStream(os.toByteArray());
    }

    public enum ImageFormat{
        PNG("png")
        ;

        private final String format;

        ImageFormat(final String format) {
            this.format = format;
        }

        @Override
        public String toString() {
            return format;
        }

    }
}
