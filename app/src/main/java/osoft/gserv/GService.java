package osoft.gserv;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;

import java.security.Security;

public class GService extends Service implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<LocationSettingsResult> {
    GoogleApiClient mGoogleApiClient = null;

    public GService() {
        Log.i("GServ","Service constructor");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onResult(LocationSettingsResult result) {
        Log.d("GServ", "Location request result: " + result.getStatus().toString());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mGoogleApiClient.connect();
        Log.d("GServ", "Service created");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("GServ","Service started");

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        Log.d("GServ", "Service destroyed");
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.d("GServ", "Google connected");
        try {
            LocationRequest mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(10000);
            mLocationRequest.setFastestInterval(5000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(mLocationRequest);


            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,
                            builder.build());
            result.setResultCallback(this);

/*            Location mLastLocation =
                    LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {
                Log.d("GServ", "Location retrieved: " + mLastLocation.toString());
            } else {
                Log.d("GServ", "got null location");
            }*/
        }
        catch (SecurityException e) {
            Log.e("GServ", "Could not get location: " + e.toString());
        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("GServ", String.format("Google connection failed: %d (%s)", connectionResult.getErrorCode(), connectionResult.getErrorMessage()));

    }

    @Override
    public void onConnectionSuspended(int code) {
        Log.d("GServ", "Google connection suspended");
    }

}
