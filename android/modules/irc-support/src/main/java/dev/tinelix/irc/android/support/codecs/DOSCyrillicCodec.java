package dev.tinelix.irc.android.support.codecs;

public class DOSCyrillicCodec {

    /* DOS Codepage 866 (Cyrillic) advanced encoder and decoder with lossy
     * text compression for Android 2.x
     *
     * NOTE: Characters in Java encoded into the UTF-16.
     */

    public static byte[] encode(String string) {
        byte[] output = new byte[string.length() / 2];

        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            output[i] = convertUTF16Char(c);
        }

        return output;
    }

    private static byte convertUTF16Char(char c) {
        switch (c) {

            // Semi-graphics characters
            case 0x263A:        // Unicode U+263A: WHITE SMILING FACE
                return 0x01;
            case 0x263B:        // Unicode U+263B: BLACK SMILING FACE
                return 0x02;
            case 0x2665:        // Unicode U+2665: BLACK HEART SUIT
                return 0x03;
            case 0x2666:        // Unicode U+2666: BLACK DIAMOND SUIT
                return 0x04;
            case 0x2663:        // Unicode U+2663: BLACK CLUB SUIT
                return 0x05;
            case 0x2660:        // Unicode U+2660: BLACK DIAMOND SUIT
                return 0x06;
            case 0x2022:        // Unicode U+2022: BULLET
                return 0x07;
            case 0x25D8:        // Unicode U+25D8: INVERSE BULLET
                return 0x08;
            case 0x25CB:        // Unicode U+25CB: WHITE CIRCLE
                return 0x09;
            case 0x25D9:        // Unicode U+25D9: INVERSE WHITE CIRCLE
                return 0x0A;
            case 0x2642:        // Unicode U+2642: MALE SIGN
                return 0x0B;
            case 0x2640:        // Unicode U+2640: FEMALE SIGN
                return 0x0C;
            case 0x266A:        // Unicode U+266A: EIGHTH NOTE
                return 0x0D;
            case 0x266B:        // Unicode U+266B: BEAMED EIGHTH NOTES
                return 0x0E;
            case 0x263C:        // Unicode U+263C: WHITE SUN WITH RAYS
                return 0x0F;
            case 0x25BA:        // Unicode U+25BA: BLACK RIGHT-POINTING POINTER
                return 0x10;
            case 0x25C4:        // Unicode U+25C4: BLACK LEFT-POINTING POINTER
                return 0x11;
            case 0x2195:        // Unicode U+2195: UP DOWN ARROW
                return 0x12;
            case 0x203C:        // Unicode U+203C: DOUBLE EXCLAMATION MARK
                return 0x13;
            case 0x00B6:        // Unicode U+00B6: PILCROW SIGN
                return 0x14;
            case 0x00A7:        // Unicode U+00A7: SECTION SIGN
                return 0x15;
            case 0x25AC:        // Unicode U+25AC: BLACK RECTANGLE
                return 0x16;
            case 0x21A8:        // Unicode U+21A8: UP DOWN ARROW WITH BASE
                return 0x17;
            case 0x2191:        // Unicode U+2191: UPWARDS ARROW
                return 0x18;
            case 0x2193:        // Unicode U+2193: DOWNWARDS ARROW
                return 0x19;
            case 0x2192:        // Unicode U+2192: RIGHTWARDS ARROW
                return 0x1A;
            case 0x2190:        // Unicode U+2190: LEFTWARDS ARROW
                return 0x1B;
            case 0x221F:        // Unicode U+221F: RIGHT ANGLE
                return 0x1C;
            case 0x2194:        // Unicode U+2194: LEFT RIGHT ARROW
                return 0x1D;
            case 0x25B2:        // Unicode U+25B2: BLACK UP-POINTING TRIANGLE
                return 0x1E;
            case 0x25BC:        // Unicode U+25BC: BLACK DOWN-POINTING TRIANGLE
                return 0x1F;
            case 0x0401:            // Unicode U+0401: Russian captial letter IO        [Ё]
                return (byte) 0xF0;
            case 0x0451:            // Unicode U+0451: Russian small letter IO          [ё]
                return (byte) 0xF1;
            case 0x0404:            // Unicode U+0404: Ukrainian capital letter IE      [Є]
                return (byte) 0xF2;
            case 0x0454:            // Unicode U+0454: Ukrainian small letter IE        [є]
                return (byte) 0xF3;
            case 0x0407:            // Unicode U+0404: Ukrainian capital letter YI      [Ї]
                return (byte) 0xF4;
            case 0x0457:            // Unicode U+0454: Ukrainian small letter YI        [ї]
                return (byte) 0xF5;
            default:
                int offset;
                if(c >= 0x0440 && c <= 0x044F) {        // Russian small letters (part 2)
                    offset = 0x350;
                    return (byte) (c - offset);
                } else if(c >= 0x0410) {                // Russian capital and small letters (part 1)
                    offset = 0x320;
                    return (byte) (c - offset);
                } else if(c >= 0x0020 && c <= 0x007E) { // Latin alphabet
                    return (byte) c;
                } else {
                    return 0x3A;
                }
        }
    }
}
