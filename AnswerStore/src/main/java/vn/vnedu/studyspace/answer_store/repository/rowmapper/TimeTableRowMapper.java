package vn.vnedu.studyspace.answer_store.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;
import vn.vnedu.studyspace.answer_store.domain.TimeTable;
import vn.vnedu.studyspace.answer_store.service.ColumnConverter;

/**
 * Converter between {@link Row} to {@link TimeTable}, with proper type conversions.
 */
@Service
public class TimeTableRowMapper implements BiFunction<Row, String, TimeTable> {

    private final ColumnConverter converter;

    public TimeTableRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link TimeTable} stored in the database.
     */
    @Override
    public TimeTable apply(Row row, String prefix) {
        TimeTable entity = new TimeTable();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setTitle(converter.fromRow(row, prefix + "_title", String.class));
        entity.setTime(converter.fromRow(row, prefix + "_time", Instant.class));
        entity.setNote(converter.fromRow(row, prefix + "_note", String.class));
        entity.setUserLogin(converter.fromRow(row, prefix + "_user_login", String.class));
        return entity;
    }
}
