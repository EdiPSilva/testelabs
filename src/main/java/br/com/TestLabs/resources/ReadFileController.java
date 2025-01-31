package br.com.TestLabs.resources;

import br.com.TestLabs.resources.response.ReadFileResponse;
import br.com.TestLabs.services.ReadFileService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(value = "read-file", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReadFileController {

    private final ReadFileService readFileService;

    @PostMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Leitura de arquivo .txt",
            description = "Utilize deste endpoint para submeter arquivos .txt posicional com os dados de usuario, pedido e produto. " +
                    "Com este recurso as respectivas informacoes ficaram disponiveis em formato json")
    public ResponseEntity<ReadFileResponse> readFile(@RequestParam("file") final MultipartFile file) throws IOException {
        log.info("m=readFile, file={}", Objects.isNull(file) ? "" : file.getOriginalFilename());
        return new ResponseEntity<>(readFileService.readFile(file), HttpStatus.OK);
    }

}
