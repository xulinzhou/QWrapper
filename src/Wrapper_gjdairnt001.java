import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.lang.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.qunar.qfwrapper.bean.booking.BookingInfo;
import com.qunar.qfwrapper.bean.booking.BookingResult;
import com.qunar.qfwrapper.bean.search.FlightDetail;
import com.qunar.qfwrapper.bean.search.FlightSearchParam;
import com.qunar.qfwrapper.bean.search.FlightSegement;
import com.qunar.qfwrapper.bean.search.OneWayFlightInfo;
import com.qunar.qfwrapper.bean.search.ProcessResultInfo;
import com.qunar.qfwrapper.bean.search.RoundTripFlightInfo;
import com.qunar.qfwrapper.constants.Constants;
import com.qunar.qfwrapper.interfaces.QunarCrawler;
import com.qunar.qfwrapper.util.QFGetMethod;
import com.qunar.qfwrapper.util.QFHttpClient;
import com.qunar.qfwrapper.util.QFPostMethod;


public class Wrapper_gjdairnt001 implements QunarCrawler{

        public static void main(String[] args) {

                FlightSearchParam searchParam = new FlightSearchParam();
                searchParam.setDep("VDE");
                searchParam.setArr("LPA");
                searchParam.setDepDate("2014-07-02");
                //searchParam.setRetDate("2014-07-12");
                //searchParam.setTimeOut("60000");
                searchParam.setToken("");
                //searchParam.setFastTrack(false);
                String html = new  Wrapper_gjdairnt001().getHtml(searchParam);
                //System.out.println(html);
        //String detail = new  Wrapper_gjdairou001().getHtml2(searchParam);
        //System.out.println(detail);
                //ProcessResultInfo result = new ProcessResultInfo();
        ProcessResultInfo   result = new  Wrapper_gjdairnt001().process(html,searchParam);
                
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
        	String bookingUrlPre = "https://www.bintercanarias.com/booking/searchDo";
    		BookingResult bookingResult = new BookingResult();
    		
    		BookingInfo bookingInfo = new BookingInfo();
    		bookingInfo.setAction(bookingUrlPre);
    		bookingInfo.setMethod("post");
    		Map<String, String> map = new LinkedHashMap<String, String>();
    		
    		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
    	 	SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
    	 	try {
				String depdate = format.format(format1.parse(arg0.getDepDate()));
				String arrdate = "";
	    	 	if(StringUtils.isNotBlank(arg0.getRetDate())){
	    	 		arrdate =format.format(format1.parse(arg0.getRetDate()));
	    	 	}
	    	 	map.put("data[search][departureDate]", depdate);
	    	 	map.put("data[search][returnDate]", arrdate);
	    	 	map.put("data[search][dateAdvance]", "2");
	    	 	map.put("data[search][departureDate]", "depdate");
	    	 	map.put("data[search][departureDate]", "depdate");
	    	 	 
	 			//map.put(("search[initDates][0][month]","07"),
	 			//map.put(("search[initDates][0][year]","2014"),
	 			
	 			//map.put(("search[initDates][1][month]","07"),
	 			//map.put(("search[initDates][1][year]","2014"),
	 			
	    	 	map.put("data[search][tipoBusqueda]","normal");
	    	 	map.put("data[search][from]",arg0.getDep());
	 			//map.put("data[search][from_text]","El Hierro");
	 			map.put("data[search][to]",arg0.getArr());
	 			//map.put("data[search][to_text]","Gran Canaria");
	 			map.put("data[search][oneWay]","0");
	 			map.put("data[search][oneWay]","1");
	 			map.put("data[search][onlyPoints]","0");
	 			map.put("data[search][onlyDirectFlights]","0");
	 			//map.put("data[search][departureDateVisual]","1 Jul 2014");
	 			
	 			//map.put("data[search][returnDateVisual]","12 Jul 2014");
	 			map.put("data[search][calendar]","0");
	 			map.put("data[search][passengers][ADTDC]","0");
	 			map.put("data[search][passengers][ADT]","1");
	 			map.put("data[search][passengers][CHDDC]","0");
	 			map.put("data[search][passengers][CHD]","0");
	 			map.put("data[search][passengers][INFDC]","0");
	 			map.put("data[search][passengers][INF]","0");
	 			map.put("data[search][conditions]","0");
	 			map.put("data[search][flagLess29Fare]","0");
	 			map.put("data[search][flagHigher60Fare]","0");
	 			map.put("data[search][flagLargeFamily]","0");
	 			map.put("data[search][flagUniversityFare]","0");
	    		
	    		bookingInfo.setInputs(map);		
	    		bookingResult.setData(bookingInfo);
	    		bookingResult.setRet(true);
	    		return bookingResult;
			} catch (Exception e) {
				e.printStackTrace();
			}
    	 	return null;
    	 	

        }

