package com.example.irad9731.loopdeloopover;

import android.os.Bundle;
import android.support.v7.widget.GridLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

public class Board7X7Activity extends BoardLogic {
    private static final int NBR_ITEMS = 49;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board7_x7);

        mGrid = (GridLayout) findViewById(R.id.grid_layout7X7);
        mClock = (TextView)findViewById(R.id.clock7X7);
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
