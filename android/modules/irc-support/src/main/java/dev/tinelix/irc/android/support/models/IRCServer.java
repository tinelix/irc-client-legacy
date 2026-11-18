package dev.tinelix.irc.android.support.models;

public class IRCServer {

    // Text encoding constants
    public static final int TEXT_ENCODING_AUTO                       = 0x1000;
    // Unicode
    public static final int TEXT_ENCODING_UNICODE_UTF8               = 0x1001;
    public static final int TEXT_ENCODING_UNICODE_UTF16_LE           = 0x1002;
    public static final int TEXT_ENCODING_UNICODE_UTF16_BE           = 0x1003;

    // Windows
    public static final int TEXT_ENCODING_WINDOWS_CYRILLIC           = 0x1100;  // Windows-1251
    public static final int TEXT_ENCODING_WINDOWS_WESTERN_EUROPE     = 0x1101;  // Windows-1252
    public static final int TEXT_ENCODING_WINDOWS_GREEK              = 0x1102;  // Windows-1253
    public static final int TEXT_ENCODING_WINDOWS_TURKISH            = 0x1103;  // Windows-1254
    public static final int TEXT_ENCODING_WINDOWS_HEBREW             = 0x1104;  // Windows-1255
    public static final int TEXT_ENCODING_WINDOWS_ARABIC             = 0x1105;  // Windows-1256

    // DOS
    public static final int TEXT_ENCODING_DOS_ASCII                  = 0x1200;  // CP437
    public static final int TEXT_ENCODING_DOS_CYRILLIC               = 0x1201;  // CP866

    // KOI-8 family (GOST 19768-74)
    public static final int TEXT_ENCODING_KOI8_RUSSIA               = 0x1400;
    public static final int TEXT_ENCODING_KOI8_UKRAINE              = 0x1401;
    public static final int TEXT_ENCODING_KOI8_TAJIK                = 0x1402;
    public static final int TEXT_ENCODING_KOI8_CAUCASUS             = 0x1403;

    public String   name;
    public String   address;
    public int      port;
    public int      inputEncoding;
    public int      outputEncoding;
    public boolean  tlsEnabled;
    public boolean  hideIP;

