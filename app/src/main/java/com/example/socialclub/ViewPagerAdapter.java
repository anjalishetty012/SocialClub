package com.example.socialclub;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {
    List<slideritems> slideritems;
    LayoutInflater mLayoutInflater;
    Context context;
    ArrayList<String> title;
    ArrayList<String> desc;

    ArrayList<String> newslink;
    VerticalViewPager verticalViewPager;



    public ViewPagerAdapter(Context context, List<com.example.socialclub.slideritems> slideritems, ArrayList<String> title, ArrayList<String> desc, ArrayList<String> newslink, VerticalViewPager verticalViewPager) {
        this.context=context;
        this.slideritems = slideritems;
        this.title=title;
        this.desc=desc;
        this.newslink=newslink;
        this.verticalViewPager=verticalViewPager;
        mLayoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return slideritems.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==((LinearLayout)object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View itemView=mLayoutInflater.inflate(R.layout.item_container,container,false);
        ImageView imageView=itemView.findViewById(R.id.imageView);
        
        ImageView imageView2;
        imageView2 = itemView.findViewById(R.id.imageView2);
        TextView Title=itemView.findViewById(R.id.Title);
        TextView Desc=itemView.findViewById(R.id.Desc);
        TextView taphere=itemView.findViewById(R.id.taphere);

        Title.setText(title.get(position));
        Desc.setText(desc.get(position));
        taphere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoUrl(newslink.get(position));
            }
        });

        Picasso.get().load(slideritems.get(position).getImage())
                .into(imageView);
        Picasso.get().load(slideritems.get(position).getImage())
                .centerCrop()
                .resize(12,12)
                .into(imageView2);

        
        container.addView(itemView);
        return  itemView;
        
    }

    private void gotoUrl(String s) {
        Uri uri=Uri.parse(s);
        context.startActivity(new Intent(Intent.ACTION_VIEW,uri));
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }
}

