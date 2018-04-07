package tests.joris.com.myapplication.util;

import android.content.Context;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ConverterUtil {
	private static final String TAG = ConverterUtil.class.getName();

	private ConverterUtil() {
		//util class
	}

	@SuppressWarnings("ResultOfMethodCallIgnored")
	public static String readFileFromRawDirectory(int resourceId, Context context) {
		InputStream iStream = context.getResources().openRawResource(resourceId);
		ByteArrayOutputStream byteStream = null;
		try {
			byte[] buffer = new byte[iStream.available()];
			iStream.read(buffer);
			byteStream = new ByteArrayOutputStream();
			byteStream.write(buffer);
			byteStream.close();
			iStream.close();
		} catch (IOException e) {
			Log.e(TAG, "Error: read rw file");
		}
		if(byteStream == null){
			return null;
		}
		return byteStream.toString();
	}
}
