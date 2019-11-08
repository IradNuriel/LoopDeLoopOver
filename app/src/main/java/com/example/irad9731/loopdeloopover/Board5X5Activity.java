package com.example.irad9731.loopdeloopover;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v7.widget.GridLayout;
import android.widget.TextView;

import java.util.Random;


public class Board5X5Activity extends BoardLogic {

    private static final int NBR_ITEMS = 25;
//    private GridLayout mGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board5_x5);

        mGrid = (GridLayout) findViewById(R.id.grid_layout5X5);
        mClock = (TextView)findViewById(R.id.clock5X5);
        mGrid.setOnDragListener(new DragListener());
        int[] content = new int[NBR_ITEMS];

        for(int i=0;i<NBR_ITEMS;i++){
            content[i] = i+1;
        }
        Random rnd = new Random();
        for (int i=0; i<content.length; i++) {
            int randomPosition = rnd.nextInt(content.length);
            int temp = content[i];
            content[i] = content[randomPosition];
            content[randomPosition] = temp;
        }

        final LayoutInflater inflater = LayoutInflater.from(this);
        for (int i = 0; i < NBR_ITEMS; i++) {
            //Adding the items dynamically into the grid.
            final View itemView = inflater.inflate(R.layout.grid_item, mGrid, false);
            final TextView text = (TextView) itemView.findViewById(R.id.text);
            text.setText(String.valueOf(content[i]));
            itemView.setOnLongClickListener(new LongPressListener());
            mGrid.addView(itemView);
        }
    }

}