package com.fringefy.urbo.app.view;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.fringefy.urbo.DebugListener;
import com.fringefy.urbo.app.R;

/**
 * The debug view can display various information. Entry points are the setField method
 * that displays data in a table and toast which makes an Android toast
 */
public class DebugView extends TableLayout implements DebugListener {

// Members

	private Map<String, TextView> mapFields;	// map of displayed fields in the table


// Construction

	public DebugView(Context context) {
		super(context);
		init();
	}

	public DebugView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		mapFields = new HashMap<String, TextView>();
	}


// Public Methods

	// create/update a field. This method is thread safe and can be called from
	// any thread.
	/**
	 * setField is thread save method that can be called from any thread
	 * <BR>to add or update your data to the list in the DebugView widget
	 * @param sId String field to add
	 * @param sVal String value to add
	 */
	public void setField(final String sId, final String sVal) {
		if (sVal == null || sVal.isEmpty()) {
			removeField(sId);
			return;
		}
		Log.i("Urbo", sId + ": " + sVal);
		((Activity) getContext()).runOnUiThread(new Runnable() {
			@Override
			public void run() {
				TextView txtField = mapFields.get(sId);
				if (txtField == null) {
					TableRow tr = (TableRow)((Activity) getContext()).getLayoutInflater()
							.inflate(R.layout.debugview_row, DebugView.this, false);

					txtField = (TextView) tr.getChildAt(1);
					mapFields.put(sId, txtField);

					TextView txtLabel = (TextView) tr.getChildAt(0);
					txtLabel.setText(sId + ": ");

					addView(tr);
				}
				txtField.setText(sVal);
			}
		});
	}

	public void removeField(final String sId) {
		((Activity) getContext()).runOnUiThread(new Runnable() {
			@Override
			public void run() {
				TextView txtField = mapFields.get(sId);
				if (txtField != null) {
					mapFields.remove(sId);
					TableRow tr = (TableRow) txtField.getParent();
					removeView(tr);
				}
			}
		});
	}

	public void toast(final String sMsg) {
		((Activity) getContext()).runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(getContext(), sMsg, Toast.LENGTH_SHORT).show();
			}
		});
	}

	public void clearFields(){
		((Activity) getContext()).runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mapFields.clear();
				removeAllViewsInLayout();
			}
		});
	}
}
