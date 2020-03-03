package foodapp.com.deepak;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import foodapp.Interface.ItemClickListener;
import foodapp.ViewHolder.FoodViewHolder;

public class FoodListActivity extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference foodlist;
    TextView txtFullName;
    RecyclerView recycler_food;
    RecyclerView.LayoutManager layoutManager;
    String categoryId = "";
    FirebaseRecyclerAdapter<Food,FoodViewHolder> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        //init firebase
        database = FirebaseDatabase.getInstance();
        foodlist = database.getReference("Food");
        recycler_food = (RecyclerView) findViewById(R.id.recycler_food);
        recycler_food.setHasFixedSize(true);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, LinearLayout.VERTICAL);
        recycler_food.setLayoutManager(layoutManager);

        if (getIntent()!=null)
            categoryId=getIntent().getStringExtra("CategoryId");
        if (!categoryId.isEmpty()&&categoryId!=null)
        {
            loadListFood(categoryId);
        }

    }

    private void loadListFood(String categoryId) {
        adapter=new FirebaseRecyclerAdapter<Food, FoodViewHolder>(Food.class,R.layout.food_item,FoodViewHolder.class,foodlist.orderByChild("MenuId").equalTo(categoryId)) {
            @Override
            protected void populateViewHolder(FoodViewHolder viewHolder, Food model, int position) {
                viewHolder.food_name.setText(model.getName());
                Picasso.get().load(model.getImage()).into(viewHolder.food_image);
                final Food local=model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent food_list=new Intent(FoodListActivity.this,FoodDetailActivity.class);
                        food_list.putExtra("FoodId",adapter.getRef(position).getKey());
                        startActivity(food_list);
                        Toast.makeText(FoodListActivity.this, ""+local.getName(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        };
        recycler_food.setAdapter(adapter);
    }
}
