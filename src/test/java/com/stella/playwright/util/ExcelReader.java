package com.stella.playwright.util;

import java.io.File;
import java.io.FileInputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.testng.annotations.DataProvider;

public class ExcelReader {
	
	private static final String DEFAULT_PATH = "./src/test/resources";

	@DataProvider
	public static Object[][] readExcel(String filePath) {
		Object[][] excelData = null;
		try {
			FileInputStream file = new FileInputStream(new File(filePath));
			Workbook workbook = WorkbookFactory.create(file);
			Sheet sheet = workbook.getSheetAt(0);
			Row headerRow = sheet.getRow(0);

			excelData = new Object[sheet.getLastRowNum()][headerRow.getLastCellNum()];

			for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
				Row row = sheet.getRow(rowIndex);
				for (int columnIndex = 0; columnIndex < row.getLastCellNum(); columnIndex++) {
					Cell cell = row.getCell(columnIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

					switch (cell.getCellType()) {
					case STRING:
						excelData[rowIndex-1][columnIndex] = cell.getStringCellValue();
						break;
					case NUMERIC:
						excelData[rowIndex-1][columnIndex] = String.valueOf(cell.getNumericCellValue());
						break;
					case BOOLEAN:
						excelData[rowIndex-1][columnIndex] = String.valueOf(cell.getBooleanCellValue());
						break;
					case BLANK:
						excelData[rowIndex-1][columnIndex] =  ""; // Add an empty string for blank cells
						break;
					default:
						excelData[rowIndex-1][columnIndex] =  "UNKNOWN";
					}
				}
			}
			workbook.close();
			file.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return excelData;
	}
	
	@DataProvider
	public static Object[][] readExcelBySheetName(String filePath, String sheetName) {
		Object[][] excelData = null;
		try {
			FileInputStream file = new FileInputStream(new File(DEFAULT_PATH + filePath));
			Workbook workbook = WorkbookFactory.create(file);
			Sheet sheet = workbook.getSheet(sheetName);
			Row headerRow = sheet.getRow(0);

			excelData = new Object[sheet.getLastRowNum()][headerRow.getLastCellNum()];

			for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
				Row row = sheet.getRow(rowIndex);
				for (int columnIndex = 0; columnIndex < row.getLastCellNum(); columnIndex++) {
					Cell cell = row.getCell(columnIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

					switch (cell.getCellType()) {
					case STRING:
						excelData[rowIndex-1][columnIndex] = cell.getStringCellValue();
						break;
					case NUMERIC:
						excelData[rowIndex-1][columnIndex] = String.valueOf(cell.getNumericCellValue());
						break;
					case BOOLEAN:
						excelData[rowIndex-1][columnIndex] = String.valueOf(cell.getBooleanCellValue());
						break;
					case BLANK:
//						excelData[rowIndex-1][columnIndex] =  ""; // Add an empty string for blank cells
						break;
					default:
						excelData[rowIndex-1][columnIndex] =  "UNKNOWN";
					}
				}
			}
			workbook.close();
			file.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return excelData;
	}
	
}
