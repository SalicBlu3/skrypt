package net.ynotapps.skrypt.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import net.ynotapps.skrypt.R;
import net.ynotapps.skrypt.model.dto.Skrypt;

import java.util.List;

public class SkryptListFragment extends BaseFragment {


    private ListView listView;

    @Override
    public String getFragmentTag() {
        return "Skrypt List";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Create ListView
        listView = new ListView(getActivity());

        // Populate skrypts to view
        listView.setAdapter(new SkryptListAdapter(getActivity()));

        return listView;
    }

    public void update() {
        ((SkryptListAdapter)listView.getAdapter()).notifyDataSetChanged();
    }

    public static class SkryptListAdapter extends BaseAdapter {

        private Context context;

        public SkryptListAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return getSkryptList().size();
        }

        private List<Skrypt> getSkryptList() {
            return Skrypt.listAll(Skrypt.class);
        }

        @Override
        public Skrypt getItem(int position) {
            return getSkryptList().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder = null;
            View view = convertView;

            if (view == null) {
                view = View.inflate(context, R.layout.list_item_skrypt, null);
                TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
                TextView tvSkrypt = (TextView) view.findViewById(R.id.tv_skrypt);
                viewHolder = new ViewHolder(tvTitle, tvSkrypt);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            Skrypt bean = getItem(position);
            viewHolder.getTitleView().setText(bean.getTitle());
            viewHolder.getSkryptView().setText(bean.getText());

            return view;
        }

        private class ViewHolder {
            private TextView titleView, skryptView;

            public ViewHolder(TextView titleView, TextView skryptView) {
                this.titleView = titleView;
                this.skryptView = skryptView;
            }

            public TextView getTitleView() {
                return titleView;
            }

            public TextView getSkryptView() {
                return skryptView;
            }
        }
    }
}
