package rikka.layoutinflater.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.collection.SimpleArrayMap;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class LayoutInflaterFactory implements LayoutInflater.Factory2 {

    private final AppCompatDelegate appCompatDelegate;

    public interface OnViewCreatedListener {

        void onViewCreated(@NonNull View view, @Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs);
    }

    private final List<OnViewCreatedListener> onViewCreatedListeners = new ArrayList<>();

    /**
     * Create a LayoutInflaterFactory instance.
     * Use this constructor if you don't use AppCompat.
     */
    public LayoutInflaterFactory() {
        this(null);
    }

    /**
     * Create a LayoutInflaterFactory instance.
     * Use this constructor if you are using AppCompat.
     *
     * @param appCompatDelegate AppCompatDelegate from AppCompatActivity
     */
    public LayoutInflaterFactory(AppCompatDelegate appCompatDelegate) {
        this.appCompatDelegate = appCompatDelegate;
    }

    @Nullable
    @Override
    public final View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return onCreateView(null, name, context, attrs);
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        View view = null;

        if (appCompatDelegate != null) {
            view = appCompatDelegate.createView(parent, name, context, attrs);
        }

        if (view == null) {
            view = LayoutInflaterFactoryDefaultImpl.INSTANCE.createViewFromTag(context, name, attrs);
        }

        if (view != null) {
            onViewCreated(view, parent, name, context, attrs);
        }

        return view;
    }

    public void onViewCreated(@NonNull View view, @Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        for (OnViewCreatedListener listener : onViewCreatedListeners) {
            listener.onViewCreated(view, parent, name, context, attrs);
        }
    }

    public final LayoutInflaterFactory addOnViewCreatedListener(@NonNull OnViewCreatedListener listener) {
        Objects.requireNonNull(listener);
        onViewCreatedListeners.add(listener);
        return this;
    }

    public final LayoutInflaterFactory addOnViewCreatedListeners(@NonNull OnViewCreatedListener... listeners) {
        Collections.addAll(onViewCreatedListeners, listeners);
        return this;
    }
}

class LayoutInflaterFactoryDefaultImpl {

    private static final Class<?>[] constructorSignature = new Class[]{Context.class, AttributeSet.class};
    private static final String[] classPrefixList = new String[]{"android.widget.", "android.view.", "android.webkit."};
    private static final SimpleArrayMap<String, Constructor<?>> constructorMap = new SimpleArrayMap<>();
    public static final LayoutInflaterFactoryDefaultImpl INSTANCE = new LayoutInflaterFactoryDefaultImpl();

    private LayoutInflaterFactoryDefaultImpl() {
    }

    @Nullable
    public final View createViewFromTag(@NonNull Context context, @NonNull String name, @NonNull AttributeSet attrs) {
        String tag = name;
        if (Objects.equals(name, "view")) {
            tag = attrs.getAttributeValue((String) null, "class");
        }

        try {
            if (-1 != tag.indexOf('.')) {
                return createViewByPrefix(context, tag, attrs, (String) null);
            } else {
                for (String prefix : classPrefixList) {
                    View view = createViewByPrefix(context, tag, attrs, prefix);
                    if (view != null) {
                        return view;
                    }
                }
            }
        } catch (Exception ignored) {
        }

        return null;
    }

    private View createViewByPrefix(Context context, String name, AttributeSet attrs, String prefix) throws InflateException {
        Constructor<?> constructor = constructorMap.get(name);

        View view;
        try {
            if (constructor == null) {
                Class<?> cls = Class.forName(prefix != null ? prefix + name : name, false, context.getClassLoader()).asSubclass(View.class);
                constructor = cls.getConstructor(constructorSignature);
                constructorMap.put(name, constructor);
            }

            constructor.setAccessible(true);
            view = (View) constructor.newInstance(context, attrs);
        } catch (Exception e) {
            view = null;
        }

        return view;
    }
}
