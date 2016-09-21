package com.example.webprog26.support.exel;

import android.util.Log;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;



public class WriteExel {

    private static final String TAG = "WriteExel";


    private String mFamiliesSheetTitle, mPersonsSheetTitle;
    private String[] mFamiliesTitles;
    private ArrayList<Integer> mFamiliesNumbers;
    private String[] mPersonsTitles;
    private ArrayList<Integer> mPersonsNumbers;


    public WriteExel(String familiesSheetTitle, String personsSheetTitle, String[] familiesTitles, String[] personsTitles,
                     ArrayList<Integer> familiesNumbers, ArrayList<Integer> personsNumbers) {
        this.mFamiliesSheetTitle = familiesSheetTitle;
        this.mPersonsSheetTitle = personsSheetTitle;
        this.mFamiliesTitles = familiesTitles;
        this.mPersonsTitles = personsTitles;
        this.mFamiliesNumbers = familiesNumbers;
        this.mPersonsNumbers = personsNumbers;
    }

   public void writeExcelBook(String filePath) {
       HSSFWorkbook wb = new HSSFWorkbook();

       Cell cell = null;
       CellStyle csTitles = wb.createCellStyle();
       csTitles.setFillForegroundColor(HSSFColor.WHITE.index);
       csTitles.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
       csTitles.setAlignment(HSSFCellStyle.ALIGN_CENTER);
       csTitles.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
       csTitles.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
       csTitles.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
       csTitles.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);

       CellStyle csData = wb.createCellStyle();
       csData.setFillForegroundColor(HSSFColor.LIME.index);
       csData.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
       csData.setAlignment(HSSFCellStyle.ALIGN_CENTER);
       csData.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
       csData.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
       csData.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
       csData.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);


       Sheet familiesSheet = null;
       Sheet personsSheet = null;

       familiesSheet = wb.createSheet(mFamiliesSheetTitle);
       personsSheet = wb.createSheet(mPersonsSheetTitle);


       Row familiesTitlesRow = familiesSheet.createRow(0);

       for (int i = 0, n = mFamiliesTitles.length; i < n; i++) {
           cell = familiesTitlesRow.createCell(i);
           cell.setCellValue(mFamiliesTitles[i]);
           cell.setCellStyle(csTitles);
           familiesSheet.setColumnWidth(i, (15 * 120));
       }

       Row personsTitlesRow = personsSheet.createRow(0);

       for(int i = 0, n = mPersonsTitles.length; i < n; i++){
           cell = personsTitlesRow.createCell(i);
           cell.setCellValue(mPersonsTitles[i]);
           cell.setCellStyle(csTitles);
           personsSheet.setColumnWidth(i, (15 * 120));
       }

       Row familiesDataRow = familiesSheet.createRow(1);

       for (int i = 0, n = mFamiliesNumbers.size(); i < n; i++) {
           cell = familiesDataRow.createCell(i);
           cell.setCellValue(mFamiliesNumbers.get(i));
           cell.setCellStyle(csData);
           familiesSheet.setColumnWidth(i, (15 * 500));
       }

       Row personsDataRow = personsSheet.createRow(1);

       for (int i = 0, n = mPersonsNumbers.size(); i < n; i++) {
           cell = personsDataRow.createCell(i);
           cell.setCellValue(mPersonsNumbers.get(i));
           cell.setCellStyle(csData);
           personsSheet.setColumnWidth(i, (15 * 500));
       }



       File file = new File(filePath);
       FileOutputStream os = null;

       try {
           os = new FileOutputStream(file);
           wb.write(os);
           Log.i(TAG, "Writing file" + file);
       } catch (IOException ioe) {
           Log.e(TAG, "Error writing " + file, ioe);
       } catch (Exception e) {
           Log.e(TAG, "Error writing to file", e);
       } finally {
           try {
               if (null != os)
                   os.close();
           } catch (Exception ex) {
           }
       }
   }
}
