package com.okunev.numberpickerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import static android.view.Gravity.CENTER;

/**
 * Project NumberPicker. Created by gwa on 12/21/16.
 */

public class NumberPickerView extends RelativeLayout {
    private Integer elements = 6;
    private Integer elementsTextSize = 13;

    private Integer defaultColor = Color.BLACK;
    private Integer defaultTextColor = Color.BLACK;

    private Integer highlightedColor = Color.BLACK;
    private Integer highlightedTextColor = Color.BLACK;

    private Boolean hasHeader;
    private Integer headerTextSize = 13;
    private Integer headerTextColor = Color.BLACK;
    private Boolean hasFooter;
    private Boolean isMultiselectable;
    private Integer elementBackground = -1;

    private TextView header;
    private LinearLayout picker;
    private RelativeLayout main;
    private Context context;
    private HashMap<Integer, Integer> values = new HashMap<>();
    private ArrayList<Integer> elementsTags = new ArrayList<>();
    OnChosenNumberListener onChosenNumberListener;

    private String plural;
    private String single;

    private Integer lastChecked = 1;
    private Integer maxSelectableItem = -1;


    public NumberPickerView(Context context) {
        super(context);
        init(context);
    }

    public NumberPickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.NumberPickerView, 0, 0);
        elements = a.getInteger(R.styleable.NumberPickerView_elements, 3);
        defaultColor = a.getInteger(R.styleable.NumberPickerView_defaultColor,
                ContextCompat.getColor(context, R.color.default_color));
        defaultTextColor = a.getInteger(R.styleable.NumberPickerView_defaultTextColor,
                ContextCompat.getColor(context, R.color.elements_text_color));
        elementsTextSize = a.getDimensionPixelSize(R.styleable.NumberPickerView_elementsTextSize, 12);
        hasHeader = a.getBoolean(R.styleable.NumberPickerView_hasHeader, true);
        hasFooter = a.getBoolean(R.styleable.NumberPickerView_hasFooter, false);
        isMultiselectable = a.getBoolean(R.styleable.NumberPickerView_isMultiselectable, true);
        a.recycle();
        init(context);
    }

    public NumberPickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.NumberPickerView, 0, 0);
        elements = a.getInteger(R.styleable.NumberPickerView_elements, 3);
        defaultColor = a.getInteger(R.styleable.NumberPickerView_defaultColor,
                ContextCompat.getColor(context, R.color.default_color));
        defaultTextColor = a.getInteger(R.styleable.NumberPickerView_defaultTextColor,
                ContextCompat.getColor(context, R.color.elements_text_color));
        elementsTextSize = a.getDimensionPixelSize(R.styleable.NumberPickerView_elementsTextSize, 12);
        hasHeader = a.getBoolean(R.styleable.NumberPickerView_hasHeader, true);
        headerTextSize = a.getDimensionPixelSize(R.styleable.NumberPickerView_headerTextSize, 15);
        headerTextColor = a.getColor(R.styleable.NumberPickerView_defaultTextColor,
                ContextCompat.getColor(context, R.color.header_text_color));
        hasFooter = a.getBoolean(R.styleable.NumberPickerView_hasFooter, false);
        isMultiselectable = a.getBoolean(R.styleable.NumberPickerView_isMultiselectable, true);
        a.recycle();
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public NumberPickerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.NumberPickerView, 0, 0);
        elements = a.getInteger(R.styleable.NumberPickerView_elements, 3);
        defaultColor = a.getInteger(R.styleable.NumberPickerView_defaultColor,
                ContextCompat.getColor(context, R.color.default_color));
        defaultTextColor = a.getInteger(R.styleable.NumberPickerView_defaultTextColor,
                ContextCompat.getColor(context, R.color.elements_text_color));
        elementsTextSize = a.getDimensionPixelSize(R.styleable.NumberPickerView_elementsTextSize, 12);
        hasHeader = a.getBoolean(R.styleable.NumberPickerView_hasHeader, true);
        hasFooter = a.getBoolean(R.styleable.NumberPickerView_hasFooter, false);
        isMultiselectable = a.getBoolean(R.styleable.NumberPickerView_isMultiselectable, true);
        a.recycle();
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_number_picker, this, true);
        header = (TextView) findViewById(R.id.header);
        picker = (LinearLayout) findViewById(R.id.numbers_view);
        main = (RelativeLayout) findViewById(R.id.main_container);
    }


    private String getName(Integer number) {
        return number.toString().endsWith("1") ? single : plural;
    }

    public void build() {
        header.setVisibility(hasHeader ? VISIBLE : GONE);
        header.setText("0" + " " + getName(0));
        header.setTextSize(headerTextSize);
        header.setTextColor(headerTextColor);
        if (elementsTags.size() == elements)
            for (int i = 0; i < elements; i++) {
                picker.addView(getNewBet(elementsTags.get(i)));
            }
        else
            for (int i = 0; i < elements; i++) {
                picker.addView(getNewBet(i + 1));
            }
        picker.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float x = event.getX();
                float y = event.getY();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        handleTap(x, y);
                        break;
                    case MotionEvent.ACTION_UP:
                        handleTap(x, y);
                        break;
                }
                return true;
            }
        });
        picker.post(new Runnable() {
            @Override
            public void run() {
                setupFooters();
                maxSelectableItem = maxSelectedBetContainedInTagsList(maxSelectableItem);
                setSelectedId(lastChecked);
            }
        });

    }

    private void handleTap(float x, float y) {
        for (int k = 0; k < picker.getChildCount(); k++) {
            if (Helpers.isPointInsideView(x, y, picker.getChildAt(k))) {
                setSelectedId(k + 1);
            }
        }
    }

    private void setupFooters() {
        for (Integer footerText : values.keySet()) {
            TextView footer = new TextView(context);
            footer.setTextSize(elementsTextSize);
            footer.setTextColor(defaultColor);
            LayoutParams params;
            int width = Helpers.getWidthOfTextInPx(context, String.valueOf(values.get(footerText)));
            int center = Math.round(picker.getChildAt(footerText).getX() + picker.getChildAt(footerText).getWidth() / 2);
            int leftEdge = Math.round(picker.getX());
            int rightEdge = Math.round(picker.getWidth());
            params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.BELOW, R.id.numbers_view);
            if (footerText != 0) {
                if (center - width / 2 < leftEdge) {//out of the left of the picker
                    footer.setX(leftEdge);
                } else if (center + width / 2 > rightEdge) {
                    footer.setX(rightEdge - width);
                } else {
                    footer.setX(center - 2 * width / 3 + Helpers.dpToPx(4));
                }
            } else
                footer.setX(leftEdge);
            footer.setLayoutParams(params);
            footer.setText(String.valueOf(values.get(footerText)));

            main.addView(footer);
        }
    }

    private void setHeaderPosition() {
        int width = Helpers.getWidthOfTextInPx(context, header.getText().toString());
        int center = Math.round(picker.getChildAt(lastChecked - 1).getX() + picker.getChildAt(lastChecked - 1).getWidth() / 2);
        int leftEdge = Math.round(picker.getX());
        int rightEdge = Math.round(picker.getWidth());
        LayoutParams params;
        if (center - 2 * width / 3 < leftEdge) {//out of the left of the picker
            params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            header.setX(leftEdge);
            header.setLayoutParams(params);
        } else if (center + 2 * width / 3 > rightEdge) {
            params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            header.setLayoutParams(params);
            header.setX(rightEdge - width * 1.1f);
        } else {
            params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            header.setLayoutParams(params);
            header.setX(center - 2 * width / 3);
        }
        header.requestLayout();
    }

    private TextView getNewBet(final int id) {
        final TextView bet = new TextView(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, Helpers.dpToPx(45), 1f);
        bet.setLayoutParams(params);
        bet.post(new Runnable() {
            @Override
            public void run() {
                if (!hasFooter)
                    if (bet.getWidth() > Helpers.getWidthOfTextInPx(context, String.valueOf(elements))) {
                        bet.setText(String.valueOf(id));
                    }
            }
        });
        bet.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        bet.setTextColor(defaultTextColor);
        bet.setId(1000 + id);
        bet.setTag(id);
        bet.setGravity(CENTER);
        if (elementBackground != -1) {
            bet.setBackgroundResource(elementBackground);
        } else {
            bet.setBackgroundResource(R.drawable.bg_element);
        }
        GradientDrawable drawable = (GradientDrawable) bet.getBackground();
        drawable.setColor(defaultColor);
        return bet;
    }

    int maxSelectedBetContainedInTagsList(int maxSelectableItem) {
        if (elementsTags.indexOf(maxSelectableItem) == -1 && maxSelectableItem != -1)
            return maxSelectedBetContainedInTagsList(maxSelectableItem - 1);
        else
            return maxSelectableItem;
    }

    public void setSelectedId(Integer n) {
        if (maxSelectableItem != -1 && elementsTags.get(n - 1) > maxSelectableItem)
            n = elementsTags.indexOf(maxSelectableItem) + 1;
        if (isMultiselectable) {
            if (lastChecked > n) {
                for (int k = lastChecked; k > n; k--) {
                    TextView bet = (TextView) picker.getChildAt(k - 1);
                    bet.setTextColor(defaultTextColor);
                    GradientDrawable drawable1 = (GradientDrawable) bet.getBackground();
                    drawable1.setColor(defaultColor);
                }
            } else {
                for (int i = 0; i < n; i++) {
                    TextView bet = (TextView) picker.getChildAt(i);
                    bet.setTextColor(highlightedTextColor);
                    GradientDrawable drawable = (GradientDrawable) bet.getBackground();
                    drawable.setColor(highlightedColor);
                }
            }
        } else {
            selectOneView((TextView) picker.getChildAt(lastChecked - 1), (TextView) picker.getChildAt(n - 1));
        }
        lastChecked = n;
        if (onChosenNumberListener != null)
            onChosenNumberListener.OnChosenNumber(elementsTags.size() == elements ? elementsTags.get(lastChecked - 1) : lastChecked);
        header.setText(elementsTags.size() == elements ? "" + elementsTags.get(lastChecked - 1) + " "
                + getName(elementsTags.get(lastChecked - 1)).toLowerCase() : "" + lastChecked + " "
                + getName(lastChecked).toLowerCase());
        setHeaderPosition();

    }

    void selectOneView(TextView v1, TextView v2) {
        v1.setTextColor(defaultTextColor);
        GradientDrawable drawable1 = (GradientDrawable) v1.getBackground();
        drawable1.setColor(defaultColor);

        v2.setTextColor(highlightedTextColor);
        GradientDrawable drawable = (GradientDrawable) v2.getBackground();
        drawable.setColor(highlightedColor);
    }

    public NumberPickerView withNumberOfElements(Integer elements) {
        this.elements = elements;
        return this;
    }

    public NumberPickerView withHeaderTemplate(String single, String plural) {
        this.hasHeader = true;
        this.single = single;
        this.plural = plural;
        return this;
    }

    public NumberPickerView withElementsTextSizeInPx(Integer size) {
        this.elementsTextSize = size;
        return this;
    }

    public NumberPickerView withHeaderTextSizeInPx(Integer size) {
        this.headerTextSize = size;
        return this;
    }

    public NumberPickerView withHighLightedTextColor(Integer highLightedTextColor) {
        this.highlightedTextColor = highLightedTextColor;
        return this;
    }

    public NumberPickerView withHighLightedColor(Integer highLightedColor) {
        this.highlightedColor = highLightedColor;
        return this;
    }

    public NumberPickerView withDefaultTextColor(Integer defTextColor) {
        this.defaultTextColor = defTextColor;
        return this;
    }

    public NumberPickerView withHeaderTextColor(Integer headerTextColor) {
        this.headerTextColor = headerTextColor;
        return this;
    }

    public NumberPickerView withDefaultColor(Integer defaultColor) {
        this.defaultColor = defaultColor;
        return this;
    }

    /**
     * @param values key = element position, value = footer text
     **/
    public NumberPickerView withFooterValues(HashMap<Integer, Integer> values) {
        this.hasFooter = true;
        this.values = values;
        return this;
    }

    public NumberPickerView withElementsTags(ArrayList<Integer> tags) {
        this.elementsTags = tags;
        return this;
    }

    public NumberPickerView withMaxSelectableItem(Integer number) {
        this.maxSelectableItem = number;
        return this;
    }

    public NumberPickerView withMultipleSelect(Boolean multipleSelect) {
        this.isMultiselectable = multipleSelect;
        return this;
    }

    public NumberPickerView withElementBackgroundResource(Integer elementBackground) {
        this.elementBackground = elementBackground;
        return this;
    }


    public int getCurrentNumber() {
        return lastChecked;
    }

    public interface OnChosenNumberListener {
        void OnChosenNumber(Integer selectedNumber);
    }

    public NumberPickerView withOnChosenNumberListener(OnChosenNumberListener listener) {
        this.onChosenNumberListener = listener;
        return this;
    }

    @Override
    public Parcelable onSaveInstanceState() {
        //begin boilerplate code that allows parent classes to save state
        Parcelable superState = super.onSaveInstanceState();

        SavedState ss = new SavedState(superState);
        //end

        ss.stateToSave = this.lastChecked;

        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        //end

        this.lastChecked = ss.stateToSave;
        setSelectedId(lastChecked);
    }

    static class SavedState extends BaseSavedState {
        int stateToSave;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.stateToSave = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.stateToSave);
        }

        //required field that makes Parcelables from a Parcel
        public static final Creator<SavedState> CREATOR =
                new Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }
}
