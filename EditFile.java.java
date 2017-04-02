import java.io.*;
import java.util.*;

public class EditFile {

	private static final String FILENAME = "filename.txt";

	public static void main(String[] args) {

		BufferedReader br = null;
		FileReader fr = null;
		BufferedWriter bw = null;
		Scanner in = new Scanner(System.in);
		FileWriter fw = null;

		try {
			String content = in.nextLine();

			fr = new FileReader(FILENAME);
			br = new BufferedReader(fr);

			String sCurrentLine;

			br = new BufferedReader(new FileReader(FILENAME));

			fw = new FileWriter(FILENAME, true);
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