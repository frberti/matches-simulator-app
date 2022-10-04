package dio.me.simulator.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;

import java.util.Comparator;
import java.util.List;
import java.util.Random;

import dio.me.simulator.R;
import dio.me.simulator.data.MatchesApi;
import dio.me.simulator.databinding.ActivityMainBinding;
import dio.me.simulator.domain.Match;
import dio.me.simulator.ui.adapter.MatchesAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MatchesApi matchesApi;
    private MatchesAdapter matchesAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupHttpCliente();
        setupMatchesList();
        setupMatchesRefresh();
        setupFloatingActionButton();

    }

    private void setupHttpCliente() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://frberti.github.io/matches-simulator-api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        matchesApi = retrofit.create(MatchesApi.class);
    }

    public void setupMatchesList(){
        binding.rvMatches.setHasFixedSize(true);
        binding.rvMatches.setLayoutManager(new LinearLayoutManager(this));
        findMatchesFromApi();

    }

    private void setupMatchesRefresh() {
        binding.srlMatches.setOnRefreshListener(this::findMatchesFromApi);
    }

    private void setupFloatingActionButton() {
        binding.fabSimulate.setOnClickListener(view ->
                view.animate().rotationBy(360).setDuration(500).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        Random random = new Random();
                        for(int i = 0; i < matchesAdapter.getItemCount(); i++){
                            Match match = matchesAdapter.getMatches().get(i);
                            match.getHomeTeam().setScore(random.nextInt(match.getHomeTeam().getStars() + 1));
                            match.getAwayTeam().setScore(random.nextInt(match.getAwayTeam().getStars() + 1));
                            matchesAdapter.notifyItemChanged(i);
                        }
                    }
                }));
    }

    private void onErrorMessage() {
        Snackbar.make(binding.fabSimulate, R.string.error_api, Snackbar.LENGTH_LONG).show();
    }

    private void findMatchesFromApi() {
        binding.srlMatches.setRefreshing(true);
        matchesApi.getMatches().enqueue(new Callback<List<Match>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<List<Match>> call, Response<List<Match>> response) {
                if(response.isSuccessful()){
                    List<Match> matches = response.body();
                    if (!matches.isEmpty() || matches != null) {
                        matches.sort(Comparator.comparing(d -> d.getDescription()));
                    }
                    matchesAdapter = new MatchesAdapter(matches);
                    binding.rvMatches.setAdapter(matchesAdapter);
                }else{
                    onErrorMessage();
                }
                binding.srlMatches.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<Match>> call, Throwable t) {
                onErrorMessage();
                binding.srlMatches.setRefreshing(false);
            }
        });
    }

}
