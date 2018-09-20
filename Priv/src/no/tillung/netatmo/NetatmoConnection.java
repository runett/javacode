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
import no.tillung.utils.Log;
import no.tillung.utils.Utils;

public class NetatmoConnection {

	private NetatmoHttpClient client = null;
	private OAuthJSONAccessTokenResponse token = null;
	private Long expiresIn = null;
	//private String refreshToken = null;
	Long createdAt = null;
	
	Log log = null;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String email = Utils.getArg(args, "-email");
		String pwd = Utils.getArg(args, "-password");
		String clientID = Utils.getArg(args, "-clientID");
		String clientSecret = Utils.getArg(args, "-clientSecret");
		String fromDate = Utils.getArg(args, "-fromDate");
		String toDate = Utils.getArg(args, "-toDate");
		String intStr = Utils.getArg(args, "-interval");
		
		Long interval = null;
		if (intStr != null)
			interval = Long.parseLong(intStr) * 1000;
		
		NetatmoConnection conn = new NetatmoConnection(email, pwd, clientID, clientSecret);
		
		SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
		Date dateBegin = null;
		Date dateEnd = null;
		if ((fromDate != null) && (toDate != null))
		try {
			dateBegin = f.parse(fromDate);
			dateEnd = f.parse(toDate);
		} catch (ParseException e) {
			e.printStackTrace();
			return;
		}
		
		boolean quit = false;
		int fails = 0;
		do {
			try {
				conn.list(clientID, clientSecret, email, pwd, Params.SCALE_THIRTY_MINUTES, dateBegin, dateEnd);
			} catch (Exception e) {
				try {
					conn.refresh();
					conn.list(clientID, clientSecret, email, pwd, Params.SCALE_THIRTY_MINUTES, dateBegin, dateEnd);
				} catch (Exception e2) {
					conn.log.trace("refresh failed...");
					e2.printStackTrace();
					
					// Refresh does not work
					fails++;
				}
			}
			try {
				Thread.sleep(interval);
				//conn.refresh();
				//Thread.sleep(2000);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (fails > 10)
				quit = true;
		} while (!quit);
	}
	
	private void refresh() throws OAuthSystemException, OAuthProblemException {
		// TODO Auto-generated method stub
		OAuthJSONAccessTokenResponse t = client.refreshToken(token);
		System.out.println("refresh token:");
		log.trace("refreshing token");
		storeToken(t);
		
	}
	
	private void storeToken(OAuthJSONAccessTokenResponse t) {
		this.token = t;
		this.createdAt = System.currentTimeMillis();
		this.expiresIn = token.getExpiresIn();
		//this.refreshToken = token.getRefreshToken();
		System.out.println("  token:        " + token.toString());
		System.out.println("  refreshtoken: " + token.getRefreshToken());
		System.out.println("  expiresin:    " + token.getExpiresIn());
		System.out.println("  timetolive:   " + (this.createdAt + (this.expiresIn * 1000) - System.currentTimeMillis()));
		log.trace("store token:\n");
		log.trace("  token:        " + token.toString() + "\n");
		log.trace("  refreshtoken: " + token.getRefreshToken() + "\n");
		log.trace("  expiresin:    " + token.getExpiresIn() + "\n");
		log.trace("  timetolive:   " + (this.createdAt + (this.expiresIn * 1000) - System.currentTimeMillis()) + "\n");
	}

	private void login(String email, String password, String clientID, String clientSecret)
	{
		log.trace("login...");
		if ((email != null) && (password != null) && (clientID != null) && (clientSecret != null))
		{
			client = new NetatmoHttpClient(clientID, clientSecret);
			try {
				OAuthJSONAccessTokenResponse t = client.login(email, password);
				storeToken(t);
				expiresIn = token.getExpiresIn();
				//refreshToken = token.getRefreshToken();
			} catch (OAuthSystemException e) {
				e.printStackTrace();
			} catch (OAuthProblemException e) {
				e.printStackTrace();
			}
		}
		
	}

	public NetatmoConnection(String email, String password, String clientID, String clientSecret)
	{
		log = new Log("c:/temp/netatmo.txt");
		log.logDateTime(true);
		this.login(email, password, clientID, clientSecret);
	}
	
