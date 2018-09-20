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

public class Test {

	public static void main(String[] args) {
		// ClientID og clientSecret finner/genererer man på https://dev.netatmo.com/ (logg inn)
		String clientID = "5ab8c7278c04c49ba28b47c6";
		String clientSecret = "i5bcqcGNJlRkspnVlJXLWOKDQrOU8l";
		
		String email = "rtillung@hotmail.com";
		String pwd = "Nkenwood0!";
		
		String fromdate = "12/09/2018";
		String todate = "31/09/2018";
		
		ListData.list(clientID, clientSecret, email, pwd, fromdate, todate);
	}
}
