package com.example.irad9731.loopdeloopover;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayout;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Random;

public abstract class BoardLogic extends AppCompatActivity {
    protected GridLayout mGrid;
    protected TextView mClock;
    public long start_time;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();

    private ClockThread clockThread;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        start_time = System.currentTimeMillis();
        clockThread = new ClockThread();
        clockThread.start();
    }

    protected int calculateNewIndex(float x, float y) {
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


    protected boolean isGameFinished(){
        boolean flag = true;
        for(int i=0;i<(mGrid.getRowCount()*mGrid.getColumnCount() -1) && flag;i++){
            View current = mGrid.getChildAt(i);
            View next = mGrid.getChildAt(i+1);
            final TextView currentText = (TextView) current.findViewById(R.id.text);
            final TextView nextText = (TextView) next.findViewById(R.id.text);
            int currentId = Integer.parseInt(currentText.getText().toString());
            int nextId = Integer.parseInt(nextText.getText().toString());
            flag = flag && (currentId == (nextId - 1));
        }
        return flag;
    }


    public void setArrayPrefs(String arrayName, ArrayList<Integer> array) {
        String fileName = this.getClass().getSimpleName();
        SharedPreferences prefs = getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(arrayName +"_size", array.size());
        for(int i=0;i<array.size();i++)
            editor.putInt(arrayName + "_" + i, array.get(i));
        editor.apply();
    }

    public ArrayList<Integer> getArrayPrefs(String arrayName) {
        String fileName = this.getClass().getSimpleName();
        SharedPreferences prefs = getSharedPreferences(fileName, Context.MODE_PRIVATE);
        int size = prefs.getInt(arrayName + "_size", 0);
        ArrayList<Integer> array = new ArrayList<>(size);
        for(int i=0;i<size;i++)
            array.add(prefs.getInt(arrayName + "_" + i,0));
        return array;
    }

    public void removeArrayFromPref(String arrayName) {
        String fileName = this.getClass().getSimpleName();
        SharedPreferences prefs = getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        int size = prefs.getInt(arrayName+"_size",0);
        editor.remove(arrayName + "_size");
        for (int i = 0; i < size; i++){
            editor.remove(arrayName + "_" + i);
        }
        editor.remove("startTime");
        editor.apply();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(!isGameFinished()) {
            ArrayList<Integer> currentState = new ArrayList<>();
            for (int i = 0; i < mGrid.getColumnCount() * mGrid.getRowCount(); i++) {
                View current = mGrid.getChildAt(i);
                final TextView currentText = (TextView) current.findViewById(R.id.text);
                currentState.add(Integer.parseInt(currentText.getText().toString()));
            }
            String s = this.getClass().getSimpleName();
            SharedPreferences sharedPreferences = getSharedPreferences(s,Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putLong("startTime",start_time);
            editor.apply();
            setArrayPrefs("gameState", currentState);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        ArrayList<Integer> state = getArrayPrefs("gameState");

        String s = this.getClass().getSimpleName();
        if(s.equals(Board5X5Activity.class.getSimpleName())){
            s="remember5X5";
        }else if(s.equals(Board7X7Activity.class.getSimpleName())){
            s="remember7X7";
        }
        else if(s.equals(Board9X9Activity.class.getSimpleName())){
            s="remember9X9";
        }
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.irad9731.loopdeloopover_preferences",Context.MODE_PRIVATE);
        boolean f = sharedPreferences.getBoolean(s,true);
        if(f && !state.isEmpty() && state.size() == (mGrid.getChildCount())){
            mGrid.removeAllViews();
            final LayoutInflater inflater = LayoutInflater.from(this);
            for (int i = 0; i < state.size(); i++) {
                //Adding the items dynamically into the grid.
                final View itemView = inflater.inflate(R.layout.grid_item, mGrid, false);
                final TextView text = (TextView) itemView.findViewById(R.id.text);
                text.setText(String.valueOf(state.get(i)));
                itemView.setOnLongClickListener(new LongPressListener());
                mGrid.addView(itemView);
            }
            sharedPreferences = getSharedPreferences(this.getClass().getSimpleName(),Context.MODE_PRIVATE);
            start_time = sharedPreferences.getLong("startTime",System.currentTimeMillis());
            removeArrayFromPref("gameState");
        }else{
            int num=mGrid.getChildCount();
            mGrid.removeAllViews();
            int[] content = new int[num];

            for(int i=0;i<num;i++){
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
            for (int i = 0; i < num; i++) {
                //Adding the items dynamically into the grid.
                final View itemView = inflater.inflate(R.layout.grid_item, mGrid, false);
                final TextView text = (TextView) itemView.findViewById(R.id.text);
                text.setText(String.valueOf(content[i]));
                itemView.setOnLongClickListener(new LongPressListener());
                mGrid.addView(itemView);
            }
            start_time=System.currentTimeMillis();
            clockThread = new ClockThread();
            clockThread.start();
        }

        //clockThread.start();
    }

    protected class DragListener implements View.OnDragListener {
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
                    if(Math.abs(index - oldIndex) != 1 && Math.abs(index - oldIndex) != mGrid.getColumnCount() && index != oldIndex){//Illegal spin!
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
                    boolean ended = isGameFinished();
                    if(ended){
                        removeArrayFromPref("gameState");
                        Intent i = new Intent(BoardLogic.this,GameFinishedActivity.class);
                        i.putExtra("level",BoardLogic.this.getClass().getSimpleName().substring(1 + BoardLogic.this.getClass().getSimpleName().indexOf('d'),BoardLogic.this.getClass().getSimpleName().lastIndexOf('A')));
                        i.putExtra("beatingTime",System.currentTimeMillis() - start_time);
                        startActivityForResult(i,1);
                    }
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        setResult(RESULT_OK);
        finish();
    }

    public class MyDragShadowBuilder extends View.DragShadowBuilder {

        @Override
        public void onProvideShadowMetrics(Point outShadowSize, Point outShadowTouchPoint) {
            outShadowSize.set(1,1);
            outShadowTouchPoint.set(0,0);
        }
    }


    protected class LongPressListener implements View.OnLongClickListener {
        @Override
        public boolean onLongClick(View view) {
            try {

                final ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new MyDragShadowBuilder();
                view.startDragAndDrop(data, shadowBuilder, view, 0);
            }catch (Exception e){
                Log.e("asd",e.getStackTrace().toString());
            }
            return true;
        }

    }

    public class ClockThread extends Thread{
        @Override
        public void run() {
            try {
                while (true) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mClock != null) {
                                long time = System.currentTimeMillis();
                                long timeFromStart = time - start_time;
                                long timeInSeconds = timeFromStart / 1000;
                                long min = timeInSeconds / 60;
                                long seconds = timeInSeconds % 60;
                                String m = String.valueOf(min);
                                m = (m.length() > 1) ? m : "0" + m;
                                String s = String.valueOf(seconds);
                                s = (s.length() > 1) ? s : "0" + s;
                                final String CLOCK = m + ":" + s;
                                mClock.setText(CLOCK);
                            }
                        }
                    });
                    sleep(1000);
                }
            }catch (Exception e){
                e.printStackTrace();
                Log.e("Clock error",e.getMessage());
            }

        }
    }

}
