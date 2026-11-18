package dev.tinelix.geomapper.library;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LocationMapper {
    public static final int FORMAT_COUNTRY_SHORT = 0x0002;
    public static final int FORMAT_COUNTRY_FULL = 0x0003;

    private static InputStream mCountryStream;
    private static BufferedReader mCountryReader;

    public static void initialize(Context context) {
        try {
            if (mCountryStream == null && mCountryReader == null) {
                mCountryStream = context.getResources().openRawResource(R.raw.countries_list);
                mCountryReader = new BufferedReader(new InputStreamReader(mCountryStream));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String findCountryNameByCode(String code, int format) {
        if(code.length() == 2 || code.length() == 3) {
            String line;

            try {
                while ((line = mCountryReader.readLine()) != null) {
                    String[] csvLine = line.split(",");
                    if(!csvLine[0].equals(code) && csvLine.length < 3)
                        continue;

                    if(format == FORMAT_COUNTRY_FULL)
                        return csvLine[2];
                    else
                        return csvLine[1];
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public static void free() {
        try {
            if (mCountryStream != null && mCountryReader != null) {
                mCountryReader.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getCountry(String isoCountry, int standard) {
        return "";
    }
}
