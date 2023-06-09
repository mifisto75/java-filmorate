package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
public class UserTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void before() {
        jdbcTemplate.update("DROP TABLE IF EXISTS film_like_list CASCADE;\n" +
                "DROP TABLE IF EXISTS user_friend_list CASCADE;\n" +
                "DROP TABLE IF EXISTS users CASCADE;\n" +
                "DROP TABLE IF EXISTS film_genres CASCADE;\n" +
                "DROP TABLE IF EXISTS genres CASCADE;\n" +
                "DROP TABLE IF EXISTS films CASCADE;\n" +
                "DROP TABLE IF EXISTS mpa_ratings CASCADE;  " +
                "CREATE TABLE IF NOT EXISTS mpa_ratings\n" +
                "(\n" +
                "    rating_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,\n" +
                "    name          VARCHAR NOT NULL UNIQUE\n" +
                ");\n" +
                "\n" +
                "CREATE TABLE IF NOT EXISTS films\n" +
                "(\n" +
                "    film_id             INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,\n" +
                "    name                VARCHAR NOT NULL,\n" +
                "    description         VARCHAR(200),\n" +
                "    release_date        DATE,\n" +
                "    duration INTEGER CHECK (duration > 0),\n" +
                "    rating_id       INTEGER REFERENCES mpa_ratings (rating_id)\n" +
                ");\n" +
                "\n" +
                "CREATE TABLE IF NOT EXISTS genres\n" +
                "(\n" +
                "    genre_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,\n" +
                "    name     VARCHAR NOT NULL UNIQUE\n" +
                ");\n" +
                "\n" +
                "CREATE TABLE IF NOT EXISTS film_genres\n" +
                "(\n" +
                "    film_id  INTEGER  NOT NULL REFERENCES films (film_id),\n" +
                "    genre_id INTEGER NOT NULL REFERENCES genres (genre_id)\n" +
                ");\n" +
                "\n" +
                "CREATE TABLE IF NOT EXISTS users\n" +
                "(\n" +
                "    user_id  INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,\n" +
                "    email    VARCHAR NOT NULL UNIQUE,\n" +
                "    login    VARCHAR NOT NULL UNIQUE,\n" +
                "    name     VARCHAR NOT NULL,\n" +
                "    birthday DATE    NOT NULL\n" +
                ");\n" +
                "\n" +
                "CREATE TABLE IF NOT EXISTS user_friend_list\n" +
                "(\n" +
                "    from_user_id INTEGER  NOT NULL REFERENCES users (user_id),\n" +
                "    to_user_id   INTEGER  NOT NULL REFERENCES users (user_id),\n" +
                "    Boolean_status     BOOLEAN NOT NULL,\n" +
                "    PRIMARY KEY (from_user_id, to_user_id)\n" +
                ");\n" +
                "\n" +
                "CREATE TABLE IF NOT EXISTS film_like_list\n" +
                "(\n" +
                "    film_id INTEGER  NOT NULL REFERENCES films (film_id),\n" +
                "    user_id INTEGER  NOT NULL REFERENCES users (user_id)\n" +
                ");");
    }

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
    void updateUseOkTest() throws Exception {
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
    void updateUseUnknownTest() throws Exception {
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