	/**
	 * List data from my station
	 * @param clientID
	 * @param clientSecret
	 * @param email
	 * @param pwd
	 * @param scale 
	 * @param dateBegin
	 * @param dateEnd
	 * @throws OAuthSystemException 
	 * @throws OAuthProblemException 
	 */
	
	
	public void list(String clientID, String clientSecret, String email, String pwd, String scale, Date dateBegin, Date dateEnd) throws OAuthSystemException, OAuthProblemException {
		
		//List<String> outdoorTypes = Arrays.asList(Params.TYPE_TEMPERATURE, Params.TYPE_MAX_TEMP, Params.TYPE_MIN_TEMP, Params.TYPE_HUMIDITY);
		//List<String> indoorTypes = Arrays.asList(Params.TYPE_TEMPERATURE, Params.TYPE_MAX_TEMP, Params.TYPE_MIN_TEMP, Params.TYPE_HUMIDITY, Params.TYPE_PRESSURE, Params.TYPE_CO2, Params.TYPE_NOISE);
		List<String> indoorTypes = Arrays.asList(Params.TYPE_TEMPERATURE, Params.TYPE_HUMIDITY, Params.TYPE_PRESSURE, Params.TYPE_CO2, Params.TYPE_NOISE);
		List<String> outdoorTypes = Arrays.asList(Params.TYPE_TEMPERATURE, Params.TYPE_HUMIDITY);
		
		if (!scale.equalsIgnoreCase(Params.SCALE_MAX))
		{
			indoorTypes = Arrays.asList(Params.TYPE_TEMPERATURE, Params.TYPE_MAX_TEMP, Params.TYPE_MIN_TEMP, Params.TYPE_HUMIDITY, Params.TYPE_PRESSURE, Params.TYPE_CO2, Params.TYPE_NOISE);
			outdoorTypes = Arrays.asList(Params.TYPE_TEMPERATURE, Params.TYPE_MAX_TEMP, Params.TYPE_MIN_TEMP, Params.TYPE_HUMIDITY);
		}
		List<String> types;
		List<Station> stations;
		
		try {
			stations = client.getStationsData(token, null, null);
			//stations = client.getDevicesList(token);
			
			for (Station station : stations)
			{
				//Station station = stations.get(0);
				//System.out.println("Station name: " + station.getName());
				log.trace("station name: " + station.getName());
				
				List<Module> modules = station.getModules();
				for (Module module : modules)
				{
					//System.out.println(module.getName() + " (" + module.getType() + ")");
					log.trace("module name: " + module.getName());
					
					if (module.getType().contentEquals(Module.TYPE_INDOOR))
						types = indoorTypes;
					else
						types = outdoorTypes;
						
					List<Measures> measures = client.getMeasures(token, station, module, types, scale, dateBegin, dateEnd, null, null);
					int count = 0;
					for (Measures measure : measures) {
						count++;
						
						long begintime = measure.getBeginTime();
						Date time = new Date(begintime);
						
						String str = "measure (" + count + ") " + time + " ";
						
						for (int c=0; c<types.size(); c++)
						{
							String type = types.get(c);
							str += "  " + type + ": ";
							if (type.equals(Params.TYPE_CO2))
								str += measure.getCO2();
							if (type.equals(Params.TYPE_HUMIDITY))
								str += measure.getHumidity();
							if (type.equals(Params.TYPE_MAX_TEMP))
								str += measure.getMaxTemp();
							if (type.equals(Params.TYPE_MIN_TEMP))
								str += measure.getMinTemp();
							if (type.equals(Params.TYPE_NOISE))
								str += measure.getNoise();
							if (type.equals(Params.TYPE_PRESSURE))
								str += measure.getPressure();
							if (type.equals(Params.TYPE_TEMPERATURE))
								str += measure.getTemperature();
						}
						
						//System.out.println(str);
						
						if (measures.size() == count)
						{
							// Write to file...
							log.trace(str + "\n");
						}
					}
				}
			}
		} catch (OAuthSystemException e) {
			e.printStackTrace();
			throw e;
		} catch (OAuthProblemException e) {
			e.printStackTrace();
			throw e;
		}
	}


}
