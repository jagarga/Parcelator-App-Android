package com.example.parcelator_osm2;

import java.util.ArrayList;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;
import org.osmdroid.util.BoundingBoxE6;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;




import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Parcelator_osm2 extends Activity {
	 
	 ArrayList coords_lat = new ArrayList();
	 ArrayList coords_lon = new ArrayList();	
	 EditText et1, et2;
	 TextView mi_area, mi_perimetro;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_parcelator_osm2);
		
		et1=(EditText)findViewById(R.id.editText1);
        et2=(EditText)findViewById(R.id.editText2);
        mi_area=(TextView)findViewById(R.id.textView3);
        mi_perimetro=(TextView)findViewById(R.id.textView4);
	}
	
	public void toma(View v)
	{
		ConfigGPS();
	}
	
	public void almacena(View v)
	{
		double lati= Double.parseDouble(et1.getText().toString());
		double longi= Double.parseDouble(et2.getText().toString());
		
				
		coords_lat.add(lati);
		
		coords_lon.add(longi);
		et1.setText("");
		et2.setText("");
		
	}
	
	public void calcula(View v)
	{
		
		ArrayList x_utm = new ArrayList();
		ArrayList y_utm = new ArrayList();
		
		for(int i=0; i<coords_lat.size();i++)
			
		{
			double utm[] = new double [2]; 
			double lat=Double.parseDouble(String.valueOf(coords_lat.get(i)));
			double lon=Double.parseDouble(String.valueOf(coords_lon.get(i)));
			utm= CoordinateConversion.latLon2UTM(lat, lon);
			
			double Xutm = utm [0];
			double Yutm = utm [1];
			   
		       x_utm.add(Xutm);
		       y_utm.add(Yutm);
		
		}

		double suma_pos=0;
		for(int i=0; i<(x_utm.size()-1); i++)
		{
			double x_punto= (Double.parseDouble(String.valueOf(x_utm.get(i))));
			double y_siguiente= Double.parseDouble(String.valueOf(y_utm.get(i+1)));
			double multiplicacion=x_punto*y_siguiente;
			suma_pos= suma_pos+multiplicacion;
		}
		double x_final=Double.parseDouble(String.valueOf(x_utm.get((x_utm.size()-1))));
		double y_principio=Double.parseDouble(String.valueOf(y_utm.get(0)));
		double multi_final=x_final*y_principio;
		suma_pos=suma_pos+multi_final;
		
		double suma_neg=0;
		for(int i=0; i<(y_utm.size()-1); i++)
		{
			double y_punto= (Double.parseDouble(String.valueOf(y_utm.get(i))));
			double x_siguiente= Double.parseDouble(String.valueOf(x_utm.get(i+1)));
			double multiplicacion2=y_punto*x_siguiente;
			suma_neg= suma_neg+multiplicacion2;
		}
		double y_final=Double.parseDouble(String.valueOf(y_utm.get((y_utm.size()-1))));
		double x_principio=Double.parseDouble(String.valueOf(x_utm.get(0)));
		double multi_final2=y_final*x_principio;
		suma_neg=suma_neg+multi_final2;
				
		double area=0.5*(Math.abs(suma_pos-suma_neg));
		
		
		double perimetro=0;
		for(int i=0; i<y_utm.size(); i++)
		{
			double incremento_x;
			double incremento_y;
			if (i==y_utm.size()-1)
			{
				incremento_x= Double.parseDouble(String.valueOf(x_utm.get(0)))- Double.parseDouble(String.valueOf(x_utm.get(x_utm.size()-1)));
				incremento_y= Double.parseDouble(String.valueOf(y_utm.get(0)))- Double.parseDouble(String.valueOf(y_utm.get(y_utm.size()-1)));
			}
			else
			{
				incremento_x= Double.parseDouble(String.valueOf(x_utm.get(i+1)))- Double.parseDouble(String.valueOf(x_utm.get(i)));
				incremento_y= Double.parseDouble(String.valueOf(y_utm.get(i+1)))- Double.parseDouble(String.valueOf(y_utm.get(i)));
			}
			
			double dist= Math.sqrt(((incremento_x*incremento_x)+(incremento_y*incremento_y)));	
			perimetro= perimetro+dist;
		}
		
		//Toast.makeText(Parcelator_osm2.this, "el area de la parcela es:" + "\n" + area + "   metros cuadrados" + "\n" + "el perimetro de la parcela es:" + "\n" + perimetro + "   metros", Toast.LENGTH_LONG).show();
		
		mi_area.setText("El area de la parcela es: "+ redondeo.redondea(area, 2) + " metros cuadrados.");
		mi_perimetro.setText("El perimetro de la parcela es: "+redondeo.redondea(perimetro, 2)+" metros.");
		
	}
	
	public void ver(View v)
	{
		double lati_punto = Double.parseDouble(String.valueOf(coords_lat.get(0)));
		double longi_punto = Double.parseDouble(String.valueOf(coords_lon.get(0)));
		//punto_fin=(Double.parseDouble(String.valueOf(mi_lati.get(0))),Double.parseDouble(String.valueOf(mi_longi.get(0);
		GeoPoint punto_fin = new GeoPoint(lati_punto, longi_punto);
		BoundingBoxE6 bb = new BoundingBoxE6(punto_fin.getLatitudeE6(), punto_fin.getLongitudeE6(), punto_fin.getLatitudeE6(), punto_fin.getLongitudeE6()); 
		Intent i = new Intent(this, Mapa.class);
		i.putStringArrayListExtra("latitudes", coords_lat);
		i.putStringArrayListExtra("longitudes", coords_lon);
		i.putExtra("mi_lati_punto", lati_punto);
		i.putExtra("mi_longi_punto", longi_punto);
		startActivity(i);
	}
	
	public void ConfigGPS()
	{
		LocationManager mLocationManager;
		LocationListener mLocationListener;
		
		mLocationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
		mLocationListener= new MyLocationListener();
		
		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 10, mLocationListener);
	
	}
 
	private class MyLocationListener implements LocationListener
	{

		@Override
		public void onLocationChanged(final Location pLoc) {
			
			double lati1=pLoc.getLatitude();
			double longi1=pLoc.getLongitude();
			et1.setText(String.valueOf(lati1));
			et2.setText(String.valueOf(longi1));
			
			
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			 Toast.makeText(Parcelator_osm2.this, "GPS desactivado", Toast.LENGTH_LONG).show();
    		    
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}
		
		

		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.parcelator_osm2, menu);
		return super.onCreateOptionsMenu(menu);
	    }
	
	@Override
	   public boolean onOptionsItemSelected(MenuItem item) {
	      Canvas canvas = null;
		switch (item.getItemId()) {
	      case R.id.instrucciones:
	    	  
	    
	    	  
	    	  
	         break;
	 
	      
	 //     case R.id.acerca_de:
	   // 	  Intent j = new Intent(this, Acerca_de.class);
		 //     startActivity(j);
	    //     break;
		}
		return true;
	}

}
