package com.optimalotaku.paraguide;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brandon on 1/7/17.
 */

final class MyDeckAdapter extends BaseAdapter {
    private final List<Item> mItems = new ArrayList<Item>();
    private final LayoutInflater mInflater;
    private Bitmap cotdBitMapImg;

    public MyDeckAdapter(Context context, String[] cotdBitMapImg, String [] cardText) {
        mInflater = LayoutInflater.from(context);

        for(int i = 0; i < cardText.length; i++){
            Item card = new Item(cardText[i], cotdBitMapImg[i]);
            mItems.add(card);
        }

    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Item getItem(int i) {
        return mItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        ImageView picture;
        TextView name;

        if (v == null) {
            v = mInflater.inflate(R.layout.grid_item, viewGroup, false);
            v.setTag(R.id.picture, v.findViewById(R.id.picture));
            v.setTag(R.id.text, v.findViewById(R.id.text));
        }

        picture = (ImageView) v.getTag(R.id.picture);
        name = (TextView) v.getTag(R.id.text);

        Item item = getItem(i);

        Glide.with(viewGroup.getContext()).load("https:" + item.drawable1).into(picture);
        name.setText(item.name);


        return v;
    }

    private static class Item {
        public final String name;
        public final String drawable1;

        Item(String name, String drawable) {
            this.name = name;
            this.drawable1 = drawable;
        }
    }
}