package uk.co.chrisjenx.paralloid;

import android.util.SparseIntArray;
import android.widget.AbsListView;
import android.widget.ListAdapter;

/**
 * Helper class for calculating relative scroll offsets in a ListView or GridView by tracking the
 * position of child views.
 */
public class ListViewScrollTracker {

    private AbsListView mListView;
    private SparseIntArray mPositions;

    public ListViewScrollTracker(final AbsListView listView) {
        mListView = listView;
    }

    /**
     * This method is by no means accurate, and Will only work to any degree of accuracy if your list items
     * are the same height.
     * Otherwise it becomes vastly more difficult to calculate the correct height.
     * @param listView listView to get height of, if no adapter is attached then nothing will happen.
     * @return 0 for failure.
     */
    public static int calculateApproximateHeight(final AbsListView listView) {
        if(listView == null) return 0;
        final ListAdapter adapter = listView.getAdapter();
        if(adapter == null) return 0;
        final int totalItems = adapter.getCount();

        // Calc height of the visible item (as this is laid out)
        int height = listView.getChildAt(0).getHeight();
        return height * totalItems;
    }

    /**
     * Call from an AbsListView.OnScrollListener to calculate the incremental offset (change in scroll offset
     * since the last calculation).
     *
     * @param firstVisiblePosition First visible item position in the list.
     * @param visibleItemCount     Number of visible items in the list.
     * @return The incremental offset, or 0 if it wasn't possible to calculate the offset.
     */
    public int calculateIncrementalOffset(final int firstVisiblePosition, final int visibleItemCount) {
        // Remember previous positions, if any
        SparseIntArray previousPositions = mPositions;

        // Store new positions
        mPositions = new SparseIntArray();
        for (int i = 0; i < visibleItemCount; i++) {
            mPositions.put(firstVisiblePosition + i, mListView.getChildAt(i).getTop());
        }

        if (previousPositions != null) {
            // Find position which exists in both mPositions and previousPositions, then return the difference
            // of the new and old Y values.
            for (int i = 0; i < previousPositions.size(); i++) {
                int position = previousPositions.keyAt(i);
                int previousTop = previousPositions.get(position);
                Integer newTop = mPositions.get(position);
                if (newTop != null) {
                    return newTop - previousTop;
                }
            }
        }

        return 0; // No view's position was in both previousPositions and mPositions
    }

    public void clear() {
        mPositions = null;
    }
}