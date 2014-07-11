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
import java.util.SimpleTimeZone;
import java.util.Date;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.*;

public class Wrapper_gjsairod001 implements QunarCrawler{


    public static void main(String[] args) {

            FlightSearchParam searchParam = new FlightSearchParam();
            searchParam.setDep("AOR");
            searchParam.setArr("SZB");
            searchParam.setDepDate("2014-07-12");
            //searchParam.setRetDate("2014-08-19");
            searchParam.setRetDate("2014-07-28");
            //searchParam.setTimeOut("60000");
            searchParam.setToken("");
            searchParam.setFastTrack(false);
            String html = new  Wrapper_gjsairod001().getHtml(searchParam);
            //System.out.println(html);
    //String detail = new  Wrapper_gjdairou001().getHtml2(searchParam);
    //System.out.println(detail);
            //ProcessResultInfo result = new ProcessResultInfo();
    ProcessResultInfo   result = new  Wrapper_gjsairod001().process(html,searchParam);
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
    
    public BookingResult getBookingInfo(FlightSearchParam arg0) {
    	String bookingUrlPre = "http://booking.croatiaairlines.com/plnext/FPCcroatiaairlines/Override.action";
		BookingResult bookingResult = new BookingResult();
		
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
					
		map.put("trip_type", "one+way");
		map.put("date_flexibility", "fixed");
		map.put("ctl00%24BodyContentPlaceHolder%24UcFlightSelection%24departurePortSelected", "Alor+Setar+%28AOR%29");
		map.put("depart", arg0.getDep());
		map.put("ctl00%24BodyContentPlaceHolder%24UcFlightSelection%24arrivalPortSelected", "Subang+%28SZB%29");
		map.put("dest.1", arg0.getArr());
		map.put("date.0", "arg0");
		map.put("date.1", "");
		map.put("persons.0", "1");
		map.put("persons.1", "0");
		map.put("persons.2", "0");
		map.put("ctl00%24BodyContentPlaceHolder%24UcFlightSelection%24txtDepartureDate", arg0.getDepDate());
		map.put("ctl00%24BodyContentPlaceHolder%24UcFlightSelection%24txtReturnDate","");
		
		if(StringUtils.isNotEmpty(arg0.getRetDate())){
			map.put("trip_type", "return");
			map.put("ctl00%24BodyContentPlaceHolder%24UcFlightSelection%24txtReturnDate", arg0.getDepDate());
		}
		bookingInfo.setInputs(map);		
		bookingResult.setData(bookingInfo);
		bookingResult.setRet(true);
		return bookingResult;
    }

    public String getHtml(FlightSearchParam arg0) {
    	
    	//String depDate=arg0.getDepDate().replaceAll("-","")+"0000";
    	String getUrl = "";
		//String getUrl = "https://secure.malindoair.com/MalindoAirCIBE/OnlineBooking.aspx?trip_type=return&date_flexibility=fixed&ctl00%24BodyContentPlaceHolder%24UcFlightSelection%24departurePortSelected=Alor+Setar+%28AOR%29&depart=AOR&ctl00%24BodyContentPlaceHolder%24UcFlightSelection%24arrivalPortSelected=Subang+%28SZB%29&dest.1=SZB&date.0=12Jul&date.1=28Jul&persons.0=1&persons.1=0&persons.2=0&ctl00%24BodyContentPlaceHolder%24UcFlightSelection%24txtDepartureDate=2014-07-12&ctl00%24BodyContentPlaceHolder%24UcFlightSelection%24txtReturnDate=2014-07-28";
		//String getUrl = "https://secure.malindoair.com/MalindoAirCIBE/OnlineBooking.aspx?trip_type=one+way&date_flexibility=fixed&ctl00%24BodyContentPlaceHolder%24UcFlightSelection%24departurePortSelected=Alor+Setar+%28AOR%29&depart=AOR&ctl00%24BodyContentPlaceHolder%24UcFlightSelection%24arrivalPortSelected=Subang+%28SZB%29&dest.1=SZB&date.0=1Sep&date.1=&persons.0=1&persons.1=0&persons.2=0&ctl00%24BodyContentPlaceHolder%24UcFlightSelection%24txtDepartureDate=2014-09-01&ctl00%24BodyContentPlaceHolder%24UcFlightSelection%24txtReturnDate=";
		if(StringUtils.isEmpty(arg0.getRetDate())){
   		   getUrl = "https://secure.malindoair.com/MalindoAirCIBE/OnlineBooking.aspx?trip_type=one+way&date_flexibility=fixed&ctl00%24BodyContentPlaceHolder%24UcFlightSelection%24departurePortSelected=Alor+Setar+%28AOR%29&depart="+arg0.getDep()+"&ctl00%24BodyContentPlaceHolder%24UcFlightSelection%24arrivalPortSelected=Subang+%28SZB%29&dest.1="+arg0.getArr()+"&date.0=1Sep&date.1=&persons.0=1&persons.1=0&persons.2=0&ctl00%24BodyContentPlaceHolder%24UcFlightSelection%24txtDepartureDate="+arg0.getDepDate()+"&ctl00%24BodyContentPlaceHolder%24UcFlightSelection%24txtReturnDate=";
		}else{
     		getUrl = "https://secure.malindoair.com/MalindoAirCIBE/OnlineBooking.aspx?trip_type=return&date_flexibility=fixed&ctl00%24BodyContentPlaceHolder%24UcFlightSelection%24departurePortSelected=Alor+Setar+%28AOR%29&depart="+arg0.getDep()+"&ctl00%24BodyContentPlaceHolder%24UcFlightSelection%24arrivalPortSelected=Subang+%28SZB%29&dest.1="+arg0.getArr()+"&date.0=12Jul&date.1=28Jul&persons.0=1&persons.1=0&persons.2=0&ctl00%24BodyContentPlaceHolder%24UcFlightSelection%24txtDepartureDate="+arg0.getDepDate()+"&ctl00%24BodyContentPlaceHolder%24UcFlightSelection%24txtReturnDate="+arg0.getRetDate();
		}
            
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
                    e.printStackTrace();
            } finally {                     
                    if (get != null) {
                    	get.releaseConnection();
                    }
            }
            return "Exception";
    
    }

    
    public String getHtml2(FlightSearchParam arg0,String name,String value,String name2,String value2) {
    	QFPostMethod post = null;
        try
        {
        	// get all query parameters from the url set by wrapperSearchInterface
    		QFHttpClient httpClient = new QFHttpClient(arg0, false);
    		httpClient.getParams().setCookiePolicy(
    				CookiePolicy.BROWSER_COMPATIBILITY);

    		post = new QFPostMethod("https://www.bintercanarias.com/booking/searchDo");
     	 	SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
    	 	SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
    	 	String depdate = format.format(format1.parse(arg0.getDepDate()));
    	 	String arrdate = "";
    	 	if(StringUtils.isNotBlank(arg0.getRetDate())){
    	 		arrdate =format.format(format1.parse(arg0.getRetDate()));
    	 	}
    	 	 
    	 	NameValuePair[] names = {
    	 			new NameValuePair("_method","POST"),
    	 			new NameValuePair("data[search][departureDate]",depdate),
    	 			new NameValuePair("data[search][returnDate]",arrdate),
    	 			new NameValuePair("data[search][dateAdvance]","2"),
    	 			//new NameValuePair("search[initDates][0][month]","07"),
    	 			//new NameValuePair("search[initDates][0][year]","2014"),
    	 			
    	 			//new NameValuePair("search[initDates][1][month]","07"),
    	 			//new NameValuePair("search[initDates][1][year]","2014"),
    	 			
    	 			new NameValuePair("data[search][tipoBusqueda]","normal"),
    	 			new NameValuePair("data[search][from]",arg0.getDep()),
    	 			//new NameValuePair("data[search][from_text]","El Hierro"),
    	 			new NameValuePair("data[search][to]",arg0.getArr()),
    	 			//new NameValuePair("data[search][to_text]","Gran Canaria"),
    	 			new NameValuePair("data[search][oneWay]","0"),
    	 			new NameValuePair("data[search][oneWay]","1"),
    	 			new NameValuePair("data[search][onlyPoints]","0"),
    	 			new NameValuePair("data[search][onlyDirectFlights]","0"),
    	 			//new NameValuePair("data[search][departureDateVisual]","1 Jul 2014"),
    	 			
    	 			//new NameValuePair("data[search][returnDateVisual]","12 Jul 2014"),
    	 			new NameValuePair("data[search][calendar]","0"),
    	 			new NameValuePair("data[search][passengers][ADTDC]","0"),
    	 			new NameValuePair("data[search][passengers][ADT]","1"),
    	 			new NameValuePair("data[search][passengers][CHDDC]","0"),
    	 			new NameValuePair("data[search][passengers][CHD]","0"),
    	 			new NameValuePair("data[search][passengers][INFDC]","0"),
    	 			new NameValuePair("data[search][passengers][INF]","0"),
    	 			new NameValuePair("data[search][conditions]","0"),
    	 			new NameValuePair("data[search][flagLess29Fare]","0"),
    	 			new NameValuePair("data[search][flagHigher60Fare]","0"),
    	 			new NameValuePair("data[search][flagLargeFamily]","0"),
    	 			new NameValuePair("data[search][flagUniversityFare]","0"),
    	 			
    	    };
    	    post.setRequestBody(names);
    		post.getParams().setContentCharset("UTF-8");
    		httpClient.executeMethod(post);	
         
		String getUrl = String.format("https://www.bintercanarias.com/booking/infoServiceFee/lang:eng");
        System.out.println("getUrl"+getUrl);;
        post = new QFPostMethod(getUrl);
        if(StringUtils.isBlank(name2)){
        	NameValuePair[] names1 = {
     	 			new NameValuePair("HD",""),
     	 			new NameValuePair(name,value),
     	 			new NameValuePair("csrf","ND"),
             };
        	post.setRequestBody(names1);
        }else{
        	NameValuePair[] namesNew = {
     	 			new NameValuePair("HD",""),
     	 			new NameValuePair(name,value),
     	 			new NameValuePair(name2,value2),
     	 			new NameValuePair("csrf","HD|ND"),
             };
        	post.setRequestBody(namesNew);
        }
         
 		post.getParams().setContentCharset("UTF-8");
 		post.setRequestHeader("X_REQUESTED_WITH", "XMLHttpRequest");
 		post.setRequestHeader("Referer", "https://www.bintercanarias.com/eng/book/select-a-flight/DKR-LPA");
 		String cookie = StringUtils.join(httpClient.getState().getCookies(),"; ");
		post.addRequestHeader("Cookie",cookie);
 		httpClient.executeMethod(post);	
 		return post.getResponseBodyAsString();
        } catch (Exception e) {
                e.printStackTrace();
        } finally {                     
                if (post != null) {
                	post.releaseConnection();
                }
        }
        return "Exception";

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

				 List<RoundTripFlightInfo> flightRoundList = new ArrayList<RoundTripFlightInfo>();
				 
				 List<RoundTripFlightInfo> flightRoundArrList = new ArrayList<RoundTripFlightInfo>();
				 
                 RoundTripFlightInfo roundTripFlightInfo = null;
                 System.out.println(htmlCompress);
		         String  depHtml  =    StringUtils.substringBetween(htmlCompress, "ctl00_BodyContentPlaceHolder_tblOutFlightBlocks", "ctl00_BodyContentPlaceHolder_pnlRetFlights");
		         String  arrHtml  =    StringUtils.substringBetween(htmlCompress, "ctl00_BodyContentPlaceHolder_tblInFlightBlocks", "panellegend");
		         System.out.println(arrHtml);
	             String countArr[] = StringUtils.substringsBetween(arrHtml, "ctl00_BodyContentPlaceHolder_flightRowOutbound", "\"");
	             String countDep[] = StringUtils.substringsBetween(depHtml, "ctl00_BodyContentPlaceHolder_flightRowInbound", "\"");
	             
	             for(int k=0;k<countArr.length;k++){
	            	 
	            	 roundTripFlightInfo = new RoundTripFlightInfo();
	            	 String air[] = countArr[k].split("_");
	            	 System.out.println(countArr[k]);
	            	 String html = "";
	            	 System.out.println("ctl00_BodyContentPlaceHolder_flightRowOutbound"+countArr[k]);
	            	 System.out.println("ctl00_BodyContentPlaceHolder_flightRowOutbound");
	            	 if(k<countArr.length-1){
		            	  html  = StringUtils.substringBetween(arrHtml, "ctl00_BodyContentPlaceHolder_flightRowOutbound"+countArr[k], "ctl00_BodyContentPlaceHolder_flightRowOutbound"+countArr[k+1]);
	            	 }else{
		            	  html  = StringUtils.substringBetween(arrHtml, "ctl00_BodyContentPlaceHolder_flightRowOutbound"+countArr[k],"</tbody>");
	            	 }
		             System.out.println(html);
	            	 int countFlightInfo = StringUtils.countMatches(html, "class=\"flight-table-row");
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
		                    String departTime = StringUtils.substringBetween(depart, "<time>一","</time>");
		                    String arrTime = StringUtils.substringBetween(arr, "<time>一","</time>");
		                    String depairport  = StringUtils.substringBetween(depart, "(",")");
		                    String arrport  = StringUtils.substringBetween(arr, "(",")");
		                    
		                    System.out.println("flightNo"+flightNo);
	   	                    System.out.println("depart"+depart);
	   	                    System.out.println("arr"+arr);
	   	                    System.out.println("depairport"+depairport);
	   	                    System.out.println("arrport"+arrport);
	   	                    System.out.println("price"+price);
	   	                    flightNoList.add(flightNo);
	   	                    seg.setFlightno(flightNo);
	                        seg.setDepDate(arg1.getRetDate());
	                        seg.setDepairport(depairport);
	                        seg.setArrairport(arrport);
	                        seg.setDeptime(departTime);
	                        seg.setArrtime(arrTime);
	                        seg.setArrDate(arg1.getRetDate());
	                        retsegs.add(seg);
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
		                    
		                    roundTripFlightInfo.setDetail(flightDetail);
		                    roundTripFlightInfo.setRetinfo(retsegs);
          	    	        roundTripFlightInfo.setRetflightno(flightNoList);
          	    	        
          	    	    
          	    	      flightRoundArrList.add(roundTripFlightInfo);
	            	 
	            	 }else{

                           //中转效果
	            		    segs = new ArrayList<FlightSegement>();
		                	baseFlight = new OneWayFlightInfo();
		                	flightDetail = new FlightDetail();
		                	seg = new FlightSegement();
		                	flightNoList = new ArrayList<String>();
		                	 int flightMany = StringUtils.countMatches(html, "class=\"flight-table-row");
		                	 for(int j=0;j<flightMany;j++){
		                		    String flightNum = StringUtils.substringBetween(html, "flight-number", "/span>"); 
				                    String depart=   StringUtils.substringBetween(html, "depart-column", "</td>");
				                    String arr = StringUtils.substringBetween(html, "arrive-column", "</td>");
				                    
				                    String flightNo = StringUtils.substringBetween(flightNum, "<br/>","<"); 
				                    String departTime = StringUtils.substringBetween(depart, "<time>一","</time>");
				                    String arrTime = StringUtils.substringBetween(arr, "<time>一","</time>");
				                    String depairport  = StringUtils.substringBetween(depart, "(",")");
				                    String arrport  = StringUtils.substringBetween(arr, "(",")");

			   	                     flightNoList.add(flightNo);
			   	                     
			   	                    seg.setFlightno(flightNo);
			                        seg.setDepDate(arg1.getRetDate());
			                        seg.setDepairport(depairport);
			                        seg.setArrairport(arrport);
			                        seg.setDeptime(departTime);
			                        seg.setArrtime(arrTime);
			                        seg.setArrDate(arg1.getRetDate());
			                        retsegs.add(seg);
				                    html = html.replaceFirst("depart-column", "");
			   	                    html = html.replaceFirst("depart-column", "");
			   	                    html = html.replaceFirst("flight-number", "");
		                	 }
		                    String price = StringUtils.substringBetween(html, "BaseFare:MYR","&lt;br/>");
		                    String tax = StringUtils.substringBetween(html, "TotalTaxes:MYR","&lt;br/>");
	   	                    System.out.println("price"+price);
		                    flightDetail.setFlightno(flightNoList);
		                    flightDetail.setMonetaryunit(monetaryunit);
		                    flightDetail.setPrice(Double.parseDouble(price));
		                    flightDetail.setTax(Double.parseDouble(tax));
		                    flightDetail.setDepcity(arg1.getDep());
		                    flightDetail.setArrcity(arg1.getArr());
		                    flightDetail.setWrapperid(arg1.getWrapperid());
		                    flightDetail.setDepdate(dateDeptDetailDate);                    
		                    roundTripFlightInfo.setDetail(flightDetail);
		                    roundTripFlightInfo.setInfo(segs);
		                    roundTripFlightInfo.setRetinfo(retsegs);
          	    	        roundTripFlightInfo.setRetflightno(flightNoList);
          	    	        
          	    	        
          	    	        flightRoundArrList.add(roundTripFlightInfo);
	            	    
	            	 }
	             }
	             
