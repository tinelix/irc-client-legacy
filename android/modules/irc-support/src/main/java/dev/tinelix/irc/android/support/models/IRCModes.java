package dev.tinelix.irc.android.support.models;

import android.content.Context;
import android.content.res.Resources;
import android.text.SpannableString;

import java.util.ArrayList;

import dev.tinelix.irc.android.support.R;
import dev.tinelix.irc.android.support.enitites.IRCBroadcastSignal;

@SuppressWarnings("WeakerAccess")
public class IRCModes extends IRCBroadcastSignal {

    public String address;
    private ArrayList<Integer> mAddedModes;
    private ArrayList<Integer> mRemovedModes;

    public static final int USER_MODE_PM_FOR_OPERATORS_ONLY         =   1;
    public static final int USER_MODE_CENSORSHIP                    =   2;
    public static final int USER_MODE_INVISIBLE                     =   3;
    public static final int USER_MODE_AUTHORIZED                    =   4;
    public static final int USER_MODE_PM_FOR_AUTHORIZED_ONLY        =   5;
    public static final int USER_MODE_NO_CTCP                       =   6;
    public static final int USER_MODE_RECEIVE_WALLOPS               =   7;
    public static final int USER_MODE_IP_ADDRESS_CLOAK              =   8;
    public static final int USER_MODE_SERVER_OPERATOR               =   9;
    public static final int USER_MODE_PM_FOR_SECURED_ONLY           =  10;

    public static final int CHANUSER_MODE_BLOCKED                   = 100;
    public static final int CHANUSER_MODE_VOICE                     = 101;
    public static final int CHANUSER_MODE_HALF_OPERATOR             = 102;
    public static final int CHANUSER_MODE_OPERATOR                  = 103;

    public static final int CHANNEL_MODE_MODERATED                  = 501;
    public static final int CHANNEL_MODE_NO_CTCP                    = 502;
    public static final int CHANNEL_MODE_NO_COLOUR_MESSAGES         = 503;
    public static final int CHANNEL_MODE_INVITE_ONLY                = 504;
    public static final int CHANNEL_MODE_TOPIC_LOCK                 = 505;
    public static final int CHANNEL_MODE_NO_MESSAGES_FROM_OUTSIDE   = 506;
    public static final int CHANNEL_MODE_PASSWORD_PROTECTED         = 507;
    public static final int CHANNEL_MODE_REGISTERED                 = 508;
    public static final int CHANNEL_MODE_NO_NICKNAME_CHANGES        = 509;
    public static final int CHANNEL_MODE_MEMBER_COUNT_IS_LIMITED    = 510;
    public static final int CHANNEL_MODE_AUTHORIZED_USERS_ONLY      = 511;
    public static final int CHANNEL_MODE_PRIVATE                    = 512;
    public static final int CHANNEL_MODE_SECRET                     = 513;
    public static final int CHANNEL_MODE_SECURED_USERS_ONLY         = 514;

    public IRCModes(String rawContent) {
        super(IN_USER_MODES, true);
        mRawContent = rawContent;
        parseContent(rawContent);
    }

    @Override
    public boolean parseContent(String rawContent) {
        String[] words = mRawContent.split(" ");

        mAddedModes = new ArrayList<>();
        mRemovedModes = new ArrayList<>();

        if(words.length < 3)
            return false;

        int addedModesIndex = -1;
        int removedModesIndex = -1;

        String modes;

        if(words.length >= 4 && !words[2].startsWith("#") && words[3].startsWith(":"))
            modes = words[3].substring(1);
        else if(words.length >= 4)
            modes = words[3];
        else if(words[2].startsWith(":"))
            modes = words[2].substring(1);
        else
            modes = words[2];

        if(modes.contains("+"))
            addedModesIndex = modes.indexOf('+');

        if(modes.contains("-"))
            removedModesIndex = modes.indexOf('-');

        switch (words[1]) {
            case "MODE":
                type = IRCBroadcastSignal.IN_USER_MODES;

                sender = words[0].startsWith(":") ?
                            words[0].substring(1) : words[0];

                if(words[2].startsWith("#") || words[2].startsWith("&"))
                    destination = words[2];

                if(destination != null && words.length == 5) {
                    address = words[words.length - 1];
                } else if(destination != null) {
                    type = IRCBroadcastSignal.IN_CMD_CHANNEL_MODES;
                }

                break;
            case "324":
                if(words.length < 5)
                    return false;

                type = IRCBroadcastSignal.IN_CMD_CHANNEL_MODES;

                sender = words[0].startsWith(":") ?
                            words[0].substring(1) : words[0];

                destination = words[3];
                break;
        }

        if(addedModesIndex == 0 && modes.substring(addedModesIndex).length() >= 2) {
            String addedModes = removedModesIndex > addedModesIndex ?
                                    modes.substring(addedModesIndex, removedModesIndex) :
                                    modes.substring(addedModesIndex);
            putIRCModes(addedModes, true);
        }

        if(removedModesIndex >= 0 && modes.substring(removedModesIndex).length() >= 2) {
            String removedModes = modes.substring(removedModesIndex);
            putIRCModes(removedModes, false);
        }

        finish();
        return true;
    }

