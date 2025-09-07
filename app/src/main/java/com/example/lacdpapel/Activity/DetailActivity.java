package com.example.lacdpapel.Activity;



import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lacdpapel.Adapters.AdapterActors;
import com.example.lacdpapel.Adapters.AdapterReviews;
import com.example.lacdpapel.Adapters.AdapterSGroup;
import com.example.lacdpapel.ApiInterface;
import com.example.lacdpapel.Models.Actors;
import com.example.lacdpapel.Models.Films;
import com.example.lacdpapel.Models.Look_after;
import com.example.lacdpapel.Models.Review;
import com.example.lacdpapel.R;
import com.example.lacdpapel.ServiceBuilder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {
    ImageView imageView;
    ApiInterface apiInterface;
    RecyclerView recyclerView, recyclerView1, recyclerView2, otzivi_recyclerView;
    VideoView videoView;
    String apiKey, userId;
    TextView textView, Year, descr, textViewactors, textView3, all_otzivi, smotret_pozhe;
    ProgressBar progressBar;
    AdapterActors adapterActors;
    private List<Actors> actorList = new ArrayList<>();
    private CompositeDisposable disposable = new CompositeDisposable();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference filmRef;
    CollectionReference reviewsRef;
    StorageReference storageRef;
    StorageReference videoRef;
    MediaController mediaController;


    private AdapterReviews adapterReviews;
    List<Review> reviewList;
    Integer filmId;
    /**
     * Метод onCreate инициализирует активность при ее первом создании.
     * Он находит и присваивает ресурсы виджетов, устанавливает макет для активности,
     * получает ID фильма из предыдущей активности, настраивает видеоплеер, загружает информацию о фильме,
     * загружает обзоры фильма, проверяет, добавлен ли фильм в список "Посмотреть позже",
     * и устанавливает обработчики нажатия для кнопок "Посмотреть позже" и "Все отзывы".
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.serial_detail_activity);

        filmId = getIntent().getIntExtra("filmId", 0);
        videoView = findViewById(R.id.videoView);
        recyclerView1 = findViewById(R.id.recyclerActors);
        recyclerView2 = findViewById(R.id.recyclerGroup);
        otzivi_recyclerView = findViewById(R.id.otzivi_rv);
        imageView = findViewById(R.id.image);
        textView3 = findViewById(R.id.textView3);
        all_otzivi = findViewById(R.id.all_otivi);
        smotret_pozhe = findViewById(R.id.smotret_pozhe);
        userId = firebaseAuth.getCurrentUser().getUid();
        reviewList = new ArrayList<>();
        mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        storageRef = FirebaseStorage.getInstance().getReference();
        if(filmId == 4795888)
        {
            StorageReference pathReference = storageRef.child("berlin.mp4");
            StorageReference gsReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://lacdpapel-af5ca.appspot.com/berlin.mp4");
            StorageReference httpsReference = FirebaseStorage.getInstance().getReferenceFromUrl("https://firebasestorage.googleapis.com/v0/b/lacdpapel-af5ca.appspot.com/o/berlin.mp4?alt=media&token=932f9dde-50e6-464f-b04c-482cb3379385");
            videoRef = storageRef.child("berlin.mp4");
        }
        else if(filmId == 1046206)
        {
            StorageReference pathReference = storageRef.child("LCDP11.mp4");
            StorageReference gsReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://lacdpapel-af5ca.appspot.com/LCDP11.mp4");
            StorageReference httpsReference = FirebaseStorage.getInstance().getReferenceFromUrl("https://firebasestorage.googleapis.com/v0/b/lacdpapel-af5ca.appspot.com/o/LCDP11.mp4?alt=media&token=b7c7be74-a0a7-4c4d-8453-0641e476328e");
            videoRef = storageRef.child("LCDP11.mp4");
        }
        else if(filmId == 1117735)
        {
            StorageReference pathReference = storageRef.child("elite.mp4");
            StorageReference gsReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://lacdpapel-af5ca.appspot.com/elite.mp4");
            StorageReference httpsReference = FirebaseStorage.getInstance().getReferenceFromUrl("https://firebasestorage.googleapis.com/v0/b/lacdpapel-af5ca.appspot.com/o/elite.mp4?alt=media&token=b3bd4393-ec01-4459-950b-89cea37d27ac");
            videoRef = storageRef.child("elite.mp4");
        }
        else if(filmId == 915196)
        {
            StorageReference pathReference = storageRef.child("osd.mp4");
            StorageReference gsReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://lacdpapel-af5ca.appspot.com/osd.mp4");
            StorageReference httpsReference = FirebaseStorage.getInstance().getReferenceFromUrl("https://firebasestorage.googleapis.com/v0/b/lacdpapel-af5ca.appspot.com/o/osd.mp4?alt=media&token=81b822b4-63d3-4722-8dfc-108f18f835a7");
            videoRef = storageRef.child("osd.mp4");
        }

        videoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri videoUri) {
                videoView.setVideoURI(videoUri);
                videoView.start();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("TAG", "Ошибка получения URL для видео", exception);
            }
        });

        Drawable look_pustaya = getResources().getDrawable(R.drawable.look_latter);
        Drawable look_polnaya = getResources().getDrawable(R.drawable.look_latter_2);
        if (firebaseAuth.getCurrentUser() != null) {
            filmRef = db.collection("FilmsAfter");
            reviewsRef = db.collection("reviews");
        }
        LoadReviews();
        Query query = filmRef.whereEqualTo("userId", userId)
                .whereEqualTo("filmIdApi", filmId);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (!task.getResult().isEmpty()) {
                        smotret_pozhe.setCompoundDrawablesRelativeWithIntrinsicBounds(null, look_polnaya, null, null);
                    } else {
                        smotret_pozhe.setCompoundDrawablesRelativeWithIntrinsicBounds(null, look_pustaya, null, null);
                    }
                } else {
                    Toast.makeText(DetailActivity.this, "Ошибка при проверке фильма", Toast.LENGTH_SHORT).show();
                }
            }
        });

        textView = findViewById(R.id.NameFilm);
        Year = findViewById(R.id.Year);
        progressBar = findViewById(R.id.progressBar);
        descr = findViewById(R.id.description);

        apiInterface = ServiceBuilder.buildRequest().create(ApiInterface.class);
        apiKey = "9e0a4403-ffba-486d-988d-dac5e7f1431a";
        textViewactors = findViewById(R.id.actors_textview);

        smotret_pozhe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Drawable[] drawables = smotret_pozhe.getCompoundDrawablesRelative();

                if (drawables[1] != null) {
                    if (drawables[1].equals(look_pustaya)) {
                        smotret_pozhe.setCompoundDrawablesRelativeWithIntrinsicBounds(null, look_polnaya, null, null);
                        addFilmLater(filmId);
                    } else if (drawables[1].equals(look_polnaya)) {
                        smotret_pozhe.setCompoundDrawablesRelativeWithIntrinsicBounds(null, look_pustaya, null, null);
                        deleteFilmLater(filmId);
                    }
                }
            }
        });

        textViewactors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NestedScrollView nestedScrollView = findViewById(R.id.nestedScrollView);
                nestedScrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
        all_otzivi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, Reviews.class);
                intent.putExtra("filmId", filmId);
                startActivity(intent);
            }
        });


        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, Reviews_Add.class);
                intent.putExtra("filmId", filmId);
                startActivity(intent);
            }
        });

        disposable.add(getFilmSingle(apiKey, filmId)
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
        //Актеры
        Disposable actorsAndGroupsDisposable = Single.zip(
                        getActorsSingle(apiKey, filmId),
                        getGroupsSingle(apiKey, filmId),
                        (actorsResponse, groupsResponse) -> {

                            for (Actors actor : actorsResponse) {
                                if ("ACTOR".equals(actor.getProfessionKey())) {
                                    actorList.add(actor);
                                }
                            }

                            List<Actors> groupList = new ArrayList<>();
                            for (Actors group : groupsResponse) {
                                if ("PRODUCER".equals(group.getProfessionKey()) || "DIRECTOR".equals(group.getProfessionKey())
                                        || "WRITER".equals(group.getProfessionKey())) {
                                    groupList.add(group);
                                }
                            }

                            return new Pair<>(actorList, groupList);
                        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        pair -> {
                            List<Actors> actorList = pair.first;
                            List<Actors> groupList = pair.second;
                            updateActorsAndGroupsUI(actorList, groupList);
                        },
                        error -> {
                            Log.e("API Error", error.getMessage());
                        }
                );

        adapterActors = new AdapterActors(this, actorList);


    }



    private void LoadReviews() {
                reviewsRef.whereEqualTo("filmId", filmId)
                .limit(3)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Review> reviews = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Review review = document.toObject(Review.class);
                                reviews.add(review);
                            }
                            reviewList = reviews;
                            updateReviews();
                        } else {
                            Log.d("TAG", "Error getting reviews: ", task.getException());
                        }
                    }
                });
    }

    private void updateReviews() {
            List<Review> limitedReviewList = reviewList.size() > 3
                    ? reviewList.subList(0, 3)
                    : reviewList;

            adapterReviews = new AdapterReviews(DetailActivity.this, limitedReviewList);
            otzivi_recyclerView.setLayoutManager(new GridLayoutManager(DetailActivity.this, 1, LinearLayoutManager.HORIZONTAL, false));
            otzivi_recyclerView.setHasFixedSize(true);
            otzivi_recyclerView.setAdapter(adapterReviews);


    }

    private void deleteFilmLater(int filmId) {
        if (firebaseAuth.getCurrentUser() != null) {
            Query query = filmRef.whereEqualTo("userId", userId)
                    .whereEqualTo("filmIdApi", filmId);

            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            filmRef.document(document.getId()).delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(DetailActivity.this, "Ошибка при удалении фильма из списка",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    } else {
                        Toast.makeText(DetailActivity.this, "Ошибка при проверке фильма",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void addFilmLater(int filmId) {
        if (firebaseAuth.getCurrentUser() != null) {
            Look_after film_later = new Look_after(userId, filmId, null);

            filmRef.add(film_later)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            String lookId = documentReference.getId();

                            film_later.setLookId(lookId);

                            filmRef.document(lookId).set(film_later)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(DetailActivity.this, "Ошибка обновления Сериала", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(DetailActivity.this, "Ошибка добавления Сериала", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }


    private void navigateToNewActivity(int actorId) {
        Intent intent = new Intent(this, DetailActor.class);
        intent.putExtra("actorId", actorId);
        startActivity(intent);
    }
    private Single<List<Actors>> getActorsSingle(String apiKey, int filmId) {
        return Single.fromCallable(() -> {
            WeakReference<DetailActivity> activityReference = new WeakReference<>(DetailActivity.this);
            Call<List<Actors>> getActors = apiInterface.getActors(apiKey, filmId);
            Response<List<Actors>> response = getActors.execute();

            if (response.isSuccessful()) {
                return response.body();
            } else {
                throw new Exception("Failed to get actor data");
            }
        });
    }
    private Single<List<Actors>> getGroupsSingle(String apiKey, int filmId) {
        return Single.fromCallable(() -> {
            WeakReference<DetailActivity> activityReference = new WeakReference<>(DetailActivity.this);
            Call<List<Actors>> getGroups = apiInterface.getActors(apiKey, filmId);
            Response<List<Actors>> response = getGroups.execute();

            if (response.isSuccessful()) {
                return response.body();
            } else {
                throw new Exception("Failed to get actor data");
            }
        });
    }
    private Single<Films> getFilmSingle(String ApiKey, int filmId){
        return Single.create(emitter -> {

            Call<Films> getFilm = apiInterface.getFilm(apiKey, filmId);
            getFilm.enqueue(new Callback<Films>() {
                @Override
                public void onResponse(Call<Films> call, Response<Films> response) {
                    if(response.isSuccessful()){
                        progressBar.setVisibility(View.GONE);
                        Films film = response.body();
                        emitter.onSuccess(film);
                    } else{
                        emitter.onError(new Exception("Failed to get film data"));
                    }
                }

                @Override
                public void onFailure(Call<Films> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    emitter.onError(t);
                }
            });
        });
    }
    private void updateFilmUI(Films film){
        if(film != null){
            Picasso.with(DetailActivity.this).load(film.getPosterUrl()).into(imageView);
            textView.setText(film.getNameRu());
            Year.setText(String.valueOf(film.getYear()));
            descr.setText(film.getDescription());

        }
    }
    private void updateActorsAndGroupsUI(List<Actors> actorList, List<Actors> groupList) {
        adapterActors = new AdapterActors(DetailActivity.this, actorList);
        adapterActors.setOnItemClickListener(new AdapterActors.OnItemClickListener() {
            @Override
            public void onItemClick(int actorId) {
                navigateToNewActivity(actorId);
            }
        });

        recyclerView1.setLayoutManager(new GridLayoutManager(DetailActivity.this, 2, LinearLayoutManager.HORIZONTAL, false));
        recyclerView1.setItemViewCacheSize(actorList.size());
        recyclerView1.setHasFixedSize(true);
        recyclerView1.setAdapter(adapterActors);

        AdapterSGroup adapterGroups = new AdapterSGroup(DetailActivity.this, groupList);
        recyclerView2.setLayoutManager(new LinearLayoutManager(DetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView2.setItemViewCacheSize(groupList.size());
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setAdapter(adapterGroups);
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(DetailActivity.this, glavnaya.class);
        startActivity(intent);
        super.onBackPressed();
    }


}
