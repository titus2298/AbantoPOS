package jtabanto22.google.com.abantopos;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

  RecyclerView homeActivity_recyclerView;
    private ArrayList<RecipeModel> recipes=new ArrayList<>();
    private ArrayList<RecipeModel> myRecipes;
    RecipeRecyclerAdapter adapter;
    FirebaseFirestore db;
    private StorageReference mStorageRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        adapter=null;
        homeActivity_recyclerView =findViewById(R.id.homeActivity_recyclerView);

        db = FirebaseFirestore.getInstance();
        db.collection("Recipe").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.e("THIS IS THE DATA", document.getId() + " => " + document.getData());

                        RecipeModel recipe = document.toObject(RecipeModel.class);
                        Log.e("RECIPE THIS",  "" + recipe.getRecipeName()  + " lALAA");
                        Log.e("RECIPE THIS",  "" + recipe.getMealType() + " lALAA");
                        Log.e("RECIPE THIS",  "" + recipe.getIngredients() + " lALAA");
                        recipes.add(recipe);
                        //   recipes =task.getDocuments().get(0).toObject(questionObject.class);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Error in Retrieving Records!!",
                            Toast.LENGTH_SHORT).show();
                }
                Log.e("RECIPES!", recipes.toString());
                if(recipes.isEmpty())
                {
                    View view =findViewById(R.id.homelayout);
                    Snackbar.make(view, "Invalid Credential, Enter Different Email", Snackbar.LENGTH_LONG).show();

                }
                else {
                    adapter = new RecipeRecyclerAdapter(recipes, HomeActivity.this);
                    homeActivity_recyclerView.addItemDecoration(new DividerItemDecoration(HomeActivity.this, LinearLayoutManager.HORIZONTAL));
                    homeActivity_recyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
                    homeActivity_recyclerView.setAdapter(adapter);
                }
            }
        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.client_create_event_button, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.addRecipe )
        {
           Intent intent = new Intent(this,AddRecipeActivity.class);
           startActivity(intent);

            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        }

        return super.onOptionsItemSelected(item);


    }
}
