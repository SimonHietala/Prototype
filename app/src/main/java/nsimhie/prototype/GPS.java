package nsimhie.prototype;

/**
 * Created by nsimhie on 2015-10-26.
 */
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class GPS extends Service implements LocationListener
{

    boolean isGPSEnabled = false;//flag for GPS status
    boolean isNetworkEnabled = false;//flag for network status
    boolean canGetLocation = false;//flag for GPS status

    private final Context context; //context
    Location location; //location
    double latitude; //latitude
    double longitude; //longitude

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    // Declaring a Location Manager
    protected LocationManager locationManager;


    //Constructor for the class.
    public GPS(Context context)
    {
        this.context = context;
        getLocation();
    }

    public Location getLocation()
    {
        try
        {
            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

            //get GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            //get network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if(!this.isGPSEnabled && !this.isNetworkEnabled)
            {
                //No information available, no gps or network.
            }

            else
            {
                this.canGetLocation = true;
                //Get location from the network
                if(this.isNetworkEnabled)
                {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    if(locationManager != null)
                    {
                        this.location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if(this.location != null)
                        {
                            this.latitude = location.getLatitude();
                            this.longitude = location.getLongitude();
                        }
                    }
                }

                //Get location from GPS.
                if(this.isGPSEnabled)
                {
                    if(this.location == null)
                    {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                        if(this.locationManager != null)
                        {
                            this.location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (this.location != null)
                            {
                                this.latitude = location.getLatitude();
                                this.longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }

        return this.location;
    }

    //Stops the GPS tracking.
    public void stopGPS()
    {
        if(this.locationManager != null)
        {
            this.locationManager.removeUpdates(GPS.this);
        }
    }

    //Getter for latitude
    public double getLatitude()
    {
        if(this.location != null)
        {
            this.latitude = this.location.getLatitude();
        }
        // return latitude
        return this.latitude;
    }

    //Getter for longitude
    public double getLongitude()
    {
        if(this.location != null)
        {
            this.longitude = this.location.getLongitude();
        }
        // return longitude
        return this.longitude;
    }

    public String getCoordinates()
    {
        return Double.toString(getLatitude()) + "\n" + Double.toString(getLongitude());
    }

    public boolean canGetLocation()
    {
        return this.canGetLocation;
    }

    @Override
    public void onLocationChanged(Location location)
    {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }
}