	             for(int k=0;k<countDep.length;k++){
	            	 
	            	 
	            	 String air[] = countDep[k].split("_");
	            	 System.out.println(countDep[k]);
	            	 String html = "";
	            	 if(k<countDep.length-1){
		            	  html  = StringUtils.substringBetween(depHtml, "ctl00_BodyContentPlaceHolder_flightRowInbound"+countDep[k], "ctl00_BodyContentPlaceHolder_flightRowInbound"+countDep[k+1]);
	            	 }else{
		            	  html  = StringUtils.substringBetween(depHtml, "ctl00_BodyContentPlaceHolder_flightRowInbound"+countDep[k],"</tbody>");
	            	 }
		             System.out.println(html);
	            	 int countFlightInfo = StringUtils.countMatches(html, "class=\"flight-table-row");
	            	 System.out.println("countFlightInfo"+countFlightInfo);
	            	 List<String> flightNoList = null;
	            	 FlightSegement seg = null;
	            	 retsegs = new ArrayList<FlightSegement>();
	            	 System.out.println("countFlightInfocountFlightInfo"+countFlightInfo);
	            	 
	            	 
	            	 if(countFlightInfo == 1){
	            		    segs = new ArrayList<FlightSegement>();
		                	baseFlight = new OneWayFlightInfo();
		                	
		                	seg = new FlightSegement();
		                	flightNoList = new ArrayList<String>();
			                String flightNum = StringUtils.substringBetween(html, "flight-number", "/span>"); 
		                    String depart=   StringUtils.substringBetween(html, "depart-column", "</td>");
		                    String arr = StringUtils.substringBetween(html, "arrive-column", "</td>");
		                    String price = StringUtils.substringBetween(html, "BaseFare:MYR","&lt;br/>");
		                    String tax = StringUtils.substringBetween(html, "TotalTaxes:MYR","&lt;br/>");
		                    
		                    String flightNo = StringUtils.substringBetween(flightNum, "<br/>","<"); 
		                    String departTime = StringUtils.substringBetween(depart, "<time>一","</time>");
		                    String arrTime = StringUtils.substringBetween(arr, "<time>一","</time>");
		                    String depairport  = StringUtils.substringBetween(depart, "(",")");
		                    String arrport  = StringUtils.substringBetween(arr, "(",")");
		                    
		                    System.out.println("flightNo"+flightNo);
	   	                    System.out.println("depart"+depart);
	   	                    System.out.println("arr"+arr);
	   	                    System.out.println("depairport"+depairport);
	   	                    System.out.println("arrport"+arrport);
	   	                    System.out.println("price"+price);
	   	                    
	   	                    flightNoList.add(flightNo);
	   	                     
	   	                    seg.setFlightno(flightNo);
	                        seg.setDepDate(arg1.getRetDate());
	                        seg.setDepairport(depairport);
	                        seg.setArrairport(arrport);
	                        seg.setDeptime(departTime);
	                        seg.setArrtime(arrTime);
	                        seg.setArrDate(arg1.getRetDate());
	                        
	                        segs.add(seg);
	                        
	   	                    html = html.replaceFirst("depart-column", "");
	   	                    html = html.replaceFirst("depart-column", "");
	   	                    html = html.replaceFirst("flight-number", "");
		                    
		   	                
                         
                         
	   	                    
             	    	   for(RoundTripFlightInfo roundTrip:flightRoundArrList){
             	    		  flightDetail = new FlightDetail();
             	    		 flightDetail.setFlightno(flightNoList);
 	                        flightDetail.setMonetaryunit(monetaryunit);
 	                        flightDetail.setPrice(Double.parseDouble(price));
 		                    flightDetail.setTax(Double.parseDouble(tax));
 	                        flightDetail.setDepcity(arg1.getDep());
 	                        flightDetail.setArrcity(arg1.getArr());
 	                        flightDetail.setWrapperid(arg1.getWrapperid());
 	                        flightDetail.setDepdate(dateDeptDetailDate);
             	    		    flightDetail.setPrice(flightDetail.getPrice()+roundTrip.getDetail().getPrice());
         	    		        flightDetail.setTax(flightDetail.getTax()+roundTrip.getDetail().getTax());
         	    		        
             	    		    roundTripFlightInfo = new RoundTripFlightInfo();
             	    		    roundTripFlightInfo.setInfo(segs);
  		                        roundTripFlightInfo.setDetail(flightDetail);
               	    	        roundTripFlightInfo.setOutboundPrice(0);
               	    	        roundTripFlightInfo.setRetdepdate(dateArriDetailDate);
               	    	        roundTripFlightInfo.setReturnedPrice(0);
		                    	roundTripFlightInfo.setRetinfo(roundTrip.getRetinfo());
	             	    	    roundTripFlightInfo.setRetflightno(roundTrip.getRetflightno());
	             	    	    flightRoundList.add(roundTripFlightInfo);
		                    }
             	    	   
		                    
	            	 }else{
                            //中转效果
	            		    segs = new ArrayList<FlightSegement>();
		                	baseFlight = new OneWayFlightInfo();
 		                	seg = new FlightSegement();
		                	flightNoList = new ArrayList<String>();
		                	
		                	 int flightMany = StringUtils.countMatches(html, "class=\"flight-table-row");
		                	 for(int j=0;j<flightMany;j++){
		                		    String flightNum = StringUtils.substringBetween(html, "flight-number", "/span>"); 
				                    String depart=   StringUtils.substringBetween(html, "depart-column", "</td>");
				                    String arr = StringUtils.substringBetween(html, "arrive-column", "</td>");
				                    
				                    String flightNo = StringUtils.substringBetween(flightNum, "<br/>","<"); 
				                    String departTime = StringUtils.substringBetween(depart, "<time>一","</time>");
				                    String arrTime = StringUtils.substringBetween(arr, "<time>一","</time>");
				                    String depairport  = StringUtils.substringBetween(depart, "(",")");
				                    String arrport  = StringUtils.substringBetween(arr, "(",")");

			   	                     flightNoList.add(flightNo);
			   	                     
			   	                    seg.setFlightno(flightNo);
			                        seg.setDepDate(arg1.getRetDate());
			                        seg.setDepairport(depairport);
			                        seg.setArrairport(arrport);
			                        seg.setDeptime(departTime);
			                        seg.setArrtime(arrTime);
			                        seg.setArrDate(arg1.getRetDate());
			                        segs.add(seg);
				                    html = html.replaceFirst("depart-column", "");
			   	                    html = html.replaceFirst("depart-column", "");
			   	                    html = html.replaceFirst("flight-number", "");
		                	 }
		                    String price = StringUtils.substringBetween(html, "BaseFare:MYR","&lt;br/>");
		                    String tax = StringUtils.substringBetween(html, "TotalTaxes:MYR","&lt;br/>");
		                    
	   	                    System.out.println("price"+price);
	   	                 System.out.println("tax"+tax);
		                    
		                    
		                    
		                    FlightDetail flightDetail1 = null;
             	    	   for(RoundTripFlightInfo roundTrip:flightRoundArrList){
             	    		   
             	    		  flightDetail = new FlightDetail();
             	    		  flightDetail.setFlightno(flightNoList);
 		                      flightDetail.setMonetaryunit(monetaryunit);
 		                      flightDetail.setPrice(Double.parseDouble(price));
 		                      flightDetail.setTax(Double.parseDouble(tax));
 		                      flightDetail.setDepcity(arg1.getDep());
 		                      flightDetail.setArrcity(arg1.getArr());
 		                      flightDetail.setWrapperid(arg1.getWrapperid());
 		                      flightDetail.setDepdate(dateDeptDetailDate); 
  	                         
             	    		    flightDetail.setPrice(flightDetail.getPrice()+roundTrip.getDetail().getPrice());
           	    		        flightDetail.setTax(flightDetail.getTax()+roundTrip.getDetail().getTax());
             	    		    roundTripFlightInfo.setInfo(segs);
  		                        roundTripFlightInfo.setDetail(flightDetail);
  		                        roundTripFlightInfo.setRetinfo(retsegs);
               	    	        roundTripFlightInfo.setRetflightno(flightNoList);
               	    	        roundTripFlightInfo.setOutboundPrice(0);
               	    	        roundTripFlightInfo.setRetdepdate(dateArriDetailDate);
               	    	        roundTripFlightInfo.setReturnedPrice(0);
		                    	roundTripFlightInfo.setRetinfo(roundTrip.getRetinfo());
	             	    	    roundTripFlightInfo.setRetflightno(roundTrip.getRetflightno());
	             	    	    flightRoundList.add(roundTripFlightInfo);
		                    }
             	    	   
	            	    }
				     }
	             result.setRet(true);
                 result.setStatus(Constants.SUCCESS);
                 result.setData(flightRoundList);
                 return result;
                 
			 }else{
	            
	             String count[] = StringUtils.substringsBetween(htmlCompress, "ctl00_BodyContentPlaceHolder_flightRowInbound", "\"");
	             for(int k=0;k<count.length;k++){
	            	 String air[] = count[k].split("_");
	            	 System.out.println(count[k]);
	            	 String html = "";
	            	 if(k<count.length-1){
		            	  html  = StringUtils.substringBetween(htmlCompress, "ctl00_BodyContentPlaceHolder_flightRowInbound"+count[k], "ctl00_BodyContentPlaceHolder_flightRowInbound"+count[k+1]);
	            	 }else{
		            	  html  = StringUtils.substringBetween(htmlCompress, "ctl00_BodyContentPlaceHolder_flightRowInbound"+count[k],"</tbody>");
	            	 }
		             System.out.println(html);
	            	 int countFlightInfo = StringUtils.countMatches(html, "class=\"flight-table-row");
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
		                    String departTime = StringUtils.substringBetween(depart, "<time>一","</time>");
		                    String arrTime = StringUtils.substringBetween(arr, "<time>一","</time>");
		                    String depairport  = StringUtils.substringBetween(depart, "(",")");
		                    String arrport  = StringUtils.substringBetween(arr, "(",")");
		                    
		                    System.out.println("flightNo"+flightNo);
	   	                    System.out.println("depart"+depart);
	   	                    System.out.println("arr"+arr);
	   	                    System.out.println("depairport"+depairport);
	   	                    System.out.println("arrport"+arrport);
	   	              
	   	                    System.out.println("price"+price);
	   	                    
	   	                     flightNoList.add(flightNo);
	   	                     
	   	                    seg.setFlightno(flightNo);
	                        seg.setDepDate(arg1.getRetDate());
	                        seg.setDepairport(depairport);
	                        seg.setArrairport(arrport);
	                        seg.setDeptime(departTime);
	                        seg.setArrtime(arrTime);
	                        seg.setArrDate(arg1.getRetDate());
	                        
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
		                	seg = new FlightSegement();
		                	flightNoList = new ArrayList<String>();
		                	
		                	 int flightMany = StringUtils.countMatches(html, "class=\"flight-table-row");
		                	 for(int j=0;j<flightMany;j++){
		                		    String flightNum = StringUtils.substringBetween(html, "flight-number", "/span>"); 
				                    String depart=   StringUtils.substringBetween(html, "depart-column", "</td>");
				                    String arr = StringUtils.substringBetween(html, "arrive-column", "</td>");
				                    
				                    String flightNo = StringUtils.substringBetween(flightNum, "<br/>","<"); 
				                    String departTime = StringUtils.substringBetween(depart, "<time>一","</time>");
				                    String arrTime = StringUtils.substringBetween(arr, "<time>一","</time>");
				                    String depairport  = StringUtils.substringBetween(depart, "(",")");
				                    String arrport  = StringUtils.substringBetween(arr, "(",")");

			   	                     flightNoList.add(flightNo);
			   	                     
			   	                    seg.setFlightno(flightNo);
			                        seg.setDepDate(arg1.getRetDate());
			                        seg.setDepairport(depairport);
			                        seg.setArrairport(arrport);
			                        seg.setDeptime(departTime);
			                        seg.setArrtime(arrTime);
			                        seg.setArrDate(arg1.getRetDate());
			                        segs.add(seg);
				                    html = html.replaceFirst("depart-column", "");
			   	                    html = html.replaceFirst("depart-column", "");
			   	                    html = html.replaceFirst("flight-number", "");
			   	                    
		                	 }
			                
		                    String price = StringUtils.substringBetween(html, "BaseFare:MYR","&lt;br/>");
		                    String tax = StringUtils.substringBetween(html, "TotalTaxes:MYR","&lt;br/>");
		                     
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

}
