package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FilmTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void addFilmOkTest() throws Exception {
        mockMvc.perform(
                post("/films")
                        .content("{\n" +
                                "  \"name\": \"nisi eiusmod\",\n" +
                                "  \"description\": \"adipisicing\",\n" +
                                "  \"releaseDate\": \"1967-03-25\",\n" +
                                "  \"duration\": 100\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }


    @Test
    void addFilmFailNameTest() throws Exception {
        mockMvc.perform(
                post("/films")
                        .content("{\n" +
                                "  \"name\": \"\",\n" +
                                "  \"description\": \"Description\",\n" +
                                "  \"releaseDate\": \"1900-03-25\",\n" +
                                "  \"duration\": 200\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is4xxClientError());
    }

    @Test
    void addFilmFailDescripsonTest() throws Exception {
        mockMvc.perform(
                post("/films")
                        .content("{\n" +
                                "  \"name\": \"Film name\",\n" +
                                "  \"description\": \"Пятеро друзей ( комик-группа «Шарло»), приезжают в город Бризуль. Здесь они хотят разыскать господина Огюста Куглова, который задолжал им деньги, а именно 20 миллионов. о Куглов, который за время «своего отсутствия», стал кандидатом Коломбани.\",\n" +
                                "    \"releaseDate\": \"1900-03-25\",\n" +
                                "  \"duration\": 200\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is4xxClientError());
    }


    @Test
    void addFilmFailDurationt() throws Exception {
        mockMvc.perform(
                post("/films")
                        .content("{\n" +
                                "  \"name\": \"Name\",\n" +
                                "  \"description\": \"Descrition\",\n" +
                                "  \"releaseDate\": \"1980-03-25\",\n" +
                                "  \"duration\": -200\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is4xxClientError());
    }

    @Test
    void updateFilmOkTest() throws Exception {
        mockMvc.perform(
                post("/films")
                        .content("{\n" +
                                "  \"name\": \"nisi eiusmod\",\n" +
                                "  \"description\": \"adipisicing\",\n" +
                                "  \"releaseDate\": \"1967-03-25\",\n" +
                                "  \"duration\": 100\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
        mockMvc.perform(
                put("/films")
                        .content("{\n" +
                                "  \"id\": 1,\n" +
                                "  \"name\": \"Film Updated\",\n" +
                                "  \"releaseDate\": \"1989-04-17\",\n" +
                                "  \"description\": \"New film update decription\",\n" +
                                "  \"duration\": 190,\n" +
                                "  \"rate\": 4\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    void updateFilmUnknownTest() throws Exception {
        mockMvc.perform(
                post("/films")
                        .content("{\n" +
                                "  \"name\": \"nisi eiusmod\",\n" +
                                "  \"description\": \"adipisicing\",\n" +
                                "  \"releaseDate\": \"1967-03-25\",\n" +
                                "  \"duration\": 100\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
        mockMvc.perform(
                put("/films")
                        .content("{\n" +
                                "  \"id\": 9999,\n" +
                                "  \"name\": \"Film Updated\",\n" +
                                "  \"releaseDate\": \"1989-04-17\",\n" +
                                "  \"description\": \"New film update decription\",\n" +
                                "  \"duration\": 190,\n" +
                                "  \"rate\": 4\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is4xxClientError());
    }
}
