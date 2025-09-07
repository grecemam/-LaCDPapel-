package com.example.lacdpapel.Activity;

import static android.app.PendingIntent.getActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lacdpapel.Adapters.AdapterFilmsActors;
import com.example.lacdpapel.ApiInterface;
import com.example.lacdpapel.ApiInterface2;
import com.example.lacdpapel.Models.Actors;
import com.example.lacdpapel.Models.ActorsFavorite;
import com.example.lacdpapel.Models.Films;
import com.example.lacdpapel.Models.Movies;
import com.example.lacdpapel.R;
import com.example.lacdpapel.ServiceBuilder;
import com.example.lacdpapel.ServiceBuilder2;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActor extends AppCompatActivity {
    ImageView imageView, ic_love;
    ApiInterface apiInterface;
    String urlForDataBase;
    ApiInterface2 apiInterface2;
    RecyclerView recyclerView;
    AdapterFilmsActors adapterFilmsActors;
    String apiKey, apiKey2, userid;
    int actorId;
    TextView textView, profession, birthday, old, rost;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference actorRef;
    private List<Movies> filmsList = new ArrayList<>();

    private CompositeDisposable disposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_actor);
        userid = firebaseAuth.getCurrentUser().getUid();
        imageView = findViewById(R.id.actor_poster);
        actorId = getIntent().getIntExtra("actorId", 0);
        ic_love = findViewById(R.id.ic_love);
        textView = findViewById(R.id.name_actor);
        profession = findViewById(R.id.profession);
        birthday = findViewById(R.id.birthday);
        old = findViewById(R.id.old);
        rost = findViewById(R.id.rost);
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerFilms);
        if (firebaseAuth.getCurrentUser() != null) {
            actorRef = db.collection("ActorsFavotite");
        }
        int inLikeResourceId = R.drawable.ic_in_like;
        int likeResourceId = R.drawable.ic_love;

        Drawable in_like = getResources().getDrawable(inLikeResourceId);
        Drawable like = getResources().getDrawable(likeResourceId);

        Query query = actorRef.whereEqualTo("userId", userid)
                .whereEqualTo("actorId", actorId);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (!task.getResult().isEmpty()) {
                        ic_love.setImageDrawable(in_like);
                    } else {
                        ic_love.setImageDrawable(like);
                    }
                } else {
                    Toast.makeText(DetailActor.this, "Ошибка при проверке фильма", Toast.LENGTH_SHORT).show();
                }
            }
        });


        apiInterface2 = ServiceBuilder2.buildRequest().create(ApiInterface2.class);
        apiKey2 = "3J3N193-QEVMCE0-GT33RXA-KYRNJ6H";

        apiInterface = ServiceBuilder.buildRequest().create(ApiInterface.class);
        apiKey = "ae974634-294f-4e55-a856-e249334bb9e5";

        disposable.add(getActor(apiKey, actorId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        film -> {
                            updateFilmUI(film);
                        },
                        error -> {
                            Log.e("API Error", error.getMessage());
                        }
                ));

        ic_love.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable currentDrawable = ic_love.getDrawable();

                if (currentDrawable != null) {
                    int currentDrawableId = (currentDrawable == like) ? likeResourceId : inLikeResourceId;

                    if (currentDrawableId == likeResourceId) {
                        ic_love.setImageDrawable(in_like);
                        addFavoriteActor(actorId);
                    } else if (currentDrawableId == inLikeResourceId) {
                        ic_love.setImageDrawable(like);
                        deleteFavoriteActor(actorId);
                    }
                }
            }
        });

    }
    private void Change()
    {
        disposable.add(getActor(apiKey, actorId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        film -> {
                            updateFilmUI(film);
                        },
                        error -> {
                            Log.e("API Error", error.getMessage());
                        }
                ));
    }
    /*private void deleteFavoriteActor(int actorId) {
        if (firebaseAuth.getCurrentUser() != null) {
            Query query = actorRef.whereEqualTo("userId", userid)
                    .whereEqualTo("actorId", actorId);

            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            actorRef.document(document.getId()).delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(DetailActor.this, "Ошибка при удалении актера из списка",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    } else {
                        Toast.makeText(DetailActor.this, "Ошибка при проверке актера",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }*/

    private void deleteFavoriteActor(int actorId) {
        if (firebaseAuth.getCurrentUser() == null) return;

        Query query = actorRef.whereEqualTo("userId", userid)
                .whereEqualTo("actorId", actorId);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    deleteDocument(document.getId());
                }
            } else {
                showToast("Ошибка при проверке актера");
            }
        });
    }
    private void deleteDocument(String documentId) {
        actorRef.document(documentId).delete()
                .addOnSuccessListener(aVoid -> {
                    Log.e("Удаление актера", "Успешное удаление актера");
                })
                .addOnFailureListener(e -> showToast("Ошибка при удалении актера из списка"));
    }
    private void showToast(String message) {
        Toast.makeText(DetailActor.this, message, Toast.LENGTH_SHORT).show();
    }

    private Single<Actors> getActor(String ApiKey, int actorId){
        return Single.create(emitter -> {

            Call<Actors> getActor = apiInterface.getStaffDetails(apiKey, actorId);
            getActor.enqueue(new Callback<Actors>() {
                @Override
                public void onResponse(Call<Actors> call, Response<Actors> response) {
                    if(response.isSuccessful()){
                        progressBar.setVisibility(View.GONE);
                        Actors actors = response.body();
                        emitter.onSuccess(actors);
                    } else{
                        emitter.onError(new Exception("Failed to get film data"));
                    }
                }

                @Override
                public void onFailure(Call<Actors> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    emitter.onError(t);
                }
            });
        });
    }
    private void addFavoriteActor(int actorId) {
        if (firebaseAuth.getCurrentUser() != null) {
            ActorsFavorite actorsFavorite = new ActorsFavorite(userid, null, actorId, textView.getText().toString(), urlForDataBase);

            actorRef.add(actorsFavorite)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            String favid = documentReference.getId();

                            actorsFavorite.setFavid(favid);

                            actorRef.document(favid).set(actorsFavorite)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(DetailActor.this, "Ошибка обновления актера", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(DetailActor.this, "Ошибка добавления актера", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
    private void updateFilmUI(Actors actors){
        if(actors != null){
            Picasso.with(DetailActor.this).load(actors.getPosterUrl()).into(imageView);
            textView.setText(actors.getNameRu());
            profession.setText(actors.getProfession());
            String birth = formatDate(actors.getBirthday());
            birthday.setText(birth);
            urlForDataBase = actors.getPosterUrl().toString();
            String normOld = formatAge(actors.getAge());
            old.setText("Возраст: "+ normOld);
            rost.setText(String.valueOf(actors.getGrowth()) + " см");

            List<Integer> filmIds = new ArrayList<>();

            List<Films> filmsList = actors.getFilms();
            if (filmsList != null && !filmsList.isEmpty()) {
                for (Films film : filmsList) {
                    int filmId = film.getFilmId();
                    filmIds.add(filmId);
                }
            }
            updateFilmsRV(filmIds);

        }
    }

    private void updateFilmsRV(List<Integer> filmIds) {
        for (int filmId : filmIds) {
            Call<Movies> getFilmCall = apiInterface2.getMovie(apiKey2, filmId);

            getFilmCall.enqueue(new Callback<Movies>() {
                @Override
                public void onResponse(Call<Movies> call, Response<Movies> response) {
                    if (response.isSuccessful()) {
                        Movies film = response.body();
                        if (film != null && film.getPoster().getUrl() != null && film.getName() != null) {
                            filmsList.add(film); // Добавляем фильм в список
                        }
                    } else {
                    }
                    initializeAdapter();

                }

                @Override
                public void onFailure(Call<Movies> call, Throwable t) {
                    Log.e("API Error", t.getMessage());
                    Toast.makeText(DetailActor.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void initializeAdapter() {
        adapterFilmsActors = new AdapterFilmsActors(DetailActor.this, filmsList);
        recyclerView.setLayoutManager(new GridLayoutManager(DetailActor.this, 2, LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemViewCacheSize(filmsList.size());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterFilmsActors);

    }

    public static String formatDate(String inputDate) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = inputFormat.parse(inputDate);

            SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMMM yyyyг", new Locale("ru", "RU"));
            return outputFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }
        public static String formatAge(int age) {
            if (age < 0) {
                return "Некорректный возраст";
            } else if (age % 10 == 1 && age != 11) {
                return age + " год";
            } else if ((age % 10 >= 2 && age % 10 <= 4) && !(age >= 12 && age <= 14)) {
                return age + " года";
            } else {
                return age + " лет";
            }
        }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(DetailActor.this, DetailActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }
}