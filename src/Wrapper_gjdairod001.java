import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Lists;
import com.qunar.qfwrapper.bean.booking.BookingResult;
import com.qunar.qfwrapper.bean.booking.BookingInfo;
import com.qunar.qfwrapper.bean.search.FlightSearchParam;
import com.qunar.qfwrapper.bean.search.ProcessResultInfo;
import com.qunar.qfwrapper.bean.search.OneWayFlightInfo;
import com.qunar.qfwrapper.bean.search.FlightDetail;
import com.qunar.qfwrapper.bean.search.FlightSegement;
import com.qunar.qfwrapper.bean.search.RoundTripFlightInfo;
import com.qunar.qfwrapper.interfaces.QunarCrawler;
import com.qunar.qfwrapper.util.QFGetMethod;
import com.qunar.qfwrapper.util.QFPostMethod;
import com.qunar.qfwrapper.util.QFHttpClient;
import com.qunar.qfwrapper.constants.Constants;
import com.travelco.rdf.infocenter.InfoCenter;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.SimpleTimeZone;
import java.util.Date;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.*;

public class Wrapper_gjdairod001 implements QunarCrawler{


    public static void main(String[] args) {

            FlightSearchParam searchParam = new FlightSearchParam();
            searchParam.setDep("COK");
            searchParam.setArr("KCH");
            searchParam.setDepDate("2014-08-23");
            //searchParam.setRetDate("2014-08-19");
            //searchParam.setRetDate("2014-07-28");
            //searchParam.setTimeOut("60000");
            searchParam.setToken("");
            searchParam.setFastTrack(false);
            String html = new  Wrapper_gjdairod001().getHtml(searchParam);
            //System.out.println(html);
    //String detail = new  Wrapper_gjdairou001().getHtml2(searchParam);
    //System.out.println(detail);
            //ProcessResultInfo result = new ProcessResultInfo();
    ProcessResultInfo   result = new  Wrapper_gjdairod001().process(html,searchParam);
    System.out.println(JSON.toJSONString(result,SerializerFeature.DisableCircularReferenceDetect));

           // result = new  Wrapper_gjdairou001().process1(html,detail,searchParam);
            /*if(result.isRet() && result.getStatus().equals(Constants.SUCCESS))
            {
                    List<OneWayFlightInfo> flightList = (List<OneWayFlightInfo>) result.getData();
                    for (OneWayFlightInfo in : flightList){
                            System.out.println("************" + in.getInfo().toString());
                            System.out.println("++++++++++++" + in.getDetail().toString());
                    }
            }
            else
            {
                    System.out.println(result.getStatus());
            }*/
    }


	private String exception;
    
