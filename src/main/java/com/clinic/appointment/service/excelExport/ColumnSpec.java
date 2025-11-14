package com.clinic.appointment.service.excelExport;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.function.Function;

@Data
@AllArgsConstructor
public class ColumnSpec<T> {
    private final String header;
    private final Function<T, String> extractor;
    private final Integer width;
}
