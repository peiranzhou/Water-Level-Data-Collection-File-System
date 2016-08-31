package waterLevelFileSystem;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.*;
import com.dropbox.core.DbxClient;


public class waterLevel {
	
	public static String stationLine =  null;
	public static String line = null;
	public static String error = "Error: No data was found. This product may not be offered at this station at the requested time.";
	public static String title = "Date Time, Water Level, Sigma, O, F, R, L, Quality ";	
	public static String station_list = "station_list.csv";
	
	public static void main(String[] args) throws Exception {
		
		downloadStationList();									// Download station list from dropbox, at the end of code, it will be deleted.
		
		String[] month = new String[24];						// Initialize month array.
		buildDateString(month);
		int function_number = 0;								// Used for record user's keyboard input.
				
		DownloadLogFile();										// Download Log File
		FileWriter fLog = new FileWriter("log.txt", true);		// Continue to write log
		BufferedWriter bLog = new BufferedWriter(fLog);
		PrintWriter logFile = new PrintWriter(bLog);
		logFile.write('\n');
				
		mainPageView();											// Print main page content.
					
		while(true){
			
			Scanner begin_input = new Scanner(System.in);		// Waiting for user's keyboard input.
			function_number = new Integer(begin_input.nextLine());
				
			if(function_number == 1 || function_number == 2 || function_number == 3){		// Different input number --> different function
				System.out.println("---------- Welcome My Friend! ----------");
				break;
			}
			else{
				System.out.println("Wrong! Please enter again!");
			}
			
		}
				
		logFile.write(getImmediateTime());						// Get the current time
		logFile.write(" Used Function: ");
		logFile.write(Integer.toString(function_number));		// Record function number
		logFile.write('\n');
				
		if (function_number == 1) {
			int startYear = 0;									// Input a year range
			int endYear = 0;
			System.out.println("Please enter start year (1990 - present year)");
			
			while(true){
				Scanner keyboard = new Scanner(System.in);
				startYear = new Integer(keyboard.nextLine());
				
				if(startYear < 1950 || startYear > 2050){					// Year range limitation
					System.out.println("Please re-enter start year");
				}
				else{
					break;
				}
			}
					
				System.out.println("Please enter end year (1990 - present year)");
				while(true){
					Scanner keyboard2 = new Scanner(System.in);
					endYear = new Integer(keyboard2.nextLine());
					if(endYear < 1950 || endYear > 2050 || endYear < startYear){
						System.out.println("Please re-enter end year");
					}
					else{
						break;
					}
				}
					
				// Generate year range array
				String[] year_1 = new String[endYear - startYear + 1];
				for(int i = 0; i < year_1.length; i++){
					year_1[i] = Integer.toString(startYear+i);
				}
					
				yearRange(year_1, month);						// Year range function realization
				
				logFile.write(getImmediateTime());				// Record log into log file 
				logFile.write(" Downloaded: " + Integer.toString(startYear) + " - " + Integer.toString(endYear) + " years' data;");
				logFile.write('\n');
			}
			
			else if (function_number == 2) {
				int temp_year = 0;
				String single_year = null;
				System.out.println("Please enter a year");
				while(true){
					Scanner input_single_year = new Scanner(System.in);
					single_year = input_single_year.nextLine();
					temp_year = Integer.valueOf(single_year);
					if(temp_year < 1950 || temp_year > 2050){
						System.out.println("Please re-enter the year");
					}
					else{
						break;
					}
				}
					
				singleYear(single_year, month);
					
				logFile.write(getImmediateTime());
				logFile.write(" Appended " + Integer.valueOf(temp_year) + "'s data;");
				logFile.write('\n');
			}
				
			else if (function_number == 3) {
				String single_year3 = null;
				String single_month3 = null;
				System.out.println("Please enter a year");
					
				int temp_year2 = 0;
					
				while(true){
					Scanner input_single_year3 = new Scanner(System.in);
					single_year3 = input_single_year3.nextLine();
					temp_year2 = Integer.valueOf(single_year3);
					if(temp_year2 < 1950 || temp_year2 > 2050){
						System.out.println("Please re-enter the year");
					}
					else{
						break;
					}
				}
					
				System.out.println("Please enter a month");
					
				int temp_month3 = 0;
				while(true){
					Scanner input_single_month3 = new Scanner(System.in);
					single_month3 = input_single_month3.nextLine();
					temp_month3 = Integer.valueOf(single_month3);
					if(temp_month3 < 1 || temp_month3 > 12){
						System.out.println("Please re-enter the month");
					}
					else{
						break;
					}
				}
				
				String[] month_range = new String[2];
				month_range[0] = month[(Integer.parseInt(single_month3)-1)*2];
				month_range[1] = month[(Integer.parseInt(single_month3)-1)*2 + 1];
					
				update(single_year3, month_range);
					
				logFile.write(getImmediateTime());
				logFile.write(" Appended " + Integer.toString(temp_year2) + "." + Integer.toString(temp_month3) + "'s data;");
				logFile.write('\n');
			
			}
			logFile.write('\n');
			logFile.close();
			// Delete station file
			File deleteFile = new File(station_list);
			deleteFile.delete();
			UploadAndDeleteLog("log.txt");

	}