    public BookingResult getBookingInfo(FlightSearchParam arg0) {
    	String bookingUrlPre = "https://secure.malindoair.com/MalindoAirCIBE/OnlineBooking.aspx?";
		BookingResult bookingResult = new BookingResult();
		
		String dayStr = "";
    	String monStr = "";
    	
    	String dayStrRet = "";
    	String monStrRet = "";
    	
    	String day[] = arg0.getDepDate().split("-");
		String days = day[2];
		int dayInt = Integer.parseInt(days);
		dayStr = dayInt+"";
		String mon = day[1];
		//Jan, Feb, Mar, Apr, May, Jun, Jul, Aug, Sep, Oct, Nov, Dec
		if(Integer.parseInt(mon) == 1){
			monStr = "Jan";
		}else if(Integer.parseInt(mon) == 2){
			monStr = "Feb";
		}else if(Integer.parseInt(mon) == 3){
			monStr = "Mar";
		}else if(Integer.parseInt(mon) == 4){
			monStr = "Apr";
		}else if(Integer.parseInt(mon) == 5){
			monStr = "May";
		}else if(Integer.parseInt(mon) == 6){
			monStr = "Jun";
		}else if(Integer.parseInt(mon) == 7){
			monStr = "Jul";
		}else if(Integer.parseInt(mon) == 8){
			monStr = "Aug";
		}else if(Integer.parseInt(mon) == 9){
			monStr = "Sep";
		}else if(Integer.parseInt(mon) == 10){
			monStr = "Oct";
		}else if(Integer.parseInt(mon) == 11){
			monStr = "Nov";
		}else if(Integer.parseInt(mon) == 12){
			monStr = "Dec";
		}
		
		if(StringUtils.isNotEmpty(arg0.getRetDate())){
			
			String dayRet[] = arg0.getRetDate().split("-");
    		String daysRet = dayRet[2];
    		int dayIntRet = Integer.parseInt(daysRet);
    		dayStrRet = dayIntRet+"";
    		String monRet = dayRet[1];
    		//Jan, Feb, Mar, Apr, May, Jun, Jul, Aug, Sep, Oct, Nov, Dec
    		if(Integer.parseInt(monRet) == 1){
    			monStrRet = "Jan";
    		}else if(Integer.parseInt(monRet) == 2){
    			monStrRet = "Feb";
    		}else if(Integer.parseInt(monRet) == 3){
    			monStrRet = "Mar";
    		}else if(Integer.parseInt(monRet) == 4){
    			monStrRet = "Apr";
    		}else if(Integer.parseInt(monRet) == 5){
    			monStrRet = "May";
    		}else if(Integer.parseInt(monRet) == 6){
    			monStrRet = "Jun";
    		}else if(Integer.parseInt(monRet) == 7){
    			monStrRet = "Jul";
    		}else if(Integer.parseInt(monRet) == 8){
    			monStrRet = "Aug";
    		}else if(Integer.parseInt(monRet) == 9){
    			monStrRet = "Sep";
    		}else if(Integer.parseInt(monRet) == 10){
    			monStrRet = "Oct";
    		}else if(Integer.parseInt(monRet) == 11){
    			monStrRet = "Nov";
    		}else if(Integer.parseInt(mon) == 12){
    			monStrRet = "Dec";
    		}
		}
		
    	
		BookingInfo bookingInfo = new BookingInfo();
		bookingInfo.setAction(bookingUrlPre);
		bookingInfo.setMethod("get");
		Map<String, String> map = new LinkedHashMap<String, String>();
		
		/*trip_type=one+way&date_flexibility=fixed&ctl00%24BodyContentPlaceHolder%24UcFlightSelection%24departurePortSelected=Alor+Setar+%28AOR%29
		&depart="+arg0.getDep()+"&ctl00%24BodyContentPlaceHolder%24UcFlightSelection%24arrivalPortSelected=Subang+%28SZB%29
		&dest.1="+arg0.getArr()+"&date.0=1Sep&date.1=&persons.0=1&persons.1=0&persons.2=0
		&ctl00%24BodyContentPlaceHolder%24UcFlightSelection%24txtDepartureDate="+arg0.getDepDate()+"
			&ctl00%24BodyContentPlaceHolder%24UcFlightSelection%24txtReturnDate=
			
		trip_type=return&date_flexibility=fixed&ctl00%24BodyContentPlaceHolder%24UcFlightSelection%24departurePortSelected=Alor+Setar+%28AOR%29&depart=*/
		if(StringUtils.isEmpty(arg0.getRetDate())){
			map.put("trip_type", "one+way");
		}
		
		map.put("date_flexibility", "fixed");
		map.put("ctl00%24BodyContentPlaceHolder%24UcFlightSelection%24departurePortSelected", "Alor+Setar+%28AOR%29");
		map.put("depart", arg0.getDep());
		map.put("ctl00%24BodyContentPlaceHolder%24UcFlightSelection%24arrivalPortSelected", "Subang+%28SZB%29");
		map.put("dest.1", arg0.getArr());
		map.put("date.0", dayStr+monStr);
		
		map.put("persons.0", "1");
		map.put("persons.1", "0");
		map.put("persons.2", "0");
		map.put("ctl00%24BodyContentPlaceHolder%24UcFlightSelection%24txtDepartureDate", arg0.getDepDate());
		map.put("ctl00%24BodyContentPlaceHolder%24UcFlightSelection%24txtReturnDate","");
		
		if(StringUtils.isNotEmpty(arg0.getRetDate())){
			map.put("trip_type", "return");
			map.put("date.1", dayStrRet+monStrRet);
			map.put("ctl00%24BodyContentPlaceHolder%24UcFlightSelection%24txtReturnDate", arg0.getDepDate());
		}
		bookingInfo.setInputs(map);		
		bookingResult.setData(bookingInfo);
		bookingResult.setRet(true);
		return bookingResult;
    }

