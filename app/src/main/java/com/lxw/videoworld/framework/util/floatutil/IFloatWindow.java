package com.lxw.videoworld.framework.util.floatutil;

import android.view.View;

public abstract class IFloatWindow {
    public abstract void show();

    public abstract void hide();

    public abstract int getX();

    public abstract int getY();

    public abstract void updateX(int x);

    public abstract void updateX(@Screen.screenType int screenType,float ratio);

    public abstract void updateY(int y);

    public abstract void updateY(@Screen.screenType int screenType,float ratio);

    public abstract View getView();

    abstract void dismiss();
}