	public static void yearRange(String[] year, String[] date) throws Exception{
		
		// Read station ID+NAME into bufferedreader
		BufferedReader stationBr = new BufferedReader(new FileReader(station_list));
		
		while ((stationLine = stationBr.readLine()) != null) {		 
			
			String staionID = stationLine.substring(1, 8);							// get the station id from station input 
			System.out.println("Station:" + staionID);							
			String fileName = stationLine.substring(9, stationLine.length()-1);		// get the station name from station input
			PrintWriter pw = new PrintWriter(new File(fileName + ".csv"));			// For each station, we can create a single file for it
			
			pw.write(title);										// add the title to each file once
			pw.write('\n');	
			
			for(int j = 0; j < year.length; j++){					// year unit loop
				
				String year1 = year[j];
				
				for(int i = 0; i < date.length; i=i+2){				// date unit loop
					
					String date1 = date[i];							// Start date;
					String date2 = date[i+1];						// End date;
					
					// Read csv to bufferedReader
					String url = "http://tidesandcurrents.noaa.gov/api/datagetter?begin_date=" + year1 + date1 +"&end_date=" + year1 + date2 + "&station=" + staionID + "&product=water_level&datum=msl&units=metric&time_zone=gmt&application=web_services&format=csv";
					
					BufferedReader in = getAPIData(url);
					
					
					if(in.readLine().equals("")) {					// 有些station id也许没有任何msl数据at all！
						continue ;
					}
					else {
						
						while ((line = in.readLine()) != null) {	// 判断是否有 没有数据的情况
							String[] myStrings = line.split(",");
							if(line.equals(title) || line.equals(error) || myStrings[1].equals("") || myStrings[2].equals("")){		// 如果一个月内有一半有数据，另一半没有数据，前面没有的部分，删除掉myStrings 1, 2空的元素
								
							}
							else{
								pw.write(line);
								pw.write('\n');
							}
						}
					}
					
				}
				
			}
			
			pw.close();
			
			UploadAndDeleteData(fileName);
			
		}
		System.out.println("finish");

	}
	
	public static void singleYear(String year, String[] date) throws Exception{
		
		// Read station ID+NAME into bufferedreader
		BufferedReader stationBr = new BufferedReader(new FileReader(station_list));
		
		
		while ((stationLine = stationBr.readLine()) != null) {		// 
			
			String staionID = stationLine.substring(1, 8);			// get the station id from station input 
			System.out.println("Station: " + staionID);							
			String fileName = stationLine.substring(9, stationLine.length()-1);				// get the station name from station input
//			PrintWriter pw = new PrintWriter(new File(fileName + ".csv"));		// For each station, we can create a single file for it
			DownloadStationData(fileName);
			
			FileWriter fw = new FileWriter(fileName + ".csv", true);		
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter pw = new PrintWriter(bw);
			pw.write(title);		// add the title to each file once
			pw.write('\n');	
			
				for(int i = 0; i < date.length; i=i+2){				// date unit loop
					
					String date1 = date[i];				// Start date;
					String date2 = date[i+1];			// End date;
					
					// Read csv to bufferedReader
					String url = "http://tidesandcurrents.noaa.gov/api/datagetter?begin_date=" + year + date1 +"&end_date=" + year + date2 + "&station=" + staionID + "&product=water_level&datum=msl&units=metric&time_zone=gmt&application=web_services&format=csv";
					
					BufferedReader in = getAPIData(url);					
					
					while ((line = in.readLine()) != null) {
						String[] myStrings = line.split(",");

						if(line.equals(title) || line.equals(error) || myStrings[1].equals("") || myStrings[2].equals("")){		// 如果一个月内有一半有数据，另一半没有数据，前面没有的部分，删除掉myStrings 1, 2空的元素
							
						}
						else{
							pw.write(line);
							pw.write('\n');
						}
					}
					
				}
					
			pw.close();
			
			UploadAndDeleteData(fileName);
		}
		
		System.out.println("finish");
	}
	
	public static void update(String year, String[] date) throws Exception{
		
		// Read station ID+NAME into bufferedreader
		BufferedReader stationBr = new BufferedReader(new FileReader(station_list));
		
		while ((stationLine = stationBr.readLine()) != null) {		
			
			String staionID = stationLine.substring(1, 8);			// get the station id from station input 
			System.out.println("Station:" + staionID);							
			String fileName = stationLine.substring(9, stationLine.length()-1);				// get the station name from station input
			
			DownloadStationData(fileName);
			
			FileWriter fw = new FileWriter(fileName + ".csv", true);		// For each station, we can create a single file for it
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter pw = new PrintWriter(bw);
			
					
			// Read csv to bufferedReader
			String url = "http://tidesandcurrents.noaa.gov/api/datagetter?begin_date=" + year + date[0] +"&end_date=" + year + date[1] + "&station=" + staionID + "&product=water_level&datum=msl&units=metric&time_zone=gmt&application=web_services&format=csv";
			BufferedReader in = getAPIData(url);
												
			while ((line = in.readLine()) != null) {
				String[] myStrings = line.split(",");

				if(line.equals(title) || line.equals(error) || myStrings[1].equals("") || myStrings[2].equals("")){		// 如果一个月内有一半有数据，另一半没有数据，前面没有的部分，删除掉myStrings 1, 2空的元素
							
				}
				else{
					pw.write(line);
					pw.write('\n');
				}
			}
				
			pw.close();
			
			UploadAndDeleteData(fileName);
		}
		System.out.println("finish");
	}

