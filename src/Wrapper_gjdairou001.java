import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SimpleTimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
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
import com.travelco.rdf.infocenter.InfoCenter;


public class Wrapper_gjdairou001 implements QunarCrawler{

        public static void main(String[] args) {

                FlightSearchParam searchParam = new FlightSearchParam();
                searchParam.setDep("CDG");
                searchParam.setArr("SJJ");
                searchParam.setDepDate("2014-08-23");
                //searchParam.setRetDate("2014-08-29");
                searchParam.setTimeOut("60000");
                searchParam.setToken("");
                
                String html = new  Wrapper_gjdairou001().getHtml(searchParam);
        //String detail = new  Wrapper_gjdairou001().getHtml2(searchParam);
        //System.out.println(detail);
                //ProcessResultInfo result = new ProcessResultInfo();
        ProcessResultInfo   result = new  Wrapper_gjdairou001().process(html,searchParam);
                
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
    		String depDate=arg0.getDepDate().replaceAll("-","")+"0000";
    		Map<String, String> map = new LinkedHashMap<String, String>();
    		map.put("EMBEDDED_TRANSACTION", "FlexPricerAvailability");
    		map.put("TRIPFLOW", "YES");
    		map.put("DIRECT_LOGIN", "NO");
    		map.put("SITE", "BAXGBAXG");
    		map.put("PRICING_TYPE", "I");
    		
    		map.put("DATE_RANGE_QUALIFIER_1", "C");
    		map.put("DATE_RANGE_QUALIFIER_2", "C");
    		map.put("SO_SITE_SEND_CMD_EMAIL", "OSI");
    		map.put("LANGUAGE", "GB");
    		map.put("B_DATE_1", depDate);
    		
    		map.put("TRIP_TYPE", "O");
    		map.put("DATE_RANGE_VALUE_1", "0");
    		map.put("DATE_RANGE_VALUE_2", "0");
    		map.put("SO_SITE_ALLOW_PROMO", "True");
    		map.put("DISPLAY_TYPE", "1");
    		
    		 
    		map.put("COMMERCIAL_FARE_FAMILY_1", "EUEU");
    		map.put("B_LOCATION_1", arg0.getDep());
    		map.put("E_LOCATION_1", arg0.getArr());
    		map.put("TRAVELLER_TYPE_1", "ADT");
    		if(StringUtils.isNotEmpty(arg0.getRetDate())){
    			String retDate=arg0.getRetDate().replaceAll("-","")+"0000";
    			map.put("B_DATE_2",retDate);
    		}
    		bookingInfo.setInputs(map);		
    		bookingResult.setData(bookingInfo);
    		bookingResult.setRet(true);
    		return bookingResult;
        }

