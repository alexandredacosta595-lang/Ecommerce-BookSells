package com.mulemba.booksells.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class FileUploadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testUploadValidPngFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", 
                "capa.png", 
                MediaType.IMAGE_PNG_VALUE, 
                "imagem fake".getBytes()
        );

        mockMvc.perform(multipart("/api/upload").file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.url").exists());
    }

    @Test
    public void testUploadValidJpgFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", 
                "capa.jpg", 
                MediaType.IMAGE_JPEG_VALUE, 
                "imagem fake".getBytes()
        );

        mockMvc.perform(multipart("/api/upload").file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.url").exists());
    }

    @Test
    public void testUploadInvalidExtension() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", 
                "script.sh", 
                MediaType.TEXT_PLAIN_VALUE, 
                "echo hacker".getBytes()
        );

        mockMvc.perform(multipart("/api/upload").file(file))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Extensão não permitida. Apenas PNG, JPG, PDF e EPUB são suportados."));
    }

    @Test
    public void testUploadEmptyFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", 
                "vazio.png", 
                MediaType.IMAGE_PNG_VALUE, 
                new byte[0]
        );

        mockMvc.perform(multipart("/api/upload").file(file))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Ficheiro vazio"));
    }
}
