package com.shivam.bottomnavigation;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.shivam.bottomnavigation.R;

import java.util.ArrayList;
import java.util.List;

public class ShivamBottomNavigation extends FrameLayout {

    public interface OnItemSelectedListener {
        void onItemSelected(int itemId);
    }

    private static final long ANIMATION_DURATION = 400L;

    private FrameLayout container;
    private LinearLayout itemsContainer;
    private View indicator;

    private final List<View> itemViews = new ArrayList<>();
    private final List<ImageView> itemIcons = new ArrayList<>();
    private final List<TextView> itemTitles = new ArrayList<>();
    private final List<MenuItem> menuItems = new ArrayList<>();

    private OnItemSelectedListener listener;

    private int selectedItemId = View.NO_ID;
    private int selectedPosition = -1;

    private int menuResource;

    private int iconSize;
    private int iconColor;
    private int selectedIconColor;

    private int textSize;
    private int textColor;
    private int selectedTextColor;

    // 0 = none, 1 = selected, 2 = all
    private int textVisibility = 1;

    private int barColor;
    private float barRadius;
    private Drawable barBackground;

    private int indicatorColor;
    private float indicatorRadius;
    private Drawable indicatorBackground;

    private boolean animationEnabled = true;
    private boolean indicatorInitialized = false;

    private int indicatorMarginStart;
    private int indicatorMarginTop;
    private int indicatorMarginEnd;
    private int indicatorMarginBottom;

    private int indicatorPaddingStart;
    private int indicatorPaddingTop;
    private int indicatorPaddingEnd;
    private int indicatorPaddingBottom;

    private static final int ITEM_WIDTH_DP = 68;

    private ValueAnimator itemWeightAnimator;

    private static final float SELECTED_ITEM_WEIGHT = 1.8f;
    private static final float NORMAL_ITEM_WEIGHT = 1.0f;
    private static final int EDGE_ITEM_PADDING_DP = 14;

    public ShivamBottomNavigation(Context context) {
        super(context);
        init(context, null);
    }

