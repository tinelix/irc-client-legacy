package dev.tinelix.irc.android.legacy.typefaces;

import android.content.Context;
import android.graphics.Typeface;

public class DefaultFontFamily {

    public static final int WEIGHT_REGULAR    = 400;
    public static final int WEIGHT_MEDIUM     = 500;
    public static final int WEIGHT_SEMIBOLD   = 600;
    public static final int WEIGHT_BOLD       = 700;

    private Context mContext;
    private String mDirectoryPrefix = "";
    private String mTypefacePrefix = "";

    public DefaultFontFamily(Context context) {
        mContext = context;
    }

    public DefaultFontFamily(Context context, String directoryPrefix, String typefacePrefix) {
        mContext = context;
        mDirectoryPrefix = directoryPrefix;
        mTypefacePrefix = typefacePrefix;
    }

    public Typeface getTypeface(int weight, boolean italic) {

        StringBuilder pathBuilder = new StringBuilder();

        pathBuilder.append("fonts")
                   .append("/").append(mDirectoryPrefix)
                   .append("/").append(mTypefacePrefix).append("-");

        switch (weight) {
            case WEIGHT_REGULAR:
                if (mDirectoryPrefix == null && mTypefacePrefix == null)
                    return Typeface.DEFAULT;

                pathBuilder.append(!italic ? "Regular.ttf" : "Italic.ttf");

                break;
            case WEIGHT_MEDIUM:
                if (mDirectoryPrefix == null && mTypefacePrefix == null)
                    return Typeface.DEFAULT;

                pathBuilder.append(!italic ? "Medium.ttf" : "MediumItalic.ttf");

                break;
            case WEIGHT_SEMIBOLD:
                if (mDirectoryPrefix == null && mTypefacePrefix == null)
                    return Typeface.DEFAULT;

                pathBuilder.append(!italic ? "SemiBold.ttf" : "SemiBoldItalic.ttf");
                break;
            case WEIGHT_BOLD:
                if (mDirectoryPrefix == null && mTypefacePrefix == null)
                    return Typeface.DEFAULT_BOLD;

                pathBuilder.append(!italic ? "Bold.ttf" : "BoldItalic.ttf");

                break;
        }

        return Typeface.createFromAsset(mContext.getAssets(), pathBuilder.toString());
    }
}
