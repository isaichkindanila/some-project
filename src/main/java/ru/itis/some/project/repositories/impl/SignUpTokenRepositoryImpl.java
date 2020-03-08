package ru.itis.some.project.repositories.impl;

import lombok.AllArgsConstructor;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.itis.some.project.models.SignUpToken;
import ru.itis.some.project.repositories.SignUpTokenRepository;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class SignUpTokenRepositoryImpl implements SignUpTokenRepository {

    private static final String SQL_FIND_ID =
            "select * from sign_up_token where token = ?";

    private static final String SQL_FIND_ALL =
            "select * from sign_up_token";

    private static final String SQL_CREATE =
            "insert into sign_up_token(token, user_id, used) values (?, ?, ?)";

    private static final String SQL_UPDATE =
            "update sign_up_token set user_id = ?, used = ? where token = ?";

    private static final String SQL_DELETE =
            "delete from sign_up_token where token = ?";

    private final RowMapper<SignUpToken> mapper = (row, index) ->
            SignUpToken.builder()
                    .token(row.getString("token"))
                    .userId(row.getLong("user_id"))
                    .isUsed(row.getBoolean("used"))
                    .build();

    private final JdbcTemplate template;

    @Override
    public Optional<SignUpToken> find(String id) {
        try {
            var token = template.queryForObject(SQL_FIND_ID, mapper, id);
            return Optional.ofNullable(token);
        } catch (IncorrectResultSizeDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<SignUpToken> findAll() {
        return template.query(SQL_FIND_ALL, mapper);
    }

    @Override
    public void create(SignUpToken model) {
        template.update(SQL_CREATE,
                model.getToken(),
                model.getUserId(),
                false);
    }

    @Override
    public void update(SignUpToken model) {
        template.update(SQL_UPDATE,
                model.getUserId(),
                model.isUsed(),
                model.getToken());
    }

    @Override
    public void delete(String id) {
        template.update(SQL_DELETE, id);
    }
}