    public String getHtml(FlightSearchParam arg0) {
    	
    	String getUrl = "";
    	String dayStr = "";
    	String monStr = "";
    	
    	String dayStrRet = "";
    	String monStrRet = "";
    	
    	try {
    		String day[] = arg0.getDepDate().split("-");
    		String days = day[2];
    		int dayInt = Integer.parseInt(days);
    		dayStr = dayInt+"";
    		String mon = day[1];
    		//Jan, Feb, Mar, Apr, May, Jun, Jul, Aug, Sep, Oct, Nov, Dec
    		if(Integer.parseInt(mon) == 1){
    			monStr = "Jan";
    		}else if(Integer.parseInt(mon) == 2){
    			monStr = "Feb";
    		}else if(Integer.parseInt(mon) == 3){
    			monStr = "Mar";
    		}else if(Integer.parseInt(mon) == 4){
    			monStr = "Apr";
    		}else if(Integer.parseInt(mon) == 5){
    			monStr = "May";
    		}else if(Integer.parseInt(mon) == 6){
    			monStr = "Jun";
    		}else if(Integer.parseInt(mon) == 7){
    			monStr = "Jul";
    		}else if(Integer.parseInt(mon) == 8){
    			monStr = "Aug";
    		}else if(Integer.parseInt(mon) == 9){
    			monStr = "Sep";
    		}else if(Integer.parseInt(mon) == 10){
    			monStr = "Oct";
    		}else if(Integer.parseInt(mon) == 11){
    			monStr = "Nov";
    		}else if(Integer.parseInt(mon) == 12){
    			monStr = "Dec";
    		}
    		
    		if(StringUtils.isNotEmpty(arg0.getRetDate())){
    			
    			String dayRet[] = arg0.getRetDate().split("-");
        		String daysRet = dayRet[2];
        		int dayIntRet = Integer.parseInt(daysRet);
        		dayStrRet = dayIntRet+"";
        		String monRet = dayRet[1];
        		//Jan, Feb, Mar, Apr, May, Jun, Jul, Aug, Sep, Oct, Nov, Dec
        		if(Integer.parseInt(monRet) == 1){
        			monStrRet = "Jan";
        		}else if(Integer.parseInt(monRet) == 2){
        			monStrRet = "Feb";
        		}else if(Integer.parseInt(monRet) == 3){
        			monStrRet = "Mar";
        		}else if(Integer.parseInt(monRet) == 4){
        			monStrRet = "Apr";
        		}else if(Integer.parseInt(monRet) == 5){
        			monStrRet = "May";
        		}else if(Integer.parseInt(monRet) == 6){
        			monStrRet = "Jun";
        		}else if(Integer.parseInt(monRet) == 7){
        			monStrRet = "Jul";
        		}else if(Integer.parseInt(monRet) == 8){
        			monStrRet = "Aug";
        		}else if(Integer.parseInt(monRet) == 9){
        			monStrRet = "Sep";
        		}else if(Integer.parseInt(monRet) == 10){
        			monStrRet = "Oct";
        		}else if(Integer.parseInt(monRet) == 11){
        			monStrRet = "Nov";
        		}else if(Integer.parseInt(mon) == 12){
        			monStrRet = "Dec";
        		}
    		}
            
		} catch (Exception e1) {
			e1.printStackTrace();
		}  
         System.out.println("dept==========");
		//String getUrl = "https://secure.malindoair.com/MalindoAirCIBE/OnlineBooking.aspx?trip_type=return&date_flexibility=fixed&ctl00%24BodyContentPlaceHolder%24UcFlightSelection%24departurePortSelected=Alor+Setar+%28AOR%29&depart=AOR&ctl00%24BodyContentPlaceHolder%24UcFlightSelection%24arrivalPortSelected=Subang+%28SZB%29&dest.1=SZB&date.0=12Jul&date.1=28Jul&persons.0=1&persons.1=0&persons.2=0&ctl00%24BodyContentPlaceHolder%24UcFlightSelection%24txtDepartureDate=2014-07-12&ctl00%24BodyContentPlaceHolder%24UcFlightSelection%24txtReturnDate=2014-07-28";
		//String getUrl = "https://secure.malindoair.com/MalindoAirCIBE/OnlineBooking.aspx?trip_type=one+way&date_flexibility=fixed&ctl00%24BodyContentPlaceHolder%24UcFlightSelection%24departurePortSelected=Alor+Setar+%28AOR%29&depart=AOR&ctl00%24BodyContentPlaceHolder%24UcFlightSelection%24arrivalPortSelected=Subang+%28SZB%29&dest.1=SZB&date.0=1Sep&date.1=&persons.0=1&persons.1=0&persons.2=0&ctl00%24BodyContentPlaceHolder%24UcFlightSelection%24txtDepartureDate=2014-09-01&ctl00%24BodyContentPlaceHolder%24UcFlightSelection%24txtReturnDate=";
		if(StringUtils.isEmpty(arg0.getRetDate())){
   		   getUrl = "https://secure.malindoair.com/MalindoAirCIBE/OnlineBooking.aspx?trip_type=one+way&date_flexibility=fixed&ctl00%24BodyContentPlaceHolder%24UcFlightSelection%24departurePortSelected=%28"+arg0.getDep()+"%29&depart="+arg0.getDep()+"&ctl00%24BodyContentPlaceHolder%24UcFlightSelection%24arrivalPortSelected=%28"+arg0.getArr()+"%29&dest.1="+arg0.getArr()+"&date.0="+dayStr+monStr+"&date.1=&persons.0=1&persons.1=0&persons.2=0&ctl00%24BodyContentPlaceHolder%24UcFlightSelection%24txtDepartureDate="+arg0.getDepDate()+"&ctl00%24BodyContentPlaceHolder%24UcFlightSelection%24txtReturnDate=";
		}else{
     		getUrl = "https://secure.malindoair.com/MalindoAirCIBE/OnlineBooking.aspx?trip_type=return&date_flexibility=fixed&ctl00%24BodyContentPlaceHolder%24UcFlightSelection%24departurePortSelected=%28"+arg0.getDep()+"%29&depart="+arg0.getDep()+"&ctl00%24BodyContentPlaceHolder%24UcFlightSelection%24arrivalPortSelected=%28"+arg0.getArr()+"%29&dest.1="+arg0.getArr()+"&date.0="+dayStr+monStr+"&date.1="+dayStrRet+monStrRet+"&persons.0=1&persons.1=0&persons.2=0&ctl00%24BodyContentPlaceHolder%24UcFlightSelection%24txtDepartureDate="+arg0.getDepDate()+"&ctl00%24BodyContentPlaceHolder%24UcFlightSelection%24txtReturnDate="+arg0.getRetDate();
		}
           System.out.println("getUrl=="+getUrl);
		   QFGetMethod get = null;
            try
            {
            QFHttpClient httpClient = new QFHttpClient(arg0, false);

            get = new QFGetMethod(getUrl);
            int status = httpClient.executeMethod(get);
            System.out.println(status);
            String ret = "";
           // if(get.getStatusCode() == HttpStatus.SC_MOVED_TEMPORARILY || get.getStatusCode() == HttpStatus.SC_MOVED_PERMANENTLY){
    			//Header location = get.getResponseHeader("Location");
    			//System.out.println(location.getValue());
    			String url = "https://secure.malindoair.com/MalindoAirCIBE/OnlineBooking.aspx";
    			String cookie = StringUtils.join(httpClient.getState().getCookies(),"; ");
    			//System.out.println("cookit================="+cookie);
    		    get = new QFGetMethod(url);
    			httpClient.getState().clearCookies();
    			get.addRequestHeader("Cookie",cookie);
    			httpClient.executeMethod(get);
    			ret = get.getResponseBodyAsString();
    			//System.out.println(ret);
    		//}
            return get.getResponseBodyAsString();
            } catch (Exception e) {
            	exception = e.toString();
                    e.printStackTrace();
            } finally {                     
                    if (get != null) {
                    	get.releaseConnection();
                    }
            }
            return "Exception"+exception;
    
    }

    
    public ProcessResultInfo process(String arg0, FlightSearchParam arg1) {
    	String monetaryunit = "MYR";
    	
            String htmlCompress = arg0;
            
            ProcessResultInfo result = new ProcessResultInfo();
            if ("Exception".equals(htmlCompress)) {
                    result.setRet(false);
                    result.setStatus(Constants.CONNECTION_FAIL);
                    return result;                  
            }
            //需要有明显的提示语句，才能判断是否INVALID_DATE|INVALID_AIRLINE|NO_RESULT
            if (htmlCompress.contains("We are unable to find recommendations for the date(s) / time(s) specified.")
            		|| htmlCompress.contains("There are no places available on this date")) {
                    result.setRet(false);
                    result.setStatus(Constants.INVALID_DATE);
                    return result;                  
            }
            if (htmlCompress.contains("We cannot find an arrival city as submitted")) {
                result.setRet(false);
                result.setStatus(Constants.NO_RESULT);
                return result;                  
            }
            if (htmlCompress.contains("We are unable to find recommendations for your search")
            		) {
                result.setRet(false);
                result.setStatus(Constants.NO_RESULT);
                return result;                  
            }
            
            htmlCompress  = htmlCompress.replaceAll("\\s+", "");
            

            
            List<OneWayFlightInfo> flightList = new ArrayList<OneWayFlightInfo>();
	        OneWayFlightInfo baseFlight = null;
	        FlightDetail flightDetail = null;
	        List<FlightSegement> segs = null;
	        List<FlightSegement> retsegs = null;
	        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
    		Date dateDeptDetailDate = null;
    		Date dateArriDetailDate = null;
			try {
				dateDeptDetailDate = format1.parse(arg1.getDepDate());
				if(StringUtils.isNotEmpty(arg1.getRetDate())){
					dateArriDetailDate = format1.parse(arg1.getRetDate());
				}
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			
			
			 if(StringUtils.isNotEmpty(arg1.getRetDate())){
				 result.setRet(false);
                 result.setStatus(Constants.NO_RESULT);
                 return result; 
			 }else{
	            
	             String count[] = StringUtils.substringsBetween(htmlCompress, "ctl00_BodyContentPlaceHolder_flightRowInbound", "\"");
	             
	             if(count==null ){
	            	 result.setRet(false);
	                 result.setStatus(Constants.NO_RESULT);
	                 return result; 
	             }
	             for(int k=0;k<count.length;k++){
	            	 String air[] = count[k].split("_");
	            	 System.out.println(count[k]);
	             }
	             List<String> list = new ArrayList<String>();
	             int  train = 1;
	             for(int k=0;k<count.length;k++){
	            	 if(train == 1){
	            		
	            		 if(k<count.length-1){
	            			 String air[] = count[k].split("_");
	            			 String air1[] = count[k+1].split("_");
	            			 if(Integer.parseInt(air[0]) == Integer.parseInt(air1[0])){
		            			 list.add(count[k]);
		            			 train = 2;
		            		 }
	            		 }
	            		 
	            		 
	            	 }else{
	            		 train = 1;
	            	 }
	             }
	              
	             for(int k=0;k<list.size();k++){
	            	 
	            	 /*System.out.println("count==="+count[k]);
	            	 if(k<count.length-1){
		            	 System.out.println("count===++++"+count[k+1]);
	            	 }*/
	            	 System.out.println("list===++++"+list.get(k));
	            	 //System.out.println("list===++++"+list.get(k+1));
	            	 String html = "";
	            	 if(k<list.size()-1){
		            	  html  = StringUtils.substringBetween(htmlCompress, "ctl00_BodyContentPlaceHolder_flightRowInbound"+list.get(k), "ctl00_BodyContentPlaceHolder_flightRowInbound"+list.get(k+1));
	            	 }else{
		            	  html  = StringUtils.substringBetween(htmlCompress, "ctl00_BodyContentPlaceHolder_flightRowInbound"+list.get(k),"</tbody>");
	            	 }
		             System.out.println(html);
	            	 int countFlightInfo = StringUtils.countMatches(html, "\"class=\"flight-table-row");
	            	 System.out.println("countFlightInfo"+countFlightInfo);
	            	 List<String> flightNoList = null;
	            	 FlightSegement seg = null;
	            	 retsegs = new ArrayList<FlightSegement>();
	            	 System.out.println("countFlightInfocountFlightInfo"+countFlightInfo);
	            	 if(countFlightInfo == 1){
	            		    segs = new ArrayList<FlightSegement>();
		                	baseFlight = new OneWayFlightInfo();
		                	flightDetail = new FlightDetail();
		                	seg = new FlightSegement();
		                	flightNoList = new ArrayList<String>();
			                String flightNum = StringUtils.substringBetween(html, "flight-number", "/span>"); 
		                    String depart=   StringUtils.substringBetween(html, "depart-column", "</td>");
		                    String arr = StringUtils.substringBetween(html, "arrive-column", "</td>");
		                    String price = StringUtils.substringBetween(html, "BaseFare:MYR","&lt;br/>");
		                    String tax = StringUtils.substringBetween(html, "TotalTaxes:MYR","&lt;br/>");
		                    
		                    String flightNo = StringUtils.substringBetween(flightNum, "<br/>","<"); 
		                    String departTime = StringUtils.substringBetween(depart, "<time>","</time>");
		                    String arrTime = StringUtils.substringBetween(arr, "<time>","</time>");
		                    String depairport  = StringUtils.substringBetween(depart, "(",")");
		                    String arrport  = StringUtils.substringBetween(arr, "(",")");
		                    
		                    departTime = departTime.substring(1);
		                    arrTime = arrTime.substring(1);
		                    
		                    System.out.println("flightNo"+flightNo);
	   	                    System.out.println("depart"+depart);
	   	                    System.out.println("arr"+arr);
	   	                    System.out.println("depairport"+depairport);
	   	                    System.out.println("arrport"+arrport);
	   	              
	   	                    System.out.println("price"+price);
	   	                    
	   	                     flightNoList.add(flightNo);
	   	                     
	   	                    seg.setFlightno(flightNo);
	                        seg.setDepDate(arg1.getDepDate());
	                        seg.setDepairport(depairport);
	                        seg.setArrairport(arrport);
	                        seg.setDeptime(departTime);
	                        seg.setArrtime(arrTime);
	                        if(compareDate(departTime,arrTime)<0){
		                        seg.setArrDate(dateAdd(arg1.getDepDate()));
	                        }else{
	                        	 seg.setArrDate(arg1.getDepDate());
	                        }
	                        
	                        
	                        segs.add(seg);
	                        
	   	                    html = html.replaceFirst("depart-column", "");
	   	                    html = html.replaceFirst("depart-column", "");
	   	                    html = html.replaceFirst("flight-number", "");
		                    
		                    flightDetail.setFlightno(flightNoList);
		                    flightDetail.setMonetaryunit(monetaryunit);
		                    flightDetail.setPrice(Double.parseDouble(price));
		                    flightDetail.setTax(Double.parseDouble(tax));
		                    flightDetail.setDepcity(arg1.getDep());
		                    flightDetail.setArrcity(arg1.getArr());
		                    flightDetail.setWrapperid(arg1.getWrapperid());
		                    flightDetail.setDepdate(dateDeptDetailDate);                    
		                    
		                    baseFlight.setDetail(flightDetail);
		                    baseFlight.setInfo(segs);
		                    flightList.add(baseFlight);
	            	 }else{
                            //中转效果
	            		    segs = new ArrayList<FlightSegement>();
		                	baseFlight = new OneWayFlightInfo();
		                	flightDetail = new FlightDetail();
		                	
		                	flightNoList = new ArrayList<String>();
		                	
		                	 int flightMany = StringUtils.countMatches(html, "\"class=\"flight-table-row");
		                	 for(int j=0;j<flightMany;j++){
		                		    seg = new FlightSegement();
		                		    String flightNum = StringUtils.substringBetween(html, "flight-number", "/span>"); 
				                    String depart=   StringUtils.substringBetween(html, "depart-column", "</td>");
				                    String arr = StringUtils.substringBetween(html, "arrive-column", "</td>");
				                    
				                    String flightNo = StringUtils.substringBetween(flightNum, "<br/>","<"); 
				                    String departTime = StringUtils.substringBetween(depart, "<time>","</time>");
				                    String arrTime = StringUtils.substringBetween(arr, "<time>","</time>");
				                    String depairport  = StringUtils.substringBetween(depart, "(",")");
				                    String arrport  = StringUtils.substringBetween(arr, "(",")");
				                    departTime = departTime.substring(1);
				                    arrTime = arrTime.substring(1);
			   	                     flightNoList.add(flightNo);
			   	                     
			   	                    seg.setFlightno(flightNo);
			                        seg.setDepDate(arg1.getDepDate());
			                        seg.setDepairport(depairport);
			                        seg.setArrairport(arrport);
			                        seg.setDeptime(departTime);
			                        seg.setArrtime(arrTime);
			                        
			                        if(compareDate(departTime,arrTime)<0){
				                        seg.setArrDate(dateAdd(arg1.getDepDate()));
			                        }else{
			                        	 seg.setArrDate(arg1.getDepDate());
			                        }
			                        
			                        segs.add(seg);
				                    html = html.replaceFirst("arrive-column", "");
			   	                    html = html.replaceFirst("depart-column", "");
			   	                    html = html.replaceFirst("flight-number", "");
			   	                    
		                	 }
		                	 monetaryunit = html.substring(html.indexOf("BaseFare:")+9,html.indexOf("BaseFare:")+12);
		                    String price = StringUtils.substringBetween(html, "BaseFare:"+monetaryunit,"&lt;br/>");
		                    String tax = StringUtils.substringBetween(html, "TotalTaxes:"+monetaryunit,"&lt;br/>");
		                    price = price.replaceAll(",", "");
		                    tax = tax.replaceAll(",", "");
	   	                    System.out.println("price"+price);
	   	                    
		                    flightDetail.setFlightno(flightNoList);
		                    flightDetail.setMonetaryunit(monetaryunit);
		                    flightDetail.setPrice(Double.parseDouble(price));
		                    flightDetail.setTax(Double.parseDouble(tax));
		                    flightDetail.setDepcity(arg1.getDep());
		                    flightDetail.setArrcity(arg1.getArr());
		                    flightDetail.setWrapperid(arg1.getWrapperid());
		                    flightDetail.setDepdate(dateDeptDetailDate);                    
		                    
		                    baseFlight.setDetail(flightDetail);
		                    baseFlight.setInfo(segs);
		                    flightList.add(baseFlight);
	            	    }
				     }
	             }
 				 
            
            result.setRet(true);
            result.setStatus(Constants.SUCCESS);
            result.setData(flightList);
            return result;
    }
    public int compareDate(String start,String end){
    	int result = 0;
    	if(StringUtils.isNotEmpty(start) || StringUtils.isNotEmpty(end)){
    		return result;
    	}
		 SimpleDateFormat formate = new SimpleDateFormat("HH:mm");
		 try {
			result =  formate.parse(end).compareTo(formate.parse(start));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
    }
    public String dateAdd(String date){
    	 Calendar calendar = Calendar.getInstance();//日历对象
		 SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd");
		 try {
			Date dateFormate  =  formate.parse("2014-9-1");
			calendar.setTime(dateFormate);//设置当前日期
			calendar.add(Calendar.DATE, 1);//月份减一
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String dateRet = formate.format(calendar.getTime());
		return dateRet;
    }
}
