package rikka.material.preference;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.ArrayRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleableRes;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;

/**
 * MaterialComboBoxPreference
 *
 * @see androidx.preference.ListPreference
 * @see androidx.preference.DropDownPreference
 *
 * @link <a href="https://github.com/material-components/material-components-android/blob/master/docs/components/Menu.md#exposed-dropdown-menus">material exposed dropdown menus</a>
 */
public class MaterialComboBoxPreference extends Preference {

    private static final String TAG_KEYS = "keys";
    private CharSequence[] keys = null;
    private static final String TAG_VALS = "vals";
    private CharSequence[] vals = null;
    private String value = null;
    private boolean valueIsInitialized = false;

    private final Context mContext;
    private final ArrayAdapter<String> textArrayAdapter;

    private MaterialAutoCompleteTextView comboBox;

    public MaterialComboBoxPreference(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize(context, attrs, defStyleAttr, defStyleRes);
        this.mContext = context;
        this.textArrayAdapter = createAdapter();

        updateEntries();
    }

    public MaterialComboBoxPreference(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, R.style.Preference_Rikka_MaterialComboBoxPreference);
    }

    public MaterialComboBoxPreference(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.materialComboBoxPreferenceStyle);
    }

    public MaterialComboBoxPreference(@NonNull Context context) {
        this(context, null);
    }

    private final AdapterView.OnItemClickListener itemClickListener = (parent, view, position, id) -> {
        if (position >= 0) {
            String value = getVals()[position].toString();
            if (!value.equals(getValue()) && callChangeListener(value)) {
                setValue(value);
            }
        }
    };

    private void initialize(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        TypedArray styledAttributes = context.obtainStyledAttributes(
                attrs, R.styleable.MaterialComboBoxPreference, defStyleAttr, defStyleRes);

        CharSequence[] keys = textArrayBy(styledAttributes, R.styleable.MaterialComboBoxPreference_keys);
        if (keys == null) {
            throw new IllegalArgumentException("keys must not be null.");
        }
        validateTextArrayElements(TAG_KEYS, keys);

        CharSequence[] vals = textArrayBy(styledAttributes, R.styleable.MaterialComboBoxPreference_vals);
        if (vals == null) {
            throw new IllegalArgumentException("vals must not be null.");
        }
        if (vals.length != keys.length) {
            throw new IllegalArgumentException("vals.length must be equal to keys.length.");
        }
        validateTextArrayElements(TAG_VALS, vals);

        styledAttributes.recycle();

        this.keys = keys;
        this.vals = vals;
    }

    public CharSequence[] getKeys() {
        return keys;
    }

    public void setKeys(CharSequence[] keys) {
        this.keys = keys;
        updateEntries();
    }

    public void setKeys(@ArrayRes int keysResId) {
        setKeys(getContext().getResources().getTextArray(keysResId));
    }

    public CharSequence[] getVals() {
        return vals;
    }

    public void setVals(CharSequence[] vals) {
        this.vals = vals;
    }

    public void setVals(@ArrayRes int valsResId) {
        setVals(getContext().getResources().getTextArray(valsResId));
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        final boolean changed = !TextUtils.equals(this.value, value);
        if (changed || !valueIsInitialized) {
            this.value = value;
            this.valueIsInitialized = true;
            persistString(value);
            if (changed) {
                notifyChanged();
            }
        }
    }
    
    @Nullable
    public CharSequence getSelected() {
        final int index = getSelectedIndex();
        return index >= 0 && keys != null ? keys[index] : null;
    }

    public void setSelectedIndex(int index) {
        if (vals != null) {
            setValue(vals[index].toString());
        }
    }

    public int getSelectedIndex() {
        return findIndexOfValue(value);
    }

    @Override
    protected void onClick() {
        comboBox.showDropDown();
    }

    @NonNull
    @SuppressLint("PrivateResource")
    protected ArrayAdapter<String> createAdapter() {
        return new ArrayAdapter<>(mContext, com.google.android.material.R.layout.mtrl_auto_complete_simple_item);
    }

    private void updateEntries() {
        textArrayAdapter.clear();
        if (getKeys() != null) {
            for (CharSequence text : getKeys()) {
                textArrayAdapter.add(text.toString());
            }
        }
    }

    @Override
    protected void notifyChanged() {
        super.notifyChanged();
        // When setting a SummaryProvider for this Preference, this method may be called before
        // mAdapter has been set in ListPreference's constructor.
        if (textArrayAdapter != null) {
            textArrayAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBindViewHolder(@NonNull PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        MaterialAutoCompleteTextView autoCompleteTextView = (MaterialAutoCompleteTextView) holder.findViewById(R.id.comboBoxWidget);
        if (autoCompleteTextView == null) {
            throw new IllegalStateException("autoCompleteTextView must not be null.");
        }
        autoCompleteTextView.setAdapter(textArrayAdapter);
        autoCompleteTextView.setOnItemClickListener(itemClickListener);
        CharSequence selected = getSelected();
        if (selected != null) {
            autoCompleteTextView.setText(selected, false);
        }
        this.comboBox = autoCompleteTextView;
    }

    private int findSelectedIndexOfValue(String value) {
        CharSequence[] vals = getVals();
        if (value != null && vals != null) {
            for (int i = vals.length - 1; i >= 0; i--) {
                if (TextUtils.equals(vals[i].toString(), value)) {
                    return i;
                }
            }
        }
        return Spinner.INVALID_POSITION;
    }
    
    private int findIndexOfValue(String value) {
        if (value != null && vals != null) {
            for (int i = vals.length - 1; i >= 0; i--) {
                if (TextUtils.equals(vals[i].toString(), value)) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    protected Object onGetDefaultValue(@NonNull TypedArray a, int index) {
        return a.getString(index);
    }

    @Override
    protected void onSetInitialValue(Object defaultValue) {
        setValue(getPersistedString((String) defaultValue));
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        if (isPersistent()) {
            // No need to save instance state since it's persistent
            return superState;
        }

        final SavedState savedState = new SavedState(superState);
        savedState.setValue(getValue());
        return savedState;
    }

    @Override
    protected void onRestoreInstanceState(@Nullable Parcelable state) {
        if (state == null || !state.getClass().equals(SavedState.class)) {
            // Didn't save state for us in onSaveInstanceState
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        setValue(savedState.getValue());
    }

    private static final String TEXT_ARRAY_ELEMENTS_VALIDATION_EX_MSG_TEMPLATE = "[%s] text must not be null. [index: %d]";
    private static void validateTextArrayElements(@NonNull String tag, @NonNull CharSequence[] textArray) {
        for (int i = 0; i < textArray.length; i++) {
            if (textArray[i] == null) {
                throw new IllegalArgumentException(String.format(TEXT_ARRAY_ELEMENTS_VALIDATION_EX_MSG_TEMPLATE, tag, i));
            }
        }
    }

    @Nullable
    private static CharSequence[] textArrayBy(@NonNull TypedArray styledAttributes, @StyleableRes int arrayId) {
        return styledAttributes.getTextArray(arrayId);
    }

    /**
     * SavedState
     *
     * @noinspection JavadocReference
     * @see androidx.preference.ListPreference.SavedState
     */
    private static class SavedState extends BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    @Override
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    @Override
                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };

        private String value;

        public SavedState(Parcel source) {
            super(source);
            value = source.readString();
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(@NonNull Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeString(value);
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