    private void putIRCModes(String mode, boolean isAddedModes) {

        if(isAddedModes && !mode.startsWith("+"))
            return;
        else if(!isAddedModes && !mode.startsWith("-"))
            return;

        if(isAddedModes) {
            if (mAddedModes != null)
                mAddedModes.clear();
            else
                mAddedModes = new ArrayList<>();
        } else {
            if (mRemovedModes != null)
                mRemovedModes.clear();
            else
                mRemovedModes = new ArrayList<>();
        }

        int value = 0;

        for (byte c : mode.substring(1).getBytes()) {
            switch (String.valueOf((char) c)) {
                case "b":
                    if(destination != null && address != null)
                        value = CHANUSER_MODE_BLOCKED;
                    break;
                case "c":
                    if(destination != null)
                        value = CHANNEL_MODE_NO_COLOUR_MESSAGES;
                    break;
                case "D":
                    value = USER_MODE_PM_FOR_OPERATORS_ONLY;
                    break;
                case "G":
                    value = USER_MODE_CENSORSHIP;
                    break;
                case "h":
                    if(destination != null && address != null)
                        value = CHANUSER_MODE_HALF_OPERATOR;
                    break;
                case "i":
                    if(type == IN_CMD_CHANNEL_MODES)
                        value = CHANNEL_MODE_INVITE_ONLY;
                    else
                        value = USER_MODE_INVISIBLE;
                    break;
                case "m":
                    if(type == IN_CMD_CHANNEL_MODES)
                        value = CHANNEL_MODE_MODERATED;
                    break;
                case "n":
                    if(type == IN_CMD_CHANNEL_MODES)
                        value = CHANNEL_MODE_NO_MESSAGES_FROM_OUTSIDE;
                    break;
                case "N":
                    if(type == IN_CMD_CHANNEL_MODES)
                        value = CHANNEL_MODE_NO_NICKNAME_CHANGES;
                    break;
                case "o":
                    if(destination != null && address != null)
                        value = CHANUSER_MODE_OPERATOR;
                    else
                        value = USER_MODE_SERVER_OPERATOR;
                    break;
                case "p":
                    if(type == IN_CMD_CHANNEL_MODES)
                        value = CHANNEL_MODE_PRIVATE;
                    break;
                case "r":
                    if(type == IN_CMD_CHANNEL_MODES)
                        value = CHANNEL_MODE_REGISTERED;
                    else
                        value = USER_MODE_AUTHORIZED;
                    break;
                case "R":
                    if(type == IN_CMD_CHANNEL_MODES)
                        value = CHANNEL_MODE_AUTHORIZED_USERS_ONLY;
                    else
                        value = USER_MODE_PM_FOR_AUTHORIZED_ONLY;
                    break;
                case "s":
                    if(type == IN_CMD_CHANNEL_MODES)
                        value = CHANNEL_MODE_SECRET;
                    break;
                case "T":
                    if(type != IN_CMD_CHANNEL_MODES)
                        value = USER_MODE_NO_CTCP;
                    break;
                case "t":
                    if(type == IN_CMD_CHANNEL_MODES)
                        value = CHANNEL_MODE_TOPIC_LOCK;
                    break;
                case "v":
                    if(destination != null && address != null)
                        value = CHANUSER_MODE_VOICE;
                    break;
                case "w":
                    value = USER_MODE_RECEIVE_WALLOPS;
                    break;
                case "x":
                    value = USER_MODE_IP_ADDRESS_CLOAK;
                    break;
                case "Z":
                    if(type == IN_CMD_CHANNEL_MODES)
                        value = CHANNEL_MODE_SECURED_USERS_ONLY;
                    else
                        value = USER_MODE_PM_FOR_SECURED_ONLY;
                    break;
            }

            if(value > 0) {
                if(isAddedModes)
                    mAddedModes.add(value);
                else
                    mRemovedModes.add(value);
            }
        }
    }


