package com.example.demounittes;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.CoreMatchers.is;

import com.example.demounittes.controller.SinhVienController;
import com.example.demounittes.model.SinhVien;
import com.example.demounittes.service.SinhVienService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author thangdt
 */

@RunWith(MockitoJUnitRunner.class)
class sinhVienControllerTest {


    @Mock
    private SinhVienService sinhVienService;

    @InjectMocks
    private SinhVienController sinhVienController;

    private AutoCloseable autoCloseable;

    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer();

    String email = "leVanA@gmail.com";
    SinhVien sinhVien = new SinhVien("A1", "Le Van A", email);

    @Autowired
    private MockMvc mvc;


    @BeforeEach
    public void setup() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        this.mvc = MockMvcBuilders.standaloneSetup(sinhVienController).build();
    }

    @Test
    void givenStudents_whenGetStudents_thenReturnJsonArray() throws Exception {
        List<SinhVien> listSinhVien = new ArrayList<>(Arrays.asList(sinhVien));
        Mockito.when(sinhVienService.getList()).thenReturn(listSinhVien);

        mvc.perform(get("/list-sinh-vien")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(sinhVien.getName())));
    }

    @Test
    void whenPostStudents_thenCreateStudent() throws Exception {
        SinhVien sinhVien1 = SinhVien.builder()
                .id("A2")
                .name("Le van b")
                .email("b@gmail.com")
                .build();

        Mockito.when(sinhVienService.addNew(sinhVien1)).thenReturn(sinhVien1);

        String content = objectWriter.writeValueAsString(sinhVien1);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.post("/add")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);

        mvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("Le van b")));
    }


    @Test
    void detailSinhVien() throws Exception {
        Mockito.when(sinhVienService.findById(sinhVien.getId())).thenReturn(sinhVien);

        mvc.perform(get("/A1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("Le Van A")));

    }

    @Test
    void updateSinhVien() throws Exception {
        SinhVien updateSinhVien = SinhVien.builder()
                .id("A1")
                .name("Le van b")
                .email("b@gmail.com")
                .build();
        SinhVien editSinhVien = new SinhVien("A1", "Le Van B", email);
        Mockito.when(sinhVienService.update(editSinhVien.getId(), editSinhVien)).thenReturn(updateSinhVien);
        String content = objectWriter.writeValueAsString(updateSinhVien);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.put("/" + sinhVien.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);
        mvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isOk());

    }

}