package ru.itis.some.project.repositories.impl;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.itis.some.project.models.SignUpToken;
import ru.itis.some.project.models.User;
import ru.itis.some.project.repositories.SignUpTokenRepository;
import ru.itis.some.project.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

@Component
public class SignUpTokenRepositoryImpl implements SignUpTokenRepository {

    private static final String SQL_FIND_ID =
            "select * from sign_up_token where token = ?";
    private static final String SQL_FIND_ALL =
            "select * from sign_up_token";
    private static final String SQL_CREATE =
            "insert into sign_up_token(token, user_id) values (?, ?)";
    private static final String SQL_UPDATE =
            "update sign_up_token set user_id = ? where token = ?";
    private static final String SQL_DELETE =
            "delete from sign_up_token where token = ?";

    private final RowMapper<SignUpToken> mapper;
    private final JdbcTemplate template;

    public SignUpTokenRepositoryImpl(UserRepository userRepository, JdbcTemplate template) {
        this.template = template;

        mapper = (row, index) -> {
            long userId = row.getLong("user_id");
            Optional<User> optional = userRepository.find(userId);

            if (optional.isPresent()) {
                return SignUpToken.builder()
                        .token(row.getString("token"))
                        .user(optional.get())
                        .build();
            } else {
                throw new IllegalStateException("can't find user with id " + userId);
            }
        };
    }

    @Override
    public Optional<SignUpToken> find(String id) {
        try {
            SignUpToken token = template.queryForObject(SQL_FIND_ID, mapper, id);
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
        template.update(SQL_CREATE, model.getToken(), model.getUser().getId());
    }

    @Override
    public void update(SignUpToken model) {
        template.update(SQL_UPDATE, model.getUser().getId(), model.getToken());
    }

    @Override
    public void delete(String id) {
        template.update(SQL_DELETE, id);
    }
}
