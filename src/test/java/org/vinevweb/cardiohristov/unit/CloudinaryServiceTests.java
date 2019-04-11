package org.vinevweb.cardiohristov.unit;

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
import static org.vinevweb.cardiohristov.Constants.*;

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


    private static final String PATH_DESKTOP_TEXT_TXT = "path\\Desktop\\text.txt";
    private static final String TEXT_TEXT = "text.text";
    private static final String TEXT_TXT = "text.txt";
    private static final String TEXT_PLAIN = "text/plain";
    private static final String TEMP_FILE = "temp-file";
    private static final String PATH = "path";
    private static final String URL = "url";

    @InjectMocks
    private CloudinaryServiceImpl cloudinaryService;

    @Mock
    private  Cloudinary cloudinary;

    @Mock
    private Uploader uploader;


    @Test
    public void uploadImageReturnsUrlFromMultipartfile() throws IOException {
        Path path = Paths.get(PATH_DESKTOP_TEXT_TXT);
        String name = TEXT_TEXT;
        String originalFileName = TEXT_TXT;
        String contentType = TEXT_PLAIN;
        byte[] content = null;
        try {
            content = Files.readAllBytes(path);
        } catch (final IOException e) {
        }
        MultipartFile mockMultipartFile = new MockMultipartFile(name,
                originalFileName, contentType, content);
        File fileToUpload = File.createTempFile(TEMP_FILE, mockMultipartFile.getOriginalFilename());
        mockMultipartFile.transferTo(fileToUpload);

        Map<String, String> map = new HashMap<>();
        map.put(URL, PATH);

        when(uploader.upload(Mockito.any(), Mockito.any())).thenReturn(map);
        when(this.cloudinary.uploader()).thenReturn(uploader);

        String result = this.cloudinaryService.uploadImage(mockMultipartFile);

        verify(cloudinary)
                .uploader();
        verify(uploader)
                .upload(any(), any());
        Assert.assertEquals(PATH,result);


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
