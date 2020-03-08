package ru.itis.some.project.repositories.impl;

import lombok.AllArgsConstructor;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.itis.some.project.models.FileInfo;
import ru.itis.some.project.repositories.FileInfoRepository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class FileInfoRepositoryImpl implements FileInfoRepository {

    private static final String SQL_FIND_ID =
            "select * from file_info where id = ?";

    private static final String SQL_FIND_TOKEN =
            "select * from file_info where token = ?";

    private static final String SQL_FIND_ALL =
            "select * from file_info";

    private static final String SQL_FIND_BY_USER =
            "select * from file_info where user_id = ?";

    private static final String SQL_CREATE =
            "insert into file_info(token, length, mime_type, original_name, user_id) " +
            "values (?, ?, ?, ?, ?)";

    private static final String SQL_UPDATE =
            "update file_info " +
            "set token = ?, length = ?, mime_type = ?, original_name = ?, user_id = ? " +
            "where id = ?";

    private static final String SQL_DELETE =
            "delete from file_info where id = ?";

    private final RowMapper<FileInfo> mapper = (row, index) ->
            FileInfo.builder()
                    .id(row.getLong("id"))
                    .length(row.getLong("length"))
                    .userId(row.getLong("user_id"))
                    .mimeType(row.getString("mime_type"))
                    .originalName(row.getString("original_name"))
                    .token(row.getString("token"))
                    .build();

    private final JdbcTemplate template;

    @Override
    public Optional<FileInfo> find(Long id) {
        try {
            var file = template.queryForObject(SQL_FIND_ID, mapper, id);
            return Optional.ofNullable(file);
        } catch (IncorrectResultSizeDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<FileInfo> findByToken(String token) {
        try {
            var file = template.queryForObject(SQL_FIND_TOKEN, mapper, token);
            return Optional.ofNullable(file);
        } catch (IncorrectResultSizeDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<FileInfo> findAll() {
        return template.query(SQL_FIND_ALL, mapper);
    }

    @Override
    public List<FileInfo> findByUserId(Long id) {
        return template.query(SQL_FIND_BY_USER, mapper, id);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void create(FileInfo model) {
        var holder = new GeneratedKeyHolder();

        template.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(SQL_CREATE, new String[]{"id"});

            statement.setString(1, model.getToken());
            statement.setLong(2, model.getLength());
            statement.setString(3, model.getMimeType());
            statement.setString(4, model.getOriginalName());
            statement.setLong(5, model.getUserId());

            return statement;
        }, holder);

        model.setId((Long) holder.getKey());
    }

    @Override
    public void update(FileInfo model) {
        template.update(SQL_UPDATE,
                model.getToken(),
                model.getLength(),
                model.getMimeType(),
                model.getOriginalName(),
                model.getUserId(),
                model.getId());
    }

    @Override
    public void delete(Long id) {
        template.update(SQL_DELETE, id);
    }
}
