package org.de_studio.phonetools;

import android.view.View;
import android.widget.Button;

/**
 * Created by hai on 10/17/2015.
 */
public class MyWrapper
{
    private View base;
    private Button button;

    public MyWrapper(View base)
    {
        this.base = base;
    }

    public Button getButton()
    {
        if (button == null)
        {
            button = (Button) base.findViewById(R.id.item_action);
        }
        return button;
    }
}
