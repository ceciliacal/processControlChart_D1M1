package projectchart;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

public class CsvWriterBoundary {

	private CsvWriterBoundary() {
		    throw new IllegalStateException("Utility class");
	}
	
	public static void write (List<Data> list) {
		
		try (PrintWriter writer = new PrintWriter(new File("calcite_outputD1M1.csv"))) {

		      StringBuilder sb = new StringBuilder();
		      sb.append("Month");
		      sb.append(',');
		      
		      sb.append("Year");
		      sb.append(',');
		      
		      sb.append("Count");
		      sb.append('\n');

		      for (int i=0;i<list.size();i++) {	  
		      
			      sb.append(list.get(i).getMonth());	//mese
			      sb.append(',');
			      sb.append(list.get(i).getYear());// anno
			      sb.append(',');
			      sb.append(list.get(i).getCount());//count
			      sb.append('\n');
		      
		      }

		      writer.write(sb.toString());

		    

		    } catch (FileNotFoundException e) {
		    	Log.infoLog(e.getMessage());
	
		    }
		
	}
}