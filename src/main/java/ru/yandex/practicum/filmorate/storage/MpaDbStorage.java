package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.PostNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Repository
@Slf4j
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Mpa getMpaById(int mpaId) {
        String sql = "SELECT * FROM mpa WHERE MPA_ID = ? ;";
        List<Mpa> ratingList = jdbcTemplate.query(sql,
                (rs, rowNum) -> new Mpa(rs.getInt("MPA_ID"), rs.getString("RATING_NAME"))
                , mpaId);
        if (ratingList.size() > 0) {
            log.info("Запрос на выдачу Мпа рейтинга с id: {}", mpaId);
            return ratingList.get(0);
        }
        log.info(" Мпа рейтинг с id: {} не найден", mpaId);
        throw new PostNotFoundException("Рейтинг мпа не найден");
    }

    @Override
    public List<Mpa> findAllMpa() {
        String sql = "SELECT * FROM mpa ORDER BY MPA_ID;";
        log.info("Запрос на выдачу всех Мпа рейтингов");
        return jdbcTemplate.query(sql,
                (rs, rowNum) -> new Mpa(rs.getInt("MPA_ID"), rs.getString("RATING_NAME")));
    }
}
