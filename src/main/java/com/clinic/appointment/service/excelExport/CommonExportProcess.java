package com.clinic.appointment.service.excelExport;

import com.clinic.appointment.model.MasterData;
import com.clinic.appointment.model.constant.FileType;
import com.clinic.appointment.service.ExportListingService;
import com.clinic.appointment.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public abstract class CommonExportProcess<T extends MasterData, Q> {
    public abstract String getRecordType();
    public abstract FileType getDownloadFileType();
    public abstract String getSheetName();
    public abstract List<T> fetchData(Q query);
    public abstract List<ColumnSpec<T>> columns();
    public abstract String getListingRoute();
    private final ExportListingService exportListingService;
    private final FileService fileService;

    public String fileName(){
        return getSheetName()+".xlsx";
    }

    @Transactional(readOnly = true)
    public ByteArrayInputStream export(Q query){
        try{
            Workbook wb = new XSSFWorkbook();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            writeSheet(wb, query);
            wb.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new ExportFailedException(getListingRoute(),"Export failed during writing sheet");
        }
    }

    @Async
    @Transactional
    public void generateExportFile(Q query){
        ExportListing exportListing = null;
        ByteArrayInputStream in = export(query);

        try {
            exportListing = exportListingService.saveExportListing(getRecordType(), getDownloadFileType());
            fileService.saveExportFileWithStatus(in, exportListing);
        } catch (Exception ex){
            ex.printStackTrace();
            if(exportListing!=null) {
                fileService.saveExportFileWithFailStatus(exportListing);
            }else{
                throw new ExportFailedException(getListingRoute(),"Export failed when it's saved into export listing database");
            }
        }
    }

    public void writeSheet(Workbook wb, Q query){
        Sheet sheet = wb.createSheet(getSheetName());

        CellStyle headerStyle = wb.createCellStyle();
        Font headerFont = wb.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        headerStyle.setWrapText(true);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        Row header = sheet.createRow(0);
        List<ColumnSpec<T>> cols = columns();
        for(int i = 0; i < cols.size(); i++){
            Cell c = header.createCell(i);
            c.setCellValue(cols.get(i).getHeader());
            c.setCellStyle(headerStyle);
        }

        int r = 1;
        for(T rowObject: fetchData(query)){
            Row row = sheet.createRow(r++);
            for(int c = 0; c < cols.size(); c++){
                String text = safe(cols.get(c).getExtractor().apply(rowObject));
                row.createCell(c).setCellValue(text);
            }
        }

        for(int i = 0; i < cols.size(); i++){
            Integer w = cols.get(i).getWidth();
            if(w != null){
                sheet.setColumnWidth(i, w);
            }else{
                sheet.autoSizeColumn(i);
            }
        }

    }

    private String safe(Object v){
        return v == null ? "-" : String.valueOf(v);
    }

}