        public String getHtml(FlightSearchParam arg0) {QFPostMethod post = null;
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
		QFGetMethod get = null;
		String ret = "";
		if(post.getStatusCode() == HttpStatus.SC_MOVED_TEMPORARILY || post.getStatusCode() == HttpStatus.SC_MOVED_PERMANENTLY){
			Header location = post.getResponseHeader("Location");
			System.out.println(location.getValue());
			String urlDetail = arg0.getDep()+"-"+arg0.getArr();
			String url = "https://www.bintercanarias.com/eng/book/select-a-flight/"+urlDetail;
			String cookie = StringUtils.join(httpClient.getState().getCookies(),"; ");
		    get = new QFGetMethod(url);
			httpClient.getState().clearCookies();
			get.addRequestHeader("Cookie",cookie);
			httpClient.executeMethod(get);
			ret = get.getResponseBodyAsString();
		}
		
		return ret;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {			
			if (post != null) {
				post.releaseConnection();
			}
		}
		return "Exception";
	}

        
            public String getHtml2(FlightSearchParam arg0,String num,String sessionId) {
                QFGetMethod get = null;
                try
                {
                QFHttpClient httpClient = new QFHttpClient(arg0, false);
        		String getUrl = String.format("http://booking.croatiaairlines.com/plnext/FPCcroatiaairlines/FlexPricerFlightDetailsPopUp.action;jsessionid=%s?SITE=BAXGBAXG&LANGUAGE=GB&PAGE_TICKET=0&TRIP_TYPE=O&PRICING_TYPE=I&DISPLAY_TYPE=1&ARRANGE_BY=D&FLIGHT_ID_1=%s&FLIGHT_ID_2=",sessionId,num);
                 System.out.println("getUrl"+getUrl);;
                get = new QFGetMethod(getUrl);
                 int status = httpClient.executeMethod(get);
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

            public String getHtml3(FlightSearchParam arg0,String num,String sessionId) {
                QFGetMethod get = null;
                try
                {
                QFHttpClient httpClient = new QFHttpClient(arg0, false);
        		String getUrl = String.format("http://booking.croatiaairlines.com/plnext/FPCcroatiaairlines/FlexPricerFlightDetailsPopUp.action;jsessionid=%s?SITE=BAXGBAXG&LANGUAGE=GB&PAGE_TICKET=0&TRIP_TYPE=O&PRICING_TYPE=I&DISPLAY_TYPE=1&ARRANGE_BY=D&FLIGHT_ID_1=&FLIGHT_ID_2=%s",sessionId,num);
                 System.out.println("getUrl"+getUrl);;
                get = new QFGetMethod(getUrl);
                 int status = httpClient.executeMethod(get);
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
            

        public ProcessResultInfo process(String arg0, FlightSearchParam arg1) {
        	String monetaryunit = "HRK";
        	
                String htmlCompress = arg0;
                
                ProcessResultInfo result = new ProcessResultInfo();
                if ("Exception".equals(htmlCompress)) {
                        result.setRet(false);
                        result.setStatus(Constants.CONNECTION_FAIL);
                        return result;                  
                }
                //需要有明显的提示语句，才能判断是否INVALID_DATE|INVALID_AIRLINE|NO_RESULT
                if (htmlCompress.contains("We are unable to find recommendations for the date(s) / time(s) specified.")) {
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
                		|| htmlCompress.contains("WDSErrorE")) {
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
	                 RoundTripFlightInfo roundTripFlightInfo = null;
	        	    	
					 String start = StringUtils.substringBetween(htmlCompress, "id=\"segment_0\"", "foot-raterounded-corners-bottom-8");
					 
					 
					 
					 int countStartFlightInfo = StringUtils.countMatches(start, "availability-cell-leftlast");
					 
					 
					 for(int i=0;i<countStartFlightInfo;i++){
						    
						    segs = new ArrayList<FlightSegement>();
						    
						    flightDetail = new FlightDetail();
	                	    FlightSegement seg = null;
	                	 
						    List<String> retflightno = new ArrayList<String>();
						    List<String> flightNoList = new ArrayList<String>();
						    String info=   StringUtils.substringBetween(start, "availability-cell-leftlastcolor-999\"><", "</div>");
		                    String price = StringUtils.substringBetween(start, "data-group=\"jsGroup-generic\"", "</div>");
		                    
		                    System.out.println("info===="+info);
		                    System.out.println("price===="+price);
		                    int countFlight = StringUtils.countMatches(info, "</span>");
		                    
		                    int lastIndex = StringUtils.lastIndexOf(price, "type=\"radio\"");
		                    //价格为空
		                    if(lastIndex==-1){
		                    	String price2 = StringUtils.substringBetween(start, "data-group=\"jsGroup-masFare\"", "</div>");
		                    	lastIndex = StringUtils.lastIndexOf(price2, "type=\"radio\"");
		                    	if(lastIndex == -1){
		                    		String price3 = StringUtils.substringBetween(start, "data-group=\"jsGroup-binterMasPoints\"", "</div>");
		                    		lastIndex = StringUtils.lastIndexOf(price3, "type=\"radio\"");
		                    		price = price3;
		                    	}else{
		                        	price = price2;
		                    	}
		                    }
		                    
		                    System.out.println("lastIndex===="+lastIndex);
		                    price = price.substring(lastIndex);
		                    String hour = StringUtils.substringBetween(price, "data-hours=\"", "\"");
		                    String hourArray[] = hour.split("\\;");
		                    System.out.println("hour"+hour);
		                    flightNoList = new ArrayList<String>();
		                   
		                    for(int j=0;j<countFlight;j++){
		                    	seg = new FlightSegement();
		                        String flightNo =   StringUtils.substringBetween(info, "'>", "</span>");
		                        flightNoList.add(flightNo);
		                        String orgin = StringUtils.substringBetween(info, "data-origin='", "'");
		                        String des = StringUtils.substringBetween(info, "data-destination='", "'");
		                        String date = StringUtils.substringBetween(info, "data-date='", "'");
		                        
		                        System.out.println("flightNo========"+flightNo);
		                        seg.setFlightno(flightNo);
		                        String detailHour = hourArray[j];
		                        seg.setDepDate(date);
		                        seg.setDepairport(orgin);
		                        seg.setArrairport(des);
		                        seg.setDeptime(detailHour.split("-")[0]);
		                        seg.setArrtime(detailHour.split("-")[1]);
		                        seg.setArrDate(date);
		                        
		                        segs.add(seg);
		                        info = info.replaceFirst("'>", "");
		                        info = info.replaceFirst("data-origin='", "");
		                        info = info.replaceFirst("data-destination='", "");
		                        info = info.replaceFirst("data-date='", "");
		                    }
		                    
		                    String taxes = StringUtils.substringBetween(price, "data-taxes=\"", "\"");
		                    String fare = StringUtils.substringBetween(price, "data-fareimport=\"", "\"");
		                    
		                    flightDetail.setFlightno(flightNoList);
                            flightDetail.setMonetaryunit(monetaryunit);
                            flightDetail.setPrice(Double.parseDouble(fare));
                            flightDetail.setDepcity(arg1.getDep());
                            flightDetail.setArrcity(arg1.getArr());
                            flightDetail.setWrapperid(arg1.getWrapperid());
                            flightDetail.setDepdate(dateDeptDetailDate);
                            
                            
		                    
		                    
		                    System.out.println("fare========="+fare);
		                    System.out.println("taxes========"+taxes);
		                    
		                    System.out.println("==============================================================================");
		                    
		                    String end = StringUtils.substringBetween(htmlCompress, "id=\"segment_1\"", "foot-raterounded-corners-bottom-8");
							int countEndFlightInfo = StringUtils.countMatches(end, "availability-cell-leftlast");
							
							
							
		                    for(int k=0;k<countEndFlightInfo;k++){
		                    	
		                    	roundTripFlightInfo = new RoundTripFlightInfo();
		                    	roundTripFlightInfo.setDetail(flightDetail);
	                            roundTripFlightInfo.setInfo(segs);
	                            
		                    	retsegs = new ArrayList<FlightSegement>();
		                    	System.out.println("======================================start");
		                    	String infoRet=   StringUtils.substringBetween(end, "availability-cell-leftlastcolor-999\"><", "</div>");
			                    String priceRet = StringUtils.substringBetween(end, "data-group=\"jsGroup-generic\"", "</div>");
			                    
			                    System.out.println("info===="+infoRet);
			                    System.out.println("price===="+priceRet);
			                    int countFlightRet = StringUtils.countMatches(infoRet, "</span>");
			                    
			                    int lastIndexRet = StringUtils.lastIndexOf(priceRet, "type=\"radio\"");
			                    //价格为空
			                    if(lastIndexRet==-1){
			                    	String price2 = StringUtils.substringBetween(end, "data-group=\"jsGroup-masFare\"", "</div>");
			                    	lastIndexRet = StringUtils.lastIndexOf(price2, "type=\"radio\"");
			                    	if(lastIndexRet == -1){
			                    		String price3 = StringUtils.substringBetween(end, "data-group=\"jsGroup-binterMasPoints\"", "</div>");
			                    		lastIndexRet = StringUtils.lastIndexOf(price3, "type=\"radio\"");
			                    		priceRet = price3;
			                    	}else{
			                    		priceRet = price2;
			                    	}
			                    }
			                    
			                    System.out.println("lastIndex===="+lastIndexRet);
			                    price = price.substring(lastIndexRet);
			                    String hourRet = StringUtils.substringBetween(priceRet, "data-hours=\"", "\"");
			                    String hourArrayRet[] = hourRet.split("\\;");
			                    System.out.println("hour"+hourRet);
			                    retflightno = new ArrayList<String>();
			                    
 			                    for(int j=0;j<countFlightRet;j++){
			                    	seg = new FlightSegement();
			                        String flightNo =   StringUtils.substringBetween(infoRet, "'>", "</span>");
			                        retflightno.add(flightNo);
			                        String orgin = StringUtils.substringBetween(infoRet, "data-origin='", "'");
			                        String des = StringUtils.substringBetween(infoRet, "data-destination='", "'");
			                        String date = StringUtils.substringBetween(infoRet, "data-date='", "'");
			                        
			                        System.out.println("flightNo============="+flightNo);
			                        seg.setFlightno(flightNo);
			                        String detailHour = hourArrayRet[j];
			                        seg.setDepDate(date);
			                        seg.setDepairport(orgin);
			                        seg.setArrairport(des);
			                        seg.setDeptime(detailHour.split("-")[0]);
			                        seg.setArrtime(detailHour.split("-")[1]);
			                        seg.setArrDate(date);
			                        
			                        retsegs.add(seg);
			                        infoRet = infoRet.replaceFirst("'>", "");
			                        infoRet = infoRet.replaceFirst("data-origin='", "");
			                        infoRet = infoRet.replaceFirst("data-destination='", "");
			                        infoRet = infoRet.replaceFirst("data-date='", "");
		                	    	
			                    }
			                    roundTripFlightInfo.setRetinfo(retsegs);
	                	    	roundTripFlightInfo.setRetflightno(retflightno);
	                	    	roundTripFlightInfo.setOutboundPrice(0);
	                	    	roundTripFlightInfo.setRetdepdate(dateArriDetailDate);
	                	    	roundTripFlightInfo.setReturnedPrice(0);
	                	    	flightRoundList.add(roundTripFlightInfo);
	                	    	 
	                	    	 
			                    end = end.replaceFirst("availability-cell-leftlastcolor-999\"><", "");
			                    end = end.replaceFirst("data-group=\"jsGroup-generic\"", "");
				                end = end.replaceFirst("data-group=\"jsGroup-masFare\"", "");
				                end = end.replaceFirst("data-group=\"jsGroup-binterMasPoints\"", "");
				                 
			                    System.out.println("======================================end");
			                    
			                    
		                    }
		                    
		                    start = start.replaceFirst("availability-cell-leftlastcolor-999\"><", "");
		                    start = start.replaceFirst("data-group=\"jsGroup-generic\"", "");
		                    start = start.replaceFirst("data-group=\"jsGroup-masFare\"", "");
		                    start = start.replaceFirst("data-group=\"jsGroup-binterMasPoints\"", "");
                	    	

					 }
					 
					 
					    result.setRet(true);
	                    result.setStatus(Constants.SUCCESS);
	                    result.setData(flightRoundList);
	                    System.out.println(JSON.toJSONString(result,SerializerFeature.DisableCircularReferenceDetect));
	                    
	            	    return result;
	                    
					
				 }else{
		             int countFlightInfo = StringUtils.countMatches(htmlCompress, "availability-cell-leftlast");
					 for(int i=0;i<countFlightInfo;i++){
		                	segs = new ArrayList<FlightSegement>();
		                	baseFlight = new OneWayFlightInfo();
		                	flightDetail = new FlightDetail();
		                	 FlightSegement seg = null;
		                	 List<String> flightNoList = null;
		                    String info=   StringUtils.substringBetween(htmlCompress, "availability-cell-leftlastcolor-999\"><", "</div>");
		                    String price = StringUtils.substringBetween(htmlCompress, "data-group=\"jsGroup-generic\"", "</div>");
		                    
		                    System.out.println("info===="+info);
		                    System.out.println("price===="+price);
		                    int countFlight = StringUtils.countMatches(info, "</span>");
		                    
		                    int lastIndex = StringUtils.lastIndexOf(price, "type=\"radio\"");
		                    //价格为空
		                    if(lastIndex==-1){
		                    	String price2 = StringUtils.substringBetween(htmlCompress, "data-group=\"jsGroup-masFare\"", "</div>");
		                    	lastIndex = StringUtils.lastIndexOf(price2, "type=\"radio\"");
		                    	if(lastIndex == -1){
		                    		String price3 = StringUtils.substringBetween(htmlCompress, "data-group=\"jsGroup-binterMasPoints\"", "</div>");
		                    		lastIndex = StringUtils.lastIndexOf(price3, "type=\"radio\"");
		                    		price = price3;
		                    	}else{
		                        	price = price2;
		                    	}
		                    }
		                    
		                    
		                    System.out.println("lastIndex===="+lastIndex);
		                    price = price.substring(lastIndex);
		                    String hour = StringUtils.substringBetween(price, "data-hours=\"", "\"");
		                    String hourArray[] = hour.split("\\;");
		                    
		                    flightNoList = new ArrayList<String>();
		                   
		                    for(int j=0;j<countFlight;j++){
		                    	 seg = new FlightSegement();
		                        String flightNo =   StringUtils.substringBetween(info, "'>", "</span>");
		                        flightNoList.add(flightNo);
		                        String orgin = StringUtils.substringBetween(info, "data-origin='", "'");
		                        String des = StringUtils.substringBetween(info, "data-destination='", "'");
		                        String date = StringUtils.substringBetween(info, "data-date='", "'");
		                        
		                        System.out.println("flightNo"+flightNo);
		                        seg.setFlightno(flightNo);
		                        String detailHour = hourArray[j];
		                        seg.setDepDate(date);
		                        seg.setDepairport(orgin);
		                        seg.setArrairport(des);
		                        seg.setDeptime(detailHour.split("-")[0]);
		                        seg.setArrtime(detailHour.split("-")[1]);
		                        seg.setArrDate(date);
		                        
		                        segs.add(seg);
		                        htmlCompress = htmlCompress.replaceFirst("'>", "");
		                        info = info.replaceFirst("'>", "");
		                        info = info.replaceFirst("data-origin='", "");
		                        info = info.replaceFirst("data-destination='", "");
		                        info = info.replaceFirst("data-date='", "");
		                    }
		                    
		                    String taxes = StringUtils.substringBetween(price, "data-taxes=\"", "\"");
		                    String fare = StringUtils.substringBetween(price, "data-fareimport=\"", "\"");
		                    
		                    System.out.println("fare========="+fare);
		                    System.out.println("taxes========"+taxes);
		                    
		                    flightDetail.setFlightno(flightNoList);
		                    flightDetail.setMonetaryunit(monetaryunit);
		                    flightDetail.setPrice(Double.parseDouble(fare));
		                    flightDetail.setTax(Double.parseDouble(taxes));
		                    flightDetail.setDepcity(arg1.getDep());
		                    flightDetail.setArrcity(arg1.getArr());
		                    flightDetail.setWrapperid(arg1.getWrapperid());
		                    flightDetail.setDepdate(dateDeptDetailDate);                    
		                    
		                    baseFlight.setDetail(flightDetail);
		                    baseFlight.setInfo(segs);
		                    flightList.add(baseFlight);
		                    
		                    htmlCompress = htmlCompress.replaceFirst("availability-cell-leftlastcolor-999\"><", "");
		                    htmlCompress = htmlCompress.replaceFirst("data-group=\"jsGroup-generic\"", "");
		                    htmlCompress = htmlCompress.replaceFirst("data-group=\"jsGroup-masFare\"", "");
		                    htmlCompress = htmlCompress.replaceFirst("data-group=\"jsGroup-binterMasPoints\"", "");
		                }
				 }
                
                result.setRet(true);
                result.setStatus(Constants.SUCCESS);
                result.setData(flightList);
                System.out.println(JSON.toJSONString(result,SerializerFeature.DisableCircularReferenceDetect));
                return result;
        }
}