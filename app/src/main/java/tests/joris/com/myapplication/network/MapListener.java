package tests.joris.com.myapplication.network;

import tests.joris.com.myapplication.model.APIPath;

public interface MapListener {

	void onPathFailedReceived(Throwable t);

	void onPathReceived(APIPath map);
}
