package vn.vnedu.studyspace.answer_store.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;
import vn.vnedu.studyspace.answer_store.domain.GroupTimeTable;
import vn.vnedu.studyspace.answer_store.service.ColumnConverter;

/**
 * Converter between {@link Row} to {@link GroupTimeTable}, with proper type conversions.
 */
@Service
public class GroupTimeTableRowMapper implements BiFunction<Row, String, GroupTimeTable> {

    private final ColumnConverter converter;

    public GroupTimeTableRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link GroupTimeTable} stored in the database.
     */
    @Override
    public GroupTimeTable apply(Row row, String prefix) {
        GroupTimeTable entity = new GroupTimeTable();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setExamId(converter.fromRow(row, prefix + "_exam_id", Long.class));
        entity.setStartAt(converter.fromRow(row, prefix + "_start_at", Instant.class));
        entity.setEndAt(converter.fromRow(row, prefix + "_end_at", Instant.class));
        entity.setGroupId(converter.fromRow(row, prefix + "_group_id", Long.class));
        entity.setNote(converter.fromRow(row, prefix + "_note", String.class));
        return entity;
    }
}
