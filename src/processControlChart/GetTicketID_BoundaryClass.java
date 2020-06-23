package processControlChart;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

/*
 * 1.	Prendere ID dei ticket tutti di un determinato tipo
2.	Trovare tutti i commit relativi ad ognuno di quei ticket
3.	Trovare ultima data di quei commit relativi a ogni specifico ticket

 */

public class GetTicketID_BoundaryClass {
	
	
	public GetTicketID_BoundaryClass CodiceFalessiGetTicketID() { 
        return this;
	}


   private static String readAll(Reader rd) throws IOException {
	      StringBuilder sb = new StringBuilder();
	      int cp;
	      while ((cp = rd.read()) != -1) {
	         sb.append((char) cp);
	      }
	      return sb.toString();
	   }

   public static JSONArray readJsonArrayFromUrl(String url) throws IOException, JSONException {
      InputStream is = new URL(url).openStream();
      try {
         BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
         String jsonText = readAll(rd);
         JSONArray json = new JSONArray(jsonText);
         return json;
       } finally {
         is.close();
       }
   }

   public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
      InputStream is = new URL(url).openStream();
      try {
         BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
         String jsonText = readAll(rd);
         JSONObject json = new JSONObject(jsonText);
         return json;
       } finally {
         is.close();
       }
   }
   
   public static void insertID(List<String> list, String id) {
	   //metodo che inserisce gli ID (filtrati) in una lista e ritorna la lista
	   list.add(id);
   }
   
   public static List<String> getLenght(List<String> list) {
	   //metodo che restituisce la lista con i Ticket ID filtrati (fixed issues)
	   return list;
   }


  
   public static List<String> getIDList() throws IOException, JSONException {
	   //metodo che restituisce TicketID di tutti i CLOSED o RESOLVED ticket relativi al progetto CALCITE
	   List<String> ticketIDList = new ArrayList<String>();
	   
  	   String projName ="CALCITE";
  	   
	   Integer j = 0, i = 0, total = 1;
	   
      //Get JSON API for closed bugs w/ AV in the project   - > MODIFICATO: prende All Types Issues, non solo bugs
	   
      do {
         //Only gets a max of 1000 at a time, so must do this multiple times if bugs >1000
         j = i + 1000;
         String url = "https://issues.apache.org/jira/rest/api/2/search?jql=project=%22"
                + projName + "%22AND(%22status%22=%22closed%22OR"
                + "%22status%22=%22resolved%22)AND%22resolution%22=%22fixed%22&fields=key,resolutiondate,versions,created&startAt="
                + i.toString() + "&maxResults=" + j.toString();
         JSONObject json = readJsonFromUrl(url);
         JSONArray issues = json.getJSONArray("issues");
         total = json.getInt("total");
         for (; i < total && i < j; i++) {
            //Iterate through each bug
            String key = issues.getJSONObject(i%1000).get("key").toString();
            insertID(ticketIDList,key);
            //System.out.println("ho inserito " + key + " nella lista");
         }  
      } while (i < total);
     
      //System.out.println("Stampo lista: "+ getIDList2(ticketIDList));
      //System.out.println("Stampo lista: "+ ticketIDList);
      return ticketIDList;
   }
   
   /*   
   
   public static void main(String[] args) {
	   try {
		getIDList();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
   }
   */

  	
}

/*
public static void main(String[] args) throws IOException, JSONException {
	   //metodo che restituisce TicketID di tutti i CLOSED o RESOLVED ticket relativi al progetto CALCITE
	   List<String> ticketIDList = new ArrayList<String>();
	   
	   String projName ="CALCITE";
	   
	   Integer j = 0, i = 0, total = 1;
	   
   //Get JSON API for closed bugs w/ AV in the project   - > MODIFICATO: prende All Types Issues, non solo bugs
	   
   do {
      //Only gets a max of 1000 at a time, so must do this multiple times if bugs >1000
      j = i + 1000;
      String url = "https://issues.apache.org/jira/rest/api/2/search?jql=project=%22"
             + projName + "%22AND(%22status%22=%22closed%22OR"
             + "%22status%22=%22resolved%22)AND%22resolution%22=%22fixed%22&fields=key,resolutiondate,versions,created&startAt="
             + i.toString() + "&maxResults=" + j.toString();
      JSONObject json = readJsonFromUrl(url);
      JSONArray issues = json.getJSONArray("issues");
      total = json.getInt("total");
      for (; i < total && i < j; i++) {
         //Iterate through each bug
         String key = issues.getJSONObject(i%1000).get("key").toString();
         insertID(ticketIDList,key);
         System.out.println("ho inserito " + key + " nella lista");
      }  
   } while (i < total);
  
   System.out.println("Stampo lista: "+ getIDList(ticketIDList));
   return;
}
*/

