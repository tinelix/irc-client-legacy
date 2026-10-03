package dev.tinelix.irc.android.legacy.ui.lists.items;

public class LazyEntity {

    public static final int TYPE_SLEEPING_ENTITY = 0;
    public static final int TYPE_LAYOUT_HEADER = 1;
    public static final int TYPE_REAL_ENTITY = 2;

    public int id;
    public int type;
    public Object object;
}