    public ArrayList<Integer> getAddedModes() {
        return mAddedModes;
    }

    public ArrayList<Integer> getRemovedModes() {
        return mRemovedModes;
    }

    private String getLocalizedBody(Context context) {

        Resources res = context.getResources();
        String localizedBody = "";

        if(destination != null && address != null) {
            if (mAddedModes.size() > 0 && mRemovedModes.size() > 0) {
                localizedBody = res.getString(
                        R.string.ircs_signal_chanusers_grants_revoke,
                        sender.split("!")[0], address,
                        translateIRCModesList(res, mAddedModes, true),
                        translateIRCModesList(res, mRemovedModes, false)
                );
            } else if (mAddedModes.size() > 1) {
                localizedBody = res.getString(
                        R.string.ircs_signal_chanusers_grants,
                        sender.split("!")[0], address,
                        translateIRCModesList(res, mAddedModes, true)
                );
            } else if (mAddedModes.size() == 1) {
                localizedBody = res.getString(
                        R.string.ircs_signal_chanusers_grants,
                        sender.split("!")[0], address,
                        translateIRCModesList(res, mAddedModes, true)
                );
            } else if (mRemovedModes.size() > 1) {
                localizedBody = res.getString(
                        R.string.ircs_signal_chanusers_revoke,
                        sender.split("!")[0], address,
                        translateIRCModesList(res, mRemovedModes, false)
                );
            } else if (mRemovedModes.size() == 1) {
                localizedBody = res.getString(
                                    R.string.ircs_signal_chanusers_revoke,
                                    sender.split("!")[0], address,
                                    translateIRCModesList(res, mRemovedModes, false)
                                );
            }
        } else if(destination != null){
            localizedBody = res.getString(
                    R.string.ircs_signal_channels_modes,
                    destination, translateIRCModesList(res, mAddedModes, true)
            );
        } else {
            if (mAddedModes.size() > 0 && mRemovedModes.size() > 0) {
                localizedBody = res.getString(
                        R.string.ircs_signal_mode_added_and_remove_many,
                        translateIRCModesList(res, mAddedModes, true),
                        translateIRCModesList(res, mRemovedModes, false)
                );
            } else if (mAddedModes.size() > 1) {
                localizedBody = res.getString(
                        R.string.ircs_signal_mode_added_many,
                        translateIRCModesList(res, mAddedModes, true)
                );
            } else if (mAddedModes.size() == 1) {
                localizedBody = res.getString(
                        R.string.ircs_signal_mode_added,
                        translateIRCModesList(res, mAddedModes, true)
                );
            } else if (mRemovedModes.size() > 1) {
                localizedBody = res.getString(
                        R.string.ircs_signal_mode_removed_many,
                        translateIRCModesList(res, mAddedModes, true)
                );
            } else if (mRemovedModes.size() == 1) {
                localizedBody = res.getString(
                        R.string.ircs_signal_mode_removed,
                        translateIRCModesList(res, mAddedModes, true)
                );
            }
        }

        return localizedBody;
    }

    @Override
    public SpannableString getLocalizedSpannedBody(Context context, int highlightColor) {

        String localizedBody = getLocalizedBody(context);

        return new SpannableString(localizedBody);
    }

