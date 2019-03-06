package com.admedia.bendre.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.admedia.bendre.R;
import com.admedia.bendre.activities.YoutubePlayerActivity;
import com.admedia.bendre.adapters.YoutubeVideoAdapter;
import com.admedia.bendre.api.YoutubeApi;
import com.admedia.bendre.model.Video;
import com.admedia.bendre.model.YoutubeVideoModel;
import com.admedia.bendre.util.Constants;
import com.admedia.bendre.util.EndpointConstants;
import com.admedia.bendre.util.MessageUtil;
import com.admedia.bendre.util.RecyclerViewOnClickListener;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChannelVideosFragment extends Fragment {
    private static final String ARG_COLUMN_COUNT = "column-count";
    private OnListFragmentInteractionListener mListener;

    private RecyclerView recyclerView;
    private ArrayList<YoutubeVideoModel> videos;
    private ProgressBar mProgressBar;

    public ChannelVideosFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ChannelVideosFragment newInstance(int columnCount) {
        ChannelVideosFragment fragment = new ChannelVideosFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
        {
            int mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

        loadData();
    }

    private void showProgress(boolean show) {
        if (!show)
        {
            if (mProgressBar != null)
            {
                mProgressBar.setVisibility(View.GONE);
            }
        }
    }

    private void loadData() {
        showProgress(true);
        new Thread(new GetVideosRunnable()).start();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_list, container, false);

        // Set the adapter
        Context context = view.getContext();
        recyclerView = view.findViewById(R.id.video_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);

        mProgressBar = view.findViewById(R.id.progressbar);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener)
        {
            mListener = (OnListFragmentInteractionListener) context;
        }
        else
        {
            throw new RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Video item);
    }

    public void fillData() {
        showProgress(false);
        YoutubeVideoAdapter adapter = new YoutubeVideoAdapter(getContext(), videos);
        recyclerView.setAdapter(adapter);

        //set click event
        recyclerView.addOnItemTouchListener(new RecyclerViewOnClickListener(getContext(), (view1, position) -> {
            //start youtube player activity by passing selected video id via intent
            startActivity(new Intent(getContext(), YoutubePlayerActivity.class)
                    .putExtra("video_id", videos.get(position).getVideoId()));

        }));
    }


    class GetVideosRunnable implements Runnable {
        @Override
        public void run() {
            videos = new ArrayList<>();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(EndpointConstants.YOUTUBE_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();

            YoutubeApi apiService = retrofit.create(YoutubeApi.class);
            Call<JsonObject> call = apiService.getChannelVideos("snippet", Constants.BENDRE_CHANNEL, Constants.YOUTUBE_API_KEY, "video", "date", 25);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                    if (response.body() != null)
                    {
                        JsonArray items = (JsonArray) response.body().get("items");
                        for (int counter = 0; counter < items.size(); counter++)
                        {
                            JsonElement element = items.get(counter);
                            String videoId = element.getAsJsonObject().get("id").getAsJsonObject().get("videoId").getAsString();
                            String title = element.getAsJsonObject().get("snippet").getAsJsonObject().get("title").getAsString();
                            String publishedDate = element.getAsJsonObject().get("snippet").getAsJsonObject().get("publishedAt").getAsString();
                            YoutubeVideoModel video = new YoutubeVideoModel();
                            video.setTitle(title);
                            video.setVideoId(videoId);
                            video.setPublishedDate(publishedDate);
                            videos.add(video);
                        }
                    }
                    fillData();
                }

                @Override
                public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                    MessageUtil.getInstance().ToastMessage(getContext(), getString(R.string.cannot_fetch_data));
                    fillData();
                }
            });
        }
    }
}
