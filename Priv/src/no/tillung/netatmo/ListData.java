package no.tillung.netatmo;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;

import losty.netatmo.NetatmoHttpClient;
import losty.netatmo.model.Measures;
import losty.netatmo.model.Module;
import losty.netatmo.model.Params;
import losty.netatmo.model.Station;

public class ListData {

	/**
	 * ClientID og clientSecret finner/genererer man på https://dev.netatmo.com/ (logg inn)
	 * 
	 * @param clientID
	 * @param clientSecret
	 * @param email
	 * @param pwd
	 */
	public static void list(String clientID, String clientSecret, String email, String pwd) {
	
		NetatmoHttpClient client = new NetatmoHttpClient(clientID, clientSecret);
		OAuthJSONAccessTokenResponse token;
		try {
			token = client.login(email, pwd);
			
			// keep token...
			//token.
		} catch (OAuthSystemException e1) {
			e1.printStackTrace();
			return;
		} catch (OAuthProblemException e1) {
			e1.printStackTrace();
			return;
		}
	
		List<String> types = Arrays.asList(Params.TYPE_TEMPERATURE, Params.TYPE_PRESSURE, Params.TYPE_HUMIDITY);
		//Date dateBegin = DateTime.parse("2015-09-10T00Z").toDate();
		//Date dateEnd = DateTime.parse("2015-09-11T00Z").toDate();
	
		SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
		Date dateBegin;
		Date dateEnd;
		try {
			dateBegin = f.parse("01/08/2018");
			dateEnd = f.parse("31/08/2018");
		} catch (ParseException e) {
			e.printStackTrace();
			return;
		}
		
		List<Station> stations;
		try {
			stations = client.getDevicesList(token);
			
			for (Station station : stations)
			{
				//Station station = stations.get(0);
				System.out.println("Station name: " + station.getName());
				
				List<Module> modules = station.getModules();
				for (Module module : modules)
				{
					System.out.println(module.getName() + " (" + module.getType() + ")");
					
					List<Measures> measures = client.getMeasures(token, station, module, types, Params.SCALE_MAX, dateBegin, dateEnd, null, null);
					int count = 0;
					for (Measures measure : measures) {
						count++;
						//new DateTime(measure.getBeginTime()).withZone(DateTimeZone.UTC).toString();
						measure.getTemperature();
						measure.getPressure();
						measure.getHumidity();
						
						long begintime = measure.getBeginTime();
						double temp = measure.getTemperature();
						double pressure = measure.getPressure();
						double humidity = measure.getHumidity();
						
						Date time = new Date(begintime);
						
						System.out.println("measure (" + count + ") " + time + " temp: " + temp + " pressure: " + pressure + " humidity: " + humidity);
					}
				}
			}
		} catch (OAuthSystemException e) {
			e.printStackTrace();
		} catch (OAuthProblemException e) {
			e.printStackTrace();
		}
	}
}
