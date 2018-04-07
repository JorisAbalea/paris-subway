package tests.joris.com.myapplication;

import android.app.Application;
import android.util.Log;

import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;
import com.microsoft.appcenter.distribute.Distribute;
import com.microsoft.appcenter.push.Push;

public class ParisSubwayApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		Log.e("ParisSubwayApplication", "ICI");
		AppCenter.start(this, "490288f2-fb10-4b4d-9b6a-a714cdb4075e",
			Analytics.class, Crashes.class, Distribute.class, Push.class);
	}
}