    private String translateIRCModesList(Resources res, ArrayList<Integer> modes, boolean isAddedModes) {

        String[] userModes = res.getStringArray(R.array.ircs_user_modes);
        String[] chanuserModes = res.getStringArray(R.array.ircs_channel_user_modes);
        String[] chanModes = res.getStringArray(R.array.ircs_channel_modes);

        StringBuilder localizedModesList = new StringBuilder();

        char c = 0;

        if(isAddedModes) c = '+'; else c = '-';

        for(int i = 0; i < modes.size(); i++) {
            int value = modes.get(i);

            localizedModesList.append(String.format(" %c ", (char)0x25CF));

            switch (value) {
                case CHANUSER_MODE_VOICE:
                    localizedModesList.append(String.format(chanuserModes[0], c, 'v'));
                    break;
                case CHANUSER_MODE_HALF_OPERATOR:
                    localizedModesList.append(String.format(chanuserModes[1], c, 'h'));
                    break;
                case CHANUSER_MODE_OPERATOR:
                    localizedModesList.append(String.format(chanuserModes[2], c, 'o'));
                    break;
                case CHANUSER_MODE_BLOCKED:
                    localizedModesList.append(String.format(chanuserModes[3], c, 'b'));
                    break;
                case CHANNEL_MODE_MODERATED:
                    localizedModesList.append(String.format(chanModes[0], c, 'm'));
                    break;
                case CHANNEL_MODE_NO_CTCP:
                    localizedModesList.append(String.format(chanModes[1], c, 'C'));
                    break;
                case CHANNEL_MODE_NO_COLOUR_MESSAGES:
                    localizedModesList.append(String.format(chanModes[2], c, 'c'));
                    break;
                case CHANNEL_MODE_INVITE_ONLY:
                    localizedModesList.append(String.format(chanModes[3], c, 'i'));
                    break;
                case CHANNEL_MODE_TOPIC_LOCK:
                    localizedModesList.append(String.format(chanModes[4], c, 't'));
                    break;
                case CHANNEL_MODE_NO_MESSAGES_FROM_OUTSIDE:
                    localizedModesList.append(String.format(chanModes[5], c, 'n'));
                    break;
                case CHANNEL_MODE_PASSWORD_PROTECTED:
                    localizedModesList.append(String.format(chanModes[6], c, 'k'));
                    break;
                case CHANNEL_MODE_NO_NICKNAME_CHANGES:
                    localizedModesList.append(String.format(chanModes[7], c, 'N'));
                    break;
                case CHANNEL_MODE_MEMBER_COUNT_IS_LIMITED:
                    localizedModesList.append(String.format(chanModes[8], c, 'l'));
                    break;
                case CHANNEL_MODE_AUTHORIZED_USERS_ONLY:
                    localizedModesList.append(String.format(chanModes[9], c, 'R'));
                    break;
                case CHANNEL_MODE_PRIVATE:
                    localizedModesList.append(String.format(chanModes[10], c, 'p'));
                    break;
                case CHANNEL_MODE_SECRET:
                    localizedModesList.append(String.format(chanModes[11], c, 's'));
                    break;
                case CHANNEL_MODE_SECURED_USERS_ONLY:
                    localizedModesList.append(String.format(chanModes[12], c, 'z'));
                    break;
                case USER_MODE_PM_FOR_OPERATORS_ONLY:
                    localizedModesList.append(String.format(userModes[0], c, 'D'));
                    break;
                case USER_MODE_CENSORSHIP:
                    localizedModesList.append(String.format(userModes[1], c, 'G'));
                    break;
                case USER_MODE_INVISIBLE:
                    localizedModesList.append(String.format(userModes[2], c, 'i'));
                    break;
                case USER_MODE_AUTHORIZED:
                    localizedModesList.append(String.format(userModes[3], c, 'r'));
                    break;
                case USER_MODE_PM_FOR_AUTHORIZED_ONLY:
                    localizedModesList.append(String.format(userModes[4], c, 'R'));
                    break;
                case USER_MODE_NO_CTCP:
                    localizedModesList.append(String.format(userModes[5], c, 'T'));
                    break;
                case USER_MODE_RECEIVE_WALLOPS:
                    localizedModesList.append(String.format(userModes[6], c, 'w'));
                    break;
                case USER_MODE_IP_ADDRESS_CLOAK:
                    localizedModesList.append(String.format(userModes[7], c, 'x'));
                    break;
                case USER_MODE_PM_FOR_SECURED_ONLY:
                    localizedModesList.append(String.format(userModes[8], c, 'Z'));
                    break;
            }

            if(i < mAddedModes.size() - 1) {
                localizedModesList.append(", \r\n");
            }
        }

        return localizedModesList.toString();
    }
}
