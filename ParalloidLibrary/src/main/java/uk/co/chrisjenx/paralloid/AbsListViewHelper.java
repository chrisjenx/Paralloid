package uk.co.chrisjenx.paralloid;

import android.view.View;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Helper class for calculating relative scroll offsets in a ListView or GridView by tracking the
 * position of child views.
 */
public final class AbsListViewHelper {

    /**
     * This method is by no means accurate, and Will only work to any degree of accuracy if your list items
     * are the same height.
     * Otherwise it becomes vastly more difficult to calculate the correct height.
     *
     * @param listView listView to get height of, if no adapter is attached then nothing will happen.
     * @return 0 for failure.
     */
    public static int calculateApproximateHeight(AbsListView listView) {

        final ListAdapter adapter = listView.getAdapter();
        if (adapter == null) return 0;

        int totalHeight = 0;
        final int totalCount = adapter.getCount();
        if (totalCount > 0) {
            final View view = adapter.getView(0, null, listView);
            if(view == null) return 0;
            view.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            totalHeight = view.getMeasuredHeight() * totalCount;
            if (listView instanceof ListView) {
                totalHeight += ((ListView) listView).getDividerHeight() * (totalCount - 1);
            }
        }
        return totalHeight;
    }

    /**
     * Call from an AbsListView.OnScrollListener to calculate the incremental offset (change in scroll offset
     * since the last calculation).
     *
     * @return The  offset, or 0 if it wasn't possible to calculate the offset.
     */
    public static int calculateOffset(final AbsListView listView) {
        final View c = listView.getChildAt(0);
        if(c == null) return 0;
        return -c.getTop() + listView.getFirstVisiblePosition() * c.getHeight();
    }

    private AbsListViewHelper(){}
}