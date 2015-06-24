package com.isispl.imagelodingfromserverinlist;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.isispl.imagelodingfromserverinlist.model.Flower;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * Created by Jaymin581 on 24/06/15.
 */
public class FlowerAdapter extends ArrayAdapter<Flower> {

    private Context context;
    private List<Flower> flowerList;

    public FlowerAdapter(Context context, int resource, List<Flower> objects) {
        super(context, resource, objects);
        this.context = context;
        this.flowerList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.row_item, parent, false);

        // Display Flower name
        Flower flower = flowerList.get(position);

        TextView tv_item = (TextView)view.findViewById(R.id.row_item_txt);

        tv_item.setText(flower.getName());

        if(flower.getBitmap() != null){
            //Display image
            ImageView img = (ImageView)view.findViewById(R.id.row_item_img);
            img.setImageBitmap(flower.getBitmap());
        }

        else{
            FlowerAndView container  =  new FlowerAndView();
            container.flower = flower;
            container.view = view;

            ImageLoader loader = new ImageLoader();
            loader.execute(container);

        }



        return view;

    }

    class FlowerAndView{
        public Flower flower;
        public View view;
        public Bitmap bitmap;
    }

    private class ImageLoader extends AsyncTask<FlowerAndView, Void, FlowerAndView>{

        @Override
        protected FlowerAndView doInBackground(FlowerAndView... params) {
            FlowerAndView container = params[0];
            Flower flower = container.flower;

            try{
                String imgURL = MainActivity.PHOTO_BASE_URL + flower.getPhoto();
                InputStream in = (InputStream)new URL(imgURL).getContent();
                Bitmap bitmap = BitmapFactory.decodeStream(in);
                flower.setBitmap(bitmap);
                in.close();
                container.bitmap = bitmap;
                return container;
            }
            catch (Exception e){
                e.printStackTrace();

            }

            return null;
        }

        @Override
        protected void onPostExecute(FlowerAndView result) {


            ImageView img = (ImageView) result.view.findViewById(R.id.row_item_img);
            img.setImageBitmap(result.bitmap); //Display image
            result.flower.setBitmap(result.bitmap); // Save image for future use
        }
    }




}
