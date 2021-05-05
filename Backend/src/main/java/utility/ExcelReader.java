package utility;


import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class ExcelReader {

	private List<ArrayList<String>> excelData;


	public ExcelReader() {
		excelData = new ArrayList<ArrayList<String>>();
	}


	public void readExcelFile(InputStream inputStream, int sheetNumber) {
		HSSFWorkbook workbook = null;
		try {
			workbook = new HSSFWorkbook(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Sheet sheet = workbook.getSheetAt(sheetNumber);


		int numberOfRows = sheet.getLastRowNum();
		//System.out.println( "number of Rows : " + numberOfRows);


		// initialise Arrays in super Array
		for (int i = 0; i <= numberOfRows; i++) {
			excelData.add(new ArrayList<>());
		}

		Iterator<Row> iterator = sheet.iterator();
		int i = 0;
		int j = 0;


		while (iterator.hasNext()) {
			Row nextRow = iterator.next();
			Iterator<Cell> cellIterator = nextRow.cellIterator();

			while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next();

				switch (cell.getCellType()) {
					case Cell.CELL_TYPE_STRING:
						//System.out.println(cell.getStringCellValue());
						excelData.get(i).add(cell.getStringCellValue());
						break;
					case Cell.CELL_TYPE_BOOLEAN:
						//System.out.println( ((Boolean) cell.getBooleanCellValue()).toString());
						excelData.get(i).add(String.valueOf(cell.getBooleanCellValue()));
						break;
					case Cell.CELL_TYPE_NUMERIC:
						double numericCellValue = cell.getNumericCellValue();
						//System.out.println( "double: " + numericCellValue);
						excelData.get(i).add(String.valueOf(numericCellValue));
						break;
					case Cell.CELL_TYPE_FORMULA:
						numericCellValue = cell.getNumericCellValue();
						//System.out.println( "double: " + numericCellValue);
						excelData.get(i).add(String.valueOf(numericCellValue));
						break;
				}
				j++;
				//System.out.println( " - ");
			}
			j = 0;
			//System.out.println( "");
			i++;
		}


		try {
			workbook.close();
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}


	}

	public void readExcelFile(String excelFilePath, int sheetNumber) {

		//System.out.println( "Going to read ExcelFile: " + excelFilePath);
		InputStream inputStream = FileUtil.getInputStream(excelFilePath);
		//String fileExtension = excelFilePath.substring(excelFilePath.lastIndexOf(".") + 1,excelFilePath.length()).toLowerCase();

		readExcelFile(inputStream, sheetNumber);

	}


	public List<ArrayList<String>> getExcelData() {
		return excelData;
	}

}
