package com.example.irad9731.loopdeloopover;


import android.content.ClipData;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v7.widget.GridLayout;
import android.widget.TextView;


public class Board5X5Activity extends AppCompatActivity {

    private static final int NBR_ITEMS = 25;
    private GridLayout mGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board5_x5);

        mGrid = (GridLayout) findViewById(R.id.grid_layout);
        mGrid.setOnDragListener(new DragListener());
        final LayoutInflater inflater = LayoutInflater.from(this);
        for (int i = 0; i < NBR_ITEMS; i++) {
            //Adding the items dynamically into the grid.
            final View itemView = inflater.inflate(R.layout.grid_item, mGrid, false);
            final TextView text = (TextView) itemView.findViewById(R.id.text);
            text.setText(String.valueOf(i + 1));
            itemView.setOnLongClickListener(new LongPressListener());
            mGrid.addView(itemView);
        }
    }


    private int calculateNewIndex(float x, float y) {
        //calculating the new column of the item
        final float cellWidth = mGrid.getWidth() / mGrid.getColumnCount();
        final int column = (int)(x / cellWidth);
        //calculating the new row of the item
        final float cellHeight = mGrid.getHeight() / mGrid.getRowCount();
        final int row = (int)Math.floor(y / cellHeight);

        // the items in the GridLayout is organized as a wrapping list
        // and not as an actual grid, so this is how to get the new index
        int index = row * mGrid.getColumnCount() + column;
        return index;
    }

    private class DragListener implements View.OnDragListener {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            final View view = (View) event.getLocalState();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_LOCATION:
                    // do nothing if hovering above own position
                    if (view == v) return true;
                    // get the old list index
                    int oldIndex = mGrid.indexOfChild(view);
                    // get the new list index
                    final int index = calculateNewIndex(event.getX(), event.getY());
                    //check the type of moving the view
                    if(Math.abs(index - oldIndex) != 1 && Math.abs(index - oldIndex) != mGrid.getColumnCount() && index != oldIndex){

                    }else if(Math.abs(index - oldIndex) == 1){//A row spinning!
                        int row = oldIndex / mGrid.getColumnCount();
                        int dir = index - oldIndex;
                        if(dir == 1){//A right spin
                            View last = mGrid.getChildAt(row*mGrid.getColumnCount() + mGrid.getColumnCount() - 1);
                            for(int i =  row*mGrid.getColumnCount() + mGrid.getColumnCount() - 2 ; i >= row*mGrid.getColumnCount() ; i--){
                                View current = mGrid.getChildAt(i);
                                mGrid.removeView(current);
                                mGrid.addView(current,i+1);
                            }
                            mGrid.removeView(last);
                            mGrid.addView(last,row*mGrid.getColumnCount());
                        }else{//A left spin
                            View last = mGrid.getChildAt(row*mGrid.getColumnCount());
                            for(int i = row*mGrid.getColumnCount() + 1; i < row*mGrid.getColumnCount() + mGrid.getColumnCount() ; i++){
                                View current = mGrid.getChildAt(i);
                                mGrid.removeView(current);
                                mGrid.addView(current,i-1);
                            }
                            mGrid.removeView(last);
                            mGrid.addView(last,(row + 1)*mGrid.getColumnCount() - 1);
                        }
                    }else if(Math.abs(index - oldIndex) == mGrid.getColumnCount()){//A column spinning!
                        int col = oldIndex % mGrid.getColumnCount();
                        int dir = (index - oldIndex) / mGrid.getColumnCount();
                        if(dir == 1){//A down spin
                            View last = mGrid.getChildAt((mGrid.getColumnCount() - 1)*mGrid.getColumnCount() + col);
                            for(int i = (mGrid.getColumnCount() - 2)*mGrid.getColumnCount() + col ; i >= col; i -= mGrid.getColumnCount()){
                                View current = mGrid.getChildAt(i);
                                mGrid.removeView(current);
                                mGrid.addView(current,i + mGrid.getColumnCount() - 1);
                            }
                            mGrid.removeView(last);
                            mGrid.addView(last,col);
                        }else{//An un spin
                            View last = mGrid.getChildAt(col);
                            for(int i = mGrid.getColumnCount() + col; i <  mGrid.getColumnCount() * mGrid.getColumnCount() + col; i += mGrid.getColumnCount()){
                                View current = mGrid.getChildAt(i);
                                mGrid.removeView(current);
                                mGrid.addView(current,i - mGrid.getColumnCount() + 1);
                            }
                            mGrid.removeView(last);
                            mGrid.addView(last,(mGrid.getColumnCount() - 1) * mGrid.getColumnCount() + col);
                        }
                    }
                    break;
                case DragEvent.ACTION_DROP:
                    view.setVisibility(View.VISIBLE);
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    if (!event.getResult()) {
                        view.setVisibility(View.VISIBLE);
                    }
                    break;
            }
            return true;
        }
    }


    private class LongPressListener implements View.OnLongClickListener {
        @Override
        public boolean onLongClick(View view) {
            final ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder();
            view.startDrag(data, shadowBuilder, view, 0);
            return true;
        }
    }
}