package vn.vnedu.studyspace.answer_store.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;
import vn.vnedu.studyspace.answer_store.domain.GroupMember;
import vn.vnedu.studyspace.answer_store.service.ColumnConverter;

/**
 * Converter between {@link Row} to {@link GroupMember}, with proper type conversions.
 */
@Service
public class GroupMemberRowMapper implements BiFunction<Row, String, GroupMember> {

    private final ColumnConverter converter;

    public GroupMemberRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link GroupMember} stored in the database.
     */
    @Override
    public GroupMember apply(Row row, String prefix) {
        GroupMember entity = new GroupMember();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setUserLogin(converter.fromRow(row, prefix + "_user_login", String.class));
        entity.setGroupId(converter.fromRow(row, prefix + "_group_id", Long.class));
        entity.setRole(converter.fromRow(row, prefix + "_role", Integer.class));
        return entity;
    }
}