        public String getHtml(FlightSearchParam arg0) {
        	
        	String depDate=arg0.getDepDate().replaceAll("-","")+"0000";
    		
    		String getUrl = "";
    		if(StringUtils.isEmpty(arg0.getRetDate())){
       		   getUrl = String.format("http://booking.croatiaairlines.com/plnext/FPCcroatiaairlines/Override.action?EMBEDDED_TRANSACTION=FlexPricerAvailability&TRIPFLOW=YES&DIRECT_LOGIN=NO&SITE=BAXGBAXG&PRICING_TYPE=I&DATE_RANGE_QUALIFIER_1=C&DATE_RANGE_QUALIFIER_2=C&SO_SITE_SEND_CMD_EMAIL=OSI&LANGUAGE=GB&B_DATE_1=%s&TRIP_TYPE=O&DATE_RANGE_VALUE_1=0&DATE_RANGE_VALUE_2=0&SO_SITE_ALLOW_PROMO=True&DISPLAY_TYPE=1&COMMERCIAL_FARE_FAMILY_1=EUEU&B_LOCATION_1=%s&E_LOCATION_1=%s&TRAVELLER_TYPE_1=ADT",depDate ,arg0.getDep(),arg0.getArr());
    		}else{
    			String retDate=arg0.getRetDate().replaceAll("-","")+"0000";
        		getUrl = String.format("https://booking.croatiaairlines.com/plnext/FPCcroatiaairlines/Override.action?EMBEDDED_TRANSACTION=FlexPricerAvailability&TRIPFLOW=YES&DIRECT_LOGIN=NO&SITE=BAXGBAXG&PRICING_TYPE=I&DATE_RANGE_QUALIFIER_1=C&DATE_RANGE_QUALIFIER_2=C&SO_SITE_SEND_CMD_EMAIL=OSI&LANGUAGE=GB&B_DATE_1=%s&TRIP_TYPE=R&DATE_RANGE_VALUE_1=0&DATE_RANGE_VALUE_2=0&SO_SITE_ALLOW_PROMO=True&DISPLAY_TYPE=1&COMMERCIAL_FARE_FAMILY_1=EUEU&B_LOCATION_1=%s&E_LOCATION_1=%s&B_DATE_2=%s&TRAVELLER_TYPE_1=ADT",depDate ,arg0.getDep(),arg0.getArr(),retDate);
    		}
            System.out.println("getUrl"+getUrl);;
                QFGetMethod get = null;
                try
                {
                QFHttpClient httpClient = new QFHttpClient(arg0, false);

                get = new QFGetMethod(getUrl);
                int status = httpClient.executeMethod(get);
                System.out.println(status);
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
                System.out.println("dateDeptDetailDate"+dateDeptDetailDate);
                if(StringUtils.isNotEmpty(arg1.getRetDate())){
                	System.out.println(arg0);
                	String jsonStr = org.apache.commons.lang.StringUtils.substringBetween(arg0, "var generatedJSon = new String('", "');");		
            		JSONObject ajson = JSON.parseObject(jsonStr);
            		String list = ajson.getString("listRecos");
            		String recosIDs = ajson.getString("recosIDs");
            		JSONObject data = JSON.parseObject(list);
            	    Map<String,Double> priceMap = new HashMap<String,Double>();
            	    String[] fightArray = recosIDs.split("\\|");
            	    for(int i=0;i<fightArray.length;i++){
            	    	String json = data.getString(""+i);
            	    	json = "["+json+"]";
            			JSONArray json1 = JSONArray.parseArray(json);
            			JSONObject ojson = json1.getJSONObject(0);
            			String price = ojson.getString("price");
            			int end = price.indexOf(" HRK");
            			double doublePrice = Double.parseDouble(price.substring(10,end).replace(",",""));
            			String out = ojson.getString("outboundFlightIDs");
            			String in = ojson.getString("inboundFlightIDs");
            			String[] fightOutDetail = out.split("\\|");
            			String[] fightInDetail = in.split("\\|");
            			for(int j=1;j<fightOutDetail.length;j++){
            				for(int t=1;t<fightInDetail.length;t++){
            					String fightNoOut = fightOutDetail[j];
            					String fightNoIn = fightInDetail[t];
            					String fightNo = fightNoOut+"|"+fightNoIn;
                				if(priceMap.get(fightNo)==null){
                					priceMap.put(fightNo, doublePrice);
                				}else if(priceMap.get(fightNo)!=null){
                					double temp = priceMap.get(fightNo);
                					if(doublePrice < temp){
                						priceMap.remove(fightNo);
                    					priceMap.put(fightNo, doublePrice);
                					}
                				}
            				}
            			}
            	    }
            	    
            	    htmlCompress  = htmlCompress.replaceAll("\\s+", "");
            	    System.out.println("htmlCompress"+htmlCompress);
            	    String count = StringUtils.substringBetween(htmlCompress, "<liclass=\"line\">&nbsp;</li><li>", "<spanclass=\"nowrap\">stop(s)");
            	    //System.out.println("count"+count);
            	    String sessionId=   StringUtils.substringBetween(arg0, ";jsessionid=", "\"");
            	    Iterator<String> iter = priceMap.keySet().iterator();
            	    
            	    List<RoundTripFlightInfo> flightList = new ArrayList<RoundTripFlightInfo>();
                    RoundTripFlightInfo roundTripFlightInfo = null;
                    List<FlightSegement> segs =  null;
        	    	List<FlightSegement> segsRet = null;
        	    	 try {           
        	    	 
            	    while (iter.hasNext()) {
            	    	
            	    	roundTripFlightInfo = new RoundTripFlightInfo();
            	    	String key  = iter.next();
            	    	String[] id = key.split("\\|");
            	    	String html2 = getHtml2(arg1,""+id[0],sessionId);
            	    	String html2NotCompress = html2;
                        html2 = html2.replaceAll("\\s+", "");
                        html2 = html2.replaceAll("\\s+", "");
                        String html3 = getHtml3(arg1,""+id[1],sessionId);
                        String html3NotCompress = html3;
                        html3 = html3.replaceAll("\\s+", "");
                        html3 = html3.replaceAll("\\s+", "");
            	    	int leftCount = StringUtils.countMatches(html2, "<tdclass=\"textBoldflight\">Flight");
            	    	int rightCount = StringUtils.countMatches(html3, "<tdclass=\"textBoldflight\">Flight");
            	    	segs = new ArrayList<FlightSegement>();
            	    	segsRet = new ArrayList<FlightSegement>();
                        FlightDetail flightDetail = new FlightDetail();
                        FlightSegement seg = null;
                        List<String> flightNoList = new ArrayList<String>();
                        List<String> retflightno = new ArrayList<String>();
                        Double priceInfo = priceMap.get(key);
            	    	 for (int t = 0; t < leftCount; t++){
            	    		 seg = new FlightSegement();
                         	//System.out.println("html2"+html2);
                             String date = StringUtils.substringBetween(html2NotCompress, "<td width=\"83%\" class=\"textBold\" colspan=\"2\">", "</td>");
                             String dateDept = StringUtils.substringBetween(html2NotCompress, "<td width=\"83%\" class=\"textBold\" colspan=\"2\">", "</td>");
                             String depTime = StringUtils.substringBetween(html2, "<spanclass=\"nowrap\">Departure:</span></td><tdclass=\"nowrap\">", "</td>");
                             String arrTime = StringUtils.substringBetween(html2, "<spanclass=\"nowrap\">Arrival:</span></td><tdclass=\"nowrap\">", "</td>");

                             String depairport = StringUtils.substringBetween(html2, "id=\"departureCode_0_"+t+"\"value=\"", "\"");
                             String arrairport = StringUtils.substringBetween(html2, "id=\"arrivalCode_0_"+t+"\"value=\"", "\"");

                             String planerno =  StringUtils.substringBetween(html2, "<tdid=\"segAirline_0_"+t+"\"width=\"35%\">", "<spanclass=\"legendText\">e</span>"); 
                             html2 = html2.replaceFirst("<td width=\"83%\"class=\"textBold\"colspan=\"2\">", "");
                             html2 = html2.replaceFirst("<spanclass=\"nowrap\">Departure:</span></td><tdclass=\"nowrap\">", "");
                             html2 = html2.replaceFirst("<spanclass=\"nowrap\">Arrival:</span></td><tdclass=\"nowrap\">", "");
                             html2 = html2.replaceFirst("</td><tdwidth=\"90%\">", "");
                             html2 = html2.replaceFirst("segAirline_0_", "");
                             html2 = html2.replaceFirst("id=\"departureCode_0_"+t+"\"value=\"", "");
                             html2 = html2.replaceFirst("id=\"arrivalCode_0_"+t+"\"value=\"", "");
                             html2NotCompress = html2NotCompress.replaceFirst("<td width=\"83%\" class=\"textBold\" colspan=\"2\">", "");
                             String plannerno = planerno.substring(planerno.indexOf("&nbsp")+6);
                             /*System.out.println("depTime==="+depTime);
                             System.out.println("arrTime==="+arrTime);
                             System.out.println("depairport==="+depairport.substring(0, depairport.indexOf(",")));
                             System.out.println("arrairport==="+arrairport.substring(0, arrairport.indexOf(",")));
                             System.out.println("planerno====="+plannerno);*/
                             
                             String dateStr = "";
                             DateFormat df3 = DateFormat.getDateInstance(DateFormat.FULL, Locale.ENGLISH);
                             DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                             Date date1 = df3.parse(StringUtils.trimToNull(date));  
                             dateStr = format.format(date1);
                                 
                             flightNoList.add(plannerno);
                             seg.setFlightno(plannerno);
                             seg.setDepDate(dateStr);
                             seg.setDepairport(depairport);
                             seg.setArrairport(arrairport);
                             seg.setDeptime(depTime);
                             seg.setArrtime(arrTime);
                             seg.setArrDate(dateStr);
                             segs.add(seg);
                         }
            	    	 
            	    	 flightDetail.setFlightno(flightNoList);
                         flightDetail.setMonetaryunit(monetaryunit);
                         flightDetail.setPrice(priceInfo);
                         flightDetail.setDepcity(arg1.getDep());
                         flightDetail.setArrcity(arg1.getArr());
                         flightDetail.setWrapperid(arg1.getWrapperid());
                         flightDetail.setDepdate(dateDeptDetailDate);
                         roundTripFlightInfo.setDetail(flightDetail);
                         roundTripFlightInfo.setInfo(segs);

            	    	 for (int t = 0; t < rightCount; t++){
            	    		 seg = new FlightSegement();
                          	//System.out.println("html2"+html2);
                             String date = StringUtils.substringBetween(html3NotCompress, "<td width=\"83%\" class=\"textBold\" colspan=\"2\">", "</td>");
                              String depTime = StringUtils.substringBetween(html3, "<spanclass=\"nowrap\">Departure:</span></td><tdclass=\"nowrap\">", "</td>");
                              String arrTime = StringUtils.substringBetween(html3, "<spanclass=\"nowrap\">Arrival:</span></td><tdclass=\"nowrap\">", "</td>");
                               
                              String depairport = StringUtils.substringBetween(html3, "id=\"departureCode_0_"+t+"\"value=\"", "\"");
                              String arrairport = StringUtils.substringBetween(html3, "id=\"arrivalCode_0_"+t+"\"value=\"", "\"");
                              
                              String planerno =  StringUtils.substringBetween(html3, "<tdid=\"segAirline_0_"+t+"\"width=\"35%\">", "<spanclass=\"legendText\">e</span>"); 
                              arrairport = arrairport.substring(arrairport.indexOf("<td>")+4);
                              html3 = html3.replaceFirst("<td width=\"83%\"class=\"textBold\"colspan=\"2\">", "");
                              html3 = html3.replaceFirst("<spanclass=\"nowrap\">Departure:</span></td><tdclass=\"nowrap\">", "");
                              html3 = html3.replaceFirst("<spanclass=\"nowrap\">Arrival:</span></td><tdclass=\"nowrap\">", "");
                              html3 = html3.replaceFirst("</td><tdwidth=\"90%\">", "");
                              html3 = html3.replaceFirst("segAirline_0_", "");
                              
                              html3 = html3.replaceFirst("id=\"departureCode_0_"+t+"\"value=\"", "");
                              html3 = html3.replaceFirst("id=\"arrivalCode_0_"+t+"\"value=\"", "");
                              
                              
                              html3NotCompress = html3NotCompress.replaceFirst("<td width=\"83%\" class=\"textBold\" colspan=\"2\">", "");
                              String plannerno = planerno.substring(planerno.indexOf("&nbsp")+6);
                              /*System.out.println("depTime==="+depTime);
                              System.out.println("arrTime==="+arrTime);
                              System.out.println("depairport==="+depairport.substring(0, depairport.indexOf(",")));
                              System.out.println("arrairport==="+arrairport.substring(0, arrairport.indexOf(",")));
                              System.out.println("planerno====="+plannerno);*/
                              String dateStr = "";
                              DateFormat df3 = DateFormat.getDateInstance(DateFormat.FULL, Locale.ENGLISH);
                              DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                              Date date1 = df3.parse(StringUtils.trimToNull(date));  
                              dateStr = format.format(date1);
                              retflightno.add(plannerno);
                              seg.setFlightno(plannerno);
                              seg.setDepDate(dateStr);
                              seg.setDepairport(depairport);
                              seg.setArrairport(arrairport);
                              seg.setDeptime(depTime);
                              seg.setArrtime(arrTime);
                              seg.setArrDate(dateStr);
                              segsRet.add(seg);
                          }
            	    	 roundTripFlightInfo.setRetinfo(segsRet);
            	    	 roundTripFlightInfo.setRetflightno(retflightno);
            	    	 roundTripFlightInfo.setOutboundPrice(0);
            	    	 roundTripFlightInfo.setRetdepdate(dateArriDetailDate);
            	    	 roundTripFlightInfo.setReturnedPrice(0);
            	    	 flightList.add(roundTripFlightInfo);
            	    }
            	    result.setRet(true);
                    result.setStatus(Constants.SUCCESS);
                    result.setData(flightList);
                    System.out.println(com.alibaba.fastjson.JSON.toJSONString(result));
                    
            	    return result;
        	       }catch(Exception e){
                          e.printStackTrace();
                          result.setRet(false);
                          result.setStatus(Constants.PARSING_FAIL);
                          return result;
                   }
                }else{
                	int flightCount = StringUtils.countMatches(htmlCompress,"value=\"radOut\"");
                    String sessionId=   StringUtils.substringBetween(arg0, ";jsessionid=", "\"");		
                    int familyCount =  StringUtils.countMatches(htmlCompress,"name=\"FamilyButton\"");
                    String jsonStr = org.apache.commons.lang.StringUtils.substringBetween(arg0, "var generatedJSon = new String('", "');");		
            		JSONObject ajson = JSON.parseObject(jsonStr);
            		String list = ajson.getString("listRecos");
            		JSONObject data = JSON.parseObject(list);
            	    Map<String,Double> priceMap = new HashMap<String,Double>();
            		for(int i=0;i<familyCount;i++){
            			String json = data.getString(""+i);
            			json = "["+json+"]";
            			JSONArray json1 = JSONArray.parseArray(json);
            			JSONObject ojson = json1.getJSONObject(0);
            			String price = ojson.getString("price");
            			String fight = ojson.getString("outboundFlightIDs");
            			int end = price.indexOf(" HRK");
            			double doublePrice = Double.parseDouble(price.substring(10,end).replace(",",""));
            			String[] fightArray = fight.split("\\|");
            			for(int j=1;j<fightArray.length;j++){
            				String fightNo = fightArray[j];
            				if(priceMap.get(fightNo)==null){
            					priceMap.put(fightNo, doublePrice);
            				}else if(priceMap.get(fightNo)!=null){
            					double temp = priceMap.get(fightNo);
            					if(doublePrice < temp){
            						priceMap.remove(fightNo);
                					priceMap.put(fightNo, doublePrice);
            					}
            				}
            			}
            		}
                    
    		        try {                   
                            List<OneWayFlightInfo> flightList = new ArrayList<OneWayFlightInfo>();
            		        OneWayFlightInfo baseFlight = null;

                            for(int i=0;i<flightCount;i++){
                            	//System.out.println(htmlCompress);
                            	String startTime = StringUtils.substringBetween(htmlCompress, "<tdclass=\"textBold\"width=\"20%\">", "</td>");
                                String endTime = StringUtils.substringBetween(htmlCompress, "<tdclass=\"textBold\">", "</td>");
                		        //String org = StringUtils.substringBetween(htmlCompress, "<spanclass=\"nameHighlight\">", "</span>");;
                		        //String dst = StringUtils.substringBetween(htmlCompress, "<br/><spanclass=\"nameHighlight\">", "</span>");
                		        String time = StringUtils.substringBetween(htmlCompress, "<tdclass=\"textBold\">", "</td>");
                		        String count = StringUtils.substringBetween(htmlCompress, "<liclass=\"line\">&nbsp;</li><li>", "<spanclass=\"nowrap\">stop(s)");

                		        baseFlight = new OneWayFlightInfo();
                		        FlightDetail flightDetail = new FlightDetail();
                		        List<String> flightNoList = new ArrayList<String>();
                		        List<FlightSegement> segs = new ArrayList<FlightSegement>();
                		        FlightSegement seg = null;
                		        
                                
                                htmlCompress = htmlCompress.replaceFirst("<tdclass=\"textBold\"width=\"20%\">", "");
                                htmlCompress = htmlCompress.replaceFirst("<tdclass=\"textBold\">", "");
                                htmlCompress = htmlCompress.replaceFirst("<liclass=\"line\">&nbsp;</li><li>", "");
                                htmlCompress = htmlCompress.replaceFirst("<liclass=\"line\">&nbsp;</li><li>", "");
                                String html2 = getHtml2(arg1,""+i,sessionId);
                                String html3 = html2;
                                html2 = html2.replaceAll("\\s+", "");
                                int countFlight = StringUtils.countMatches(html2, "<tdclass=\"textBoldflight\">Flight");
                                for (int t = 0; t < countFlight; t++){
                                	seg = new FlightSegement();
                                	System.out.println("html2"+html2);
                                    String datedept = StringUtils.substringBetween(html3, "<td width=\"83%\" class=\"textBold\" colspan=\"2\">", "</td>");
                                    String depTime = StringUtils.substringBetween(html2, "<spanclass=\"nowrap\">Departure:</span></td><tdclass=\"nowrap\">", "</td>");
                                    String arrTime = StringUtils.substringBetween(html2, "<spanclass=\"nowrap\">Arrival:</span></td><tdclass=\"nowrap\">", "</td>");
                                    
                                    String depairport = StringUtils.substringBetween(html2, "id=\"departureCode_0_"+t+"\"value=\"", "\"");
                                    String arrairport = StringUtils.substringBetween(html2, "id=\"arrivalCode_0_"+t+"\"value=\"", "\"");
                                    
                                    //String depairport = StringUtils.substringBetween(html2, "</td><tdwidth=\"90%\">", "</td>");
                                    //tring arrairport = StringUtils.substringBetween(html2, "<spanclass=\"nowrap\">Arrival:</span></td><tdclass=\"nowrap\">", "</td></tr></table></td></tr><tr>");
                                    String planerno =  StringUtils.substringBetween(html2, "<tdid=\"segAirline_0_"+t+"\"width=\"35%\">", "<spanclass=\"legendText\">e</span>"); 
                                    //arrairport = arrairport.substring(arrairport.indexOf("<td>")+4);
                                    html2 = html2.replaceFirst("<td width=\"83%\"class=\"textBold\"colspan=\"2\">", "");
                                    html2 = html2.replaceFirst("<spanclass=\"nowrap\">Departure:</span></td><tdclass=\"nowrap\">", "");
                                    html2 = html2.replaceFirst("<spanclass=\"nowrap\">Arrival:</span></td><tdclass=\"nowrap\">", "");
                                    html2 = html2.replaceFirst("</td><tdwidth=\"90%\">", "");
                                    html2 = html2.replaceFirst("segAirline_0_", "");
                                    html2 = html2.replaceFirst("id=\"departureCode_0_"+t+"\"value=\"", "");
                                    html2 = html2.replaceFirst("id=\"arrivalCode_0_"+t+"\"value=\"", "");
                                    html3 = html3.replaceFirst("<td width=\"83%\" class=\"textBold\" colspan=\"2\">", "");
                                    String plannerno = planerno.substring(planerno.indexOf("&nbsp")+6);
                                    System.out.println("depTime==="+depTime);
                                    System.out.println("arrTime==="+arrTime);
                                    System.out.println("depairport==="+depairport);
                                    System.out.println("arrairport==="+arrairport);
                                    System.out.println("planerno====="+plannerno);
                                    
                                    String dateStr = "";
                                    DateFormat df3 = DateFormat.getDateInstance(DateFormat.FULL, Locale.ENGLISH);
                                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                    Date date1 = df3.parse(StringUtils.trimToNull(datedept));  
                                    dateStr = format.format(date1);
                                    System.out.println("dateStr====="+dateStr);
                                    seg.setFlightno(plannerno);
                                    seg.setDepDate(dateStr);
                                    seg.setDepairport(depairport);
                                    seg.setArrairport(depairport);
                                    seg.setDeptime(depTime);
                                    seg.setArrtime(arrTime);
                                    seg.setArrDate(dateStr);
                                    flightNoList.add(plannerno);
                                    segs.add(seg);
                                }
                                flightDetail.setFlightno(flightNoList);
                                flightDetail.setMonetaryunit(monetaryunit);
                                flightDetail.setPrice(Math.round(priceMap.get(String.valueOf(i))));
                                flightDetail.setDepcity(arg1.getDep());
                                flightDetail.setArrcity(arg1.getArr());
                                flightDetail.setWrapperid(arg1.getWrapperid());
                                flightDetail.setDepdate(dateDeptDetailDate);
                                baseFlight.setDetail(flightDetail);
                                baseFlight.setInfo(segs);
                                flightList.add(baseFlight);
                            }
                            System.out.println(flightList.toString());
                            result.setRet(true);
                            result.setStatus(Constants.SUCCESS);
                            result.setData(flightList);
                            return result;
    		        } catch(Exception e){
                    	e.printStackTrace();
                            result.setRet(false);
                            result.setStatus(Constants.PARSING_FAIL);
                            return result;
                    }
                }

        }
        
         public ProcessResultInfo process1(String arg0, String html2 ,FlightSearchParam arg1) {
                 
        
                String html = arg0;
                ProcessResultInfo result = new ProcessResultInfo();
                if ("Exception".equals(html)) {
                        result.setRet(false);
                        result.setStatus(Constants.CONNECTION_FAIL);
                        return result;                  
                }
                //需要有明显的提示语句，才能判断是否INVALID_DATE|INVALID_AIRLINE|NO_RESULT
                if (html.contains("Today Flight is full, select an other day or check later for any seat released. ")) {
                        result.setRet(false);
                        result.setStatus(Constants.INVALID_DATE);
                        return result;                  
                }
         String html3= html2;
        String aaa  = html.replaceAll("\\s+", "");
        html2 = html2.replaceAll("\\s+", "");
        //System.out.println("===============html"+html);
        System.out.println("===============html"+aaa);
        System.out.println("=============html2"+html2);
                String price = org.apache.commons.lang.StringUtils.substringBetween(html, "checked=\"checked\"  />", "</td>");
                
                
                String startTime = StringUtils.substringBetween(html, "<td class=\"textBold\" width=\"20%\">", "</td>");
                String endTime = StringUtils.substringBetween(html, "<td class=\"textBold\">", "</td>");
        String org = StringUtils.substringBetween(html, "<span class=\"nameHighlight\">", "</span>");;
        String dst = StringUtils.substringBetween(aaa, "<br/><spanclass=\"nameHighlight\">", "</span>");;
        String time = StringUtils.substringBetween(html, "<td class=\"textBold\">", "</td>");;
        String count = StringUtils.substringBetween(aaa, "<liclass=\"line\">&nbsp;</li><li>", "<spanclass=\"nowrap\">stop(s)");;
        System.out.println("startTime=="+StringUtils.trimToNull(startTime));
        System.out.println("endTime=="+StringUtils.trimToNull(endTime));
        System.out.println("org=="+StringUtils.trimToNull(org));
        System.out.println("dst=="+StringUtils.trimToNull(dst));
        System.out.println("time=="+StringUtils.trimToNull(time));
        System.out.println("count=="+StringUtils.trimToNull(count));
        price = StringUtils.trimToNull(price);
        Double doublePrice = Double.parseDouble(price.substring(0,price.indexOf(" ")+1).replace(",",""));
        String Monetaryunit = price.substring(price.indexOf(" ")+1);
        
        System.out.println(doublePrice);
        System.out.println(Monetaryunit);
        
        try {                   
                        List<OneWayFlightInfo> flightList = new ArrayList<OneWayFlightInfo>();
                        for (int i = 0; i < Integer.parseInt(count)+1; i++){
                                
                                System.out.println("=============");
                                OneWayFlightInfo baseFlight = new OneWayFlightInfo();
                                List<FlightSegement> segs = new ArrayList<FlightSegement>();
                                FlightDetail flightDetail = new FlightDetail();
                                FlightSegement seg = new FlightSegement();
                                List<String> flightNoList = new ArrayList<String>();
                                //JSONObject ojson = ajson.getJSONObject(i);
                                //String flightNo = ojson.getString("flight").replaceAll("[^a-zA-Z\\d]", "");
                                //flightNoList.add(flightNo);
                                String date = StringUtils.substringBetween(html3, "<td width=\"83%\" class=\"textBold\" colspan=\"2\">", "</td>");
                                String depTime = StringUtils.substringBetween(html2, "<spanclass=\"nowrap\">Departure:</span></td><tdclass=\"nowrap\">", "</td>");
                                String arrTime = StringUtils.substringBetween(html2, "<spanclass=\"nowrap\">Arrival:</span></td><tdclass=\"nowrap\">", "</td>");
                                
                                
                                String depairport = StringUtils.substringBetween(html2, "</td><tdwidth=\"90%\">", "</td>");
                                String arrairport = StringUtils.substringBetween(html2, "<spanclass=\"nowrap\">Arrival:</span></td><tdclass=\"nowrap\">", "</td></tr></table></td></tr><tr>");
                                String planerno =  StringUtils.substringBetween(html2, "<tdid=\"segAirline_0_"+i+"\"width=\"35%\">", "<spanclass=\"legendText\">e</span>"); 
                                
                                arrairport = arrairport.substring(arrairport.indexOf("<td>")+4);
                                html2 = html2.replaceFirst("<td width=\"83%\"class=\"textBold\"colspan=\"2\">", "");
                                html2 = html2.replaceFirst("<spanclass=\"nowrap\">Departure:</span></td><tdclass=\"nowrap\">", "");
                                html2 = html2.replaceFirst("<spanclass=\"nowrap\">Arrival:</span></td><tdclass=\"nowrap\">", "");
                                html2 = html2.replaceFirst("</td><tdwidth=\"90%\">", "");
                                
                                DateFormat df3 = DateFormat.getDateInstance(DateFormat.FULL, Locale.ENGLISH);
                                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                String dateStr = "";
                                  
                                try {  
                                        Date date1 = df3.parse(StringUtils.trimToNull(date));  
                                    dateStr = format.format(date1);
                                    
                                } catch (Exception e) {  
                                    System.out.println(e.getMessage());  
                                }  
                                String plannerno = planerno.substring(planerno.indexOf("&nbsp")+6);
                                System.out.println("dateStr==="+dateStr);
                                System.out.println("depTime==="+depTime);
                                System.out.println("arrTime==="+arrTime);
                                System.out.println("depairport==="+depairport);
                                System.out.println("arrairport==="+arrairport);
                                System.out.println("planerno====="+plannerno);
                                seg.setFlightno(plannerno);
                                seg.setDepDate(dateStr);
                                seg.setDepairport(depairport);
                                seg.setArrairport(arrairport);
                                seg.setDeptime(depTime);
                                seg.setArrtime(arrTime);
                                
                                flightDetail.setFlightno(flightNoList);
                                flightDetail.setMonetaryunit(Monetaryunit);
                                flightDetail.setPrice(doublePrice);
                                flightDetail.setDepcity(arg1.getDep());
                                flightDetail.setArrcity(arg1.getArr());
                                flightDetail.setWrapperid(arg1.getWrapperid());
                                segs.add(seg);
                                baseFlight.setDetail(flightDetail);
                                baseFlight.setInfo(segs);
                                flightList.add(baseFlight);
                        }
                        result.setRet(true);
                        result.setStatus(Constants.SUCCESS);
                        result.setData(flightList);
                        return result;
                } catch(Exception e){
                        result.setRet(false);
                        result.setStatus(Constants.PARSING_FAIL);
                        return result;
                }
        
       
        
        
        }

        
}