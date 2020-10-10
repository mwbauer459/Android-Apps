package com.maxbauer.photoalbum;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;

/***************************ViewPhotos Activity*********************/
public class ViewPhotos extends Activity {


/**************************onCreate*******************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_photos);
        ArrayList<Integer> ids = getPhotoIds();
        ImageView iv = (ImageView)findViewById(R.id.imageView);
        iv.setImageResource(ids.get(1));
        iv.setTag(1);

    }

    /**************************onCreateOptionsMenu*************************/

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_photos, menu);
        return true;
    }
    /****************************onOptionsItemSelected***********************/

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    /**********************getPhotoIds()*****************************/
    // This method returns an Integer ArrayList populated by
    // the ids of the resources in @drawable

    public ArrayList<Integer> getPhotoIds()
    {
        R.drawable drawableResources = new R.drawable();
        Class<R.drawable> c = R.drawable.class;
        Field[] fields = c.getDeclaredFields();
        Log.e("Tag", "Shit");
        ArrayList<Integer> ids = new ArrayList<>();

        for (int i = 0, max = fields.length; i < max; i++)
        {
            final int resourceId;
            try
            {
                resourceId = fields[i].getInt(drawableResources);
                ids.add(resourceId);
            } catch (Exception e) {
                Log.e("getPhotoIds()", "Problem Getting Resources");
                continue;
            }
    /* make use of resourceId for accessing Drawables here */
        }
        return ids;
    }
/*******************************next()**************************/
// onClick for next button. Scrolls to next photo
    public void next(View v)
    {

        ImageView iv = (ImageView)findViewById(R.id.imageView);
        int currentId =Integer.parseInt(iv.getTag().toString());
        if(currentId < getPhotoIds().size()-1)
        {
            iv.setTag(currentId + 1);
            iv.setImageResource(getPhotoIds().get(currentId + 1));

        }
        else
            iv.setTag(currentId -1);

        Log.e("next", "" + currentId + "");
    }
/***********************************back()*******************/
    public void back(View v)
    {
        ImageView iv = (ImageView)findViewById(R.id.imageView);
        int currentId =Integer.parseInt(iv.getTag().toString());
        Log.e("Back", "Current ID = " + currentId);

        if(currentId > 1)
        {
            iv.setVisibility(View.VISIBLE);
            iv.setTag(currentId - 1);
            iv.setImageResource(getPhotoIds().get(currentId - 1));

        }
        else
            iv.setTag(currentId +1);

        Log.e("Tag", "" + currentId + "");
    }
/**********************************autoScroll******************/
// autoScroll function that scrolls through the photos one at a time
// until the end of the album is reached. When the end of the album
// is reached, user control of the photo album returns to normal
    public void autoScroll(View v)
    {


        new Thread(new Runnable()
        {

            @Override
            public void run()
            {
            ImageView iv = (ImageView) findViewById(R.id.imageView);
            int i = Integer.parseInt(iv.getTag().toString());
            ArrayList ar = getPhotoIds();
            while (i < ar.size() - 1)
            {
                try
                {
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {

                            ImageView iv = (ImageView) findViewById(R.id.imageView);
                            int currentId = Integer.parseInt(iv.getTag().toString());
                            if (currentId < getPhotoIds().size() - 1)
                            {
                                iv.setTag(currentId + 1);
                                iv.setImageResource(getPhotoIds().get(currentId + 1));

                            }

                        }
                    });

                    Thread.sleep(2000);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                i++;
            }
        }
        }).start();

    }
}

