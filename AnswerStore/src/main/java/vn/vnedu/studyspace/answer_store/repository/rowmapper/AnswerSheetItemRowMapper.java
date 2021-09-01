package vn.vnedu.studyspace.answer_store.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;
import vn.vnedu.studyspace.answer_store.domain.AnswerSheetItem;
import vn.vnedu.studyspace.answer_store.service.ColumnConverter;

/**
 * Converter between {@link Row} to {@link AnswerSheetItem}, with proper type conversions.
 */
@Service
public class AnswerSheetItemRowMapper implements BiFunction<Row, String, AnswerSheetItem> {

    private final ColumnConverter converter;

    public AnswerSheetItemRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link AnswerSheetItem} stored in the database.
     */
    @Override
    public AnswerSheetItem apply(Row row, String prefix) {
        AnswerSheetItem entity = new AnswerSheetItem();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setQuestionId(converter.fromRow(row, prefix + "_question_id", Long.class));
        entity.setAnswerId(converter.fromRow(row, prefix + "_answer_id", Long.class));
        entity.setAnswerSheetId(converter.fromRow(row, prefix + "_answer_sheet_id", Long.class));
        return entity;
    }
}