	public static String getImmediateTime() {
		long systemLogTime = System.currentTimeMillis();
		SimpleDateFormat simpleDateFormat_log = new SimpleDateFormat("MMM dd,yyyy HH:mm:ss");    
	    Date logTime = new Date(systemLogTime);
		return simpleDateFormat_log.format(logTime);
	}
	
	private static void buildDateString(String[] date){

		date[0] = "0101";
		date[1] = "0131";
		date[2] = "0201";
		date[3] = "0228";
		date[4] = "0301";
		date[5] = "0331";
		date[6] = "0401";
		date[7] = "0430";
		date[8] = "0501";
		date[9] = "0531";
		date[10] = "0601";
		date[11] = "0630";
		date[12] = "0701";
		date[13] = "0731";
		date[14] = "0801";
		date[15] = "0831";
		date[16] = "0901";
		date[17] = "0930";
		date[18] = "1001";
		date[19] = "1031";
		date[20] = "1101";
		date[21] = "1130";
		date[22] = "1201";
		date[23] = "1231";


	}

	public static void mainPageView() {
		System.out.println();
		System.out.println("---------- Welcome to Water Level data downloading application! ----------");
		
		// Let user choose which function they want
		System.out.println("Please choose a download type (1, 2, 3):");
		System.out.println("1. Download a year range data (like: 1985 - 2005)");
		System.out.println("2. Download a certain year's data (like: 2015)");
		System.out.println("3. Update original database by month");
		
		System.out.println();
		System.out.println("You choose function NO.: ");
	}
	
	public static void UploadAndDeleteData(String fileName) throws Exception {
		dropbox dropbox = new dropbox();
		DbxClient client = dropbox.connectToDropBox();
		// Delete firstly (take place of overwriting function)
		if (client.getMetadata("/water_level/data/" + fileName + ".csv") != null) {
			client.delete("/water_level/data/" + fileName + ".csv");
		}
		String uploadingFileName = fileName + ".csv";
		dropbox.uploadingFiles(client, uploadingFileName);
		// Delete
		File deleteFile = new File(fileName + ".csv");
		deleteFile.delete();
	}
	
	public static void UploadAndDeleteLog(String fileName) throws Exception {
		dropbox dropbox = new dropbox();
		DbxClient client = dropbox.connectToDropBox();
		// Delete firstly (take place of overwriting function)
		if (client.getMetadata("/water_level/" + fileName) != null) {
			client.delete("/water_level/" + fileName);
		}
		String uploadingFileName = fileName;
		dropbox.uploadingFiles2(client, uploadingFileName);
		// Delete
		File deleteFile = new File(fileName);
		deleteFile.delete();
	}
	
	
	public static void downloadStationList() throws Exception {
		// Initialize dropbox class, in order to use dropbox's methods (upload, download, listing)
		dropbox dropbox = new dropbox();
		DbxClient client = dropbox.connectToDropBox();
				
		// Download Station file, at the end of the code, it will be canceled
		String downloadingFileName = "station_list.csv";
		dropbox.downloadingListLogFiles(client, downloadingFileName);
	}
	
	public static PrintWriter CreateLogFile() throws Exception {
		FileWriter logFileFw = new FileWriter("log.txt", true);		// Recording log for each operation
		BufferedWriter logFileBw = new BufferedWriter(logFileFw);	
		PrintWriter logFile = new PrintWriter(logFileBw);
		return logFile;
	}
	
	public static void DownloadStationData(String fileName) throws Exception {
		// Downloading datafiles to update
		dropbox dropbox = new dropbox();
		DbxClient client = dropbox.connectToDropBox();
		// Download Station file, at the end of the code, it will be canceled
		String downloadingFileName = fileName + ".csv";
		dropbox.downloadingFiles(client, downloadingFileName);
	}
	
	public static BufferedReader getAPIData(String url) throws IOException, IOException {
		InputStream input = new URL(url).openStream();
		Reader reader = new InputStreamReader(input, "UTF-8");
		return new BufferedReader(reader);
	}
	
	public static void DownloadLogFile() throws Exception {
		dropbox dropbox = new dropbox();
		DbxClient client = dropbox.connectToDropBox();
		String downloadingFileName = "log.txt";
		dropbox.downloadingListLogFiles(client, downloadingFileName);
	}
	
}
