package jUnitScrubAllBrowsers;
//TODO TRAP FILE NOT FOUND FOR FILEWRITE OUTPUT FOLDER/FILE NAME 2017-06-05-100525
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.text.SimpleDateFormat;


public class FileWrite {   //  METHODS write and writeln takes a filename NOT THE SAME as FileWriteRRR
	static File fpathName; // = new File(System.getenv("USERPROFILE")  + File.separator + "Desktop//xSetupAndSmoke"  + File.separator + affixTimeStamp("Test"));
	static String desktopTargetFolder = "xSetupAndSmoke";
	static String userProfileDesktopPath = System.getenv("USERPROFILE") + File.separator + "Desktop//";
	
	public enum LogCode { INFO, WARN, ERROR, FAIL, PASS, FATAL, CSV, HTML }  // used by LogThis (writeln with timestamp and loglevel
 
	public static String createOutputFilepath(String Basefilename, String dotExtension)
			//void setPathFileName(String[] filename, String[] content) // originally an array, now just one file
			{  fpathName = new File( userProfileDesktopPath + desktopTargetFolder );
			  if(!fpathName.exists()) fpathName.mkdirs();
		       // REUSE SAME VARIABLE?!?!
		fpathName = new File( userProfileDesktopPath + desktopTargetFolder  + File.separator +  Basefilename +  dotExtension);
		        int fc=0;	
		// TODO allow for SUBDIR and CREATE if missing, on error report      // 2017-04-29-133549 moved up outside so all can use filename
		     //   boolean exists = findFilesForId(new File("some/path/to/dir"), "XXX").length > 0;
		        System.out.println(fpathName.getAbsolutePath());
		        System.out.println(fpathName.getPath());
		        System.out.println("user.dir: " + System.getProperty("user.dir"));
		        System.out.println("absolute path:  " + fpathName.getAbsolutePath());
		        System.out.println("getName  " + fpathName.getName() );
		        
		        
			while(fpathName.exists() && !fpathName.isDirectory()) {
					// if ( file.getName() == filename){  // there must be a simpler fileexists function
		       fc++;
		       if (fc > 5){ 
		    	System.err.println("FILE NAMING ERROR IN FILEWRITE" );
		   		System.exit(999);
		    	   }
		       // Basefilename=Basefilename + fc;
		    //   fpathName = new File(System.getenv("USERPROFILE") + File.separator + "Desktop//xSetupAndSmoke"  + File.separator +  Basefilename + dotExtension);
		       fpathName = new File(System.getenv("USERPROFILE") + File.separator + "Desktop"  + File.separator + affixTimeStamp(Basefilename)  + dotExtension);
        	    }
	return  fpathName.getAbsolutePath();      //  fpathName.getName();
	}
	
	public static void write(String filePathName, String text) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(filePathName), true));
			bw.write(text);
			System.out.println("file write " + filePathName + ": " + text);
			// bw.newLine();
			bw.close();
		} catch (IOException e) { 
			 System.out.println("FileWrite: line 50 EXCEPTION in ''WRITE'' \n with filePathName: " 
					     + filePathName + "\n and text: + ''" + text + "''"  ) ;
			
			e.printStackTrace() ;
		}
	}
	
	public static void writeln(String filePathName, String text) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(filePathName), true));
			bw.write(text);
			System.out.println("file writeln " + filePathName + ": " + text);
			bw.newLine();
			bw.close();
		} catch (Exception e)
		  {  System.out.println("line 65 CAUGHT EXCEPTION in FileWrite.writeln \n with \nfilePathName: " 
		     + filePathName + "\n and text: + ''" + text + "''"  ) ;
		}
	}

	public static void logthis( LogCode Lcode, String text) { // (String filePathName, String text) {

		String DelimCol = "\",\"";
		String DelimRec = "\"";     // = "\r\n"; handled by writeLN  // end of record //TODO test import with missing beginning of record DelimBOR
		String Tstamp = datetimestamp();
		text = Tstamp + DelimCol + Lcode + DelimCol + text + DelimRec;
		try {
			// return  fpathName.getAbsolutePath(); 
			BufferedWriter bw = new BufferedWriter(new FileWriter(fpathName, true));
			bw.write(text);
			System.out.println("file writeln " + fpathName + ": " + text);
			bw.newLine();
			bw.close();
		} catch (Exception e) {
		System.out.println("line 84 CAUGHT EXCEPTION in  FileWrite.LOGTHIS with \nfile/text:" + fpathName + "\ntext: ''" + text+"''" ) ;
		}
	}

	public static String datetimestamp() {
		String Tstamp = new SimpleDateFormat( "yyyy-MM-dd_HHmmss.SS" ).format( Calendar.getInstance().getTime() );
		return Tstamp;
	}
	
public static String affixTimeStamp(String Basename) // throws IOException {
{
		try {
			String newname = new SimpleDateFormat( "yyyy-MM-dd_HHmmss" ).format( Calendar.getInstance().getTime() );
			Basename  = Basename + "_" + newname;
		} catch (Exception e) {
			System.out.println("FAIL TRY/CATCH in FileWrite affixTimeStamp");
			e.printStackTrace() ;
		}
		return Basename;
	}

public static File[] findFilesForId(File dir, final String basefilename) {
    return dir.listFiles(new FileFilter() {
        public boolean accept(File pathname) {
            return pathname.getName().contains(basefilename); // .equals(basefilename + ".*");
        }
    });
}

public static String readSystemProperties(boolean FLAGVERBOSE) {
	String sysProps="";
if (FLAGVERBOSE) {
	sysProps = "CPU: " + System.getProperty("os.arch") + "\r\n" 
					+ "O/S: " + System.getProperty("os.name") + "\r\n"
					+ "User: " + System.getProperty("user.name") + "\r\n"
					+ "User Home: " + System.getProperty("user.home") + "\r\n"					
					+ "User Folder: " + System.getProperty("user.dir") + "Desktop \r\n"  
					+ "path marker: ''" + System.getProperty("path.separator") + "''"; 
} else { sysProps = System.getProperty("os.arch") + "\r\n" + System.getProperty("os.name") + "\r\n"
	+ System.getProperty("user.name") + "\r\n" + System.getProperty("user.home") + "\r\n"
	+ System.getProperty("user.dir") + "\r\n" + System.getProperty("path.separator");}
return sysProps;
																					
}

/*public static String readSystemProperties() {
 
String sysProps = System.getProperty("os.arch") + "\r\n" + 
		 System.getProperty("os.name") + "\r\n" + 
		 System.getProperty("user.name") + "\r\n" + 
		 System.getProperty("user.home") + "\r\n" + 
		 System.getProperty("user.dir") + "\r\n" + 
		 System.getProperty("path.separator");  // (":" on UNIX)  
return sysProps;
}*/
		
	}

