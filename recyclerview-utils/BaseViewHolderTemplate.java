package ${PACKAGE_NAME};

import android.content.Context;
import android.view.View;

import moe.shizuku.support.recyclerview.BaseViewHolder;

#parse("File Header.java")
public class ${NAME} extends BaseViewHolder<${TYPE}> {

    public static final Creator<${TYPE}> CREATOR = new Creator<${TYPE}>() {
        @Override
        public BaseViewHolder<${TYPE}> createViewHolder(LayoutInflater inflater, ViewGroup parent) {
            return new ${NAME}(inflater.inflate(R.layout., parent, false));
        }
    };

    public ${NAME}(View itemView) {
        super(itemView);
    }

    @Override
    public void onBind() {
        Context context = itemView.getContext();
    }
}