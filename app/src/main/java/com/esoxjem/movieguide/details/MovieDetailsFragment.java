package com.esoxjem.movieguide.details;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.esoxjem.movieguide.Api;
import com.esoxjem.movieguide.BaseApplication;
import com.esoxjem.movieguide.Constants;
import com.esoxjem.movieguide.Movie;
import com.esoxjem.movieguide.R;
import com.esoxjem.movieguide.Review;
import com.esoxjem.movieguide.Video;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MovieDetailsFragment extends Fragment implements MovieDetailsView, View.OnClickListener {
    @Inject
    MovieDetailsPresenter movieDetailsPresenter;

    @BindView(R.id.movie_poster)
    ImageView poster;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.movie_name)
    TextView title;
    @BindView(R.id.movie_year)
    TextView releaseDate;
    @BindView(R.id.movie_rating)
    TextView rating;
    @BindView(R.id.movie_description)
    TextView overview;
    @BindView(R.id.trailers_label)
    TextView label;
    @BindView(R.id.trailers)
    LinearLayout trailers;
    @BindView(R.id.trailers_container)
    HorizontalScrollView horizontalScrollView;
    @BindView(R.id.reviews_label)
    TextView reviews;
    @BindView(R.id.reviews)
    LinearLayout reviewsContainer;
    @BindView(R.id.favorite)
    FloatingActionButton favorite;
    @BindView(R.id.toolbar)
    @Nullable
    Toolbar toolbar;

    private Movie mymovie;
    private Unbinder unbinder;

    public MovieDetailsFragment() {
        // Required empty public constructor
    }

    public static MovieDetailsFragment getInstance(@NonNull Movie mymovie) {
        Bundle args = new Bundle();
        args.putParcelable(Constants.MOVIE, mymovie);
        MovieDetailsFragment movieDetailsFragment = new MovieDetailsFragment();
        movieDetailsFragment.setArguments(args);
        return movieDetailsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        ((BaseApplication) getActivity().getApplication()).createDetailsComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        setToolbar();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            Movie movie = (Movie) getArguments().get(Constants.MOVIE);
            if (movie != null) {
                this.mymovie = movie;
                movieDetailsPresenter.setView(this);
                movieDetailsPresenter.showDetails((movie));
                movieDetailsPresenter.showFavoriteButton(movie);
            }
        }
    }

    private void setToolbar() {
        collapsingToolbar.setContentScrimColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        collapsingToolbar.setTitle(getString(R.string.movie_details));
        collapsingToolbar.setCollapsedTitleTextAppearance(R.style.CollapsedToolbar);
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedToolbar);
        collapsingToolbar.setTitleEnabled(true);

        if (toolbar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        } else {
            // Don't inflate. Tablet is in landscape mode.
        }
    }

    @Override
    public void showDetails(Movie mymovie) {
        Glide.with(getContext()).load(Api.getBackdropPath(mymovie.getBackdropPath())).into(poster);
        title.setText(mymovie.getTitle());
        releaseDate.setText(String.format(getString(R.string.release_date), mymovie.getReleaseDate()));
        rating.setText(String.format(getString(R.string.rating), String.valueOf(mymovie.getVoteAverage())));
        overview.setText(mymovie.getOverview());
        movieDetailsPresenter.showTrailers(mymovie);
        movieDetailsPresenter.showReviews(mymovie);
    }

    @Override
    public void showTrailers(List<Video> trailers) {
        if (trailers.isEmpty()) {
            label.setVisibility(View.GONE);
            this.trailers.setVisibility(View.GONE);
            horizontalScrollView.setVisibility(View.GONE);

        } else {
            label.setVisibility(View.VISIBLE);
            this.trailers.setVisibility(View.VISIBLE);
            horizontalScrollView.setVisibility(View.VISIBLE);

            this.trailers.removeAllViews();
            LayoutInflater inflater = getActivity().getLayoutInflater();
            RequestOptions options = new RequestOptions()
                    .placeholder(R.color.colorPrimary)
                    .centerCrop()
                    .override(150, 150);

            for (Video trailer : trailers) {
                View thumbContainer = inflater.inflate(R.layout.video, this.trailers, false);
                ImageView thumbView = thumbContainer.findViewById(R.id.video_thumb);
                thumbView.setTag(R.id.glide_tag, Video.getUrl(trailer));
                thumbView.requestLayout();
                thumbView.setOnClickListener(this);
                Glide.with(requireContext())
                        .load(Video.getThumbnailUrl(trailer))
                        .apply(options)
                        .into(thumbView);
                this.trailers.addView(thumbContainer);
            }
        }
    }

    @Override
    public void showReviews(List<Review> reviews) {
        if (reviews.isEmpty()) {
            this.reviews.setVisibility(View.GONE);
            reviewsContainer.setVisibility(View.GONE);
        } else {
            this.reviews.setVisibility(View.VISIBLE);
            reviewsContainer.setVisibility(View.VISIBLE);

            reviewsContainer.removeAllViews();
            LayoutInflater inflater = getActivity().getLayoutInflater();
            for (Review review : reviews) {
                ViewGroup reviewContainer = (ViewGroup) inflater.inflate(R.layout.review, reviewsContainer, false);
                TextView reviewAuthor = reviewContainer.findViewById(R.id.review_author);
                TextView reviewContent = reviewContainer.findViewById(R.id.review_content);
                reviewAuthor.setText(review.getAuthor());
                reviewContent.setText(review.getContent());
                reviewContent.setOnClickListener(this);
                reviewsContainer.addView(reviewContainer);
            }
        }
    }

    @Override
    public void showFavorited() {
        favorite.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_favorite_white_24dp));
    }

    @Override
    public void showUnFavorited() {
        favorite.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_favorite_border_white_24dp));
    }

    @SuppressLint("InvalidR2Usage")
    @OnClick(R.id.favorite)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.video_thumb:
                onThumbnailClick(view);
                break;

            case R.id.review_content:
                onReviewClick((TextView) view);
                break;

            case R.id.favorite:
                onFavoriteClick();
                break;

            default:
                break;
        }
    }

    private void onReviewClick(TextView view) {
        if (view.getMaxLines() == 5) {
            view.setMaxLines(500);
        } else {
            view.setMaxLines(5);
        }
    }

    private void onThumbnailClick(View view) {
        String videoUrl = (String) view.getTag(R.id.glide_tag);
        Intent playVideoIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
        startActivity(playVideoIntent);
    }

    private void onFavoriteClick() {
        movieDetailsPresenter.onFavoriteClick(mymovie);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        movieDetailsPresenter.destroy();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((BaseApplication) getActivity().getApplication()).releaseDetailsComponent();
    }
}
