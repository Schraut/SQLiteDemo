package com.danshrout.sqlitedemo;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    RecipeDatabaseHelper recipeDb;
    TextView topText;
    EditText editTitle, editIngredients, editDirections, editID;
    Button saveButton,
            showAllRecipesButton,
            updateRecipesButton,
            deleteRecipeBtn,
            showUpdateRecipePage,
            showAddRecipePage,
            showDeleteRecipePage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recipeDb = new RecipeDatabaseHelper(this);

        topText = findViewById(R.id.textTopMain);

        editTitle = findViewById(R.id.title_recipe);
        editIngredients = findViewById(R.id.ingredients_recipe);
        editDirections = findViewById(R.id.directions_recipe);
        editID = findViewById(R.id.id_recipe);

        //Buttons
        saveButton = findViewById(R.id.save_recipe);
        showAllRecipesButton = findViewById(R.id.show_all_recipes_button);
        updateRecipesButton = findViewById(R.id.update_button);
        deleteRecipeBtn = findViewById(R.id.delete_button);

        showAddRecipePage = findViewById(R.id.add_recipe_page);
        showUpdateRecipePage = findViewById(R.id.show_update_page);
        showDeleteRecipePage = findViewById(R.id.show_delete_page);


        addRecipe();
        showAllRecipes();
        updateRecipes();
        deleteRecipe();

    }

    public void showUpdatePage(View view) {
        topText.setText("Update Recipe");

        editID.setVisibility(View.VISIBLE);
        showAddRecipePage.setVisibility(View.VISIBLE);
        updateRecipesButton.setVisibility(View.VISIBLE);

        saveButton.setVisibility(View.GONE);
        showAllRecipesButton.setVisibility(View.GONE);
        showDeleteRecipePage.setVisibility(View.GONE);
        showUpdateRecipePage.setVisibility(View.GONE);

    }

    public void showAddRecipePage(View view) {
        topText.setText("Add Recipe");

        editTitle.setVisibility(View.VISIBLE);
        editIngredients.setVisibility(View.VISIBLE);
        editDirections.setVisibility(View.VISIBLE);

        saveButton.setVisibility(View.VISIBLE);
        showAllRecipesButton.setVisibility(View.VISIBLE);
        showDeleteRecipePage.setVisibility(View.VISIBLE);
        showUpdateRecipePage.setVisibility(View.VISIBLE);

        editID.setVisibility(View.GONE);
        deleteRecipeBtn.setVisibility(View.GONE);
        showAddRecipePage.setVisibility(View.GONE);
        updateRecipesButton.setVisibility(View.GONE);

    }

    public void showDeleteRecipePage(View view) {
        topText.setText("Delete Recipe by ID");

        editID.setVisibility(View.VISIBLE);
        deleteRecipeBtn.setVisibility(View.VISIBLE);
        showAddRecipePage.setVisibility(View.VISIBLE);

        editTitle.setVisibility(View.GONE);
        editIngredients.setVisibility(View.GONE);
        editDirections.setVisibility(View.GONE);

        saveButton.setVisibility(View.GONE);
        showAllRecipesButton.setVisibility(View.GONE);
        showDeleteRecipePage.setVisibility(View.GONE);
        showUpdateRecipePage.setVisibility(View.GONE);
    }

    public void deleteRecipe() {
        deleteRecipeBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Integer deletedRows = recipeDb.deleteRecipe(editID.getText().toString());
                        if (deletedRows > 0)
                            Toast.makeText(MainActivity.this, "Recipe Deleted", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(MainActivity.this, "Recipe not deleted.", Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    public void updateRecipes() {
        updateRecipesButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean isUpdated = recipeDb.updateRecipes(
                                editID.getText().toString(),
                                editTitle.getText().toString(),
                                editIngredients.getText().toString(),
                                editDirections.getText().toString()
                        );

                        if (isUpdated == true)
                            Toast.makeText(MainActivity.this, "Recipe Updated", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(MainActivity.this, "Recipe not updated", Toast.LENGTH_LONG).show();

                    }
                }
        );
    }

    public void addRecipe() {
        saveButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isInserted = recipeDb.insertData(
                                editTitle.getText().toString(),
                                editIngredients.getText().toString(),
                                editDirections.getText().toString()
                        );
                        if (isInserted = true) {
                            Toast.makeText(MainActivity.this, "Data Inserted", Toast.LENGTH_LONG).show();
                            editTitle.getText().clear();
                            editIngredients.getText().clear();
                            editDirections.getText().clear();
                        } else
                            Toast.makeText(MainActivity.this, "Data not inserted", Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    public void showAllRecipes() {
        showAllRecipesButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Cursor res = recipeDb.getAllRecipes();
                        if (res.getCount() == 0) {
                            //show alert message
                            showMessage("Empty", "Add recipes");
                            return;
                        }

                        StringBuffer buffer = new StringBuffer();
                        while (res.moveToNext()) {
                            buffer.append("ID :" + res.getString(0) + "\n");
                            buffer.append("TITLE :" + res.getString(1) + "\n");
                            buffer.append("INGREDIENTS :" + res.getString(2) + "\n");
                            buffer.append("DIRECTIONS :" + res.getString(3) + "\n");
                        }

                        showMessage("Recipes", buffer.toString());
                    }
                }
        );
    }

    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }


    //TextView textToBeChanged = findViewById(R.id.textChange);


//        //I'm creating and naming my database "users"
//        try {
//            SQLiteDatabase usersDatabase = this.openOrCreateDatabase( "users", MODE_PRIVATE, null);
//            Toast.makeText(this, "User Database Created", Toast.LENGTH_LONG).show();
//            //I'm creating a table called "users". int is 3 because age will be three digits long.
//            usersDatabase.execSQL("CREATE TABLE IF NOT EXISTS users (name VARCHAR, age INT(3))");
//            //
////            usersDatabase.execSQL("INSERT INTO users (name, age) VALUES('Dan', 37)");
////            usersDatabase.execSQL("INSERT INTO users (name, age) VALUES('Ellis', 999)");
//
//            Cursor userCursor = usersDatabase.rawQuery( "SELECT * FROM users", null);
//            int nameIndex = userCursor.getColumnIndex("name");
//            int ageIndex = userCursor.getColumnIndex("age");
//            userCursor.moveToFirst();
//
//            while (userCursor != null) {
//                Log.i("name", userCursor.getString(nameIndex));
//                Log.i("age", userCursor.getString(ageIndex));
//
//                //The Cursor moves to the next row.
//                userCursor.moveToNext();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
}
