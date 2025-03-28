package ru.testPr.test.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import java.awt.image.BufferedImage;
import java.util.Hashtable;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;

public class QRCodeGenerator {

    public static byte[] generateQRCodeImage(String text) throws Exception {
        Hashtable<EncodeHintType, String> hints = new Hashtable<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        
        MultiFormatWriter barcodeWriter = new MultiFormatWriter();
        BitMatrix bitMatrix = barcodeWriter.encode(text, BarcodeFormat.QR_CODE, 300, 300, hints);
        
        BufferedImage image = new BufferedImage(300, 300, BufferedImage.TYPE_INT_RGB);
        image.createGraphics().fillRect(0, 0, 300, 300);
        
        for (int i = 0; i < 300; i++) {
            for (int j = 0; j < 300; j++) {
                image.setRGB(i, j, (bitMatrix.get(i, j) ? 0x000000 : 0xFFFFFF));
            }
        }
        
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "PNG", byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}