    public IRCServer(String address) {
        String[] array = address.split(":");

        if(array.length == 2) {
            try {
                this.address = array[0];
                this.port = Integer.parseInt(array[1]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        tlsEnabled = false;
        inputEncoding = TEXT_ENCODING_UNICODE_UTF8;
        outputEncoding = TEXT_ENCODING_UNICODE_UTF8;
    }

    public IRCServer(String address, int port) {
        this.address = address;
        this.port = port;

        tlsEnabled = false;
        inputEncoding = TEXT_ENCODING_UNICODE_UTF8;
        outputEncoding = TEXT_ENCODING_UNICODE_UTF8;
    }

    public IRCServer(String address, boolean tlsEnabled) {
        String[] array = address.split(":");

        if(array.length == 2) {
            try {
                this.address = array[0];
                this.port = Integer.parseInt(array[1]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        this.tlsEnabled = tlsEnabled;
        inputEncoding = TEXT_ENCODING_UNICODE_UTF8;
        outputEncoding = TEXT_ENCODING_UNICODE_UTF8;
    }

    public IRCServer(String address, int port, boolean tlsEnabled) {
        this.address = address;
        this.port = port;

        this.tlsEnabled = tlsEnabled;
        inputEncoding = TEXT_ENCODING_UNICODE_UTF8;
        outputEncoding = TEXT_ENCODING_UNICODE_UTF8;
    }

    public IRCServer(String address, int port, int textEncoding) {
        this.address = address;
        this.port = port;

        tlsEnabled = false;
        this.inputEncoding = textEncoding;
        this.outputEncoding = textEncoding;
    }

    public IRCServer(String address, int port, int inputEncoding, int outputEncoding) {
        this.address = address;
        this.port = port;

        tlsEnabled = false;
        this.inputEncoding = inputEncoding;
        this.outputEncoding = outputEncoding;
    }

    public IRCServer(String address, int port, boolean enableTLS, int textEncoding) {
        this.address = address;
        this.port = port;

        tlsEnabled = enableTLS;
        this.inputEncoding = textEncoding;
        this.outputEncoding = textEncoding;
    }

    public IRCServer(String address, int port, boolean enableTLS,
                     int inputEncoding, int outputEncoding) {
        this.address = address;
        this.port = port;

        tlsEnabled = enableTLS;
        this.inputEncoding = inputEncoding;
        this.outputEncoding = outputEncoding;
    }

    public void setInputTextEncoding(String encoding) {
        switch (encoding) {
            default:
                inputEncoding = IRCServer.TEXT_ENCODING_UNICODE_UTF8;
                break;
            case "utf-16be":
                inputEncoding = IRCServer.TEXT_ENCODING_UNICODE_UTF16_BE;
                break;
            case "utf-16le":
                inputEncoding = IRCServer.TEXT_ENCODING_UNICODE_UTF16_LE;
                break;
            case "cp437":
                inputEncoding = IRCServer.TEXT_ENCODING_DOS_ASCII;
                break;
            case "cp866":
                inputEncoding = IRCServer.TEXT_ENCODING_DOS_CYRILLIC;
                break;
            case "windows-1251":
                inputEncoding = IRCServer.TEXT_ENCODING_WINDOWS_CYRILLIC;
                break;
            case "windows-1252":
                inputEncoding = IRCServer.TEXT_ENCODING_WINDOWS_WESTERN_EUROPE;
                break;
            case "windows-1253":
                inputEncoding = IRCServer.TEXT_ENCODING_WINDOWS_GREEK;
                break;
            case "windows-1254":
                inputEncoding = IRCServer.TEXT_ENCODING_WINDOWS_TURKISH;
                break;
            case "windows-1255":
                inputEncoding = IRCServer.TEXT_ENCODING_WINDOWS_HEBREW;
                break;
            case "windows-1256":
                inputEncoding = IRCServer.TEXT_ENCODING_WINDOWS_ARABIC;
                break;
        }
    }

    public void setOutputTextEncoding(String encoding) {
        switch (encoding) {
            default:
                outputEncoding = IRCServer.TEXT_ENCODING_UNICODE_UTF8;
                break;
            case "utf-16be":
                outputEncoding = IRCServer.TEXT_ENCODING_UNICODE_UTF16_BE;
                break;
            case "utf-16le":
                outputEncoding = IRCServer.TEXT_ENCODING_UNICODE_UTF16_LE;
                break;
            case "cp437":
                outputEncoding = IRCServer.TEXT_ENCODING_DOS_ASCII;
                break;
            case "cp866":
                outputEncoding = IRCServer.TEXT_ENCODING_DOS_CYRILLIC;
                break;
            case "windows-1251":
                outputEncoding = IRCServer.TEXT_ENCODING_WINDOWS_CYRILLIC;
                break;
            case "windows-1252":
                outputEncoding = IRCServer.TEXT_ENCODING_WINDOWS_WESTERN_EUROPE;
                break;
            case "windows-1253":
                outputEncoding = IRCServer.TEXT_ENCODING_WINDOWS_GREEK;
                break;
            case "windows-1254":
                outputEncoding = IRCServer.TEXT_ENCODING_WINDOWS_TURKISH;
                break;
            case "windows-1255":
                outputEncoding = IRCServer.TEXT_ENCODING_WINDOWS_HEBREW;
                break;
            case "windows-1256":
                outputEncoding = IRCServer.TEXT_ENCODING_WINDOWS_ARABIC;
                break;
        }
    }

    public static String getCurrentEncodingStr(int value) {
        switch (value) {
            case TEXT_ENCODING_UNICODE_UTF8:
                return "utf-8";
            case TEXT_ENCODING_UNICODE_UTF16_BE:
                return "utf-16be";
            case TEXT_ENCODING_UNICODE_UTF16_LE:
                return "utf-16le";
            case TEXT_ENCODING_DOS_ASCII:
                return "cp437";
            case TEXT_ENCODING_WINDOWS_WESTERN_EUROPE:
                return "windows-1252";
            case TEXT_ENCODING_DOS_CYRILLIC:
                return "cp866";
            case TEXT_ENCODING_WINDOWS_CYRILLIC:
                return "windows-1251";
        }
        return null;
    }
}
