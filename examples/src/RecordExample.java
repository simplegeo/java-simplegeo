import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import com.simplegeo.client.model.DefaultRecord;
import com.simplegeo.client.model.GeoJSONObject;
import com.simplegeo.client.model.GeoJSONRecord;
import com.simplegeo.client.model.IRecord;
import com.simplegeo.client.model.RecordType;
import com.simplegeo.client.service.LocationService;
import com.simplegeo.client.service.exceptions.ValidLayerException;
import com.simplegeo.client.service.query.LatLonNearbyQuery;


public class RecordExample {

	private DefaultRecord record = null;
	private GeoJSONRecord geoJSON = null;
	
	// Change these values to your account
	// access and secret keys.
	public static String key = "my-key";
	public static String secret = "my-secret";

	// Change this to a valid layer that you own
	// and have created from the http://simplegeo.com
	private String layer = "com.layer.my";
	
	private final double lat = 39.91;
	private final double lon = -105.12;
	
	public RecordExample() {

		// We can either create a GeoJSON object or use a DefaultRecord
		DefaultRecord record = new DefaultRecord("r-12345", layer, RecordType.AUDIO.toString());
		record.setLatitude(lat);
		record.setLongitude(lon);
		this.record = record;
		
		// Using a GeoJSONRecord gives us the behavior of a GeoJSONObject but 
		// conforms to the IRecord interface
		GeoJSONRecord geoJSON = new GeoJSONRecord("r-23456", layer, RecordType.VIDEO.toString());
		geoJSON.setLatitude(lat);
		geoJSON.setLongitude(lon);
		this.geoJSON = geoJSON;
		
		
	}
	
	public void sendRecords() {
	
		List<IRecord> records = new ArrayList<IRecord>();
		records.add(record);
		records.add(geoJSON);
		
		try {
			
			LocationService.getInstance().update(records);
			
		} catch(ClientProtocolException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public GeoJSONObject getRecords() {
		
		GeoJSONObject geoJSON = null;
		try {
			LatLonNearbyQuery query  = new LatLonNearbyQuery(lat, lon, 10.0, this.layer, null, 2, null);
			geoJSON = (GeoJSONObject)LocationService.getInstance().nearby(query);
		} catch(ClientProtocolException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		} catch (ValidLayerException e) {
			e.printStackTrace();
		}
		
		return geoJSON;
	}
	
	public static void main(String[] args) throws Exception {
		
		if(key.equals("my-key") || secret.equals("my-secret"))
			throw new Exception("Please provide a valid key/secret pair");
		
		// Grab the shared instance
		LocationService locationService = LocationService.getInstance();
		
		// Set the proper key/secret pair for signing OAuth requests
		locationService.getHttpClient().setToken(key, secret);
		
		RecordExample sendRecord = new RecordExample();
		sendRecord.sendRecords();
		
		// Allow a 5 second propgation time
		Thread.sleep(5000);
	
		GeoJSONObject geoJSON = sendRecord.getRecords();
		System.out.println(String.format("Here is my record: %s", geoJSON.toString()));
	}

}
