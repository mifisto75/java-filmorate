package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Slf4j
@Component
@RequiredArgsConstructor
public class RecommendationService {
    private final JdbcTemplate jdbcTemplate;
    private final FilmStorage filmStorage;

    public List<Film> getRecommendedFilms(Integer id) {

        List<Like> allLikes = jdbcTemplate.query("SELECT * FROM film_like_list", this::rsToLike);
        // получаем список id всех пользователей и список id фильмов, который они пролайкали.
        Map<Integer, List<Integer>> usersAndLikedFilmsIds = makeMapOfUsersAndLikes(allLikes);
        // получаем список id фильмов, которые пролайкал наш пользователь.
        List<Integer> userLikedFilmsIds = getLikesForUser(id);
        // получаем список рекомендаций для пользователя
        List<Integer> listOfRecommendedFilmsIds = makeRecommendationsList(usersAndLikedFilmsIds, userLikedFilmsIds);

        if (listOfRecommendedFilmsIds.size() < 1) {
            return Collections.emptyList();
        }
        // получаем список рекомендованных фильмов
        List<Film> recommendedFilms = new ArrayList<>();
        for (int i : listOfRecommendedFilmsIds) {
            recommendedFilms.add(filmStorage.getFilmId(i));
        }
        return recommendedFilms;
    }

    private List<Integer> makeRecommendationsList(
            Map<Integer, List<Integer>> usersAndLikedFilmsIds,
            List<Integer> userLikedFilmsIds) {
        // создаем и заполняем список id пользователей и количества совпадений по лайкам с нашим пользователем.
        Map<Integer, Long> idsAndMatchesCount = new HashMap<>();

        for (int id : usersAndLikedFilmsIds.keySet()) {
            List<Integer> user = userLikedFilmsIds;
            user.retainAll(usersAndLikedFilmsIds.get(id));
            long count = user.size();
            if (user.isEmpty()) {
                continue;
            }
            idsAndMatchesCount.put(id, count);
        }

        if (idsAndMatchesCount.isEmpty()) {
            return Collections.emptyList();
        }
        // Найти 10 пользователей с максимальным количеством пересечения по лайкам.
        Map<Integer, Long> topTenMatchesUsersIds = idsAndMatchesCount.entrySet().stream()
                .sorted(Comparator.comparingLong(e -> -e.getValue())).limit(10)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> {
                            throw new AssertionError();
                        },
                        LinkedHashMap::new
                ));

        // Определить id фильмов, которые наш пользователь не лайкал.
        List<Integer> recommendedFilmIds = new ArrayList<>();
        for (int id : topTenMatchesUsersIds.keySet()) {
            List<Integer> currentUserLikes = getLikesForUser(id);
            List<Integer> notLikedFilmIds = setUniqueFilmIds(currentUserLikes, userLikedFilmsIds);
            for (int i : notLikedFilmIds) {
                if (!recommendedFilmIds.contains(i)) {
                    recommendedFilmIds.add(i);
                }
            }
        }
        return recommendedFilmIds;
    }

    public List<Integer> setUniqueFilmIds(List<Integer> one, List<Integer> two) {
        List<Integer> notLikedFilmIds = new ArrayList<>();
        for (int i : one) {
            if (!two.contains(i)) {
                notLikedFilmIds.add(i);
            }
        }
        return notLikedFilmIds;
    }

    public List<Integer> getLikesForUser(int userId) {
        return jdbcTemplate.queryForList(format(
                "SELECT film_id FROM film_like_list WHERE user_id=%d", userId), Integer.class);
    }

    private Map<Integer, List<Integer>> makeMapOfUsersAndLikes(List<Like> likesList) {
        Map<Integer, List<Integer>> usersAndTheirLikes = new HashMap<>();

        for (Like like : likesList) {
            int userId = like.getUserId();
            int filmId = like.getFilmId();
            if (!usersAndTheirLikes.containsKey(userId)) {
                usersAndTheirLikes.put(userId, new ArrayList<>());
                List<Integer> list = usersAndTheirLikes.get(userId);
                list.add(filmId);
                usersAndTheirLikes.put(userId, list);
            } else {
                List<Integer> list = usersAndTheirLikes.get(userId);
                list.add(filmId);
                usersAndTheirLikes.put(userId, list);
            }
        }
        return usersAndTheirLikes;
    }

    private Like rsToLike(ResultSet rs, int rowNum) throws SQLException {
        int filmId = rs.getInt("film_id");
        int userId = rs.getInt("user_id");
        return Like.builder()
                .filmId(filmId)
                .userId(userId)
                .build();
    }
}
