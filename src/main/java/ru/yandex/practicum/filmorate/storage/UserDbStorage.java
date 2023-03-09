package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.PostNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

@Slf4j
@Repository
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private User makeUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("USER_ID"));
        user.setEmail(rs.getString("EMAIL"));
        user.setLogin(rs.getString("LOGIN"));
        user.setName(rs.getString("NAME"));
        user.setBirthday(rs.getDate("BIRTHDAY").toLocalDate());
        return user;
    }

    public boolean isUserExist(long userId) {
        String sql = "SELECT COUNT(*) FROM USERS WHERE USER_ID = ? ;";
        int countUser = jdbcTemplate.queryForObject(sql, Integer.class, userId);

        return countUser > 0;
    }

    @Override
    public Collection<User> findAllUsers() {
        String sql = "SELECT * FROM USERS;";

        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
    }

    @Override
    public User getUserById(Long userId) {
        if (isUserExist(userId)) {
            String sql = "SELECT * FROM USERS WHERE USER_ID = ? ;";
            List<User> users = jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), userId);
            return users.get(0);
        } else {
            throw new PostNotFoundException("Пользователь с таким id не найден");
        }
    }

    @Override
    public User addUser(User user) {
        String sqlCreateUser = "INSERT INTO USERS(" +
                "EMAIL, " +
                "LOGIN, " +
                "NAME, " +
                "BIRTHDAY) " +
                "VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlCreateUser, new String[]{"USER_ID"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        user.setId(keyHolder.getKey().longValue());
        return getUserById(user.getId());
    }

    @Override
    public User updateUser(User user) {
        if (isUserExist(user.getId())) {
            String sqlUpdateUser = "UPDATE USERS SET " +
                    "EMAIL = ?, " +
                    "LOGIN = ?, " +
                    "NAME = ?, " +
                    "BIRTHDAY = ? " +
                    "WHERE USER_ID = ?";

            jdbcTemplate.update(sqlUpdateUser
                    , user.getEmail()
                    , user.getLogin()
                    , user.getName()
                    , Date.valueOf(user.getBirthday())
                    , user.getId());
            return getUserById(user.getId());
        } else {
            throw new PostNotFoundException("Пользователь с таким id не найден");
        }
    }

    @Override
    public void deleteUser(Long userId) {
        if (isUserExist(userId)) {
            String sql = "DELETE FROM users WHERE user_id = ?";
            jdbcTemplate.update(sql, userId);
        } else {
            throw new PostNotFoundException("Пользователь с таким id не найден");
        }
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        if (isUserExist(userId) && isUserExist(friendId)) {
            String sql = "INSERT INTO FRIENDSHIP(USER_ID, FRIEND_ID, STATUS) VALUES (?, ?, ?)";
            jdbcTemplate.update(sql, userId, friendId, "CONFIRMED");
        } else {
            throw new PostNotFoundException("Один из пользователей с таким id не найден");
        }
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        if (isUserExist(userId) && isUserExist(friendId)) {
            String sql = "DELETE FROM FRIENDSHIP WHERE USER_ID = ? AND FRIEND_ID = ?;";
            jdbcTemplate.update(sql, userId, friendId);
        } else {
            throw new PostNotFoundException("Один из пользователей с таким id не найден");
        }
    }

    @Override
    public List<User> findAllFriends(Long userId) {
        if (isUserExist(userId)) {
            String sql = "SELECT * FROM USERS " +
                    " WHERE USER_ID IN " +
                    " (SELECT FRIEND_ID FROM FRIENDSHIP WHERE USER_ID = ? AND STATUS = 'CONFIRMED');";

            return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), userId);
        } else {
            throw new PostNotFoundException("Пользователь с таким id не найден");
        }
    }

    @Override
    public List<User> findAllMutualFriends(Long userId, Long friendId) {
        if (isUserExist(userId) && isUserExist(friendId)) {
            String sql = "SELECT * FROM USERS WHERE USER_ID IN( " +
                    "SELECT DISTINCT(FRIEND_ID) FROM FRIENDSHIP WHERE USER_ID = ? AND STATUS = 'CONFIRMED' " +
                    " AND FRIEND_ID IN (SELECT FRIEND_ID FROM FRIENDSHIP WHERE USER_ID = ? AND STATUS = 'CONFIRMED')" +
                    " );";

            return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), userId, friendId);
        } else {
            throw new PostNotFoundException("Один из пользователей с таким id не найден");
        }
    }
}
