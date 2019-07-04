package resources;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Exceldata {
	//Identify Testcases coloum by scanning the entire 1st row
	//once coloumn is identified then scan entire testcase coloum to identify purcjhase testcase row
	//after you grab purchase testcase row = pull all the data of that row and feed into test

	//method to get rows count
	//@Test
	public int getrowscount(String testsheetname, String Excelinppath) throws IOException
	{
		FileInputStream fis1=new FileInputStream(Excelinppath);
		
		XSSFWorkbook workbook=new XSSFWorkbook(fis1);
		XSSFSheet sheet1=workbook.getSheet(testsheetname);
		int sheetscount=sheet1.getLastRowNum();
		workbook.close();
		return sheetscount;	
	}
	
	
	@SuppressWarnings("deprecation")
	//@Test
	public ArrayList<String> getData(String elementname, String Excelinppath, String testsheetname) throws IOException
	{
	
	//Print elementname
	System.out.println("element name in get data code is "+elementname);
	//fileInputStream argument
	ArrayList<String> a=new ArrayList<String>();
	int rowscount=0;
	FileInputStream fis=new FileInputStream(Excelinppath);
	@SuppressWarnings("resource")
	XSSFWorkbook workbook=new XSSFWorkbook(fis);

	int sheets=workbook.getNumberOfSheets();
	
	for(int i=0;i<sheets;i++)
	{
	if(workbook.getSheetName(i).equalsIgnoreCase(testsheetname))
	{
	XSSFSheet sheet=workbook.getSheetAt(i);
	//Identify Testcases coloum by scanning the entire 1st row
	 rowscount = sheet.getLastRowNum();
	 System.out.println("Total number of rows"+rowscount);
	Iterator<Row>  rows= sheet.iterator();// sheet is collection of rows
	Row firstrow= rows.next();
	Iterator<Cell> ce=firstrow.cellIterator();//row is collection of cells
	int k=0;
	int coloumn = 0;
	while(ce.hasNext())
	{
	Cell value=ce.next();

	if(value.getStringCellValue().equalsIgnoreCase("ElementName"))
	{
	coloumn=k;

	}

	k++;
	}
	System.out.println("column number"+coloumn);

	////once coloumn is identified then scan entire testcase coloum to identify purcjhase testcase row
	while(rows.hasNext())
	{

	Row r=rows.next();

	if(r.getCell(coloumn).getStringCellValue().equalsIgnoreCase(elementname))
	{

	////after you grab purchase testcase row = pull all the data of that row and feed into test

	Iterator<Cell>  cv=r.cellIterator();
	while(cv.hasNext())
	{
	Cell c= cv.next();
	if(c.getCellTypeEnum()==CellType.STRING)
	{

	a.add(c.getStringCellValue());
	}
	else{

	a.add(NumberToTextConverter.toText(c.getNumericCellValue()));
	

	}
	}
	}


	}
	}
	}
	return a;

	}

/*	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		datagetfromExcel d=new datagetfromExcel();
		ArrayList<String> data = d.getData("Add Profile");
	System.out.println(data.get(0));
	}*/

	}
