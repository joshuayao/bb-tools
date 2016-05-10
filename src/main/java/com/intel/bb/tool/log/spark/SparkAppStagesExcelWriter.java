package com.intel.bb.tool.log.spark;

import com.intel.bb.tool.log.LogDataWriter;
import com.intel.bb.tool.log.LogFetcherClient;
import com.intel.bb.tool.log.dto.SparkAppStages;
import com.intel.bb.tool.log.homr.MRJobCounterClient;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by yaoy on 5/9/2016.
 */
public class SparkAppStagesExcelWriter implements LogDataWriter {

    private Workbook[] wbs = new Workbook[] { new XSSFWorkbook() };

    @Override
    public void exec(Map<String, List<LogFetcherClient.HSResponse>> data) {
        try {
            for (int i = 0; i < wbs.length; i++) {
                Workbook wb = wbs[i];
                CreationHelper createHelper = wb.getCreationHelper();

                // set work book style
                // For app id
                CellStyle cs = wb.createCellStyle();
                Font jobIdFont = wb.createFont();
                jobIdFont.setFontHeightInPoints((short) 20);
                jobIdFont.setColor(IndexedColors.BLUE.getIndex());
                jobIdFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
                cs.setFont(jobIdFont);

                // For stage id
                CellStyle cs2 = wb.createCellStyle();
                Font stageIdFont = wb.createFont();
                stageIdFont.setFontHeightInPoints((short) 13);
                stageIdFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
                cs2.setFont(stageIdFont);

                for (String key : data.keySet()) {
                    List<MRJobCounterClient.HSResponse> counters = data.get(key);

                    // create sheet for each log file
                    Sheet s = wb.createSheet(key);
                    s.setColumnWidth(0, 10000);
                    s.setColumnWidth(1, 5000);
                    int rowNo = 0;
                    for (LogFetcherClient.HSResponse counter : counters) {

                        SparkAppStagesClient.SparkAppStagesResponse sparkStageCounter
                                = (SparkAppStagesClient.SparkAppStagesResponse) counter;

                        // write app id
                        s.createRow(rowNo++);
                        Row jidRow = s.createRow(rowNo++);
                        Cell jobIdCell = jidRow.createCell(0);
                        jobIdCell.setCellStyle(cs);
                        jobIdCell.setCellValue(sparkStageCounter.getAppId());

                        // Print error msg if the data is incorrect.
                        if (!sparkStageCounter.isSuccess()) {
                            Row errRow = s.createRow(rowNo++);
                            errRow.createCell(0).setCellValue(sparkStageCounter.getMsg());
                            continue;
                        }
                        for (SparkAppStages sas : sparkStageCounter.getRs()) {
                            Row stageRow = s.createRow(rowNo++);
                            Cell stageIdCell = stageRow.createCell(0);
                            stageIdCell.setCellStyle(cs2);
                            stageIdCell.setCellValue("Stage-" + sas.getStageId());

                            // title
                            Row titleRow = s.createRow(rowNo++);
                            titleRow.createCell(0).setCellValue("Property Name");
                            titleRow.createCell(1).setCellValue("Property Value");

                            Row row1 = s.createRow(rowNo++);
                            row1.createCell(0).setCellValue("Name");
                            row1.createCell(1).setCellValue(sas.getName());

                            Row row2 = s.createRow(rowNo++);
                            row2.createCell(0).setCellValue("Details");
                            row2.createCell(1).setCellValue(sas.getDetails());

                            Row row3 = s.createRow(rowNo++);
                            row3.createCell(0).setCellValue("Number of complete tasks");
                            row3.createCell(1).setCellValue(sas.getNumCompleteTasks());

                            Row row4 = s.createRow(rowNo++);
                            row4.createCell(0).setCellValue("Number of failed tasks");
                            row4.createCell(1).setCellValue(sas.getNumFailedTasks());

                            Row row5 = s.createRow(rowNo++);
                            row5.createCell(0).setCellValue("Executor run time");
                            row5.createCell(1).setCellValue(sas.getExecutorRunTime());

                            Row row6 = s.createRow(rowNo++);
                            row6.createCell(0).setCellValue("Input bytes");
                            row6.createCell(1).setCellValue(sas.getInputBytes());

                            Row row7 = s.createRow(rowNo++);
                            row7.createCell(0).setCellValue("Input records");
                            row7.createCell(1).setCellValue(sas.getInputRecords());

                            Row row8 = s.createRow(rowNo++);
                            row8.createCell(0).setCellValue("Output bytes");
                            row8.createCell(1).setCellValue(sas.getOutputBytes());

                            Row row9 = s.createRow(rowNo++);
                            row9.createCell(0).setCellValue("Output records");
                            row9.createCell(1).setCellValue(sas.getOutputRecords());

                            Row row10 = s.createRow(rowNo++);
                            row10.createCell(0).setCellValue("Memory bytes spilled");
                            row10.createCell(1).setCellValue(sas.getMemoryBytesSpilled());

                            Row row11 = s.createRow(rowNo++);
                            row11.createCell(0).setCellValue("Disk bytes spilled");
                            row11.createCell(1).setCellValue(sas.getDiskBytesSpilled());
                        }
                    }
                }
                String filename = "workbook-hos.xls";
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
