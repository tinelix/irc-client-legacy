package dev.tinelix.irc.android.support.models;

public class IRCServer {

    // Unicode
    public static final int TEXT_ENCODING_UNICODE_UTF8               = 0x1000;
    public static final int TEXT_ENCODING_UNICODE_UTF16_LE           = 0x1001;
    public static final int TEXT_ENCODING_UNICODE_UTF16_BE           = 0x1002;

    // Windows
    public static final int TEXT_ENCODING_WINDOWS_CYRILLIC           = 0x1100;
    public static final int TEXT_ENCODING_WINDOWS_WESTERN_EUROPE     = 0x1101;
    public static final int TEXT_ENCODING_WINDOWS_GREEK              = 0x1102;
    public static final int TEXT_ENCODING_WINDOWS_TURKISH            = 0x1103;
    public static final int TEXT_ENCODING_WINDOWS_HEBREW             = 0x1104;
    public static final int TEXT_ENCODING_WINDOWS_ARABIC             = 0x1105;

    // DOS
    public static final int TEXT_ENCODING_DOS_ASCII                  = 0x1200;
    public static final int TEXT_ENCODING_DOS_CYRILLIC               = 0x1201;

    public String address;
    public int port;
    public boolean tlsEnabled;
    public int inputEncoding;
    public int outputEncoding;

    public IRCServer(String address, int port) {
        this.address = address;
        this.port = port;

        tlsEnabled = false;
        inputEncoding = TEXT_ENCODING_UNICODE_UTF8;
        outputEncoding = TEXT_ENCODING_UNICODE_UTF8;
    }

    public IRCServer(String address, int port, boolean enableTLS) {
        this.address = address;
        this.port = port;

        tlsEnabled = enableTLS;
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

}
