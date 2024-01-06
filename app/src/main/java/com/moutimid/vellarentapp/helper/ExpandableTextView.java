package com.moutimid.vellarentapp.helper;

import android.content.Context;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

public class ExpandableTextView extends androidx.appcompat.widget.AppCompatTextView {
    private static final int MAX_LINES_COLLAPSED = 5;
    private static final int MAX_LINES_EXPANDED = Integer.MAX_VALUE;

    private boolean isExpanded = false;

    public ExpandableTextView(Context context) {
        super(context);
        initialize();
    }

    public ExpandableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public ExpandableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    private void initialize() {
        ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ViewTreeObserver observer = getViewTreeObserver();
                observer.removeOnGlobalLayoutListener(this);

                if (getLineCount() > MAX_LINES_COLLAPSED) {
                    setMaxLines(MAX_LINES_COLLAPSED);
                }
            }
        };

        getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListener);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isExpanded) {
                    setMaxLines(MAX_LINES_COLLAPSED);
                } else {
                    setMaxLines(MAX_LINES_EXPANDED);
                    setEllipsize(null);
                }

                isExpanded = !isExpanded;
            }
        });
    }
}
