package com.russel;


import com.russel.file.service.FileStorageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = MinIOApplication.class)
public class MtTest {

    @Autowired
    private FileStorageService fileStorageService;

    @Test
    public void testUpdateImgFile() {
        try {
            FileInputStream fileInputStream = new FileInputStream("D:\\workplace\\java\\hot-off-press\\hot-off-press\\hot-off-press-test\\hot-off-press-test-minio\\src\\main\\resources\\2AA6777692AAA8E54A0F44452E78BFF4.jpg");
            String filePath = fileStorageService.uploadImgFile("", "2AA6777692AAA8E54A0F44452E78BFF4.jpg", fileInputStream);
            System.out.println(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}