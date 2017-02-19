package com.example.parcelator_osm2;

import java.util.ArrayList;

import org.osmdroid.ResourceProxy;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.BoundingBoxE6;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.PathOverlay;


import android.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Mapa extends Activity {
	 
	 MapView myOpenMapView;
	 IMapController myMapController;
	 OnlineTileSourceBase MAPBOXSATELLITELABELLED;
	
     GeoPoint punto_fin;
     
     ArrayList latitudes= new ArrayList();
     ArrayList longitudes= new ArrayList();
    
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapa);
		
		MAPBOXSATELLITELABELLED = new XYTileSource("MapBoxSatelliteLabelled", ResourceProxy.string.mapquest_aerial, 1, 19, 256, ".png",
                "http://a.tiles.mapbox.com/v3/dennisl.map-6g3jtnzm/",
                "http://b.tiles.mapbox.com/v3/dennisl.map-6g3jtnzm/",
                "http://c.tiles.mapbox.com/v3/dennisl.map-6g3jtnzm/",
                "http://d.tiles.mapbox.com/v3/dennisl.map-6g3jtnzm/");
		TileSourceFactory.addTileSource(MAPBOXSATELLITELABELLED);
		
		myOpenMapView = (MapView)findViewById(R.id.openmapview);
        myOpenMapView.setBuiltInZoomControls(true);
        myOpenMapView.setMultiTouchControls(true);
       
        //niveles de zoom y centrado del mapa
       myMapController = myOpenMapView.getController();
        GeoPoint center = new GeoPoint(39.412325, -0.522741666667);       
        myMapController.setZoom(12);
        myMapController.setCenter(center);
        
        //asignacion del mapa OSM
        
        myOpenMapView.setTileSource(TileSourceFactory.MAPQUESTOSM);
        
        myOpenMapView.invalidate();
        
       
       
	
	}
	
	@Override
	public
	void onWindowFocusChanged(boolean hasFocus) {
	    dibuja();
	}
	
	public void dibuja()
	{
		Bundle bundle = getIntent().getExtras();
		latitudes=bundle.getStringArrayList("latitudes");
	    longitudes=bundle.getStringArrayList("longitudes");
	    
	    
	    PathOverlay myPath = new PathOverlay(Color.RED, this);
		for(int i=0; i<latitudes.size();i++)
		{
			String lati_n=String.valueOf(latitudes.get(i));
			String longi_n=String.valueOf(longitudes.get(i));
			GeoPoint punto = new GeoPoint(Double.parseDouble(lati_n),Double.parseDouble(longi_n));
			
		    myPath.addPoint(punto);
		}
		
		
		
		
		myPath.addPoint(punto_fin);
	     myOpenMapView.getOverlays().add(myPath);
	     
	     
	    double maxima_lat=Double.parseDouble(String.valueOf(latitudes.get(0))); 
	    double maxima_lon=Double.parseDouble(String.valueOf(longitudes.get(0)));
	    for (int i=0;i<latitudes.size();i++)
	    {
	    	double latitud_punto= Double.parseDouble(String.valueOf(latitudes.get(i)));
	    	double longitud_punto= Double.parseDouble(String.valueOf(longitudes.get(i)));
	    	
	    	if(latitud_punto> maxima_lat)
	    	{
	    		maxima_lat=latitud_punto;
	    	}
	    	
	    	if(longitud_punto> maxima_lon)
	    	{
	    		maxima_lon=longitud_punto;
	    	}
	    	
	    }
	    
	    double minima_lat=Double.parseDouble(String.valueOf(latitudes.get(0))); 
	    double minima_lon=Double.parseDouble(String.valueOf(longitudes.get(0)));
	    for (int i=0;i<latitudes.size();i++)
	    {
	    	double latitud_punto2= Double.parseDouble(String.valueOf(latitudes.get(i)));
	    	double longitud_punto2= Double.parseDouble(String.valueOf(longitudes.get(i)));
	    	
	    	if(latitud_punto2< minima_lat)
	    	{
	    		minima_lat=latitud_punto2;
	    	}
	    	
	    	if(longitud_punto2< minima_lon)
	    	{
	    		minima_lon=longitud_punto2;
	    	}
	    	
	    }
	     
	     GeoPoint geo1=new GeoPoint(maxima_lat,minima_lon);
	     GeoPoint geo2=new GeoPoint(minima_lat,maxima_lon);
	     
	     BoundingBoxE6 bb = new BoundingBoxE6(geo1.getLatitudeE6(),geo1.getLongitudeE6(),geo2.getLatitudeE6(),geo2.getLongitudeE6());
	     
	    
	     myOpenMapView.zoomToBoundingBox(bb);
	   
	}

}

	