package com.intel.bb.tool.log.homr;

import com.intel.bb.tool.log.LogDataWriter;
import com.intel.bb.tool.log.LogFetcherClient;
import com.intel.bb.tool.log.dto.MRJobCounter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by yaoy on 5/6/2016.
 */
public class MRJobCounterExcelWriter implements LogDataWriter {

    private Workbook[] wbs = new Workbook[] { new XSSFWorkbook() };

    public void exec(Map<String, List<LogFetcherClient.HSResponse>> data) {
        try {

            for (int i = 0; i < wbs.length; i++) {
                Workbook wb = wbs[i];
                CreationHelper createHelper = wb.getCreationHelper();

                // set work book style
                // For JobId
                CellStyle cs = wb.createCellStyle();
                Font jobIdFont = wb.createFont();
                jobIdFont.setFontHeightInPoints((short) 20);
                jobIdFont.setColor(IndexedColors.BLUE.getIndex());
                jobIdFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
                cs.setFont(jobIdFont);

                // For Counter Group Title
                CellStyle cs2 = wb.createCellStyle();
                Font cgTitleFont = wb.createFont();
                cgTitleFont.setFontHeightInPoints((short) 13);
                cgTitleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
                cs2.setFont(cgTitleFont);

                for (String key : data.keySet()) {
                    List<MRJobCounterClient.HSResponse> counters = data.get(key);

                    // create sheet for each log file
                    Sheet s = wb.createSheet(key);
                    s.setColumnWidth(0, 20000);
                    s.setColumnWidth(1, 10000);
                    s.setColumnWidth(2, 4000);
                    s.setColumnWidth(3, 4000);
                    s.setColumnWidth(4, 4000);

                    int rowNo = 0;
                    for (LogFetcherClient.HSResponse counter : counters) {

                        MRJobCounterClient.YarnJobCounterResponse mrJobCounter
                                = (MRJobCounterClient.YarnJobCounterResponse) counter;

                        // write job id
                        s.createRow(rowNo++);
                        Row jidRow = s.createRow(rowNo++);
                        Cell jobIdCell = jidRow.createCell(0);
                        jobIdCell.setCellStyle(cs);
                        jobIdCell.setCellValue(mrJobCounter.getAppId());

                        // counter title
                        Row cTitleRow = s.createRow(rowNo++);
                        cTitleRow.createCell(0).setCellValue("Counter Group");
                        cTitleRow.createCell(1).setCellValue("Counters");

                        // Print error msg if the data is incorrect.
                        if (!mrJobCounter.isSuccess()) {
                            Row errRow = s.createRow(rowNo++);
                            errRow.createCell(0).setCellValue(mrJobCounter.getMsg());
                            continue;
                        }
                        MRJobCounter.JobCounters rs = mrJobCounter.getRs().getJobCounters();
                        for (MRJobCounter.CounterGroup cg : rs.getCounterGroup()) {
                            // Print counter group title
                            Row cgTitle = s.createRow(rowNo++);
                            Cell cgTitle0 = cgTitle.createCell(0);
                            cgTitle0.setCellValue(cg.getCounterGroupName());
                            cgTitle0.setCellStyle(cs2);
                            Cell cgTitle1 = cgTitle.createCell(1);
                            cgTitle1.setCellValue("Name");
                            cgTitle1.setCellStyle(cs2);
                            Cell cgTitle2 = cgTitle.createCell(2);
                            cgTitle2.setCellValue("Map");
                            cgTitle2.setCellStyle(cs2);
                            Cell cgTitle3 = cgTitle.createCell(3);
                            cgTitle3.setCellValue("Reduce");
                            cgTitle3.setCellStyle(cs2);
                            Cell cgTitle4 = cgTitle.createCell(4);
                            cgTitle4.setCellValue("Total");
                            cgTitle4.setCellStyle(cs2);

                            // Print counters for each counter group
                            for (MRJobCounter.Counter c : cg.getCounter()) {
                                Row cRow = s.createRow(rowNo++);
                                cRow.createCell(1).setCellValue(c.getName());
                                cRow.createCell(2).setCellValue(c.getMapCounterValue());
                                cRow.createCell(3).setCellValue(c.getReduceCounterValue());
                                cRow.createCell(4).setCellValue(c.getTotalCounterValue());
                            }
                        }
                    }
                }
                String filename = "workbook-homr.xls";
                if (wb instanceof XSSFWorkbook) {
                    filename = filename + "x";
                }
                File file = new File(filename);
                if (file.exists() && file.isFile()) file.delete();
                FileOutputStream out = new FileOutputStream(filename);
                wb.write(out);
                out.close();
            }
        } catch (Exception e) {
            System.err.println("Failed to generate excel data.");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
