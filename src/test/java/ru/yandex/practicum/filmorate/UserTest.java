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
public class UserTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void addUserOkTest() throws Exception {
        mockMvc.perform(
                post("/users")
                        .content("{\n" +
                                "  \"login\": \"dolore\",\n" +
                                "  \"name\": \"Nick Name\",\n" +
                                "  \"email\": \"mail@mail.ru\",\n" +
                                "  \"birthday\": \"1946-08-20\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    void addUserFailLoginTest() throws Exception {
        mockMvc.perform(
                post("/users")
                        .content("{\n" +
                                "  \"login\": \"dolore ullamco\",\n" +
                                "  \"email\": \"yandex@mail.ru\",\n" +
                                "  \"birthday\": \"2446-08-20\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is4xxClientError());
    }

    @Test
    void addUserFailEmailTest() throws Exception {
        mockMvc.perform(
                post("/users")
                        .content("{\n" +
                                "  \"login\": \"dolore ullamco\",\n" +
                                "  \"name\": \"\",\n" +
                                "  \"email\": \"mail.ru\",\n" +
                                "  \"birthday\": \"1980-08-20\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is4xxClientError());
    }

    @Test
    void addUserFailBirthdayTest() throws Exception {
        mockMvc.perform(
                post("/users")
                        .content("{\n" +
                                "  \"login\": \"dolore\",\n" +
                                "  \"name\": \"\",\n" +
                                "  \"email\": \"test@mail.ru\",\n" +
                                "  \"birthday\": \"2446-08-20\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is4xxClientError());
    }

    @Test
    void UpdateUseOkTest() throws Exception {
        mockMvc.perform(
                post("/users")
                        .content("{\n" +
                                "  \"login\": \"dolore\",\n" +
                                "  \"name\": \"Nick Name\",\n" +
                                "  \"email\": \"mail@mail.ru\",\n" +
                                "  \"birthday\": \"1946-08-20\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
        );
        mockMvc.perform(
                put("/users")
                        .content("{\n" +
                                "  \"login\": \"doloreUpdate\",\n" +
                                "  \"name\": \"est adipisicing\",\n" +
                                "  \"id\": 1,\n" +
                                "  \"email\": \"mail@yandex.ru\",\n" +
                                "  \"birthday\": \"1976-09-20\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    void UpdateUseUnknownTest() throws Exception {
        mockMvc.perform(
                post("/users")
                        .content("{\n" +
                                "  \"login\": \"dolore\",\n" +
                                "  \"name\": \"Nick Name\",\n" +
                                "  \"email\": \"mail@mail.ru\",\n" +
                                "  \"birthday\": \"1946-08-20\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
        );
        mockMvc.perform(
                put("/users")
                        .content("{\n" +
                                "  \"login\": \"doloreUpdate\",\n" +
                                "  \"name\": \"est adipisicing\",\n" +
                                "  \"id\": 9999,\n" +
                                "  \"email\": \"mail@yandex.ru\",\n" +
                                "  \"birthday\": \"1976-09-20\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is4xxClientError());
    }
}












