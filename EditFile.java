package edupack;
import java.io.*;
import java.util.*;

public class EditFile {
	public static void addMessage(String id, String message){

    	String FILENAME = id+".txt";
		BufferedReader br = null;
		FileReader fr = null;
		BufferedWriter bw = null;
		Scanner in = new Scanner(System.in);
		FileWriter fw = null;
		File file= new File (FILENAME);

		try {
			String content = message;//MESSAGE
			content = "<div id=\"rec\">"+content+"</div>";
			/*fr = new FileReader(FILENAME);
			br = new BufferedReader(fr);
			String sCurrentLine;
			br = new BufferedReader(new FileReader(FILENAME));
			*/

			if (file.exists())
			{
			   fw = new FileWriter(FILENAME,true);//if file exists append to file. Works fine.
			}
			else
			{
			   file.createNewFile();
			   fw = new FileWriter(file);
			}
			bw = new BufferedWriter(fw);
			bw.write(content);
			bw.newLine();
			/*while ((sCurrentLine = br.readLine()) != null) {
				bw.write(sCurrentLine);	
				bw.newLine();
			}*/
			System.out.println("Done");

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (br != null)
					br.close();

				if (fr != null)
					fr.close();

				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}

	}

}