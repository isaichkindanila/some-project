package ru.itis.some.project.repositories.impl;

import lombok.AllArgsConstructor;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import ru.itis.some.project.models.User;
import ru.itis.some.project.repositories.UserRepository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private static final String SQL_FIND_ID =
            "select * from public.user where id = ?";
    private static final String SQL_FIND_EMAIL =
            "select * from public.user where email = ?";
    private static final String SQL_FIND_ALL =
            "select * from public.user";
    private static final String SQL_CREATE =
            "insert into public.user(email, pass_hash, activated) values (?, ?, ?)";
    private static final String SQL_UPDATE =
            "update public.user set email = ?, pass_hash = ?, activated = ? where id = ?";
    private static final String SQL_DELETE =
            "delete from public.user where id = ?";

    private final RowMapper<User> mapper = (row, index) ->
            User.builder()
                    .id(row.getLong("id"))
                    .email(row.getString("email"))
                    .passHash(row.getString("pass_hash"))
                    .isActivated(row.getBoolean("activated"))
                    .build();

    private final JdbcTemplate template;

    @Override
    public Optional<User> find(Long id) {
        try {
            var user = template.queryForObject(SQL_FIND_ID, mapper, id);
            return Optional.ofNullable(user);
        } catch (IncorrectResultSizeDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        try {
            var user = template.queryForObject(SQL_FIND_EMAIL, mapper, email);
            return Optional.ofNullable(user);
        } catch (IncorrectResultSizeDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<User> findAll() {
        return template.query(SQL_FIND_ALL, mapper);
    }

    @Override
    public void create(User model) {
        var holder = new GeneratedKeyHolder();

        template.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(SQL_CREATE, new String[]{"id"});

            statement.setString(1, model.getEmail());
            statement.setString(2, model.getPassHash());
            statement.setBoolean(3, model.isActivated());

            return statement;
        }, holder);

        model.setId((Long) holder.getKey());
    }

    @Override
    public void update(User model) {
        template.update(SQL_UPDATE,
                model.getEmail(),
                model.getPassHash(),
                model.isActivated(),
                model.getId());
    }

    @Override
    public void delete(Long id) {
        template.update(SQL_DELETE, id);
    }
}