    public ShivamBottomNavigation(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ShivamBottomNavigation(Context context, AttributeSet attrs,
                                  int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        iconSize = dp(24);
        textSize = sp(12);

        iconColor = Color.GRAY;
        selectedIconColor = Color.WHITE;

        textColor = Color.GRAY;
        selectedTextColor = Color.WHITE;

        barColor = Color.parseColor("#191919");
        barRadius = dp(32);

        indicatorColor = Color.parseColor("#E83E8C");
        indicatorRadius = dp(100);

        readAttributes(context, attrs);

        createNavigationView(context);

        updateBarBackground();

        if (menuResource != 0) {
            setMenu(menuResource);
        }
    }

    private void readAttributes(Context context, AttributeSet attrs) {

        if (attrs == null) return;

        TypedArray a = context.obtainStyledAttributes(
                attrs,
                R.styleable.CustomBottomNavigation
        );

        menuResource = a.getResourceId(
                R.styleable.CustomBottomNavigation_cbnMenu, 0
        );

        iconSize = a.getDimensionPixelSize(
                R.styleable.CustomBottomNavigation_cbnIconSize,
                dp(24)
        );

        iconColor = a.getColor(
                R.styleable.CustomBottomNavigation_cbnIconColor,
                Color.GRAY
        );

        selectedIconColor = a.getColor(
                R.styleable.CustomBottomNavigation_cbnSelectedIconColor,
                Color.WHITE
        );

        textSize = a.getDimensionPixelSize(
                R.styleable.CustomBottomNavigation_cbnTextSize,
                sp(12)
        );

        textColor = a.getColor(
                R.styleable.CustomBottomNavigation_cbnTextColor,
                Color.GRAY
        );

        selectedTextColor = a.getColor(
                R.styleable.CustomBottomNavigation_cbnSelectedTextColor,
                Color.WHITE
        );

        textVisibility = a.getInt(
                R.styleable.CustomBottomNavigation_cbnTextVisibility,
                1
        );

        barColor = a.getColor(
                R.styleable.CustomBottomNavigation_cbnBarColor,
                barColor
        );

        barRadius = a.getDimension(
                R.styleable.CustomBottomNavigation_cbnBarRadius,
                barRadius
        );

        barBackground = a.getDrawable(
                R.styleable.CustomBottomNavigation_cbnBarBackground
        );

        indicatorColor = a.getColor(
                R.styleable.CustomBottomNavigation_cbnIndicatorColor,
                indicatorColor
        );

        indicatorRadius = a.getDimension(
                R.styleable.CustomBottomNavigation_cbnIndicatorRadius,
                indicatorRadius
        );

        indicatorBackground = a.getDrawable(
                R.styleable.CustomBottomNavigation_cbnIndicatorBackground
        );

        animationEnabled = a.getBoolean(
                R.styleable.CustomBottomNavigation_cbnAnimationEnabled,
                true
        );

        indicatorMarginStart = a.getDimensionPixelSize(
                R.styleable.CustomBottomNavigation_cbnIndicatorMarginStart, 0
        );

        indicatorMarginTop = a.getDimensionPixelSize(
                R.styleable.CustomBottomNavigation_cbnIndicatorMarginTop, 0
        );

        indicatorMarginEnd = a.getDimensionPixelSize(
                R.styleable.CustomBottomNavigation_cbnIndicatorMarginEnd, 0
        );

        indicatorMarginBottom = a.getDimensionPixelSize(
                R.styleable.CustomBottomNavigation_cbnIndicatorMarginBottom, 0
        );

        indicatorPaddingStart = a.getDimensionPixelSize(
                R.styleable.CustomBottomNavigation_cbnIndicatorPaddingStart, 0
        );

        indicatorPaddingTop = a.getDimensionPixelSize(
                R.styleable.CustomBottomNavigation_cbnIndicatorPaddingTop, 0
        );

        indicatorPaddingEnd = a.getDimensionPixelSize(
                R.styleable.CustomBottomNavigation_cbnIndicatorPaddingEnd, 0
        );

        indicatorPaddingBottom = a.getDimensionPixelSize(
                R.styleable.CustomBottomNavigation_cbnIndicatorPaddingBottom, 0
        );

        a.recycle();
    }

    private void createNavigationView(Context context) {

        setClipChildren(false);
        setClipToPadding(false);

        container = new FrameLayout(context);
        container.setClipChildren(false);
        container.setClipToPadding(false);


        addView(container, new FrameLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
        ));

        // Indicator behind navigation items.
        indicator = new View(context);
        indicator.setClickable(false);
        indicator.setFocusable(false);
        indicator.setVisibility(View.INVISIBLE);

        FrameLayout.LayoutParams indicatorParams =
                new FrameLayout.LayoutParams(
                        0,
                        LayoutParams.MATCH_PARENT,
                        Gravity.TOP | Gravity.START
                );

        container.addView(indicator, indicatorParams);

        updateIndicatorBackground();

        // Navigation items.
        itemsContainer = new LinearLayout(context);
        itemsContainer.setOrientation(LinearLayout.HORIZONTAL);
        itemsContainer.setGravity(Gravity.CENTER_VERTICAL);
        itemsContainer.setClipChildren(false);
        itemsContainer.setClipToPadding(false);
        // Keep the item layout full-width. The indicator uses these bounds directly.
        itemsContainer.setPadding(0, 0, 0, 0);

        container.addView(itemsContainer, new FrameLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
        ));
    }

    // ---------------------------------------------------------
    // MENU
    // ---------------------------------------------------------

    public void setMenu(int menuRes) {

        menuResource = menuRes;

        if (menuRes == 0) return;

        if (itemWeightAnimator != null) {
            ValueAnimator oldAnimator = itemWeightAnimator;
            itemWeightAnimator = null;
            oldAnimator.cancel();
        }

        itemsContainer.removeAllViews();

        itemViews.clear();
        itemIcons.clear();
        itemTitles.clear();
        menuItems.clear();

        selectedPosition = -1;
        selectedItemId = View.NO_ID;
        indicatorInitialized = false;

        indicator.setVisibility(View.INVISIBLE);

        PopupMenu popupMenu = new PopupMenu(getContext(), this);
        Menu menu = popupMenu.getMenu();

        MenuInflater inflater = new MenuInflater(getContext());
        inflater.inflate(menuRes, menu);

        for (int i = 0; i < menu.size(); i++) {

            MenuItem menuItem = menu.getItem(i);

            if (!menuItem.isVisible()) continue;

            menuItems.add(menuItem);

            createMenuItem(menuItem, menuItems.size() - 1);
        }

        if (!menuItems.isEmpty()) {
            selectItem(0, false, false);
        }


    }

    /**
     * Returns a newly inflated Menu instance for the configured menu resource.
     * Mutating this returned Menu does not update the already-rendered navigation items.
     */
    public Menu getMenu() {
        PopupMenu popupMenu = new PopupMenu(getContext(), this);
        Menu menu = popupMenu.getMenu();
        if (menuResource != 0) {
            new MenuInflater(getContext()).inflate(menuResource, menu);
        }
        return menu;
    }

    private void createMenuItem(MenuItem menuItem, int position) {

        Context context = getContext();

        LinearLayout item = new LinearLayout(context);
        item.setOrientation(LinearLayout.VERTICAL);
        item.setGravity(Gravity.CENTER);
        item.setClickable(menuItem.isEnabled());
        item.setFocusable(menuItem.isEnabled());

        item.setContentDescription(menuItem.getTitle());

        LinearLayout.LayoutParams itemParams =
                new LinearLayout.LayoutParams(
                        0,
                        LayoutParams.MATCH_PARENT,
                        NORMAL_ITEM_WEIGHT
                );

        itemsContainer.addView(item, itemParams);

        // Icon.
        ImageView icon = new ImageView(context);

        Drawable iconDrawable = menuItem.getIcon();

        if (iconDrawable != null) {
            icon.setImageDrawable(iconDrawable.mutate());
        }

        icon.setScaleType(ImageView.ScaleType.FIT_CENTER);

        LinearLayout.LayoutParams iconParams =
                new LinearLayout.LayoutParams(
                        iconSize,
                        iconSize
                );

        item.addView(icon, iconParams);

        // Title.
        TextView title = new TextView(context);

        title.setText(menuItem.getTitle());

        title.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                textSize
        );

        title.setSingleLine(true);
        title.setGravity(Gravity.CENTER);
        title.setIncludeFontPadding(false);
        title.setMaxLines(1);
        title.setEllipsize(android.text.TextUtils.TruncateAt.END);

        LinearLayout.LayoutParams titleParams =
                new LinearLayout.LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT
                );

        titleParams.topMargin = dp(2);

        item.addView(title, titleParams);

        itemViews.add(item);

        // Keep the indicator aligned with the real selected-item bounds
        // whenever weighted layout changes during the expansion animation.
        item.addOnLayoutChangeListener((view, left, top, right, bottom,
                                        oldLeft, oldTop, oldRight, oldBottom) -> {
            if (position == selectedPosition) {
                updateIndicatorFromSelectedItem();
            }
        });

        itemIcons.add(icon);
        itemTitles.add(title);




        item.setOnClickListener(v -> {

            if (selectedPosition == position) return;

            selectItem(position, true, true);
        });

        updateItemStyle(position, false);
    }

    // ---------------------------------------------------------
    // SELECTION
    // ---------------------------------------------------------

    private void selectItem(int position,
                            boolean animate,
                            boolean notifyListener) {

        if (position < 0 || position >= itemViews.size()) return;

        selectedPosition = position;
        updateEdgeItemPadding(position);
        selectedItemId = menuItems.get(position).getItemId();

        for (int i = 0; i < itemViews.size(); i++) {

            boolean selected = i == position;

            itemViews.get(i).setSelected(selected);

            itemIcons.get(i).setSelected(selected);
            itemIcons.get(i).refreshDrawableState();

            updateItemStyle(i, selected);
        }

        animateItemSelection(position, animate);

        if (notifyListener && listener != null) {
            listener.onItemSelected(selectedItemId);
        }
    }

    private void updateItemStyle(int position, boolean selected) {

        ImageView icon = itemIcons.get(position);
        TextView title = itemTitles.get(position);

        int[][] states = new int[][] {
                new int[] { android.R.attr.state_selected },
                new int[] {}
        };

        int[] colors = new int[] {
                selectedIconColor,
                iconColor
        };

        icon.setImageTintList(
                new ColorStateList(states, colors)
        );

        title.setTextColor(
                selected ? selectedTextColor : textColor
        );

        if (textVisibility == 0) {

            title.setVisibility(View.GONE);

        } else if (textVisibility == 1) {

            title.setVisibility(
                    selected ? View.VISIBLE : View.GONE
            );

        } else {

            title.setVisibility(View.VISIBLE);
        }
    }

    // ---------------------------------------------------------
    // ITEM WIDTH + INDICATOR
    // ---------------------------------------------------------

    private void animateItemSelection(int position, boolean animate) {

        if (position < 0 || position >= itemViews.size()) return;

        if (itemWeightAnimator != null) {
            ValueAnimator oldAnimator = itemWeightAnimator;
            itemWeightAnimator = null;
            oldAnimator.cancel();
        }

        final int count = itemViews.size();
        final float[] startWeights = new float[count];
        final float[] endWeights = new float[count];

        for (int i = 0; i < count; i++) {
            LinearLayout.LayoutParams params =
                    (LinearLayout.LayoutParams) itemViews.get(i).getLayoutParams();

            startWeights[i] = params.weight > 0f
                    ? params.weight
                    : NORMAL_ITEM_WEIGHT;

            endWeights[i] = (i == position)
                    ? SELECTED_ITEM_WEIGHT
                    : NORMAL_ITEM_WEIGHT;


        }

        // Before the first layout, apply the final weights immediately.
        if (!animate || !animationEnabled || !itemsContainer.isLaidOut()) {
            applyItemWeights(endWeights);
            itemsContainer.requestLayout();
            itemsContainer.post(this::updateIndicatorFromSelectedItem);
            return;
        }

        itemWeightAnimator = ValueAnimator.ofFloat(0f, 1f);
        itemWeightAnimator.setDuration(ANIMATION_DURATION);
        itemWeightAnimator.setInterpolator(new DecelerateInterpolator(1.5f));

        itemWeightAnimator.addUpdateListener(animation -> {
            float progress = (float) animation.getAnimatedValue();
            float[] currentWeights = new float[count];

            for (int i = 0; i < count; i++) {
                currentWeights[i] = startWeights[i]
                        + (endWeights[i] - startWeights[i]) * progress;
            }

            applyItemWeights(currentWeights);
            // The indicator is updated from each item's actual layout bounds.
        });

        itemWeightAnimator.addListener(new android.animation.AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                if (animation != itemWeightAnimator) return;

                applyItemWeights(endWeights);
                itemsContainer.post(ShivamBottomNavigation.this::updateIndicatorFromSelectedItem);
                itemWeightAnimator = null;
            }

            @Override
            public void onAnimationCancel(android.animation.Animator animation) {
                if (animation == itemWeightAnimator) {
                    itemWeightAnimator = null;
                }
            }
        });

        itemWeightAnimator.start();
    }


    private void applyItemWeights(float[] weights) {

        int count = Math.min(itemViews.size(), weights.length);

        for (int i = 0; i < count; i++) {
            View item = itemViews.get(i);

            LinearLayout.LayoutParams params =
                    (LinearLayout.LayoutParams) item.getLayoutParams();

            if (params.width != 0 || params.weight != weights[i]) {
                params.width = 0;
                params.weight = weights[i];
                item.setLayoutParams(params);
            }
        }
    }


    /**
     * Positions the indicator from the selected item's real measured bounds.
     * This avoids rounding errors caused by calculating weighted widths manually.
     */
    private void updateIndicatorFromSelectedItem() {

        if (selectedPosition < 0 || selectedPosition >= itemViews.size()) return;
        if (container == null || itemsContainer == null) return;

        View selectedView = itemViews.get(selectedPosition);

        if (selectedView.getWidth() <= 0 || container.getHeight() <= 0) return;

        boolean isRtl = getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;

        int physicalLeftMargin = isRtl
                ? indicatorMarginEnd
                : indicatorMarginStart;

        int physicalRightMargin = isRtl
                ? indicatorMarginStart
                : indicatorMarginEnd;

        int targetWidth = selectedView.getWidth()
                - physicalLeftMargin
                - physicalRightMargin;

        int targetHeight = container.getHeight()
                - indicatorMarginTop
                - indicatorMarginBottom;

        if (targetWidth <= 0 || targetHeight <= 0) return;

        // Item coordinates are relative to itemsContainer; indicator coordinates
        // are relative to container. Include the itemsContainer's own left offset.
        float targetX = itemsContainer.getLeft()
                + selectedView.getLeft()
                + physicalLeftMargin;

        FrameLayout.LayoutParams params =
                (FrameLayout.LayoutParams) indicator.getLayoutParams();

        boolean layoutParamsChanged =
                params.width != targetWidth ||
                        params.height != targetHeight ||
                        params.topMargin != indicatorMarginTop;

        if (layoutParamsChanged) {
            params.width = targetWidth;
            params.height = targetHeight;
            params.leftMargin = 0;
            params.rightMargin = 0;
            params.topMargin = indicatorMarginTop;
            params.gravity = Gravity.TOP | Gravity.START;
            indicator.setLayoutParams(params);
        }

        indicator.setTranslationX(targetX);
        indicator.setTranslationY(0f);
        indicator.setScaleX(1f);
        indicator.setScaleY(1f);
        indicator.setAlpha(1f);
        indicator.setVisibility(View.VISIBLE);
        indicatorInitialized = true;
    }


    // ---------------------------------------------------------
    // BACKGROUNDS
    // ---------------------------------------------------------

    private void updateBarBackground() {

        if (barBackground != null) {

            Drawable drawable = barBackground.getConstantState() != null
                    ? barBackground.getConstantState()
                    .newDrawable(getResources()).mutate()
                    : barBackground.mutate();

            setBackground(drawable);

        } else {

            GradientDrawable drawable = new GradientDrawable();

            drawable.setColor(barColor);
            drawable.setCornerRadius(barRadius);

            setBackground(drawable);
        }

        setClipToOutline(true);
    }

    private void updateIndicatorBackground() {

        if (indicator == null) return;

        Drawable drawable;

        if (indicatorBackground != null) {

            drawable = indicatorBackground.getConstantState() != null
                    ? indicatorBackground.getConstantState()
                    .newDrawable(getResources()).mutate()
                    : indicatorBackground.mutate();

        } else {

            GradientDrawable shape = new GradientDrawable();

            shape.setColor(indicatorColor);
            shape.setAlpha(50);
            shape.setCornerRadius(indicatorRadius);

            drawable = shape;
        }

        boolean isRtl =
                getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;

        int left = isRtl
                ? indicatorPaddingEnd
                : indicatorPaddingStart;

        int right = isRtl
                ? indicatorPaddingStart
                : indicatorPaddingEnd;

        InsetDrawable insetDrawable = new InsetDrawable(
                drawable,
                left,
                indicatorPaddingTop,
                right,
                indicatorPaddingBottom
        );

        indicator.setBackground(insetDrawable);
    }

    private void updateEdgeItemPadding(int selectedPosition) {

        int count = itemViews.size();
        if (count < 2) return;

        int padding = dp(EDGE_ITEM_PADDING_DP);

        boolean isFirstSelected = selectedPosition == 0;
        boolean isLastSelected = selectedPosition == count - 1;

        for (int i = 0; i < count; i++) {

            View item = itemViews.get(i);

            int startPadding = 0;
            int endPadding = 0;

            // First item selected: last item gets end padding.
            if (isFirstSelected && i == count - 1) {
                endPadding = padding;
            }

            // Last item selected: first item gets start padding.
            else if (isLastSelected && i == 0) {
                startPadding = padding;
            }

            // Middle item selected: both edge items get padding.
            else if (!isFirstSelected && !isLastSelected) {

                if (i == 0) {
                    startPadding = padding;
                }

                if (i == count - 1) {
                    endPadding = padding;
                }
            }

            if (getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
                item.setPadding(endPadding, 0, startPadding, 0);
            } else {
                item.setPadding(startPadding, 0, endPadding, 0);
            }
        }
    }

    // ---------------------------------------------------------
    // PUBLIC METHODS
    // ---------------------------------------------------------

    public void setOnItemSelectedListener(
            OnItemSelectedListener listener) {

        this.listener = listener;
    }

    public void setSelectedItemId(int itemId) {

        for (int i = 0; i < menuItems.size(); i++) {

            if (menuItems.get(i).getItemId() == itemId) {

                selectItem(i, true, false);
                return;
            }
        }
    }

    public int getSelectedItemId() {
        return selectedItemId;
    }

    /** Returns the currently selected menu position, or -1 when nothing is selected. */
    public int getSelectedItemPosition() {
        return selectedPosition;
    }

    /** Selects a menu item by its zero-based position. */
    public void setSelectedItemPosition(int position) {
        selectItem(position, true, false);
    }

    public void setAnimationEnabled(boolean enabled) {
        animationEnabled = enabled;
    }

    public void setIconSize(int sizeDp) {

        iconSize = dp(sizeDp);

        for (ImageView icon : itemIcons) {

            LinearLayout.LayoutParams params =
                    (LinearLayout.LayoutParams)
                            icon.getLayoutParams();

            params.width = iconSize;
            params.height = iconSize;

            icon.setLayoutParams(params);
        }
    }

    public void setTextVisibility(int visibility) {

        // 0 = none, 1 = selected, 2 = all
        textVisibility = visibility;

        for (int i = 0; i < itemTitles.size(); i++) {
            updateItemStyle(i, i == selectedPosition);
        }
    }

    public void setBarColor(int color) {

        barColor = color;
        barBackground = null;

        updateBarBackground();
    }

    public void setIndicatorColor(int color) {

        indicatorColor = color;
        indicatorBackground = null;

        updateIndicatorBackground();
    }

    public void setIndicatorBackground(Drawable drawable) {

        indicatorBackground = drawable;
        updateIndicatorBackground();
    }

    public void setBarBackground(Drawable drawable) {

        barBackground = drawable;
        updateBarBackground();
    }

    public int getItemCount() {
        return menuItems.size();
    }

    public void setTextColor(int color) {

        textColor = color;

        for (int i = 0; i < itemTitles.size(); i++) {
            updateItemStyle(i, i == selectedPosition);
        }
    }

    public void setSelectedTextColor(int color) {

        selectedTextColor = color;

        for (int i = 0; i < itemTitles.size(); i++) {
            updateItemStyle(i, i == selectedPosition);
        }
    }

    public void setIconColor(int color) {

        iconColor = color;

        for (int i = 0; i < itemIcons.size(); i++) {
            updateItemStyle(i, i == selectedPosition);
        }
    }

    public void setSelectedIconColor(int color) {

        selectedIconColor = color;

        for (int i = 0; i < itemIcons.size(); i++) {
            updateItemStyle(i, i == selectedPosition);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h,
                                 int oldw, int oldh) {

        super.onSizeChanged(w, h, oldw, oldh);

        if (w != oldw || h != oldh) {

            if (selectedPosition >= 0 && !itemViews.isEmpty()) {

                float[] weights = new float[itemViews.size()];

                for (int i = 0; i < itemViews.size(); i++) {
                    weights[i] = (i == selectedPosition)
                            ? SELECTED_ITEM_WEIGHT
                            : NORMAL_ITEM_WEIGHT;
                }

                applyItemWeights(weights);

                itemsContainer.post(this::updateIndicatorFromSelectedItem);
            }
        }
    }

    private int dp(float value) {

        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                value,
                getResources().getDisplayMetrics()
        );
    }

    private int sp(float value) {

        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                value,
                getResources().getDisplayMetrics()
        );
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int itemCount = menuItems.size();

        int desiredWidth = dp(ITEM_WIDTH_DP * itemCount)
                + getPaddingLeft()
                + getPaddingRight();

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int measuredWidth;

        if (widthMode == MeasureSpec.EXACTLY) {

            measuredWidth = widthSize;

        } else if (widthMode == MeasureSpec.AT_MOST) {

            measuredWidth = Math.min(desiredWidth, widthSize);

        } else {

            measuredWidth = desiredWidth;
        }

        int exactWidthSpec = MeasureSpec.makeMeasureSpec(
                measuredWidth,
                MeasureSpec.EXACTLY
        );

        super.onMeasure(exactWidthSpec, heightMeasureSpec);
    }

}