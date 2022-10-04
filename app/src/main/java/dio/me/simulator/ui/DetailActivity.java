package dio.me.simulator.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.bumptech.glide.Glide;

import java.io.Serializable;

import dio.me.simulator.databinding.ActivityDetailBinding;
import dio.me.simulator.domain.Match;

public class DetailActivity extends AppCompatActivity {

    private ActivityDetailBinding binding;
    public static final String EXTRA_MATCH = "EXTRA_MATCH";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadMatchFromExtra();





    }
    private void loadMatchFromExtra() {
        Match match = getIntent().getParcelableExtra(DetailActivity.EXTRA_MATCH);

        if(match != null){
            Glide.with(this).load(match.getPlace().getImage()).into(binding.ivPlace);
            binding.tvDescription.setText(match.getDescription());
            Glide.with(this).load(match.getHomeTeam().getImage()).into(binding.ivHomeTeam);
            Glide.with(this).load(match.getAwayTeam().getImage()).into(binding.ivAwayTeam);
            binding.rbHomeTeamStars.setRating(match.getHomeTeam().getStars());
            binding.rbAwayTeamStars.setRating(match.getAwayTeam().getStars());
            binding.tvHomeTeamName.setText(match.getHomeTeam().getName());
            binding.tvAwayTeamName.setText(match.getAwayTeam().getName());
            if(match.getHomeTeam().getScore() != null && match.getAwayTeam().getScore() != null) {
                binding.tvHomeTeamScore.setText(match.getHomeTeam().getScore().toString());
                binding.tvAwayTeamScore.setText(match.getAwayTeam().getScore().toString());
            }
            getSupportActionBar().setTitle(match.getPlace().getName());
        }



    }
}