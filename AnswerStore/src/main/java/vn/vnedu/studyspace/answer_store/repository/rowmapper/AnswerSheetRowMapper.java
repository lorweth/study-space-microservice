package vn.vnedu.studyspace.answer_store.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;
import vn.vnedu.studyspace.answer_store.domain.AnswerSheet;
import vn.vnedu.studyspace.answer_store.service.ColumnConverter;

/**
 * Converter between {@link Row} to {@link AnswerSheet}, with proper type conversions.
 */
@Service
public class AnswerSheetRowMapper implements BiFunction<Row, String, AnswerSheet> {

    private final ColumnConverter converter;

    public AnswerSheetRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link AnswerSheet} stored in the database.
     */
    @Override
    public AnswerSheet apply(Row row, String prefix) {
        AnswerSheet entity = new AnswerSheet();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", Instant.class));
        entity.setEndAt(converter.fromRow(row, prefix + "_end_at", Instant.class));
        entity.setUserLogin(converter.fromRow(row, prefix + "_user_login", String.class));
        entity.setExamId(converter.fromRow(row, prefix + "_exam_id", Long.class));
        entity.setGroupTimeTableId(converter.fromRow(row, prefix + "_group_time_table_id", Long.class));
        return entity;
    }
}
