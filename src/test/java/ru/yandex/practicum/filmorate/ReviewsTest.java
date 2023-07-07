package ru.yandex.practicum.filmorate;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
public class ReviewsTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private void baza()throws Exception{
        mockMvc.perform(
                post("/users")
                        .content("{\n" +
                                "  \"login\": \"dolore\",\n" +
                                "  \"name\": \"Nick Name\",\n" +
                                "  \"email\": \"mail@mail.ru\",\n" +
                                "  \"birthday\": \"1946-08-20\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()); // создение юзера

        mockMvc.perform(
                post("/films")
                        .content("{\n" +
                                "  \"name\": \"nisi eiusmod\",\n" +
                                "  \"description\": \"adipisicing\",\n" +
                                "  \"releaseDate\": \"1967-03-25\",\n" +
                                "  \"duration\": 100,\n" +
                                "  \"mpa\": { \"id\": 1}\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()); // создание фильма

        mockMvc.perform(
                post("/reviews")
                        .content("{\n" +
                                "  \"content\": \"This film is soo bad.\",\n" +
                                "  \"isPositive\": false,\n" +
                                "  \"userId\": 1,\n" +
                                "  \"filmId\": 1\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is2xxSuccessful()); // создание отзыва

    }





    @Test
    @Sql(scripts = {"/schema.sql", "/data.sql"})
    void addReviewTest( )throws Exception { //Добавление нового отзыва.
        mockMvc.perform(
                post("/reviews")
                        .content("{\n" +
                                "  \"content\": \"This film is soo bad.\",\n" +
                                "  \"isPositive\": false,\n" +
                                "  \"userId\": 1,\n" +
                                "  \"filmId\": 1\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is4xxClientError()); //код 400 нет юзера и фильма

        mockMvc.perform(
                post("/users")
                        .content("{\n" +
                                "  \"login\": \"dolore\",\n" +
                                "  \"name\": \"Nick Name\",\n" +
                                "  \"email\": \"mail@mail.ru\",\n" +
                                "  \"birthday\": \"1946-08-20\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()); // создение юзера

        mockMvc.perform(
                post("/reviews")
                        .content("{\n" +
                                "  \"content\": \"This film is soo bad.\",\n" +
                                "  \"isPositive\": false,\n" +
                                "  \"userId\": 1,\n" +
                                "  \"filmId\": 1\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is4xxClientError()); // код 400 нет фильма


        mockMvc.perform(
                post("/films")
                        .content("{\n" +
                                "  \"name\": \"nisi eiusmod\",\n" +
                                "  \"description\": \"adipisicing\",\n" +
                                "  \"releaseDate\": \"1967-03-25\",\n" +
                                "  \"duration\": 100,\n" +
                                "  \"mpa\": { \"id\": 1}\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()); // создание фильма

        mockMvc.perform(
                post("/reviews")
                        .content("{\n" +
                                "  \"content\": \"This film is soo bad.\",\n" +
                                "  \"isPositive\": false,\n" +
                                "  \"userId\": 1,\n" +
                                "  \"filmId\": 1\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is2xxSuccessful()); // од 200 ОК

    }
    @Test
    @Sql(scripts = {"/schema.sql", "/data.sql"})
    void changeReviewTest()throws Exception { //Редактирование уже имеющегося отзыва.
      baza();
        mockMvc.perform(
                put("/reviews")
                        .content("{\n" +
                                "  \"reviewId\": 3,\n" +
                                "  \"content\": \"This film is not too bad.\",\n" +
                                "  \"isPositive\": true,\n" +
                                "  \"userId\": 2,\n" +
                                "  \"filmId\": 2,\n" +
                                "  \"useful\": 10\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is4xxClientError()); // код 400 редактирование не существующего отзыва


        mockMvc.perform(
                put("/reviews")
                        .content("{\n" +
                                "  \"reviewId\": 1,\n" +
                                "  \"content\": \"is not too bad.\",\n" +
                                "  \"isPositive\": true,\n" +
                                "  \"userId\": 2,\n" +
                                "  \"filmId\": 2,\n" +
                                "  \"useful\": 10\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is2xxSuccessful()); // од 200 ОК


    }
    @Test
    @Sql(scripts = {"/schema.sql", "/data.sql"})
    void deleteReviewTest()throws Exception { //Удаление уже имеющегося отзыва.
        mockMvc.perform(
                delete("/reviews/1")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is4xxClientError()); // код 400 нечего удалять

        baza();

        mockMvc.perform(
                delete("/reviews/1")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is2xxSuccessful()); // код 200 ОК

    }
    @Test
    @Sql(scripts = {"/schema.sql", "/data.sql"})
    void getReviewTest()throws Exception { //Получение отзыва по идентификатору.
        mockMvc.perform(
                get("/reviews/1")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is4xxClientError()); // код 400

        baza();

        mockMvc.perform(
                get("/reviews/1")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is2xxSuccessful()); // код 200 ОК
    }

    @Test
    @Sql(scripts = {"/schema.sql", "/data.sql"})
    void getTopReviewsFilmTest()throws Exception { //Получение всех отзывов по идентификатору фильма,
        mockMvc.perform(
                get("/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());// выдаёт  список дефолт 10

        mockMvc.perform(
                get("/reviews?filmId=1")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());// выдаёт  список по id фильма

        mockMvc.perform(
                get("/reviews?count=3")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());// выдаёт  список с лимитом 3
    }

    @Test
    @Sql(scripts = {"/schema.sql", "/data.sql"})
    void addAndDeleteReviewLikeTest()throws Exception { //пользователь ставит лайк отзыву./удаляет
        mockMvc.perform(
                put("/reviews/1/like/1")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is4xxClientError());// код 400 не кому ставить лайк

        mockMvc.perform(
                delete("/reviews/1/like/1")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is4xxClientError());// код 400 не у кого удалить лайк

        baza();

        mockMvc.perform(
                put("/reviews/1/like/1")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());// код 200

        mockMvc.perform(
                delete("/reviews/1/like/1    ")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());// код 200
    }
    @Test
    @Sql(scripts = {"/schema.sql", "/data.sql"})
    void addAndDeleteReviewDislikeTest()throws Exception { //пользователь ставит дизлайк отзыву./удаляет
        mockMvc.perform(
                put("/reviews/1/dislike/1")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is4xxClientError());// код 400 не кому ставить дизлай

        mockMvc.perform(
                delete("/reviews/1/dislike/1")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is4xxClientError());// код 400 не у кого удалить дизлайк

        baza();

        mockMvc.perform(
                put("/reviews/1/dislike/1")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());// код 200

        mockMvc.perform(
                delete("/reviews/1/dislike/1    ")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());// код 200
    }




















    @Test
    @Sql(scripts = {"/schema.sql", "/data.sql"})
    void CreateBadReviewToFilmId1COD400NotFilm() throws Exception {
        mockMvc.perform(
                post("/reviews")
                        .content("{\n" +
                                "  \"content\": \"This film is soo bad.\",\n" +
                                "  \"isPositive\": false,\n" +
                                "  \"userId\": 1,\n" +
                                "  \"filmId\": 1\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is4xxClientError());
    }

    @Test
    @Sql(scripts = {"/schema.sql", "/data.sql"})
    void CreateBadReviewToFilmId1COD200ok() throws Exception {
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

        mockMvc.perform(
                post("/films")
                        .content("{\n" +
                                "  \"name\": \"nisi eiusmod\",\n" +
                                "  \"description\": \"adipisicing\",\n" +
                                "  \"releaseDate\": \"1967-03-25\",\n" +
                                "  \"duration\": 100,\n" +
                                "  \"mpa\": { \"id\": 1}\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        mockMvc.perform(
                post("/reviews")
                        .content("{\n" +
                                "  \"content\": \"This film is soo bad.\",\n" +
                                "  \"isPositive\": false,\n" +
                                "  \"userId\": 1,\n" +
                                "  \"filmId\": 1\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is2xxSuccessful());
    }


//    @Test
//    @Sql(scripts = {"/schema.sql", "/data.sql"})
//    void addUserOkTest() throws Exception {
//        mockMvc.perform(
//                post("/users")
//                        .content("{\n" +
//                                "  \"login\": \"dolore\",\n" +
//                                "  \"name\": \"Nick Name\",\n" +
//                                "  \"email\": \"mail@mail.ru\",\n" +
//                                "  \"birthday\": \"1946-08-20\"\n" +
//                                "}")
//                        .contentType(MediaType.APPLICATION_JSON)
//        ).andExpect(status().isOk());
//    }
//
//
//    @Test
//    @Sql(scripts = {"/schema.sql", "/data.sql"})
//    void addUserOkTest() throws Exception {
//        mockMvc.perform(
//                post("/users")
//                        .content("{\n" +
//                                "  \"login\": \"dolore\",\n" +
//                                "  \"name\": \"Nick Name\",\n" +
//                                "  \"email\": \"mail@mail.ru\",\n" +
//                                "  \"birthday\": \"1946-08-20\"\n" +
//                                "}")
//                        .contentType(MediaType.APPLICATION_JSON)
//        ).andExpect(status().isOk());
//    }
//
//
//    @Test
//    @Sql(scripts = {"/schema.sql", "/data.sql"})
//    void addUserOkTest() throws Exception {
//        mockMvc.perform(
//                post("/users")
//                        .content("{\n" +
//                                "  \"login\": \"dolore\",\n" +
//                                "  \"name\": \"Nick Name\",\n" +
//                                "  \"email\": \"mail@mail.ru\",\n" +
//                                "  \"birthday\": \"1946-08-20\"\n" +
//                                "}")
//                        .contentType(MediaType.APPLICATION_JSON)
//        ).andExpect(status().isOk());
//    }
//
//
//    @Test
//    @Sql(scripts = {"/schema.sql", "/data.sql"})
//    void addUserOkTest() throws Exception {
//        mockMvc.perform(
//                post("/users")
//                        .content("{\n" +
//                                "  \"login\": \"dolore\",\n" +
//                                "  \"name\": \"Nick Name\",\n" +
//                                "  \"email\": \"mail@mail.ru\",\n" +
//                                "  \"birthday\": \"1946-08-20\"\n" +
//                                "}")
//                        .contentType(MediaType.APPLICATION_JSON)
//        ).andExpect(status().isOk());
//    }
//
//
//    @Test
//    @Sql(scripts = {"/schema.sql", "/data.sql"})
//    void addUserOkTest() throws Exception {
//        mockMvc.perform(
//                post("/users")
//                        .content("{\n" +
//                                "  \"login\": \"dolore\",\n" +
//                                "  \"name\": \"Nick Name\",\n" +
//                                "  \"email\": \"mail@mail.ru\",\n" +
//                                "  \"birthday\": \"1946-08-20\"\n" +
//                                "}")
//                        .contentType(MediaType.APPLICATION_JSON)
//        ).andExpect(status().isOk());
//    }
//
//
//    @Test
//    @Sql(scripts = {"/schema.sql", "/data.sql"})
//    void addUserOkTest() throws Exception {
//        mockMvc.perform(
//                post("/users")
//                        .content("{\n" +
//                                "  \"login\": \"dolore\",\n" +
//                                "  \"name\": \"Nick Name\",\n" +
//                                "  \"email\": \"mail@mail.ru\",\n" +
//                                "  \"birthday\": \"1946-08-20\"\n" +
//                                "}")
//                        .contentType(MediaType.APPLICATION_JSON)
//        ).andExpect(status().isOk());
//    }
//
//
//    @Test
//    @Sql(scripts = {"/schema.sql", "/data.sql"})
//    void addUserOkTest() throws Exception {
//        mockMvc.perform(
//                post("/users")
//                        .content("{\n" +
//                                "  \"login\": \"dolore\",\n" +
//                                "  \"name\": \"Nick Name\",\n" +
//                                "  \"email\": \"mail@mail.ru\",\n" +
//                                "  \"birthday\": \"1946-08-20\"\n" +
//                                "}")
//                        .contentType(MediaType.APPLICATION_JSON)
//        ).andExpect(status().isOk());
//    }
//
//
//    @Test
//    @Sql(scripts = {"/schema.sql", "/data.sql"})
//    void addUserOkTest() throws Exception {
//        mockMvc.perform(
//                post("/users")
//                        .content("{\n" +
//                                "  \"login\": \"dolore\",\n" +
//                                "  \"name\": \"Nick Name\",\n" +
//                                "  \"email\": \"mail@mail.ru\",\n" +
//                                "  \"birthday\": \"1946-08-20\"\n" +
//                                "}")
//                        .contentType(MediaType.APPLICATION_JSON)
//        ).andExpect(status().isOk());
//    }
//
//
//    @Test
//    @Sql(scripts = {"/schema.sql", "/data.sql"})
//    void addUserOkTest() throws Exception {
//        mockMvc.perform(
//                post("/users")
//                        .content("{\n" +
//                                "  \"login\": \"dolore\",\n" +
//                                "  \"name\": \"Nick Name\",\n" +
//                                "  \"email\": \"mail@mail.ru\",\n" +
//                                "  \"birthday\": \"1946-08-20\"\n" +
//                                "}")
//                        .contentType(MediaType.APPLICATION_JSON)
//        ).andExpect(status().isOk());
//    }
//
//
//    @Test
//    @Sql(scripts = {"/schema.sql", "/data.sql"})
//    void addUserOkTest() throws Exception {
//        mockMvc.perform(
//                post("/users")
//                        .content("{\n" +
//                                "  \"login\": \"dolore\",\n" +
//                                "  \"name\": \"Nick Name\",\n" +
//                                "  \"email\": \"mail@mail.ru\",\n" +
//                                "  \"birthday\": \"1946-08-20\"\n" +
//                                "}")
//                        .contentType(MediaType.APPLICATION_JSON)
//        ).andExpect(status().isOk());
//    }
//
//
//    @Test
//    @Sql(scripts = {"/schema.sql", "/data.sql"})
//    void addUserOkTest() throws Exception {
//        mockMvc.perform(
//                post("/users")
//                        .content("{\n" +
//                                "  \"login\": \"dolore\",\n" +
//                                "  \"name\": \"Nick Name\",\n" +
//                                "  \"email\": \"mail@mail.ru\",\n" +
//                                "  \"birthday\": \"1946-08-20\"\n" +
//                                "}")
//                        .contentType(MediaType.APPLICATION_JSON)
//        ).andExpect(status().isOk());
//    }
//
//
//    @Test
//    @Sql(scripts = {"/schema.sql", "/data.sql"})
//    void addUserOkTest() throws Exception {
//        mockMvc.perform(
//                post("/users")
//                        .content("{\n" +
//                                "  \"login\": \"dolore\",\n" +
//                                "  \"name\": \"Nick Name\",\n" +
//                                "  \"email\": \"mail@mail.ru\",\n" +
//                                "  \"birthday\": \"1946-08-20\"\n" +
//                                "}")
//                        .contentType(MediaType.APPLICATION_JSON)
//        ).andExpect(status().isOk());
//    }
//
//
//    @Test
//    @Sql(scripts = {"/schema.sql", "/data.sql"})
//    void addUserOkTest() throws Exception {
//        mockMvc.perform(
//                post("/users")
//                        .content("{\n" +
//                                "  \"login\": \"dolore\",\n" +
//                                "  \"name\": \"Nick Name\",\n" +
//                                "  \"email\": \"mail@mail.ru\",\n" +
//                                "  \"birthday\": \"1946-08-20\"\n" +
//                                "}")
//                        .contentType(MediaType.APPLICATION_JSON)
//        ).andExpect(status().isOk());
//    }
//
//
//    @Test
//    @Sql(scripts = {"/schema.sql", "/data.sql"})
//    void addUserOkTest() throws Exception {
//        mockMvc.perform(
//                post("/users")
//                        .content("{\n" +
//                                "  \"login\": \"dolore\",\n" +
//                                "  \"name\": \"Nick Name\",\n" +
//                                "  \"email\": \"mail@mail.ru\",\n" +
//                                "  \"birthday\": \"1946-08-20\"\n" +
//                                "}")
//                        .contentType(MediaType.APPLICATION_JSON)
//        ).andExpect(status().isOk());
//    }
//
//
//    @Test
//    @Sql(scripts = {"/schema.sql", "/data.sql"})
//    void addUserOkTest() throws Exception {
//        mockMvc.perform(
//                post("/users")
//                        .content("{\n" +
//                                "  \"login\": \"dolore\",\n" +
//                                "  \"name\": \"Nick Name\",\n" +
//                                "  \"email\": \"mail@mail.ru\",\n" +
//                                "  \"birthday\": \"1946-08-20\"\n" +
//                                "}")
//                        .contentType(MediaType.APPLICATION_JSON)
//        ).andExpect(status().isOk());
//    }
//
//
//    @Test
//    @Sql(scripts = {"/schema.sql", "/data.sql"})
//    void addUserOkTest() throws Exception {
//        mockMvc.perform(
//                post("/users")
//                        .content("{\n" +
//                                "  \"login\": \"dolore\",\n" +
//                                "  \"name\": \"Nick Name\",\n" +
//                                "  \"email\": \"mail@mail.ru\",\n" +
//                                "  \"birthday\": \"1946-08-20\"\n" +
//                                "}")
//                        .contentType(MediaType.APPLICATION_JSON)
//        ).andExpect(status().isOk());
//    }
//
//
//    @Test
//    @Sql(scripts = {"/schema.sql", "/data.sql"})
//    void addUserOkTest() throws Exception {
//        mockMvc.perform(
//                post("/users")
//                        .content("{\n" +
//                                "  \"login\": \"dolore\",\n" +
//                                "  \"name\": \"Nick Name\",\n" +
//                                "  \"email\": \"mail@mail.ru\",\n" +
//                                "  \"birthday\": \"1946-08-20\"\n" +
//                                "}")
//                        .contentType(MediaType.APPLICATION_JSON)
//        ).andExpect(status().isOk());
//    }
//
//
//    @Test
//    @Sql(scripts = {"/schema.sql", "/data.sql"})
//    void addUserOkTest() throws Exception {
//        mockMvc.perform(
//                post("/users")
//                        .content("{\n" +
//                                "  \"login\": \"dolore\",\n" +
//                                "  \"name\": \"Nick Name\",\n" +
//                                "  \"email\": \"mail@mail.ru\",\n" +
//                                "  \"birthday\": \"1946-08-20\"\n" +
//                                "}")
//                        .contentType(MediaType.APPLICATION_JSON)
//        ).andExpect(status().isOk());
//    }
//
//
//    @Test
//    @Sql(scripts = {"/schema.sql", "/data.sql"})
//    void addUserOkTest() throws Exception {
//        mockMvc.perform(
//                post("/users")
//                        .content("{\n" +
//                                "  \"login\": \"dolore\",\n" +
//                                "  \"name\": \"Nick Name\",\n" +
//                                "  \"email\": \"mail@mail.ru\",\n" +
//                                "  \"birthday\": \"1946-08-20\"\n" +
//                                "}")
//                        .contentType(MediaType.APPLICATION_JSON)
//        ).andExpect(status().isOk());
//    }
//
//
//    @Test
//    @Sql(scripts = {"/schema.sql", "/data.sql"})
//    void addUserOkTest() throws Exception {
//        mockMvc.perform(
//                post("/users")
//                        .content("{\n" +
//                                "  \"login\": \"dolore\",\n" +
//                                "  \"name\": \"Nick Name\",\n" +
//                                "  \"email\": \"mail@mail.ru\",\n" +
//                                "  \"birthday\": \"1946-08-20\"\n" +
//                                "}")
//                        .contentType(MediaType.APPLICATION_JSON)
//        ).andExpect(status().isOk());
//    }
//
//
//    @Test
//    @Sql(scripts = {"/schema.sql", "/data.sql"})
//    void addUserOkTest() throws Exception {
//        mockMvc.perform(
//                post("/users")
//                        .content("{\n" +
//                                "  \"login\": \"dolore\",\n" +
//                                "  \"name\": \"Nick Name\",\n" +
//                                "  \"email\": \"mail@mail.ru\",\n" +
//                                "  \"birthday\": \"1946-08-20\"\n" +
//                                "}")
//                        .contentType(MediaType.APPLICATION_JSON)
//        ).andExpect(status().isOk());
//    }
//
//
//    @Test
//    @Sql(scripts = {"/schema.sql", "/data.sql"})
//    void addUserOkTest() throws Exception {
//        mockMvc.perform(
//                post("/users")
//                        .content("{\n" +
//                                "  \"login\": \"dolore\",\n" +
//                                "  \"name\": \"Nick Name\",\n" +
//                                "  \"email\": \"mail@mail.ru\",\n" +
//                                "  \"birthday\": \"1946-08-20\"\n" +
//                                "}")
//                        .contentType(MediaType.APPLICATION_JSON)
//        ).andExpect(status().isOk());
//    }


}
