package waterLevelFileSystem;

//Include the Dropbox SDK
import com.dropbox.core.*;
import com.dropbox.core.DbxDelta.Entry;

import java.io.*;
import java.util.Locale;

public class dropbox {
	
	public DbxClient connectToDropBox() throws Exception {
		
		// Dropbox API used to connect Java with your Dropbox Account
		final String APP_KEY = "******";
	     final String APP_SECRET = "******";
	     DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);
	     DbxRequestConfig config = new DbxRequestConfig(
	             "JavaTutorial/1.0", Locale.getDefault().toString());
	     DbxWebAuthNoRedirect webAuth = new DbxWebAuthNoRedirect(config, appInfo);
//	     String authorizeUrl = webAuth.start();
//	     System.out.println("1. Go to: " + authorizeUrl);
//	     System.out.println("2. Click \"Allow\" (you might have to log in first)");
//	     System.out.println("3. Copy the authorization code.");
//	     String code = new BufferedReader(new InputStreamReader(System.in)).readLine().trim();
//	     DbxAuthFinish authFinish = webAuth.finish(code);
//	     String accessToken = authFinish.accessToken;
//	     System.out.println(accessToken);
	     String accessToken = "**********";
	     DbxClient client = new DbxClient(config, accessToken);
//	     System.out.println("Linked account: " + client.getAccountInfo().displayName);
	     
	     return client;
	     
	}
	
	// Listing the files and folders under the path you provided
	public void ListingFolders(DbxClient client) throws Exception {
		// Listing Folders
		DbxEntry.WithChildren listing = client.getMetadataWithChildren("/");
	    System.out.println("Files in the root path:");
	    for (DbxEntry child : listing.children) {
	    	System.out.println(child.name);
	    }
	}
	
	// Uploading files to your Dropbox account
	public void uploadingFiles(DbxClient client, String uploadingFileName) throws Exception {
		File inputFile = new File(uploadingFileName);	// Just provide the file name
	    FileInputStream inputStream = new FileInputStream(inputFile);
	    
	    try {
	    	DbxEntry.File uploadedFile = client.uploadFile("/water_level/data/" + uploadingFileName, 
	    			 DbxWriteMode.add(), inputFile.length(), inputStream);
//	    	 System.out.println("Uploaded: " + uploadedFile.toString());
	    } finally {
	    	 inputStream.close();
	    }
	}
	public void uploadingFiles2(DbxClient client, String uploadingFileName) throws Exception {
		File inputFile = new File(uploadingFileName);	// Just provide the file name
	    FileInputStream inputStream = new FileInputStream(inputFile);
	    
	    try {
	    	DbxEntry.File uploadedFile = client.uploadFile("/water_level/" + uploadingFileName, 
	    			 DbxWriteMode.add(), inputFile.length(), inputStream);
//	    	 System.out.println("Uploaded: " + uploadedFile.toString());
	    } finally {
	    	 inputStream.close();
	    }
	}
	
	// Downloading station list and log file. (It locates at a different a path with station data)
	public void downloadingListLogFiles(DbxClient client, String downloadingFileName) throws Exception {
	     FileOutputStream outputStream = new FileOutputStream(downloadingFileName);
	     try {
	    	 DbxEntry.File downloadedFile = client.getFile("/water_level/" + downloadingFileName, null,
	    			 outputStream);
//			 System.out.println("Metadata: " + downloadedFile.toString());
	     } finally {
	    	 outputStream.close();
	     }
	}
	
	// Downloading data files --> used for update operation and model calculation
	public void downloadingFiles(DbxClient client, String downloadingFileName) throws Exception {
	     FileOutputStream outputStream = new FileOutputStream(downloadingFileName);
	     try {
	    	 DbxEntry.File downloadedFile = client.getFile("/water_level/data/" + downloadingFileName, null,
	    			 outputStream);
//			 System.out.println("Metadata: " + downloadedFile.toString());
	     } finally {
	    	 outputStream.close();
	     }
	}


}


