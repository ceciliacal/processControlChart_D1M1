package processControlChart;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.json.JSONException;

public class CountBugsPerMonth_ControlClass {
	
    public static void main(String[] args) {
    	
    	//set hash map <key: ticketID, value: date>
    	HashMap<String, LocalDate> commitList = new HashMap<String, LocalDate>();
    	List<Data> dataChartList= new ArrayList<>();
    	CsvWriter writer= new CsvWriter();

    	String path= new String();
    	
    	path="D:\\Cecilia\\Desktop\\calcite";
    	   	
    	try {
			getCommitFromTicketID(path,commitList);
		} catch (NoHeadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	countByMonth(commitList, dataChartList);
    	orderDate(dataChartList);
    	fillDates(dataChartList);
    	printDC(dataChartList);
    	
    	writer.write(dataChartList);
    	
    	
		
    }
    
    public static void orderDate(List<Data> dataChartList) {
    	
    	dataChartList.sort((o1, o2) -> {
    	    int cmp = o1.getYear().compareTo(o2.getYear());
    	    if (cmp == 0)
    	        cmp = Integer.compare(o1.getMonth(), o2.getMonth());
    	    return cmp;
    	});
    	
    	
    	//Collections.sort(dataChartList, (o1, o2) ->  o1.getYear().compareTo(o2.getYear()));
    	//Collections.sort(dataChartList, (o1, o2) ->  o1.getMonth().compareTo(o2.getMonth()));
    	
    	
    }
    
    public static void fillDates(List<Data> dataChartList) {
    	
    	Integer initMonth;
    	Integer lastMonth;
    	Integer initYear;
    	Integer lastYear;
    	int i=1;
    	Data elem;
    	Data previousElem;
    	
    	int dim = dataChartList.size();
    	
    	initMonth=dataChartList.get(0).getMonth();
    	initYear=dataChartList.get(0).getYear();
    	
    	lastMonth=dataChartList.get(dim-1).getMonth();
    	lastYear=dataChartList.get(dim-1).getYear();
    	
    	//scorro lista data
    	//se elem non è elem-1 ++ -> aggiungo alla lista un elemento in indice elem con mese= elem-1 ++ e anno lo stesso di elem (IF mese!=12 -> anno succ e mese=1) con count =0
    	
    	//fai con while e termina quando ultimo Data = last Month, last Year
    	
    	while(dataChartList.get(i).getMonth()!=lastMonth && dataChartList.get(i).getYear()!=lastYear) {
    		Integer newMonth;
    		Integer currentMonth;
    		
    		elem=dataChartList.get(i);
    		previousElem=dataChartList.get(i-1);
    		
    		//System.out.println(" elem 		  -> meese: "+elem.getMonth()+"    anno: "+elem.getYear());
    		System.out.println("previous elem -> mese: "+previousElem.getMonth()+"    anno: "+previousElem.getYear());
    		
    		currentMonth=previousElem.getMonth();
    		newMonth=currentMonth+1;
    		
    		System.out.println("currentMonth: "+currentMonth+"    newMonth: "+newMonth);
    		
			if (currentMonth==12) {
				newMonth=1;
			
			}
    		
			if (elem.getMonth()<12) {
    			if ( elem.getMonth()!= newMonth ) {
    				System.out.println("elem -> mese: "+elem.getMonth()+"    anno: "+elem.getYear());
    				
    				System.out.println("elem da aggiungere-> mese: "+newMonth+"    anno: "+elem.getYear());
    				dataChartList.add(i, new Data(elem.getYear(),newMonth, 0));
    			
    			}
    		}
			else {

	    		if (dataChartList.get(i+1).getMonth()!=1 && dataChartList.get(i+1).getYear()!=elem.getYear()+1) {
	    				
	    				System.out.println("anno succ. sbagliato! ");
	    			}
			}

    		
    		i++;
    		System.out.println("---------------------------------------");
    		System.out.println("ecc");
	
    	}
    }

    
    /*Devo Trovare tutti i commit relativi ad ognuno di quei ticket
     * quindi devo prendere tutti i commit del log e controllare che i Message dei commit contengono l'id del ticket
     * 
     * TO DO:
     * Quindi: devo prendere tutti gli ID ritornati dal CodiceFalessiGetTicketID e verificare che, se uno dei messaggi dei commit nel log
     * contiene UNO di quegli ID, salvo il commit in una lista (e lo stampo)
     * 
     * 
     */
    
    
    public static void getCommitFromTicketID(String pathName, HashMap<String, LocalDate> commitList ) throws IOException, NoHeadException, GitAPIException {
    	//metodo restituisce ticketID + data piu recente del commit in cui quel ticket è stato risolto
    	
    	String currentId;
    	int len;	//len lista dei ticket ID
    	int i;		//indice che scorre lista di ticket ID
    	
    	Git git= Git.open(new File(pathName));
     	
    	//get Commits
    	Iterable<RevCommit> log = git.log().call();
  	
        List<RevCommit> logCommitList = new  ArrayList<RevCommit>();
       
        for (RevCommit commit : log) {
           
            logCommitList.add(commit);
   
        }
    	
    	//get ticket ID list
    	List<String> ticketIDList = new ArrayList<String>();
    	
    	
    	GetTicketID_BoundaryClass ls= new GetTicketID_BoundaryClass();

    	
    	try {
			ticketIDList= ls.getIDList();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) { 
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	len=ticketIDList.size();
    	
    	//scansiono commits nel log e verifico se un commit è relativo a uno degli ID in listaID 
    	
    	
    
    	
    	for (i=0;i<len;i++) {
    		//prendo ticket ID
    		//creo lista che contiene le date dei commit che contengono tale ticketID
    		
    		List<Date> dateList = new ArrayList<Date>();
    		LocalDate latestDate;
    				
    		for (RevCommit commit : logCommitList) {
    			//prendo ogni commit e vedo se contiene un certo ticketID
	    		
    			currentId=ticketIDList.get(i)+"]";
    			
    			if ( commit.getFullMessage().contains(currentId) == true) {
					//inserisco le date dei commit relativi al ticketID corrente in una lista 
					
					dateList.add( commit.getAuthorIdent().getWhen());
					
					//di queste date, prendo quella piu recente 
					latestDate= getLatestDate(dateList).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();	//converting Date to LocalDate (getWhen returns Date type) 
					
					//inserisco <ticketID, latestDate> in hash map
					commitList.put(ticketIDList.get(i), latestDate);	
					
	    		}

    		}
    	
    	}
    	
    	
    	return;
  
    }

  
    public static void printHM(HashMap<String, LocalDate> list) {
    	System.out.println("Stampo hash map: ");
    	
    	for (String i : list.keySet()) {
  	      System.out.println( i + "				" + list.get(i));
  	
    	}
    }
    
    //Trovare ultima data di quei commit relativi a ogni specifico ticket
    
    public static Date getLatestDate(List<Date> list) {
    	
    	Date maxDate = Collections.max(list);
		return maxDate;
    	
    }
    

    
    
    public static List<Data> countByMonth(HashMap<String, LocalDate> list,  List<Data> dataChartList) {
    	//conta quante occorrenze ci sono di un certo mese e anno -> si fa col doppio for
    	
    	//Collections.frequency(list.values(), )
    	List<LocalDate> dateList= new ArrayList<LocalDate> (((HashMap<String,LocalDate>) list).values());
    	int count;
    	int j;
    	int res=0;
    	  
    	//inserisco elementi diversi in lista DataChart in base al mese e all'anno, settando l'attributo count a 0
		for(int i=0;i<list.size();i++){
        
            for (j = 0; j < i; j++) {

               if ((dateList.get(i).getMonthValue() == dateList.get(j).getMonthValue()) && (dateList.get(i).getYear() == dateList.get(j).getYear())) {
                    break;
               }
            }
            
            if(i==j) {
            	dataChartList.add( new Data(dateList.get(j).getYear(),dateList.get(j).getMonthValue(),0));
            	res++;
            }
        }
 	
    	//System.out.println("res= "+res);
    	
    	//Setto attributo count dei DataChart, andando a contare numero di occorrenze in list.values e confrontando con i mesi/anno gia inseriti in ogni DataChart
    	
    	for (j=0;j<dataChartList.size();j++) {
    		
    		count=0;
    		
    		for (int i=0;i<dateList.size();i++) {
    			
    			//System.out.println("j=  "+j+" i= "+i+" -- mese dateList= "+dateList.get(i).getMonthValue()+" -- mese chartList= "+dataChartList.get(j).month+" --  anno dateList= "+dateList.get(i).getYear()+" --  anno chartList= "+dataChartList.get(j).year);
    			if ((dateList.get(i).getMonthValue() == dataChartList.get(j).getMonth()) && (dateList.get(i).getYear() == dataChartList.get(j).getYear())) {
    				count++;	
    			}			
    			
    		}
    		
    		dataChartList.get(j).setCount(count);  		
    	}
    	
    	
    	return dataChartList;

    	
    }
    
    public static void printDC(List<Data> list) {
    	System.out.println("Printing Data Chart list");
    	System.out.println("MONTH"+"		"+"YEAR"+"		"+"COUNT");
    	
    	for (int i=0;i<list.size();i++) {
    		System.out.println(list.get(i).getMonth()+"		"+list.get(i).getYear()+"		"+list.get(i).getCount());
    	}
    	
    }
    
    
}
    
