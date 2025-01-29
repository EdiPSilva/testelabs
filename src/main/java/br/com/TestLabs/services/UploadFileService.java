package br.com.TestLabs.services;

import br.com.TestLabs.exceptions.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.Objects;

@Service
public class UploadFileService {
    private static final Logger log = LoggerFactory.getLogger(UploadFileService.class);

    private final String validExtension = "txt";

    public void uploadFile(final MultipartFile multipartFile) {
        if (Objects.isNull(multipartFile)) {
            throw new CustomException("Invalid file", HttpStatus.BAD_REQUEST);
        }
        validateExtension(multipartFile.getName());

    }

    private void validateExtension(final String fileName) {
        assert fileName != null;
        final String extension = fileName.substring(fileName.indexOf(".") + 1);
        if (!extension.contains(validExtension)) {
            throw new CustomException("Invalid extension file", HttpStatus.BAD_REQUEST);
        }
    }
}
