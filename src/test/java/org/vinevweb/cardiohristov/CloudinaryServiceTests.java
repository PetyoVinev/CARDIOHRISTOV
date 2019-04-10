package org.vinevweb.cardiohristov;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;
import org.vinevweb.cardiohristov.services.CloudinaryServiceImpl;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CloudinaryServiceTests {


    @InjectMocks
    private CloudinaryServiceImpl cloudinaryService;

    @Mock
    private  Cloudinary cloudinary;

    @Mock
    private Uploader uploader;


    @Test
    public void uploadImageReturnsUrlFromMultipartfile() throws IOException {
        Path path = Paths.get("path\\Desktop\\text.txt");
        String name = "text.text";
        String originalFileName = "text.txt";
        String contentType = "text/plain";
        byte[] content = null;
        try {
            content = Files.readAllBytes(path);
        } catch (final IOException e) {
        }
        MultipartFile mockMultipartFile = new MockMultipartFile(name,
                originalFileName, contentType, content);
        File fileToUpload = File.createTempFile("temp-file", mockMultipartFile.getOriginalFilename());
        mockMultipartFile.transferTo(fileToUpload);

        Map<String, String> map = new HashMap<>();
        map.put("url", "path");

        when(uploader.upload(Mockito.any(), Mockito.any())).thenReturn(map);
        when(this.cloudinary.uploader()).thenReturn(uploader);

        String result = this.cloudinaryService.uploadImage(mockMultipartFile);

        verify(cloudinary)
                .uploader();
        verify(uploader)
                .upload(any(), any());
        Assert.assertEquals("path",result);


    }

    @Test
    public void deleteImgReturnNull() {
         String result = this.cloudinaryService.deleteImage("image");
        Assert.assertNull(result);

    }

    @Test
    public void deleteImgReturnNullWithNull() {
        String result = this.cloudinaryService.deleteImage(null);
        Assert.assertNull(result);

    }

    @Test
    public void deleteImgReturnNullWithEmptyString() {
        String result = this.cloudinaryService.deleteImage(null);
        Assert.assertNull(result);

    }

}
