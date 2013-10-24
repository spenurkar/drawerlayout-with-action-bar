package com.demogooglemaps.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class MainActivity extends Activity implements OnMapLongClickListener, OnMapClickListener,
														OnMarkerClickListener{

	static final LatLng HAMBURG = new LatLng(53.588, 9.927);
	static final LatLng KIEL = new LatLng(53.511, 9.993);
	private GoogleMap gMap;
	private static int count = 1;
	private boolean markerClicked = false;
	Polyline polyline;
	PolylineOptions polyLineOptions; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		gMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();

		Marker hamburg = gMap.addMarker(new MarkerOptions().position(HAMBURG)
				.title("Hamburg"));
		Marker kiel = gMap.addMarker(new MarkerOptions()
				.position(KIEL)
				.title("Kiel")
				.snippet("Kiel is cool")
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.ic_launcher)));

		gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(HAMBURG, 15));
		gMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
		gMap.setOnMapLongClickListener(this);
		gMap.setOnMapClickListener(this);
		gMap.setOnMarkerClickListener(this);
	}
	

	@Override
	public void onMapClick(LatLng arg0) {
		// TODO Auto-generated method stub
		gMap.animateCamera(CameraUpdateFactory.newLatLng(arg0));
		markerClicked = false;
	}

	@Override
	public void onMapLongClick(LatLng newLatLong) {
		// TODO Auto-generated method stub
		showDialog("Added new LatLong");
		gMap.addMarker(new MarkerOptions().position(newLatLong).title("New "+count));
		count++;
		markerClicked = false;
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		// TODO Auto-generated method stub
		if(markerClicked){
			/**
			 * Add Polyline
			 */
			if(polyline != null){
				polyline.remove();
				polyline = null;
			}
			polyLineOptions.add(marker.getPosition());
			polyLineOptions.color(Color.RED);
			polyline = gMap.addPolyline(polyLineOptions);
		}else{
			/**
			 * Remove Polyline
			 */
			if(polyline != null){
				polyline.remove();
				polyline = null;
				polyLineOptions = new PolylineOptions().add(marker.getPosition());
				markerClicked = true;
			}
		}
		return true;
	}

	private void showAlert(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}

	private void showDialog(String msg) {
		AlertDialog.Builder LicenseDialog = new AlertDialog.Builder(
				MainActivity.this);
		LicenseDialog.setTitle("Legal Notices");
		LicenseDialog.setMessage(msg);
		LicenseDialog.show();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		int RQS_GooglePlayServices = 1;
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

		if (resultCode == ConnectionResult.SUCCESS) {
			showAlert("isGooglePlayServicesAvailable SUCCESS");
		} else {
			GooglePlayServicesUtil.getErrorDialog(resultCode, this,RQS_GooglePlayServices);
		}
	}
}
